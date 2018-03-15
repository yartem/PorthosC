package mousquetaires.languages.common.graph.traverse;

import com.google.common.collect.ImmutableSet;
import mousquetaires.languages.common.graph.FlowGraph;
import mousquetaires.languages.common.graph.Node;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;


public abstract class FlowGraphUnrollingTraverser<T extends Node, G extends FlowGraph<T>> {

    private final int unrollingBound;
    private final FlowGraph<T> graph;
    private final BiFunction<T, Integer, T> createNodeRef;

    private final FlowGraphUnrollingActor<T, G> unrollingActor;
    private final CompositeFlowGraphTraverseActor<T> actors;

    private HashMap<T, Integer> nodeCounterMap;
    private Stack<T> enteredNodesStack;
    private Set<T> visitedRefs;

    // TODO: check this heap pollution!
    protected FlowGraphUnrollingTraverser(FlowGraph<T> graph,
                                          BiFunction<T, Integer, T> createNodeRef,
                                          int unrollingBound,
                                          FlowGraphUnrollingActor<T, G> unrollingActor,
                                          FlowGraphTraverseActor<T>... actors) {
        this.graph = graph;
        this.createNodeRef = createNodeRef;
        this.unrollingBound = unrollingBound;

        this.unrollingActor = unrollingActor;
        HashSet<FlowGraphTraverseActor<T>> allActors = new HashSet<>();
        allActors.add(unrollingActor);
        allActors.addAll(Arrays.asList(actors));
        this.actors = new CompositeFlowGraphTraverseActor<>(allActors);

        this.nodeCounterMap = new HashMap<>();
        this.enteredNodesStack = new Stack<>();
        this.visitedRefs = new HashSet<>();
    }

    public ImmutableSet<FlowGraphTraverseActor<T>> getActors() {
        return actors.getActors();
    }

    public G getUnrolledGraph() {
        return unrollingActor.build();
    }

    public void doUnroll() {
        actors.onStart();
        unrollRecursively(graph.source(), 0);
        actors.onFinish();
    }

    private void unrollRecursively(T node, int counter) {
        if (node.equals(graph.sink())) { return; }

        // TODO: Probably algorithm breaks if the DFS firstly discovers node with not largest path ?

        T nodeRef = getParentRef(node);
        enteredNodesStack.push(node);
        visitedRefs.add(nodeRef);

        int childCounter = counter + 1;

        if (graph.hasChild(node)) {
            unrollChildRecursively(node, nodeRef, childCounter, graph::child, actors::onVisitEdge);
        }
        if (graph.hasAlternativeChild(node)) {
            unrollChildRecursively(node, nodeRef, childCounter, graph::alternativeChild, actors::onVisitAltEdge);
        }

        decrementNodeIndex(node);
        T popped =  enteredNodesStack.pop();
        assert popped == node : popped + " must be " + node;
    }

    private void unrollChildRecursively(T node,
                                        T nodeRef,
                                        int childrensCounter,
                                        Function<T, T> getChild,
                                        BiConsumer<T, T> onVisitAction) {
        T child = getChild.apply(node);
        boolean childIsSink = (child == graph.sink());
        boolean boundAchieved = (childrensCounter > unrollingBound);

        T childRef = childIsSink ? child : getOrCreateChildRef(child);
        boolean childRefIsVisited = visitedRefs.contains(childRef);

        onVisitAction.accept(nodeRef, childRef);

        if (childIsSink || boundAchieved) {
            actors.onBoundAchieved(nodeRef, graph.sink());
        }
        else if (!childRefIsVisited) {
            unrollRecursively(child, childrensCounter);
        }
    }

    private T getParentRef(T node) {
        return getNodeRef(node, lastNodeIndex(node));
    }

    private T getOrCreateChildRef(T node) {
        int index = inLoop(node)
                ? incrementNodeIndex(node)
                : lastNodeIndex(node);
        return getNodeRef(node, index);
    }

    private int incrementNodeIndex(T node) {
        int index = lastNodeIndex(node) + 1;
        nodeCounterMap.put(node, index);
        return index;
    }

    private void decrementNodeIndex(T node) {
        int currentIndex = lastNodeIndex(node);
        if (currentIndex > 1) {
            nodeCounterMap.put(node, currentIndex - 1);
        }
    }

    private int lastNodeIndex(T node) {
        if (!nodeCounterMap.containsKey(node)) {
            nodeCounterMap.put(node, 1);
        }
        return nodeCounterMap.get(node);
    }

    private boolean inLoop(T node) {
        return enteredNodesStack.contains(node);
    }

    private T getNodeRef(T node, int index) {
        if (node == graph.source() || node == graph.sink()) {
            return node;
        }
        return createNodeRef.apply(node, index);
    }
}