package gwt.client.view.desktop;

import gwt.client.TCF;
import gwt.client.TConstants;
import gwt.client.def.ComDef;
import gwt.client.view.ComView;
import gwt.shared.model.SCom.ComType;
import gwt.shared.model.SCom.YearType;

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
    @UiField
    TextBox addressTB;
    @UiField
    Label errAddressLb;
    @UiField
    TextBox telNoTB;
    @UiField
    Label errTelNoLb;
    @UiField
    TextBox comTypeTB;
    @UiField
    Label errComTypeLb;
    @UiField
    TextBox taxIDTB;
    @UiField
    Label errTaxIDLb;
    @UiField
    TextBox merchantIDTB;
    @UiField
    Label errMerchantIDLb;
    @UiField
    TextBox yearTypeTB;
    @UiField
    Label errYearTypeLb;
    @UiField
    TextBox vatRateTB;
    @UiField
    Label errVatRateLb;
    
    private static final TConstants constants = TCF.get();
    
    private Presenter presenter;
    private ComDef<T> comDef;
    private String keyString;
    
    public ComViewImpl(ComDef<T> comDef) {
        this.comDef = comDef;
        initWidget(uiBinder.createAndBindUi(this));
        
        nameLb.setText(constants.name());
        
        addressTB.setVisible(false);
        telNoTB.setVisible(false);
        comTypeTB.setVisible(false);
        taxIDTB.setVisible(false);
        merchantIDTB.setVisible(false);
        vatRateTB.setVisible(false);
        yearTypeTB.setVisible(false);
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
        addressTB.setText("");
        errAddressLb.setText("");
        telNoTB.setText("");
        errTelNoLb.setText("");
        comTypeTB.setText("");
        errComTypeLb.setText("");
        taxIDTB.setText("");
        errTaxIDLb.setText("");
        merchantIDTB.setText("");
        errMerchantIDLb.setText("");
        yearTypeTB.setText("");
        errYearTypeLb.setText("");
        vatRateTB.setText("");
        errVatRateLb.setText("");
    }

    @Override
    public void setCom(T t, boolean editable) {
        if(t!=null){
            keyString = comDef.getKeyString(t);
            nameTB.setText(comDef.getName(t));
            addressTB.setText(comDef.getAddress(t));
            telNoTB.setText(comDef.getTelNo(t));
            comTypeTB.setText(comDef.getComType(t));
            taxIDTB.setText(comDef.getTaxID(t));
            merchantIDTB.setText(comDef.getMerchantID(t));
            yearTypeTB.setText(comDef.getYearType(t));
            vatRateTB.setText(comDef.getVatRate(t));
        }
        setInputsEnabled(editable);
    }

    @Override
    public void addComBtnClicked() {
        if(!validateInputs()){
            return;
        }
        
        presenter.addCom(nameTB.getValue(), "", "", ComType.LIMITED, "", "", YearType.THAI, 0.0);
    }

    @Override
    public void editComBtnClicked() {
        if(!validateInputs()){
            return;
        }
        
        presenter.editCom(keyString, nameTB.getValue(), "", "", ComType.LIMITED, "", "", YearType.THAI, 0.0);
    }
    
    private boolean validateInputs(){
        if(nameTB.getValue().isEmpty()){
            errNameLb.setText(constants.invalid());
            return false;
        }else{
            return true;
        }
    }

    private void setInputsEnabled(boolean enabled){
        nameTB.setEnabled(enabled);
        addressTB.setEnabled(enabled);
        telNoTB.setEnabled(enabled);
        comTypeTB.setEnabled(enabled);
        taxIDTB.setEnabled(enabled);
        merchantIDTB.setEnabled(enabled);
        yearTypeTB.setEnabled(enabled);
        vatRateTB.setEnabled(enabled);
    }
}
