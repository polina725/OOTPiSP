package sample.helpClass;

import javafx.scene.control.Alert;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import static sample.helpClass.FXUtilities.showAlertDialog;

public class AdditionalFunction {
    public static String getClassName(String tmp){
        int fromIndex = tmp.indexOf(' ') + 1;
        int toIndex = tmp.length();
        tmp = tmp.substring(fromIndex, toIndex);
        return tmp;
    }
////////
    public static boolean isNameUnique(Map<String,Object> map, String name){
        if(map.containsKey(name)) {
            showAlertDialog(Alert.AlertType.INFORMATION,"Each device must have a unique name.");
            return false;
        }
        return true;
    }

    public static Map<String,Object> convertStringToDeviceClass(Map<String,Object> map) {
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

}
