import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import java.io.File;


public class CodeWriter {

    private BufferedWriter bWriter;
    private String fileName;
    private int counter = 0;

    public CodeWriter(File vmFile) throws IOException {
        this.fileName = vmFile.getName().substring(0, vmFile.getName().indexOf('.'));
        File outPutFile = new File(fileName + ".asm");
        this.bWriter = new BufferedWriter(new FileWriter(outPutFile));
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void writeArithmetic(String command) throws IOException {
        if (command.equals("neg")) {
            bWriter.write("D=0");
            bWriter.newLine();
            bWriter.write("@SP");
            bWriter.newLine();
            bWriter.write("A=M-1");
            bWriter.newLine();
            bWriter.write("M=D-M");
            bWriter.newLine();
        }
        if (command.equals("add")) {
            bWriter.write("@SP");
            bWriter.newLine();
            bWriter.write("AM=M-1");
            bWriter.newLine();
            bWriter.write("D=M");
            bWriter.newLine();
            bWriter.write("@SP");
            bWriter.newLine();
            bWriter.write("A=M-1");
            bWriter.newLine();
            bWriter.write("M=D+M");
            bWriter.newLine();
        }
        if (command.equals("sub")) {
            bWriter.write("@SP");
            bWriter.newLine();
            bWriter.write("AM=M-1");
            bWriter.newLine();
            bWriter.write("D=M");
            bWriter.newLine();
            bWriter.write("@SP");
            bWriter.newLine();
            bWriter.write("A=M-1");
            bWriter.newLine();
            bWriter.write("M=M-D");
            bWriter.newLine();
        }
        if (command.equals("eq")) {
            bWriter.write("@SP");
            bWriter.newLine();
            bWriter.write("AM=M-1");
            bWriter.newLine();
            bWriter.write("D=M");
            bWriter.newLine();
            bWriter.write("@SP");
            bWriter.newLine();
            bWriter.write("A=M-1");
            bWriter.newLine();
            bWriter.write("D=M-D");
            bWriter.newLine();

            bWriter.write("@EQUAL" + counter);
            bWriter.newLine();
            bWriter.write("D;JEQ");
            bWriter.newLine();
            bWriter.write("@SP");
            bWriter.newLine();
            bWriter.write("A=M-1");
            bWriter.newLine();
            bWriter.write("M=0");
            bWriter.newLine();
            bWriter.write("@ENDEQUAL" + counter);
            bWriter.newLine();
            bWriter.write("0;JMP");
            bWriter.newLine();
            bWriter.write("(EQUAL" + counter + ")");
            bWriter.newLine();
            bWriter.write("@SP");
            bWriter.newLine();
            bWriter.write("A=M-1");
            bWriter.newLine();
            bWriter.write("M=-1");
            bWriter.newLine();
            bWriter.write("(ENDEQUAL" + counter + ")");
            bWriter.newLine();
            counter++;
        }
        if (command.equals("gt")) {
            bWriter.write("@SP");
            bWriter.newLine();
            bWriter.write("AM=M-1");
            bWriter.newLine();
            bWriter.write("D=M");
            bWriter.newLine();
            bWriter.write("@SP");
            bWriter.newLine();
            bWriter.write("A=M-1");
            bWriter.newLine();
            bWriter.write("D=M-D");
            bWriter.newLine();

            bWriter.write("@GT" + counter);
            bWriter.newLine();
            bWriter.write("D;JGT");
            bWriter.newLine();
            bWriter.write("@SP");
            bWriter.newLine();
            bWriter.write("A=M-1");
            bWriter.newLine();
            bWriter.write("M=0");
            bWriter.newLine();
            bWriter.write("@ENDGT" + counter);
            bWriter.newLine();
            bWriter.write("0;JMP");
            bWriter.newLine();
            bWriter.write("(GT" + counter + ")");
            bWriter.newLine();
            bWriter.write("@SP");
            bWriter.newLine();
            bWriter.write("A=M-1");
            bWriter.newLine();
            bWriter.write("M=-1");
            bWriter.newLine();
            bWriter.write("(ENDGT" + counter + ")");
            bWriter.newLine();
            counter++;
        }
        if (command.equals("lt")) {
            bWriter.write("@SP");
            bWriter.newLine();
            bWriter.write("AM=M-1");
            bWriter.newLine();
            bWriter.write("D=M");
            bWriter.newLine();
            bWriter.write("@SP");
            bWriter.newLine();
            bWriter.write("A=M-1");
            bWriter.newLine();
            bWriter.write("D=M-D");
            bWriter.newLine();

            bWriter.write("@LT" + counter);
            bWriter.newLine();
            bWriter.write("D;JLT");
            bWriter.newLine();
            bWriter.write("@SP");
            bWriter.newLine();
            bWriter.write("A=M-1");
            bWriter.newLine();
            bWriter.write("M=0");
            bWriter.newLine();
            bWriter.write("@ENDLT" + counter);
            bWriter.newLine();
            bWriter.write("0;JMP");
            bWriter.newLine();
            bWriter.write("(LT" + counter + ")");
            bWriter.newLine();
            bWriter.write("@SP");
            bWriter.newLine();
            bWriter.write("A=M-1");
            bWriter.newLine();
            bWriter.write("M=-1");
            bWriter.newLine();
            bWriter.write("(ENDLT" + counter + ")");
            bWriter.newLine();
            counter++;
        }
        if (command.equals("or")) {
            bWriter.write("@SP");
            bWriter.newLine();
            bWriter.write("AM=M-1");
            bWriter.newLine();
            bWriter.write("D=M");
            bWriter.newLine();
            bWriter.write("A=A-1");
            bWriter.newLine();
            bWriter.write("M=D|M");
            bWriter.newLine();
        }
        if (command.equals("and")) {
            bWriter.write("@SP");
            bWriter.newLine();
            bWriter.write("AM=M-1");
            bWriter.newLine();
            bWriter.write("D=M");
            bWriter.newLine();
            bWriter.write("A=A-1");
            bWriter.newLine();
            bWriter.write("M=D&M");
            bWriter.newLine();
        }
        if (command.equals("not")) {
            bWriter.write("@SP");
            bWriter.newLine();
            bWriter.write("A=M-1");
            bWriter.newLine();
            bWriter.write("M=!M");
            bWriter.newLine();
        }
    }

    public void writePushPop(Parser.CommandType commandType, String segment, int index) throws IOException {
        if (commandType == Parser.CommandType.C_PUSH) {
            if (segment.equals("constant")) {
                bWriter.write("@" + index);
                bWriter.newLine();
                bWriter.write("D=A");
                bWriter.newLine();
                bWriter.write("@SP");
                bWriter.newLine();
                bWriter.write("A=M");
                bWriter.newLine();
                bWriter.write("M=D");
                bWriter.newLine();
                bWriter.write("@SP");
                bWriter.newLine();
                bWriter.write("M=M+1");
                bWriter.newLine();
            }

            if (segment.equals("local") || segment.equals("argument") ||
                         segment.equals("this") || segment.equals("that")) {
                if (segment.equals("local")) {
                    bWriter.write("@LCL");
                } 
                if (segment.equals("argument")) {
                    bWriter.write("@ARG");
                }
                if (segment.equals("this")) {
                    bWriter.write("@THIS");
                }
                if (segment.equals("that")) {
                    bWriter.write("@THAT");
                }
                bWriter.newLine();
                bWriter.write("D=M");
                bWriter.newLine();
                bWriter.write("@" + index);
                bWriter.newLine();
                bWriter.write("A=D+A");
                bWriter.newLine();
                bWriter.write("D=M");
                bWriter.newLine();
                bWriter.write("@SP");
                bWriter.newLine();
                bWriter.write("A=M");
                bWriter.newLine();
                bWriter.write("M=D");
                bWriter.newLine();
                bWriter.write("@SP");
                bWriter.newLine();
                bWriter.write("M=M+1");
                bWriter.newLine();
            }

            if (segment.equals("static")) {
                bWriter.write("@" + this.fileName + "." + index);
                bWriter.newLine();
                bWriter.write("D=M");
                bWriter.newLine();
                bWriter.write("@SP");
                bWriter.newLine();
                bWriter.write("A=M");
                bWriter.newLine();
                bWriter.write("M=D");
                bWriter.newLine();
                bWriter.write("@SP");
                bWriter.newLine();
                bWriter.write("M=M+1");
                bWriter.newLine();
            }

            if (segment.equals("temp")) {
                bWriter.write("@R5");
                bWriter.newLine();
                bWriter.write("D=M");
                bWriter.newLine();
                bWriter.write("@" + (index + 5));
                bWriter.newLine();
                bWriter.write("A=D+A");
                bWriter.newLine();
                bWriter.write("D=M");
                bWriter.newLine();
                bWriter.write("@SP");
                bWriter.newLine();
                bWriter.write("A=M");
                bWriter.newLine();
                bWriter.write("M=D");
                bWriter.newLine();
                bWriter.write("@SP");
                bWriter.newLine();
                bWriter.write("M=M+1");
                bWriter.newLine();
            }

            if (segment.equals("pointer")) {
                if (index == 0) {
                    bWriter.write("@THIS");
                    bWriter.newLine();
                    bWriter.write("D=M");
                    bWriter.newLine();
                }
                if (index == 1) {
                    bWriter.write("@THAT");
                    bWriter.newLine();
                    bWriter.write("D=M");
                    bWriter.newLine();
                }
                bWriter.write("@SP");
                bWriter.newLine();
                bWriter.write("A=M");
                bWriter.newLine();
                bWriter.write("M=D");
                bWriter.newLine();
                bWriter.write("@SP");
                bWriter.newLine();
                bWriter.write("M=M+1");
                bWriter.newLine();
            }
        }

        if (commandType == Parser.CommandType.C_POP) {
            if (segment.equals("local") || segment.equals("argument") ||
                    segment.equals("this") || segment.equals("that")) {
                if (segment.equals("local")) {
                    bWriter.write("@LCL");
                }
                if (segment.equals("argument")) {
                    bWriter.write("@ARG");
                }
                if (segment.equals("this")) {
                    bWriter.write("@THIS");
                }
                if (segment.equals("that")) {
                    bWriter.write("@THAT");
                }
                bWriter.newLine();
                bWriter.write("D=M");
                bWriter.newLine();
                bWriter.write("@" + index);
                bWriter.newLine();
                bWriter.write("D=D+A");
                bWriter.newLine();
                bWriter.write("@R13");
                bWriter.newLine();
                bWriter.write("M=D");
                bWriter.newLine();
                bWriter.write("@SP");
                bWriter.newLine();
                bWriter.write("AM=M-1");
                bWriter.newLine();
                bWriter.write("D=M");
                bWriter.newLine();
                bWriter.write("@R13");
                bWriter.newLine();
                bWriter.write("A=M");
                bWriter.newLine();
                bWriter.write("M=D");
                bWriter.newLine();
            }

            if (segment.equals("static")) {
                bWriter.write("@SP");
                bWriter.newLine();
                bWriter.write("M=M-1");
                bWriter.newLine();
                bWriter.write("A=M");
                bWriter.newLine();
                bWriter.write("D=M");
                bWriter.newLine();
                bWriter.write("@" + this.fileName + "." + index);
                bWriter.newLine();
                bWriter.write("M=D");
                bWriter.newLine();
            }
        
            if (segment.equals("temp")) {
                bWriter.write("@R5");
                bWriter.newLine();
                bWriter.write("D=M");
                bWriter.newLine();
                bWriter.write("@" + (index + 5));
                bWriter.newLine();
                bWriter.write("D=D+A");
                bWriter.newLine(); 
                bWriter.write("@R13"); 
                bWriter.newLine();
                bWriter.write("M=D");
                bWriter.newLine();
                bWriter.write("@SP");
                bWriter.newLine();
                bWriter.write("AM=M-1");
                bWriter.newLine(); 
                bWriter.write("D=M");
                bWriter.newLine(); 
                bWriter.write("@R13");
                bWriter.newLine();
                bWriter.write("A=M");
                bWriter.newLine();
                bWriter.write("M=D");
                bWriter.newLine(); 
            }
        
            if (segment.equals("pointer") && index == 0) {
                bWriter.write("@THIS");
                bWriter.newLine();
                bWriter.write("D=A");
                bWriter.newLine();
                bWriter.write("@R13");
                bWriter.newLine();
                bWriter.write("M=D");
                bWriter.newLine();
                bWriter.write("@SP");
                bWriter.newLine();
                bWriter.write("AM=M-1");
                bWriter.newLine();
                bWriter.write("D=M");
                bWriter.newLine();
                bWriter.write("@R13");
                bWriter.newLine();
                bWriter.write("A=M");
                bWriter.newLine();
                bWriter.write("M=D");
                bWriter.newLine();
            }
        
            if (segment.equals("pointer") && index == 1) {
                bWriter.write("@THAT");
                bWriter.newLine();
                bWriter.write("D=A");
                bWriter.newLine();
                bWriter.write("@R13");
                bWriter.newLine();
                bWriter.write("M=D");
                bWriter.newLine();
                bWriter.write("@SP");
                bWriter.newLine();
                bWriter.write("AM=M-1");
                bWriter.newLine();
                bWriter.write("D=M");
                bWriter.newLine();
                bWriter.write("@R13");
                bWriter.newLine();
                bWriter.write("A=M");
                bWriter.newLine();
                bWriter.write("M=D");
                bWriter.newLine();
            }
        }
    }
    public void close() throws IOException {
        this.bWriter.close();
    }
    
}

