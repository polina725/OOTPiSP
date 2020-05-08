package sample.serializersCreators.serializers;

import sample.devices.Device;
import sample.serializersCreators.Serializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


import static sample.helpClass.AdditionalFunction.convertStringToDeviceObject;

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
          //  e.printStackTrace();
        }
    }

    @Override
    public Map<String, Object> deserialize(File file){
        String str;
        try {
            Scanner scan = new Scanner(file);
            str = scan.nextLine();
            scan.close();
        } catch (FileNotFoundException e) {
           // e.printStackTrace();
            return  null;
        }

        ArrayList<String> mapEntries = getMapElement(str.substring(1,str.length()-2));
        Map<String,Object> map = new HashMap<>();
        for (String s : mapEntries){
            StringBuilder tmp = new StringBuilder(s);
            String k =  getKey(tmp.toString());
            String v = getValue(tmp.toString());
            map.put(k,v);
        }
        return convertStringToDeviceObject(map);
    }

    private static ArrayList<String> getMapElement(String str){
        ArrayList<String> arr = new ArrayList<>();
        String tmp;
        while (!str.isEmpty()){
            int index = str.indexOf(',');
            if (index != -1) {
                tmp = str.substring(0, index - 1);
                str = str.substring(index+1);
            }
            else {
                tmp = str;
                str = "";
            }
            arr.add(tmp);
        }
        return arr;
    }

    private static String getKey(String str){
        int index = str.indexOf(':');
        return str.substring(1,index-1);
    }

    private static String getValue(String str){
        int index = str.indexOf(':');
        return str.substring(index+2);
    }
}
