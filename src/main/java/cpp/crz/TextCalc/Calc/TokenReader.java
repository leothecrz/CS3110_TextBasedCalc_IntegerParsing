package cpp.crz.TextCalc.Calc;

import com.sun.jdi.Value;
import cpp.crz.TextCalc.Tokens.Token;
import cpp.crz.TextCalc.Tokens.TokenFactoryDFA;

import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.Stack;

public class TokenReader {

    private LinkedList<Token> tokenLinkedList;

    Stack<Token> memory = new Stack<>();

    private Token NumOne;
    private Token NumTwo;
    private Token Operator;

    /**
     *
     * @param tokenList
     */
    public TokenReader(LinkedList<Token> tokenList)
    {
        tokenLinkedList = new LinkedList<>();
        toPostFix(tokenList);

        for(int i=0; i< tokenLinkedList.size(); i++)
        {
            System.out.println(tokenLinkedList.get(i));
        } System.out.println("      PostFix Operation Done");

    }

    public Stack<Token> runCalculation() throws RuntimeException
    {

        memory = new Stack<>();
        Token currentTK;

        while( (currentTK = this.tokenLinkedList.pollLast()) != null )
        {
            if(currentTK.getTokenType() == Token.TokenType.OPERATOR)
            {
                operatorCalculation(currentTK);
            }
            else
            {
                memory.push(currentTK);
            }
        }

        return memory;

    }

    private void operatorCalculation(Token tk) throws RuntimeException
    {
        long sum = 0;

        switch (tk.getTokenData()[0])
        {
            case '+' ->
            {
                sum = additionCalculation(tk);
            }
            case '-' ->
            {
                if(tk.isNegativeINT())
                {
                    sum = negationCalculation(tk);
                }
                else
                {
                    sum = subtractionCalculation(tk);
                }
            }
            case '/' ->
            {
                sum = divisionCalculation(tk);
            }
            case '*' ->
            {
                sum = multiplicationCalculation(tk);
            }
            case '^' ->
            {
                sum = powerCalculation(tk);
            }
        }

        TokenFactoryDFA factoryDFA = new TokenFactoryDFA();
        String stringSum = String.valueOf(sum);
        for (int i = 0; i < stringSum.length(); i++) {
            factoryDFA.takeStep(stringSum.charAt(i));
        }
        factoryDFA.endInput();

        if(factoryDFA.getTokensList().size() > 1)
        {
            factoryDFA.getTokensList().getLast().setNegativeINT(!factoryDFA.getTokensList().getLast().isNegativeINT());
        }
        Token pollLast = factoryDFA.getTokensList().pollLast();
        memory.push(pollLast);

    }

    private long additionCalculation(Token tk) throws RuntimeException
    {
        Token valOne = memory.pop();
        Token ValTwo = memory.pop();
        long sum = Token.getDataAsLong(valOne) + Token.getDataAsLong(ValTwo);
        return sum;
    }

    private long negationCalculation(Token tk) throws RuntimeException
    {
        Token valOne = memory.pop();
        long sum = Token.getDataAsLong(valOne) * (-1);
        return sum;
    }

    private long subtractionCalculation(Token tk) throws RuntimeException
    {
        Token ValTwo = memory.pop();
        Token valOne = memory.pop();
        long sum = Token.getDataAsLong(valOne) - Token.getDataAsLong(ValTwo);
        return sum;
    }

    private long divisionCalculation(Token tk) throws RuntimeException
    {
        Token ValTwo = memory.pop();
        Token valOne = memory.pop();
        long sum = Token.getDataAsLong(valOne) / Token.getDataAsLong(ValTwo);
        return sum;
    }

    private long multiplicationCalculation(Token tk) throws RuntimeException
    {
        Token valOne = memory.pop();
        Token ValTwo = memory.pop();
        long sum = Token.getDataAsLong(valOne) * Token.getDataAsLong(ValTwo);
        return sum;
    }

    private long powerCalculation(Token tk) throws RuntimeException
    {
        return 0;
    }




