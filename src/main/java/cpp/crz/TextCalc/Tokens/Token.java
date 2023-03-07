package cpp.crz.TextCalc.Tokens;

import cpp.crz.TextCalc.Errors.TokenTypeMismatch;

import java.util.Arrays;
import java.util.Objects;

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

    private final TokenType tokenType;
    private final Character[] tokenData;
    private boolean negativeFlag;

    public Token(TokenType type, Character[] data)
    {
        tokenType = type;
        tokenData = data.clone();
        negativeFlag = false;
    }

    public TokenType getTokenType()
    {
        return this.tokenType;
    }

    public Character[] getTokenData()
    {
        return tokenData;
    }

    public boolean hasNegativeFlag()
    {
        return this.negativeFlag;
    }

    public void setNegativeFlag(boolean bool)
    {
        this.negativeFlag = bool;
    }

    @Override
    public String toString() {
        return  "Token{" +
                "tokenType=" + tokenType +
                ", tokenDataRAW=" + Arrays.toString(tokenData) +
                ", tokenData=" + (((tokenType == TokenType.ERROR)||(tokenType == TokenType.OPERATOR)) ?  null: getDataAsLong(this))  +
                ", isNegative= " + this.negativeFlag +
                '}';

    }

    public static long getDataAsLong(Token tk) throws TokenTypeMismatch
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

        long returnVal = 0;

        for(int i=0; i<tk.getTokenData().length; i++)
        {
            char currentChar = tk.tokenData[i];
            if(TokenFactoryNFA.isDecimal(currentChar))
                returnVal += (long)(currentChar - '0') *  (Math.pow(base, i));
            else if (TokenFactoryNFA.isHex(currentChar))
            {
                if(currentChar <= 'F')
                    returnVal += (long)(currentChar - 'A' + 10) *  (Math.pow(base, i));
                else
                    returnVal += (long)(currentChar - 'a' + 10) *  (Math.pow(base, i));
            }
        }
        return ((tk.hasNegativeFlag()) ? ((-1) * returnVal) : returnVal);
    }

    public static char getDataAsChar(Token tk) throws TokenTypeMismatch
    {
        if((tk.tokenType == TokenType.DEC_INT || tk.tokenType == TokenType.HEX_INT || tk.tokenType == TokenType.OCT_INT ))
            throw new TokenTypeMismatch();

        return tk.getTokenData()[0];

    }

    public static Token getZeroToken()
    {
        return new Token(TokenType.DEC_INT, new Character[] {'0'});
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return negativeFlag == token.negativeFlag &&
                tokenType == token.tokenType &&
                Arrays.equals(tokenData, token.tokenData);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(tokenType, negativeFlag);
        result = 41 * result + Arrays.hashCode(tokenData);
        return result;
    }
}
