import java.io.InputStream;
import java.io.IOException;

class Parser 
{
    private int lookaheadToken;
    private InputStream in;

    public Parser(InputStream in)throws IOException
    {
        this.in=in;
        lookaheadToken=in.read();
    }

    private void consume(int symbol) throws IOException,ParseError
    {
        if(lookaheadToken!=symbol)
            throw new ParseError();
        lookaheadToken=in.read();
        while(lookaheadToken==' ')
            lookaheadToken=in.read();
    }
    private int expr(int level) throws IOException,ParseError
    {
        int s1=0,s2,s3;
        int flg=0;
        if(lookaheadToken=='(')
        {
            flg+=1;
            consume(lookaheadToken);
            s1=expr(level+1);
            if(lookaheadToken==')')
            {
                consume(lookaheadToken);
                s2=term2(level+1);
                s3=expr2(level+1);
            }
            else throw new ParseError();
        }
        else
        {
            s2=term(level+1);
            s3=expr2(level+1);
        }
        if(flg==1)
        {
            if(s2==-1)
            {
                if(s3==-1)
                {
                    return s1;
                }
                else
                {
                    return s1^s3;
                }
            }
            else
            {
                if(s3==-1)
                {
                    return s1&s2;
                }
                else
                {
                    return (s1&s2)^s3;
                }
            }
        }
        else
        {
            if(s3==-1)
                return s2;
            return (s2^s3);
        }
        
        
    }
    private int expr2(int level) throws IOException,ParseError
    {
        int s1=0,s2,s3;
        int flg=0;
        if(lookaheadToken=='^')
        {
            consume(lookaheadToken);
            if(lookaheadToken=='(')
            {
                flg=1;
                consume(lookaheadToken);
                s1=expr(level+1);
                if(lookaheadToken==')')
                {
                    consume(lookaheadToken);
                    s2=term2(level+1);
                    s3=expr2(level+1);
                }
                else throw new ParseError();     
            }
            else
            {
                s2=term(level+1);
                s3=expr2(level+1);
            }
        }
        else return -1;
        if(flg==1)
        {
            if(s2==-1)
            {
                if(s3==-1)
                {
                    return s1;
                }
                else
                {
                    return s1^s3;
                }
            }   
            else
            {
                if(s3==-1)
                {
                    return s1&s2;
                }
                else
                {
                    return (s1&s2)^s3;
                }

            }
        }
        else
        {
            if(s3==-1)
            {
                return s2;
            }
            else
            {
                return s2^s3;
            }
        }
    }

    private int term(int level) throws IOException, ParseError
    {
        int s1=num(level+1);
        int s2=term2(level+1);
        if(s2==-1)
            return s1;
        return s1&s2;
    }
    private int term2(int level) throws IOException,ParseError
    {
        int s1,s2,s3=0;
        int flg=0;
        if(lookaheadToken=='&')
        {
            consume(lookaheadToken);
            if(lookaheadToken=='(')
            {
                flg=1;
                consume(lookaheadToken);
                s1=expr(level+1);
                if(lookaheadToken==')')
                {
                    consume(lookaheadToken);
                    s2=term2(level+1);
                }
                else throw new ParseError();     
            }
            else
            {
                s1=num(level+1);
                s2=term2(level+1);
            }
        }
        else return -1;
        if(s2==-1)
        {
            return s1;
        }
        else return s1&s2;
    }
    private int num(int level) throws IOException,ParseError
    {
        if(lookaheadToken<'0'||lookaheadToken>'9')
            throw new ParseError();
        int tmp=lookaheadToken;
        consume(lookaheadToken);
        return evalDigit(tmp);
    }
    private int evalDigit(int digit)
    {
        return digit-'0';
    }
    public int eval() throws IOException,ParseError
    {
        int level=0;
        int rv=expr(level);
        if (lookaheadToken != '\n' && lookaheadToken != -1)
	        throw new ParseError();
	    return rv;
    }
    public static void main(String[] args) 
    {
        System.out.println("Please enter an expression to evaluate");
        try
        {
            Parser evaluate=new Parser(System.in);
            System.out.println("Result: "+evaluate.eval());
        }
        catch(IOException e)
        {
            System.err.println(e.getMessage());
        }
        catch(ParseError err)
        {
            System.err.println(err.getMessage());
        }    
    } 
} 