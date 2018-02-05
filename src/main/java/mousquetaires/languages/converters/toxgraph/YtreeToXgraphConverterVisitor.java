package mousquetaires.languages.converters.toxgraph;

import mousquetaires.languages.ProgramLanguage;
import mousquetaires.languages.converters.toxgraph.helpers.TypeCastHelper;
import mousquetaires.languages.converters.toxgraph.helpers.XBinaryOperatorConverter;
import mousquetaires.languages.converters.toxgraph.helpers.XUnaryOperatorHelper;
import mousquetaires.languages.syntax.xgraph.XEntity;
import mousquetaires.languages.syntax.xgraph.XProgram;
import mousquetaires.languages.syntax.xgraph.XProgramBuilder;
import mousquetaires.languages.syntax.xgraph.datamodels.DataModel;
import mousquetaires.languages.syntax.xgraph.events.XEvent;
import mousquetaires.languages.syntax.xgraph.events.computation.XComputationEvent;
import mousquetaires.languages.syntax.xgraph.events.computation.XOperator;
import mousquetaires.languages.syntax.xgraph.events.memory.XLocalMemoryEvent;
import mousquetaires.languages.syntax.xgraph.memories.*;
import mousquetaires.languages.syntax.xgraph.types.XMockType;
import mousquetaires.languages.syntax.xgraph.types.XType;
import mousquetaires.languages.syntax.ytree.YEntity;
import mousquetaires.languages.syntax.ytree.YSyntaxTree;
import mousquetaires.languages.syntax.ytree.definitions.YFunctionDefinition;
import mousquetaires.languages.syntax.ytree.expressions.YConstant;
import mousquetaires.languages.syntax.ytree.expressions.YExpression;
import mousquetaires.languages.syntax.ytree.expressions.YTernaryExpression;
import mousquetaires.languages.syntax.ytree.expressions.YVariableRef;
import mousquetaires.languages.syntax.ytree.expressions.accesses.YIndexerExpression;
import mousquetaires.languages.syntax.ytree.expressions.accesses.YInvocationExpression;
import mousquetaires.languages.syntax.ytree.expressions.accesses.YMemberAccessExpression;
import mousquetaires.languages.syntax.ytree.expressions.assignments.YAssignmentExpression;
import mousquetaires.languages.syntax.ytree.expressions.binary.YBinaryExpression;
import mousquetaires.languages.syntax.ytree.expressions.unary.YUnaryExpression;
import mousquetaires.languages.syntax.ytree.specific.YPostludeStatement;
import mousquetaires.languages.syntax.ytree.specific.YPreludeStatement;
import mousquetaires.languages.syntax.ytree.specific.YProcessStatement;
import mousquetaires.languages.syntax.ytree.specific.YVariableAssertion;
import mousquetaires.languages.syntax.ytree.statements.*;
import mousquetaires.languages.syntax.ytree.statements.jumps.YJumpStatement;
import mousquetaires.languages.syntax.ytree.types.YType;
import mousquetaires.languages.syntax.ytree.types.signatures.YMethodSignature;
import mousquetaires.languages.syntax.ytree.types.signatures.YParameter;
import mousquetaires.languages.visitors.ytree.YtreeVisitorBase;
import mousquetaires.utils.exceptions.NotImplementedException;
import mousquetaires.utils.exceptions.xgraph.XCompilationError;
import mousquetaires.utils.exceptions.xgraph.XCompilatorUsageError;
import mousquetaires.utils.exceptions.xgraph.XInvalidTypeError;


// TODO: move this visitor to converter (replace conv. with visitor)
class YtreeToXgraphConverterVisitor extends YtreeVisitorBase<XEntity> {

    // TODO: non-null checks everywhere!

    private final XProgramBuilder program;

    YtreeToXgraphConverterVisitor(ProgramLanguage language, DataModel dataModel) {
        //this.interpreter = interpreter;
        this.program = new XProgramBuilder(new XMemoryManager(language, dataModel));
    }


    public XProgram getProgram() {
        return program.build();
    }

    @Override
    public XEntity visit(YSyntaxTree node) {
        for (YEntity root : node.getRoots()) {
            visit(root);
        }
        return null;
    }

    @Override
    public XEntity visit(YPreludeStatement node) {
        throw new NotImplementedException();
    }

    @Override
    public XEntity visit(YPostludeStatement node) {
        throw new NotImplementedException();
    }

    @Override
    public XEntity visit(YVariableAssertion node) {
        throw new NotImplementedException();
    }

    // start of Litmus-specific visits:

    @Override
    public XEvent visit(YProcessStatement node) {
        program.startProcessDefinition(node.getProcessId());
        return visit(node.getBody());
    }

    @Override
    public XEvent visit(YCompoundStatement node) {
        for (YStatement statement : node.getStatements()) {
            visit(statement);
        }
        return null;
    }

    @Override
    public XEvent visit(YStatement node) {
        XEntity result = node.accept(this);
        if (result == null) {
            return null;
        }
        if (result instanceof XEvent) {
            return (XEvent) result;
        }
        throw new XInvalidTypeError(result, XEvent.class);
    }

    // end of Litmus-specific visits.

    @Override
    public XConstant visit(YConstant node) {
        // todo: convert type 'node.getType()'
        return program.memoryManager.getConstant(node.getValue());
    }

    @Override
    public XEntity visit(YIndexerExpression node) {
        throw new NotImplementedException();
    }

    @Override
    public XEntity visit(YMemberAccessExpression node) {
        throw new NotImplementedException();
    }

