package sample.serializersCreators.serializers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import sample.devices.Device;
import sample.serializersCreators.Serializer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static sample.helpClass.AdditionalFunction.convertStringToDeviceObject;

public class JsonSerializer implements Serializer {
    @Override
    public void serialize(Map<String,Object> map,File file){
        ObjectMapper objectMapper = new ObjectMapper();
        String globalTmp="{";
        for (Map.Entry<String,Object> el : map.entrySet()) {
            String jsonResult;
            try {
                jsonResult = objectMapper.writeValueAsString(el);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                break;
            }
            int index=jsonResult.indexOf("}")-1;
            int len = jsonResult.length();
            String tmp = jsonResult.substring(1,index) + " / " + el.getValue().getClass() + jsonResult.substring(index,len-2)+"\",";
            globalTmp += tmp;
        }
        String jsonEndResult = globalTmp.substring(0,globalTmp.length()-1) + "}";
        try {
            if (!file.exists())
                file.createNewFile();
            FileWriter f = new FileWriter(file);
            f.write(jsonEndResult);
            f.close();
        } catch (IOException e) {
        //    e.printStackTrace();
        }
    }

    @Override
    public Map<String,Object> deserialize(File file){
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<HashMap<String, Object>> typeRef = new TypeReference<>() {};
        Map<String,Object> map;
        try {
            map = objectMapper.readValue(file,typeRef);
        } catch (IOException e) {
         //   e.printStackTrace();
            return null;
        }
        return convertStringToDeviceObject(map);
    }

}
