/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jacklsoft.jengine.controls;

import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Skin;
import jacklsoft.jengine.interfaces.Control;
/**
 *
 * @author MW
 */
public class JBCheck extends CheckBox implements Skin<JBCheck>, Control{
    
    public JBCheck() {
        super();
    }
    @Override
    public void reset(){
        setSelected(false);
    }
    public void putBoolean(boolean selected){
        setSelected(selected);
    }
    public boolean retBoolean(){
        return isSelected();
    }
    
    @Override
    public JBCheck getSkinnable() { return this; }
    @Override
    public Node getNode() { return this; }
    @Override
    public void dispose() {}
}
