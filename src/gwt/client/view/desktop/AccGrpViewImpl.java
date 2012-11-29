package gwt.client.view.desktop;

import gwt.client.TCF;
import gwt.client.TConstants;
import gwt.client.def.FisDef;
import gwt.client.view.AccGrpView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class AccGrpViewImpl<T> extends Composite implements AccGrpView<T> {

    @SuppressWarnings("rawtypes")
    @UiTemplate("AccGrpViewImpl.ui.xml")
    interface MyUiBinder extends UiBinder<Widget, AccGrpViewImpl> {
    }
    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    interface Style extends CssResource {
        
    }
    @UiField
    Style style;
    @UiField
    Label nameLb;
    @UiField
    TextBox nameTB;
    @UiField
    Label errNameLb;
    
    private static final TConstants constants = TCF.get();
    
    private Presenter presenter;
    private FisDef<T> fisDef;
    private String keyString;
    
    public AccGrpViewImpl(FisDef<T> fisDef) {
        this.fisDef = fisDef;
        initWidget(uiBinder.createAndBindUi(this));
        
        nameLb.setText(constants.name());
    }

    @Override
    public Widget asWidget() {
        return this;
    }
    
    @Override
    public void init(Presenter presenter) {
        this.presenter = presenter;
        
        keyString = null;
        nameTB.setText("");
        errNameLb.setText("");
    }

    @Override
    public void setAccGrp(T t, String keyString, boolean editable) {
        if(keyString != null){
            this.keyString = keyString;
            nameTB.setText(fisDef.getAGName(t, keyString));
        }
        
        setInputsEnabled(editable);
    }

    @Override
    public void addAccGrpBtnClicked() {
        if(!validateInputs()){
            return;
        }
        
        presenter.addAccGrp(nameTB.getValue());
    }

    @Override
    public void editAccGrpBtnClicked() {
        if(!validateInputs()){
            return;
        }
        
        presenter.editAccGrp(keyString, nameTB.getValue());
    }

    private boolean validateInputs(){
        boolean isValid = true;
        
        if(nameTB.getValue().isEmpty()){
            errNameLb.setText(constants.invalid());
            isValid = false;
        }else{
            errNameLb.setText("");
        }
        
        return isValid;
    }
    
    private void setInputsEnabled(boolean enabled){
        nameTB.setEnabled(enabled);
    }
}
