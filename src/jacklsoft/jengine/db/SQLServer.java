package jacklsoft.jengine.db;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.io.ByteArrayInputStream;

import java.io.InputStream;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import jacklsoft.jengine.tools.Tools;

public class SQLServer<T>{
    static Connection db;
    
    CallableStatement cs;
    ResultSet rs;
    ObservableList<T> result;
    public static String version;
    
    public static boolean checkVersion(){
        boolean RV = false;
        String newVersion = "";
        try {
            CallableStatement sql = db.prepareCall("{CALL _Config_get(?,?,?,?,?)}");
            sql.setString("ID", "version");
            sql.setNull("stringValue", Types.VARCHAR);
            sql.setNull("intValue", Types.INTEGER);
            sql.setNull("floatValue", Types.FLOAT);
            sql.setNull("dateValue", Types.DATE);
            ResultSet rs = sql.executeQuery();
            if(rs.next()){
                newVersion = rs.getString("stringValue");
                if(newVersion.equals(version)){
                    RV = true;
                }
            }
            sql.close();
        } catch (SQLException ex) { SQLServer.showException(ex); }
        if(!RV){
            Tools.errorDialog("Version Error", "Hay una nueva actualización disponible ["+newVersion+"] / "+version+". La aplicación se cerrará para que pueda ser instalada");
            System.exit(0);
        }
        return RV;
    }
    public static boolean init(String driverString, String connectionString, String version){
        try {
            SQLServer.version = version;
            Class.forName(driverString);
            db = DriverManager.getConnection(connectionString);
        } catch (ClassNotFoundException e) { 
            Tools.exceptionDialog("SQL Error", "No se encuentra el driver de la base de datos", e);
            return false;
        } catch (SQLException e) {
            SQLServer.showException(e);
            return false;
        }
        return true;
    }
    public static boolean close(){
            try {
                 db.close();
                 return true;
            } catch (SQLException e) {
                SQLServer.showException(e);
                return false;
            }
    }
    public ResultSetMetaData getMetaData(){
        try {
            return (rs != null ? rs.getMetaData() : null);
        } catch (SQLException e) {
            SQLServer.showException(e); return null;}
    }
    public SQLServer(String st) throws SQLException{
        if(checkVersion()){
            cs = db.prepareCall(st);
        }
    }
    public void query() throws SQLException{
        if(checkVersion()){
            rs = cs.executeQuery();
            result = FXCollections.observableArrayList();
        }
    }
    public void addItem(T item){
        result.add(item);
    }
    public ObservableList<T> getResult(){
        return result;
    }
    public boolean next() throws SQLException {
        return rs.next();
    }
    public static void showException(SQLException e){
        if(e.getClass().equals(SQLServerException.class)){
            switch(e.getErrorCode()){
                case 547:
                    if(e.getMessage().substring(4, 10).equals("UPDATE")){
                        Tools.exceptionDialog("SQL Error", "Uno o varios campos contienen valores inconsistentes.", e);
                    } else if(e.getMessage().substring(4, 10).equals("INSERT")){
                        Tools.exceptionDialog("SQL Error", "Uno o varios campos contienen valores inconsistentes.", e);
                    } else if(e.getMessage().substring(4, 10).equals("DELETE")){
                        Tools.exceptionDialog("SQL Error", "El registro no puede ser borrado por tener datos asociados.\nDe ser posible se marcará como obsoleto.", e);
                    }
                    break;
                case 8152:
                    Tools.exceptionDialog("SQL Error", "Uno de los campos superó el limite de caracteres.", e);
                    break;
                default:
                    Tools.exceptionDialog("SQL Error", "Error desconocido", e);
                    break;
            }
        }
    }
    public void update() throws SQLException {
        if(checkVersion()){
            cs.executeUpdate();
        }
    }
    public void end() throws SQLException{
            cs.close();
    }
    public void setBoolean(String name, Boolean value) throws SQLException{
            if(value == null){
                    cs.setNull(name, Types.BOOLEAN);
            } else {
                    cs.setBoolean(name, value);
            }
    }
    public void setInt(String name, Integer value) throws SQLException{
            if(value == null){
                    cs.setNull(name, Types.INTEGER);
            } else {
                    cs.setInt(name, value);
            }
    }
    public void setString(String name, String value) throws SQLException{
            if(value == null){
                    cs.setNull(name, Types.VARCHAR);
            } else {
                    cs.setString(name, value);
            }
    }
    public void setFloat(String name, Float value) throws SQLException{
            if(value == null){
                    cs.setNull(name, Types.FLOAT);
            } else {
                    cs.setFloat(name, value);
            }
    }
    public void setDouble(String name, Double value) throws SQLException{
            if(value == null){
                    cs.setNull(name, Types.DOUBLE);
            } else {
                    cs.setDouble(name, value);
            }
    }
    public void setDate(String name, Date value) throws SQLException{
            if(value == null){
                    cs.setNull(name, Types.DATE);
            } else {
                    cs.setDate(name, value);
            }
    }
    public boolean getBoolean(String name) throws SQLException{
        return rs.getBoolean(name);
    }
    public Integer getInt(String name) throws SQLException{
        Integer RV = rs.getInt(name);
        if(rs.wasNull()) RV = null;
        return RV;
    }
    public String getString(String name) throws SQLException{
        return rs.getString(name);
    }
    public Float getFloat(String name) throws SQLException{
        Float RV = rs.getFloat(name);
        if(rs.wasNull()) RV = null;
        return RV;
    }
    public Double getDouble(String name) throws SQLException{
        Double RV = rs.getDouble(name);
        if(rs.wasNull()) RV = null;
        return RV;
    }
    public Date getDate(String name) throws SQLException{
        Date RV = rs.getDate(name);
        if(rs.wasNull()) RV = null;
        return RV;
    }
    public String getDateString(String name) throws SQLException {
        return Tools.dateToString2(rs.getDate(name));
    }
    public byte[] getBytes(String name) throws SQLException {
        return rs.getBytes(name);
    }
    public InputStream getImageStream(String name) throws SQLException {
        byte[] image = rs.getBytes(name);
        if(image != null){
            return new ByteArrayInputStream(image);
        } else {
            return null;
        }
    }
    public static Boolean getBoolean(SQLServer sql, String name){
        try{
            sql.query();
            if(sql.next()){
                Boolean RV = sql.getBoolean(name);
                sql.end();
                return RV;
            } else {
                sql.end();
                return null;
            }
        }catch(SQLException e){
            SQLServer.showException(e);
            return null;
        }
    }
    public static Integer getInt(SQLServer sql, String name){
        try{
            sql.query();
            if(sql.next()){
                Integer RV = sql.getInt(name);
                sql.end();
                return RV;
            } else {
                sql.end();
                return null;
            }
        }catch(SQLException e){
            SQLServer.showException(e); return null;}
    }
    public static String getString(SQLServer sql, String name){
        try{
            sql.query();
            if(sql.next()){
                String RV = sql.getString(name);
                sql.end();
                return RV;
            } else {
                sql.end();
                return null;
            }
        }catch(SQLException e){
            SQLServer.showException(e); return null;}
    }
    public static Float getFloat(SQLServer sql, String name){
        try{
            sql.query();
            if(sql.next()){
                Float RV = sql.getFloat(name);
                sql.end();
                return RV;
            } else {
                sql.end();
                return null;
            }
        }catch(SQLException e){
            SQLServer.showException(e); return null;}
    }
    public static Date getDate(SQLServer sql, String name){
        try{
            sql.query();
            if(sql.next()){
                Date RV = sql.getDate(name);
                sql.end();
                return RV;
            } else {
                sql.end();
                return null;
            }
        }catch(SQLException e){
            SQLServer.showException(e); return null;}
    }
    public void regInt(String name) throws SQLException {
        cs.registerOutParameter(name, Types.INTEGER);
    }
    public Integer outInt(String name) throws SQLException {
        return cs.getInt(name);
    }
}
