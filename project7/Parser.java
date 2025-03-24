
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Parser {
    
    private BufferedReader bReader;
    private String currentLine;
    private String nextLine;

    public enum CommandType {
        C_ARITHMETIC, C_PUSH, C_POP
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
        if (trimLine.startsWith("pop")) return CommandType.C_POP;
        if (trimLine.startsWith("push")) return CommandType.C_PUSH;
        return CommandType.C_ARITHMETIC;
    }

    public String arg1() {
        String trimLine = this.currentLine.trim();
        if (this.commandType() == CommandType.C_ARITHMETIC) {
            return trimLine.split(" ")[0];
        }
        return trimLine.split(" ")[1];
    }

    public String arg2() {
        String trimLine = this.currentLine.trim();
        return trimLine.split(" ")[2];
    }

}
