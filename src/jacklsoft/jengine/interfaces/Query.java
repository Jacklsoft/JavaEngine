/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jacklsoft.jengine.interfaces;

import javafx.collections.ObservableList;
import jacklsoft.jengine.tools.AlertException;

/**
 *
 * @author leonardo.mangano
 * @param <T>
 */
public interface Query<T> {
    public ObservableList<T> execute() throws AlertException;
}
