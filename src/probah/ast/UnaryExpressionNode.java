package probah.ast;

import probah.lexer.Token;

public class UnaryExpressionNode
        extends ExpressionNode {

    private final Token operator;
    private final ExpressionNode operand;

    public UnaryExpressionNode(
            Token operator,
            ExpressionNode operand
    ) {

        super(operator.getLine());

        this.operator = operator;
        this.operand = operand;
    }

    public Token getOperator() {
        return operator;
    }

    public ExpressionNode getOperand() {
        return operand;
    }
}