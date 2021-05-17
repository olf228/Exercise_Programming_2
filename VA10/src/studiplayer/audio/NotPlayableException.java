package studiplayer.audio;

public class NotPlayableException extends Exception {
    // Attributes
    private String pathname = "";

    // Constructors
    public NotPlayableException(String pathname, String msg){
        super(msg);
        this.pathname = pathname;
    }
    public NotPlayableException(String pathname, Throwable t){
        super(t);
        this.pathname = pathname;
    }
    public NotPlayableException(String pathname, String msg, Throwable t){
        super(msg, t);
        this.pathname = pathname;
    }

    // Methods
    @Override
    public String toString(){
        return this.pathname + ":" + super.toString();
    }
}
