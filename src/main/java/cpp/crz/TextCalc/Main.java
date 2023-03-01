package cpp.crz.TextCalc;

import cpp.crz.TextCalc.Calc.TokenReader;
import cpp.crz.TextCalc.Tokens.Token;
import cpp.crz.TextCalc.Tokens.TokenFactoryDFA;

class Main
{

    public static void main(String[] args)
    {
        //INPUT
        StringBuilder input = new StringBuilder();
        for (String str : args)
        {
            input.append(str);
        }
        System.err.println(input);

        //Parse String
        TokenFactoryDFA factoryDFA = new TokenFactoryDFA();
        boolean syntaxError = false;
        for(int i=0; i<input.length(); i++)
        {
            try {
                factoryDFA.takeStep(input.charAt(i));
            }
            catch (RuntimeException RE)
            {
                System.out.println(RE);
                syntaxError = true;
                factoryDFA.reset();
                break;
            }
        }

        //Deal with tokens
        if(syntaxError){
            System.out.println("SyntaxError");
            return; // END
        } // ELSE

        //Debug OUT
        factoryDFA.endInput();
        for(int i=0; i<factoryDFA.getTokensList().size(); i++)
        {
            System.out.println(factoryDFA.getTokensList().get(i));
        } System.out.println("END");

        //Reader
        TokenReader reader = new TokenReader(factoryDFA.getTokensList());
        factoryDFA.reset();

    }
}