package probah.symbol;

import probah.lexer.TokenType;

public class Symbol {

    private final String name;
    private final TokenType type;
    private final int scopeLevel;
    private final String scopeName;
    private final int declarationLine;

    private Object value;
    private boolean initialized;


    public Symbol(
            String name,
            TokenType type,
            int scopeLevel,
            String scopeName,
            int declarationLine,
            Object value
    ) {

        this.name = name;
        this.type = type;
        this.scopeLevel = scopeLevel;
        this.scopeName = scopeName;
        this.declarationLine = declarationLine;

        this.value = value;
        this.initialized = value != null;
    }


    public String getName() {
        return name;
    }


    public TokenType getType() {
        return type;
    }


    public int getScopeLevel() {
        return scopeLevel;
    }


    public String getScopeName() {
        return scopeName;
    }


    public int getDeclarationLine() {
        return declarationLine;
    }


    public Object getValue() {
        return value;
    }


    public boolean isInitialized() {
        return initialized;
    }


    public void initialize(Object value) {

        this.value = value;
        this.initialized = true;
    }


    @Override
    public String toString() {

        String typeName;

        if (type == TokenType.INTEGER_TYPE) 
        {
            typeName = "Integer";
        } else if (type == TokenType.BOOLEAN_TYPE) {
            typeName = "Boolean";
        } else {
            typeName = type.toString();
        }


        return String.format(
                "%-12s %-12s %-10s %-8d %-12s",
                name,
                typeName,
                scopeName,
                declarationLine,
                initialized ? "yes" : "no"
        );
    }
}