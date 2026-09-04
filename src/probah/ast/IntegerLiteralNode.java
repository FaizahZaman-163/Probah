package probah.ast;

public class IntegerLiteralNode extends ExpressionNode {

    private final int value;

    public IntegerLiteralNode(
            int value,
            int line
    ) {
        super(line);
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}