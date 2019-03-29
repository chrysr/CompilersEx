import java_cup.runtime.*;
import java.io.*;

class Main {
    public static void main(String[] argv) throws Exception{
        Parser p = new Parser(new Scanner(new InputStreamReader(System.in)));
        System.out.println("class Output {");
        p.parse();
        System.out.println("\t}\n}");
    }
}
