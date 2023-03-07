package cpp.Tests;

import cpp.crz.TextCalc.Calc.TokenReader;
import cpp.crz.TextCalc.Tokens.Token;
import cpp.crz.TextCalc.Tokens.TokenFactoryNFA;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TokenReaderTest {

    @Test
    public void tripleNegationTest()
    {
        String tripleNeg = "---20";
        TokenFactoryNFA factoryNFA = new TokenFactoryNFA();
        for (int i = 0; i < tripleNeg.length(); i++) {
            factoryNFA.takeStep(tripleNeg.charAt(i));
        }
        factoryNFA.endInput();

        TokenReader reader = new TokenReader(factoryNFA.getTokensList(), false);
        Token out = reader.runCalculation().pop();
        Token expected = new Token(Token.TokenType.DEC_INT, new Character[] {'0', '2'});
        expected.setNegativeFlag(true);

        Assertions.assertTrue(expected.equals(out));

    }

    @Test
    public void tripleNegationTestTwo()
    {
        String tripleNeg = "20+---20";
        TokenFactoryNFA factoryNFA = new TokenFactoryNFA();
        for (int i = 0; i < tripleNeg.length(); i++) {
            factoryNFA.takeStep(tripleNeg.charAt(i));
        }
        factoryNFA.endInput();

        TokenReader reader = new TokenReader(factoryNFA.getTokensList(), false);
        Token out = reader.runCalculation().pop();
        Token expected = new Token(Token.TokenType.DEC_INT, new Character[] {'0'});
        expected.setNegativeFlag(false);

        Assertions.assertTrue(expected.equals(out));

    }

    @Test
    public void parenthesisNegationTest()
    {
        String tripleNeg = "-(10+10)+15";
        TokenFactoryNFA factoryNFA = new TokenFactoryNFA();
        for (int i = 0; i < tripleNeg.length(); i++) {
            factoryNFA.takeStep(tripleNeg.charAt(i));
        }
        factoryNFA.endInput();

        TokenReader reader = new TokenReader(factoryNFA.getTokensList(), false);
        Token out = reader.runCalculation().pop();
        Token expected = new Token(Token.TokenType.DEC_INT, new Character[] {'5'});
        expected.setNegativeFlag(true);

        Assertions.assertTrue(expected.equals(out));

    }

    @Test
    public void parenthesisNegationTestTwo()
    {
        String tripleNeg = "-(-(10+10))+(15*3)/5+1";
        TokenFactoryNFA factoryNFA = new TokenFactoryNFA();
        for (int i = 0; i < tripleNeg.length(); i++) {
            factoryNFA.takeStep(tripleNeg.charAt(i));
        }
        factoryNFA.endInput();

        TokenReader reader = new TokenReader(factoryNFA.getTokensList(), false);
        Token out = reader.runCalculation().pop();
        Token expected = new Token(Token.TokenType.DEC_INT, new Character[] {'0', '3'});
        expected.setNegativeFlag(false);

        Assertions.assertTrue(expected.equals(out));

    }

    @Test
    public void LimitTest()
    {
        String tripleNeg = "((10+2-(20*3)/5)*-3)+(10*10*(-10)*-10)";
        TokenFactoryNFA factoryNFA = new TokenFactoryNFA();
        for (int i = 0; i < tripleNeg.length(); i++) {
            factoryNFA.takeStep(tripleNeg.charAt(i));
        }
        factoryNFA.endInput();

        TokenReader reader = new TokenReader(factoryNFA.getTokensList(), false);
        Token out = reader.runCalculation().pop();
        Token expected = new Token(Token.TokenType.DEC_INT, new Character[] {'0', '0','0','0','1'});
        expected.setNegativeFlag(false);

        Assertions.assertTrue(expected.equals(out));

    }


}
