package mousquetaires.languages.syntax.ytree.expressions.accesses;

import mousquetaires.languages.syntax.ytree.expressions.YExpression;
import mousquetaires.languages.syntax.ytree.expressions.YMultiExpression;
import mousquetaires.languages.syntax.ytree.expressions.assignments.YAssignee;
import mousquetaires.languages.syntax.ytree.visitors.ytree.YtreeVisitor;


public class YIndexerExpression extends YMultiExpression implements YAssignee {

    public YIndexerExpression(YExpression baseExpression, YExpression indexExpression) {
        super(baseExpression, indexExpression);
    }

    @Override
    public <T> T accept(YtreeVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public YExpression getBaseExpression() {
        return getElements().get(0);
    }

    public YExpression getIndexExpression() {
        return getElements().get(1);
    }

    // todo: override hashCode?

    @Override
    public String toString() {
        return getBaseExpression() + "[" + getIndexExpression() + "]";
    }
}
