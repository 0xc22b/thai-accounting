package gwt.client.view.desktop;

import gwt.client.TCF;
import gwt.client.TConstants;
import gwt.client.def.FisDef;
import gwt.client.view.JournalTypeView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class JournalTypeViewImpl<T> extends Composite implements JournalTypeView<T> {

    @SuppressWarnings("rawtypes")
    @UiTemplate("JournalTypeViewImpl.ui.xml")
    interface MyUiBinder extends UiBinder<Widget, JournalTypeViewImpl> {
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
    @UiField
    Label shortNameLb;
    @UiField
    TextBox shortNameTB;
    @UiField
    Label errShortNameLb;
    
    private static final TConstants constants = TCF.get();
    
    private Presenter presenter;
    private FisDef<T> fisDef;
    private String keyString;
    
    public JournalTypeViewImpl(FisDef<T> fisDef) {
        this.fisDef = fisDef;
        initWidget(uiBinder.createAndBindUi(this));
        
        nameLb.setText(constants.name());
        shortNameLb.setText(constants.shortName());
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
        shortNameTB.setText("");
        errShortNameLb.setText("");
    }

    @Override
    public void setJournalType(T t, String keyString, boolean editable) {
        if(keyString != null){
            this.keyString = keyString;
            nameTB.setText(fisDef.getJTName(t, keyString));
            shortNameTB.setText(fisDef.getJTShortName(t, keyString));
        }
        setInputsEnabled(editable);
    }

    @Override
    public void addJournalTypeBtnClicked() {
        if(!validateInputs()){
            return;
        }
        
        presenter.addJournalType(nameTB.getValue(), shortNameTB.getValue());
    }

    @Override
    public void editJournalTypeBtnClicked() {
        if(!validateInputs()){
            return;
        }
        
        presenter.editJournalType(keyString, nameTB.getValue(), shortNameTB.getValue());
    }
    
    private boolean validateInputs(){
        boolean isValid = true;
        
        if(nameTB.getValue().isEmpty()){
            errNameLb.setText(constants.invalid());
            isValid = false;
        }else{
            errNameLb.setText("");
        }
        
        if(shortNameTB.getValue().isEmpty()){
            errShortNameLb.setText(constants.invalid());
            isValid = false;
        }else{
            errShortNameLb.setText("");
        }
        
        return isValid;
    }

    private void setInputsEnabled(boolean enabled){
        nameTB.setEnabled(enabled);
        shortNameTB.setEnabled(enabled);
    }
}
