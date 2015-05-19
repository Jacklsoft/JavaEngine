package jacklsoft.jengine.units;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import jacklsoft.jengine.JEngine;
import jacklsoft.jengine.Model.Permission;

/**
 *
 * @author leonardo.mangano
 */
public class Main implements Initializable {
    static String rootName = "Units";
    static String password = "entrar";
    @FXML TreeView<String> aTree;
    @FXML TabPane tpMain;
    @FXML TitledPane tpTree;
    @FXML PasswordField pfAdmin;
    @FXML ToggleButton nightStyle;
    @FXML Button tbFullScreen;
    @FXML ImageView nightStyleImg;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        JEngine.ST.main = this;
        TreeItem<String> rootItem = new TreeItem(rootName);
        aTree.setRoot(rootItem);
        aTree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        aTree.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends TreeItem<String>> observable, TreeItem<String> oldValue, TreeItem<String> newValue) -> {
            if(newValue != null && newValue.getChildren().isEmpty()){
                goToUnit(newValue.getValue());
            }
        });
        pfAdmin.setOnAction((e) -> {
            if(pfAdmin.getText().equals(password)){
                pfAdmin.clear();
                goToUnit("Rights");
            }
        });
        refreshTree();
    }
    public void refreshTree(){
        aTree.getRoot().getChildren().clear();
        ObservableList<Permission> sql = Permission.query(System.getProperty("user.name"), null);
        for(Permission i: sql){
            if(i.getAllowed()){
                StringTokenizer tokenizer = new StringTokenizer((i.getFolder() == null ? "" : i.getFolder()), "/");
                TreeItem<String> path = aTree.getRoot();
                while(tokenizer.hasMoreTokens()){
                    String folder = tokenizer.nextToken();
                    boolean exists = false;
                    for(TreeItem<String> j: path.getChildren()){
                        if(folder.equals(j.getValue())){
                            exists = true;
                            path = j;
                        }
                    }
                    if(!exists){
                        TreeItem<String> newPath = new TreeItem(folder);
                        path.getChildren().add(newPath);
                        path = newPath;
                    }
                }
                TreeItem<String> newItem = new TreeItem(i.getUnit());
                path.getChildren().add(newItem);
            }
        }
    }
    public void goToUnit(String unitRoot){
            if(unitRoot != null && !unitRoot.equals(rootName)){
                try {
                    AnchorPane unit = FXMLLoader.load(getClass().getResource(unitRoot+".fxml"));
                    Tab newTab = new Tab(unitRoot);
                    newTab.setOnClosed((AE)->{
                        System.gc();
                    });
                    newTab.setContent(unit);
                    tpMain.getTabs().add(newTab);
                    tpMain.getSelectionModel().select(newTab);
                } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            aTree.getSelectionModel().clearAndSelect(0);
            tpTree.setExpanded(false);
    }
    public void setNightStyle(ActionEvent ae){
        if(nightStyle.isSelected()){
            nightStyleImg.setImage(new Image("/jacklsoft/jengine/img/whiteMoon.png"));
            JEngine.ST.scene.getStylesheets().add(getClass().getResource("/jacklsoft/jengine/units/NightStyle.css").toExternalForm());
        } else {
            nightStyleImg.setImage(new Image("/jacklsoft/jengine/img/blackMoon.png"));
            JEngine.ST.scene.getStylesheets().remove(getClass().getResource("/jacklsoft/jengine/units/NightStyle.css").toExternalForm());
        }
    }
    public void fullScreen(ActionEvent ae){
        JEngine.ST.stage.setFullScreen(!JEngine.ST.stage.isFullScreen());
    }
}
