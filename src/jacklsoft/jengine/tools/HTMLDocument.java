package jacklsoft.jengine.tools;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.awt.*;
import java.io.*;

/**
 * Created by leonardo.mangano on 27/09/2015.
 */
public class HTMLDocument {
    public interface JSONFiller<T>{
        public ObjectNode fromSQL(T sql);
    }

    Document document;

    public HTMLDocument(String root){
        try {
            document = Jsoup.parse(new File(root), "UTF-8", "");
        } catch (IOException e) {
            Tools.exceptionDialog("HTML Error", "Error al cargar el documento HTML", e);
        }
    }
    public void setHTML(String select, String html){
        document.select(select).html(html);
    }
    public void appendHTML(String select, String html){document.select(select).append(html);}
    public static String loadHTML(String file){
        try {
            return Jsoup.parse(new File(file), "UTF-8", "").toString();
        } catch (IOException e) {
            Tools.exceptionDialog("HTML Error", "Error al cargar el documento HTML", e);
            return null;
        }
    }
    public void addJS(String path){
        appendHTML("head", "<script src='" + new File(path).getAbsolutePath() + "'></script>");
    }
    public void addCSS(String path){
        appendHTML("head", "<link href='" + new File(path).getAbsolutePath() + "' rel='stylesheet'/>");
    }
    public void addData(String selector, String varName, ObjectNode data){
        setHTML(selector, "var " + varName + " = " + data.toString() + ";");
    }
    public static <T> ArrayNode SQLtoJSON(ObservableList<T> result, JSONFiller<T> filler){
        JsonNodeFactory JNF = JsonNodeFactory.instance;
        ArrayNode array = JNF.arrayNode();
        for(T i: result){
            array.add(filler.fromSQL(i));
        }

        return array;
    }
    public void run(){
        try {
            File output = Tools.saveFile(new FileChooser.ExtensionFilter("HTML File", "*.html"));
            PrintWriter writer = new PrintWriter(output, "UTF-8");
            writer.write(document.html());
            writer.flush();
            writer.close();
            Desktop.getDesktop().browse(output.toURI());
        } catch (IOException e) {
            Tools.exceptionDialog("File not found", "No se encontró el archivo", e);
        }
    }
}
