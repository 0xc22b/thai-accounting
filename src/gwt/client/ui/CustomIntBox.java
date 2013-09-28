package gwt.client.ui;

import gwt.shared.InvalidValueException;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.TextBox;

public class CustomIntBox extends TextBox {
    
    public interface CustomIntBoxCallback {
        void onInvalidInput(String input);
        void onValidInput(String input);
    }

    private boolean hasRange = false;
    private int begin;
    private int end;
    private CustomIntBoxCallback callback;
    
    public CustomIntBox(){
        this.addKeyUpHandler(intBoxKeyUpHandler);
    }
    
    public int getCustomValue(){
        if(getValue().isEmpty()){
            return 0;
        }
        
        int n = Integer.parseInt(getValue()); 
        if(hasRange && (n < begin || n > end)){
            throw new InvalidValueException();
        }
        return n;
    }
    
    public void clear(){
        this.setText("");
    }
    
    public void setCustomValue(int value){
        this.setText(value + "");
    }
    
    public void setRange(int begin, int end){
        if(end < begin){
            throw new IllegalArgumentException();
        }
        
        this.hasRange = true;
        this.begin = begin;
        this.end = end;
    }
    
    public void clearRange() {
        this.hasRange = false;
        this.begin = 0;
        this.end = 0;
    }
    
    public void addCustomIntBoxCallback(CustomIntBoxCallback callback){
        this.callback = callback;
    }
    
    private KeyUpHandler intBoxKeyUpHandler = new KeyUpHandler(){
        @Override
        public void onKeyUp(KeyUpEvent event) {
            if(callback != null){
                String s = getValue();
                if(s.isEmpty()){
                    callback.onValidInput(s);
                }else{
                    try{
                        getCustomValue();
                        callback.onValidInput(s);
                    }catch(NumberFormatException e){
                        callback.onInvalidInput(s);
                    }catch(InvalidValueException e){
                        callback.onInvalidInput(s);
                    }
                }
            }
        }
    };
}
