/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jacklsoft.jengine.interfaces;

import javafx.scene.Node;

/**
 *
 * @author MW
 * @param <T>
 */
public interface Control<T extends Node> {
    public void reset();
}
