package jacklsoft.jengine;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import jacklsoft.jengine.db.SQLServer;
import jacklsoft.jengine.units.Main;
import java.util.HashSet;
import java.util.TreeSet;
import javax.json.JsonString;

/**
 *
 * @author leonardo.mangano
 */
public class JEngine{
    public static JEngine ST = new JEngine();
    public static String rootPath;
    public Stage stage;
    public Scene scene;
    public Parent root;
    public Main main;
    public String title;
    public String connection;
    public TreeSet<String> rights;
    
    private JEngine(){};
    public void init(Stage stage){
        try {
            this.stage = stage;
            JsonReader reader = Json.createReader(getClass().getResourceAsStream("/jacklsoft/jengine/config.json"));
            JsonObject cfg = reader.readObject();
            cfg = cfg.getJsonObject("run");
            rootPath = cfg.getString("path");
            title = cfg.getString("title");
            connection = cfg.getString("connection");
            if(!SQLServer.init("com.microsoft.sqlserver.jdbc.SQLServerDriver", connection)){
                System.exit(0);
            }
            rights = new TreeSet();
            for(JsonString i: cfg.getJsonArray("rights").getValuesAs(JsonString.class)){
                rights.add(i.getString());
            }
            reader.close();
            root = FXMLLoader.load(getClass().getResource("/jacklsoft/jengine/units/Main.fxml"));
            stage.setTitle(title);
            scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/jacklsoft/jengine/units/Styles.css").toExternalForm());
            stage.setOnCloseRequest((e) -> this.close(e));
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(JEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void close(WindowEvent e){
        SQLServer.close();
    }
    public TreeSet<String> getRights(){
        return rights;
    }
    public void refreshRights(){
        main.refreshTree();
    }
}
