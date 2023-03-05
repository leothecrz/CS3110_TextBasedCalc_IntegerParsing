package cpp.crz.TextCalc;

import cpp.crz.TextCalc.Calc.TokenReader;
import cpp.crz.TextCalc.Tokens.Token;
import cpp.crz.TextCalc.Tokens.TokenFactoryDFA;

import java.util.LinkedList;

class Main
{

    public static void main(String[] args)
    {
        long timeStart = System.currentTimeMillis();

        //INPUT
        String input = new String();
        if(args.length > 0)
            input = buildInputString(args);

        //Parse String
        TokenFactoryDFA factoryDFA = new TokenFactoryDFA();
        boolean invalidCharacterErrors = false;
        for(int i=0; i<input.length(); i++)
        {
            try {
                factoryDFA.takeStep(input.charAt(i));
            }
            catch (RuntimeException RE)
            {
                System.out.println(RE.getMessage());
                invalidCharacterErrors = true;
                factoryDFA.reset();
                break;
            }
        }

        //Deal with tokens
        if(invalidCharacterErrors){
            System.out.println("\n invalidCharacterErrors");
            return; // END
        } // ELSE Continue


        { //Debug OUT
            factoryDFA.endInput();
            for (int i = 0; i < factoryDFA.getTokensList().size(); i++) {
                System.out.println(factoryDFA.getTokensList().get(i));
            }
            System.out.println("String To Token END \n");
        }

        //Reader
        TokenReader reader = new TokenReader(factoryDFA.getTokensList());
        try{
            //LinkedList<Token> results = reader.calculate();

            System.out.println("\n RESULTS: \n" + reader.runCalculation());
        }
        catch (RuntimeException RTE)
        {
            //TODO:: HANDLE IMPROPER INPUT
            System.out.println(RTE.getMessage());
        }

        System.out.println("\nEstimated Time Passed: " +
                (System.currentTimeMillis() - timeStart) +
                " ms.");

    }

    private static String buildInputString(String[] args)
    {
        StringBuilder input = new StringBuilder();
        for (String str : args)
        {
            input.append(str);
        }
        System.out.println("\n" + input + "\n");
        return input.toString();
    }

}