package gwt.client.view.desktop;

import gwt.client.TCF;
import gwt.client.TConstants;
import gwt.client.def.FisDef;
import gwt.client.def.JournalDef;
import gwt.client.ui.CustomDoubleBox;
import gwt.client.ui.CustomDoubleBox.DoubleBoxCallback;
import gwt.client.ui.CustomIntBox;
import gwt.client.ui.CustomListBox;
import gwt.client.ui.CustomSuggestBox;
import gwt.client.view.JournalView;
import gwt.shared.InvalidValueException;
import gwt.shared.Utils;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class JournalViewImpl<T, J, M> extends Composite implements JournalView<T, J, M> {

    private class Item extends Composite {

        FlowPanel panel;
        Label errLb;
        CustomSuggestBox accNoSB;
        CustomDoubleBox debitDB;
        CustomDoubleBox creditDB;
        Button removeBtn;

        public Item(T t) {
            panel = new FlowPanel();
            panel.addStyleName(style.row() + " " + style.clearfix());

            errLb = new Label();
            errLb.addStyleName(style.errLb());
            panel.add(errLb);

            accNoSB = new CustomSuggestBox();
            accNoSB.addTextBoxStyleName(style.accNoSB());
            accNoSB.addStyleName(style.cell() + " " + style.accNoCell());
            for (int i = 0; i < fisDef.getACListSize(t); i++) {
                if (fisDef.getACIsEntry(t, i)) {
                    accNoSB.add(fisDef.getACKeyString(t, i), fisDef.getACNo(t, i) + " - " + fisDef.getACName(t, i));
                }
            }
            panel.add(accNoSB);

            debitDB = new CustomDoubleBox();
            debitDB.setRange(0.00, 9999999999999.99);
            debitDB.addStyleName(style.cell() + " " + style.debitCell());
            debitDB.addDoubleBoxCallback(doubleBoxCallback);
            panel.add(debitDB);

            creditDB = new CustomDoubleBox();
            creditDB.setRange(0.00, 9999999999999.99);
            creditDB.addStyleName(style.cell() + " " + style.creditCell());
            creditDB.addDoubleBoxCallback(doubleBoxCallback);
            panel.add(creditDB);

            removeBtn = new Button(constants.remove());
            removeBtn.addStyleName(style.cell());
            removeBtn.addClickHandler(removeBtnClickHandler);
            panel.add(removeBtn);
            
            initWidget(panel);
        }

        public Item(T t, M m) {
            this(t);

            accNoSB.setKey(journalDef.getItemACKeyString(m));
            double amt = journalDef.getItemAmt(m);
            if (amt > 0) {
                debitDB.setCustomValue(amt);
            } else {
                creditDB.setCustomValue(Math.abs(amt));
            }
        }

        public void setEnabled(boolean enabled) {
            accNoSB.setEnabled(enabled);
            debitDB.setEnabled(enabled);
            creditDB.setEnabled(enabled);
            removeBtn.setEnabled(enabled);
        }
        
        public void setErrText(String text) {
            errLb.setText(text);
        }

        public String getACKeyString() {
            return accNoSB.getKey();
        }

        public double getDebit() {
            return debitDB.getCustomValue();
        }

        public double getCredit() {
            return creditDB.getCustomValue();
        }
        
        private DoubleBoxCallback doubleBoxCallback = new DoubleBoxCallback() {
            @Override
            public void onInvalidInput(String input) {
                errLb.setText(constants.invalidMsg());
            }

            @Override
            public void onValidInput(String input) {
                if (validateInputs()) {
                    validateTotal();
                }
            }
        };
        
        private ClickHandler removeBtnClickHandler = new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                Item.this.removeFromParent();
                validateTotal();
            }
        };
        
        private boolean validateInputs() {
            boolean isValid = true;
            
            try {
                String accNoKeyString = getACKeyString();
                if (accNoKeyString != null) {
                    try {
                        double debit = getDebit();
                        double credit = getCredit();

                        if (debit == 0 && credit == 0) {
                            errLb.setText(constants.invalidMsg());
                            isValid = false;
                        } else if (debit != 0 && credit != 0) {
                            errLb.setText(constants.invalidMsg());
                            isValid = false;
                        } else {
                            errLb.setText("");
                        }
                    } catch (NumberFormatException e) {
                        errLb.setText(constants.invalidNumberMsg());
                        isValid = false;
                    } catch (InvalidValueException e) {
                        errLb.setText(constants.invalidNumberMsg());
                        isValid = false;
                    }
                }
            } catch (InvalidValueException e) {
                errLb.setText(constants.invalidMsg());
                isValid = false;
            }
            
            return isValid;
        }
    }

    @SuppressWarnings("rawtypes")
    @UiTemplate("JournalViewImpl.ui.xml")
    interface MyUiBinder extends UiBinder<Widget, JournalViewImpl> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    interface Style extends CssResource {
        String row();

        String errLb();

        String cell();

        String accNoCell();

        String debitCell();

        String creditCell();

        String accNoSB();

        String clearfix();
    }

    @UiField
    Style style;
    @UiField
    Label journalTypeLb;
    @UiField
    Label journalTypeInputLb;
    @UiField
    Label docTypeLb;
    @UiField
    CustomListBox docTypeLB;
    @UiField
    Label errDocTypeLb;
    @UiField
    Label noLb;
    @UiField
    TextBox noTB;
    @UiField
    Button suggestedNoBtn;
    @UiField
    Label errNoLb;
    @UiField
    Label dateLb;
    @UiField
    Label dayLb;
    @UiField
    CustomIntBox dayIB;
    @UiField
    Label monthLb;
    @UiField
    Label monthInputLb;
    @UiField
    Label yearLb;
    @UiField
    Label yearInputLb;
    @UiField
    Label errDateLb;
    @UiField
    Label descLb;
    @UiField
    TextBox descTB;
    @UiField
    Label errDescLb;
    @UiField
    Label accNoLb;
    @UiField
    Label debitLb;
    @UiField
    Label creditLb;
    @UiField
    FlowPanel itemPanel;
    @UiField
    Label totalDebitLb;
    @UiField
    Label totalCreditLb;
    @UiField
    Label errTotalLb;

    private static final TConstants constants = TCF.get();

    private Presenter presenter;

    private FisDef<T> fisDef;
    private JournalDef<J, M> journalDef;

    private String keyString;

    private int month;
    private int year;
    
    private int beginMonth;
    private int beginYear;
    private int endMonth;
    private int endYear;

    public JournalViewImpl(FisDef<T> fisDef, JournalDef<J, M> journalDef) {
        this.fisDef = fisDef;
        this.journalDef = journalDef;
        initWidget(uiBinder.createAndBindUi(this));

        journalTypeLb.setText(constants.journalType() + ":");
        docTypeLb.setText(constants.docType());
        noLb.setText(constants.journalNo());
        dateLb.setText(constants.date());
        dayLb.setText(constants.day());
        monthLb.setText(constants.month());
        yearLb.setText(constants.year());
        descLb.setText(constants.desc());

        accNoLb.setText(constants.accNo());
        debitLb.setText(constants.debit());
        creditLb.setText(constants.credit());

        dayIB.setRange(1, 31);

        docTypeLB.addChangeHandler(docTypeChangeHandler);
        suggestedNoBtn.addClickHandler(suggestedNoBtnClickHandler);
    }

    @Override
    public Widget asWidget() {
        return this;
    }

    @Override
    public void init(Presenter presenter) {
        this.presenter = presenter;

        keyString = null;
        
        month = 0;
        year = 0;

        beginMonth = 0;
        beginYear = 0;
        endMonth = 0;
        endYear = 0;

        docTypeLB.clear();

        journalTypeInputLb.setText("");

        // Don't reset here as it will be used for suggestion later.
        //noTB.setText("");
        errDocTypeLb.setText("");

        suggestedNoBtn.setVisible(false);
        errNoLb.setText("");

        // Leave them here as suggestions.
        //dayIB.clear();

        monthInputLb.setText("");
        yearInputLb.setText("");

        errDateLb.setText("");
        descTB.setText("");
        errDescLb.setText("");
        itemPanel.clear();
        totalCreditLb.setText(NumberFormat.getFormat("#,##0.00").format(0));
        totalDebitLb.setText(NumberFormat.getFormat("#,##0.00").format(0));
    }

    @Override
    public void setJournal(T t, String journalTypeKeyString, int month, int year, J j,
            boolean editable) {

        beginMonth = fisDef.getFBeginMonth(t);
        beginYear = fisDef.getFBeginYear(t);
        endMonth = fisDef.getFEndMonth(t);
        endYear = fisDef.getFEndYear(t);

        journalTypeInputLb.setText(fisDef.getJTShortName(t, journalTypeKeyString));

        if (fisDef.getDTListSize(t) != 0) {
            for (int i = 0; i < fisDef.getDTListSize(t); i++) {
                if (fisDef.getDTJTKeyString(t, i).equals(journalTypeKeyString)) {
                    docTypeLB.addItem(fisDef.getDTCode(t, i), fisDef.getDTKeyString(t, i));
                }
            }
            
            // change docTypeLB -> update descTB
            descTB.setText(fisDef.getDTJournalDesc(t, docTypeLB.getValue()));
        } else {
            errDocTypeLb.setText(constants.invalidMsg());
        }

        this.month = month;
        this.year = year;

        monthInputLb.setText(month + "");
        yearInputLb.setText(year + "");

        if (j == null) {
            // Try to get suggested no.
            String no = noTB.getValue();
            try {
                int n = Integer.parseInt(no) + 1;
                suggestedNoBtn.setText(n + "");
                suggestedNoBtn.setVisible(true);
            } catch (NumberFormatException e) {
            }
            noTB.setText("");
            
            itemPanel.add(new Item(t));
            itemPanel.add(new Item(t));
            itemPanel.add(new Item(t));
        } else {
            this.keyString = journalDef.getKeyString(j);

            noTB.setText(journalDef.getNo(j));
            dayIB.setCustomValue(journalDef.getDay(j));

            descTB.setText(journalDef.getDesc(j));

            //Items            
            for (M m : journalDef.getItemList(j)) {
                Item journalItemPanel = new Item(t, m);
                itemPanel.add(journalItemPanel);
            }
            
            validateInputs();
        }
        setInputsEnabled(editable);
    }

    @Override
    public void addJournalBtnClicked() {
        if (!validateInputs()) {
            return;
        }

        ArrayList<ItemData> itemDataList = genItemDataList();
        presenter.addJournal(docTypeLB.getValue(), noTB.getValue(), dayIB.getCustomValue(),
                month, year, descTB.getValue(), itemDataList);
    }

    @Override
    public void editJournalBtnClicked() {
        if (!validateInputs()) {
            return;
        }

        ArrayList<ItemData> itemDataList = genItemDataList();
        presenter.editJournal(keyString, docTypeLB.getValue(), noTB.getValue(),
                dayIB.getCustomValue(), month, year, descTB.getValue(), itemDataList);
    }

    @Override
    public void addItemBtnClicked(T t) {
        itemPanel.add(new Item(t));
    }
    
    @Override
    public void updateJournalDesc(T t) {
        String s = descTB.getText();
        for(int i = 0; i < docTypeLB.getItemCount(); i++){
            if(fisDef.getDTJournalDesc(t, docTypeLB.getValue(i)).equals(s)){
                descTB.setText(fisDef.getDTJournalDesc(t, docTypeLB.getValue()));
                break;
            }
        }
    }

    private void setInputsEnabled(boolean enabled) {
        docTypeLB.setEnabled(enabled);
        noTB.setEnabled(enabled);
        dayIB.setEnabled(enabled);
        descTB.setEnabled(enabled);

        for (int i = 0; i < itemPanel.getWidgetCount(); i++) {
            @SuppressWarnings("unchecked")
            Item item = (Item) itemPanel.getWidget(i);
            item.setEnabled(enabled);
        }
        
        if (enabled) noTB.setFocus(true);
    }

    private boolean validateInputs() {
        boolean isValid = true;

        if (docTypeLB.getValue() == null) {
            errDocTypeLb.setText(constants.invalidMsg());
            isValid = false;
        } else {
            errDocTypeLb.setText("");
        }
        
        if (noTB.getValue().isEmpty() || Utils.hasSpace(noTB.getValue())) {
            errNoLb.setText(constants.invalidMsg());
            isValid = false;
        } else {
            // Check duplicate journal no.
            if (presenter.isJournalNoDuplicate(keyString, docTypeLB.getValue(),
                    noTB.getValue())) {
                errNoLb.setText(constants.duplicateJournalNoMsg());
                isValid = false;
            } else {
                errNoLb.setText("");
            }
        }

        try {
            int day = dayIB.getCustomValue();

            if (Utils.compareDate(day, month, year, 1, beginMonth, beginYear) < 0
                    || Utils.compareDate(day, month, year,
                    Utils.getLastDay(endMonth, endYear), endMonth, endYear) > 0) {
                errDateLb.setText(constants.invalidMsg());
                isValid = false;
            } else {
                errDateLb.setText("");
            }
        } catch (NumberFormatException e) {
            errDateLb.setText(constants.invalidNumberMsg());
            isValid = false;
        } catch (InvalidValueException e) {
            errDateLb.setText(constants.invalidNumberMsg());
            isValid = false;
        }

        for (int i = 0; i < itemPanel.getWidgetCount(); i++) {
            @SuppressWarnings("unchecked")
            Item item = (Item) itemPanel.getWidget(i);
            if (!item.validateInputs()) {
                isValid = false;
            }
        }
        
        if (!validateTotal()) {
            isValid = false;
        }

        // Items are required
        if (isValid) {
            boolean doHaveItems = false;
            for (int i = 0; i < itemPanel.getWidgetCount(); i++) {
                @SuppressWarnings("unchecked")
                Item item = (Item) itemPanel.getWidget(i);
                if (item.getACKeyString() != null) {
                    doHaveItems = true;
                    break;
                }
            }
            if (!doHaveItems) {
                isValid = false;
                if (itemPanel.getWidgetCount() > 0) {
                    @SuppressWarnings("unchecked")
                    Item item = (Item) itemPanel.getWidget(0);
                    item.setErrText(constants.invalidMsg());
                } else {
                    Window.alert(constants.itemRequiredMsg());
                }
            }
        }
        return isValid;
    }

    private boolean validateTotal() {
        boolean isValid = true;
        double debit = 0.00;
        double credit = 0.00;
        
        try {
            for (int i = 0; i < itemPanel.getWidgetCount(); i++) {
                @SuppressWarnings("unchecked")
                Item item = (Item) itemPanel.getWidget(i);
                if (item.getACKeyString() != null) {
                    debit += item.getDebit();
                    credit += item.getCredit();
                }
            }
            
            String debitText = NumberFormat.getFormat("#,##0.00").format(debit);
            String creditText = NumberFormat.getFormat("#,##0.00").format(credit);
            
            totalDebitLb.setText(debitText);
            totalCreditLb.setText(creditText);
            
            if (debitText.equals(creditText)){
                errTotalLb.setText("");
            }else{
                errTotalLb.setText(constants.invalidMsg());
                isValid = false;
            }
        } catch (NumberFormatException e) {
            errTotalLb.setText(constants.invalidMsg());
            isValid = false;
        } catch (InvalidValueException e) {
            errTotalLb.setText(constants.invalidMsg());
            isValid = false;
        }
        return isValid;
    }
    
    private ArrayList<ItemData> genItemDataList() {
        ArrayList<ItemData> itemDataList = new ArrayList<ItemData>();
        double debit;
        double credit;
        double amt;
        for (int i = 0; i < itemPanel.getWidgetCount(); i++) {
            @SuppressWarnings("unchecked")
            Item item = (Item) itemPanel.getWidget(i);
            if (item.getACKeyString() != null) {
                debit = item.getDebit();
                credit = item.getCredit();

                amt = debit != 0 ? debit : credit * -1;
                ItemData itemData = new ItemData(item.getACKeyString(), amt, new Date());
                itemDataList.add(itemData);
            }
        }
        return itemDataList;
    }
    
    private ChangeHandler docTypeChangeHandler = new ChangeHandler(){
        @Override
        public void onChange(ChangeEvent event) {
            presenter.onDocTypeChanged();
        }
    };
    
    private ClickHandler suggestedNoBtnClickHandler = new ClickHandler(){
        @Override
        public void onClick(ClickEvent event) {
            noTB.setText(suggestedNoBtn.getText());
        }
    };
}
