package probah.ast;

import java.util.List;

public class ProgramNode extends AstNode {

    private final List<StatementNode> statements;

    public ProgramNode(List<StatementNode> statements) {

        super(
            statements.isEmpty()
                ? 1
                : statements.get(0).getLine()
        );

        this.statements = List.copyOf(statements);
    }

    public List<StatementNode> getStatements() {
        return statements;
    }
}