    /**
     * Read the expression from left to right
     * If current element is a value push it to the stack
     * If current element is an operator,
     *      pop last two operands from stack,
     *      apply operator and
     *      push the result back to the stack
     * the very last element that was left in the stack will be our answer
     *
     * @throws RuntimeException
     */
    public LinkedList<Token> calculate() throws RuntimeException
    {
        Stack<Token> memoryStack = new Stack<>();
        Token tk, returnToken;
        Token valOneTk, valTwoTk;
        while( (tk = tokenLinkedList.pollLast()) != null)
        {
            if(tk.getTokenType() == Token.TokenType.OPERATOR )
            {
                valOneTk = memoryStack.pop();
                try
                {
                    valTwoTk = memoryStack.pop();
                    LinkedList<Token> opReturn = runOperationOn(tk, valOneTk, valTwoTk);
                    returnToken = opReturn.pollLast();
                    if(opReturn.size() == 2)
                    {
                        returnToken.setNegativeINT(true);
                    }
                    memoryStack.push(returnToken);
                }
                catch (EmptyStackException ESE)
                {
                    if(tk.getTokenData()[0] != '-')
                        throw new RuntimeException("", ESE);
                    valOneTk.setNegativeINT(true);
                    memoryStack.push(valOneTk);

//                    valTwoTk = Token.getZeroToken();
//                    LinkedList<Token> opReturn = runOperationOn(tk, valOneTk, valTwoTk);
//                    Token returnToken;
//                    while ((returnToken = opReturn.pollLast()) != null)
//                        memoryStack.push(returnToken);
                }

            } else {
                memoryStack.push(tk);
            }
        }

        LinkedList<Token> returnList = new LinkedList<>();
        while(!memoryStack.isEmpty())
        {
            returnList.addLast(memoryStack.pop());
        }
        return returnList;
    }

    /**
     * Given An Operator Token and 2 Integer Tokens will run the operation and return the result.
     * @param op
     * @param valOne
     * @param valTwo
     * @return
     */
    private LinkedList<Token> runOperationOn(Token op, Token valOne, Token valTwo)
    {
        long out = 0;

        long valueOne = Token.getDataAsLong(valOne);
        long valueTwo = Token.getDataAsLong(valTwo);

        switch (op.getTokenData()[0]) {
            //Order Does Not Matter
            case '+' -> out = valueOne + valueTwo;
            case '*' -> out = valueOne * valueTwo;

            //Order Matters
            case '/' -> out = valueOne / valueTwo;
            case '-' -> out = valueOne - valueTwo;
            case '^' ->
                {
                    out = valueTwo;
                    for (long i = 1; i < valueOne; i++) {
                        out *= valueTwo;
                }
            }

        }

        String outString = String.valueOf(out);
        TokenFactoryDFA tempFactory = new TokenFactoryDFA();
        for (int i = 0; i <outString.length(); i++) {
            tempFactory.takeStep(outString.charAt(i));
        }
        tempFactory.endInput();

        return  tempFactory.getTokensList();
    }

    /**
     * Reads from left to right and orders into postfix notation
     * @param tokens
     */
    public void toPostFix(LinkedList<Token> tokens)
    {
        Stack<Token> memoryStack = new Stack<>();

        Token tk;
        while( (tk = tokens.pollFirst()) != null)
        {
            //1. IS A NUMBER
            if(tk.getTokenType() == Token.TokenType.DEC_INT || tk.getTokenType() == Token.TokenType.OCT_INT || tk.getTokenType() == Token.TokenType.HEX_INT )
            {
                this.tokenLinkedList.push(tk);
                continue;
            } // 2. IS OPEN PARENTHESIS (
            else if (tk.getTokenType() == Token.TokenType.OPERATOR && tk.getTokenData()[0] == '(')
            {
                memoryStack.push(tk);
            } // 3. IS POWER OPERATOR
            else if (tk.getTokenType() == Token.TokenType.OPERATOR && tk.getTokenData()[0] == '^')
            {
                memoryStack.push(tk);
            } // 4. IS CLOSING PARENTHESIS
            else if (tk.getTokenType() == Token.TokenType.OPERATOR && tk.getTokenData()[0] == ')')
            {
                while(!memoryStack.isEmpty() &&
                        !( (memoryStack.peek().getTokenType() == Token.TokenType.OPERATOR) &&
                                (memoryStack.peek().getTokenData()[0] == '(') ) )
                {
                    Token memoryPop = memoryStack.pop();
                    this.tokenLinkedList.push(memoryPop);
                }
                Token memoryPop = memoryStack.pop();
            }
            else // OTHER OPERATORS
            {
                while (!memoryStack.isEmpty() && ( (postFixPrecedenceRank(tk)) <= (postFixPrecedenceRank(memoryStack.peek())) ))
                {
                    this.tokenLinkedList.push(memoryStack.pop());
                }
                memoryStack.push(tk);
            }
        }

        while (!memoryStack.isEmpty())
        {
            this.tokenLinkedList.push(memoryStack.pop());
        }

    }

    /**
     *
     * @param tk
     * @return
     */
    private int postFixPrecedenceRank(Token tk)
    {
        if(tk.getTokenType() != Token.TokenType.OPERATOR)
            return -10;

        char op = tk.getTokenData()[0];
        switch (op)
        {
            case '+' ->
            {
                return 0;
            }
            case '-' ->
            {
                if(!tk.isNegativeINT())
                    return 0;
                return 10;
            }
            case '*','/' ->
            {
                return 5;
            }
            case '^' ->
            {
                return 10;
            }
        }

        return -100;
    }

    /**
     *
     * @return
     */
    public LinkedList<Token> getTokenLinkedList()
    {
        return this.tokenLinkedList;
    }

}
