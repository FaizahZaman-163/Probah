package probah.ast;

public class AstPrinter {

    public static void print(ProgramNode program) {
        printNode(program, "", true);
    }

    private static void printNode(
            AstNode node,
            String prefix,
            boolean last
    ) {

        System.out.println(
                prefix +
                (last ? "└── " : "├── ") +
                nodeName(node)
        );

        String childPrefix =
                prefix + (last ? "    " : "│   ");

        if (node instanceof ProgramNode program) {

            for (int i = 0;
                 i < program.getStatements().size();
                 i++) {

                printNode(
                        program.getStatements().get(i),
                        childPrefix,
                        i == program.getStatements().size() - 1
                );
            }

        } else if (node instanceof BlockNode block) {

            for (int i = 0;
                 i < block.getStatements().size();
                 i++) {

                printNode(
                        block.getStatements().get(i),
                        childPrefix,
                        i == block.getStatements().size() - 1
                );
            }

        } else if (node instanceof DeclarationNode declaration) {

            System.out.println(
                    childPrefix +
                    "├── Type: " +
                    declaration.getDeclaredType()
            );

            System.out.println(
                    childPrefix +
                    "├── Name: " +
                    declaration.getName()
            );

            System.out.println(
                    childPrefix +
                    "└── Initializer:"
            );

            printNode(
                    declaration.getInitializer(),
                    childPrefix + "    ",
                    true
            );

        } else if (node instanceof AssignmentNode assignment) {

            System.out.println(
                    childPrefix +
                    "├── Name: " +
                    assignment.getName()
            );

            System.out.println(
                    childPrefix +
                    "└── Value:"
            );

            printNode(
                    assignment.getValue(),
                    childPrefix + "    ",
                    true
            );

        } else if (node instanceof PrintNode print) {

            printNode(
                    print.getExpression(),
                    childPrefix,
                    true
            );

        } else if (node instanceof IfNode ifNode) {

            System.out.println(
                    childPrefix +
                    "├── Condition:"
            );

            printNode(
                    ifNode.getCondition(),
                    childPrefix + "│   ",
                    true
            );

            System.out.println(
                    childPrefix +
                    "├── Then:"
            );

            printNode(
                    ifNode.getThenBranch(),
                    childPrefix + "│   ",
                    true
            );

            if (ifNode.getElseBranch() != null) {

                System.out.println(
                        childPrefix +
                        "└── Else:"
                );

                printNode(
                        ifNode.getElseBranch(),
                        childPrefix + "    ",
                        true
                );
            }

        } else if (node instanceof WhileNode whileNode) {

            System.out.println(
                    childPrefix +
                    "├── Condition:"
            );

            printNode(
                    whileNode.getCondition(),
                    childPrefix + "│   ",
                    true
            );

            System.out.println(
                    childPrefix +
                    "└── Body:"
            );

            printNode(
                    whileNode.getBody(),
                    childPrefix + "    ",
                    true
            );

        } else if (node instanceof BinaryExpressionNode binary) {

            System.out.println(
                    childPrefix +
                    "├── Operator: " +
                    binary.getOperator().getLexeme()
            );

            System.out.println(
                    childPrefix +
                    "├── Left:"
            );

            printNode(
                    binary.getLeft(),
                    childPrefix + "│   ",
                    true
            );

            System.out.println(
                    childPrefix +
                    "└── Right:"
            );

            printNode(
                    binary.getRight(),
                    childPrefix + "    ",
                    true
            );

        } else if (node instanceof UnaryExpressionNode unary) {

            System.out.println(
                    childPrefix +
                    "├── Operator: " +
                    unary.getOperator().getLexeme()
            );

            System.out.println(
                    childPrefix +
                    "└── Operand:"
            );

            printNode(
                    unary.getOperand(),
                    childPrefix + "    ",
                    true
            );
        }
    }

    private static String nodeName(AstNode node) {

        if (node instanceof ProgramNode) {
            return "Program";
        }

        if (node instanceof BlockNode) {
            return "Block";
        }

        if (node instanceof DeclarationNode) {
            return "Declaration";
        }

        if (node instanceof AssignmentNode) {
            return "Assignment";
        }

        if (node instanceof PrintNode) {
            return "Print";
        }

        if (node instanceof IfNode) {
            return "If";
        }

        if (node instanceof WhileNode) {
            return "While";
        }

        if (node instanceof IntegerLiteralNode literal) {
            return "Integer: " + literal.getValue();
        }

        if (node instanceof BooleanLiteralNode literal) {
            return "Boolean: " + literal.getValue();
        }

        if (node instanceof IdentifierNode identifier) {
            return "Identifier: " + identifier.getName();
        }

        if (node instanceof BinaryExpressionNode) {
            return "Binary Expression";
        }

        if (node instanceof UnaryExpressionNode) {
            return "Unary Expression";
        }

        return "Unknown";
    }
}