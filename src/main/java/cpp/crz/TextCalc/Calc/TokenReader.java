package cpp.crz.TextCalc.Calc;

import cpp.crz.TextCalc.Tokens.Token;

import java.util.LinkedList;
import java.util.Stack;

public class TokenReader {

    private LinkedList<Token> tokenLinkedList;

    public TokenReader(LinkedList<Token> tokenList)
    {
        tokenLinkedList = new LinkedList<>();
        toPostFix(tokenList);


        for(int i=0; i< tokenLinkedList.size(); i++)
        {
            System.out.println(tokenLinkedList.get(i));
        } System.out.println("END");
    }

    /**
     * Read the expression from left to right
     * If current element is a value (e.g. Integer) push it to the stack
     * If current element is an operator, pop last two operands from stack, apply operator and push the result back to the stack
     * After doing this steps, the very last element that was left in the stack will be our answer. Simple, pop the last element and return it.
     *
     * @throws RuntimeException
     */
    public void calculate() throws RuntimeException
    {
        Stack<Token> memoryStack = new Stack<>();
        Token tk;
        while( (tk = tokenLinkedList.pollFirst()) != null)
        {

            switch (tk.getTokenType())
            {
                case HEX_INT ->
                {

                }
                case OCT_INT ->
                {

                }
                case DEC_INT ->
                {

                }
                case OPERATOR ->
                {

                }
            }


        }

    }



    /**
     *`````````````
     * @param tokens
     */
    public void toPostFix(LinkedList<Token> tokens)
    {
        Stack<Token> memoryStack = new Stack<>();

        Token tk;
        while( (tk = tokens.pollFirst()) != null)
        {
            if(tk.getTokenType() == Token.TokenType.DEC_INT || tk.getTokenType() == Token.TokenType.OCT_INT || tk.getTokenType() == Token.TokenType.HEX_INT)
            {
                this.tokenLinkedList.push(tk);
                continue;
            }
            else if (tk.getTokenType() == Token.TokenType.OPERATOR && tk.getTokenData()[0] == '(')
            {
                memoryStack.push(tk);
            }
            else if (tk.getTokenType() == Token.TokenType.OPERATOR && tk.getTokenData()[0] == '^')
            {
                memoryStack.push(tk);
            }
            else if (tk.getTokenType() == Token.TokenType.OPERATOR && tk.getTokenData()[0] == ')')
            {
                while(!memoryStack.isEmpty() && !( memoryStack.peek().getTokenType() == Token.TokenType.OPERATOR && memoryStack.peek().getTokenData()[0] == '(') )
                {
                    Token memoryPop = memoryStack.pop();
                    this.tokenLinkedList.push(memoryPop);
                }
                Token memoryPop = memoryStack.pop();
            }
            else
            {
                while (!memoryStack.isEmpty() && ( (postFixPrecedenceRank(tk)) <= (postFixPrecedenceRank(memoryStack.peek())) ))
                {
                    this.tokenLinkedList.push(memoryStack.pop());
                }
                memoryStack.push(tk);
            }
        }
        while (!memoryStack.isEmpty()){
            this.tokenLinkedList.push(memoryStack.pop());
        }
    }

    private int postFixPrecedenceRank(Token tk)
    {
        if(tk.getTokenType() != Token.TokenType.OPERATOR)
            return -10;

        char op = tk.getTokenData()[0];
        switch (op)
        {
            case '+','-' ->
            {
                return 0;
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
