/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jacklsoft.jengine.controls;

import java.sql.Date;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Skin;
import jacklsoft.jengine.interfaces.Control;
import jacklsoft.jengine.tools.Alert;

/**
 *
 * @author MW
 */
public class JBDateField extends DatePicker implements Skin<JBDateField>, Control{
    
    String defStyle;
    
    public JBDateField() {
        super();
        this.setShowWeekNumbers(true);
        this.setPromptText("dd/mm/yy");
        defStyle = this.getStyle();
    }
    @Override
    public void reset(){
        setStyle(defStyle);
        this.setValue(null);
    }
    public boolean isNull() {
        setStyle(defStyle);
        return (getValue() == null);
    }
    public void validate(boolean isValid, String message) throws Alert{
        if(isValid){
            setStyle(defStyle);
        } else {
            setStyle("-fx-background-color: #FF6666");
            throw new Alert("Valor erróneo", message);
        }
    }
    public void putDate(Date value){
        setValue((value == null ? null : value.toLocalDate()));
    }
    public Date retDate() throws Alert {
        if(isNull()){
                validate(false, "El campo "+getId()+" no puede quedar vacío, debe contener un fecha válida [dd/MM/yyyy].");
                return null;
        } else {
                Date RV = Date.valueOf(getValue());
                if(RV != null){
                        validate(true, null);
                        return RV;
                } else {
                        validate(false, "El campo "+getId()+" debe contener un fecha válida [dd/MM/yyyy].");
                        return null;
                }
        }
    }
    public Date retNDate() throws Alert {
        if(isNull()){
                validate(true, null);
                return null;
        } else {
                Date RV = Date.valueOf(getValue());
                if(RV != null){
                        validate(true, null);
                        return RV;
                } else {
                        validate(false, "El campo "+getId()+" debe contener un fecha válida [dd/MM/yyyy].");
                        return null;
                }
        }
    }
    
    @Override
    public JBDateField getSkinnable() { return this;}
    @Override
    public Node getNode() { return this; }
    @Override
    public void dispose() {}
}
