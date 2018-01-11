package mousquetaires.tests.unit.languages.parsers.cmin.statements;

import mousquetaires.languages.internalrepr.YSyntaxTree;
import mousquetaires.languages.internalrepr.expressions.YAssignmentExpression;
import mousquetaires.languages.internalrepr.expressions.YEqualityExpression;
import mousquetaires.languages.internalrepr.statements.YBranchingStatement;
import mousquetaires.languages.internalrepr.statements.YLinearStatement;
import org.junit.Test;


public class CminParseBranchingStatementTest extends CminParseStatementTest {

    @Test
    public void test_branchingStatement() {
        YSyntaxTree expected = new YSyntaxTree(
                new YBranchingStatement(
                        new YEqualityExpression(variableX, constant1),
                        new YLinearStatement(new YAssignmentExpression(variableY, constant2)),
                        new YLinearStatement(new YAssignmentExpression(variableY, constant3))));
        runParserTest(statementsDirectory + "branchingStatement.c", expected);
    }
}