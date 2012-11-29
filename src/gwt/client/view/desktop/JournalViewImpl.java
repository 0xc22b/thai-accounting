package gwt.client.view.desktop;

import java.util.ArrayList;
import java.util.Date;

import gwt.client.TCF;
import gwt.client.TConstants;
import gwt.client.def.FisDef;
import gwt.client.ui.CustomDoubleBox;
import gwt.client.ui.CustomDoubleBox.DoubleBoxCallback;
import gwt.client.ui.CustomIntBox;
import gwt.client.ui.CustomListBox;
import gwt.client.ui.CustomSuggestBox;
import gwt.client.view.JournalView;
import gwt.shared.InvalidValueException;
import gwt.shared.Utils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class JournalViewImpl<T> extends Composite implements JournalView<T> {

    private class Item extends Composite {

        FlowPanel panel;
        Label errLb;
        CustomSuggestBox accNoSB;
        CustomDoubleBox debitDB;
        CustomDoubleBox creditDB;

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

            initWidget(panel);
        }

        public Item(T t, String journalKeyString, int i) {
            this(t);

            accNoSB.setKey(fisDef.getJItemACKeyString(t, journalKeyString, i));
            double amt = fisDef.getJItemAmt(t, journalKeyString, i);
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
                errLb.setText(constants.invalid());
            }

            @Override
            public void onValidInput(String input) {
                if (validateInputs()) {
                    validateTotal();
                }
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
                            errLb.setText(constants.invalid());
                            isValid = false;
                        } else if (debit != 0 && credit != 0) {
                            errLb.setText(constants.invalid());
                            isValid = false;
                        } else {
                            errLb.setText("");
                        }
                    } catch (NumberFormatException e) {
                        errLb.setText(constants.invalid());
                        isValid = false;
                    } catch (InvalidValueException e) {
                        errLb.setText(constants.invalid());
                        isValid = false;
                    }
                }
            } catch (InvalidValueException e) {
                errLb.setText(constants.invalid());
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
    Label noLb;
    @UiField
    TextBox noTB;
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
    CustomIntBox monthIB;
    @UiField
    Label yearLb;
    @UiField
    CustomIntBox yearIB;
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
    private String keyString;
    private int beginMonth;
    private int beginYear;
    private int endMonth;
    private int endYear;

    public JournalViewImpl(FisDef<T> fisDef) {
        this.fisDef = fisDef;
        initWidget(uiBinder.createAndBindUi(this));

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
        monthIB.setRange(1, 12);
        
        docTypeLB.addChangeHandler(docTypeChangeHandler);
    }

    @Override
    public Widget asWidget() {
        return this;
    }

    @Override
    public void init(Presenter presenter) {
        this.presenter = presenter;

        keyString = null;
        beginMonth = 0;
        beginYear = 0;
        endMonth = 0;
        endYear = 0;

        docTypeLB.clear();
        journalTypeLb.setText("");
        journalTypeInputLb.setText("");
        noTB.setText("");
        errNoLb.setText("");
        dayIB.clear();
        monthIB.clear();
        yearIB.clear();
        errDateLb.setText("");
        descTB.setText("");
        errDescLb.setText("");
        itemPanel.clear();
        totalCreditLb.setText(NumberFormat.getFormat("#,##0.00").format(0));
        totalDebitLb.setText(NumberFormat.getFormat("#,##0.00").format(0));
    }

    @Override
    public void setJournal(T t, String journalTypeKeyString, String keyString, boolean editable) {

        beginMonth = fisDef.getFBeginMonth(t);
        beginYear = fisDef.getFBeginYear(t);
        endMonth = fisDef.getFEndMonth(t);
        endYear = fisDef.getFEndYear(t);

        journalTypeLb.setText(constants.journalType() + ":");
        journalTypeInputLb.setText(fisDef.getJTShortName(t, journalTypeKeyString));

        for (int i = 0; i < fisDef.getDTListSize(t); i++) {
            if (fisDef.getDTJTKeyString(t, i).equals(journalTypeKeyString)) {
                docTypeLB.addItem(fisDef.getDTCode(t, i), fisDef.getDTKeyString(t, i));
            }
        }
        
        // change docTypeLB -> update descTB
        descTB.setText(fisDef.getDTJournalDesc(t, docTypeLB.getValue()));

        yearIB.setRange(fisDef.getFBeginYear(t), fisDef.getFEndYear(t));

        if (keyString == null) {
            itemPanel.add(new Item(t));
            itemPanel.add(new Item(t));
            itemPanel.add(new Item(t));
        } else {
            this.keyString = keyString;

            noTB.setText(fisDef.getJNo(t, keyString));
            dayIB.setCustomValue(fisDef.getJDay(t, keyString));
            monthIB.setCustomValue(fisDef.getJMonth(t, keyString));
            yearIB.setCustomValue(fisDef.getJYear(t, keyString));
            descTB.setText(fisDef.getJDesc(t, keyString));

            //Items
            for (int i = 0; i < fisDef.getJItemListSize(t, keyString); i++) {
                Item journalItemPanel = new Item(t, keyString, i);
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
        presenter.addJournal(docTypeLB.getValue(), noTB.getValue(), dayIB.getCustomValue(), monthIB.getCustomValue(), yearIB.getCustomValue(),
                descTB.getValue(), itemDataList);
    }

    @Override
    public void editJournalBtnClicked() {
        if (!validateInputs()) {
            return;
        }

        ArrayList<ItemData> itemDataList = genItemDataList();
        presenter.editJournal(keyString, docTypeLB.getValue(), noTB.getValue(), dayIB.getCustomValue(), monthIB.getCustomValue(),
                yearIB.getCustomValue(), descTB.getValue(), itemDataList);
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
        monthIB.setEnabled(enabled);
        yearIB.setEnabled(enabled);
        descTB.setEnabled(enabled);

        for (int i = 0; i < itemPanel.getWidgetCount(); i++) {
            @SuppressWarnings("unchecked")
            Item item = (Item) itemPanel.getWidget(i);
            item.setEnabled(enabled);
        }
    }

    private boolean validateInputs() {
        boolean isValid = true;

        if (noTB.getValue().isEmpty()) {
            errNoLb.setText(constants.invalid());
            isValid = false;
        } else {
            errNoLb.setText("");
        }

        try {
            int day = dayIB.getCustomValue();
            int month = monthIB.getCustomValue();
            int year = yearIB.getCustomValue();

            if (Utils.compareDate(day, month, year, 1, beginMonth, beginYear) < 0
                    || Utils.compareDate(day, month, year, Utils.getLastDay(endMonth, endYear), endMonth, endYear) > 0) {
                errDateLb.setText(constants.invalid());
                isValid = false;
            } else {
                errDateLb.setText("");
            }
        } catch (NumberFormatException e) {
            errDateLb.setText(constants.invalid());
            isValid = false;
        } catch (InvalidValueException e) {
            errDateLb.setText(constants.invalid());
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
                errTotalLb.setText(constants.invalid());
                isValid = false;
            }
        } catch (NumberFormatException e) {
            errTotalLb.setText(constants.invalid());
            isValid = false;
        } catch (InvalidValueException e) {
            errTotalLb.setText(constants.invalid());
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
}
