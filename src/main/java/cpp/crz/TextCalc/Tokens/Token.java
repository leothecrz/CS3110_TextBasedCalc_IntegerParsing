package cpp.crz.TextCalc.Tokens;

import cpp.crz.TextCalc.Errors.TokenTypeMismatch;

import java.util.Arrays;

public class Token
{

    public static enum TokenType
    {
        DEC_INT,
        HEX_INT,
        OCT_INT,
        OPERATOR,
        ERROR
    }

    private TokenType tokenType;
    private Character[] tokenData;

    public Token(TokenType type, Character[] data)
    {
        tokenType = type;
        tokenData = data.clone();
    }

    public TokenType getTokenType()
    {
        return this.tokenType;
    }

    public Character[] getTokenData()
    {
        return tokenData;
    }

    @Override
    public String toString() {
        return "Token{" +
                "tokenType=" + tokenType +
                ", tokenData=" + Arrays.toString(tokenData) +
                '}';
    }

    public static int getDataAsInT(Token tk) throws TokenTypeMismatch
    {
        if(!(tk.tokenType == TokenType.DEC_INT || tk.tokenType == TokenType.HEX_INT || tk.tokenType == TokenType.OCT_INT ))
            throw new TokenTypeMismatch();

        int base = 0;
        switch (tk.getTokenType())
        {
            case DEC_INT -> base = 10;
            case HEX_INT -> base = 16;
            case OCT_INT -> base = 8;
        }

        if(tk.getTokenData().length > 9)
            if(tk.getTokenData()[9] > '2')
                throw new RuntimeException("NeedsLong");

        int returnVal = 0;

        for(int i=0; i<tk.getTokenData().length; i++)
        {
            returnVal += (int)(tk.getTokenData()[i] - '0') *  (int)(Math.pow(base, i));
        }
        return returnVal;
    }

    public static long getDataAsLong(Token tk) throws TokenTypeMismatch
    {
        if(!(tk.tokenType == TokenType.DEC_INT || tk.tokenType == TokenType.HEX_INT || tk.tokenType == TokenType.OCT_INT ))
            throw new TokenTypeMismatch();

        if(tk.getTokenData().length > 9)
            if(tk.getTokenData()[9] > '2')
                throw new RuntimeException("NeedsLong");

        int base = 0;
        switch (tk.getTokenType())
        {
            case DEC_INT -> base = 10;
            case HEX_INT -> base = 16;
            case OCT_INT -> base = 8;
        }

        long returnVal = 0;

        for(int i=0; i<tk.getTokenData().length; i++)
        {
            returnVal += (long) (tk.getTokenData()[i] - '0') * (int)(Math.pow(base, i) ) ;
        }
        return returnVal;

    }

    public static char getDataAsChar(Token tk) throws TokenTypeMismatch
    {
        if((tk.tokenType == TokenType.DEC_INT || tk.tokenType == TokenType.HEX_INT || tk.tokenType == TokenType.OCT_INT ))
            throw new TokenTypeMismatch();

        return tk.getTokenData()[0];

    }



    //    /**
//     * WIP
//     * @return
//     * @throws TokenTypeMismatch
//     */
//    public long getDataLong() throws TokenTypeMismatch
//    {
//        if(!needsLong)
//            throw new TokenTypeMismatch();
//        return 0;
//    }
//
//    /**
//     * WIP
//     * @return
//     * @throws TokenTypeMismatch
//     */
//    public int getDataInt()throws TokenTypeMismatch
//    {
//        if(needsLong)
//            throw new TokenTypeMismatch();
//        return 0;
//    }
//
//    /**
//     * WIP
//     * @return
//     * @throws TokenTypeMismatch
//     */
//    public char getOp() throws TokenTypeMismatch
//    {
//        if(this.tokenType != TokenType.OPERATOR && this.tokenType !=TokenType.OPEN_PARENTHESIS && this.tokenType !=TokenType.CLOSED_PARENTHESIS)
//            throw new TokenTypeMismatch();
//        return '\0';
//    }


}
