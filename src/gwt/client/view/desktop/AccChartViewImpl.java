package gwt.client.view.desktop;

import gwt.client.TCF;
import gwt.client.TConstants;
import gwt.client.def.FisDef;
import gwt.client.place.AllPlace;
import gwt.client.ui.CustomListBox;
import gwt.client.ui.CustomSuggestBox;
import gwt.client.view.AccChartView;
import gwt.shared.InvalidValueException;
import gwt.shared.Utils;
import gwt.shared.model.SAccChart.AccType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class AccChartViewImpl<T> extends Composite implements AccChartView<T> {

    @SuppressWarnings("rawtypes")
    @UiTemplate("AccChartViewImpl.ui.xml")
    interface MyUiBinder extends UiBinder<Widget, AccChartViewImpl> {
    }
    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    interface Style extends CssResource {
        String noSB();
        String nameSB();
        String parentAccNoSB();
    }
    @UiField
    Style style;
    @UiField
    Label accGrpLb;
    @UiField
    CustomListBox accGrpLB;
    @UiField
    Label errAccGrpLb;
    @UiField
    Label parentAccNoLb;
    @UiField
    CustomSuggestBox parentAccNoSB;
    @UiField
    Label errParentAccNoLb;
    @UiField
    Label noLb;
    @UiField
    CustomSuggestBox noSB;
    @UiField
    Label errNoLb;
    @UiField
    Label nameLb;
    @UiField
    CustomSuggestBox nameSB;
    @UiField
    Label errNameLb;
    @UiField
    Label typeLb;
    @UiField
    CustomListBox typeLB;
    @UiField
    Label errTypeLb;
    @UiField
    Label levelLb;
    @UiField
    CustomListBox levelLB;
    @UiField
    Label errLevelLb;
    
    private static final TConstants constants = TCF.get();
    
    private Presenter presenter;
    private FisDef<T> fisDef;
    private String keyString;
    
    public AccChartViewImpl(FisDef<T> fisDef) {
        this.fisDef = fisDef;
        
        initWidget(uiBinder.createAndBindUi(this));

        accGrpLb.setText(constants.accGrp());
        parentAccNoLb.setText(constants.parentAccNo());
        noLb.setText(constants.accNo());
        nameLb.setText(constants.accName());
        typeLb.setText(constants.accType());
        levelLb.setText(constants.level());

        noSB.addTextBoxStyleName(style.noSB());
        nameSB.addTextBoxStyleName(style.nameSB());
        parentAccNoSB.addTextBoxStyleName(style.parentAccNoSB());
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
        nameSB.clear();
        errNameLb.setText("");
        parentAccNoSB.clear();
        errParentAccNoLb.setText("");
        typeLB.clear();
        errTypeLb.setText("");
        levelLB.clear();
        errLevelLb.setText("");
        accGrpLB.clear();
        errAccGrpLb.setText("");
    }

    @Override
    public void setAccChart(T t, String action, String keyString, String newType) {

        for(int i=0; i<fisDef.getACListSize(t); i++){
            noSB.add(fisDef.getACNo(t, i), fisDef.getACNo(t, i));
            nameSB.add(fisDef.getACName(t, i), fisDef.getACName(t, i));
            
            if(fisDef.getACIsControl(t, i)){
                parentAccNoSB.add(fisDef.getACKeyString(t, i), fisDef.getACNo(t, i) + " - " + fisDef.getACName(t, i));
            }
        }
        
        typeLB.addItem("", "");
        typeLB.addItem(constants.control(), AccType.CONTROL.name());
        typeLB.addItem(constants.entry(), AccType.ENTRY.name());
        
        levelLB.addItem("", "");
        for(int i=1; i<6; i++){
            levelLB.addItem(i + "", i + "");
        }
        
        accGrpLB.addItem("", "");
        for(int i = 0; i<fisDef.getAGListSize(t); i++){
            accGrpLB.addItem(fisDef.getAGName(t, i), fisDef.getAGKeyString(t, i));
        }
        
        if(action.equals(AllPlace.EDIT) || action.equals(AllPlace.VIEW)){
            this.keyString = keyString;
            noSB.setKey(fisDef.getACNo(t, keyString));
            nameSB.setKey(fisDef.getACName(t, keyString));
            parentAccNoSB.setKey(fisDef.getACParentACKeyString(t, keyString));
            
            typeLB.setSelectedValue(fisDef.getACType(t, keyString).name());
            levelLB.setSelectedValue(fisDef.getACLevel(t, keyString) + "");
            accGrpLB.setSelectedValue(fisDef.getACAGKeyString(t, keyString));
            
        }else if(action.equals(AllPlace.NEW) && keyString != null){
            if (newType.equals(AllPlace.CHILD)) {
                if (fisDef.getACType(t, keyString).equals(AccType.CONTROL)) {
                    parentAccNoSB.setKey(keyString);
                    levelLB.setSelectedValue((fisDef.getACLevel(t, keyString) + 1) + "");
                    accGrpLB.setSelectedValue(fisDef.getACAGKeyString(t, keyString));
                }
            } else if (newType.equals(AllPlace.SIBLING)) {
                String pKeyString = fisDef.getACParentACKeyString(t, keyString);
                parentAccNoSB.setKey(pKeyString);
                levelLB.setSelectedValue(fisDef.getACLevel(t, keyString) + "");
                accGrpLB.setSelectedValue(fisDef.getACAGKeyString(t, keyString));
                
                typeLB.setSelectedValue(fisDef.getACType(t, keyString).name());
            }
        }
        
        if(action.equals(AllPlace.EDIT) || action.equals(AllPlace.NEW)){
            setInputsEnabled(true);
        }else if(action.equals(AllPlace.VIEW)){
            setInputsEnabled(false);
        }else{
            throw new AssertionError();
        }
    }

    @Override
    public void addAccChartBtnClicked() {
        if(!validateInputs()){
            return;
        }
        
        presenter.addAccChart(accGrpLB.getValue(), parentAccNoSB.getKey(), noSB.getValue(), nameSB.getValue(), 
                AccType.valueOf(typeLB.getValue()), Integer.parseInt(levelLB.getValue()));
    }

    @Override
    public void editAccChartBtnClicked() {
        if(!validateInputs()){
            return;
        }
        
        presenter.editAccChart(keyString, accGrpLB.getValue(), parentAccNoSB.getKey(), noSB.getValue(), nameSB.getValue(), 
                AccType.valueOf(typeLB.getValue()), Integer.parseInt(levelLB.getValue()));
    }
    
    private boolean validateInputs(){
        boolean isValid = true;

        
        if(noSB.getValue().isEmpty() || Utils.hasSpace(noSB.getValue())){
            errNoLb.setText(constants.invalidMsg());
            isValid = false;
        }else{
        	// Check duplicate acc no.
        	if (presenter.isAccChartNoDuplicate(keyString, noSB.getValue())) {
        	    errNoLb.setText(constants.duplicateAccChartNoMsg());
                isValid = false;
        	} else {
        	    errNoLb.setText("");
        	}
        }
        
        if(nameSB.getValue().isEmpty()){
            errNameLb.setText(constants.invalidMsg());
            isValid = false;
        }else{
        	// Check duplicate name.
        	if (presenter.isAccChartNameDuplicate(keyString, nameSB.getValue())) {
        	    errNameLb.setText(constants.duplicateNameMsg());
        	    isValid = false;
        	} else {
        	    errNameLb.setText("");
        	}
        }
        
        try{
            parentAccNoSB.getKey();
            errParentAccNoLb.setText("");
        }catch(InvalidValueException e){
            errParentAccNoLb.setText(constants.invalidMsg());
            isValid = false;
        }
        
        if(typeLB.getValue().isEmpty()){
            errTypeLb.setText(constants.invalidMsg());
            isValid = false;
        }else{
            errTypeLb.setText("");
        }
        
        if(levelLB.getValue().isEmpty()){
            errLevelLb.setText(constants.invalidMsg());
            isValid = false;
        }else{
            errLevelLb.setText("");
        }
        
        if(accGrpLB.getValue().isEmpty()){
            errAccGrpLb.setText(constants.invalidMsg());
            isValid = false;
        }else{
            errAccGrpLb.setText("");
        }
        
        if (isValid) {
            // Level is greater than parent's
            if (!presenter.isAccChartLevelValid(parentAccNoSB.getKey(),
                    Integer.parseInt(levelLB.getValue()))) {
                errLevelLb.setText(constants.invalidMsg());
                isValid = false;
            }
            
            if (keyString != null) {
                // Check type changes
                if (!presenter.isAccChartTypeValid(keyString,
                        AccType.valueOf(typeLB.getValue()))) {
                    errTypeLb.setText(constants.invalidMsg());
                    isValid = false;
                }
            }
        }

        return isValid;
    }

    private void setInputsEnabled(boolean enabled){
        noSB.setEnabled(enabled);
        nameSB.setEnabled(enabled);
        parentAccNoSB.setEnabled(enabled);
        typeLB.setEnabled(enabled);
        levelLB.setEnabled(enabled);
        accGrpLB.setEnabled(enabled);
        
        if (enabled) noSB.setFocus(true);
    }
}
