package gwt.client.view.desktop;

import gwt.client.TCF;
import gwt.client.TConstants;
import gwt.client.def.FisDef;
import gwt.client.ui.CustomDoubleBox;
import gwt.client.ui.CustomSuggestBox;
import gwt.client.view.BeginView;
import gwt.shared.InvalidValueException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class BeginViewImpl<T> extends Composite implements BeginView<T> {

    @SuppressWarnings("rawtypes")
    @UiTemplate("BeginViewImpl.ui.xml")
    interface MyUiBinder extends UiBinder<Widget, BeginViewImpl> {
    }
    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    interface Style extends CssResource {
        String noSB();
    }
    @UiField
    Style style;
    @UiField
    Label noLb;
    @UiField
    CustomSuggestBox noSB;
    @UiField
    Label errNoLb;
    @UiField
    Label beginLb;
    @UiField
    Label debitLb;
    @UiField
    CustomDoubleBox debitDB;
    @UiField
    Label creditLb;
    @UiField
    CustomDoubleBox creditDB;
    @UiField
    Label errBeginLb;
    
    private static final TConstants constants = TCF.get();
    
    private Presenter presenter;
    private FisDef<T> fisDef;
    private String keyString;
    
    public BeginViewImpl(FisDef<T> fisDef) {
        this.fisDef = fisDef;
        initWidget(uiBinder.createAndBindUi(this));
        
        noLb.setText(constants.accNo());
        beginLb.setText(constants.beginning());
        debitLb.setText(constants.debit());
        creditLb.setText(constants.or() + " " + constants.credit());
        
        debitDB.setRange(0, 999999999999.99);
        creditDB.setRange(0, 999999999999.99);
        
        noSB.addTextBoxStyleName(style.noSB());
    }

    @Override
    public Widget asWidget() {
        return this;
    }
    
    @Override
    public void init(Presenter presenter) {
        this.presenter = presenter;
        
        keyString = null;
        noSB.clear();
        errNoLb.setText("");
        debitDB.clear();
        creditDB.clear();
        errBeginLb.setText("");
    }

    @Override
    public void setBegin(T t, String keyString, boolean editable) {
        
        if(keyString == null){
            for(int i=0; i<fisDef.getACListSize(t); i++){
                if(fisDef.getACIsEntry(t, i)){
                    noSB.add(fisDef.getACKeyString(t, i), fisDef.getACNo(t, i) + " - " + fisDef.getACName(t, i));
                }
            }
            noSB.setEnabled(true);
        }else{    
            this.keyString = keyString;
            
            noSB.add(keyString, fisDef.getACNo(t, keyString) + " - " + fisDef.getACName(t, keyString));
            noSB.setValue(fisDef.getACNo(t, keyString) + " - " + fisDef.getACName(t, keyString));
            noSB.setEnabled(false);
            
            if(fisDef.getACBeginning(t, keyString) >= 0){
                debitDB.setCustomValue(fisDef.getACBeginning(t, keyString));
            }else{
                creditDB.setCustomValue(Math.abs(fisDef.getACBeginning(t, keyString)));
            }
        }
        setInputsEnabled(editable);
    }

    @Override
    public void setBeginBtnClicked() {
        if(!validateInputs()){
            return;
        }
        
        String ks = keyString != null ? keyString : noSB.getKey();
        double debit = debitDB.getCustomValue();
        double credit = creditDB.getCustomValue();
        double beginning = debit != 0 ? debit : credit * -1;
        presenter.setBegin(ks, beginning);
    }
    
    private boolean validateInputs(){
        boolean isValid = true;
        
        try{
            String ks = noSB.getKey();
            if(ks == null){
                errNoLb.setText(constants.invalid());
                isValid = false;
            }else{
                errNoLb.setText("");
            }
        }catch(InvalidValueException e){
            errNoLb.setText(constants.invalid());
            isValid = false;
        }
        
        try{
            double debit = debitDB.getCustomValue();
            double credit = creditDB.getCustomValue();
            
            if(debit != 0 && credit != 0){
                errBeginLb.setText(constants.invalid());
                isValid = false;
            }else if(debit == 0 && credit == 0){
                errBeginLb.setText(constants.invalid());
                isValid = false;
            }else{
                errBeginLb.setText("");
            }
        }catch(NumberFormatException e){
            errBeginLb.setText(constants.invalid());
            isValid = false;
        }catch(InvalidValueException e){
            errBeginLb.setText(constants.invalid());
            isValid = false;
        }
        
        return isValid;
    }

    private void setInputsEnabled(boolean enabled){
        debitDB.setEnabled(enabled);
        creditDB.setEnabled(enabled);
    }
    
}
