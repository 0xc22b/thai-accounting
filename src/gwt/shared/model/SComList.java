package gwt.shared.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class SComList implements Serializable{

	private static final long serialVersionUID = 1L;

	private ArrayList<SCom> sComList = new ArrayList<SCom>();
	
	public SComList(){
		
	}
	
	public List<SCom> getSComList(){
	    return Collections.unmodifiableList(sComList);
	}
	
	public SCom getSCom(String comKeyString){
	    for(SCom sCom : sComList){
            if(sCom.getKeyString().equals(comKeyString)){
                return sCom;
            }
        }
	    return null;
	}
	
	public void addSCom(SCom sCom){
        sComList.add(sCom);
    }
	
	public SCom editSCom(SCom newSCom){
	    for(SCom sCom : sComList){
            if(sCom.getKeyString().equals(newSCom.getKeyString())){
                sCom.setName(newSCom.getName());
                return sCom;
            }
        }
	    return null;
	}
	
	public void removeSCom(String keyString){
	    Iterator<SCom> it = sComList.iterator();
	    while (it.hasNext()) {
            SCom sCom = it.next();
            if (sCom.getKeyString().equals(keyString)) {
                it.remove();
                break;
            }
        }
	}
}
