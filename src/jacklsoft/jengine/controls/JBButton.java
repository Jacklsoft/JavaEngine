/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jacklsoft.jengine.controls;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Skin;
import jacklsoft.jengine.interfaces.Control;

/**
 *
 * @author MW
 */
public class JBButton extends Button implements Skin<JBButton>, Control{
    public JBButton(){
        super();
    }

    @Override
    public JBButton getSkinnable() {return this;}
    @Override
    public Node getNode() { return this;}
    @Override
    public void dispose() {}

    @Override
    public void reset() {}
}
