import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import java.io.File;


public class CodeWriter {

    private BufferedWriter bWriter;
    private String fileName;
    private int counter = 0;
    private int countRet = 1;

    public CodeWriter(File vmFile) throws IOException {
        this.fileName = vmFile.getName().substring(0, vmFile.getName().indexOf('.'));
        File outPutFile = new File(fileName + ".asm");
        this.bWriter = new BufferedWriter(new FileWriter(outPutFile));
    }

    public void writeInit() throws IOException {
        bWriter.write("@256" + "\n" + "D=A" + "\n" + "@SP" + "\n" + "M=D" + "\n");
        writeCall("Sys.init", 0);
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void writeArithmetic(String command) throws IOException {
        if (command.equals("neg")) {
            bWriter.write("D=0" + "\n" + "@SP" + "\n" + "A=M-1" + "\n" + "M=D-M" + "\n");
        } else if (command.equals("add")) {
            bWriter.write("@SP" + "\n" + "AM=M-1" + "\n" + "D=M" + "\n" + "@SP" + "\n" + "A=M-1" + "\n" + "M=D+M" + "\n");
        } else if (command.equals("sub")) {
            bWriter.write("@SP" + "\n" + "AM=M-1" + "\n" + "D=M" + "\n" + "@SP" + "\n" + "A=M-1" + "\n" + "M=M-D" + "\n");
        } else if (command.equals("eq")) {
            bWriter.write("@SP" + "\n" + "AM=M-1" + "\n" + "D=M" + "\n" + "@SP" + "\n" + "A=M-1" + "\n" + "D=M-D" + "\n");
            bWriter.write("@EQUAL" + counter + "\n" + "D;JEQ" + "\n" + "@SP" + "\n" + "A=M-1" + "\n" + "M=0" + "\n");
            bWriter.write("@ENDEQUAL" + counter + "\n" + "0;JMP" + "\n" + "(EQUAL" + counter + ")" + "\n" + "@SP" + "\n");
            bWriter.write("A=M-1" + "\n" + "M=-1" + "\n" + "(ENDEQUAL" + counter + ")" + "\n");
            counter++;
        } else if (command.equals("gt")) {
            bWriter.write("@SP" + "\n" + "AM=M-1" + "\n" + "D=M" + "\n" + "@SP" + "\n" + "A=M-1" + "\n" + "D=M-D" + "\n");
            bWriter.write("@GT" + counter + "\n" + "D;JGT" + "\n" + "@SP" + "\n" + "A=M-1" + "\n" + "M=0" + "\n");
            bWriter.write("@ENDGT" + counter + "\n" + "0;JMP" + "\n" + "(GT" + counter + ")" + "\n" + "@SP" + "\n");
            bWriter.write("A=M-1" + "\n" + "M=-1" + "\n" + "(ENDGT" + counter + ")" + "\n");
            counter++;
        } else if (command.equals("lt")) {
            bWriter.write("@SP" + "\n" + "AM=M-1" + "\n" + "D=M" + "\n" + "@SP" + "\n" + "A=M-1" + "\n" + "D=M-D" + "\n");
            bWriter.write("@LT" + counter + "\n" + "D;JLT" + "\n" + "@SP" + "\n" + "A=M-1" + "\n" + "M=0" + "\n");
            bWriter.write("@ENDLT" + counter + "\n" + "0;JMP" + "\n" + "(LT" + counter + ")" + "\n" + "@SP" + "\n" + "A=M-1" + "\n");
            bWriter.write("M=-1" + "\n" + "(ENDLT" + counter + ")" + "\n");
            counter++;
        } else if (command.equals("or")) {
            bWriter.write("@SP" + "\n" + "AM=M-1" + "\n" + "D=M" + "\n" + "A=A-1" + "\n" + "M=D|M" + "\n");
            
        } else if (command.equals("and")) {
            bWriter.write("@SP" + "\n" + "AM=M-1" + "\n" + "D=M" + "\n" + "A=A-1" + "\n" + "M=D&M" + "\n");

        } else if (command.equals("not")) {
            bWriter.write("@SP" + "\n" + "A=M-1" + "\n" + "M=!M" + "\n");
        }
    }

    public void writePushPop(Parser.CommandType commandType, String segment, int index) throws IOException {
        if (commandType == Parser.CommandType.C_PUSH) {
            if (segment.equals("constant")) {
                bWriter.write("@" + index + "\n" + "D=A" + "\n" + "@SP" + "\n" + "A=M" + "\n" + "M=D" + "\n" + "@SP" + "\n" + "M=M+1" + "\n");
            }

            if (segment.equals("local") || segment.equals("argument") ||
                         segment.equals("this") || segment.equals("that")) {
                if (segment.equals("local")) {
                    bWriter.write("@LCL" + "\n");
                } 
                if (segment.equals("argument")) {
                    bWriter.write("@ARG" + "\n");
                }
                if (segment.equals("this")) {
                    bWriter.write("@THIS" + "\n");
                }
                if (segment.equals("that")) {
                    bWriter.write("@THAT" + "\n");
                }
                bWriter.write("D=M" + "\n" + "@" + index + "\n" + "A=D+A" + "\n" + "D=M" + "\n" + "@SP" + "\n");
                bWriter.write("A=M" + "\n" + "M=D" + "\n" + "@SP" + "\n" + "M=M+1" + "\n");
            }

            if (segment.equals("static")) {
                bWriter.write("@" + this.fileName + "." + index + "\n" + "D=M" + "\n" + "@SP" + "\n" + "A=M" + "\n" + "M=D" + "\n");
                bWriter.write("@SP" + "\n" + "M=M+1" + "\n");
            }

            if (segment.equals("temp")) {
                bWriter.write("@5" + "\n" + "D=A" + "\n" + "@" + index + "\n" + "A=D+A" + "\n" + "D=M" + "\n" + "@SP" + "\n");
                bWriter.write("A=M" + "\n" + "M=D" + "\n" + "@SP" + "\n" + "M=M+1" + "\n");
            }

            if (segment.equals("pointer")) {
                if (index == 0) {
                    bWriter.write("@THIS" + "\n" + "D=M" + "\n");
                }
                if (index == 1) {
                    bWriter.write("@THAT" + "\n" + "D=M" + "\n");
                }
                bWriter.write("@SP" + "\n" + "A=M" + "\n" + "M=D" + "\n" + "@SP" + "\n" + "M=M+1" + "\n");
            }
        }

        if (commandType == Parser.CommandType.C_POP) {
            if (segment.equals("local") || segment.equals("argument") ||
                    segment.equals("this") || segment.equals("that")) {
                if (segment.equals("local")) {
                    bWriter.write("@LCL" + "\n");
                }
                if (segment.equals("argument")) {
                    bWriter.write("@ARG" + "\n");
                }
                if (segment.equals("this")) {
                    bWriter.write("@THIS" + "\n");
                }
                if (segment.equals("that")) {
                    bWriter.write("@THAT" + "\n");
                }
                bWriter.write("D=M" + "\n" + "@" + index + "\n" + "D=D+A" + "\n" + "@R13" + "\n" + "M=D" + "\n" + "@SP" + "\n");
                bWriter.write("AM=M-1" + "\n" + "D=M" + "\n" + "@R13" + "\n" + "A=M" + "\n" + "M=D" + "\n");
            }

            if (segment.equals("static")) {
                bWriter.write("@SP" + "\n" + "M=M-1" + "\n" + "A=M" + "\n" + "D=M" + "\n");
                bWriter.write("@" + this.fileName + "." + index + "\n" + "M=D" + "\n");
            }
        
            if (segment.equals("temp")) {
                bWriter.write("@5" + "\n" + "D=A" + "\n" + "@" + index + "\n" + "D=D+A" + "\n" + "@R13" + "\n" + "M=D" + "\n");
                bWriter.write("@SP" + "\n" + "AM=M-1" + "\n" + "D=M" + "\n" + "@R13" + "\n" + "A=M" + "\n" + "M=D" + "\n");
            }
        
            if (segment.equals("pointer") && index == 0) {
                bWriter.write("@THIS" + "\n" + "D=A" + "\n" + "@R13" + "\n" + "M=D" + "\n" + "@SP" + "\n" + "AM=M-1" + "\n");
                bWriter.write("D=M" + "\n" + "@R13" + "\n" + "A=M" + "\n" + "M=D" + "\n");
            }
        
            if (segment.equals("pointer") && index == 1) {
                bWriter.write("@THAT" + "\n" + "D=A" + "\n" + "@R13" + "\n" + "M=D" + "\n" + "@SP" + "\n" + "AM=M-1" + "\n");
                bWriter.write("D=M" + "\n" + "@R13" + "\n" + "A=M" + "\n" + "M=D" + "\n");
            }
        }
    }

    public void writeLabel(String label) throws IOException {
        bWriter.write("(" + label + ")" + "\n");
    }
    
    public void writeGoto(String label) throws IOException {
        bWriter.write("@" + label + "\n" + "0;JMP" + "\n");
    }
    
    public void writeIf(String label) throws IOException {
        bWriter.write("@SP" + "\n" + "AM=M-1" + "\n" + "D=M" + "\n" + "@" + label + "\n" + "D;JNE" + "\n");
    }
    
    public void writeFunction(String functionName, int nVars) throws IOException {
        bWriter.write("(" + functionName + ")" + "\n"); // Create the function label
        for (int i = 0; i < nVars; i++) {
            bWriter.write("@0" + "\n" + "D=A" + "\n" + "@SP" + "\n" + "A=M" + "\n" + "M=D" + "\n" + "@SP" + "\n" + "M=M+1" + "\n"); // Initialize local variables to 0
        }
    }
    
    public void writeCall(String functionName, int nArgs) throws IOException {
        bWriter.write("@" + functionName + "$ret." + countRet + "\n" + "D=A" + "\n" + "@SP" + "\n" + "A=M" + "\n" + "M=D" + "\n" + "@SP" + "\n" + "M=M+1" + "\n"); // push return address
        bWriter.write("@LCL" + "\n" + "D=M" + "\n" + "@SP" + "\n" + "A=M" + "\n" + "M=D" + "\n" + "@SP" + "\n" + "M=M+1" + "\n"); // push LCL
        bWriter.write("@ARG" + "\n" + "D=M" + "\n" + "@SP" + "\n" + "A=M" + "\n" + "M=D" + "\n" + "@SP" + "\n" + "M=M+1" + "\n"); // push ARG
        bWriter.write("@THIS" + "\n" + "D=M" + "\n" + "@SP" + "\n" + "A=M" + "\n" + "M=D" + "\n" + "@SP" + "\n" + "M=M+1" + "\n"); // push THIS
        bWriter.write("@THAT" + "\n" + "D=M" + "\n" + "@SP" + "\n" + "A=M" + "\n" + "M=D" + "\n" + "@SP" + "\n" + "M=M+1" + "\n"); // push THAT
        bWriter.write("@SP" + "\n" + "D=M" + "\n" + "@5" + "\n" + "D=D-A" + "\n" + "@" + nArgs + "\n" + "D=D-A" + "\n" + "@ARG" + "\n" + "M=D" + "\n"); // ARG = SP - 5 - nArgs
        bWriter.write("@SP" + "\n" + "D=M" + "\n" + "@LCL" + "\n" + "M=D" + "\n"); // LCL = SP
        bWriter.write("@" + functionName + "\n" + "0;JMP" + "\n" + "(" + functionName + "$ret." + countRet + ")" + "\n"); // goto functionName, (retAddrLabel)
        countRet++;
    }

    public void writeReturn() throws IOException {
        bWriter.write("@LCL" + "\n" + "D=M" + "\n" + "@endFrame" + "\n" + "M=D" + "\n"); // endFrame = LCL
        bWriter.write("@5" + "\n" + "A=D-A" + "\n" + "D=M" + "\n" + "@retAddr" + "\n" + "M=D" + "\n"); // retAddr = *(endFrame - 5)
        bWriter.write("@ARG" + "\n" + "D=M" + "\n" + "@0" + "\n" + "D=D+A" + "\n" + "@R13" + "\n" + "M=D" + "\n" + "@SP" + "\n"); 
        bWriter.write("AM=M-1" + "\n" + "D=M" + "\n" + "@R13" + "\n" + "A=M" + "\n" + "M=D" + "\n"); // *ARG = pop()
        bWriter.write("@ARG" + "\n" + "D=M" + "\n" + "@SP" + "\n" + "M=D+1" + "\n"); // SP = ARG + 1
        bWriter.write("@endFrame" + "\n" + "D=M-1" + "\n" + "AM=D" + "\n" + "D=M" + "\n" + "@THAT" + "\n" + "M=D" + "\n"); // THAT = *(endFrame - 1)
        bWriter.write("@endFrame" + "\n" + "D=M-1" + "\n" + "AM=D" + "\n" + "D=M" + "\n" + "@THIS" + "\n" + "M=D" + "\n"); // THIS = *(endFrame - 2)
        bWriter.write("@endFrame" + "\n" + "D=M-1" + "\n" + "AM=D" + "\n" + "D=M" + "\n" + "@ARG" + "\n" + "M=D" + "\n"); // ARG = *(endFrame - 3)
        bWriter.write("@endFrame" + "\n" + "D=M-1" + "\n" + "AM=D" + "\n" + "D=M" + "\n" + "@LCL" + "\n" + "M=D" + "\n"); // LCL = *(endFrame - 4)
        bWriter.write("@retAddr" + "\n" + "A=M" + "\n" + "0;JMP" + "\n"); // goto retAddr
    }
    

    public void close() throws IOException {
        this.bWriter.close();
    }

}