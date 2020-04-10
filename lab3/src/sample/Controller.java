package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.helpClass.FXUtilities;
import sample.helpClass.Reflection;
import sample.serializersCreators.Serializer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static sample.GlobalVariable.objectMap;
import static sample.helpClass.AdditionalFunction.getClassName;
import static sample.helpClass.AdditionalFunction.isNameUnique;
import static sample.helpClass.FXUtilities.chooseSerializer;
import static sample.helpClass.FXUtilities.placeNodesToComboBox;


public class Controller {

    @FXML
    private Button editButton;

    @FXML
    private ComboBox<String> deviceComboBox;

    @FXML
    private Button chooseDeviceButton;

    @FXML
    private TextField devicesNameTextFiled;

    @FXML
    private ComboBox<String> createdDevicesComboBox;

    @FXML
    private MenuItem loadObjects;

    @FXML
    private MenuItem saveObjects;

    @FXML
    private TextArea devicesListTextArea;

    @FXML
    void initialize(){

        deviceComboBox.getItems().add("PC");
        deviceComboBox.getItems().add("Laptop");
        deviceComboBox.getItems().add("WirelessHeadphones");

        objectMap = new HashMap<>();

        chooseDeviceButton.setOnAction(actionEvent -> {
            if (deviceComboBox.getValue()!=null && !devicesNameTextFiled.getText().equals("")&& isNameUnique(devicesNameTextFiled.getText())) {
                createPane(chooseDeviceButton.getScene(), "/sample/createObject.fxml",
                        "sample.devices." + deviceComboBox.getValue(), devicesNameTextFiled.getText(), null);
                if (objectMap.get(GlobalVariable.deviceName) != null) {
                    createdDevicesComboBox.getItems().add(GlobalVariable.deviceName);
                    redrawTextArea(devicesListTextArea,objectMap);
                }
                GlobalVariable.deviceName = null;
                GlobalVariable.currentClass = null;
            }
        });
        
        editButton.setOnAction(actionEvent -> {
            if (createdDevicesComboBox.getValue()!=null && !createdDevicesComboBox.getValue().equals("")) {
                GlobalVariable.editingObject = objectMap.get(createdDevicesComboBox.getValue());
                String name = getClassName(GlobalVariable.editingObject.getClass().toString());
                Map<String, String> fieldValues = new HashMap<>();
                createPane(editButton.getScene(), "/sample/editObject.fxml", name,
                        createdDevicesComboBox.getValue(), fieldValues);
                if (GlobalVariable.isDeleted){
                    createdDevicesComboBox.getItems().remove(GlobalVariable.deviceName);
                    GlobalVariable.isDeleted =false;
                }
                redrawTextArea(devicesListTextArea,objectMap);
                GlobalVariable.editingObject = null;
                GlobalVariable.deviceName = null;
            }
        });

        saveObjects.setOnAction(actionEvent -> {
            if (objectMap.size()!=0) {
                StringBuilder chosenFile = new StringBuilder();
                Serializer serializer = chooseSerializer(loadObjects.getParentPopup().getOwnerWindow(), chosenFile,true);
                if (serializer!=null)
                    serializer.serialize(objectMap, new File(chosenFile.toString()));
            }
        });

        loadObjects.setOnAction(actionEvent -> {
            StringBuilder chosenFile= new StringBuilder();
            Serializer serializer = chooseSerializer(loadObjects.getParentPopup().getOwnerWindow(),chosenFile,false);
            if (serializer!=null) {
                Map<String, Object> map = serializer.deserialize(new File(chosenFile.toString()));
                placeNodesToComboBox(map, createdDevicesComboBox);
                redrawTextArea(devicesListTextArea, objectMap);
            }
        });

    }

    public void redrawTextArea(TextArea ta,Map<String,Object> map){
        StringBuilder str= new StringBuilder();
        for (Map.Entry<String,Object> el: map.entrySet())
            str.append("Device's name: "+el.getKey()+" Field values: "+el.getValue()+"\n");
        ta.clear();
        ta.setText(str.toString());
    }

    public void createPane(Scene sc, String pathToFxmlFile,String className, String deviceName,Map<String,String> map){
        ArrayList<String> fieldNames = new ArrayList<>();
        try{
            GlobalVariable.currentClass = Class.forName(className); //"sample.devices." + deviceComboBox.getValue()
            Reflection.getAllFieldNames(GlobalVariable.currentClass,fieldNames);
        }catch(ClassNotFoundException  e) {
            e.printStackTrace();
        }

        if (map != null) try {
            Reflection.getFieldsValue(GlobalVariable.currentClass, GlobalVariable.editingObject,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        sc.getWindow().setOpacity(0.0);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(pathToFxmlFile));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        AnchorPane pane = new AnchorPane();
        pane.setStyle("-fx-border-color: green; -fx-border-width: 5px 5px 5px 5px");
        pane.setId("allFields");

        FXUtilities.createLabels(fieldNames,pane);
        FXUtilities.createTextFields(fieldNames,pane,map);
        GlobalVariable.deviceName = deviceName;
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        ((AnchorPane)root).getChildren().add(pane);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.showAndWait();
        sc.getWindow().setOpacity(1.0);

    }
}