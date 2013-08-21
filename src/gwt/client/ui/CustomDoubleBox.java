package gwt.client.ui;

import gwt.shared.InvalidValueException;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.text.client.DoubleParser;
import com.google.gwt.user.client.ui.ValueBox;

public class CustomDoubleBox extends ValueBox<Double> {
    
    public interface DoubleBoxCallback {
        void onInvalidInput(String input);
        void onValidInput(String input);
    }

    private boolean hasRange = false;
    private double begin;
    private double end;
    private DoubleBoxCallback callback;
    
    public CustomDoubleBox(){
        super(Document.get().createTextInputElement(),
                CustomDoubleRenderer.instance(), DoubleParser.instance());

        this.addKeyUpHandler(doubleBoxKeyUpHandler);
    }
    
    public Double getCustomValue(){
        String t = getText();
        if (t.isEmpty()){
            return new Double(0);
        }

        Double n = getValue();
        if(n == null){
            throw new NumberFormatException();
        }
        
        if(hasRange && (n < begin || n > end)){
            throw new InvalidValueException();
        }
        
        // Allow 2 decimals max!
        if (t.indexOf(".") != -1) {
            if (t.substring(t.indexOf(".") + 1).length() > 2) {
                throw new InvalidValueException();
            }
        }
        
        return n;
    }
    
    public void clear(){
        this.setText("");
    }
    
    public void setCustomValue(Double value){
        this.setValue(value);
    }
    
    public void setRange(double begin, double end){
        if(end < begin){
            throw new IllegalArgumentException();
        }
        
        this.hasRange = true;
        this.begin = begin;
        this.end = end;
    }
    
    public void addDoubleBoxCallback(DoubleBoxCallback callback){
        this.callback = callback;
    }
    
    private KeyUpHandler doubleBoxKeyUpHandler = new KeyUpHandler(){
        @Override
        public void onKeyUp(KeyUpEvent event) {
            
            if(callback != null){
                String s = getText();
                if(s.isEmpty()){
                    callback.onValidInput(s);
                }else{
                    try{
                        getCustomValue();
                        callback.onValidInput(s);
                    }catch(NumberFormatException e){
                        callback.onInvalidInput(getText());
                    }catch(InvalidValueException e){
                        callback.onInvalidInput(getText());
                    }
                }
            }
        }
    };
}
