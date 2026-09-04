package probah.ast;

public class WhileNode extends StatementNode {

    private final ExpressionNode condition;
    private final BlockNode body;

    public WhileNode(
            ExpressionNode condition,
            BlockNode body,
            int line
    ) {
        super(line);

        this.condition = condition;
        this.body = body;
    }

    public ExpressionNode getCondition() {
        return condition;
    }

    public BlockNode getBody() {
        return body;
    }
}