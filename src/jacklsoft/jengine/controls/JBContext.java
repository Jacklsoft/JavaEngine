package jacklsoft.jengine.controls;

import java.util.HashMap;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.MenuItem;
import jacklsoft.jengine.interfaces.Control;
import jacklsoft.jengine.interfaces.Query;

/**
 *
 * @author leonardo.mangano
 */
public class JBContext extends ContextMenu{
    HashMap<String, Control> controls = new HashMap();
    EventHandler<ActionEvent> aeFilter;
    EventHandler<ActionEvent> aeFilterMethod;
    public JBContext(){
        super();
        aeFilter = (ae) -> {filter(ae);};
    }
    public void setFilterAction(EventHandler<ActionEvent> action){
        aeFilterMethod = action;
    }
    public final void filter(ActionEvent ae){
        if(aeFilterMethod != null){
            aeFilterMethod.handle(ae);
        }
    }
    public JBContext addAction(String nombre, EventHandler<ActionEvent> action){
        MenuItem MI = new MenuItem(nombre);
        getItems().add(MI);
        MI.setOnAction(action);
        return this;
    }
    public JBContext addField(String nombre, JBField field){
        field.setPromptText(nombre);
        field.setOnAction(aeFilter);
        CustomMenuItem CMI = new CustomMenuItem(field);
        CMI.setHideOnClick(false);
        getItems().add(CMI);
        controls.put(nombre, field);
        
        return this;
    }
    public JBContext addDate(String nombre, JBDateField field){
        field.setPromptText(nombre);
        field.setOnAction(aeFilter);
        CustomMenuItem CMI = new CustomMenuItem(field);
        CMI.setHideOnClick(false);
        getItems().add(CMI);
        controls.put(nombre, field);
        
        return this;
    }
    public <T> JBContext addCombo(String nombre, JBCombo combo, Query<T> query, JBContext _context){
        combo.init(query, _context);
        combo.setPromptText(nombre);
        combo.setOnAction(aeFilter);
        CustomMenuItem CMI = new CustomMenuItem(combo);
        CMI.setHideOnClick(false);
        getItems().add(CMI);
        controls.put(nombre, combo);
        
        return this;
    }
    public JBContext addCheck(String nombre, JBCheck check){
        check.setText(nombre);
        check.setOnAction(aeFilter);
        CustomMenuItem CMI = new CustomMenuItem(check);
        CMI.setHideOnClick(false);
        getItems().add(CMI);
        controls.put(nombre, check);
        
        return this;
    }
    public void reset(){
        for(Map.Entry<String, Control> i: controls.entrySet()){
            i.getValue().reset();
        }
    }
}
