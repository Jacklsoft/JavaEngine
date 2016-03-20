package jacklsoft.jengine.units;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import jacklsoft.jengine.JEngine;
import jacklsoft.jengine.Model.Permission;
import jacklsoft.jengine.controls.JBButton;
import jacklsoft.jengine.controls.JBContext;
import jacklsoft.jengine.controls.JBField;
import jacklsoft.jengine.controls.JBTable;
import jacklsoft.jengine.tools.AlertException;
import jacklsoft.jengine.tools.Tools;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;

public class Rights implements Initializable {
    @FXML JBField tfUser;
    @FXML JBButton bSave;
    @FXML ComboBox<String> cbUnit;
    @FXML JBTable<Permission> tvRights;
        @FXML TableColumn<Permission, String> tcRights_unit;
        @FXML TableColumn<Permission, Boolean> tcRights_allow;
    @FXML JBTable<Permission> tvUsers;
        @FXML TableColumn<Permission, String> tcUsers_user;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tcRights_unit.setCellValueFactory((cellData) -> new SimpleStringProperty(cellData.getValue().getUnit()));
        tcRights_allow.setCellFactory(CheckBoxTableCell.forTableColumn(tcRights_allow));
        tcRights_allow.setCellValueFactory(new PropertyValueFactory("allowed"));
        tvRights.getSelectionModel().selectedItemProperty().addListener((OL, OV, NV) -> {
            if(NV != null){
                cbUnit.setValue(NV.getUnit());
                cbUnitAction(null);
            }
        });
        
        tcUsers_user.setCellValueFactory((cellData-> new SimpleStringProperty(cellData.getValue().getUser())));
        tvUsers.getSelectionModel().selectedItemProperty().addListener((OL, OV, NV)->{
            if(NV != null){
                tfUser.putString(NV.getUser());
                tfUserAction(null);
            }
        });
        
        cbUnit.setItems(FXCollections.observableArrayList(JEngine.ST.getRights()));
        cbUnit.getSelectionModel().select(0);
        
        tvRights.init(()->{
            if(!tfUser.isNull()){
                try {
                    return Permission.query(tfUser.retString(), null);
                } catch (AlertException ex) {Tools.alertDialog(ex);}
            }
            return null;
        }, new JBContext());
        tvUsers.init(()->{
            if(!cbUnit.getItems().isEmpty()){
                return Permission.query(null, cbUnit.getValue());
            }
            return null;
        }, new JBContext());
    }
    
    @FXML public void tfUserAction(ActionEvent ae){
        tvRights.filter();
    }
    @FXML public void cbUnitAction(ActionEvent ae){
        tvUsers.filter();
    }
    @FXML public void bSaveAction(ActionEvent ae){
        tvRights.sendRows((row)->{
            return row.update();
        });
        tvRights.filter();
        tvUsers.filter();
        JEngine.ST.refreshRights();
    }
}
