package gwt.shared;

import java.io.Serializable;

public class DataNotFoundException extends Exception implements Serializable{

	private static final long serialVersionUID = 1L;

	public DataNotFoundException(){
	    super();
	}
	
	public DataNotFoundException(String message){
        super(message);
    }
	
	public DataNotFoundException(String message, Throwable cause){
        super(message, cause);
    }
	
	public DataNotFoundException(Throwable cause){
        super(cause);
    }
	
}
