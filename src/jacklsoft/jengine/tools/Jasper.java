package jacklsoft.jengine.tools;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import jacklsoft.jengine.JEngine;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.data.ListOfArrayDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.view.JasperViewer;

public class Jasper {
	InputStream root;
	HashMap<String, Object> parameters;
	ListOfArrayDataSource loads;
	
	public Jasper(String root, HashMap<String, Object> parameters, List<Object[]> data, String... vars){
            this.root = getClass().getResourceAsStream(root);
            if(parameters == null){
                this.parameters = new HashMap<>();
            } else {
                this.parameters = parameters;
            }
            this.parameters.put("REPORT_ROOT", JEngine.launcherCFG.getString("resources"));
            this.loads = new ListOfArrayDataSource(data, vars);
	}
	public InputStream getRoot(){
		return root;
	}
	public HashMap<String, Object> getParameters(){
		return parameters;
	}
	public ListOfArrayDataSource getDataSource(){
		return loads;
	}
	public static void view(Jasper report) { 
		try {
			JasperPrint print = JasperFillManager.fillReport(report.getRoot(), report.getParameters(), report.getDataSource());
			JasperViewer.viewReport(print, false);
		} catch (JRException e) {Tools.exceptionDialog("Report Error", "Error en el reporte", e); }
	}
	
	public static void print(
				Jasper report,
				boolean withDialog) {
		try {
			JasperPrint print = JasperFillManager.fillReport(report.getRoot(), report.getParameters(), report.getDataSource());
			JasperPrintManager.printReport(print, withDialog);
		} catch (JRException e) {Tools.exceptionDialog("Report Error", "Error en el reporte", e); }
	}
	
	public static JasperPrint getPrint(Jasper report) {
		JasperPrint print = null;
		try {
			print = JasperFillManager.fillReport(report.getRoot(), report.getParameters(), report.getDataSource());
		} catch (JRException e) {Tools.exceptionDialog("Report Error", "Error en el reporte", e); }
		
		return print;
	}

	public static void mergeInPDF(String name, ArrayList<JasperPrint> prints) {
		try {
			File tempFile = File.createTempFile(name, ".pdf");
			JRPdfExporter pdf = new JRPdfExporter();
			pdf.setParameter(JRPdfExporterParameter.JASPER_PRINT_LIST, prints);
			pdf.setParameter(JRPdfExporterParameter.OUTPUT_FILE, tempFile);
			pdf.exportReport();
			runDocument(tempFile.getAbsolutePath());
			tempFile.deleteOnExit();
		} catch (JRException | IOException e){Tools.exceptionDialog("Report Error", "Error en el reporte", e); }
	}
	
	public static void toPDF(String name, Jasper report) {
		JasperPrint print = null;
		try {
			File tempFile = File.createTempFile(name, ".pdf");
			OutputStream os = new BufferedOutputStream(new FileOutputStream(tempFile));
			print = JasperFillManager.fillReport(report.getRoot(), report.getParameters(), report.getDataSource());
			JasperExportManager.exportReportToPdfStream(print, os);
			os.close();
			runDocument(tempFile.getAbsolutePath());
			tempFile.deleteOnExit();
		} catch(JRException | IOException e){Tools.exceptionDialog("Report Error", "Error en el reporte", e); }
	}
	
	public static void toExcel(String name, Jasper report) {
		JasperPrint print = null;
		
		try {
			File tempFile = File.createTempFile(name, ".xls");
			OutputStream os = new FileOutputStream(tempFile);
			print = JasperFillManager.fillReport(report.getRoot(), report.getParameters(), report.getDataSource());
			JRXlsExporter xls = new JRXlsExporter();
			xls.setParameter(JRExporterParameter.JASPER_PRINT, print); 
			xls.setParameter(JRExporterParameter.OUTPUT_STREAM, os); 
			xls.exportReport();
			os.close();
			runDocument(tempFile.getAbsolutePath());
			tempFile.deleteOnExit();
		} catch(JRException | IOException e){Tools.exceptionDialog("Report Error", "Error en el reporte", e); }
	}
	
	public static void mergeInExcel(String name, ArrayList<JasperPrint> prints) {
		try {
			File tempFile = File.createTempFile(name, ".xls");
			JRXlsExporter xls = new JRXlsExporter();
			xls.setParameter(JRPdfExporterParameter.JASPER_PRINT_LIST, prints);
			xls.setParameter(JRPdfExporterParameter.OUTPUT_FILE, tempFile);
			xls.exportReport();
			runDocument(tempFile.getAbsolutePath());
			tempFile.deleteOnExit();
		} catch (JRException | IOException e){Tools.exceptionDialog("Report Error", "Error en el reporte", e); }
	}
	
	public static void runDocument(String root) {
		try {
			Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + root);
		} catch (IOException e) {Tools.exceptionDialog("Report Error", "Error en el reporte", e); }
	}
}
