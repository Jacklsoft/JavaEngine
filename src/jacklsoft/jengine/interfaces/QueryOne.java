/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jacklsoft.jengine.interfaces;

/**
 *
 * @author leonardo.mangano
 * @param <T>
 */
public interface QueryOne<T> {
    public boolean match(T list, Object item);
}
