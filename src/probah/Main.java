package probah;

import probah.lexer.Lexer;
import probah.lexer.Token;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println(
                    "Usage: java -cp out probah.Main <source.probah>"
            );
            return;
        }

        Path sourcePath = Path.of(args[0]);

        try {

            String source = Files.readString(
                    sourcePath,
                    StandardCharsets.UTF_8
            );

            Lexer lexer = new Lexer(source);

            List<Token> tokens = lexer.tokenize();

            System.out.println(
                    "TOKENS :"
            );

            for (Token token : tokens) {
                System.out.println(token);
            }

            System.out.println(
                    "\nLEXICAL ERRORS:"
            );

            if (lexer.getErrors().isEmpty()) {

                System.out.println(
                        "No lexical errors."
                );

            } else {

                lexer.getErrors()
                        .forEach(System.out::println);
            }

        } catch (IOException e) {

            System.out.println(
                    "Compiler Error: Could not read source file: "
                            + sourcePath
            );

            System.out.println(
                    "Reason: " + e.getMessage()
            );

        } catch (Exception e) {

            System.out.println(
                    "Compiler Error: Unexpected error is handled."
            );

            System.out.println(
                    "Reason: " + e.getMessage()
            );
        }
    }
}