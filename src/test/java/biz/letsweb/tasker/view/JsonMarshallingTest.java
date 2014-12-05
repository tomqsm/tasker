package biz.letsweb.tasker.view;

import biz.letsweb.tasker.response.JsonMarshalling;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author toks
 */
public class JsonMarshallingTest {  
    
    public JsonMarshallingTest() {
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
     * Test of marshall method, of class JsonMarshalling.
     */
    @Test
    public void testMarshall() throws Exception {
        JsonMarshalling instance = new JsonMarshalling();
        instance.marshall();
    }
    
}
