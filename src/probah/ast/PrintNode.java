package probah.ast;

public class PrintNode extends StatementNode {

    private final ExpressionNode expression;

    public PrintNode(
            ExpressionNode expression,
            int line
    ) {
        super(line);
        this.expression = expression;
    }

    public ExpressionNode getExpression() {
        return expression;
    }
}