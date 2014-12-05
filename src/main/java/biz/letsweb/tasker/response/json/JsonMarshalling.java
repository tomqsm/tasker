package biz.letsweb.tasker.response.json;

import biz.letsweb.tasker.chronicle.model.ChronicleLine;
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
        ChronicleLine line = new ChronicleLine();
        line.setId(1);
        line.setParentId(0);
        line.setDescription("opis");
        line.setTimestamp(new Timestamp(System.currentTimeMillis()));
        line.setDuration(Duration.ZERO);
        line.setTotalDuration(Duration.ZERO);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
        objectMapper.configure(SerializationConfig.Feature.AUTO_DETECT_GETTERS, true);
        objectMapper.configure(SerializationConfig.Feature.WRAP_ROOT_VALUE, false);
        objectMapper.writeValue(System.out, line);
    }

}
