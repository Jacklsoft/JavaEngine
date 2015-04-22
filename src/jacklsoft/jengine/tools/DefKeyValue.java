/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jacklsoft.jengine.tools;

import jacklsoft.jengine.interfaces.KeyValue;

/**
 *
 * @author MW
 * @param <K>
 * @param <V>
 */
public class DefKeyValue<K, V> implements KeyValue<K,V> {
    K key;
    V value;
    public DefKeyValue(K key, V value){
        this.key = key;
        this.value = value;
    }
    @Override
    public K key() { return key;}
    @Override
    public V value() { return value;}
    @Override
    public String toString(){return key.toString()+": "+value.toString();}
}
