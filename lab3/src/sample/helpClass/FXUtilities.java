package sample.helpClass;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import sample.serializersCreators.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

//import static sample.GlobalVariable.objectMap;

public class FXUtilities {
    public static void showAlertDialog(Alert.AlertType type, String str){
        Alert alert = new Alert(type);
        alert.setTitle("");
        alert.setHeaderText(null);
        alert.setContentText(str);
        alert.showAndWait();
    }

    public static void createLabels(ArrayList<String> list, AnchorPane pane){
        double topAnchor = 10.0, leftAnchor = 10.0;
        int fontSize = 20;
        Properties property = new Properties();
        try {
            FileInputStream fis = new FileInputStream("src/configs/config.properties");
            property.load(fis);
            fis.close();
        } catch (IOException e) {
         //   System.err.println("ОШИБКА: Файл свойств отсуствует!");
        }
        for (int i = list.size()-1; i>=0 ;i--) {
            String labelName = property.getProperty("db." + list.get(i));
            Label lb = new Label(labelName);
            lb.setFont(Font.font("Arial", fontSize));
            lb.setId(list.get(i) + "Label");
            AnchorPane.setTopAnchor(lb, topAnchor);
            AnchorPane.setLeftAnchor(lb, leftAnchor);
            pane.getChildren().add(lb);
            topAnchor += (fontSize + 8);
        }
    }

    public static void createTextFields(ArrayList<String> list, AnchorPane pane, Map<String,String> map){
        double topAnchor = 10.0, leftAnchor = 260.0;
        int height = 20;
        for (int i = list.size()-1; i>=0 ;i--) {
            TextField text = new TextField();
            if (map!=null){
                text.setText(map.get(list.get(i)));
            }
            text.setPrefHeight(height);
            text.setPrefWidth(250);
            text.setId(list.get(i));
            AnchorPane.setTopAnchor(text, topAnchor);
            AnchorPane.setLeftAnchor(text, leftAnchor);
            pane.getChildren().add(text);
            topAnchor += (height + 8);
        }
    }

    public static <T extends Pane> Map<String, String> findTextFields(T parent) {
        return findTextFields(parent, new HashMap<>());
    }

    private static <T extends Pane> Map<String, String> findTextFields(T parent, Map<String, String> map) {
        for (Node node : parent.getChildren()) {
            if (node instanceof TextField) {
                map.put( node.getId(), ((TextField) node).getText());
            }
            if (node instanceof Pane) {
                findTextFields((Pane) node, map);
            }
        }
        return map;
    }

    public static Serializer chooseSerializer(Window win, StringBuilder fileName, boolean saveMode, ArrayList<String> fileExtensionEndings){
        Creator[] creators ={new BinarySerializerCreator(),new JsonSerializerCreator(),new CustomSerializerCreator()};

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("./src/data"));
        String[][] filterName ={{"Binary (*.out)","*.out"},{"Json format (*.json)","*.json"},{"Text file (*.txt)","*.txt"}};
        ArrayList<String> list = new ArrayList<>();

        for (int i = 0; i<3;i++) {
            List<String> ext=new ArrayList<>();
            ext.add(filterName[i][1]);
            for (String end:fileExtensionEndings)
                ext.add(filterName[i][1].concat(end));
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(filterName[i][0], ext));
            list.add(filterName[i][0]);
        }
        File file;
        if (saveMode)
            file = fileChooser.showSaveDialog(win);//fileChooser.showOpenDialog(win);
        else
            file = fileChooser.showOpenDialog(win);
        if (file==null)
            return null;
        fileName.append(file.toString());
        int i = list.indexOf(fileChooser.getSelectedExtensionFilter().getDescription());
        return creators[i].create();
    }

    public static Map<String,Object> placeNodesToComboBox(Map<String,Object> objectMap,Map<String,Object> newMap, ComboBox<String> comboBox){
        if (objectMap.size()==0 && newMap!=null)
            objectMap=newMap;
        else if (newMap!=null){
            for (Map.Entry<String,Object> el : newMap.entrySet()){
                String tmp="";
                if (objectMap.containsKey(el.getKey())) { /////
                    int i = 0;
                    tmp = el.getKey();
                    while (objectMap.get(tmp) != null) {
                        tmp = el.getKey().concat(("_" + ++i));
                    }
                    tmp = el.getKey().concat("_" + i);
                }
                if (!tmp.equals(""))
                    objectMap.put(tmp,el.getValue());
                objectMap.put(el.getKey(),el.getValue());
            }
        }
        comboBox.getItems().clear();
        for(Map.Entry<String,Object> el:objectMap.entrySet())
            comboBox.getItems().add(el.getKey());
        return objectMap;
    }

}
