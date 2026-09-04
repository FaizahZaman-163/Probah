package probah;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import probah.ast.ProgramNode;
import probah.lexer.Lexer;
import probah.lexer.Token;
import probah.parser.Parser;
import probah.symbol.SymbolTable;
import probah.symbol.SymbolTableBuilder;

public class Main {

    public static void main(String[] args) {

       
        if (args.length != 1) {
            System.out.println("Usage: java -cp out probah.Main <source-file.probah>");
            return;
        }

        String fileName = args[0];

        try {

        
            String source = Files.readString(
                    Path.of(fileName),
                    StandardCharsets.UTF_8
            );

            
            System.out.println("PROBAH COMPILER");
            
            System.out.println("Source file: " + fileName);
            System.out.println();

            
            System.out.println(" TOKENS");

            Lexer lexer = new Lexer(source);
            List<Token> tokens = lexer.tokenize();

            for (Token token : tokens) {
                System.out.println(token);
            }

            if (!lexer.getErrors().isEmpty()) {

                System.out.println();
                System.out.println("LEXICAL ERRORS");

                for (String error : lexer.getErrors()) {
                    System.out.println(error);
                }

                System.out.println();
                System.out.println("Compilation stopped because of lexical errors.");
                return;
            }

            System.out.println();
            System.out.println("Lexical analysis completed successfully.");

            
            System.out.println();
            System.out.println("PARSING");

            Parser parser = new Parser(tokens);
            ProgramNode program = parser.parse();

            if (!parser.getErrors().isEmpty()) {

                System.out.println();
                System.out.println("SYNTAX ERRORS");

                for (String error : parser.getErrors()) {
                    System.out.println(error);
                }

                System.out.println();
                System.out.println("Compilation stopped because of syntax errors.");
                return;
            }

            System.out.println("Parsing completed successfully.");

           
            System.out.println();
            System.out.println(" AST ");

            System.out.println(program);

        
            System.out.println();
            System.out.println("SYMBOL TABLE");

            SymbolTableBuilder symbolTableBuilder =
                    new SymbolTableBuilder();

            SymbolTable symbolTable =
                    symbolTableBuilder.build(program);

            symbolTable.printTable();

            
            if (!symbolTableBuilder.getErrors().isEmpty()) {

                System.out.println();
                System.out.println("SYMBOL TABLE ERRORS");

                for (String error : symbolTableBuilder.getErrors()) {
                    System.out.println(error);
                }

            } else {

                System.out.println();
                System.out.println("Symbol table is constructed.");
            }

        
            System.out.println();
            System.out.println("Semantic Analysis: NOT IMPLEMENTED YET.");

        } catch (IOException e) {

            
            System.out.println(
                    "File Error: Could not read source file '" +
                    fileName + "'."
            );

            System.out.println("Reason: " + e.getMessage());

        } catch (Exception e) {

            
            System.out.println();
            System.out.println("Compiler Error: " + e.getMessage());
        }
    }
}