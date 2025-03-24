import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;


public class Main {
    
    public static void main(String[] args) {
        String fileName = args[0].substring(0, args[0].indexOf('.'));
        File inputFile = new File(args[0]);
        File outPutFile = new File(fileName + ".hack");
        try (BufferedWriter bWriter = new BufferedWriter(new FileWriter(outPutFile))) {
            SymboleTable symbolTable = new SymboleTable();

            symbolTable.addEntry("R0", 0);
            symbolTable.addEntry("R1", 1);
            symbolTable.addEntry("R2", 2);
            symbolTable.addEntry("R3", 3);
            symbolTable.addEntry("R4", 4);
            symbolTable.addEntry("R5", 5);
            symbolTable.addEntry("R6", 6);
            symbolTable.addEntry("R7", 7);
            symbolTable.addEntry("R8", 8);
            symbolTable.addEntry("R9", 9);
            symbolTable.addEntry("R10", 10);
            symbolTable.addEntry("R11", 11);
            symbolTable.addEntry("R12", 12);
            symbolTable.addEntry("R13", 13);
            symbolTable.addEntry("R14", 14);
            symbolTable.addEntry("R15", 15);
            symbolTable.addEntry("SCREEN", 16384);
            symbolTable.addEntry("KBD", 24576);
            symbolTable.addEntry("SP", 0);
            symbolTable.addEntry("LCL", 1);
            symbolTable.addEntry("ARG", 2);
            symbolTable.addEntry("THIS", 3);
            symbolTable.addEntry("THAT", 4);

            Parser parserFirstPass = new Parser(inputFile);
            int lineIndex = 0;
            while (parserFirstPass.hasMoreLines()) {
               if (parserFirstPass.instructionType() == Parser.InstructionType.L_INSTRUCTION) {
                    symbolTable.addEntry(parserFirstPass.symbol(), lineIndex);
                }
                else lineIndex++;
                parserFirstPass.advance();
            }

            Parser parserSecondPass = new Parser(inputFile);
            int index = 16, num;
            Code code = new Code();
            while (parserSecondPass.hasMoreLines()) {
                if (parserSecondPass.instructionType() == Parser.InstructionType.A_INSTRUCTION) {
                    if (!symbolTable.contains(parserSecondPass.symbol())){
                        try {
                            num = Integer.parseInt(parserSecondPass.symbol());  
                            symbolTable.addEntry(parserSecondPass.symbol(), num);
                        } catch (NumberFormatException e) {
                            symbolTable.addEntry(parserSecondPass.symbol(), index);
                            index++;
                        } 
                    }
                    bWriter.write(String.format("%16s", Integer.toBinaryString(symbolTable.getAddress(parserSecondPass.symbol()))).replace(' ', '0'));
                    bWriter.write("\n");
                }
                if (parserSecondPass.instructionType() == Parser.InstructionType.C_INSTRUCTION) {
                    bWriter.write("111" + code.comp(parserSecondPass.comp()) + code.dest(parserSecondPass.dest()) + code.jump(parserSecondPass.jump()));
                    bWriter.write("\n");
                }
                parserSecondPass.advance();
            }
            
        }
        catch (IOException e) {
			e.printStackTrace();
		}
    }
}
