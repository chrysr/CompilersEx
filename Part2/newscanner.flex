/* JFlex example: part of Java language lexer specification */
import java_cup.runtime.*;
/**
%%
/* -----------------Options and Declarations Section----------------- */

/*
   The name of the class JFlex will create will be Lexer.
   Will write the code to the file Lexer.java.
*/
%class Scanner

/*
  The current line number can be accessed with the variable yyline
  and the current column number with the variable yycolumn.
*/
%line
%column

/*
   Will switch to a CUP compatibility mode to interface with a CUP
   generated parser.
*/
%cup

/*
  Declarations

  Code between %{ and %}, both of which must be at the beginning of a
  line, will be copied letter to letter into the lexer class source.
  Here you declare member variables and functions that are used inside
  scanner actions.
*/

%{
StringBuffer stringBuffer = new StringBuffer();
private Symbol symbol(int type) {
   return new Symbol(type, yyline, yycolumn);
}
private Symbol symbol(int type, Object value) {
    return new Symbol(type, yyline, yycolumn, value);
}
%}

/*
  Macro Declarations

  These declarations are regular expressions that will be used latter
  in the Lexical Rules Section.
*/


/* A line terminator is a \r (carriage return), \n (line feed), or
   \r\n. */
LineTerminator = \r|\n|\r\n

/* White space is a line terminator, space, tab, or line feed. */
WhiteSpace     = {LineTerminator} | [ \t\f]

Identifier = [:jletter:][:jletterdigit:]*

String = [\"]([:jletterdigit:]*|[" "]*)*[\"]






%state STRING

%%
/* ------------------------Lexical Rules Section---------------------- */
<YYINITIAL> {
"if"            { return symbol(sym.IF,yytext());    }
 "else"         { return symbol(sym.ELSE,yytext());  }
 "+"            { return symbol(sym.PLUS,yytext());  }
 "("            { return symbol(sym.LPAR,yytext());  }
 ")"            { return symbol(sym.RPAR,yytext());  }
 "{"            { return symbol(sym.LBRA,yytext());  }
 "}"            { return symbol(sym.RBRA,yytext());  }
 ","            { return symbol(sym.COMMA,yytext()); }
 "prefix"       { return symbol(sym.PREF,yytext());  }
 "suffix"       { return symbol(sym.SUF,yytext());   }
 {Identifier}   { return symbol(sym.ID,yytext());    }
 {String}       { return symbol(sym.STRING,yytext());}
{WhiteSpace}    {     /*Nothing Here*/  }
 .              {     /*Nothing Here*/  }
}


/* No token was found for the input so through an error.  Print out an
   Illegal character message with the illegal character that was found. */
[^]                    { throw new Error("Illegal character <"+yytext()+">"); }
