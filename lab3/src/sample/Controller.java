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
import sample.serializersCreators.serializers.JsonSerializer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//import static sample.GlobalVariable.objectMap;
import static sample.helpClass.AdditionalFunction.getClassName;
import static sample.helpClass.AdditionalFunction.isNameUnique;
import static sample.helpClass.FXUtilities.chooseSerializer;
import static sample.helpClass.FXUtilities.placeNodesToComboBox;


public class Controller {
    private Controller selfReferent;
    private Map<String,Object> objectMap;
    private boolean isDeleted;

    public void setGlobalValues(Map<String,Object> map){
        this.objectMap=map;
    }

    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
    }

    public void setSelfReferent() {
        this.selfReferent = this;
    }
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

        this.objectMap = new HashMap<>();

        chooseDeviceButton.setOnAction(actionEvent -> {
            String deviceName=devicesNameTextFiled.getText();
            if (deviceName!=null && !deviceName.equals("")&& isNameUnique(this.objectMap,deviceName)) {
                createPane(chooseDeviceButton.getScene(), "/sample/createObject.fxml",
                        "sample.devices." + deviceComboBox.getValue(), deviceName, null,null);
                if (this.objectMap.get(deviceName) != null) {
                    createdDevicesComboBox.getItems().add(deviceName);
                    redrawTextArea(devicesListTextArea,this.objectMap);
                }
            }
        });
        
        editButton.setOnAction(actionEvent -> {
            String deviceName=createdDevicesComboBox.getValue();
            if (deviceName!=null && !deviceName.equals("")) {
                Object editingObject = this.objectMap.get(deviceName);
                String name = getClassName(editingObject.getClass().toString());
                Map<String, String> fieldValues = new HashMap<>();
                createPane(editButton.getScene(), "/sample/editObject.fxml", name,
                        deviceName, fieldValues,editingObject);
                if (this.isDeleted){
                    createdDevicesComboBox.getItems().remove(deviceName);
                    this.isDeleted =false;
                }
                redrawTextArea(devicesListTextArea,objectMap);
            }
        });

        saveObjects.setOnAction(actionEvent -> {
            if (this.objectMap.size()!=0) {
                StringBuilder chosenFile = new StringBuilder();
                Serializer serializer = chooseSerializer(loadObjects.getParentPopup().getOwnerWindow(), chosenFile,true);
                if (serializer!=null)
                    serializer.serialize(this.objectMap, new File(chosenFile.toString()));
            }
        });

        loadObjects.setOnAction(actionEvent -> {
            StringBuilder chosenFile= new StringBuilder();
            Serializer serializer = chooseSerializer(loadObjects.getParentPopup().getOwnerWindow(),chosenFile,false);
            if (serializer!=null) {
                Map<String, Object> map = serializer.deserialize(new File(chosenFile.toString()));
                objectMap = placeNodesToComboBox(objectMap,map, createdDevicesComboBox);
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

    public void createPane(Scene sc, String pathToFxmlFile,String className, String deviceName,Map<String,String> map,Object editingObject){
        ArrayList<String> fieldNames = new ArrayList<>();
        Class<?> currentClass=null;
        try{
            currentClass = Class.forName(className); //"sample.devices." + deviceComboBox.getValue()
            Reflection.getAllFieldNames(currentClass,fieldNames);
        }catch(ClassNotFoundException  e) {
            e.printStackTrace();
        }

        if (map != null) try {
            Reflection.getFieldsValue(currentClass, editingObject,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

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
        ControllerInterface con = loader.getController();
        con.setValues(this.objectMap,deviceName,currentClass,selfReferent);
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        ((AnchorPane)root).getChildren().add(pane);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        sc.getWindow().setOpacity(0.0);
        stage.showAndWait();
        sc.getWindow().setOpacity(1.0);

    }
}