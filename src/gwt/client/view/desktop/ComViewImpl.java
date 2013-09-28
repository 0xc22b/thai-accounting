package gwt.client.view.desktop;

import gwt.client.TCF;
import gwt.client.TConstants;
import gwt.client.def.ComDef;
import gwt.client.view.ComView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class ComViewImpl<T> extends Composite implements ComView<T> {

    @SuppressWarnings("rawtypes")
    @UiTemplate("ComViewImpl.ui.xml")
    interface MyUiBinder extends UiBinder<Widget, ComViewImpl> {
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
    private ComDef<T> comDef;
    private String keyString;
    
    public ComViewImpl(ComDef<T> comDef) {
        this.comDef = comDef;
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
    public void setCom(T t, boolean editable) {
        if(t!=null){
            keyString = comDef.getKeyString(t);
            nameTB.setText(comDef.getName(t));
        }
        setInputsEnabled(editable);
    }

    @Override
    public void addComBtnClicked() {
        if(!validateInputs()){
            return;
        }
        
        presenter.addCom(nameTB.getValue());
    }

    @Override
    public void editComBtnClicked() {
        if(!validateInputs()){
            return;
        }
        
        presenter.editCom(keyString, nameTB.getValue());
    }
    
    private boolean validateInputs(){
        if(nameTB.getValue().isEmpty()){
            errNameLb.setText(constants.invalidMsg());
            return false;
        }else{
            return true;
        }
    }

    private void setInputsEnabled(boolean enabled){
        nameTB.setEnabled(enabled);
    }
}
