package sample.helpClass;

import javafx.scene.control.Alert;

import java.util.Map;

import static sample.GlobalVariable.objectMap;
import static sample.helpClass.FXUtilities.showAlertDialog;

public class AdditionalFunction {
    public static String getClassName(String tmp) {
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
}
