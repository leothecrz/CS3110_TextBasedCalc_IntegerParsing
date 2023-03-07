package cpp.crz.TextCalc;

import cpp.crz.TextCalc.Calc.TokenReader;
import cpp.crz.TextCalc.Tokens.Token;
import cpp.crz.TextCalc.Tokens.TokenFactoryNFA;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

class Main
{
    private static final String linePointer = " -> ";
    private static final String exitString = "";
    private static final String goodbyeMessage = "Good Bye, TextBaseCalc Closed.";

    public static void main(String[] args) throws IOException {

        //State
        boolean runInLoop = false;
        BufferedReader inputReader = null;

        //INPUT
        String input = new String();


        if(args.length > 0)
            input = buildInputString(args);
        else
        {
            //Setup Main Loop
            runInLoop = true;
            inputReader = new BufferedReader(new InputStreamReader(System.in));

            System.out.print(linePointer);
            input = inputReader.readLine();

            //End Condition
            if(input.equals(exitString))
                System.exit(0);
        }

        do
        {
            // Time Tracking
            long timeStart = System.currentTimeMillis();

            //Parse
            LinkedList<Token> parsedList = TokenFactoryNFA.parseString(input, true);
            if (parsedList != null) //Stage One Success
            {
                //Reader
                TokenReader reader = new TokenReader(parsedList, true);
                if (reader.getMemory() != null) //Stage Two Success
                {
                    if(reader.getMemory().size() > 1) // User Input Validation
                    {
                        System.out.println("\n Not enough operators");
                    }
                    else // Successful User Input Validation
                    {
                        //Calculation
                        System.out.println("\n RESULTS: \n" + reader.getMemory());

                        //Time Taken
                        System.out.println("\n  Estimated Time Passed: " + (System.currentTimeMillis() - timeStart) + " ms.");
                    }
                }

            } // Skip if Parse Null

            //Input Loop
            if(inputReader == null)
                break;

            System.out.print(linePointer);
            input = inputReader.readLine();

            //End Condition
            if(input.equals(exitString))
                runInLoop = false;

        } while (runInLoop) ;

        System.out.println(goodbyeMessage);
    }

    /**
     * Given a string array concatenate all strings together.
     * @param args String array
     * @return single string of concatenated results.
     */
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