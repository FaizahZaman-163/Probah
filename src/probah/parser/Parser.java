package probah.parser;

import probah.ast.*;
import probah.lexer.Token;
import probah.lexer.TokenType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Parser {

    private final List<Token> tokens;
    private final List<String> errors = new ArrayList<>();

    private int position = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }



    public ProgramNode parse() {

        List<StatementNode> statements = new ArrayList<>();

        while (!isAtEnd()) {

            int before = position;

            StatementNode statement = parseStatementSafely();

            if (statement != null) {
                statements.add(statement);
            }

           
            if (position == before && !isAtEnd()) {
                advance();
            }
        }

        return new ProgramNode(statements);
    }

    public List<String> getErrors() {
        return Collections.unmodifiableList(errors);
    }


    private StatementNode parseStatementSafely() {

        int startLine = current().getLine();

        try {
            return statement();

        } catch (ParseException error) {

            errors.add(error.getMessage());

            synchronize(startLine);

            return null;
        }
    }

    private StatementNode statement() {

        if (check(TokenType.DECLARE)) {
            return declarationStatement();
        }

        if (check(TokenType.IDENTIFIER)) {
            return assignmentStatement();
        }

        if (check(TokenType.PRINT)) {
            return printStatement();
        }

        if (check(TokenType.IF)) {
            return ifStatement();
        }

        if (check(TokenType.WHILE)) {
            return whileStatement();
        }

        throw error(
                current(),
                "Expected a statement."
        );
    }

  

    private StatementNode declarationStatement() {

        Token declareToken = consume(
                TokenType.DECLARE,
                "Expected 'ধরি'."
        );

        Token typeToken;

        if (match(
                TokenType.INTEGER_TYPE,
                TokenType.BOOLEAN_TYPE
        )) {

            typeToken = previous();

        } else {

            throw error(
                    current(),
                    "Expected a data type ('পূর্ণ' or 'যুক্তি') after 'ধরি'."
            );
        }

        Token name = consume(
                TokenType.IDENTIFIER,
                "Expected an identifier after the data type."
        );

        consume(
                TokenType.ASSIGN,
                "Expected ':=' after variable name."
        );

        ExpressionNode initializer = expression();

        consumeTerminator(
                "Expected ';' after declaration."
        );

        return new DeclarationNode(
                typeToken.getType(),
                name.getLexeme(),
                initializer,
                declareToken.getLine()
        );
    }

  

    private StatementNode assignmentStatement() {

        Token name = consume(
                TokenType.IDENTIFIER,
                "Expected an identifier."
        );

        consume(
                TokenType.ASSIGN,
                "Expected ':=' after identifier in assignment."
        );

        ExpressionNode value = expression();

        consumeTerminator(
                "Expected ';' after assignment."
        );

        return new AssignmentNode(
                name.getLexeme(),
                value,
                name.getLine()
        );
    }

    

    private StatementNode printStatement() {

        Token printToken = consume(
                TokenType.PRINT,
                "Expected 'দেখাও'."
        );

        ExpressionNode value = expression();

        consumeTerminator(
                "Expected ';' after print statement."
        );

        return new PrintNode(
                value,
                printToken.getLine()
        );
    }

  
    private StatementNode ifStatement() {

        Token ifToken = consume(
                TokenType.IF,
                "Expected 'যদি'."
        );

        consume(
                TokenType.LEFT_PAREN,
                "Expected '(' after 'যদি'."
        );

        ExpressionNode condition = expression();

        consume(
                TokenType.RIGHT_PAREN,
                "Expected ')' after IF condition."
        );

        BlockNode thenBranch = block();

        StatementNode elseBranch = null;

      

        if (match(TokenType.ELSE)) {

         
            if (check(TokenType.IF)) {

                elseBranch = ifStatement();

            } else {

                elseBranch = block();
            }
        }

        return new IfNode(
                condition,
                thenBranch,
                elseBranch,
                ifToken.getLine()
        );
    }

   

    private StatementNode whileStatement() {

        Token whileToken = consume(
                TokenType.WHILE,
                "Expected 'যতক্ষণ'."
        );

        consume(
                TokenType.LEFT_PAREN,
                "Expected '(' after 'যতক্ষণ'."
        );

        ExpressionNode condition = expression();

        consume(
                TokenType.RIGHT_PAREN,
                "Expected ')' after WHILE condition."
        );

        BlockNode body = block();

        return new WhileNode(
                condition,
                body,
                whileToken.getLine()
        );
    }

    

    private BlockNode block() {

        Token openBrace = consume(
                TokenType.LEFT_BRACE,
                "Expected '{' to start block."
        );

        List<StatementNode> statements = new ArrayList<>();

        while (
                !check(TokenType.RIGHT_BRACE)
                && !isAtEnd()
        ) {

            int before = position;

            StatementNode statement =
                    parseStatementSafely();

            if (statement != null) {
                statements.add(statement);
            }

            
            if (position == before && !isAtEnd()) {
                advance();
            }
        }

        if (check(TokenType.RIGHT_BRACE)) {

            advance();

        } else {

            errors.add(
                    formatError(
                            current(),
                            "Expected '}' to close block."
                    )
            );
        }

        return new BlockNode(
                statements,
                openBrace.getLine()
        );
    }


    private ExpressionNode expression() {
        return orExpression();
    }

   

    private ExpressionNode orExpression() {

        ExpressionNode left =
                andExpression();

        while (match(TokenType.OR)) {

            Token operator = previous();

            ExpressionNode right =
                    andExpression();

            left = new BinaryExpressionNode(
                    left,
                    operator,
                    right
            );
        }

        return left;
    }

    

    private ExpressionNode andExpression() {

        ExpressionNode left =
                equalityExpression();

        while (match(TokenType.AND)) {

            Token operator = previous();

            ExpressionNode right =
                    equalityExpression();

            left = new BinaryExpressionNode(
                    left,
                    operator,
                    right
            );
        }

        return left;
    }

  

    private ExpressionNode equalityExpression() {

        ExpressionNode left =
                comparisonExpression();

        while (
                match(
                        TokenType.EQUAL_EQUAL,
                        TokenType.NOT_EQUAL
                )
        ) {

            Token operator = previous();

            ExpressionNode right =
                    comparisonExpression();

            left = new BinaryExpressionNode(
                    left,
                    operator,
                    right
            );
        }

        return left;
    }

  

    private ExpressionNode comparisonExpression() {

        ExpressionNode left =
                additionExpression();

        if (
                match(
                        TokenType.LESS,
                        TokenType.LESS_EQUAL,
                        TokenType.GREATER,
                        TokenType.GREATER_EQUAL
                )
        ) {

            Token operator = previous();

            ExpressionNode right =
                    additionExpression();

            left = new BinaryExpressionNode(
                    left,
                    operator,
                    right
            );
        }

        return left;
    }

   

    private ExpressionNode additionExpression() {

        ExpressionNode left =
                multiplicationExpression();

        while (
                match(
                        TokenType.PLUS,
                        TokenType.MINUS
                )
        ) {

            Token operator = previous();

            ExpressionNode right =
                    multiplicationExpression();

            left = new BinaryExpressionNode(
                    left,
                    operator,
                    right
            );
        }

        return left;
    }

   

    private ExpressionNode multiplicationExpression() {

        ExpressionNode left =
                unaryExpression();

        while (
                match(
                        TokenType.MULTIPLY,
                        TokenType.DIVIDE
                )
        ) {

            Token operator = previous();

            ExpressionNode right =
                    unaryExpression();

            left = new BinaryExpressionNode(
                    left,
                    operator,
                    right
            );
        }

        return left;
    }

   

    private ExpressionNode unaryExpression() {

        if (
                match(
                        TokenType.NOT,
                        TokenType.MINUS
                )
        ) {

            Token operator = previous();

            ExpressionNode operand =
                    unaryExpression();

            return new UnaryExpressionNode(
                    operator,
                    operand
            );
        }

        return primaryExpression();
    }

   
    private ExpressionNode primaryExpression() {

       
        if (match(TokenType.INTEGER_LITERAL)) {

            Token token = previous();

            try {

                return new IntegerLiteralNode(
                        Integer.parseInt(token.getLexeme()),
                        token.getLine()
                );

            } catch (NumberFormatException ex) {

                throw error(
                        token,
                        "Integer literal is outside the supported Java int range."
                );
            }
        }

        
        if (match(TokenType.TRUE)) {

            return new BooleanLiteralNode(
                    true,
                    previous().getLine()
            );
        }

        
        if (match(TokenType.FALSE)) {

            return new BooleanLiteralNode(
                    false,
                    previous().getLine()
            );
        }

        
        if (match(TokenType.IDENTIFIER)) {

            Token token = previous();

            return new IdentifierNode(
                    token.getLexeme(),
                    token.getLine()
            );
        }

        
        if (match(TokenType.LEFT_PAREN)) {

            ExpressionNode inside =
                    expression();

            consume(
                    TokenType.RIGHT_PAREN,
                    "Expected ')' after expression."
            );

            return inside;
        }

        throw error(
                current(),
                "Expected an expression."
        );
    }


    private void synchronize(int statementStartLine) {

        while (!isAtEnd()) {

           
            if (check(TokenType.SEMICOLON)) {

                advance();
                return;
            }

          
            if (check(TokenType.RIGHT_BRACE)) {
                return;
            }

           
            if (
                    current().getLine()
                    > statementStartLine
            ) {

                return;
            }

            advance();
        }
    }

    

    private void consumeTerminator(String message) {

        if (match(TokenType.SEMICOLON)) {
            return;
        }

        
        if (
                position > 0
                && (
                    isAtEnd()
                    || current().getLine()
                       > previous().getLine()
                )
        ) {

            Token previousToken = previous();

            throw new ParseException(
                    String.format(
                            "Syntax Error [Line %d, Column %d]: %s",
                            previousToken.getLine(),
                            previousToken.getColumn(),
                            message
                    )
            );
        }

        throw error(
                current(),
                message
        );
    }


    private Token consume(
            TokenType type,
            String message
    ) {

        if (check(type)) {
            return advance();
        }

        throw error(
                current(),
                message
        );
    }

    private boolean match(TokenType... types) {

        for (TokenType type : types) {

            if (check(type)) {

                advance();
                return true;
            }
        }

        return false;
    }

    private boolean check(TokenType type) {

        if (isAtEnd()) {
            return type == TokenType.EOF;
        }

        return current().getType() == type;
    }

    private Token advance() {

        if (!isAtEnd()) {
            position++;
        }

        return previous();
    }

    private boolean isAtEnd() {

        return current().getType()
                == TokenType.EOF;
    }

    private Token current() {

        return tokens.get(position);
    }

    private Token previous() {

        return tokens.get(position - 1);
    }

  

    private ParseException error(
            Token token,
            String message
    ) {

        return new ParseException(
                formatError(token, message)
        );
    }

    private String formatError(
            Token token,
            String message
    ) {

        String found;

        if (token.getType() == TokenType.EOF) {
            found = "EOF";
        } else {
            found = token.getLexeme();
        }

        return String.format(
                "Syntax Error [Line %d, Column %d]: %s Found '%s'.",
                token.getLine(),
                token.getColumn(),
                message,
                found
        );
    }

    

    private static class ParseException
            extends RuntimeException {

        ParseException(String message) {
            super(message);
        }
    }
}