/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jacklsoft.jengine.interfaces;

/**
 *
 * @author MW
 * @param <K>
 * @param <V>
 */
public interface KeyValue<K, V> {
    public K key();
    public V value();
}
