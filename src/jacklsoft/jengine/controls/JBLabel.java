package jacklsoft.jengine.controls;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;
import jacklsoft.jengine.interfaces.Control;
import jacklsoft.jengine.tools.AlertException;

public class JBLabel extends Label implements Skin<JBLabel>, Control{
    @SuppressWarnings("compatibility:-1465854168171926622")
    private static final long serialVersionUID = 1L;

    public JBLabel() {
        super();
        setStyle("-fx-foreground-color: #00FF00");
    }
    @Override
    public void reset(){
        setText("*");
    }
    public boolean isNull(){
        return (getText().equals("*"));
    }
    public void putString(String value){
        if(value == null){
            reset();
        } else {
            setText(value);
        }
    }
    public String retString() throws AlertException {
        if(isNull()){
                throw new AlertException("Valor erróneo", "El campo "+getId()+" no puede quedar vacío.");
        } else {
                return getText();
        }
    }
    public String retNString() throws AlertException {
        if(isNull()){
                return null;
        } else {
                return getText();
        }
    }

    @Override public JBLabel getSkinnable() { return this; }
    @Override public Node getNode() { return this; }
    @Override public void dispose() {}
}
