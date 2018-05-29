package mousquetaires.languages.syntax.xgraph.events;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import mousquetaires.languages.common.graph.FlowGraphNode;
import mousquetaires.languages.syntax.xgraph.XEntity;
import mousquetaires.languages.syntax.xgraph.XProcessLocalElement;
import mousquetaires.languages.syntax.xgraph.process.XProcessId;
import mousquetaires.languages.syntax.xgraph.visitors.XEventVisitor;


public interface XEvent extends FlowGraphNode, XProcessLocalElement, XEntity {

    <T> T accept(XEventVisitor<T> visitor);

    XEventInfo getInfo();

    XEvent asNodeRef(int refId);

    //TODO: old-code method, to be replaced
    default BoolExpr executes(Context ctx) {
        return ctx.mkBoolConst(repr());
    }

    default String repr() {
        return String.format("E%s", getInfo().getEventId());
    }

    default boolean isInit() {
        return getProcessId()==XProcessId.PreludeProcessId;
    }
}
