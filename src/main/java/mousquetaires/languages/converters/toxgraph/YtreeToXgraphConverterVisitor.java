package mousquetaires.languages.converters.toxgraph;

import mousquetaires.languages.ProgramLanguage;
import mousquetaires.languages.syntax.xgraph.XProgram;
import mousquetaires.languages.syntax.xgraph.XProgramInterpretationBuilder;
import mousquetaires.languages.syntax.xgraph.datamodels.DataModel;
import mousquetaires.languages.syntax.xgraph.events.XEvent;
import mousquetaires.languages.syntax.xgraph.events.computation.XComputationEvent;
import mousquetaires.languages.syntax.zformula.XZOperator;
import mousquetaires.languages.syntax.xgraph.events.computation.XZOperatorHelper;
import mousquetaires.languages.syntax.xgraph.events.memory.XMemoryEvent;
import mousquetaires.languages.syntax.xgraph.memories.*;
import mousquetaires.languages.syntax.ytree.YEntity;
import mousquetaires.languages.syntax.ytree.YSyntaxTree;
import mousquetaires.languages.syntax.ytree.definitions.YFunctionDefinition;
import mousquetaires.languages.syntax.ytree.expressions.YExpression;
import mousquetaires.languages.syntax.ytree.expressions.accesses.YIndexerExpression;
import mousquetaires.languages.syntax.ytree.expressions.accesses.YInvocationExpression;
import mousquetaires.languages.syntax.ytree.expressions.accesses.YMemberAccessExpression;
import mousquetaires.languages.syntax.ytree.expressions.assignments.YAssignmentExpression;
import mousquetaires.languages.syntax.ytree.expressions.binary.YIntegerBinaryExpression;
import mousquetaires.languages.syntax.ytree.expressions.binary.YLogicalBinaryExpression;
import mousquetaires.languages.syntax.ytree.expressions.binary.YRelativeBinaryExpression;
import mousquetaires.languages.syntax.ytree.expressions.ternary.YTernaryExpression;
import mousquetaires.languages.syntax.ytree.expressions.unary.YIntegerUnaryExpression;
import mousquetaires.languages.syntax.ytree.expressions.unary.YLogicalUnaryExpression;
import mousquetaires.languages.syntax.ytree.expressions.unary.YPointerUnaryExpression;
import mousquetaires.languages.syntax.ytree.specific.YPostludeStatement;
import mousquetaires.languages.syntax.ytree.specific.YPreludeStatement;
import mousquetaires.languages.syntax.ytree.specific.YProcessStatement;
import mousquetaires.languages.syntax.ytree.specific.YVariableAssertion;
import mousquetaires.languages.syntax.ytree.statements.*;
import mousquetaires.languages.syntax.ytree.statements.jumps.YJumpStatement;
import mousquetaires.languages.syntax.ytree.types.signatures.YMethodSignature;
import mousquetaires.languages.syntax.ytree.types.signatures.YParameter;
import mousquetaires.languages.syntax.ytree.visitors.ytree.YtreeVisitorBase;
import mousquetaires.utils.exceptions.NotImplementedException;
import mousquetaires.utils.exceptions.xgraph.XInterpretationError;


public class YtreeToXgraphConverterVisitor extends YtreeVisitorBase<XEvent> {

    private final XMemoryManager memoryManager;
    private final XProgramInterpretationBuilder program;
    private final YExpressionToXMemoryUnitConverter memoryUnitConverter;

    public YtreeToXgraphConverterVisitor(ProgramLanguage language, DataModel dataModel) {
        this.memoryManager = new XMemoryManager(language, dataModel);
        this.program = new XProgramInterpretationBuilder(memoryManager);
        this.memoryUnitConverter = new YExpressionToXMemoryUnitConverter(memoryManager, program);
    }

    public XProgram getProgram() {
        return program.build();
    }


    @Override
    public XEvent visit(YSyntaxTree node) {
        for (YEntity root : node.getRoots()) {
            root.accept(this);
        }
        return null;
    }

