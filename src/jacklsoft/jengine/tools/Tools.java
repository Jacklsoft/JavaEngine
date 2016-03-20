package jacklsoft.jengine.tools;

import java.io.*;
import java.nio.file.Files;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Optional;
import java.util.Scanner;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import jacklsoft.jengine.JEngine;
import java.nio.file.StandardCopyOption;
import javafx.scene.control.Alert;

public class Tools {
    public static Image getResourcesImage(String path, String ID, String extension){
        Image image = null;
        try {
            try (FileInputStream fis = new FileInputStream(new File(JEngine.launcherCFG.getString("resources")+"resources\\img\\"+path+"\\"+ID+extension))) {
                image = new Image(fis);
            }
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {Tools.exceptionDialog("IO Error", "Error en la lectura del archivo", ex);}
        return image;
    }
    public static boolean storeResource(String path, String ID, String extension, File file){
        try{
            if(file == null){
                File dest = new File(JEngine.launcherCFG.getString("resources")+"resources\\"+path+ID+extension);
                if(dest.exists()){
                    Files.delete(dest.toPath());
                }
            } else {
                File dest = new File(JEngine.launcherCFG.getString("resources")+"resources\\"+path+ID+extension);
                Files.copy(file.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            return true;
        } catch (IOException ex) {Tools.exceptionDialog("IO Error", "Error en la escritura/lectura del archivo", ex);}
        return false;
    }
    public static File openFile(ExtensionFilter... extension){
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(extension);
        return fc.showOpenDialog(jacklsoft.jengine.JEngine.ST.scene.getWindow());
    }
    public static File saveFile(ExtensionFilter... extension){
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(extension);
        return fc.showSaveDialog(jacklsoft.jengine.JEngine.ST.scene.getWindow());
    }
    public static Date getToday(){
        return new Date(new java.util.Date().getTime());
    }
    public static void alertDialog(AlertException e){
        exceptionDialog(e.toString(), e.getMessage(), e);
    }
    public static void exceptionDialog(String title, String msj, Throwable e){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Exception Dialog");
        alert.setHeaderText(title);
        alert.setContentText(msj);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);
        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();
    }
    public static void warningDialog(String title, String msj){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Alert Dialog");
        alert.setHeaderText(title);
        alert.setContentText(msj);

        alert.showAndWait();
    }
    public static void informationDialog(String title, String msj){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(title);
        alert.setContentText(msj);

        alert.showAndWait();
    }
    public static void errorDialog(String title, String msj){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText(title);
        alert.setContentText(msj);

        alert.showAndWait();
    }
    public static boolean confirmDialog(String title, String msj){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(title);
        alert.setContentText(msj);

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){
            return true;
        } else {
            return false;
        }
    }
    public static Calendar getCalendar(Date date){
        if(date != null){
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal;
        }
        return null;
    }
    public static Date weekToDate(String week){
        if(week != null){
            Calendar calendar = Calendar.getInstance();
            calendar.clear();
            if(week.length() == 7){
                calendar.set(Calendar.WEEK_OF_YEAR, Integer.valueOf(week.substring(5, 7)));
                calendar.set(Calendar.YEAR, Integer.valueOf(week.substring(0, 4)));
                return new Date(calendar.getTimeInMillis());
            }
        }
        return null;
    }
    public static Date stringToDate(String fecha){
            if(fecha == null) return null;
            else{
                    DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
                    df.setLenient(false);
                    Date d = null;
                    try {
                            d = new Date(df.parse(fecha).getTime());
                    } catch (ParseException e) {return null;}
                    return d;
            }
    }
    public static String dateToString(Date fecha){
            String RV = null;
            if (fecha == null) {
                    return "";
            } else {
                    DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
                    df.setLenient(false);
                    RV = df.format(fecha);
                    if (RV == null) {
                            return "";
                    } else {
                            return RV;
                    }
            }
    }
    public static String dateToString2(Date fecha){
            String RV = null;
            if (fecha == null) {
                    return "";
            } else {
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    df.setLenient(false);
                    RV = df.format(fecha);
                    if (RV == null) {
                            return "";
                    } else {
                            return RV;
                    }
            }
    }
    public static Scanner getClipboard(){
        Clipboard clipboard = Clipboard.getSystemClipboard();
        String cadena = null;
        Scanner scanner = null;
        cadena = (String)(clipboard.getContent(DataFormat.PLAIN_TEXT));
        Integer columns = 1;
        Integer len = cadena.length();
        for(int i = 0; i < len && cadena.charAt(i) != '\n'; i++){
                if(cadena.charAt(i) == '\t'){
                        columns++;
                }
        }
        scanner = new Scanner(columns+"\t" + cadena);
        scanner.useDelimiter("\\t|\\n");
        
        return (cadena == null ? null : scanner);
    }
}
