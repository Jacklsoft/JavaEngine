package jacklsoft.jengine;

import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
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

            JsonFactory JF = new JsonFactory();
            JsonParser rightsReader = JF.createParser(getClass().getResourceAsStream("/jacklsoft/jengine/config.json"));
            rights = new TreeSet();
            rightsReader.nextToken();
            while(rightsReader.nextToken() != JsonToken.END_OBJECT){
                if(rightsReader.getCurrentName() == "rights"){
                    rightsReader.nextToken();
                    while(rightsReader.nextToken() != JsonToken.END_ARRAY){
                        rights.add(rightsReader.getText());
                    }
                }
            }
            rightsReader.close();

            JsonParser configReader = JF.createParser(new FileReader(new File("rsc/launcher.json")));
            configReader.nextToken();
            while(configReader.nextToken() != JsonToken.END_OBJECT){
                switch(configReader.getCurrentName()){
                    case "title":
                        title = configReader.nextTextValue();
                        break;
                    case "resources":
                        rootPath = configReader.nextTextValue();
                        break;
                    case "connection":
                        connection = configReader.nextTextValue();
                        break;
                }
            }
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
