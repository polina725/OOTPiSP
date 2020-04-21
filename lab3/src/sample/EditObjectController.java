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

public class EditObjectController implements Initializable ,ControllerInterface {
    private Class<?> currentClass;
    private Map<String,Object> objectMap;
    private String deviceName;
    private Controller mainWindowController;

    @Override
    public void setValues(Map<String,Object> map,String name,Class<?> currentClass,Controller con){
        this.currentClass =currentClass;
        this.objectMap=map;
        this.mainWindowController= con;
        this.deviceName=name;
    }

    @FXML
    private Button deleteButton;

    @FXML
    private Button saveButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        saveButton.setOnAction(actionEvent -> {
            Parent root  = saveButton.getScene().getRoot();
            Object editingObject = objectMap.get(this.deviceName);
            Map<String,String> textFields = FXUtilities.findTextFields((Pane)root);
            try {
                Reflection.setFieldsValue(this.currentClass,editingObject,textFields);
            } catch (IllegalAccessException | ClassNotFoundException | NoSuchMethodException |
                    InvocationTargetException | InstantiationException e) {
                e.printStackTrace();
            }
            this.objectMap.put(this.deviceName, editingObject);
            root.getScene().getWindow().hide();
        });

        deleteButton.setOnAction(actionEvent -> {
            this.objectMap.remove(this.deviceName);
            mainWindowController.setGlobalValues(objectMap);
            mainWindowController.setDeleted(true);
            Parent root  = saveButton.getScene().getRoot();
            root.getScene().getWindow().hide();
        });
    }
}
