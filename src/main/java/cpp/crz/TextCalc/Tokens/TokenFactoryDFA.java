package cpp.crz.TextCalc.Tokens;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

public class TokenFactoryDFA
{

    //Alphabets
    private static final char[] zeroDigit = {'0'};
    private static final char[] octalDigits = {'1', '2', '3', '4', '5', '6', '7'};
    private static final char[] nonZeroDigits = {'8', '9'};
    private static final char[] operators = {'+', '-', '/', '*'};
    private static final char[] hexDigits = {'A','B','C','D','E','F','a','b','c','d','e','f'};
    private static final char[] ignore = {'_'};

    private enum States{
        PreStart,
        Start,
        Op,
        DecInt,
        Undecided,
        ZeroDecInt,
        OctInt,
        HexInt,
        Close
    }
    private LinkedList<Token> tokensList;
    private Deque<Character> currentTokenChars;
    private States factoryState;

    public TokenFactoryDFA()
    {
        tokensList = new LinkedList<>();
        currentTokenChars = new LinkedList<>();
        factoryState = States.Start;
    }

    /**
     * Step By Step Loop
     * @param c
     */
    public void takeStep(char c)
    {
        switch(factoryState)
        {
            case PreStart ->
            {
                preStartStateActions(c);
            }
            case Start ->
            {
                startStateActions(c);
            }
            case Op ->
            {
                opStateActions(c);
            }
            case DecInt ->
            {
                decIntStateActions(c);
            }
            case Undecided ->
            {
                undecidedStateActions(c);
            }
            case ZeroDecInt ->
            {
                zeroIntStateActions(c);
            }
            case OctInt ->
            {
                octIntStateActions(c);
            }
            case HexInt ->
            {
                hexIntStateActions(c);
            }
            case Close ->
            {
                closeStateActions(c);
            }
        }
    }

    private void preStartStateActions(char c)
    {

    }
    private void startStateActions(char c)
    {
        if (isZero(c))
        {

        }
        else if (isNonZeroDecimal(c))
        {

        }
        else if(isOperator(c))
        {
            this.factoryState = States.Op;
            currentTokenChars.push(c);
        }
        else
        {

        }
    }
    private void opStateActions(char c)
    {


    }
    private void decIntStateActions(char c)
    {

    }
    private void hexIntStateActions(char c)
    {

    }
    private void octIntStateActions(char c)
    {

    }
    private void undecidedStateActions(char c)
    {

    }
    private void zeroIntStateActions(char c)
    {

    }
    private void closeStateActions(char c)
    {

    }

    private boolean isZero(char c)
    {
        return c == '0';
    }
    private boolean isOctal(char c)
    {
        return isZero(c) || ((c > '0') && (c <= '7'));
    }

    private boolean isDecimal(char c)
    {
        return isOctal(c) || ((c > '7') && (c <= '9'));
    }

    private boolean isNonZeroDecimal(char c)
    {
        return !isZero(c) && isDecimal(c);
    }

    private boolean isHex(char c)
    {
        return isDecimal(c) || ((c >= 'A')&&( c <= 'F')) || ((c >= 'a')&&(c <= 'f')) ;
    }

    private boolean isOperator(char c)
    {
        for (char op : operators)
        {
            if (c == op)
                return true;
        }
        return false;
    }

    private boolean shouldIgnore(char c)
    {
        for (char ig : ignore)
        {
            if (c == ig)
                return true;
        }
        return false;
    }

    /**
     * If internal buffer state is in accept state create final token.
     * else trow syntax fail.
     * @return
     */
    public boolean endInput()
    {

        return false;
    }




}
