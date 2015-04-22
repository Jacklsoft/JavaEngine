/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jacklsoft.jengine.interfaces;

import javax.json.JsonObject;

/**
 *
 * @author leonardo.mangano
 */
public interface Neo4jQuery<T> {
    public T getRecord(JsonObject jo);
}