    @Override
    public XEntity visit(YInvocationExpression node) {
        throw new NotImplementedException();
    }

    @Override
    public XEntity visit(YFunctionDefinition node) {
        program.startProcessDefinition("function_name"); // todo: function name
        XEntity result = visit(node.getBody());
        program.finishProcessDefinition();
        return result;
    }

    @Override
    public XLocalMemoryUnit visit(YUnaryExpression node) {
        XEntity base = visit(node.getExpression());
        XLocalMemoryUnit baseLocal = TypeCastHelper.castToLocalMemoryUnitOrThrow(base);
        YUnaryExpression.Kind yOperator = node.getKind();
        if (XUnaryOperatorHelper.isPrefixIntegerOperator(yOperator)) {
            XOperator xOperator = XUnaryOperatorHelper.isPrefixIncrement(yOperator)
                    ? XOperator.IntegerPlus
                    : XOperator.IntegerMinus;
            XConstant one = program.memoryManager.getConstant(1);
            XComputationEvent incremented = program.currentProcess.emitComputationEvent(xOperator, baseLocal, one);
            XLocalMemoryEvent incrementEvent = program.currentProcess.emitMemoryEvent(baseLocal, incremented);
            return incrementEvent.getDestination();
        } else if (XUnaryOperatorHelper.isPostfixIntegerOperator(yOperator)) {
            throw new NotImplementedException("Postfix operators are not supported for now");
        }

        throw new NotImplementedException("Unsupported unary operator: " + yOperator); //todo
    }

    @Override
    public XComputationEvent visit(YBinaryExpression node) {
        XEntity left = visit(node.getLeftExpression());
        XLocalMemoryUnit leftLocal = TypeCastHelper.castToLocalMemoryUnitOrThrow(left);

        XEntity right = visit(node.getRightExpression());
        XLocalMemoryUnit rightLocal = TypeCastHelper.castToLocalMemoryUnitOrThrow(right);

        XOperator operator = XBinaryOperatorConverter.convert(node.getKind());
        return program.currentProcess.emitComputationEvent(operator, leftLocal, rightLocal);
    }

    @Override
    public XComputationEvent visit(YTernaryExpression node) {
        throw new NotImplementedException();
    }

    @Override
    public XLocalMemoryEvent visit(YAssignmentExpression node) {
        XLocalMemoryUnit assignee = TypeCastHelper.castToLocalMemoryUnitOrThrow(visit(node.getAssignee()));
        XLocalMemoryUnit expression = TypeCastHelper.castToLocalMemoryUnitOrThrow(visit(node.getExpression()));
        return program.currentProcess.emitMemoryEvent(assignee, expression);
    }

    @Override
    public XEvent visit(YLinearStatement node) {
        YExpression yExpression = node.getExpression();
        if (yExpression != null) {
            XEntity xExpression = visit(yExpression);
            if (xExpression != null) {
                return processExpression(xExpression);
            }
        }
        return program.currentProcess.emitComputationEvent();
    }

    @Override
    public XMemoryUnit visit(YVariableDeclarationStatement node) {
        // TODO: case global variable
        return program.memoryManager.newLocalMemoryUnit(node.getVariable().getName());
    }

    @Override
    public XType visit(YType node) {
        //todo: parse type
        return new XMockType();
    }

    @Override
    public XMemoryUnit visit(YVariableRef node) {
        // TODO: switch (node.getType() )
        return program.memoryManager.newLocalMemoryUnit(node.getName());
    }

    @Override
    public XEntity visit(YBranchingStatement node) {
        XEntity condition = visit(node.getCondition());
        XComputationEvent conditionEvent = evaluate(condition);

        program.currentProcess.startBranchingDefinition(conditionEvent);
        program.currentProcess.startTrueBranch();
        visit(node.getThenBranch());
        program.currentProcess.finishTrueBranch();
        YStatement elseBranch = node.getElseBranch();
        if (elseBranch != null) {
            program.currentProcess.startFalseBranch();
            visit(elseBranch);
            program.currentProcess.finishFalseBranch();
        }
        program.currentProcess.finishBranchingDefinition();
        return null;
    }

    @Override
    public XEntity visit(YWhileLoopStatement node) {
        XEntity condition = visit(node.getCondition());
        XComputationEvent conditionEvent = evaluate(condition);

        program.currentProcess.startLoopDefinition(conditionEvent);
        program.currentProcess.startLoopBodyDefinition();
        visit(node.getBody());
        program.currentProcess.finishLoopBodyDefinition();
        program.currentProcess.finishLoopDefinition();
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
                throw new XCompilationError("Unknown jump statement kind: " + node.getKind());
        }
        return null; //program.currentProcess.emitFakeEvent();
    }

    @Override
    public XEntity visit(YMethodSignature node) {
        throw new NotImplementedException();
    }

    @Override
    public XEntity visit(YParameter node) {
        throw new NotImplementedException();
    }

    private XComputationEvent evaluate(XEntity expression) {
        XLocalMemoryUnit conditionLocal = TypeCastHelper.castToLocalMemoryUnitOrThrow(expression);
        return conditionLocal instanceof XComputationEvent
                ? (XComputationEvent) conditionLocal
                :  program.currentProcess.emitComputationEvent(conditionLocal);
    }

    private XEvent processExpression(XEntity expression) {
        if (expression instanceof XRegister) {
            return program.currentProcess.emitComputationEvent((XRegister) expression);
        }
        if (expression instanceof XEvent) {
            return (XEvent) expression;
        }
        throw new XCompilatorUsageError("Unexpected expression type: " + expression.getClass().getSimpleName());
    }
}
