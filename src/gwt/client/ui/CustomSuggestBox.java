package gwt.client.ui;

import gwt.shared.InvalidValueException;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

public class CustomSuggestBox extends Composite{

    public interface SuggestBoxCallback {
        void onInvalidInput(String input);
        void onValidInput(String input);
        void onInCompleteInput(String value);
        void onCompleteInput(String key, String value);
    }
    
    private FlowPanel panel;
    private SuggestBox suggestBox;
    private Button showAllBtn;
    
    private MultiWordSuggestOracle suggest;
    
    private ArrayList<String> valueList;
    private ArrayList<String> keyList;
    
    private SuggestBoxCallback callback;
    
    public CustomSuggestBox(){
        keyList = new ArrayList<String>();
        valueList = new ArrayList<String>();
        
        suggest = new MultiWordSuggestOracle();
        suggestBox = new SuggestBox(suggest);
        suggestBox.getTextBox().addKeyUpHandler(suggestBoxKeyUpHandler);
        suggestBox.getTextBox().addValueChangeHandler(suggestBoxValueChangeHandler);
        suggestBox.addSelectionHandler(suggestBoxSelectionHandler);
        
        showAllBtn = new Button("show");
        showAllBtn.addClickHandler(showAllBtnClickHandler);
        
        panel = new FlowPanel();
        panel.add(suggestBox);
        panel.add(showAllBtn);
        
        initWidget(panel);
    }
    
    public void add(String key, String value){
        keyList.add(key);
        valueList.add(value);
        
        suggest.add(value);
        suggest.setDefaultSuggestionsFromText(valueList);
    }
    
    public String getValue(){
        return suggestBox.getValue();
    }
    
    public String getKey() {
        if(suggestBox.getValue().isEmpty()){
            return null;
        }
        
        int i = valueList.indexOf(suggestBox.getValue());
        if(i < 0){
            throw new InvalidValueException();
        }
        
        return keyList.get(i);
    }
    
    public void clear(){
        keyList.clear();
        valueList.clear();
        
        suggestBox.setValue("");
        suggest.clear();
        
        callback = null;
    }
    
    public void setValue(String value){
        suggestBox.setValue(value, true);
    }
    
    public void setKey(String key){
        if(key != null){
            int i = keyList.indexOf(key);
            if(i >= 0){
                suggestBox.setValue(valueList.get(i), true);
            }
        }
    }
    
    public void setEnabled(boolean enabled){
        suggestBox.getTextBox().setEnabled(enabled);
    }
    
    public void addTextBoxStyleName(String style){
        suggestBox.addStyleName(style);
    }
    
    public void addSuggestBoxCallback(SuggestBoxCallback callback){
        this.callback = callback;
    }
    
    private ClickHandler showAllBtnClickHandler = new ClickHandler(){
        @Override
        public void onClick(ClickEvent event) {
            suggestBox.showSuggestionList();
        }
    };
    
    private KeyUpHandler suggestBoxKeyUpHandler = new KeyUpHandler(){
        @Override
        public void onKeyUp(KeyUpEvent event) {
            if(valueList.isEmpty()){
                return;
            }
            
            boolean found = false;
            for(String s : valueList){
                if(s.startsWith(getValue())){
                    found = true;
                    break;
                }
            }
            if(callback != null){
                if(found){
                    callback.onValidInput(getValue());
                }else{
                    callback.onInvalidInput(getValue());
                }
            }
        }
    };
    
    private ValueChangeHandler<String> suggestBoxValueChangeHandler = new ValueChangeHandler<String>(){
        @Override
        public void onValueChange(ValueChangeEvent<String> event) {
            callbackCompleteInput();
        }
    };
    
    private SelectionHandler<Suggestion> suggestBoxSelectionHandler = new SelectionHandler<Suggestion>(){
        @Override
        public void onSelection(SelectionEvent<Suggestion> event) {
            callbackCompleteInput();
        }
    };
    
    private void callbackCompleteInput(){
        if(valueList.isEmpty()){
            return;
        }
        
        boolean complete = false;
        for(String s : valueList){
            if(s.equals(getValue())){
                complete = true;
                break;
            }
        }
        if(callback != null){
            if(complete){
                callback.onCompleteInput(getKey(), getValue());
            }else{
                callback.onInCompleteInput(getValue());
            }
        }
    }
}
