package jacklsoft.jengine.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import jacklsoft.jengine.JEngine;

import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialogs;

public class Tools {
    public static Image getResourcesImage(String path, String ID, String extension){
        Image image = null;
        try {
            try (FileInputStream fis = new FileInputStream(new File(JEngine.rootPath+"resources\\img\\"+path+"\\"+ID+extension))) {
                image = new Image(fis);
            }
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {Tools.exceptionDialog("IO Error", "Error en la lectura del archivo", ex);}
        return image;
    }
    public static boolean storeResource(String path, String ID, String extension, File file){
        try{
            if(file == null){
                File dest = new File(JEngine.rootPath+"resources\\"+path+ID+extension);
                Files.delete(dest.toPath());
            } else {
                File dest = new File(JEngine.rootPath+"resources\\"+path+ID+extension);
                Files.copy(file.toPath(), dest.toPath());
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
    public static Date getToday(){
        return new Date(new java.util.Date().getTime());
    }
    public static Action alertDialog(Alert e){
        return exceptionDialog(e.toString(), e.getMessage(), e);
    }
    public static Action exceptionDialog(String title, String msj, Throwable e){
        return Dialogs.create()
                .owner(null)
                .title("Exception")
                .masthead(title)
                .message(msj)
                .showException(e);
    }
    public static Action warningDialog(String title, String msj){
        return Dialogs.create()
                .owner(null)
                .title("Message")
                .masthead(title)
                .message(msj)
                .showWarning();
    }
    public static void informationDialog(String title, String msj){
        Dialogs.create()
                .owner(null)
                .title("Message")
                .masthead(title)
                .message(msj)
                .showInformation();
    }
    public static Action errorDialog(String title, String msj){
        return Dialogs.create()
                .owner(null)
                .title("Error")
                .masthead(title)
                .message(msj)
                .showError();
    }
    public static Action confirmDialog(String title, String msj){
        return Dialogs.create()
                .owner(null)
                .title("Confirm")
                .masthead(title)
                .message(msj)
                .showConfirm();
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
