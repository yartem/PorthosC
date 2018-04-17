package mousquetaires.languages.syntax.ytree.statements;

import mousquetaires.languages.common.citation.CodeLocation;
import mousquetaires.languages.syntax.ytree.expressions.YExpression;
import mousquetaires.languages.syntax.ytree.visitors.ytree.YtreeVisitor;

import javax.annotation.Nullable;
import java.util.Objects;


public class YLinearStatement extends YStatement {

    private final YExpression expression;

    public YLinearStatement(CodeLocation location, YExpression expression) {
        this(location, newLabel(), expression);
    }

    private YLinearStatement(CodeLocation location, String label, YExpression expression) {
        super(location, label);
        this.expression = expression;
    }

    @Nullable
    public YExpression getExpression() {
        return expression;
    }

    public static YLinearStatement createEmptyStatement() {
        return new YLinearStatement(CodeLocation.empty, null);
    }

    @Override
    public YLinearStatement withLabel(String newLabel) {
        return new YLinearStatement(codeLocation(), newLabel, getExpression());
    }

    @Override
    public <T> T accept(YtreeVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return getExpression() + ";";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof YLinearStatement)) return false;
        YLinearStatement that = (YLinearStatement) o;
        return Objects.equals(getExpression(), that.getExpression());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getExpression());
    }
}
