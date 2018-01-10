package mousquetaires.languages.ytree.expressions;

import mousquetaires.languages.ytree.YEntity;

import java.util.Objects;


public class YUnaryExpression extends YExpression {
    public enum Operator implements YEntity {
        Not,                // !x
        IncrementPrefix,    // ++x
        IncrementPostfix,   // x++
        DecrementPrefix,    // --x
        DecrementPostfix,   // x--
        PointerDereference,  // *x
        PointerReference,   // &x
    }

    public final Operator operator;
    public final YExpression expression;

    public YUnaryExpression(YExpression expression, Operator operator) {
        this.operator = operator;
        this.expression = expression;
    }

    @Override
    public String toString() {
        switch (operator) {
            case Not:
                return "!" + expression;
            case IncrementPrefix:
                return "++" + expression;
            case IncrementPostfix:
                return expression + "++";
            case DecrementPrefix:
                return "--" + expression;
            case DecrementPostfix:
                return expression + "--";
            case PointerDereference:
                return "*" + expression;
            case PointerReference:
                return "&" + expression;
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof YUnaryExpression)) return false;
        YUnaryExpression that = (YUnaryExpression) o;
        return operator == that.operator &&
                Objects.equals(expression, that.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operator, expression);
    }
}