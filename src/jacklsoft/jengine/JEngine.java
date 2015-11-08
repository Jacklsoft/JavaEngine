package jacklsoft.jengine;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import jacklsoft.jengine.db.SQLServer;
import jacklsoft.jengine.units.Main;
import java.io.File;
import java.util.TreeSet;
import javafx.application.Application.Parameters;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;

/**
 *
 * @author leonardo.mangano
 */
public class JEngine{
    public static JEngine ST = new JEngine();
    public static JsonObject launcherCFG;
    public Stage stage;
    public Scene scene;
    public Parent root;
    public Main main;
    public TreeSet<String> rights;
    
    private JEngine(){};
    public void init(Stage stage, Parameters args){
        try {
            this.stage = stage;

            JsonReader configReader = Json.createReader(new FileInputStream("rsc/launcher.json"));
            JsonReader rightsReader = Json.createReader(getClass().getResourceAsStream("/jacklsoft/jengine/config.json"));
            JsonObject rightsObject = rightsReader.readObject();
            launcherCFG = configReader.readObject();

            rights = new TreeSet();
            for(JsonString i: rightsObject.getJsonArray("rights").getValuesAs(JsonString.class)){
                rights.add(i.getString());
            }
            rightsReader.close();

            String title = launcherCFG.getString("title");
            String rootPath = launcherCFG.getString("resources");
            String connection = launcherCFG.getString("connection");
            
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
