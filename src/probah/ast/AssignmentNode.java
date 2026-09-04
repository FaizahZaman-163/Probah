package probah.ast;

public class AssignmentNode extends StatementNode {

    private final String name;
    private final ExpressionNode value;

    public AssignmentNode(
            String name,
            ExpressionNode value,
            int line
    ) {
        super(line);

        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public ExpressionNode getValue() {
        return value;
    }
}