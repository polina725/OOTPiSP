package sample;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import sample.helpClass.FXUtilities;
import sample.helpClass.Reflection;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class EditObjectController implements Initializable {

    @FXML
    private Button deleteButton;

    @FXML
    private Button saveButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        saveButton.setOnAction(actionEvent -> {
            Parent root  = saveButton.getScene().getRoot();

            Map<String,String> textFields = FXUtilities.findTextFields((Pane)root);
            try {
                Reflection.setFieldsValue(GlobalVariable.currentClass, GlobalVariable.editingObject,textFields);
            } catch (IllegalAccessException | ClassNotFoundException | NoSuchMethodException |
                    InvocationTargetException | InstantiationException e) {
                e.printStackTrace();
            }
            GlobalVariable.objectMap.put(GlobalVariable.deviceName, GlobalVariable.editingObject);
            root.getScene().getWindow().hide();
        });

        deleteButton.setOnAction(actionEvent -> {
            GlobalVariable.objectMap.remove(GlobalVariable.deviceName);
            GlobalVariable.isDeleted=true;
            Parent root  = saveButton.getScene().getRoot();
            root.getScene().getWindow().hide();
        });
    }
}
