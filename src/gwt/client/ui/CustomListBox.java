package gwt.client.ui;

import com.google.gwt.user.client.ui.ListBox;

public class CustomListBox extends ListBox{
    
    public void setSelectedValue(String value){
        for(int i = 0; i < this.getItemCount(); i++){
            if(this.getValue(i).equals(value)){
                this.setSelectedIndex(i);
                break;
            }
        }        
    }
    
    public String getValue(){
        return this.getValue(this.getSelectedIndex());
    }
    
}
