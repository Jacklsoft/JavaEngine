package jacklsoft.jengine.controls;

import com.sun.javafx.scene.control.skin.ComboBoxListViewSkin;
import java.util.function.Predicate;

import jacklsoft.jengine.tools.AlertException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Skin;
import javafx.scene.input.KeyCode;
import jacklsoft.jengine.interfaces.Control;
import jacklsoft.jengine.interfaces.KeyValue;
import jacklsoft.jengine.interfaces.Query;
import jacklsoft.jengine.tools.Tools;

/**
 *
 * @author leonardo.mangano
 * @param <T>
 * @param <K>
 * @param <V>
 */
public class JBCombo<T extends KeyValue<K, V>, K, V> extends ComboBox<T> implements Skin<JBCombo>, Control{
    Query<T> query;
    JBField searchField = new JBField();
    
    Predicate<T> pFilter1 = (value)->{
        if(searchField.isNull()){
            return true;
        } else {
            return value.toString().toUpperCase().contains(searchField.getText().toUpperCase());
        }
    };
    Predicate<T> pFilter2 = (value)->{
        if(searchField.isNull()){
            return true;
        } else {
            return value.toString().toUpperCase().contains(searchField.getText().toUpperCase());
        }
    };
    
    ObservableList<T> data = FXCollections.observableArrayList();
    FilteredList<T> filtered = new FilteredList(data, pFilter1);
    
    JBContext context;
    EventHandler<ActionEvent> aeFilter = (ae) -> filter();
    EventHandler<ActionEvent> aeClear = (ae) -> getSelectionModel().clearSelection();
    String defStyle;
    
    public JBCombo(){
        super();
        defStyle = this.getStyle();
        skinProperty().addListener((OL, OV, NV)->{
            if(NV instanceof ComboBoxListViewSkin){
                ((ComboBoxListViewSkin)NV).getListView().setOnKeyPressed((AL)->{
                    if (AL.getCode() == KeyCode.ENTER ||
                            AL.getCode() == KeyCode.ESCAPE) {
                        hide();
                    }
                });
            }
        });
        setOnHidden((AL)->{
            if(context != null){
                context.hide();
            }
        });
        setOnContextMenuRequested((AL)->{
            if(!isShowing() && context != null) context.hide();
        });
        searchField.textProperty().addListener((OL, OV, NV)->{
            search();
        });
    }
    public void init(Query<T> _query, JBContext _context){
        query = _query;
        if(_context != null){
            context = _context;
            context.setFilterAction(aeFilter);
            context.addField("Filtro", searchField);
            context.addAction("Refrescar", aeFilter);
            context.addAction("Vaciar", aeClear);
            setContextMenu(context);
        } else {
            context = null;
            setContextMenu(null);
        }
        filter();
    }
    public void filter(){
        try {
            data.clear();
            ObservableList<T> D = query.execute();
            if(D != null){
                data.setAll(D);
                setItems(filtered);
                this.getSelectionModel().clearSelection();
            }
        } catch (AlertException ex) {Tools.alertDialog(ex);}
    }
    @Override
    public void reset(){
        setStyle(defStyle);
        searchField.setText("");
        this.getSelectionModel().clearSelection();
    }
    public void setRedStyle(){
        setStyle("-fx-border-color: #FF6666");
    }
    public void search(){
        filtered.setPredicate(pFilter1);
        filtered.setPredicate(pFilter2);
        setItems(filtered);
        if(isFocused() && !context.isShowing()) show();
    }
    public boolean isNull(){
        setStyle(defStyle);
        return getSelectionModel().getSelectedItem() == null;
    }
    public void putItem(K item){
        reset();
        if(item == null){
            this.getSelectionModel().clearSelection();
        } else{
            for(T i: getItems()){
                if((i.key().getClass().equals(String.class)) && (((String) i.key()).compareToIgnoreCase((String) item)) == 0){
                    this.getSelectionModel().select(i);
                    return;
                } else if(i.key().equals(item)){
                    this.getSelectionModel().select(i);
                    return;
                }
            }
            setRedStyle();
            Tools.errorDialog("Error message", "El valor \""+item.toString()+"\" del campo "+getId()+" no existe o es obsoleto.");
            this.getSelectionModel().clearSelection();
        }
    }
    public T retNItem() throws AlertException {
        setStyle(defStyle);
        return getSelectionModel().getSelectedItem();
    }
    public T retItem() throws AlertException {
        T RV = getSelectionModel().getSelectedItem();
        if(RV == null){
            setRedStyle();
            throw new AlertException("Error message", "El campo "+getId()+" se encuentra vacío");
        } else {
            setStyle(defStyle);
            return RV;
        }
    }
    public K retNKey() throws AlertException {
        setStyle(defStyle);
        T item = retNItem();
        if(item == null){
            return null;
        } else return item.key();
    }
    public K retKey() throws AlertException {
        return retItem().key();
    }
    public V retNValue() throws AlertException {
        setStyle(defStyle);
        T item = retNItem();
        if(item == null){
            return null;
        } else return item.value();
    }
    public V retValue() throws AlertException {
        return retItem().value();
    }
    
    @Override
    public JBCombo getSkinnable() { return this; }
    @Override
    public Node getNode() { return this; }
    @Override
    public void dispose() {}
}
