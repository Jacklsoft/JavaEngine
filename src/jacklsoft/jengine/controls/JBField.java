package jacklsoft.jengine.controls;

import java.sql.Date;

import jacklsoft.jengine.tools.AlertException;
import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.control.TextField;
import jacklsoft.jengine.interfaces.Control;
import jacklsoft.jengine.tools.Tools;

public class JBField extends TextField implements Skin<JBField>, Control{
    @SuppressWarnings("compatibility:-5360705238910717593")
    private static final long serialVersionUID = 1L;
    
    String defStyle;
    
    public JBField() {
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
    public void validate(boolean isValid, String message) throws AlertException {
        if(isValid){
            setStyle(defStyle);
        } else {
            setStyle("-fx-base: #FF6666");
            setStyle("-fx-background: #FF6666");
            setStyle("-fx-control-inner-background: #FF6666");
            throw new AlertException("Valor erróneo", message);
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
        setText(value == null ? "" : String.valueOf(value));
    }
    public void putDouble(Double value){
        setText(value == null ? "" : String.valueOf(value));
    }
    public Integer retInt() throws AlertException {
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
    public Integer retNInt() throws AlertException {
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
    public String retString() throws AlertException {
        if(isNull()){
                validate(false, "El campo "+getId()+" no puede quedar vacío.");
                return null;
        } else {
                validate(true, null);
                return getText();
        }
    }
    public String retNString() throws AlertException {
        if(isNull()){
                validate(true, null);
                return null;
        } else {
                validate(true, null);
                return getText();
        }
    }
    public Date retDate() throws AlertException {
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
    public Date retNDate() throws AlertException {
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
    public Float retFloat() throws AlertException {
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
    public Float retNFloat() throws AlertException {
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
    public Double retDouble() throws AlertException {
        if(isNull()){
                validate(false, "El campo "+getId()+" no puede quedar vacío, debe contener un número decimal válido.");
                return null;
        } else {
                try{
                        Double RV = Double.valueOf(getText());
                        validate(true, null);
                        return RV;
                }catch(NumberFormatException e){
                        validate(false, "El campo "+getId()+" debe contener un número decimal válido.");
                        return null;
                }
        }
    }
    public Double retNDouble() throws AlertException {
        if(isNull()){
                validate(true, null);
                return null;
        } else {
                try{
                        Double RV = Double.valueOf(getText());
                        validate(true, null);
                        return RV;
                }catch(NumberFormatException e){
                        validate(false, "El campo "+getId()+" debe contener un número decimal válido.");
                        return null;
                }
        }
    }

    @Override public JBField getSkinnable() { return this; }
    @Override public Node getNode() { return this; }
    @Override public void dispose() {}
}
