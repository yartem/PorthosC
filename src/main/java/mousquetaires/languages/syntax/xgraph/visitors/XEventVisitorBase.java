package mousquetaires.languages.syntax.xgraph.visitors;

import mousquetaires.languages.syntax.xgraph.events.computation.XBinaryComputationEvent;
import mousquetaires.languages.syntax.xgraph.events.computation.XNullaryComputationEvent;
import mousquetaires.languages.syntax.xgraph.events.computation.XUnaryComputationEvent;
import mousquetaires.languages.syntax.xgraph.events.fake.XEntryEvent;
import mousquetaires.languages.syntax.xgraph.events.fake.XExitEvent;
import mousquetaires.languages.syntax.xgraph.events.fake.XJumpEvent;
import mousquetaires.languages.syntax.xgraph.events.fake.XNopEvent;
import mousquetaires.languages.syntax.xgraph.events.memory.XLoadMemoryEvent;
import mousquetaires.languages.syntax.xgraph.events.memory.XRegisterMemoryEvent;
import mousquetaires.languages.syntax.xgraph.events.memory.XStoreMemoryEvent;
import mousquetaires.utils.exceptions.encoding.XVisitorIllegalStateException;


public abstract class XEventVisitorBase<T> implements XEventVisitor<T> {

    @Override
    public T visit(XEntryEvent event) {
        throw new XVisitorIllegalStateException();
    }

    @Override
    public T visit(XExitEvent event) {
        throw new XVisitorIllegalStateException();
    }

    @Override
    public T visit(XNullaryComputationEvent event) {
        throw new XVisitorIllegalStateException();
    }

    @Override
    public T visit(XUnaryComputationEvent event) {
        throw new XVisitorIllegalStateException();
    }

    @Override
    public T visit(XBinaryComputationEvent event) {
        throw new XVisitorIllegalStateException();
    }

    @Override
    public T visit(XRegisterMemoryEvent event) {
        throw new XVisitorIllegalStateException();
    }

    @Override
    public T visit(XStoreMemoryEvent event) {
        throw new XVisitorIllegalStateException();
    }

    @Override
    public T visit(XLoadMemoryEvent event) {
        throw new XVisitorIllegalStateException();
    }

    @Override
    public T visit(XJumpEvent event) {
        throw new XVisitorIllegalStateException();
    }

    @Override
    public T visit(XNopEvent event) {
        throw new XVisitorIllegalStateException();
    }

    //@Override
    //public T visit(XMethodCallEvent event) {
    //    throw new XVisitorIllegalStateException();
    //}
}
