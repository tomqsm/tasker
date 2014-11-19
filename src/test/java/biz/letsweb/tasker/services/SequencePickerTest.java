package biz.letsweb.tasker.services;

import biz.letsweb.tasker.persistence.model.ChronicleRecordLine;
import java.util.ArrayList;
import java.util.List;
import static org.fest.assertions.Assertions.assertThat;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author toks
 */
public class SequencePickerTest {

    public SequencePickerTest() {
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
     * Test of pickSequenceByCount method, of class SequencePicker.
     */
    @Test
    public void testPickSequenceByCount() {
        ChronicleRecordLine line0 = new ChronicleRecordLine(1, 2, "work", "", null);
        ChronicleRecordLine line1 = new ChronicleRecordLine(2, 3, "work", "", null);
        ChronicleRecordLine line2 = new ChronicleRecordLine(3, 4, "break", "", null);
        ChronicleRecordLine line3 = new ChronicleRecordLine(4, 5, "work", "", null);
        ChronicleRecordLine line4 = new ChronicleRecordLine(5, 6, "work", "", null);
        ChronicleRecordLine line5 = new ChronicleRecordLine(6, 7, "work", "", null);
        List<ChronicleRecordLine> list = new ArrayList<>();
        list.add(line0);
        list.add(line1);
        list.add(line2);
        list.add(line3);
        list.add(line4);
        list.add(line5);
        String key = "work";
        SequencePicker instance = new SequencePicker();
        List<ChronicleRecordLine> result = instance.pickSequenceByCount(list, key);
        assertThat(result.toString()).contains("bla");
//        assertThat(result).hasSize(3);

    }

}
