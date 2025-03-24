import java.io.File;
import java.io.IOException;


public class Main {
    public static void main(String[] args) {
        File inputPath = new File(args[0]);
        try {
            if (inputPath.isDirectory()) {
                File[] vmFiles = inputPath.listFiles((dir, name) -> name.endsWith(".vm"));
                if (vmFiles == null || vmFiles.length == 0) {
                    System.out.println("No .vm files found in the directory.");
                    return;
                }
                File outputFile = new File(inputPath, inputPath.getName() + ".asm");
                CodeWriter cWriter = new CodeWriter(outputFile);

                for (File vmFile : vmFiles) {
                    Parser parser = new Parser(vmFile);
                    cWriter.setFileName(vmFile.getName());

                    while (parser.hasMoreLines()) {
                        if (parser.commandType() == Parser.CommandType.C_PUSH || parser.commandType() == Parser.CommandType.C_POP) {
                            cWriter.writePushPop(parser.commandType(), parser.arg1(), Integer.parseInt(parser.arg2()));
                        } else if (parser.commandType() == Parser.CommandType.C_ARITHMETIC) {
                            cWriter.writeArithmetic(parser.arg1());
                        }
                        parser.advance();
                    }
                }
                cWriter.close();

            } else if (inputPath.isFile() && inputPath.getName().endsWith(".vm")) {
                Parser parser = new Parser(inputPath);
                CodeWriter cWriter = new CodeWriter(new File(inputPath.getParent(), inputPath.getName().replace(".vm", ".asm")));

                while (parser.hasMoreLines()) {
                    if (parser.commandType() == Parser.CommandType.C_PUSH || parser.commandType() == Parser.CommandType.C_POP) {
                        cWriter.writePushPop(parser.commandType(), parser.arg1(), Integer.parseInt(parser.arg2()));
                    } else if (parser.commandType() == Parser.CommandType.C_ARITHMETIC) {
                        cWriter.writeArithmetic(parser.arg1());
                    }
                    parser.advance();
                }
                cWriter.close();
            } else {
                System.out.println("Invalid input: Please provide a .vm file or a directory containing .vm files.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}