    @Override
    public XEvent visit(YPreludeStatement node) {
        throw new NotImplementedException();
    }

    @Override
    public XEvent visit(YPostludeStatement node) {
        throw new NotImplementedException();
    }

    @Override
    public XEvent visit(YVariableAssertion node) {
        throw new NotImplementedException();
    }

    // start of Litmus-specific visits:

    @Override
    public XEvent visit(YProcessStatement node) {
        program.startProcessDefinition(node.getProcessId());
        XEvent result = node.getBody().accept(this);
        program.finishProcessDefinition();
        return result;
    }

    @Override
    public XEvent visit(YCompoundStatement node) {
        XEvent lastEvent = null;
        for (YStatement statement : node.getStatements()) {
            lastEvent = statement.accept(this);
        }
        if (lastEvent == null) {
            lastEvent = program.currentProcess.emitNopEvent();
        }
        return lastEvent;
    }

    // end of Litmus-specific visits.


    @Override
    public XEvent visit(YMethodSignature node) {
        throw new NotImplementedException();
    }

    // =================================================================================================================


    @Override
    public XEvent visit(YIndexerExpression node) {
        throw new NotImplementedException();
    }

    @Override
    public XEvent visit(YMemberAccessExpression node) {
        throw new NotImplementedException();
    }

    @Override
    public XEvent visit(YInvocationExpression node) {
        throw new NotImplementedException();
    }

    @Override
    public XEvent visit(YFunctionDefinition node) {
        program.startProcessDefinition("P0"); // todo: function name
        XEvent result = node.getBody().accept(this);
        program.finishProcessDefinition();
        return result;
    }

    @Override
    public XEvent visit(YIntegerUnaryExpression node) {
        YIntegerUnaryExpression.Kind yOperator = node.getKind();
        YExpression yBaseExpression = node.getExpression();

        if (XOperatorHelper.isPrefixOperator(yOperator)) {
            XLocalMemoryUnit baseLocal = memoryUnitConverter.tryConvertToLocalOrNull(yBaseExpression);
            XZOperator xOperator = XOperatorHelper.isPrefixIncrement(yOperator)
                    ? XZOperator.IntegerPlus
                    : XZOperator.IntegerMinus;
            XConstant one = program.currentProcess.getConstant(1);
            XComputationEvent incremented = program.currentProcess.emitComputationEvent(xOperator, baseLocal, one);
            return program.currentProcess.emitMemoryEvent(baseLocal, incremented);
        }
        else if (XOperatorHelper.isPostfixOperator(yOperator)) {
            throw new NotImplementedException("Postfix operators are not supported for now");
        }

        throw new NotImplementedException();
    }

    @Override
    public XEvent visit(YLogicalUnaryExpression node) {
        throw new NotImplementedException();
    }

    @Override
    public XEvent visit(YPointerUnaryExpression node) {
        XMemoryUnit location = memoryUnitConverter.tryConvertOrThrow(node);
        return program.currentProcess.evaluateMemoryUnit(location);
    }

    @Override
    public XComputationEvent visit(YRelativeBinaryExpression node) {
        XLocalMemoryUnit left  = memoryUnitConverter.tryConvertToLocalOrThrow(node.getLeftExpression());
        XLocalMemoryUnit right = memoryUnitConverter.tryConvertToLocalOrThrow(node.getRightExpression());
        XZOperator operator = XZOperatorHelper.convert(node.getKind());
        return program.currentProcess.emitComputationEvent(operator, left, right);
    }

    @Override
    public XEvent visit(YLogicalBinaryExpression node) {
        throw new NotImplementedException();
    }

    @Override
    public XEvent visit(YIntegerBinaryExpression node) {
        throw new NotImplementedException();
    }

    @Override
    public XComputationEvent visit(YTernaryExpression node) {
        throw new NotImplementedException();
    }

