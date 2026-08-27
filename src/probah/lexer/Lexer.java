package probah.lexer;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lexer {

    private final String source;

    private final List<Token> tokens = new ArrayList<>();
    private final List<String> errors = new ArrayList<>();

    private int position = 0;
    private int line = 1;
    private int column = 1;

    private static final Map<String, TokenType> KEYWORDS =
            createKeywordMap();

    public Lexer(String source) {
        this.source = Normalizer.normalize(
                source == null ? "" : source,
                Normalizer.Form.NFC
        );
    }

    private static Map<String, TokenType> createKeywordMap() {

        Map<String, TokenType> map = new HashMap<>();

        map.put("ধরি", TokenType.DECLARE);
        map.put("পূর্ণ", TokenType.INTEGER_TYPE);
        map.put("যুক্তি", TokenType.BOOLEAN_TYPE);

        map.put("সত্য", TokenType.TRUE);
        map.put("মিথ্যা", TokenType.FALSE);

        map.put("যদি", TokenType.IF);
        map.put("নাহলে", TokenType.ELSE);
        map.put("যতক্ষণ", TokenType.WHILE);
        map.put("দেখাও", TokenType.PRINT);

        map.put("এবং", TokenType.AND);
        map.put("অথবা", TokenType.OR);
        map.put("না", TokenType.NOT);

        return Collections.unmodifiableMap(map);
    }

    public List<Token> tokenize() {

        while (!isAtEnd()) {
            scanToken();
        }

        tokens.add(
                new Token(
                        TokenType.EOF,
                        "",
                        line,
                        column
                )
        );

        return Collections.unmodifiableList(tokens);
    }

    public List<String> getErrors() {
        return Collections.unmodifiableList(errors);
    }

    private void scanToken() {

        char current = peek();

        
        if (Character.isWhitespace(current)) {
            advance();
            return;
        }

        
        if (current == '#') {
            skipSingleLineComment();
            return;
        }

        
        if (isIdentifierStart(current)) {
            scanIdentifierOrKeyword();
            return;
        }

        
        if (isAsciiDigit(current)) {
            scanNumber();
            return;
        }

        int startLine = line;
        int startColumn = column;

        switch (current) {

            case ':':
                advance();

                if (match('=')) {
                    addToken(
                            TokenType.ASSIGN,
                            ":=",
                            startLine,
                            startColumn
                    );
                } else {
                    addError(
                            startLine,
                            startColumn,
                            "Unexpected ':'. Assignment operator is ':='."
                    );
                }
                break;

            case '=':
                advance();

                if (match('=')) {
                    addToken(
                            TokenType.EQUAL_EQUAL,
                            "==",
                            startLine,
                            startColumn
                    );
                } else {
                    addError(
                            startLine,
                            startColumn,
                            "Unexpected '='. Use ':=' for assignment or '==' for equality."
                    );
                }
                break;

            case '!':
                advance();

                if (match('=')) {
                    addToken(
                            TokenType.NOT_EQUAL,
                            "!=",
                            startLine,
                            startColumn
                    );
                } else {
                    addError(
                            startLine,
                            startColumn,
                            "Unexpected '!'. Use 'না' for logical NOT or '!=' for not-equal."
                    );
                }
                break;

            case '<':
                advance();

                boolean lessHasEqual = match('=');

                addToken(
                        lessHasEqual
                                ? TokenType.LESS_EQUAL
                                : TokenType.LESS,
                        lessHasEqual ? "<=" : "<",
                        startLine,
                        startColumn
                );
                break;

            case '>':
                advance();

                boolean greaterHasEqual = match('=');

                addToken(
                        greaterHasEqual
                                ? TokenType.GREATER_EQUAL
                                : TokenType.GREATER,
                        greaterHasEqual ? ">=" : ">",
                        startLine,
                        startColumn
                );
                break;

            case '+':
                advance();
                addToken(
                        TokenType.PLUS,
                        "+",
                        startLine,
                        startColumn
                );
                break;

            case '-':
                advance();
                addToken(
                        TokenType.MINUS,
                        "-",
                        startLine,
                        startColumn
                );
                break;

            case '*':
                advance();
                addToken(
                        TokenType.MULTIPLY,
                        "*",
                        startLine,
                        startColumn
                );
                break;

            case '/':
                advance();
                addToken(
                        TokenType.DIVIDE,
                        "/",
                        startLine,
                        startColumn
                );
                break;

            case '(':
                advance();
                addToken(
                        TokenType.LEFT_PAREN,
                        "(",
                        startLine,
                        startColumn
                );
                break;

            case ')':
                advance();
                addToken(
                        TokenType.RIGHT_PAREN,
                        ")",
                        startLine,
                        startColumn
                );
                break;

            case '{':
                advance();
                addToken(
                        TokenType.LEFT_BRACE,
                        "{",
                        startLine,
                        startColumn
                );
                break;

            case '}':
                advance();
                addToken(
                        TokenType.RIGHT_BRACE,
                        "}",
                        startLine,
                        startColumn
                );
                break;

            case ';':
                advance();
                addToken(
                        TokenType.SEMICOLON,
                        ";",
                        startLine,
                        startColumn
                );
                break;

            default:

                addError(
                        startLine,
                        startColumn,
                        "Unexpected character '" + current + "'."
                );

                
                advance();
        }
    }

    private void scanIdentifierOrKeyword() {

        int start = position;
        int startLine = line;
        int startColumn = column;

        advance();

        while (!isAtEnd() && isIdentifierPart(peek())) {
            advance();
        }

        String word = source.substring(start, position);

        TokenType type = KEYWORDS.getOrDefault(
                word,
                TokenType.IDENTIFIER
        );

        addToken(
                type,
                word,
                startLine,
                startColumn
        );
    }

    private void scanNumber() {

        int start = position;
        int startLine = line;
        int startColumn = column;

        while (!isAtEnd() && isAsciiDigit(peek())) {
            advance();
        }

        String number = source.substring(start, position);

        addToken(
                TokenType.INTEGER_LITERAL,
                number,
                startLine,
                startColumn
        );
    }

    private void skipSingleLineComment() {

        while (!isAtEnd() && peek() != '\n') {
            advance();
        }
    }

    private boolean isIdentifierStart(char ch) {

        return ch == '_'
                || Character.isLetter(ch);
    }

    private boolean isIdentifierPart(char ch) {

        int type = Character.getType(ch);

        return ch == '_'
                || Character.isLetterOrDigit(ch)
                || type == Character.NON_SPACING_MARK
                || type == Character.COMBINING_SPACING_MARK;
    }

    private boolean isAsciiDigit(char ch) {

        return ch >= '0' && ch <= '9';
    }

    private boolean match(char expected) {

        if (isAtEnd() || peek() != expected) {
            return false;
        }

        advance();
        return true;
    }

    private char peek() {
        return source.charAt(position);
    }

    private char advance() {

        char ch = source.charAt(position++);

        if (ch == '\n') {
            line++;
            column = 1;
        } else {
            column++;
        }

        return ch;
    }

    private boolean isAtEnd() {
        return position >= source.length();
    }

    private void addToken(
            TokenType type,
            String lexeme,
            int tokenLine,
            int tokenColumn
    ) {

        tokens.add(
                new Token(
                        type,
                        lexeme,
                        tokenLine,
                        tokenColumn
                )
        );
    }

    private void addError(
            int errorLine,
            int errorColumn,
            String message
    ) {

        errors.add(
                String.format(
                        "Lexical Error[Line %d,Column %d]:%s",
                        errorLine,
                        errorColumn,
                        message
                )
        );
    }
}