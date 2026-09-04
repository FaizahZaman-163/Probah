package probah.ast;

public class BooleanLiteralNode extends ExpressionNode {

    private final boolean value;

    public BooleanLiteralNode(
            boolean value,
            int line
    ) {
        super(line);
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }
}