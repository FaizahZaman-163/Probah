package probah.ast;

import probah.lexer.Token;

public class BinaryExpressionNode
        extends ExpressionNode {

    private final ExpressionNode left;
    private final Token operator;
    private final ExpressionNode right;

    public BinaryExpressionNode(
            ExpressionNode left,
            Token operator,
            ExpressionNode right
    ) {

        super(operator.getLine());

        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    public ExpressionNode getLeft() {
        return left;
    }

    public Token getOperator() {
        return operator;
    }

    public ExpressionNode getRight() {
        return right;
    }
}