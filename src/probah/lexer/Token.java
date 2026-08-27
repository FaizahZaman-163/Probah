package probah.lexer;

public class Token {

    private final TokenType type;
    private final String lexeme;
    private final int line;
    private final int column;

    public Token(
            TokenType type,
            String lexeme,
            int line,
            int column
    ) {
        this.type = type;
        this.lexeme = lexeme;
        this.line = line;
        this.column = column;
    }

    public TokenType getType() {
        return type;
    }

    public String getLexeme() {
        return lexeme;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public String toString() {

        if (type == TokenType.EOF) {
            return String.format(
                    "<EOF, line=%d, col=%d>",
                    line,
                    column
            );
        }

        return String.format(
                "<%s, %s, line=%d, col=%d>",
                type,
                lexeme,
                line,
                column
        );
    }
}