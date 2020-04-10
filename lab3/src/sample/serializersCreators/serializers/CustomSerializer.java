package sample.serializersCreators.serializers;

import sample.serializersCreators.Serializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static sample.helpClass.AdditionalFunction.*;
import static sample.helpClass.AdditionalFunction.proceedObjects;

public class CustomSerializer implements Serializer {
    @Override
    public void serialize(Map<String,Object> map,File file){
        String finalString = "{";
        for (Map.Entry<String,Object> el : map.entrySet()) {
            String mapValue = "\"" + el.getValue() + " / " + el.getValue().getClass() + "\",";
            String mapKey = "\"" + el.getKey() + "\":";
            finalString += (mapKey + mapValue);
        }
        String tmp = finalString.substring(0,finalString.length()-1) + "}";
        try {
            if (!file.exists())
                file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(tmp);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<String,Object> deserialize(File file){
        String str;
        try {
            Scanner scan = new Scanner(file);
            str = scan.nextLine();
            scan.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return  null;
        }

        ArrayList<String> mapEntries = getMapElement(str.substring(1,str.length()-2));
        Map<String,Object> map = new HashMap<>();
        for (String s : mapEntries){
            StringBuilder tmp = new StringBuilder(s);
            String k = getKey(tmp.toString());
            String v = getValue(tmp.toString());
            map.put(k,v);
        }
        return proceedObjects(map);
    }
}
