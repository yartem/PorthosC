package mousquetaires.languages.ytree.expressions;

import java.util.Objects;


public class YTernaryExpression extends YExpression {
    public final YExpression condition;
    public final YExpression trueExpression;
    public final YExpression falseExpression;

    public YTernaryExpression(YExpression condition, YExpression trueExpression, YExpression falseExpression) {
        this.condition = condition;
        this.trueExpression = trueExpression;
        this.falseExpression = falseExpression;
    }

    @Override
    public String toString() {
        final StringBuffer builder = new StringBuffer();
        builder.append(condition)     .append(" ? ");
        builder.append(trueExpression).append(" : ");
        builder.append(falseExpression);
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof YTernaryExpression)) return false;
        YTernaryExpression that = (YTernaryExpression) o;
        return Objects.equals(condition, that.condition) &&
                Objects.equals(trueExpression, that.trueExpression) &&
                Objects.equals(falseExpression, that.falseExpression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(condition, trueExpression, falseExpression);
    }
}
