import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Parser {
    private BufferedReader bReader;
    private String currentLine;
    private String nextLine;
    public enum CommandType {
        C_ARITHMETIC, C_PUSH, C_POP, C_LABEL, C_GOTO, C_IF, C_FUNCTION, C_RETURN, C_CALL
    }

    public Parser(File hackFile) throws IOException {
        this.bReader = new BufferedReader(new FileReader(hackFile));
        this.currentLine = this.bReader.readLine();
        String trimLine;
        while (this.currentLine != null) {
            trimLine = this.currentLine.trim();
            if (!trimLine.startsWith("/") && !trimLine.isEmpty()) {
                break;
            }
            this.currentLine = this.bReader.readLine();
        }
        this.nextLine = this.bReader.readLine();
        while (this.nextLine != null) {
            trimLine = this.nextLine.trim();
            if (!trimLine.startsWith("/") && !trimLine.isEmpty()) {
                break;
            }
            this.nextLine = this.bReader.readLine();
        }
    }

    public boolean hasMoreLines() throws IOException {
        return (this.currentLine != null);
    }

    public void advance() throws IOException {
        this.currentLine = this.nextLine;
        String trimLine;
        while (true) {
            this.nextLine = this.bReader.readLine();
            if (this.nextLine == null) {
                break; 
            }
            trimLine = this.nextLine.trim();
            if (!trimLine.startsWith("/") && !trimLine.isEmpty()) {
                break;
            }
       }

    }

    public CommandType commandType() {
        String trimLine = this.currentLine.trim();
        if (trimLine.startsWith("push")) {
            return CommandType.C_PUSH;

        } else if (trimLine.startsWith("pop")) {
            return CommandType.C_POP;

        } else if (trimLine.startsWith("label")) {
            return CommandType.C_LABEL;

        } else if (trimLine.startsWith("goto")) {
            return CommandType.C_GOTO;   

        } else if (trimLine.startsWith("if")) {
            return CommandType.C_IF; 

        } else if (trimLine.startsWith("function")) {
            return CommandType.C_FUNCTION;   

        } else if (trimLine.startsWith("return")) {
            return CommandType.C_RETURN;   

        } else if (trimLine.startsWith("call")) {
            return CommandType.C_CALL;            
        }

        return CommandType.C_ARITHMETIC;
    }

    public String arg1() {
        String trimLine = this.currentLine.trim();
        if (this.commandType() == CommandType.C_ARITHMETIC) {
            return trimLine.split(" ")[0];
        }

        return trimLine.split(" ")[1];
    }

    public int arg2() {
        String trimLine = currentLine.trim();
        trimLine = trimLine.split(" ")[2].trim();
        trimLine = trimLine.split("\t")[0].trim();
        int arg2Return = Integer.parseInt(trimLine);
        return arg2Return;
    }
}