package sample;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.helpClass.FXUtilities;
import sample.helpClass.Reflection;

import sample.pluginSupport.Plugin;
import sample.pluginSupport.PluginLoader;
import sample.serializersCreators.Serializer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static sample.helpClass.AdditionalFunction.getClassName;
import static sample.helpClass.AdditionalFunction.isNameUnique;
import static sample.helpClass.FXUtilities.*;


public class Controller {
    private Controller selfReferent;
    private Map<String,Object> objectMap;
    private boolean isDeleted;
    private PluginLoader pluginLoader;

    public void setGlobalValues(Map<String,Object> map){
        this.objectMap=map;
    }

    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
    }

    public void setSelfReferent() {
        this.selfReferent = this;
    }

    public void setPluginLoader(){this.pluginLoader=new PluginLoader();}
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
   //     this.pluginLoader=new PluginLoader();

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
                Serializer serializer = chooseSerializer(loadObjects.getParentPopup().getOwnerWindow(), chosenFile,
                        true,pluginLoader.getAllFileExtensionEndings());

                //Plugin encoder =
                if (serializer!=null) {
                    Plugin encoder = encoderChooser();
                    serializer.serialize(this.objectMap, new File(chosenFile.toString()));
                    if (encoder!=null) {
                        try {
                            encoder.encode(chosenFile.toString());
                        } catch (IOException ignored) {}
                    }
                }
            }
        });

        loadObjects.setOnAction(actionEvent -> {
            StringBuilder chosenFile= new StringBuilder();
            Serializer serializer = chooseSerializer(loadObjects.getParentPopup().getOwnerWindow(),chosenFile,
                                                           false,pluginLoader.getAllFileExtensionEndings());
            if (serializer!=null) {
                StringBuilder newFileName=new StringBuilder();
                SimpleBooleanProperty fl = new SimpleBooleanProperty(false);
                Plugin decoder = checkFileForEncoding(chosenFile.toString(),newFileName,fl);
                if (!fl.get()) {
                    if (decoder != null) {
                        try {
                            decoder.decode(chosenFile.toString());
                        } catch (IOException ignored) {
                        }
                    } else
                        newFileName.append(chosenFile.toString());
                    Map<String, Object> map = serializer.deserialize(new File(newFileName.toString()));
                    objectMap = placeNodesToComboBox(objectMap, map, createdDevicesComboBox);
                    redrawTextArea(devicesListTextArea, objectMap);
                }
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
            currentClass = Class.forName(className);
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

    public Plugin encoderChooser(){
        if (this.pluginLoader.getPlugins().size()==0)
            return null;
        ChoiceDialog<String> chDialog = new ChoiceDialog<>("",this.pluginLoader.getAllPluginNames());
        chDialog.setTitle("Post processing");
        chDialog.setHeaderText("Do you want to encode you output file?");
        chDialog.setContentText("Choose your encoder:");
        Optional<String> result = chDialog.showAndWait();
        if (result.isPresent()&&!result.get().equals("")){
            int i = this.pluginLoader.getAllPluginNames().indexOf(result.get());
            return this.pluginLoader.getPlugins().get(i);
        }
        return null;
    }

    public Plugin checkFileForEncoding(String fileName, StringBuilder newFileName, SimpleBooleanProperty fl){
        String extension = fileName.substring(fileName.lastIndexOf('.'));
        String[] fileExtensions = {".txt",".out",".json"};
        if (this.pluginLoader.getPlugins().size()==0) {
            for (String ext : fileExtensions)
                if (extension.contains(ext) && !extension.equals(ext)) {
                    showAlertDialog(Alert.AlertType.ERROR, "There is no any plugin for this file");
                    fl.set(true);
                    return null;
                }
        }
        else
            for (String ending : this.pluginLoader.getAllFileExtensionEndings())
                if(extension.endsWith(ending)) {
                    String tmp = fileName.substring(0,fileName.length()-ending.length());
                    newFileName.append(tmp);
                    int i= this.pluginLoader.getAllFileExtensionEndings().indexOf(ending);
                    return this.pluginLoader.getPlugins().get(i);
                }
        return null;
    }
}

// The Java 8 way to get the response value (with lambda expression).
//        result.ifPresent(letter -> System.out.println("Your choice: " + letter));