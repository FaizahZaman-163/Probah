package probah.ast;

public class IdentifierNode extends ExpressionNode {

    private final String name;

    public IdentifierNode(
            String name,
            int line
    ) {
        super(line);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}