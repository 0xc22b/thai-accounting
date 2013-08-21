package gwt.client.view.desktop;

import gwt.client.TCF;
import gwt.client.TConstants;
import gwt.client.def.FisDef;
import gwt.client.view.FinHeaderView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class FinHeaderViewImpl<T> extends Composite implements FinHeaderView<T> {

    @SuppressWarnings("rawtypes")
    @UiTemplate("FinHeaderViewImpl.ui.xml")
    interface MyUiBinder extends UiBinder<Widget, FinHeaderViewImpl> {
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

    public FinHeaderViewImpl(FisDef<T> fisDef) {
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
    public void setFinHeader(T t, String keyString, boolean editable) {

        if (keyString == null) {
            
        } else {
            this.keyString = keyString;

            nameTB.setText(fisDef.getFinHeaderName(t, keyString));
        }
        setInputsEnabled(editable);
    }

    @Override
    public void addFinHeaderBtnClicked() {
        if(!validateInputs()){
            return;
        }
        
        presenter.addFinHeader(nameTB.getValue());
    }

    @Override
    public void editFinHeaderBtnClicked() {
        if(!validateInputs()){
            return;
        }
        
        presenter.editFinHeader(keyString, nameTB.getValue());
    }

    private void setInputsEnabled(boolean enabled) {
        nameTB.setEnabled(enabled);
    }
    
    private boolean validateInputs(){
        boolean isValid = true;
        
        if(nameTB.getValue().isEmpty()){
            errNameLb.setText(constants.invalidMsg());
            isValid = false;
        }else{
            errNameLb.setText("");
        }
        
        return isValid;
    }
}
