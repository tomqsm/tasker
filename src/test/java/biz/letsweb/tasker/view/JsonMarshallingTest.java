package biz.letsweb.tasker.view;

import biz.letsweb.tasker.chronicle.model.ChronicleLine;
import biz.letsweb.tasker.response.json.JsonMarshalling;
import java.sql.Timestamp;
import java.util.ArrayList;
import json.Jsoner;
import org.joda.time.DateTime;
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
    @Test
    public void jsonerWorks(){
        Jsoner jsoner = new Jsoner();
        DateTime dateTime = new DateTime();
        ChronicleLine line_1 = new ChronicleLine();
        line_1.setTag("work_2");
        line_1.setDescription("line_1 description");
        dateTime = new DateTime(2014, 11, 24, 0, 0, 0, 0);
        line_1.setTimestamp(new Timestamp(dateTime.getMillis()));

        ChronicleLine line0 = new ChronicleLine();
        line0.setTag("work0");
        line0.setDescription("line0 description");
        dateTime = new DateTime(2014, 11, 24, 0, 0, 0, 0);
        line0.setTimestamp(new Timestamp(dateTime.getMillis()));
        ArrayList<ChronicleLine> list = new ArrayList<>();
        list.add(line0);
        list.add(line_1);
        System.out.println(jsoner.objectToJson(list));
    }
    
}
