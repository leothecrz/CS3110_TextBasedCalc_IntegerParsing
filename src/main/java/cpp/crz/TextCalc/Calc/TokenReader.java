package cpp.crz.TextCalc.Calc;

import com.sun.source.doctree.ValueTree;
import cpp.crz.TextCalc.Tokens.Token;
import cpp.crz.TextCalc.Tokens.TokenFactoryNFA;

import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.Stack;
import java.util.concurrent.ExecutionException;

public class TokenReader {

    private LinkedList<Token> tokenLinkedList;

    private Stack<Token> memory;

    /**
     * constructor
     * @param tokenList list of tokens
     */
    public TokenReader(LinkedList<Token> tokenList, boolean runCalculation)
    {
        tokenLinkedList = new LinkedList<>();
        memory = new Stack<>();
        try
        {
            toPostFix(tokenList);
        }
        catch (RuntimeException RE)
        {
            runCalculation = false;
            memory = null;
            System.out.println("To PostFix Failed");
        }

        for(int i=0; i< tokenLinkedList.size(); i++)  {
            System.out.println(tokenLinkedList.get(i));
        } System.out.println("      PostFix Operation Done");

        if(runCalculation)
            runCalculation();
    }

    public Stack<Token> getMemory()
    {
        return this.memory;
    }


    public Stack<Token> runCalculation()
    {

        memory = new Stack<>();
        Token currentTK;

        while( (currentTK = this.tokenLinkedList.pollLast()) != null )
        {
            if(currentTK.getTokenType() == Token.TokenType.OPERATOR)
            {
                try
                {
                    operatorCalculation(currentTK);
                }
                catch (RuntimeException RTE)
                {
                    System.out.println("\n      Error During Calculation: " + RTE.getMessage());
                    memory = null;
                    return null;
                }
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
                sum = additionCalculation(tk);

            case '-' ->
            {
                if(tk.hasNegativeFlag())
                {
                    sum = negationCalculation(tk);
                }
                else
                {
                    sum = subtractionCalculation(tk);
                }
            }
            case '/' ->
                sum = divisionCalculation(tk);

            case '*' ->
                sum = multiplicationCalculation(tk);

            case '^' ->
                sum = powerCalculation(tk);

        }

        TokenFactoryNFA factoryDFA = new TokenFactoryNFA();
        String stringSum = String.valueOf(sum);
        for (int i = 0; i < stringSum.length(); i++) {
            factoryDFA.takeStep(stringSum.charAt(i));
        }
        factoryDFA.endInput();

        if(factoryDFA.getTokensList().size() > 1)
        {
            factoryDFA.getTokensList().getLast().setNegativeFlag(!factoryDFA.getTokensList().getLast().hasNegativeFlag());
        }
        Token pollLast = factoryDFA.getTokensList().pollLast();
        memory.push(pollLast);

    }

    private long additionCalculation(Token tk) throws RuntimeException {

        Token valOne = null;
        Token valTwo = null;
        try {
            valOne = memory.pop();
            valTwo = memory.pop();
            long sum = Token.getDataAsLong(valOne) + Token.getDataAsLong(valTwo);
            return sum;
        } catch (EmptyStackException ESE) {
            throw new RuntimeException(("Addition Failure \nTokenOne: " + valOne + " \nTokenTwo: " + valTwo), ESE);
        }

    }

    private long negationCalculation(Token tk) throws RuntimeException {

        Token valOne = null;
        try {
            valOne = memory.pop();
            long sum = Token.getDataAsLong(valOne) * (-1);
            return sum;
        } catch (EmptyStackException ESE) {
            throw new RuntimeException(("Negation Failure \nTokenOne: " + valOne), ESE);
        }

    }

    private long subtractionCalculation(Token tk) throws RuntimeException {
        Token valOne = null;
        Token valTwo = null;
        try {
            valTwo = memory.pop();
            valOne = memory.pop();
            long sum = Token.getDataAsLong(valOne) - Token.getDataAsLong(valTwo);
            return sum;
        } catch (EmptyStackException ESE) {
            throw new RuntimeException(("Subtraction Failure \nTokenOne: " + valOne + " \nTokenTwo: " + valTwo), ESE);
        }
    }

    private long divisionCalculation(Token tk) throws RuntimeException {
        Token valOne = null;
        Token valTwo = null;
        try {
            valTwo = memory.pop();
            valOne = memory.pop();
            long sum = Token.getDataAsLong(valOne) / Token.getDataAsLong(valTwo);
            return sum;
        } catch (EmptyStackException ESE) {
            throw new RuntimeException(("Division Failure \nTokenOne: " + valOne + " \nTokenTwo: " + valTwo), ESE);
        }
    }

    private long multiplicationCalculation(Token tk) throws RuntimeException {
        Token valOne = null;
        Token valTwo = null;
        try {
            valOne = memory.pop();
            valTwo = memory.pop();
            long sum = Token.getDataAsLong(valOne) * Token.getDataAsLong(valTwo);
            return sum;
        } catch (EmptyStackException ESE) {
            throw new RuntimeException(("Multiplication Failure \nTokenOne: " + valOne + " \nTokenTwo: " + valTwo), ESE);
        }
    }

    private long powerCalculation(Token tk) throws RuntimeException
    {
        return 0;
    }

    /**
     * Given a list of tokens will read from left to right assuming infix notation.
     * Will store in buffer the postfix ordered list.
     * @param tokens - infix sorted list
     */
    public void toPostFix(LinkedList<Token> tokens) throws RuntimeException
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
                    if(tk.hasNegativeFlag()) // Negation Exception
                        break;
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
     * @param tk - Token to be ranked.
     * @return rank of precedence as a comparable value.
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
                if(!tk.hasNegativeFlag())
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

}
