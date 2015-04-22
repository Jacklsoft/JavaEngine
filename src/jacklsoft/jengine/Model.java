package jacklsoft.jengine;

import java.sql.SQLException;
import javafx.collections.ObservableList;
import jacklsoft.jengine.db.SQLServer;
import jacklsoft.jengine.interfaces.KeyValue;
import java.util.HashSet;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Model {
    public static class Permission implements KeyValue<String, String>{
        @Override public String key() {return getUnit();}
        @Override public String value() {return getUnit();}
        @Override public String toString() {return getUnit();}
        
        String user;
        String unit;
        String folder;
        private SimpleBooleanProperty allowed;
        
        public Permission(  String user,
                            String unit,
                            String folder,
                            boolean allowed){
            this.user = user;
            this.unit = unit;
            this.folder = folder;
            this.allowed = new SimpleBooleanProperty(allowed);
        }
        public static ObservableList<Permission> query(
                String user,
                String unit){
            HashSet<String> allowedRights = new HashSet();
            try{
                SQLServer sql = new SQLServer("{CALL _Permission_get(?,?)}");
                sql.setString("user", user);
                sql.setString("unit", unit);
                sql.query();
                while(sql.next()){
                    sql.addItem(new Permission( 
                            sql.getString("user"),
                            sql.getString("unit"),
                            sql.getString("folder"),
                            true));
                    
                    if(user != null) allowedRights.add(sql.getString("unit"));
                }
                if(user != null){
                    for(String i: JEngine.ST.getRights()){
                        if(!allowedRights.contains(i)){
                            sql.addItem(new Permission(
                                    user,
                                    i,
                                    null,
                                    false
                            ));
                        }
                    }
                }
                sql.end();
                return sql.getResult();
            }catch(SQLException e){SQLServer.showException(e);}
            return null;
        }
        public boolean update(){
            try{
                SQLServer sql = new SQLServer("{CALL _Permission_set(?,?,?,?)}");
                sql.setString("user", getUser());
                sql.setString("unit", getUnit());
                sql.setString("folder", getFolder());
                sql.setBoolean("allowed", getAllowed());
                sql.update();
                sql.end();
                return true;
            }catch(SQLException e){SQLServer.showException(e);}
            return false;
        }
        
        public String getUser() {return user;}
        public String getUnit() {return unit;}
        public String getFolder() {return folder;}
        public boolean getAllowed() {
            return this.allowed.get();
        }
        public void setAllowed(boolean allowed){
            this.allowed.set(allowed);
        }
        public BooleanProperty allowedProperty(){
            return allowed;
        }
    }
}