    @Override
    public XMemoryEvent visit(YAssignmentExpression node) {
        XMemoryUnit left = memoryUnitConverter.tryConvertOrThrow(node.getAssignee());
        XMemoryUnit right = memoryUnitConverter.tryConvertOrThrow(node.getExpression());

        XLocalMemoryUnit rightLocal, leftLocal;
        if (left instanceof XSharedMemoryUnit) {
            XSharedMemoryUnit leftShared = (XSharedMemoryUnit) left;
            if (right instanceof XSharedMemoryUnit) {
                rightLocal = program.currentProcess.copyToLocalMemory((XSharedMemoryUnit) right);
            }
            else if (right instanceof XLocalMemoryUnit) {
                rightLocal = (XLocalMemoryUnit) right;
            }
            else {
                throw new IllegalStateException(getUnexpectedOperandTypeErrorMessage(right));
            }
            return program.currentProcess.emitMemoryEvent(leftShared, rightLocal);
        }
        else if (left instanceof XLocalMemoryUnit) {
            leftLocal = (XLocalMemoryUnit) left;
            if (right instanceof XSharedMemoryUnit) {
                return program.currentProcess.emitMemoryEvent(leftLocal, (XSharedMemoryUnit) right);
            }
            else if (right instanceof XLocalMemoryUnit) {
                return program.currentProcess.emitMemoryEvent(leftLocal, (XLocalMemoryUnit) right);
            }
            else {
                throw new IllegalStateException(getUnexpectedOperandTypeErrorMessage(right));
            }
        }
        else {
            throw new IllegalStateException(getUnexpectedOperandTypeErrorMessage(left));
        }
    }

    @Override
    public XEvent visit(YLinearStatement node) {
        YExpression yExpression = node.getExpression();
        if (yExpression != null) {
            XEvent xExpression = yExpression.accept(this);
            if (xExpression != null) {
                return xExpression;
            }
        }
        return program.currentProcess.emitNopEvent();
    }

    @Override
    public XEvent visit(YVariableDeclarationStatement node) {
        // TODO: case global variable
        //return program.memoryManager.newLocalMemoryUnit(node.getVariable().getName());
        throw new NotImplementedException();
    }

    @Override
    public XEvent visit(YBranchingStatement node) {
        program.currentProcess.startBranchingBlockDefinition();

        program.currentProcess.startConditionDefinition();
        node.getCondition().accept(this);
        program.currentProcess.finishConditionDefinition();

        program.currentProcess.startThenBranchDefinition();
        node.getThenBranch().accept(this);
        program.currentProcess.finishBranchDefinition();

        YStatement elseBranch = node.getElseBranch();
        program.currentProcess.startElseBranchDefinition();
        if (elseBranch != null) {
            elseBranch.accept(this);
        }
        else {
            program.currentProcess.emitNopEvent();
        }
        program.currentProcess.finishBranchDefinition();



        program.currentProcess.finishNonlinearBlockDefinition();

        return null;
    }

    @Override
    public XEvent visit(YWhileLoopStatement node) {
        program.currentProcess.startLoopBlockDefinition();

        program.currentProcess.startConditionDefinition();
        node.getCondition().accept(this);
        program.currentProcess.finishConditionDefinition();

        program.currentProcess.startThenBranchDefinition();
        node.getBody().accept(this);
        program.currentProcess.finishBranchDefinition();

        program.currentProcess.finishNonlinearBlockDefinition();
        return null;
    }

    @Override
    public XEvent visit(YJumpStatement node) {
        switch (node.getKind()) {
            case Goto:
                throw new NotImplementedException();
                //break;
            case Return:
                throw new NotImplementedException();
                //break;
            case Break:
                program.currentProcess.processLoopBreakStatement();
                break;
            case Continue:
                program.currentProcess.processLoopContinueStatement();
                break;
            default:
                throw new XInterpretationError("Unknown jump statement kind: " + node.getKind());
        }
        return null; //program.currentProcess.emitFakeEvent();
    }

    @Override
    public XEvent visit(YParameter node) {
        throw new NotImplementedException();
    }

    private static String getUnexpectedOperandTypeErrorMessage(XMemoryUnit operand) {
        return "Unexpected type of operand: " + operand + ", type of " + operand.getClass().getSimpleName();
    }
}
