package gwt.client.view.desktop;

import gwt.client.TCF;
import gwt.client.TConstants;
import gwt.client.def.FisDef;
import gwt.client.place.AllPlace;
import gwt.client.ui.CustomIntBox;
import gwt.client.ui.CustomListBox;
import gwt.client.ui.CustomSuggestBox;
import gwt.client.ui.CustomSuggestBox.SuggestBoxCallback;
import gwt.client.view.MenuView;
import gwt.shared.InvalidValueException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class MenuViewImpl<T> extends Composite implements MenuView<T> {

    enum State {
        FINANCE, JOURNAL, LEDGER
    }

    @SuppressWarnings("rawtypes")
    @UiTemplate("MenuViewImpl.ui.xml")
    interface MyUiBinder extends UiBinder<Widget, MenuViewImpl> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    interface Style extends CssResource {
        String btn();
        String suggestBox();
    }

    @UiField
    Style style;
    @UiField
    FlowPanel setupPanel;
    @UiField
    Button journalTypeBtn;
    @UiField
    Button docTypeBtn;
    @UiField
    Button accGrpBtn;
    @UiField
    Button accChartBtn;
    @UiField
    Button beginningBtn;
    @UiField
    Button finBtn;
    @UiField
    FlowPanel journalPanel;
    @UiField
    FlowPanel reportPanel;
    @UiField
    Button accChartRepBtn;
    @UiField
    Button journalRepBtn;
    @UiField
    Button ledgerRepBtn;
    @UiField
    Button trialRepBtn;
    @UiField
    FlowPanel finRepPanel;
    @UiField
    FlowPanel subMenuPanel;
    @UiField
    FlowPanel datePanel;
    @UiField
    Label journalTypeLb;
    @UiField
    CustomListBox journalTypeLB;
    @UiField
    Label beginAccNoLb;
    @UiField
    CustomSuggestBox beginAccNoSB;
    @UiField
    Label endAccNoLb;
    @UiField
    CustomSuggestBox endAccNoSB;
    @UiField
    Label errAccNoLb;
    @UiField
    Label beginDateLb;
    @UiField
    Label beginDayLb;
    @UiField
    CustomIntBox beginDayIB;
    @UiField
    Label beginMonthLb;
    @UiField
    CustomIntBox beginMonthIB;
    @UiField
    Label beginYearLb;
    @UiField
    CustomIntBox beginYearIB;
    @UiField
    Label errBeginDateLb;
    @UiField
    Label endDateLb;
    @UiField
    Label endDayLb;
    @UiField
    CustomIntBox endDayIB;
    @UiField
    Label endMonthLb;
    @UiField
    CustomIntBox endMonthIB;
    @UiField
    Label endYearLb;
    @UiField
    CustomIntBox endYearIB;
    @UiField
    Label errEndDateLb;
    @UiField
    Button okBtn;

    private static final TConstants constants = TCF.get();
    private Presenter presenter;
    private FisDef<T> fisDef;
    private State state;

    public MenuViewImpl(FisDef<T> fisDef) {
        this.fisDef = fisDef;
        initWidget(uiBinder.createAndBindUi(this));

        journalTypeBtn.setText(constants.setup() + constants.journalType());
        journalTypeBtn.addStyleName(style.btn());
        docTypeBtn.setText(constants.setup() + constants.docType());
        docTypeBtn.addStyleName(style.btn());
        accGrpBtn.setText(constants.setup() + constants.accGrp());
        accGrpBtn.addStyleName(style.btn());
        accChartBtn.setText(constants.setup() + constants.accChart());
        accChartBtn.addStyleName(style.btn());
        beginningBtn.setText(constants.setup() + constants.beginning());
        beginningBtn.addStyleName(style.btn());
        finBtn.setText(constants.setup() + constants.fin());
        finBtn.addStyleName(style.btn());
        
        accChartRepBtn.setText(constants.accChart());
        accChartRepBtn.addStyleName(style.btn());
        journalRepBtn.setText(constants.journalType());
        journalRepBtn.addStyleName(style.btn());
        ledgerRepBtn.setText(constants.ledger());
        ledgerRepBtn.addStyleName(style.btn());
        trialRepBtn.setText(constants.trial());
        trialRepBtn.addStyleName(style.btn());
        
        journalTypeLb.setText(constants.journalType());
        beginAccNoLb.setText(constants.accNo());
        beginAccNoSB.addTextBoxStyleName(style.suggestBox());
        endAccNoLb.setText(constants.end());
        endAccNoSB.addTextBoxStyleName(style.suggestBox());
        
        beginDateLb.setText(constants.begin());
        beginDayLb.setText(constants.day());
        beginMonthLb.setText(constants.month());
        beginYearLb.setText(constants.year());
        endDateLb.setText(constants.end());
        endDayLb.setText(constants.day());
        endMonthLb.setText(constants.month());
        endYearLb.setText(constants.year());
        
        okBtn.setText(constants.ok());
    }

    @Override
    public Widget asWidget() {
        return this;
    }

    @Override
    public void init(Presenter presenter) {
        this.presenter = presenter;

        setupPanel.setVisible(false);
        journalPanel.setVisible(false);
        reportPanel.setVisible(false);
        subMenuPanel.setVisible(false);

        journalTypeLB.clear();
        beginAccNoSB.clear();
        endAccNoSB.clear();

        beginAccNoSB.addSuggestBoxCallback(accNoSBcallback);
        endAccNoSB.addSuggestBoxCallback(accNoSBcallback);
    }

    @Override
    public void setMenu(T t, String action) {

        if (action.equals(AllPlace.SETUP)) {
            setupPanel.setVisible(true);
            journalPanel.setVisible(false);
            reportPanel.setVisible(false);
        } else if (action.equals(AllPlace.JOUR)) {
            journalPanel.clear();
            for (int i = 0; i < fisDef.getJTListSize(t); i++) {
                final String keyString = fisDef.getJTKeyString(t, i);
                Button btn = new Button(fisDef.getJTShortName(t, i));
                btn.addStyleName(style.btn());
                btn.addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        presenter.goToJournal(keyString);
                    }
                });
                journalPanel.add(btn);
            }

            setupPanel.setVisible(false);
            journalPanel.setVisible(true);
            reportPanel.setVisible(false);
        } else if (action.equals(AllPlace.REPORT)) {
            finRepPanel.clear();
            for (int i = 0; i < fisDef.getFinHeaderListSize(t); i++) {
                final String keyString = fisDef.getFinHeaderKeyString(t, i);
                Button btn = new Button(fisDef.getFinHeaderName(t, keyString));
                btn.addStyleName(style.btn());
                btn.addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        presenter.goToFinRep(keyString);
                    }
                });
                finRepPanel.add(btn);
            }
            
            for (int i = 0; i < fisDef.getJTListSize(t); i++) {
                journalTypeLB.addItem(fisDef.getJTShortName(t, i), fisDef.getJTKeyString(t, i));
            }

            for (int i = 0; i < fisDef.getACListSize(t); i++) {
                if (fisDef.getACIsEntry(t, i)) {
                    beginAccNoSB.add(fisDef.getACKeyString(t, i), fisDef.getACNo(t, i) + " - " + fisDef.getACName(t, i));
                    endAccNoSB.add(fisDef.getACKeyString(t, i), fisDef.getACNo(t, i) + " - " + fisDef.getACName(t, i));
                }
            }

            setupPanel.setVisible(false);
            journalPanel.setVisible(false);
            reportPanel.setVisible(true);
        } else {
            throw new AssertionError(action);
        }
    }

    @UiHandler("journalTypeBtn")
    void onJournalTypeBtnClicked(ClickEvent event) {
        presenter.goToJournalType();
    }

    @UiHandler("docTypeBtn")
    void onDocTypeBtnClicked(ClickEvent event) {
        presenter.goToDocType();
    }

    @UiHandler("accGrpBtn")
    void onAccGrpBtnClicked(ClickEvent event) {
        presenter.goToAccGrp();
    }

    @UiHandler("accChartBtn")
    void onAccChartBtnClicked(ClickEvent event) {
        presenter.goToAccChart();
    }

    @UiHandler("beginningBtn")
    void onBeginningBtnClicked(ClickEvent event) {
        presenter.goToBeginning();
    }

    @UiHandler("finBtn")
    void onFinanceBtnClicked(ClickEvent event) {
        presenter.goToFin();
    }

    @UiHandler("accChartRepBtn")
    void onAccChartRepBtnClicked(ClickEvent event) {
        presenter.goToAccChartRep();
    }

    @UiHandler("journalRepBtn")
    void onJournalRepBtnClicked(ClickEvent event) {
        journalTypeLb.setVisible(true);
        journalTypeLB.setVisible(true);
        beginAccNoLb.setVisible(false);
        beginAccNoSB.setVisible(false);
        endAccNoLb.setVisible(false);
        endAccNoSB.setVisible(false);
        datePanel.setVisible(true);
        subMenuPanel.setVisible(true);

        state = State.JOURNAL;
    }

    @UiHandler("ledgerRepBtn")
    void onLedgerRepBtnClicked(ClickEvent event) {
        journalTypeLb.setVisible(false);
        journalTypeLB.setVisible(false);
        beginAccNoLb.setVisible(true);
        beginAccNoSB.setVisible(true);
        endAccNoLb.setVisible(true);
        endAccNoSB.setVisible(true);
        datePanel.setVisible(true);
        subMenuPanel.setVisible(true);

        state = State.LEDGER;
    }

    @UiHandler("trialRepBtn")
    void onTrialRepBtnClicked(ClickEvent event) {
        presenter.goToTrialRep();
    }

    @UiHandler("okBtn")
    void onOkBtnClicked(ClickEvent event) {

        // Validate
        int beginDay;
        int beginMonth;
        int beginYear;
        int endDay;
        int endMonth;
        int endYear;

        try {
            beginDay = beginDayIB.getCustomValue();
            beginMonth = beginMonthIB.getCustomValue();
            beginYear = beginYearIB.getCustomValue();
            endDay = endDayIB.getCustomValue();
            endMonth = endMonthIB.getCustomValue();
            endYear = endYearIB.getCustomValue();
        } catch (NumberFormatException e) {
            Window.alert(e.getMessage());
            return;
        } catch (InvalidValueException e) {
            Window.alert(e.getMessage());
            return;
        }

        if (!(beginDay == 0 && beginMonth == 0 && beginYear == 0 && endDay == 0 && endMonth == 0 && endYear == 0)
                && !(beginDay != 0 && beginMonth != 0 && beginYear != 0 && endDay != 0 && endMonth != 0 && endYear != 0)) {
            // some empty some not not allowed
            Window.alert("some empty some not not allowed");
            return;
        }

        if (state.equals(State.JOURNAL)) {
            presenter.goToJournalRep(journalTypeLB.getValue(), beginDayIB.getCustomValue(), beginMonthIB.getCustomValue(),
                    beginYearIB.getCustomValue(), endDayIB.getCustomValue(), endMonthIB.getCustomValue(), endYearIB.getCustomValue());
        } else if (state.equals(State.LEDGER)) {
            String beginAccChartKeyString = beginAccNoSB.getKey();
            String endAccChartKeyString = endAccNoSB.getKey();

            if (beginAccChartKeyString == null && endAccChartKeyString == null) {
                if (!beginAccNoSB.getValue().isEmpty()) {
                    Window.alert("Begin Acc no is invalid");
                    return;
                }
                if (!endAccNoSB.getValue().isEmpty()) {
                    Window.alert("End Acc no is invalid");
                    return;
                }
                beginAccChartKeyString = AllPlace.FIRST;
                endAccChartKeyString = AllPlace.LAST;
            } else if (beginAccChartKeyString == null && endAccChartKeyString != null) {
                beginAccChartKeyString = AllPlace.FIRST;
            } else if (beginAccChartKeyString != null && endAccChartKeyString == null) {
                endAccChartKeyString = AllPlace.LAST;
            }

            presenter.goToLedgerRep(beginAccChartKeyString, endAccChartKeyString, beginDayIB.getCustomValue(), beginMonthIB.getCustomValue(),
                    beginYearIB.getCustomValue(), endDayIB.getCustomValue(), endMonthIB.getCustomValue(), endYearIB.getCustomValue());
        } else {
            throw new AssertionError(state);
        }

    }

    private SuggestBoxCallback accNoSBcallback = new SuggestBoxCallback() {
        @Override
        public void onInvalidInput(String input) {
            errAccNoLb.setText(input + " is incorrect.");
        }

        @Override
        public void onValidInput(String input) {
            errAccNoLb.setText("");
        }

        @Override
        public void onInCompleteInput(String value) {

        }

        @Override
        public void onCompleteInput(String key, String value) {

        }
    };
}
