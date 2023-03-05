package cpp.crz.TextCalc.Tokens;

import java.util.Deque;
import java.util.LinkedList;

public class TokenFactoryDFA
{

    //Alphabets
    private static final char[] zeroDigit = {'0'};
    private static final char[] octalDigits = {'1', '2', '3', '4', '5', '6', '7'};
    private static final char[] nonZeroDigits = {'8', '9'};
    private static final char[] operators = {'+', '-', '/', '*', '^', '(', ')'};
    private static final char[] hexDigits = {'A','B','C','D','E','F','a','b','c','d','e','f'};
    private static final char[] ignore = {'_', ' '};
    private enum States
    {
        PreStart,
        Start,
        Op,
        DecInt,
        Undecided,
        ZeroDecInt,
        OctInt,
        HexInt,
    }
    private LinkedList<Token> tokensList;
    private Deque<Character> currentTokenChars;
    private States factoryState;
    private int stepsTaken;

    public TokenFactoryDFA()
    {
        tokensList = new LinkedList<>();
        currentTokenChars = new LinkedList<>();
        factoryState = States.Start;
        stepsTaken = 0;
    }

    /**
     * Step By Step Loop
     * @param c
     */
    public void takeStep(char c) throws RuntimeException
    {
        stepsTaken++;
        switch(factoryState)
        {
            case Start -> startStateActions(c);
            case Op -> opStateActions(c);
            case DecInt -> decIntStateActions(c);
            case Undecided -> undecidedStateActions(c);
            case ZeroDecInt -> zeroIntStateActions(c);
            case OctInt -> octIntStateActions(c);
            case HexInt -> hexIntStateActions(c);
            default ->
            {
                throw new RuntimeException("State Unrecognized - Catastrophic Failure");
            }
        }
    }

    /**
     * Stop at current state and make a token with the buffer.
     * @param type
     */
    private void bufferToToken(Token.TokenType type)
    {
        Character[] tokenData = new Character[currentTokenChars.size()];
        int i = 0;
        while(!currentTokenChars.isEmpty())
        {
            tokenData[i] = currentTokenChars.poll();
            i++;
        }
        tokensList.add(new Token(type, tokenData));
    }

    private void startStateActions(char c) throws RuntimeException
    {
        if (isZero(c))
        {
            this.factoryState = States.Undecided;
            currentTokenChars.push(c);
        }
        else if (isNonZeroDecimal(c))
        {
            this.factoryState = States.DecInt;
            currentTokenChars.push(c);
        }
        else if(isOperator(c))
        {
            this.factoryState = States.Op;
            currentTokenChars.push(c);
        } else if (isIgnorable(c)) {

        } else
        {
            throw new RuntimeException(String.valueOf(c) + " at start State. Index:" + String.valueOf(stepsTaken));
        }
    }

    private boolean negationCheck()
    {
        if(this.tokensList.size() == 1)
        {
            this.tokensList.getLast().setNegativeINT(true);
            return true;
        }

        if(this.tokensList.size() > 1)
        {
            if (this.tokensList.get(this.tokensList.size() - 2).getTokenData()[0] == '(') {
                this.tokensList.getLast().setNegativeINT(true);
                return true;
            }

            if (isOperator(this.tokensList.get(this.tokensList.size() - 2).getTokenData()[0])) {
                this.tokensList.getLast().setNegativeINT(true);
                return true;
            }
        }
        return false;

    }

    private void opStateActions(char c)
    {
        bufferToToken(Token.TokenType.OPERATOR);
        if (this.tokensList.getLast().getTokenData()[0] == '-')
        {
            negationCheck();
        }
        this.factoryState = States.Start;
        startStateActions(c);
    }

    private void decIntStateActions(char c)
    {
        if(isDecimal(c) || c == '_')
        {
            factoryState = States.DecInt;
            currentTokenChars.push(c);
        }
        else
        {
            bufferToToken(Token.TokenType.DEC_INT);
            factoryState = States.Start;
            startStateActions(c);
        }
    }

    private void hexIntStateActions(char c)
    {
        if(isHex(c)){
            currentTokenChars.push(c);
        } else {
            bufferToToken(Token.TokenType.HEX_INT);
            factoryState = States.Start;
            startStateActions(c);
        }
    }

    private void octIntStateActions(char c)
    {
        if(isOctal(c)){
            currentTokenChars.push(c);
        } else {
            bufferToToken(Token.TokenType.OCT_INT);
            factoryState = States.Start;
            startStateActions(c);
        }
    }

    private void undecidedStateActions(char c) throws RuntimeException
    {
        if(isZero(c))
        {
            factoryState = States.ZeroDecInt;
            currentTokenChars.push(c);
        }
        else if (c == 'O' || c == 'o')
        {
            factoryState = States.OctInt;
            currentTokenChars.push(c);
        }
        else if (c == 'X' || c == 'x')
        {
            factoryState = States.HexInt;
            currentTokenChars.push(c);
        }
        else if (isOperator(c))
        {
            bufferToToken(Token.TokenType.DEC_INT);
            factoryState = States.Start;
            startStateActions(c);
        }
        else
        {
            throw new RuntimeException(c + " at undecided state." + (isDecimal(c) ? " Leading Zeros Invalid" : "" ));
        }

    }
    private void zeroIntStateActions(char c) throws RuntimeException
    {
        if(isZero(c) || c == '_'){
            currentTokenChars.push(c);
        } else {
            bufferToToken(Token.TokenType.DEC_INT);
            factoryState = States.Start;
            startStateActions(c);
        }

    }

    public static boolean isZero(char c)
    {
        return c == '0';
    }
    public static boolean isOctal(char c)
    {
        return isZero(c) || ((c > '0') && (c <= '7'));
    }

    public static boolean isDecimal(char c)
    {
        return isOctal(c) || ((c > '7') && (c <= '9'));
    }

    public static boolean isNonZeroDecimal(char c)
    {
        return !isZero(c) && isDecimal(c);
    }

    public static boolean  isHex(char c)
    {
        return isDecimal(c) || ((c >= 'A')&&( c <= 'F')) || ((c >= 'a')&&(c <= 'f')) ;
    }

    public static boolean  isOperator(char c)
    {
        for (char op : operators)
        {
            if (c == op)
                return true;
        }
        return false;
    }

    public static boolean  isIgnorable(char c)
    {
        for (char op : ignore)
        {
            if (c == op)
                return true;
        }
        return false;
    }

    /**
     * If internal buffer state is in accept state create final token.
     * else trow syntax fail.
     * @return
     */
    public void endInput()
    {
        switch (factoryState)
        {
            case DecInt, ZeroDecInt, Undecided ->
            {
                bufferToToken(Token.TokenType.DEC_INT);
            }
            case OctInt ->
            {
                bufferToToken(Token.TokenType.OCT_INT);
            }
            case HexInt ->
            {
                bufferToToken(Token.TokenType.HEX_INT);
            }
            case Op ->
            {
                bufferToToken(Token.TokenType.OPERATOR);
            }
            default ->
            {
                bufferToToken(Token.TokenType.ERROR);
            }
        }
    }

    public LinkedList<Token> getTokensList()
    {
        return this.tokensList;
    }

    public void reset()
    {
        this.factoryState = States.Start;
        this.tokensList = new LinkedList<>();
        this.currentTokenChars = new LinkedList<>();
        this.stepsTaken = 0;
    }

}
