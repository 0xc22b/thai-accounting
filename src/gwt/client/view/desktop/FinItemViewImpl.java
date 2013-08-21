package gwt.client.view.desktop;

import gwt.client.TCF;
import gwt.client.TConstants;
import gwt.client.def.FisDef;
import gwt.client.ui.CustomIntBox;
import gwt.client.ui.CustomListBox;
import gwt.client.ui.CustomSuggestBox;
import gwt.client.view.FinItemView;
import gwt.shared.InvalidValueException;
import gwt.shared.model.SFinItem.CalCon;
import gwt.shared.model.SFinItem.Comm;
import gwt.shared.model.SFinItem.Operand;
import gwt.shared.model.SFinItem.PrintCon;
import gwt.shared.model.SFinItem.PrintStyle;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class FinItemViewImpl<T> extends Composite implements FinItemView<T> {

    @SuppressWarnings("rawtypes")
    @UiTemplate("FinItemViewImpl.ui.xml")
    interface MyUiBinder extends UiBinder<Widget, FinItemViewImpl> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    interface Style extends CssResource {
        String accNoSB();
    }

    @UiField
    Style style;
    
    @UiField
    Label seqLb;
    @UiField
    CustomIntBox seqIB;
    @UiField
    Label errSeqLb;
    @UiField
    Label commLb;
    @UiField
    CustomListBox commLB;
    @UiField
    Label argLb;
    @UiField
    TextBox argTB;
    @UiField
    CustomSuggestBox accNoSB;
    @UiField
    Label errArgLb;
    @UiField
    Label calConLb;
    @UiField
    CustomListBox calConLB;    
    @UiField
    Label printConLb;
    @UiField
    CustomListBox printConLB;    
    @UiField
    Label printStyleLb;
    @UiField
    CustomListBox printStyleLB;
    @UiField
    Label var1Lb;
    @UiField
    CustomListBox var1LB;
    @UiField
    Label var2Lb;
    @UiField
    CustomListBox var2LB;
    @UiField
    Label var3Lb;
    @UiField
    CustomListBox var3LB;
    @UiField
    Label var4Lb;
    @UiField
    CustomListBox var4LB;

    private static final TConstants constants = TCF.get();

    private Presenter presenter;
    private FisDef<T> fisDef;
    private String keyString;

    public FinItemViewImpl(FisDef<T> fisDef) {
        this.fisDef = fisDef;
        initWidget(uiBinder.createAndBindUi(this));
        
        seqLb.setText(constants.seq());
        commLb.setText(constants.comm());
        argLb.setText(constants.arg());
        calConLb.setText(constants.calCon());
        printConLb.setText(constants.printCon());
        printStyleLb.setText(constants.printStyle());
        var1Lb.setText(constants.var1());
        var2Lb.setText(constants.var2());
        var3Lb.setText(constants.var3());
        var4Lb.setText(constants.var4());
        
        Comm[] comms = Comm.values();
        commLB.clear();
        for(int i=0; i<comms.length; i++){
            commLB.addItem(comms[i].name(), comms[i].name());
        }
        commLB.addChangeHandler(commLBChangeHandler);
        
        accNoSB.addTextBoxStyleName(style.accNoSB());
        
        calConLB.clear();
        CalCon[] calCons = CalCon.values();
        for(int i=0; i<calCons.length; i++){
            calConLB.addItem(calCons[i].name(), calCons[i].name());
        }
        
        printConLB.clear();
        PrintCon[] printCons = PrintCon.values();
        for(int i=0; i<printCons.length; i++){
            printConLB.addItem(printCons[i].name(), printCons[i].name());
        }
        
        printStyleLB.clear();
        PrintStyle[] printStyles = PrintStyle.values();
        for(int i=0; i<printStyles.length; i++){
            printStyleLB.addItem(printStyles[i].name(), printStyles[i].name());
        }
        
        var1LB.clear();
        var2LB.clear();
        var3LB.clear();
        var4LB.clear();
        Operand[] operands = Operand.values();
        for(int i=0; i<operands.length; i++){
            var1LB.addItem(operands[i].name(), operands[i].name());
            var2LB.addItem(operands[i].name(), operands[i].name());
            var3LB.addItem(operands[i].name(), operands[i].name());
            var4LB.addItem(operands[i].name(), operands[i].name());
        }
    }

    @Override
    public Widget asWidget() {
        return this;
    }

    @Override
    public void init(Presenter presenter) {
        this.presenter = presenter;

        keyString = null;
        
        seqIB.clear();
        errSeqLb.setText("");
        commLB.setSelectedIndex(0);
        argTB.setText("");
        accNoSB.clear();
        errArgLb.setText("");
        calConLB.setSelectedIndex(0);
        printConLB.setSelectedIndex(0);
        printStyleLB.setSelectedIndex(0);
        var1LB.setSelectedIndex(0);
        var2LB.setSelectedIndex(0);
        var3LB.setSelectedIndex(0);
        var4LB.setSelectedIndex(0);
        
        argTB.setVisible(true);
        accNoSB.setVisible(false);
    }

    @Override
    public void setFinItem(T t, String finHeaderKeyString,
            String keyString, boolean editable) {

        accNoSB.clear();
        for(int i=0; i<fisDef.getACListSize(t); i++){
            if(fisDef.getACIsEntry(t, i)){
                accNoSB.add(fisDef.getACKeyString(t, i), fisDef.getACNo(t, i) + " - " + fisDef.getACName(t, i));
            }
        }
        
        if (keyString == null) {

        } else {
            this.keyString = keyString;
            
            seqIB.setCustomValue(fisDef.getFinItemSeq(t, finHeaderKeyString, keyString));
            commLB.setSelectedValue(fisDef.getFinItemComm(t, finHeaderKeyString, keyString).name());
            
            if(fisDef.getFinItemComm(t, finHeaderKeyString, keyString).equals(Comm.ACCNO)){
                accNoSB.setKey(fisDef.getFinItemArg(t, finHeaderKeyString, keyString));
                argTB.setVisible(false);
                accNoSB.setVisible(true);
            }else{
                argTB.setText(fisDef.getFinItemArg(t, finHeaderKeyString, keyString));
            }
            
            DomEvent.fireNativeEvent(Document.get().createChangeEvent(), commLB);
            
            calConLB.setSelectedValue(fisDef.getFinItemCalCon(t, finHeaderKeyString, keyString).name());
            printConLB.setSelectedValue(fisDef.getFinItemPrintCon(t, finHeaderKeyString, keyString).name());
            printStyleLB.setSelectedValue(fisDef.getFinItemPrintStyle(t, finHeaderKeyString, keyString).name());
            var1LB.setSelectedValue(fisDef.getFinItemVar1(t, finHeaderKeyString, keyString).name());
            var2LB.setSelectedValue(fisDef.getFinItemVar2(t, finHeaderKeyString, keyString).name());
            var3LB.setSelectedValue(fisDef.getFinItemVar3(t, finHeaderKeyString, keyString).name());
            var4LB.setSelectedValue(fisDef.getFinItemVar4(t, finHeaderKeyString, keyString).name());
        }
        setInputsEnabled(editable);
    }

    @Override
    public void addFinItemBtnClicked() {
        if(!validateInputs()){
            return;
        }
        
        presenter.addFinItem(getSeq(), getComm(), getArg(), getCalCon(),
                getPrintCon(), getPrintStyle(), getVar1(), getVar2(), getVar3(),
                getVar4());
    }

    @Override
    public void editFinItemBtnClicked() {
        if(!validateInputs()){
            return;
        }
        
        presenter.editFinItem(keyString, getSeq(), getComm(), getArg(),
                getCalCon(), getPrintCon(), getPrintStyle(), getVar1(),
                getVar2(), getVar3(), getVar4());
    }

    private void setInputsEnabled(boolean enabled) {
        seqIB.setEnabled(enabled);
        commLB.setEnabled(enabled);
        argTB.setEnabled(enabled);
        accNoSB.setEnabled(enabled);
        calConLB.setEnabled(enabled);
        printConLB.setEnabled(enabled);
        printStyleLB.setEnabled(enabled);
        var1LB.setEnabled(enabled);
        var2LB.setEnabled(enabled);
        var3LB.setEnabled(enabled);
        var4LB.setEnabled(enabled);
    }
    
    private boolean validateInputs(){
        
        boolean isValid = true;
        
        try{
            int n = getSeq();
            if (n == 0) {
                errSeqLb.setText(constants.invalidMsg());
                isValid = false;
            } else {
                errSeqLb.setText("");
            }
        }catch(NumberFormatException e){
            errSeqLb.setText(constants.invalidMsg());
            isValid = false;
        }catch(InvalidValueException e){
            errSeqLb.setText(constants.invalidMsg());
            isValid = false;
        }
        
        if(getComm().equals(Comm.ACCNO)){
            try{
                String k = getArg();
                if(k == null){
                    errArgLb.setText(constants.invalidMsg());
                    isValid = false;
                } else {
                    errArgLb.setText("");
                }
            }catch(InvalidValueException e){
                errArgLb.setText(constants.invalidMsg());
                isValid = false;
            }
        } else {
            errArgLb.setText("");
        }
        return isValid;
    }
    
    private ChangeHandler commLBChangeHandler = new ChangeHandler(){
        @Override
        public void onChange(ChangeEvent event) {
            if(Comm.valueOf(commLB.getValue()).equals(Comm.ACCNO)){
                accNoSB.setVisible(true);
                argTB.setVisible(false);
            }else{
                accNoSB.setVisible(false);
                argTB.setVisible(true);
            }
        }
    };
    
    private int getSeq(){
        return seqIB.getCustomValue();
    }
    
    private Comm getComm(){
        return Comm.valueOf(commLB.getValue());
    }
    
    private String getArg(){
        return getComm().equals(Comm.ACCNO) ? accNoSB.getKey() : argTB.getValue();
    }
    
    private CalCon getCalCon(){
        return CalCon.valueOf(calConLB.getValue());
    }
    
    private PrintCon getPrintCon(){
        return PrintCon.valueOf(printConLB.getValue());
    }
    
    private PrintStyle getPrintStyle(){
        return PrintStyle.valueOf(printStyleLB.getValue());
    }
    
    private Operand getVar1(){
        return Operand.valueOf(var1LB.getValue());
    }
    
    private Operand getVar2(){
        return Operand.valueOf(var2LB.getValue());
    }
    
    private Operand getVar3(){
        return Operand.valueOf(var3LB.getValue());
    }
    
    private Operand getVar4(){
        return Operand.valueOf(var4LB.getValue());
    }
}
