package mousquetaires.languages.syntax.ytree.statements;

import mousquetaires.languages.common.citation.Origin;
import mousquetaires.languages.syntax.ytree.expressions.YExpression;
import mousquetaires.languages.syntax.ytree.visitors.YtreeVisitor;

import javax.annotation.Nullable;
import java.util.Objects;


public class YBranchingStatement extends YStatement {  // TODO: inherit from YJumpStatement

    private final YExpression condition;
    private final YStatement thenBranch;
    @Nullable private final YStatement elseBranch;

    public YBranchingStatement(Origin origin,
                               YExpression condition,
                               YStatement thenBranch,
                               YStatement elseBranch) {
        this(origin, null, condition, thenBranch, elseBranch);
    }

    private YBranchingStatement(Origin origin,
                                String label,
                                YExpression condition,
                                YStatement thenBranch,
                                YStatement elseBranch) {
        super(origin, label);
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    public YExpression getCondition() {
        return condition;
    }

    public YStatement getThenBranch() {
        return thenBranch;
    }

    @Nullable
    public YStatement getElseBranch() {
        return elseBranch;
    }

    @Override
    public YBranchingStatement withLabel(String newLabel) {
        return new YBranchingStatement(origin(), newLabel, getCondition(), getThenBranch(), getElseBranch());
    }

    @Override
    public <T> T accept(YtreeVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return String.format("if (%s) %s else %s", getCondition(), getThenBranch(), getElseBranch());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof YBranchingStatement)) return false;
        YBranchingStatement that = (YBranchingStatement) o;
        return Objects.equals(getCondition(), that.getCondition()) &&
                Objects.equals(getThenBranch(), that.getThenBranch()) &&
                Objects.equals(getElseBranch(), that.getElseBranch());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCondition(), getThenBranch(), getElseBranch());
    }
}
