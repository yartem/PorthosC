package mousquetaires.languages.eventrepr.memory;

import mousquetaires.languages.internalrepr.YEntity;


public abstract class XMemoryLocation implements YEntity, Cloneable {

    public enum Kind {
        Registry,
        SharedMemory,
    }

    public final String name;

    public final Kind kind;

    public XMemoryLocation(String name, Kind kind) {
        this.name = name;
        this.kind = kind;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public XMemoryLocation clone() {
        return this;
    }
}