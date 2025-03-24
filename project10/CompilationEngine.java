import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CompilationEngine {
    private FileWriter fWriter;
    private JackTokenizer jTokenizer;
    private boolean isFirstRoutine;
    private boolean hasSubRoutines;

    public CompilationEngine(File inputFile, File outputFile) {
        try {
            fWriter = new FileWriter(outputFile);
            jTokenizer = new JackTokenizer(inputFile);
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
        isFirstRoutine = true;
    }

    // compiles a complete class
    public void compileClass() {
        try {
            jTokenizer.advance();
            fWriter.write("<class>\n");
            fWriter.write("<keyword> class </keyword>\n");
            jTokenizer.advance();
            fWriter.write("<identifier> " + jTokenizer.identifier() + " </identifier>\n");
            jTokenizer.advance();
            fWriter.write("<symbol> { </symbol>\n");
            compileClassVarDec();
            compileSubRoutine();
            fWriter.write("<symbol> } </symbol>\n");
            fWriter.write("</class>\n");
            fWriter.close();
        } 
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    // compiles a static declaration or a field declaration
    public void compileClassVarDec() {
        jTokenizer.advance();
        try {
            while (jTokenizer.keyWord().equals("static") || jTokenizer.keyWord().equals("field")) {
                fWriter.write("<classVarDec>\n");
                fWriter.write("<keyword> " + jTokenizer.keyWord() + " </keyword>\n");
                jTokenizer.advance();
                if (jTokenizer.tokenType().equals("IDENTIFIER")) {
                    fWriter.write("<identifier> " + jTokenizer.identifier() + " </identifier>\n");
                }
                else {
                    fWriter.write("<keyword> " + jTokenizer.keyWord() + " </keyword>\n");
                }
                jTokenizer.advance();
                //third word of the class var declaraion
                fWriter.write("<identifier> " + jTokenizer.identifier() + " </identifier>\n");
                jTokenizer.advance();
                if (jTokenizer.symbol() == ',') {
                    fWriter.write("<symbol> , </symbol>\n");
                    jTokenizer.advance();
                    fWriter.write(("<identifier> " + jTokenizer.identifier() + " </identifier>\n"));
                    jTokenizer.advance();
                }
                fWriter.write("<symbol> ; </symbol>\n");
                jTokenizer.advance();
                fWriter.write("</classVarDec>\n");
            }
            // if reach a subroutine, go back in the arraylist to adjust for advance in the next call
            if (jTokenizer.keyWord().equals("function") || jTokenizer.keyWord().equals("method") || jTokenizer.keyWord().equals("constructor")) {
                jTokenizer.decrementPointer();
                return;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // compiles a complete method, function, or a constructor
    public void compileSubRoutine() {
        hasSubRoutines = false;

        jTokenizer.advance();

        try {
            // if reach the end, return (no more subroutines, base case)
            if (jTokenizer.symbol() == '}' && jTokenizer.tokenType().equals("SYMBOL")) {
                return;
            }
            // subroutine declaration tag
            if ((isFirstRoutine) && (jTokenizer.keyWord().equals("function") || jTokenizer.keyWord().equals("method") || jTokenizer.keyWord().equals("constructor"))) {
                isFirstRoutine = false;
                fWriter.write("<subroutineDec>\n");
                hasSubRoutines = true;
            }
            // if function or method or constructor
            if (jTokenizer.keyWord().equals("function") || jTokenizer.keyWord().equals("method") || jTokenizer.keyWord().equals("constructor")) {
                hasSubRoutines = true;
                fWriter.write("<keyword> " + jTokenizer.keyWord() + " </keyword>\n");
                jTokenizer.advance();
            }
            // if there is an identifier in the subroutine statement
            if (jTokenizer.tokenType().equals("IDENTIFIER")) {
                fWriter.write("<identifier> " + jTokenizer.identifier() + " </identifier>\n");
                jTokenizer.advance();
            }
            // if keyword in the subroutine statement
            else if (jTokenizer.tokenType().equals("KEYWORD")) {
                fWriter.write("<keyword> " + jTokenizer.keyWord() + " </keyword>\n");
                jTokenizer.advance();
            }
            if (jTokenizer.tokenType().equals("IDENTIFIER")) {
                fWriter.write("<identifier> " + jTokenizer.identifier() + " </identifier>\n");
                jTokenizer.advance();
            }
            // get parameters (if there are)
            if (jTokenizer.symbol() == '(') {
                fWriter.write("<symbol> ( </symbol>\n");
                fWriter.write("<parameterList>\n");

                compileParameterList();
                fWriter.write("</parameterList>\n");
                fWriter.write("<symbol> ) </symbol>\n");
            }
            jTokenizer.advance();

            compileSubRoutineBody();
            
            // recursive call
            compileSubRoutine();
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // compiles a (possibly empty) parameter list including the "()"
    public void compileParameterList() {
        jTokenizer.advance();
        try {
            while (!(jTokenizer.tokenType().equals("SYMBOL") && jTokenizer.symbol() == ')')) {
                if (jTokenizer.tokenType().equals("IDENTIFIER")) {
                    fWriter.write("<identifier> " + jTokenizer.identifier() + " </identifier>\n");
                    jTokenizer.advance();
                } 
                else if (jTokenizer.tokenType().equals("KEYWORD")) {
                    fWriter.write("<keyword> " + jTokenizer.keyWord() + " </keyword>\n");
                    jTokenizer.advance();
                }
                // commas separate the list, if there are multiple
                else if ((jTokenizer.tokenType().equals("SYMBOL")) && (jTokenizer.symbol() == ',')) {
                    fWriter.write("<symbol> , </symbol>\n");
                    jTokenizer.advance();
                }
            }
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void compileSubRoutineBody() {
        try {
            if (jTokenizer.symbol() == '{') {
                fWriter.write("<subroutineBody>\n");
                fWriter.write("<symbol> { </symbol>\n");
                jTokenizer.advance();
            }
            // gets all var declarations in the subroutine
            while (jTokenizer.keyWord().equals("var") && (jTokenizer.tokenType().equals("KEYWORD"))) {
                fWriter.write("<varDec>\n ");
                jTokenizer.decrementPointer();
                compileVarDec();
                fWriter.write(" </varDec>\n");
            }
            fWriter.write("<statements>\n");
            compileStatements();
            fWriter.write("</statements>\n");
            fWriter.write("<symbol> " + jTokenizer.symbol() + " </symbol>\n");
            if (hasSubRoutines) {
                fWriter.write("</subroutineBody>\n");
                fWriter.write("</subroutineDec>\n");
                isFirstRoutine = true;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // compiles a var declaration
    public void compileVarDec() {
        jTokenizer.advance();
        try {
            if (jTokenizer.keyWord().equals("var") && (jTokenizer.tokenType().equals("KEYWORD"))) {
                fWriter.write("<keyword> var </keyword>\n");
                jTokenizer.advance();
            }
            // if type of var is identifier
            if (jTokenizer.tokenType().equals("IDENTIFIER")) {
                fWriter.write("<identifier> " + jTokenizer.identifier() + " </identifier>\n");
                jTokenizer.advance();
            }
            // if type of var is keyword
            else if (jTokenizer.tokenType().equals("KEYWORD")) {
                fWriter.write("<keyword> " + jTokenizer.keyWord() + " </keyword>\n");
                jTokenizer.advance();
            }
            // name of var
            if (jTokenizer.tokenType().equals("IDENTIFIER")) {
                fWriter.write("<identifier> " + jTokenizer.identifier() + " </identifier>\n");
                jTokenizer.advance();
            }
            // if there are mutliple vars in 1 line
            if ((jTokenizer.tokenType().equals("SYMBOL")) && (jTokenizer.symbol() == ',')) {
                fWriter.write("<symbol> , </symbol>\n");
                jTokenizer.advance();
                fWriter.write(("<identifier> " + jTokenizer.identifier() + " </identifier>\n"));
                jTokenizer.advance();
            }
            // end of line
            if ((jTokenizer.tokenType().equals("SYMBOL")) && (jTokenizer.symbol() == ';')) {
                fWriter.write("<symbol> ; </symbol>\n");
                jTokenizer.advance();

            }
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // compiles a sequence of statements
    public void compileStatements() {
        try {
            if (jTokenizer.symbol() == '}' && (jTokenizer.tokenType().equals("SYMBOL"))) {
                return;
            }
            else if (jTokenizer.keyWord().equals("let") && (jTokenizer.tokenType().equals("KEYWORD"))) {
                fWriter.write("<letStatement>\n ");
                compileLet();
                fWriter.write((" </letStatement>\n"));
            } 
            else if (jTokenizer.keyWord().equals("if") && (jTokenizer.tokenType().equals("KEYWORD"))) {
                fWriter.write("<ifStatement>\n ");
                compileIf();
                fWriter.write((" </ifStatement>\n"));
            } 
            else if (jTokenizer.keyWord().equals("while") && (jTokenizer.tokenType().equals("KEYWORD"))) {
                fWriter.write("<whileStatement>\n ");
                compileWhile();
                fWriter.write((" </whileStatement>\n"));
            } 
            else if (jTokenizer.keyWord().equals("do") && (jTokenizer.tokenType().equals("KEYWORD"))) {
                fWriter.write("<doStatement>\n ");
                compileDo();
                fWriter.write((" </doStatement>\n"));
            } 
            else if (jTokenizer.keyWord().equals("return") && (jTokenizer.tokenType().equals("KEYWORD"))) {
                fWriter.write("<returnStatement>\n ");
                compileReturn();
                fWriter.write((" </returnStatement>\n"));
            }
            jTokenizer.advance();

            compileStatements();
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // compiles a let statement
    public void compileLet() {
        try {
            fWriter.write("<keyword> " + jTokenizer.keyWord() + " </keyword>\n");
            jTokenizer.advance();
            fWriter.write("<identifier> " + jTokenizer.identifier() + " </identifier>\n");
            jTokenizer.advance();
            if ((jTokenizer.tokenType().equals("SYMBOL")) && (jTokenizer.symbol() == '[')) {
                fWriter.write("<symbol> " + jTokenizer.symbol() + " </symbol>\n");
                compileExpression();
                jTokenizer.advance();
                if ((jTokenizer.tokenType().equals("SYMBOL")) && ((jTokenizer.symbol() == ']'))) {
                    fWriter.write("<symbol> " + jTokenizer.symbol() + " </symbol>\n");
                }
                // only advance if there is an expression
                jTokenizer.advance();

            }
            // = sign
            fWriter.write("<symbol> " + jTokenizer.symbol() + " </symbol>\n");
            compileExpression();
            fWriter.write("<symbol> " + jTokenizer.symbol() + " </symbol>\n");
            jTokenizer.advance();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // compiles an if statement, possibly with a trailing else clause
    public void compileIf() {
        try {
            fWriter.write("<keyword> if </keyword>\n");
            jTokenizer.advance();
            fWriter.write("<symbol> ( </symbol>\n");
            // expression in if () condition
            compileExpression();
            fWriter.write("<symbol> ) </symbol>\n");
            jTokenizer.advance();
            fWriter.write("<symbol> { </symbol>\n");
            jTokenizer.advance();
            fWriter.write("<statements>\n");
            // compile statements in if clause { }
            compileStatements();
            fWriter.write("</statements>\n");
            fWriter.write("<symbol> } </symbol>\n");
            jTokenizer.advance();
            // if there is an else clause of the if statement
            if (jTokenizer.tokenType().equals("KEYWORD") && jTokenizer.keyWord().equals("else")) {
                fWriter.write("<keyword> else </keyword>\n");
                jTokenizer.advance();
                fWriter.write("<symbol> { </symbol>\n");
                jTokenizer.advance();
                fWriter.write("<statements>\n");
                // compile statements within else clause
                compileStatements();
                fWriter.write("</statements>\n");
                fWriter.write("<symbol> } </symbol>\n");
            } 
            else {
                // keep pointer correct
                jTokenizer.decrementPointer();
            }
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // compiles a while statement
    public void compileWhile() {
        try {
            // while
            fWriter.write("<keyword> " + jTokenizer.keyWord() + " </keyword>\n");
            jTokenizer.advance();
            // (
            fWriter.write("<symbol> " + jTokenizer.symbol() + " </symbol>\n");
            // compile inside of () - expression
            compileExpression();
            // )
            jTokenizer.advance();
            fWriter.write("<symbol> " + jTokenizer.symbol() + " </symbol>\n");
            jTokenizer.advance();
            // {
            fWriter.write("<symbol> " + jTokenizer.symbol() + " </symbol>\n");
            // in  while statement
            fWriter.write("<statements>\n");
            compileStatements();
            fWriter.write("</statements>\n");
            // }
            fWriter.write("<symbol> " + jTokenizer.symbol() + " </symbol>\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // compiles a do statement
    public void compileDo() {
        try {
            if (jTokenizer.keyWord().equals("do")) {
                fWriter.write("<keyword> do </keyword>\n");
            }
            compileCall();
            jTokenizer.advance();
            fWriter.write("<symbol> " + jTokenizer.symbol() + " </symbol>\n");
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // compiles a return statement
    public void compileReturn() {
        try {
            fWriter.write("<keyword> return </keyword>\n");
            jTokenizer.advance();
            if (!((jTokenizer.tokenType().equals("SYMBOL") && jTokenizer.symbol() == ';'))) {
                jTokenizer.decrementPointer();
                compileExpression();
            }
            if (jTokenizer.tokenType().equals("SYMBOL") && jTokenizer.symbol() == ';') {
                fWriter.write("<symbol> ; </symbol>\n");
            }
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // compiles an expression
    public void compileExpression() {
        try {
            fWriter.write("<expression>\n");
            compileTerm();
            while (true) {
                jTokenizer.advance();
                if (jTokenizer.tokenType().equals("SYMBOL") && jTokenizer.isOperation()) {
                    // < > & = have different xml code
                    if (jTokenizer.symbol() == '<') {
                        fWriter.write("<symbol> &lt; </symbol>\n");
                    } 
                    else if (jTokenizer.symbol() == '>') {
                        fWriter.write("<symbol> &gt; </symbol>\n");
                    } 
                    else if (jTokenizer.symbol() == '&') {
                        fWriter.write("<symbol> &amp; </symbol>\n");
                    } 
                    else {
                        fWriter.write("<symbol> " + jTokenizer.symbol() + " </symbol>\n");
                    }
                    compileTerm();
                } 
                else {
                    jTokenizer.decrementPointer();
                    break;
                }
            }
            fWriter.write("</expression>\n");
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // compiles a term. If current token is an identifier, must resolve it into a variable, an array entry, or a subroutine call
    public void compileTerm() {
        try {
            fWriter.write("<term>\n");
            jTokenizer.advance();
            if (jTokenizer.tokenType().equals("IDENTIFIER")) {
                String prevIdentifier = jTokenizer.identifier();
                jTokenizer.advance();
                if (jTokenizer.tokenType().equals("SYMBOL") && jTokenizer.symbol() == '[') {
                    fWriter.write("<identifier> " + prevIdentifier + " </identifier>\n");
                    fWriter.write("<symbol> [ </symbol>\n");
                    compileExpression();
                    jTokenizer.advance();
                    fWriter.write("<symbol> ] </symbol>\n");
                }
                else if (jTokenizer.tokenType().equals("SYMBOL") && (jTokenizer.symbol() == '(' || jTokenizer.symbol() == '.')) {
                    jTokenizer.decrementPointer();
                    jTokenizer.decrementPointer();
                    compileCall();
                } 
                else {
                    fWriter.write("<identifier> " + prevIdentifier + " </identifier>\n");
                    jTokenizer.decrementPointer();
                }
            } else {
                // integer
                if (jTokenizer.tokenType().equals("INT_CONST")) {
                    fWriter.write("<integerConstant> " + jTokenizer.intVal() + " </integerConstant>\n");

                }
                // strings
                else if (jTokenizer.tokenType().equals("STRING_CONST")) {
                    fWriter.write("<stringConstant> " + jTokenizer.stringVal() + " </stringConstant>\n");
                }
                // this true null or false
                else if (jTokenizer.tokenType().equals("KEYWORD") && (jTokenizer.keyWord().equals("this") || jTokenizer.keyWord().equals("null")
                        || jTokenizer.keyWord().equals("false") || jTokenizer.keyWord().equals("true"))) {
                    fWriter.write("<keyword> " + jTokenizer.keyWord() + " </keyword>\n");
                }
                else if (jTokenizer.tokenType().equals("SYMBOL") && jTokenizer.symbol() == '(') {
                    fWriter.write("<symbol> " + jTokenizer.symbol() + " </symbol>\n");
                    compileExpression();
                    jTokenizer.advance();
                    fWriter.write("<symbol> " + jTokenizer.symbol() + " </symbol>\n");
                }
                // unary operators
                else if (jTokenizer.tokenType().equals("SYMBOL") && (jTokenizer.symbol() == '-' || jTokenizer.symbol() == '~')) {
                    fWriter.write("<symbol> " + jTokenizer.symbol() + " </symbol>\n");
                    compileTerm();
                }
            }
            fWriter.write("</term>\n");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // compiles (possibly empty) comma separated list of expressions
    public void compileExpressionList() {
        jTokenizer.advance();
        // end of list
        if (jTokenizer.symbol() == ')' && jTokenizer.tokenType().equals("SYMBOL")) {
            jTokenizer.decrementPointer();
        } else {
            jTokenizer.decrementPointer();
            compileExpression();
        }
        while (true) {
            jTokenizer.advance();
            if (jTokenizer.tokenType().equals("SYMBOL") && jTokenizer.symbol() == ',') {
                try {
                    fWriter.write("<symbol> , </symbol>\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                compileExpression();
            } 
            else {
                jTokenizer.decrementPointer();
                break;
            }
        }
    }

    private void compileCall() {
        jTokenizer.advance();
        try {
            fWriter.write("<identifier> " + jTokenizer.identifier() + " </identifier>\n");
            jTokenizer.advance();
            if ((jTokenizer.tokenType().equals("SYMBOL")) && (jTokenizer.symbol() == '.')) {
                fWriter.write("<symbol> " + jTokenizer.symbol() + " </symbol>\n");
                jTokenizer.advance();
                fWriter.write("<identifier> " + jTokenizer.identifier() + " </identifier>\n");
                jTokenizer.advance();
                fWriter.write("<symbol> " + jTokenizer.symbol() + " </symbol>\n");
                fWriter.write("<expressionList>\n");
                compileExpressionList();
                fWriter.write("</expressionList>\n");
                jTokenizer.advance();
                fWriter.write("<symbol> " + jTokenizer.symbol() + " </symbol>\n");
            }
            else if ((jTokenizer.tokenType().equals("SYMBOL")) && (jTokenizer.symbol() == '(')) {
                fWriter.write("<symbol> " + jTokenizer.symbol() + " </symbol>\n");
                fWriter.write("<expressionList>\n");
                compileExpressionList();
                fWriter.write("</expressionList>\n");
                jTokenizer.advance();
                fWriter.write("<symbol> " + jTokenizer.symbol() + " </symbol>\n");
            }
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}