package jacklsoft.jengine.tools;

public class Alert extends Exception{
    @SuppressWarnings("compatibility:444876904121810702")
    private static final long serialVersionUID = 1L;
    String title;
    String message;
    
    public Alert(String title, String message){
        this.title = title;
        this.message = message;
    }
    @Override
    public String getMessage(){
        return message;
    }
    @Override
    public String toString(){
        return title;
    }
}
