package probah.ast;

import probah.lexer.TokenType;

public class DeclarationNode extends StatementNode {

    private final TokenType declaredType;
    private final String name;
    private final ExpressionNode initializer;

    public DeclarationNode(
            TokenType declaredType,
            String name,
            ExpressionNode initializer,
            int line
    ) {
        super(line);

        this.declaredType = declaredType;
        this.name = name;
        this.initializer = initializer;
    }

    public TokenType getDeclaredType() {
        return declaredType;
    }

    public String getName() {
        return name;
    }

    public ExpressionNode getInitializer() {
        return initializer;
    }
}