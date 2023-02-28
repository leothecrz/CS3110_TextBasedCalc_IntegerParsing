package cpp.crz.TextCalc.Tokens;

public class Token
{

    public static enum TokenType
    {
        EMPTY,
        DEC_INT,
        HEX_INT,
        OCT_INT,
        OPERATOR,
        OPEN_PARENTHESIS,
        CLOSED_PARENTHESIS,
        ERROR
    }

    private TokenType tokenType;
    private Character[] tokenData;
    private boolean needsLong;

    private Integer integer;
    private Long longInteger;
    private Character operator;

    public Token(TokenType type, Character[] data)
    {
        tokenType = type;
        tokenData = data;
        needsLong = false;
    }

    public TokenType getTokenType()
    {
        return this.tokenType;
    }

    public Character[] getTokenData()
    {
        return tokenData;
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
