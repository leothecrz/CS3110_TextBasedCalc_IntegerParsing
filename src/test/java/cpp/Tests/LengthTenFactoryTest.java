package cpp.Tests;

import cpp.crz.TextCalc.Tokens.TokenFactoryNFA;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS) @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LengthTenFactoryTest
{

    private String tenStepParse;
    private TokenFactoryNFA factoryNFA;

    private int index;

    @Test @Order(0)
    public void stepOne()
    {
        tenStepParse = "10+10/2*99";
        index = 0;
        factoryNFA = new TokenFactoryNFA();

        factoryNFA.takeStep(tenStepParse.charAt(index));
        index ++;

        Assertions.assertEquals(TokenFactoryNFA.States.DecInt, factoryNFA.getFactoryState());
    }

    @Test @Order(1)
    public void stepTwo()
    {
        factoryNFA.takeStep(tenStepParse.charAt(index));
        index ++;
        Assertions.assertEquals(TokenFactoryNFA.States.DecInt, factoryNFA.getFactoryState());
    }

    @Test @Order(2)
    public void stepThree()
    {
        factoryNFA.takeStep(tenStepParse.charAt(index));
        index ++;
        Assertions.assertEquals(TokenFactoryNFA.States.Op, factoryNFA.getFactoryState());
    }

    @Test @Order(3)
    public void stepFour()
    {
        factoryNFA.takeStep(tenStepParse.charAt(index));
        index ++;
        Assertions.assertEquals(TokenFactoryNFA.States.DecInt, factoryNFA.getFactoryState());
    }

    @Test @Order(4)
    public void stepThreeVerification()
    {
        Assertions.assertEquals(2, factoryNFA.getTokensList().size());
    }

    @Test @Order(5)
    public void stepFive()
    {
        factoryNFA.takeStep(tenStepParse.charAt(index));
        index ++;
        Assertions.assertEquals(TokenFactoryNFA.States.DecInt, factoryNFA.getFactoryState());
    }

    @Test @Order(6)
    public void stepSix()
    {
        factoryNFA.takeStep(tenStepParse.charAt(index));
        index ++;
        Assertions.assertEquals(TokenFactoryNFA.States.Op, factoryNFA.getFactoryState());
    }

    @Test @Order(7)
    public void stepSeven()
    {
        factoryNFA.takeStep(tenStepParse.charAt(index));
        index ++;
        Assertions.assertEquals(TokenFactoryNFA.States.DecInt, factoryNFA.getFactoryState());
    }
    @Test @Order(8)
    public void stepEight()
    {
        factoryNFA.takeStep(tenStepParse.charAt(index));
        index ++;
        Assertions.assertEquals(TokenFactoryNFA.States.Op, factoryNFA.getFactoryState());
    }

    @Test @Order(9)
    public void stepNine()
    {
        factoryNFA.takeStep(tenStepParse.charAt(index));
        index ++;
        Assertions.assertEquals(TokenFactoryNFA.States.DecInt, factoryNFA.getFactoryState());
    }

    @Test @Order(10)
    public void stepTen()
    {
        factoryNFA.takeStep(tenStepParse.charAt(index));
        index ++;
        Assertions.assertEquals(TokenFactoryNFA.States.DecInt, factoryNFA.getFactoryState());
    }

    @Test @Order(11)
    public void stepEleven()
    {
        factoryNFA.endInput();
        Assertions.assertEquals(7, factoryNFA.getTokensList().size());
    }








}
