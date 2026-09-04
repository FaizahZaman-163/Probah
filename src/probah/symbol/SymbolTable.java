package probah.symbol;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.Map;

import probah.lexer.TokenType;

public class SymbolTable {

    private final Deque<ScopeFrame> scopes =
            new ArrayDeque<>();

    private int nextScopeNumber = 1;


    public SymbolTable() {

        enterScope("global");
    }


    public void enterScope() {

        enterScope("block-" + nextScopeNumber++);
    }


    public void enterScope(String scopeName) {

        int level = scopes.size();

        scopes.push(
                new ScopeFrame(
                        scopeName,
                        level
                )
        );
    }


    public void exitScope() {

        if (scopes.size() <= 1) {
            throw new IllegalStateException(
                    "Cannot remove global scope."
            );
        }

        scopes.pop();
    }


    public int currentScopeLevel() {

        return scopes.peek().level;
    }


    public String currentScopeName() {

        return scopes.peek().name;
    }


    public boolean define(
            String name,
            TokenType type,
            int declarationLine
    ) {

        return define(
                name,
                type,
                declarationLine,
                null
        );
    }


    public boolean define(
            String name,
            TokenType type,
            int declarationLine,
            Object value
    ) {

        ScopeFrame current =
                scopes.peek();


        /*
         * Duplicate declarations are checked
         * only inside the current scope.
         */
        if (current.symbols.containsKey(name)) {
            return false;
        }


        Symbol symbol =
                new Symbol(
                        name,
                        type,
                        current.level,
                        current.name,
                        declarationLine,
                        value
                );


        current.symbols.put(
                name,
                symbol
        );


        return true;
    }


    public Symbol lookup(
            String name
    ) {

        /*
         * Search from the innermost scope
         * outward.
         */
        for (ScopeFrame scope : scopes) {

            Symbol symbol =
                    scope.symbols.get(name);

            if (symbol != null) {
                return symbol;
            }
        }


        return null;
    }


    public Symbol lookupCurrentScope(
            String name
    ) {

        return scopes
                .peek()
                .symbols
                .get(name);
    }


    public boolean isDefined(
            String name
    ) {

        return lookup(name) != null;
    }


    public boolean isDefinedInCurrentScope(
            String name
    ) {

        return lookupCurrentScope(name)
                != null;
    }


    public boolean update(
            String name,
            Object value
    ) {

        Symbol symbol =
                lookup(name);


        if (symbol == null) {
            return false;
        }


        symbol.initialize(value);

        return true;
    }


    public boolean isInitialized(
            String name
    ) {

        Symbol symbol =
                lookup(name);


        return symbol != null
                && symbol.isInitialized();
    }


    public void printTable() {

        System.out.println();
        System.out.println(
                "========== SYMBOL TABLE =========="
        );


        System.out.printf(
                "%-12s %-12s %-10s %-8s %-12s%n",
                "Name",
                "Type",
                "Scope",
                "Line",
                "Initialized"
        );


        System.out.println(
                "--------------------------------------------------------"
        );


        /*
         * Print from global toward inner scopes.
         */
        ArrayDeque<ScopeFrame> ordered =
                new ArrayDeque<>(
                        scopes
                );


        ScopeFrame[] frames =
                ordered.toArray(
                        new ScopeFrame[0]
                );


        for (int i = frames.length - 1;
             i >= 0;
             i--) {

            ScopeFrame scope = frames[i];


            for (Symbol symbol :
                    scope.symbols.values()) {

                System.out.println(symbol);
            }
        }
    }


    private static class ScopeFrame {

        private final String name;
        private final int level;

        private final Map<String, Symbol> symbols =
                new LinkedHashMap<>();


        private ScopeFrame(
                String name,
                int level
        ) {

            this.name = name;
            this.level = level;
        }
    }
}