package sample.serializersCreators.serializers;
import javafx.scene.control.Alert;
import sample.serializersCreators.Serializer;

import java.io.*;
import java.util.Map;

import static sample.helpClass.FXUtilities.showAlertDialog;

public class BinarySerializer implements Serializer {
    @Override
    public void serialize(Map<String,Object> map,File file){
        try {
            if (!file.exists())
                file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(map);
            objectOutputStream.close();
        } catch (IOException e) {
         //   e.printStackTrace();
        }

    }

    @Override
    public  Map<String,Object> deserialize(File file){
        Map<String,Object> map;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
            map = (Map<String, Object>) objectInputStream.readObject();
            objectInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
      //      showAlertDialog(Alert.AlertType.ERROR,"An error occurred");
            map = null;
        }
        return map;
    }
}
