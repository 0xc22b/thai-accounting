package gwt.client.view.desktop;

import gwt.client.TCF;
import gwt.client.TConstants;
import gwt.client.def.FisDef;
import gwt.client.ui.CustomIntBox;
import gwt.client.view.FisView;
import gwt.shared.InvalidValueException;
import gwt.shared.Utils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class FisViewImpl<T> extends Composite implements FisView<T> {

    @SuppressWarnings("rawtypes")
    @UiTemplate("FisViewImpl.ui.xml")
    interface MyUiBinder extends UiBinder<Widget, FisViewImpl> {
    }
    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    interface Style extends CssResource {
        
    }
    @UiField
    Style style;
    @UiField
    Label beginLb;
    @UiField
    Label beginMonthLb;
    @UiField
    CustomIntBox beginMonthIB;
    @UiField
    Label errBeginMonthLb;
    @UiField
    Label beginYearLb;
    @UiField
    CustomIntBox beginYearIB;
    @UiField
    Label errBeginYearLb;
    @UiField
    Label endLb;
    @UiField
    Label endMonthLb;
    @UiField
    CustomIntBox endMonthIB;
    @UiField
    Label errEndMonthLb;
    @UiField
    Label endYearLb;
    @UiField
    CustomIntBox endYearIB;
    @UiField
    Label errEndYearLb;
    
    private static final TConstants constants = TCF.get();
    
    private Presenter presenter;
    private FisDef<T> fisDef;
    private String keyString;
    
    public FisViewImpl(FisDef<T> fisDef) {
        this.fisDef = fisDef;
        initWidget(uiBinder.createAndBindUi(this));
        
        beginLb.setText(constants.begin());
        beginMonthLb.setText(constants.month());
        beginYearLb.setText(constants.year());
        endLb.setText(constants.end());
        endMonthLb.setText(constants.month());
        endYearLb.setText(constants.year());
        
        beginMonthIB.setRange(1, 12);
        beginYearIB.setRange(1980, 2600);
        endMonthIB.setRange(1, 12);
        endYearIB.setRange(1980, 2600);
    }

    @Override
    public Widget asWidget() {
        return this;
    }
    
    @Override
    public void init(Presenter presenter) {
        this.presenter = presenter;
        
        keyString = null;
        beginMonthIB.clear();
        errBeginMonthLb.setText("");
        beginYearIB.clear();
        errBeginYearLb.setText("");
        endMonthIB.clear();
        errEndMonthLb.setText("");
        endYearIB.clear();
        errEndYearLb.setText("");
    }

    @Override
    public void setFis(T t, boolean editable) {
        if(t!=null){
            keyString = fisDef.getFKeyString(t);
            beginMonthIB.setCustomValue(fisDef.getFBeginMonth(t));
            beginYearIB.setCustomValue(fisDef.getFBeginYear(t));
            endMonthIB.setCustomValue(fisDef.getFEndMonth(t));
            endYearIB.setCustomValue(fisDef.getFEndYear(t));
        }
        setInputsEnabled(editable);
    }

    @Override
    public void addFisBtnClicked(int setupType) {
        if(!validateInputs()){
            return;
        }
        
        presenter.addFis(setupType, beginMonthIB.getCustomValue(),
                beginYearIB.getCustomValue(), endMonthIB.getCustomValue(),
                endYearIB.getCustomValue());
    }

    @Override
    public void editFisBtnClicked() {
        if(!validateInputs()){
            return;
        }
        
        presenter.editFis(keyString, beginMonthIB.getCustomValue(), beginYearIB.getCustomValue(), endMonthIB.getCustomValue(), 
                endYearIB.getCustomValue());
    }
    
    private boolean validateInputs(){
        
        boolean isValid = true;
        int beginMonth = 0;
        int beginYear = 0;
        int endMonth = 0;
        int endYear = 0;
        
        try{
            beginMonth = beginMonthIB.getCustomValue();
            // It's 0, if empty
            if(beginMonth == 0){
                errBeginMonthLb.setText(constants.invalidNumberMsg());
                isValid = false;
            }else{
                errBeginMonthLb.setText("");
            }
        }catch(NumberFormatException e){
            errBeginMonthLb.setText(constants.invalidNumberMsg());
            isValid = false;
        }catch(InvalidValueException e){
            errBeginMonthLb.setText(constants.invalidNumberMsg());
            isValid = false;
        }
        
        try{
            beginYear = beginYearIB.getCustomValue();
            // It's 0, if empty
            if(beginYear == 0){
                errBeginYearLb.setText(constants.invalidNumberMsg());
                isValid = false;
            }else{
                errBeginYearLb.setText("");
            }
        }catch(NumberFormatException e){
            errBeginYearLb.setText(constants.invalidNumberMsg());
            isValid = false;
        }catch(InvalidValueException e){
            errBeginYearLb.setText(constants.invalidNumberMsg());
            isValid = false;
        }
        
        
        try{
            endMonth = endMonthIB.getCustomValue();
            // It's 0, if empty
            if(endMonth == 0){
                errEndMonthLb.setText(constants.invalidNumberMsg());
                isValid = false;
            }else{
                errEndMonthLb.setText("");
            }
        }catch(NumberFormatException e){
            errEndMonthLb.setText(constants.invalidNumberMsg());
            isValid = false;
        }catch(InvalidValueException e){
            errEndMonthLb.setText(constants.invalidNumberMsg());
            isValid = false;
        }
        
        try{
            endYear = endYearIB.getCustomValue();
            // It's 0, if empty
            if(endYear == 0){
                errEndYearLb.setText(constants.invalidNumberMsg());
                isValid = false;
            }else{
                errEndYearLb.setText("");
            }
        }catch(NumberFormatException e){
            errEndYearLb.setText(constants.invalidNumberMsg());
            isValid = false;
        }catch(InvalidValueException e){
            errEndYearLb.setText(constants.invalidNumberMsg());
            isValid = false;
        }

        if (isValid) {
	        if (Utils.compareDate(1, beginMonth, beginYear,
	        		Utils.getLastDay(endMonth, endYear), endMonth, endYear) > 0) {
	            errBeginMonthLb.setText(constants.invalidMsg());
	            errBeginYearLb.setText(constants.invalidMsg());
	            errEndMonthLb.setText(constants.invalidMsg());
	            errEndYearLb.setText(constants.invalidMsg());
	            isValid = false;
	        }
        }

        return isValid;
    }

    private void setInputsEnabled(boolean enabled){
        beginMonthIB.setEnabled(enabled);
        beginYearIB.setEnabled(enabled);
        endMonthIB.setEnabled(enabled);
        endYearIB.setEnabled(enabled);
    }
}
