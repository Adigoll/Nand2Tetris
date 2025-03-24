
public class Code {

    public String dest(String destStr) {
        if (destStr == null) return "000";
        if (destStr.equals("M")) return "001";
        if (destStr.equals("D")) return "010";
        if (destStr.equals("DM") || destStr.equals("MD")) return "011";
        if (destStr.equals("A")) return "100";
        if (destStr.equals("AM")) return "101";
        if (destStr.equals("AD")) return "110";
        if (destStr.equals("ADM")) return "111";    
        return null;
    }

    public String comp(String compStr) {
        if (compStr.equals("0")) return "0101010";
        if (compStr.equals("1")) return "0111111";
        if (compStr.equals("-1")) return "0111010";
        if (compStr.equals("D")) return "0001100";
        if (compStr.equals("A")) return "0110000";
        if (compStr.equals("!D")) return "0001101";
        if (compStr.equals("!A")) return "0110001";
        if (compStr.equals("-D")) return "0001111";
        if (compStr.equals("-A")) return "0110011";
        if (compStr.equals("D+1")) return "0011111";
        if (compStr.equals("A+1")) return "0110111";
        if (compStr.equals("D-1")) return "0001110";
        if (compStr.equals("A-1")) return "0110010";
        if (compStr.equals("D+A")) return "0000010";
        if (compStr.equals("D-A")) return "0010011";
        if (compStr.equals("A-D")) return "0000111";
        if (compStr.equals("D&A")) return "0000000";
        if (compStr.equals("D|A")) return "0010101";
        if (compStr.equals("M")) return "1110000";
        if (compStr.equals("!M")) return "1110001";
        if (compStr.equals("-M")) return "1110011";
        if (compStr.equals("M+1")) return "1110111";
        if (compStr.equals("M-1")) return "1110010";
        if (compStr.equals("D+M")) return "1000010";
        if (compStr.equals("D-M")) return "1010011";
        if (compStr.equals("M-D")) return "1000111";
        if (compStr.equals("D&M")) return "1000000";
        if (compStr.equals("D|M")) return "1010101";
        return null;
    }

    public String jump(String jumpStr) {
        if (jumpStr == null) return "000";
        if (jumpStr.equals("JGT")) return "001";
        if (jumpStr.equals("JEQ")) return "010";
        if (jumpStr.equals("JGE")) return "011";
        if (jumpStr.equals("JLT")) return "100";
        if (jumpStr.equals("JNE")) return "101";
        if (jumpStr.equals("JLE")) return "110";
        if (jumpStr.equals("JMP")) return "111";    
        return null;
    }
    
}
