/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jacklsoft.jengine.controls;

import com.opencsv.CSVReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Skin;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.FileChooser;
import jacklsoft.jengine.JEngine;
import jacklsoft.jengine.interfaces.ArrayLoop;
import jacklsoft.jengine.interfaces.Control;
import jacklsoft.jengine.interfaces.Query;
import jacklsoft.jengine.interfaces.SendRow;
import jacklsoft.jengine.tools.Alert;
import jacklsoft.jengine.tools.Tools;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.SortType;

/**
 *
 * @author MW
 * @param <T>
 */
public class JBTable<T> extends TableView<T> implements Skin<JBTable>, Control{
    JBContext context;
    Query<T> query;
    EventHandler<ActionEvent> aeFilter = (ae) -> filter();
    EventHandler<ActionEvent> aeReset = (ae) -> reset();
    EventHandler<ActionEvent> aeCopy = (ae) -> copyData();
    boolean filterOnReset = true;
    
    public JBTable(){
        super();
        setTableMenuButtonVisible(true);
        getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        getSelectionModel().setCellSelectionEnabled(true);
    }
    public void init(Query<T> _query, JBContext _context){
        init(true, _query, _context);
    }
    public void init(boolean filterOnReset, Query<T> _query, JBContext _context){
        this.filterOnReset = filterOnReset;
        query = _query;
        if(_context != null){
            context = _context;
            context.setFilterAction(aeFilter);
            context.addAction("Consultar", aeFilter);
            context.addAction("Resetear", aeReset);
            context.addAction("Copiar", aeCopy);
            setContextMenu(context);
        } else {
            context = null;
            setContextMenu(null);
        }
        reset();
    }
    public void filter(){
        try {
            ObservableList<TableColumn<T, ?>> col = FXCollections.observableArrayList();
            for(TableColumn<T, ?> i: this.getSortOrder()){ col.add(i); }
            getItems().clear();
            if(query != null){
                ObservableList<T> D = query.execute();
                if(D != null){
                    setItems(query.execute());
                }
            }
            for(TableColumn<T, ?> i: col){ this.getSortOrder().add(i); }
        } catch (Alert ex) {Tools.alertDialog(ex);}
    }
    public void setFilterOnReset(boolean filter){
        this.filterOnReset = filter;
    }
    @Override
    public void reset(){
        if(context != null) context.reset();
        if(filterOnReset) filter();
        else getItems().clear();
    }
    public void removeSelectedRows(){
        for(T i: this.getSelectionModel().getSelectedItems()){
            getItems().remove(i);
        }
    }
    public void importCSV(ArrayLoop<T, String> action){
        FileChooser jfc = new FileChooser();
        File F = jfc.showOpenDialog(JEngine.ST.stage);
        if(F != null && F.getName().endsWith(".csv")){
            try {
                CSVReader csvr = new CSVReader(new FileReader(F));
                List<String[]> entries = csvr.readAll();
                ObservableList<T> datos = FXCollections.observableArrayList();
                for(String[] i: entries){
                    datos.add(action.loop(i));
                }
                getItems().clear();
                setItems(datos);
                csvr.close();
            } catch (FileNotFoundException e) {Tools.exceptionDialog("IO Error", "No se encuentra el archivo", e);
            } catch (IOException e) {Tools.exceptionDialog("IO Error", "Error en la lecutra del archivo", e);}
        }
    }
    public void pasteClipboard(ArrayLoop<T, String> action){
        Scanner data = Tools.getClipboard();
        if(data != null){
            int cols = data.nextInt();
            cols = (this.getColumns().size() < cols ? this.getColumns().size() : cols);
            String[] newRow = new String[cols];
            
            ObservableList<T> datos = FXCollections.observableArrayList();
            while(data.hasNext()){
                for(int i = 0; i < cols; i++){
                    newRow[i] = data.next();
                }
                getItems().add(action.loop(newRow));
            }
        }
    }
    public void copyData(){
        SortedList<TablePosition> posList = getSelectionModel().getSelectedCells().sorted(
                (TablePosition tp1, TablePosition tp2) -> {
                    if(Integer.compare(tp1.getRow(),tp2.getRow()) == 0){
                        return Integer.compare(tp1.getColumn(), tp2.getColumn());
                    } else {
                        return Integer.compare(tp1.getRow(),tp2.getRow());
                    }
                });
        int old_r = -1;
        StringBuilder clipboardString = new StringBuilder();
        for (TablePosition p : posList) {
            int r = p.getRow();
            int c = p.getColumn();
            Object cell = getColumns().get(c).getCellData(r);
            if (cell == null) cell = "";
            if (old_r == r) clipboardString.append('\t');
            else if (old_r != -1) clipboardString.append('\n');
            clipboardString.append(cell);
            old_r = r;
        }
        final ClipboardContent content = new ClipboardContent();
        content.putString(clipboardString.toString());
        Clipboard.getSystemClipboard().setContent(content);
    }
    public void sendRows(SendRow<T> action){
        for(Iterator<T> i = getItems().iterator(); i.hasNext();){
            if(action.send(i.next())){
                i.remove();
            }
        }
    }
    
    @Override
    public JBTable getSkinnable() { return this; }
    @Override
    public Node getNode() { return this; }
    @Override
    public void dispose() {}
}
