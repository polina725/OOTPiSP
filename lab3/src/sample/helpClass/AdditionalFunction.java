package sample.helpClass;

import javafx.scene.control.Alert;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Map;

import static sample.GlobalVariable.objectMap;
import static sample.helpClass.FXUtilities.showAlertDialog;

public class AdditionalFunction {
    public static String getClassName(String tmp){
        int fromIndex = tmp.indexOf(' ') + 1;
        int toIndex = tmp.length();
        tmp = tmp.substring(fromIndex, toIndex);
        return tmp;
    }

    public static boolean isNameUnique(String name){
        for (Map.Entry el:objectMap.entrySet())
            if(el.getKey().equals(name)) {
                showAlertDialog(Alert.AlertType.INFORMATION,"Each device must have a unique name.");
                return false;
            }
        return true;
    }

    public static Map<String,Object> proceedObjects(Map<String,Object> map) {
        for(Map.Entry<String,Object> el : map.entrySet()) {
            String tmp = (String) el.getValue();
            int ind = tmp.lastIndexOf('/');
            String str = "" + tmp.substring(0,ind-1);
            String className = "" + tmp.substring(ind+2);
            Constructor<?>[] con = new Constructor[0];
            try {
                con = Class.forName(getClassName(className)).getConstructors();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            Object obj = null;
            try {
                obj = con[1].newInstance(str);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            el.setValue(obj);
        }
        return map;
    }

    public static ArrayList<String> getMapElement(String str){
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

    public static String getKey(String str){
        int index = str.indexOf(':');
        return str.substring(1,index-1);
    }

    public static String getValue(String str){
        int index = str.indexOf(':');
        return str.substring(index+2);
    }
}
