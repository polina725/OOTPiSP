package sample.serializersCreators;

import java.io.File;
import java.util.Map;

public interface Serializer {
    void serialize(Map<String, Object> map, File file);
    Map<String,Object> deserialize(File file);
}
