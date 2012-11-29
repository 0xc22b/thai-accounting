package gwt.shared;

import java.io.Serializable;

public class NotLoggedInException extends Exception implements Serializable {

	private static final long serialVersionUID = 1L;

	public NotLoggedInException(){
	    super("NotLoggedInException");
	}
	
	public NotLoggedInException(String message){
        super(message);
    }
	
	public NotLoggedInException(String message, Throwable cause){
        super(message, cause);
    }
	
	public NotLoggedInException(Throwable cause){
        super(cause);
    }
	
}
