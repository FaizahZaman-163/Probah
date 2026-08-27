package probah.lexer;

public enum TokenType {

    
    DECLARE,        // ধরি

    INTEGER_TYPE,   // পূর্ণ
    BOOLEAN_TYPE,   // যুক্তি

    TRUE,           // সত্য
    FALSE,          // মিথ্যা

    IF,             // যদি
    ELSE,           // নাহলে
    WHILE,          // যতক্ষণ
    PRINT,          // দেখাও

    AND,            // এবং
    OR,             // অথবা
    NOT,            // না

    
    IDENTIFIER,
    INTEGER_LITERAL,

    
    ASSIGN,         // :=

    
    PLUS,           // +
    MINUS,          // -
    MULTIPLY,       // *
    DIVIDE,         // /

    
    LESS,           // <
    LESS_EQUAL,     // <=
    GREATER,        // >
    GREATER_EQUAL,  // >=
    EQUAL_EQUAL,    // ==
    NOT_EQUAL,      // !=

    
    LEFT_PAREN,     // (
    RIGHT_PAREN,    // )

    LEFT_BRACE,     // {
    RIGHT_BRACE,    // }

    SEMICOLON,      // ;

    
    EOF
}