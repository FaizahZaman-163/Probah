package probah.ast;

import java.util.List;

public class BlockNode extends StatementNode {

    private final List<StatementNode> statements;

    public BlockNode(
            List<StatementNode> statements,
            int line
    ) {
        super(line);
        this.statements = List.copyOf(statements);
    }

    public List<StatementNode> getStatements() {
        return statements;
    }
}