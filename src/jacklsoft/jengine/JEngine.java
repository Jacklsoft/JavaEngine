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
import java.io.File;
import java.util.TreeSet;
import javafx.application.Application.Parameters;
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
    public void init(Stage stage, Parameters args){
        try {
            this.stage = stage;
            
            JsonReader reader = Json.createReader(getClass().getResourceAsStream("/jacklsoft/jengine/config.json"));
            JsonObject cfg = reader.readObject();
            rights = new TreeSet();
            for(JsonString i: cfg.getJsonArray("rights").getValuesAs(JsonString.class)){
                rights.add(i.getString());
            }
            reader.close();
            
            title = args.getNamed().get("title");
            rootPath = args.getNamed().get("resources");
            connection = 
                    "jdbc:sqlserver://"+args.getNamed().get("server")+":"+args.getNamed().get("port")+
                    ";databaseName="+args.getNamed().get("database")+
                    ";user="+args.getNamed().get("user")+
                    ";password="+args.getNamed().get("password")+";";
            
            new File(rootPath+"resources").mkdir();
            new File(rootPath+"resources\\img").mkdir();
            
            if(!SQLServer.init("com.microsoft.sqlserver.jdbc.SQLServerDriver", connection)){
                System.exit(0);
            }
            
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
