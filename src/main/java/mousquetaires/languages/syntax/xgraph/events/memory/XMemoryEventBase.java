package mousquetaires.languages.syntax.xgraph.events.memory;

import mousquetaires.languages.syntax.xgraph.events.XEventBase;
import mousquetaires.languages.syntax.xgraph.memories.XMemoryUnit;
import mousquetaires.languages.syntax.xgraph.processes.XEventInfo;


public abstract class XMemoryEventBase extends XEventBase implements XMemoryEvent {

    private final XMemoryUnit destination;
    private final XMemoryUnit source;

    XMemoryEventBase(XEventInfo info, XMemoryUnit destination, XMemoryUnit source) {
        super(info);
        this.destination = destination;
        this.source = source;
    }

    public XMemoryUnit getDestination() {
        return destination;
    }

    public XMemoryUnit getSource() {
        return source;
    }
}