package sample;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import sample.helpClass.FXUtilities;
import sample.helpClass.Reflection;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.*;

public class CreateObjectController implements Initializable {

    @FXML
    private Button createButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createButton.setOnAction(actionEvent -> {
            Parent root  = createButton.getScene().getRoot();

            Map<String,String> textFields = FXUtilities.findTextFields((Pane)root);

            try {
                Object obj = GlobalVariable.currentClass.getDeclaredConstructor().newInstance();
                Reflection.setFieldsValue(GlobalVariable.currentClass,obj,textFields);
                GlobalVariable.objectMap.put(GlobalVariable.deviceName,obj);
            } catch (InstantiationException | InvocationTargetException | NoSuchMethodException |
                    IllegalAccessException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            root.getScene().getWindow().hide();
        });
    }
}