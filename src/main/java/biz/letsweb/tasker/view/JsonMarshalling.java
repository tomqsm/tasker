package biz.letsweb.tasker.view;

import biz.letsweb.tasker.model.ConsoleViewModel;
import biz.letsweb.tasker.persistence.model.ChronicleRecordLine;
import java.io.IOException;
import java.sql.Timestamp;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author toks
 */
public class JsonMarshalling {

    public static final Logger log = LoggerFactory.getLogger(JsonMarshalling.class);

    public void marshall() throws IOException{
        ConsoleViewModel consoleViewModel = new ConsoleViewModel();
        consoleViewModel.setChronicleRecordLine(new ChronicleRecordLine(1, 1, "work", "desc", new Timestamp(System.currentTimeMillis())));
        consoleViewModel.setDuration(Duration.ZERO);
        consoleViewModel.setTotalDuration(Duration.ZERO);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
        objectMapper.configure(SerializationConfig.Feature.AUTO_DETECT_GETTERS, true);
        objectMapper.configure(SerializationConfig.Feature.WRAP_ROOT_VALUE, false);
        objectMapper.writeValue(System.out, consoleViewModel);
    }

}
