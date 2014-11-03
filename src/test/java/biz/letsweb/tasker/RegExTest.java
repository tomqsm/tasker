package biz.letsweb.tasker;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
//import static org.fest.assertions.api.Assertions.atIndex; // for List assertion
//import static org.fest.assertions.api.Assertions.entry;  // for Map assertion
//import static org.fest.assertions.api.Assertions.extractProperty; // for Iterable/Array assertion
//import static org.fest.assertions.api.Assertions.fail; // use when making exception tests
//import static org.fest.assertions.api.Assertions.failBecauseExceptionWasNotThrown; // idem
//import static org.fest.assertions.api.Assertions.filter; // for Iterable/Array assertion
//import static org.fest.assertions.api.Assertions.offset; // for floating number assertion
//import static org.fest.assertions.api.Assertions.anyOf; // use with Condition

/**
 *
 * @author toks
 */
public class RegExTest {

    public RegExTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getClientConnectionPoolDataSource method, of class
     * DerbyPooledDataSourceMaker.
     */
    @Test
    public void buildADecimalNumberOutOfDigitsInStringTest() {
        String inputString = "coś miałe mdo powiedzenia 1 234., fg .5 ksda 6767"; //1234,5
        String regex = "(\\b\\d+\\b)";
        Pattern pattern = Pattern.compile(regex);

        int endIdxPrevious = -1, startIdxCurrent = -1;
        Matcher matcher = pattern.matcher(inputString);
        StringBuilder builder = new StringBuilder();
        boolean isDecimalSet = false;
        while (matcher.find()) {
            System.out.println(String.format("I found the text"
                    + " \"%s\" starting at "
                    + "index %d and ending at index %d. Length: %d%n",
                    matcher.group(),
                    matcher.start(),
                    matcher.end(),
                    matcher.end() - matcher.start()));
            startIdxCurrent = matcher.start();
            if (endIdxPrevious > 0) {
//                // runs second time, first time startIdx is 0
                String gapString = inputString.substring(endIdxPrevious, startIdxCurrent);
                int gapStringLength = (startIdxCurrent - endIdxPrevious);
                if(isDecimalSet==false && gapString.contains(",") || gapString.contains(".")){
                    builder.append(",");
                    isDecimalSet = true;
                }
                System.out.println("gap string: '" + gapString + "', length: " + gapStringLength);
            }
            endIdxPrevious = matcher.end();
            builder.append(matcher.group());
        }
        System.out.println("extracted: " + builder.toString());
    }
    @Test
    public void regexFindingDiditsBuildingANumber() {
        String inputString = "coś miałe mdo powiedzenia 1 234., fg .5 ksda 6767"; //1234,56767
        String inputString1 = "coś miałe mdo powiedzenia 1 234 fg .5 ksda7"; //1234,5
        inputString = inputString1;
        String regex = "(\\b\\d+\\b)";
        Pattern pattern = Pattern.compile(regex);
        int endIdxPrevious = -1, startIdxCurrent = -1;
        Matcher matcher = pattern.matcher(inputString);
        StringBuilder builder = new StringBuilder();
        boolean decimalPointFound = false;
        while (matcher.find()) {
            startIdxCurrent = matcher.start();
            if (endIdxPrevious > 0) {
                String gapString = inputString.substring(endIdxPrevious, startIdxCurrent);
                if(decimalPointFound==false && gapString.contains(",") || gapString.contains(".")){
                    builder.append(",");
                    decimalPointFound = true;
                }
            }
            endIdxPrevious = matcher.end();
            builder.append(matcher.group());
        }
        System.out.println(builder.toString());
    }

}
