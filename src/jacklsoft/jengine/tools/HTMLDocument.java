package jacklsoft.jengine.tools;

import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import org.apache.poi.util.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.awt.*;
import java.io.*;
import java.util.Scanner;

/**
 * Created by leonardo.mangano on 27/09/2015.
 */
public class HTMLDocument {
    public interface JSONFiller<T>{
        public JSONObject fromSQL(T sql);
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
    public void addData(String selector, String varName, JSONObject data){
        setHTML(selector, "var " + varName + " = " + data.toString() + ";");
    }
    public static <T> JSONArray SQLtoJSON(ObservableList<T> result, JSONFiller<T> filler){
        JSONArray array = new JSONArray();
        for(T i: result){
            array.put(filler.fromSQL(i));
        }

        return array;
    }
    public void run(File output){
        try {
            PrintWriter writer = new PrintWriter(output, "UTF-8");
            writer.write(document.html());
            writer.flush();
            writer.close();
            Desktop.getDesktop().browse(output.toURI());
        } catch (IOException e) {
            Tools.exceptionDialog("File not found", "No se encontró el archivo", e);
        }
    }
    public void run(){
        run(Tools.saveFile(new FileChooser.ExtensionFilter("HTML File", "*.html")));
    }
}
