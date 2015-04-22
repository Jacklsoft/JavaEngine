package jacklsoft.jengine.controls;

import java.sql.Date;
import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.control.TextArea;
import jacklsoft.jengine.interfaces.Control;
import jacklsoft.jengine.tools.Alert;
import jacklsoft.jengine.tools.Tools;

/**
 *
 * @author MW
 */
public class JBTextArea extends TextArea implements Skin<JBTextArea>, Control{

    String defStyle;
    
    public JBTextArea() {
        super();
        defStyle = this.getStyle();
    }
    @Override
    public void reset(){
        setStyle(defStyle);
        setText(null);
    }
    public boolean isNull() {
        setStyle(defStyle);
        return (getText() == null || getText().equals(""));
    }
    public void validate(boolean isValid, String message) throws Alert{
        if(isValid){
            setStyle(defStyle);
        } else {
            setStyle("-fx-background-color: #FF6666");
            throw new Alert("Valor erróneo", message);
        }
    }
    public void putInt(Integer value){
        setText(value == null ? "" : String.valueOf(value));
    }
    public void putString(String value){
        setText(value == null ? "" : value);
    }
    public void putDate(Date value){
        setText(Tools.dateToString(value));
    }
    public void putFloat(Float value){
        setText(String.valueOf(value));
    }
    public Integer retInt() throws Alert {
        if(isNull()){
                validate(false, "El campo "+getId()+" no puede quedar vacío, debe contener un número entero válido.");
                return null;
        } else {
                try{
                        Integer RV = Integer.valueOf(getText());
                        validate(true, null);
                        return RV;
                }catch(NumberFormatException e){
                        validate(false, "El campo "+getId()+" debe contener un número entero válido.");
                        return null;
                }
        }
    }
    public Integer retNInt() throws Alert {
        if(isNull()){
                validate(true, null);
                return null;
        } else {
                try{
                        Integer RV = Integer.valueOf(getText());
                        validate(true, null);
                        return RV;
                }catch(NumberFormatException e){
                        validate(false, "El campo "+getId()+" debe contener un número entero válido.");
                        return null;
                }
        }
    }
    public String retString() throws Alert {
        if(isNull()){
                validate(false, "El campo "+getId()+" no puede quedar vacío.");
                return null;
        } else {
                validate(true, null);
                return getText();
        }
    }
    public String retNString() throws Alert {
        if(isNull()){
                validate(true, null);
                return null;
        } else {
                validate(true, null);
                return getText();
        }
    }
    public Date retDate() throws Alert {
        if(isNull()){
                validate(false, "El campo "+getId()+" no puede quedar vacío, debe contener un fecha válida [dd/MM/yyyy].");
                return null;
        } else {
                Date RV = Tools.stringToDate(getText());
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
                Date RV = Tools.stringToDate(getText());
                if(RV != null){
                        validate(true, null);
                        return RV;
                } else {
                        validate(false, "El campo "+getId()+" debe contener un fecha válida [dd/MM/yyyy].");
                        return null;
                }
        }
    }
    public Float retFloat() throws Alert {
        if(isNull()){
                validate(false, "El campo "+getId()+" no puede quedar vacío, debe contener un número decimal válido.");
                return null;
        } else {
                try{
                        Float RV = Float.valueOf(getText());
                        validate(true, null);
                        return RV;
                }catch(NumberFormatException e){
                        validate(false, "El campo "+getId()+" debe contener un número decimal válido.");
                        return null;
                }
        }
    }
    public Float retNFloat() throws Alert {
        if(isNull()){
                validate(true, null);
                return null;
        } else {
                try{
                        Float RV = Float.valueOf(getText());
                        validate(true, null);
                        return RV;
                }catch(NumberFormatException e){
                        validate(false, "El campo "+getId()+" debe contener un número decimal válido.");
                        return null;
                }
        }
    }
    
    @Override
    public JBTextArea getSkinnable() { return this; }
    @Override
    public Node getNode() { return this; }
    @Override
    public void dispose() {}
}
