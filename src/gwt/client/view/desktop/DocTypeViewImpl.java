package gwt.client.view.desktop;

import gwt.client.TCF;
import gwt.client.TConstants;
import gwt.client.def.FisDef;
import gwt.client.ui.CustomListBox;
import gwt.client.view.DocTypeView;
import gwt.shared.Utils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class DocTypeViewImpl<T> extends Composite implements DocTypeView<T> {

    @SuppressWarnings("rawtypes")
    @UiTemplate("DocTypeViewImpl.ui.xml")
    interface MyUiBinder extends UiBinder<Widget, DocTypeViewImpl> {
    }
    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    interface Style extends CssResource {
        
    }
    @UiField
    Style style;
    @UiField
    Label journalTypeLb;
    @UiField
    CustomListBox journalTypeLB;
    @UiField
    Label errJournalTypeLb;
    @UiField
    Label codeLb;
    @UiField
    TextBox codeTB;
    @UiField
    Label errCodeLb;
    @UiField
    Label nameLb;
    @UiField
    TextBox nameTB;
    @UiField
    Label errNameLb;
    @UiField
    Label journalDescLb;
    @UiField
    TextBox journalDescTB;
    @UiField
    Label errJournalDescLb;
    
    private static final TConstants constants = TCF.get();
    
    private Presenter presenter;
    private FisDef<T> fisDef;
    private String keyString;
    
    public DocTypeViewImpl(FisDef<T> fisDef) {
        this.fisDef = fisDef;
        initWidget(uiBinder.createAndBindUi(this));
        
        journalTypeLb.setText(constants.journalType());
        codeLb.setText(constants.code());
        nameLb.setText(constants.name());
        journalDescLb.setText(constants.journalDesc());
    }

    @Override
    public Widget asWidget() {
        return this;
    }
    
    @Override
    public void init(Presenter presenter) {
        this.presenter = presenter;
        
        keyString = null;
        journalTypeLB.clear();
        errJournalTypeLb.setText("");
        codeTB.setText("");
        errCodeLb.setText("");
        nameTB.setText("");
        errNameLb.setText("");
        journalDescTB.setText("");
        errJournalDescLb.setText("");
    }

    @Override
    public void setDocType(T t, String keyString, boolean editable) {
        
        journalTypeLB.addItem("", "");
        for(int i = 0; i<fisDef.getJTListSize(t); i++){
            journalTypeLB.addItem(fisDef.getJTShortName(t, i), fisDef.getJTKeyString(t, i));
        }
        
        if(keyString!=null){
            this.keyString = keyString;
            journalTypeLB.setSelectedValue(fisDef.getDTJTKeyString(t, keyString));
            codeTB.setText(fisDef.getDTCode(t, keyString));
            nameTB.setText(fisDef.getDTName(t, keyString));
            journalDescTB.setText(fisDef.getDTJournalDesc(t, keyString));
        }
        setInputsEnabled(editable);
    }

    @Override
    public void addDocTypeBtnClicked() {
        if(!validateInputs()){
            return;
        }
        
        presenter.addDocType(journalTypeLB.getValue(), codeTB.getValue(), nameTB.getValue(), journalDescTB.getValue());
    }

    @Override
    public void editDocTypeBtnClicked() {
        if(!validateInputs()){
            return;
        }
        
        presenter.editDocType(keyString, journalTypeLB.getValue(), codeTB.getValue(), nameTB.getValue(), journalDescTB.getValue());
    }
    
    private boolean validateInputs(){
        boolean isValid = true;
        
        if(journalTypeLB.getValue().isEmpty()){
            errJournalTypeLb.setText(constants.invalidMsg());
            isValid = false;
        }else{
            errJournalTypeLb.setText("");
        }
        
        if(codeTB.getValue().isEmpty() || Utils.hasSpace(codeTB.getValue())){
            errCodeLb.setText(constants.invalidMsg());
            isValid = false;
        }else{
            // Check duplicate code.
            if (presenter.isDocTypeCodeDuplicate(keyString, codeTB.getValue())) {
                errCodeLb.setText(constants.duplicateNameMsg());
                isValid = false;
            } else {
                errCodeLb.setText("");
            }
        }
        
        if(nameTB.getValue().isEmpty()){
            errNameLb.setText(constants.invalidMsg());
            isValid = false;
        }else{
            errNameLb.setText("");
        }
        
        return isValid;
    }

    private void setInputsEnabled(boolean enabled){
        journalTypeLB.setEnabled(enabled);
        codeTB.setEnabled(enabled);
        nameTB.setEnabled(enabled);
        journalDescTB.setEnabled(enabled);
        
        if (enabled) codeTB.setFocus(true);
    }
}
