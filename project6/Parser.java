
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class Parser {

    private BufferedReader bReader;
    private String currentLine;
    private String nextLine;

    public enum InstructionType {
        A_INSTRUCTION, C_INSTRUCTION, L_INSTRUCTION
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

    public InstructionType instructionType() {
        String trimLine = this.currentLine.trim();
        if (trimLine.startsWith("@")) {
            return InstructionType.A_INSTRUCTION;
        } 
        if (trimLine.startsWith("(") && this.currentLine.endsWith(")")) {
            return InstructionType.L_INSTRUCTION;
        }
        return InstructionType.C_INSTRUCTION;
    }

    public String symbol() {
        String trimLine = this.currentLine.trim();
        if (trimLine.startsWith("@")) {
            return trimLine.substring(1);
        }
        if (trimLine.startsWith("(")) {
            return trimLine.substring(1, (trimLine.length() - 1));
        }
        return null;
    }

    public String dest() {
        String trimLine = this.currentLine.trim();
		int eqIndex = trimLine.indexOf("=");
        if (eqIndex == -1) return null;
		return trimLine.substring(0, eqIndex);
    }

    public String comp() {
        String trimLine = this.currentLine.trim();
		int eqIndex = trimLine.indexOf("=");
        int scIndex = trimLine.indexOf(";");
        if (eqIndex != -1) {
            if (scIndex != -1) {
                return trimLine.substring(eqIndex + 1, scIndex); 
            } else {
                return trimLine.substring(eqIndex + 1); 
            }
        } else {
            if (scIndex != -1) {
                return trimLine.substring(0, scIndex); 
            }
        }
        return trimLine; 
    }

    public String jump() {
        String trimLine = this.currentLine.trim();
        int scIndex = trimLine.indexOf(";");
        if (scIndex == -1) return null;
		return trimLine.substring(scIndex + 1);
    }
    
}
