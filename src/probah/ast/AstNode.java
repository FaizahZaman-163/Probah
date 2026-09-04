package probah.ast;

public abstract class AstNode {

    private final int line;

    protected AstNode(int line) {
        this.line = line;
    }

    public int getLine() {
        return line;
    }
}