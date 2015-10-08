package jacklsoft.jengine;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
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

            JsonReader rightsReader = new JsonReader(new InputStreamReader(getClass().getResourceAsStream("/jacklsoft/jengine/config.json")));
            rights = new TreeSet();
            rightsReader.beginObject();
            while(rightsReader.hasNext()){
                if(rightsReader.nextName() == "rights"){
                    rightsReader.beginArray();
                    while(rightsReader.hasNext()){
                        rights.add(rightsReader.nextString());
                    }
                    rightsReader.endArray();
                }
            }
            rightsReader.endObject();
            rightsReader.close();

            JsonReader configReader = new JsonReader(new FileReader(new File("rsc/launcher.json")));
            configReader.beginObject();
            while(configReader.hasNext()){
                switch(configReader.nextName()){
                    case "title":
                        title = configReader.nextString();
                        break;
                    case "resources":
                        rootPath = configReader.nextString();
                        break;
                    case "connection":
                        connection = configReader.nextString();
                        break;
                }
            }
            configReader.endObject();
            configReader.close();
            
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
