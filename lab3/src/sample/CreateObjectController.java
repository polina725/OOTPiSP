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

public class CreateObjectController implements Initializable ,ControllerInterface {
    private Class<?> currentClass;
    private Map<String,Object> objectMap;
    private String deviceName;
    private Controller mainWindowController;

    public void setValues(Map<String,Object> map,String deviceName,Class<?> c,Controller con){
        this.currentClass =c;
        this.objectMap=map;
        this.deviceName=deviceName;
        this.mainWindowController= con;
    }

    @FXML
    private Button createButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createButton.setOnAction(actionEvent -> {
            Parent root  = createButton.getScene().getRoot();

            Map<String,String> textFields = FXUtilities.findTextFields((Pane)root);

            try {
                Object obj = this.currentClass.getDeclaredConstructor().newInstance();
                Reflection.setFieldsValue(this.currentClass,obj,textFields);
                this.objectMap.put(this.deviceName,obj);
                mainWindowController.setGlobalValues(this.objectMap);
            } catch (InstantiationException | InvocationTargetException | NoSuchMethodException |
                    IllegalAccessException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            root.getScene().getWindow().hide();
        });
    }
}