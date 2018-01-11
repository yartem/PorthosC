package mousquetaires.tests.unit.languages.parsers.cmin;

import mousquetaires.languages.ytree.YSyntaxTree;
import mousquetaires.languages.ytree.expressions.YAssignmentExpression;
import mousquetaires.languages.ytree.expressions.YEqualityExpression;
import mousquetaires.languages.ytree.statements.YBlockStatement;
import mousquetaires.languages.ytree.statements.YBranchingStatement;
import mousquetaires.languages.ytree.statements.YLinearStatement;
import org.junit.Test;


public class CminBranchingStatementTest extends CminParserTest {

    @Test
    public void test_branchingStatement() {
        YSyntaxTree expected = new YSyntaxTree(
                new YBranchingStatement(
                        new YEqualityExpression(
                                variableX,
                                constant1),
                        new YBlockStatement(
                                new YLinearStatement(
                                        new YAssignmentExpression(variableY, constant2))),
                        new YBlockStatement(
                                new YLinearStatement(
                                        new YAssignmentExpression(variableY, constant3)))));
        runParserTest(structuresDirectory + "branchingStatement.c", expected);
    }
}
