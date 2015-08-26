package jacklsoft.jengine.tools;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import javafx.stage.FileChooser.ExtensionFilter;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.PDFRenderOption;
import org.eclipse.core.internal.registry.RegistryProviderFactory;

public class BIRT {
    IReportEngine engine;
    EngineConfig config;
    String url;
    String user;
    String password;
    
    public BIRT(String url, String user, String password){
        try {
            config = new EngineConfig();
            this.url = url;
            this.user = user;
            this.password = password;
            Platform.startup(config);
            IReportEngineFactory factory = (IReportEngineFactory) Platform.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
            engine = factory.createReportEngine(config);
            engine.changeLogLevel(Level.WARNING);
        } catch (BirtException ex) {Tools.exceptionDialog("BIRT Error", "Error al iniciar BIRT", ex);}
    }
    public void destroy(){
        engine.destroy();
        Platform.shutdown();
        RegistryProviderFactory.releaseDefault();
    }
    public void createReport(String root, HashMap<String, Object> parameters){
        try {
            IReportRunnable report = engine.openReportDesign(getClass().getResourceAsStream(root));
            IRunAndRenderTask task = engine.createRunAndRenderTask(report);
            task.getAppContext().put(EngineConstants.APPCONTEXT_CLASSLOADER_KEY, this.getClass().getClassLoader());
            parameters.put("url", url);
            parameters.put("user", user);
            parameters.put("password", password);
            task.setParameterValues(parameters);
            task.validateParameters();
            PDFRenderOption pdf = new PDFRenderOption();
            String output = Tools.saveFile(new ExtensionFilter("PDF File", "*.pdf")).getAbsolutePath();
            pdf.setOutputFileName(output);
            pdf.setOutputFormat("pdf");
            pdf.setEmbededFont(true);
            task.setRenderOption(pdf);
            task.run();
            task.close();
            runReport(output);
        } catch (EngineException ex) {Tools.exceptionDialog("Report error", "Error al abrir el reporte", ex);}
    }
    public static void runReport(String root) {
        try {
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + root);
        } catch (IOException e) {Tools.exceptionDialog("Report Error", "Error al ejecutar el reporte", e); }
    }
}
