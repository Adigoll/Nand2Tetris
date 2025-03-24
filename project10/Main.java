import java.io.File;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        // input is the jack file
        File jackFileDir = new File(args[0]);
        ArrayList<File> files = new ArrayList<>();

        if (jackFileDir.isFile() && args[0].endsWith(".jack")) {
            files.add(jackFileDir);
        } 
        else if (jackFileDir.isDirectory()) {
            File[] arrFiles = jackFileDir.listFiles();
            if (arrFiles != null) {
                for (File file : arrFiles) {
                    if (file.getName().endsWith(".jack")) files.add(file);
                }
            }
        }
        else {
            System.out.println("No valid .jack file or directory");
            return;
        }

        // Process each file
        for (File file : files) {
            try {
                String outputFileName = file.toString().substring(0, file.toString().length() - 5) + ".xml";
                File outputFile = new File(outputFileName);
                CompilationEngine compilationEngine = new CompilationEngine(file, outputFile);
                compilationEngine.compileClass();
                System.out.println("Processed: " + file.getName());
            } 
            catch (Exception e) {
                System.err.println("Error processing file " + file.getName());
                e.printStackTrace();
            }
        }
    }
}