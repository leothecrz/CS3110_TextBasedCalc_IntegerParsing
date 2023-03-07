package cpp.Tests;

import cpp.crz.TextCalc.Tokens.Token;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class TokenTest {

    private Token constructorTestToken;
    private Token dataToLongToken;
    private Token dataToCharToken;

    private Token operatorNegationToken;
    private Token numberNegationToken;


    private void constructorTestSetup()
    {
        constructorTestToken = new Token(Token.TokenType.DEC_INT, new Character[] {'0'});
    }

    private void setDataToLongToken()
    {
        Character[] tokenData = {'0', '0', '0', '0', '2', '1','4','2'};
        dataToLongToken = new Token( Token.TokenType.DEC_INT, tokenData);
    }

    private void setDataToCharToken()
    {
        dataToCharToken = new Token( Token.TokenType.OPERATOR, new Character[] {'+'});
    }


    @Test
    public void typeCheck()
    {
        constructorTestSetup();
        Assertions.assertEquals( Token.TokenType.DEC_INT, constructorTestToken.getTokenType());
    }

    @Test
    public void dataCheck()
    {
        constructorTestSetup();
        Assertions.assertEquals('0', constructorTestToken.getTokenData()[0]);
    }

    @Test
    public void dataAsLongTest()
    {
        setDataToLongToken();
        Assertions.assertEquals( 24120000L, Token.getDataAsLong(dataToLongToken) );
    }

    @Test
    public void dataAsCharTest()
    {
        setDataToCharToken();
        Assertions.assertEquals('+', Token.getDataAsChar(dataToCharToken) );
    }



}
