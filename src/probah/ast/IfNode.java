package probah.ast;

public class IfNode extends StatementNode {

    private final ExpressionNode condition;
    private final BlockNode thenBranch;
    private final StatementNode elseBranch;

    public IfNode(
            ExpressionNode condition,
            BlockNode thenBranch,
            StatementNode elseBranch,
            int line
    ) {
        super(line);

        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    public ExpressionNode getCondition() {
        return condition;
    }

    public BlockNode getThenBranch() {
        return thenBranch;
    }

    public StatementNode getElseBranch() {
        return elseBranch;
    }
}