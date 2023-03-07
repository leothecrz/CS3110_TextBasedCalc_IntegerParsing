package cpp.crz.TextCalc.Tokens;

import java.util.Deque;
import java.util.LinkedList;

public class TokenFactoryNFA
{
    //Alphabets
    private static final char[] zeroDigit = {'0'};
    private static final char[] octalDigits = {'1', '2', '3', '4', '5', '6', '7'};
    private static final char[] nonZeroDigits = {'8', '9'};
    private static final char[] operators = {'+', '-', '/', '*', '^', '(', ')'};
    private static final char[] hexDigits = {'A','B','C','D','E','F','a','b','c','d','e','f'};
    private static final char[] ignore = {'_', ' '};
    public enum States
    {
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

    /**
     * Constructor
     */
    public TokenFactoryNFA()
    {
        this.reset();
    }

    /**
     * Take one step in the string parse.
     * Feed every char from a string to function.
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
                //TODO: TRANSITIONED INTO A NO STATE CONDITION
                System.err.println("Should Not Be Possible");
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

    /**
     * Possible actions from start state
     * @param c
     * @throws RuntimeException
     */
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

    /**
     * Evaluates a '-' char to discover if it is a negation or subtraction operator.
     * @return True - when the last read token was a negation.
     */
    private boolean negationCheck()
    {
        if(this.tokensList.size() == 1)
        {
            this.tokensList.getLast().setNegativeFlag(true);
            return true;
        }

        if(this.tokensList.size() > 1)
        {
            if (this.tokensList.get(this.tokensList.size() - 2).getTokenData()[0] == '(') {
                this.tokensList.getLast().setNegativeFlag(true);
                return true;
            }

            if (isOperator(this.tokensList.get(this.tokensList.size() - 2).getTokenData()[0])) {
                if(this.tokensList.get(this.tokensList.size() - 2).getTokenData()[0] == ')')
                    return false;
                this.tokensList.getLast().setNegativeFlag(true);
                return true;
            }
        }
        return false;

    }

    /**
     * possible actions from OP state
     * @param c
     */
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

    /**
     * Decimal Number Reading State
     * @param c
     */
    private void decIntStateActions(char c)
    {
        if(isDecimal(c))
        {
            factoryState = States.DecInt;
            currentTokenChars.push(c);
        }
        else if ( isIgnorable(c) )
        {}
        else
        {
            bufferToToken(Token.TokenType.DEC_INT);
            factoryState = States.Start;
            startStateActions(c);
        }
    }

    /**
     * Hexadecimal Number Reading State
     * @param c
     */
    private void hexIntStateActions(char c)
    {
        if(isHex(c)){
            this.factoryState = States.HexInt;
            currentTokenChars.push(c);
        }
        else if ( isIgnorable(c) )
        {}
        else
        {
            bufferToToken(Token.TokenType.HEX_INT);
            factoryState = States.Start;
            startStateActions(c);
        }
    }

    /**
     * Octagonal Numbers Reading State
     * @param c
     */
    private void octIntStateActions(char c)
    {
        if(isOctal(c)){
            factoryState = States.OctInt;
            currentTokenChars.push(c);
        }
        else if ( isIgnorable(c) )
        {

        }
        else
        {
            bufferToToken(Token.TokenType.OCT_INT);
            factoryState = States.Start;
            startStateActions(c);
        }
    }

    /**
     * Number Type Unselected State
     * @param c
     * @throws RuntimeException
     */
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
        else if (isIgnorable(c))
        {

        }
        else
        {
            throw new RuntimeException(c + " at undecided state." + (isDecimal(c) ? " Leading Zeros Invalid" : "" ));
        }

    }

    /**
     * Decimal number made of only zeros. Leading zeros are not allowed for non zero numbers.
     * @param c
     * @throws RuntimeException
     */
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

    /**
     * @param c - char to check
     * @return if is a zero.
     */
    public static boolean isZero(char c)
    {
        return c == '0';
    }

    /**
     * @param c - char to check
     * @return if c is a zero or an octagonal number. (0-7)
     */
    public static boolean isOctal(char c)
    {
        return isZero(c) || ((c > '0') && (c <= '7'));
    }

    /**
     * Uses previous smallest alphabet to build next.
     * @param c - char to check
     * @return if c is an octagonal number or 8 or 9.
     */
    public static boolean isDecimal(char c)
    {
        return isOctal(c) || ((c > '7') && (c <= '9'));
    }

    /**
     * @param c - char to check
     * @return if c is a non zero decimal number
     */
    public static boolean isNonZeroDecimal(char c)
    {
        return !isZero(c) && isDecimal(c);
    }

    /**
     * @param c - char to check
     * @return if c is a decimal number or contains a hex value (a-f)
     */
    public static boolean  isHex(char c)
    {
        return isDecimal(c) || ((c >= 'A')&&( c <= 'F')) || ((c >= 'a')&&(c <= 'f')) ;
    }

    /**
     * @param c - char to check
     * @return if is in the operator alphabet
     */
    public static boolean  isOperator(char c)
    {
        for (char op : operators)
        {
            if (c == op)
                return true;
        }
        return false;
    }

    /**
     * @param c - char to check
     * @return if is in the ignorable chars alphabet.
     */
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
    public void endInput() throws RuntimeException
    {
        switch (factoryState)
        {
            case DecInt, ZeroDecInt, Undecided ->
                bufferToToken(Token.TokenType.DEC_INT);

            case OctInt ->
                bufferToToken(Token.TokenType.OCT_INT);

            case HexInt ->
                bufferToToken(Token.TokenType.HEX_INT);

            case Op ->
                bufferToToken(Token.TokenType.OPERATOR);

            default ->
            {
                bufferToToken(Token.TokenType.ERROR);
                //TODO:: WHAT HAPPENS WITH ERROR TOKEN
                System.err.println(this.tokensList.getLast());
                throw new RuntimeException("ERROR TOKEN CREATED.");
            }

        }
    }

    public LinkedList<Token> getTokensList()
    {
        return this.tokensList;
    }

    public States getFactoryState()
    {
        return this.factoryState;
    }

    /**
     * Reset Factory.
     * Clear All buffers.
     */
    public void reset()
    {
        this.factoryState = States.Start;
        this.tokensList = new LinkedList<>();
        this.currentTokenChars = new LinkedList<>();
        this.stepsTaken = 0;
    }

    /**
     *
     * @param str
     * @param debug
     * @return
     */
    public static LinkedList<Token> parseString(String str, boolean debug)
    {
        try
        {
            TokenFactoryNFA factoryDFA = new TokenFactoryNFA();
            for(int i=0; i<str.length(); i++)
            {
                factoryDFA.takeStep(str.charAt(i));
            }
            factoryDFA.endInput();

            if(debug)
            {
                for (int i = 0; i < factoryDFA.getTokensList().size(); i++) {
                    System.out.println(factoryDFA.getTokensList().get(i));
                } System.out.println("      String To Token END \n");
            }
            return factoryDFA.getTokensList();
        }
        catch (RuntimeException RE)
        {
            System.out.println(RE.getMessage());

            return null;
        }

    }


}
