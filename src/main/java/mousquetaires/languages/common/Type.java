package mousquetaires.languages.common;

// TODO: add more properties of bitness (floating-point/fixed-point; signed/unsigned) and rename it to Type
public enum Type {
    bit1,
    bit16,
    int32,
    bit64,
    ;

    public int toInt() {
        switch (this) {
            case bit1:  return 1;
            case bit16: return 16;
            case int32: return 32;
            case bit64: return 64;
            default:
                throw new IllegalArgumentException(this.name());
        }
    }

    public static Type parseInt(int bitness) {
        for (Type bit : values()) {
            if (bit.toInt() == bitness) {
                return bit;
            }
        }
        throw new IllegalArgumentException("" + bitness);
    }

    public String getText() {
        return toInt() + "bit";
    }

    @Override
    public String toString() {
        return getText();
    }
}