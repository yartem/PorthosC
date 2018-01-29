package mousquetaires.languages.syntax.ytree.expressions.unary;

import mousquetaires.languages.syntax.ytree.YEntity;
import mousquetaires.languages.syntax.ytree.expressions.YExpression;
import mousquetaires.languages.visitors.YtreeVisitor;


public class YLogicalUnaryExpression extends YUnaryPrefixExpression {
    public enum Kind implements YUnaryExpression.Kind {
        Negation,
        ;

        @Override
        public String toString() {
            switch (this) {
                case Negation: return "!";
                default: throw new IllegalArgumentException(this.name());
            }
        }

        @Override
        public YLogicalUnaryExpression createExpression(YExpression baseExpression) {
            return new YLogicalUnaryExpression(this, baseExpression);
        }
    }

    private YLogicalUnaryExpression(YLogicalUnaryExpression.Kind kind, YExpression expression) {
        super(kind, expression);
    }

    @Override
    public YLogicalUnaryExpression.Kind getKind() {
        return (YLogicalUnaryExpression.Kind) super.getKind();
    }

    @Override
    public <T> T accept(YtreeVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public YEntity copy() {
        return new YLogicalUnaryExpression(getKind(), getExpression());
    }

    @Override
    public String toString() {
        return "" + getKind() + getExpression();
    }

}