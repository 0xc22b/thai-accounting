package gwt.client.activity;

import gwt.client.ClientFactory;
import gwt.client.TCF;
import gwt.client.TConstants;
import gwt.client.event.ActionEvent;
import gwt.client.event.ActionNames;
import gwt.client.place.AllPlace;
import gwt.client.view.PdfView;
import gwt.client.view.ReportView;
import gwt.shared.SConstants;
import gwt.shared.Utils;
import gwt.shared.model.SAccAmt;
import gwt.shared.model.SAccChart;
import gwt.shared.model.SCom;
import gwt.shared.model.SFiscalYear;
import gwt.shared.model.SAccChart.AccType;

import java.util.HashMap;
import java.util.List;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.ResettableEventBus;

public class ReportActivity extends AbstractActivity
        implements ReportView.Presenter, PdfView.Presenter {

    private static final TConstants constants = TCF.get();

    private AllPlace place;
    private ClientFactory clientFactory;
    private ResettableEventBus eventBus;

    public ReportActivity(AllPlace place, ClientFactory clientFactory) {
        this.place = place;
        this.clientFactory = clientFactory;
        this.eventBus = new ResettableEventBus(clientFactory.getEventBus());
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus _eventBus) {
        String action = place.getAction();
        
        if (action.equals(AllPlace.JOUR) || action.equals(AllPlace.LEDGER)) {
            clientFactory.getPdfView().init(this);
            panel.setWidget(clientFactory.getPdfView().asWidget());
        } else {
            clientFactory.getReportView().init(this);
            panel.setWidget(clientFactory.getReportView().asWidget());
        }

        getData();
    }

    @Override
    public String mayStop() {
        return null;
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onStop() {
        eventBus.removeHandlers();
    }

    private void getData(){
        // Waiting for getting data
        clientFactory.getShell().setLoading();

        // Get data
        clientFactory.getModel().getCom(place.getComKeyString(),
                new AsyncCallback<SCom>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
            }

            @Override
            public void onSuccess(final SCom sCom) {
                clientFactory.getModel().getAccChart(place.getComKeyString(),
                        place.getFisKeyString(), null, new AsyncCallback<SFiscalYear>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert(caught.getMessage());
                    }

                    @Override
                    public void onSuccess(SFiscalYear sFis) {
                        // Update view
                        initView(sFis, sCom.getName());
                    }
                });
            }
        });
    }

    private void initShell(){

        clientFactory.getShell().reset();
        clientFactory.getShell().setHLb(constants.reports());
        clientFactory.getShell().setActBtn(0, constants.print(), ActionNames.PRINT, true);
        clientFactory.getShell().setActBtn(1, constants.back(), ActionNames.BACK, true);


        ActionEvent.register(eventBus, ActionNames.PRINT, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                String action = place.getAction();
                if (action.equals(AllPlace.JOUR) || action.equals(AllPlace.LEDGER)) {
                    clientFactory.getPdfView().onPrintBtnClicked();
                } else {
                    clientFactory.getReportView().onPrintBtnClicked();
                }
            }
        });
        ActionEvent.register(eventBus, ActionNames.BACK, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.MENU,
                        AllPlace.REPORT, place.getComKeyString(),
                        place.getFisKeyString()));
            }
        });

    }

    private void initView(final SFiscalYear sFis, final String comName){

        String action = place.getAction();

        if (action.equals(AllPlace.CHART)) {
            // Let view updates itself first
            new Timer() {
                @Override
                public void run() {

                    // set Shell and actBtns
                    // add Shell handlers via EventBus
                    initShell();

                    clientFactory.getReportView().setChartData(sFis, comName);
                }
            }.schedule(100);
        } else if (action.equals(AllPlace.JOUR)){
            goToJournalRepUrl(comName, sFis, place.getKeyString(),
                    Integer.parseInt(place.getKeyString2()),
                    Integer.parseInt(place.getKeyString3()),
                    Integer.parseInt(place.getKeyString4()),
                    Integer.parseInt(place.getKeyString5()),
                    Integer.parseInt(place.getKeyString6()),
                    Integer.parseInt(place.getKeyString7()));
        } else if (action.equals(AllPlace.LEDGER)){
            boolean doShowAll = place.getKeyString().equals(AllPlace.SHOW_ALL);
            goToLedgerRepUrl(comName, sFis, place.getKeyString(), place.getKeyString2(),
                    Integer.parseInt(place.getKeyString3()),
                    Integer.parseInt(place.getKeyString4()),
                    Integer.parseInt(place.getKeyString5()),
                    Integer.parseInt(place.getKeyString6()),
                    Integer.parseInt(place.getKeyString7()),
                    Integer.parseInt(place.getKeyString8()),
                    doShowAll);
        } else if (action.equals(AllPlace.TRIAL)){

            clientFactory.getModel().getAccAmtMap(place.getComKeyString(),
                    place.getFisKeyString(), new AsyncCallback<HashMap<String, SAccAmt>>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            Window.alert(caught.getMessage());
                        }

                        @Override
                        public void onSuccess(HashMap<String, SAccAmt> result) {

                            // set Shell and actBtns
                            // add Shell handlers via EventBus
                            initShell();

                            boolean doShowAll = place.getKeyString().equals(AllPlace.SHOW_ALL);

                            clientFactory.getReportView().setTrialData(sFis, result, comName,
                                    doShowAll);
                        }
                    });
        } else if (action.equals(AllPlace.BALANCE)) {

            clientFactory.getModel().getAccAmtMap(place.getComKeyString(),
                    place.getFisKeyString(), new AsyncCallback<HashMap<String, SAccAmt>>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            Window.alert(caught.getMessage());
                        }

                        @Override
                        public void onSuccess(HashMap<String, SAccAmt> result) {

                            // set Shell and actBtns
                            // add Shell handlers via EventBus
                            initShell();

                            boolean doShowAll = place.getKeyString7().equals(AllPlace.SHOW_ALL);
                            boolean doesSplit = place.getKeyString8().equals(AllPlace.SPLIT);
                            clientFactory.getReportView().setBalanceData(sFis, result, comName,
                                    place.getKeyString(), place.getKeyString2(),
                                    place.getKeyString3(), place.getKeyString4(),
                                    place.getKeyString5(), place.getKeyString6(), doShowAll,
                                    doesSplit);
                        }
                    });
        } else if (action.equals(AllPlace.PROFIT)) {

            clientFactory.getModel().getAccAmtMap(place.getComKeyString(),
                    place.getFisKeyString(), new AsyncCallback<HashMap<String, SAccAmt>>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            Window.alert(caught.getMessage());
                        }

                        @Override
                        public void onSuccess(HashMap<String, SAccAmt> result) {

                            // set Shell and actBtns
                            // add Shell handlers via EventBus
                            initShell();

                            boolean doShowAll = place.getKeyString3().equals(AllPlace.SHOW_ALL);
                            boolean doesSplit = place.getKeyString4().equals(AllPlace.SPLIT);
                            clientFactory.getReportView().setProfitData(sFis, result, comName,
                                    place.getKeyString(), place.getKeyString2(), doShowAll,
                                    doesSplit);
                        }
                    });
        } else if (action.equals(AllPlace.COST)) {

            clientFactory.getModel().getAccAmtMap(place.getComKeyString(),
                    place.getFisKeyString(), new AsyncCallback<HashMap<String, SAccAmt>>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            Window.alert(caught.getMessage());
                        }

                        @Override
                        public void onSuccess(HashMap<String, SAccAmt> result) {

                            // set Shell and actBtns
                            // add Shell handlers via EventBus
                            initShell();

                            boolean doShowAll = place.getKeyString2().equals(AllPlace.SHOW_ALL);
                            clientFactory.getReportView().setCostData(sFis, result, comName,
                                    place.getKeyString(), doShowAll);
                        }
                    });
        } else {
            throw new AssertionError(action);
        }
    }
    
    private void goToJournalRepUrl(String comName, SFiscalYear sFis, String journalTypeKeyString,
            int beginDay, int beginMonth, int beginYear, int endDay, int endMonth, int endYear) {

        if(beginDay == 0 && beginMonth == 0 && beginYear == 0 && endDay == 0
                && endMonth == 0 && endYear == 0){
            beginDay = 1;
            beginMonth = sFis.getBeginMonth();
            beginYear = sFis.getBeginYear();
            endDay = Utils.getLastDay(sFis.getEndMonth(), sFis.getEndYear());
            endMonth = sFis.getEndMonth();
            endYear = sFis.getEndYear();
        }else if(beginDay != 0 && beginMonth != 0 && beginYear != 0 && endDay != 0
                && endMonth != 0 && endYear != 0 ){
            // No need to do anything
        }else{
            throw new AssertionError();
        }

        final String fisKeyString = place.getFisKeyString();

        final String journalTypeName = sFis.getSJournalType(journalTypeKeyString).getName();

        String url = Window.Location.createUrlBuilder().setPath(SConstants.PDF_PATH)
                .setParameter(SConstants.ACTION, SConstants.JOURNAL_ACTION)
                .setParameter(SConstants.FIS_KEY_STRING, fisKeyString)
                .setParameter(SConstants.JOURNAL_TYPE_KEY_STRING, journalTypeKeyString)
                .setParameter(SConstants.BEGIN_DAY, beginDay + "")
                .setParameter(SConstants.BEGIN_MONTH, beginMonth + "")
                .setParameter(SConstants.BEGIN_YEAR, beginYear + "")
                .setParameter(SConstants.END_DAY, endDay + "")
                .setParameter(SConstants.END_MONTH, endMonth + "")
                .setParameter(SConstants.END_YEAR, endYear + "")
                .setParameter(SConstants.COM_NAME, comName)
                .setParameter(SConstants.JOURNAL_TYPE_NAME, journalTypeName)
                .setParameter(SConstants.LANG, LocaleInfo.getCurrentLocale().getLocaleName())
                .buildString();

        clientFactory.getPdfView().setData(url);
        clientFactory.getPdfView().addReadyStateChangeHandler(
                new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                // set Shell and actBtns
                // add Shell handlers via EventBus
                initShell();
            }
        });
    }
    
    private void goToLedgerRepUrl(String comName, SFiscalYear sFis, String beginACKeyString,
            String endACKeyString, int beginDay, int beginMonth, int beginYear, 
            int endDay, int endMonth, int endYear, boolean doShowAll) {
        
        String fisKeyString = place.getFisKeyString();

        // Assume that Acc chart is sorted
        List<SAccChart> sAccChartList = sFis.getSAccChartList();

        if (beginACKeyString.equals(AllPlace.FIRST)) {
            for (SAccChart sAccChart : sAccChartList) {
                if (sAccChart.getType() == AccType.ENTRY) {
                    beginACKeyString = sAccChart.getKeyString();
                    break;
                }
            }
        }

        if (endACKeyString.equals(AllPlace.LAST)) {
            for (int i = sAccChartList.size() - 1; i >= 0; i--) {
                SAccChart sAccChart = sAccChartList.get(i);
                if (sAccChart.getType() == AccType.ENTRY) {
                    endACKeyString = sAccChart.getKeyString();
                    break;
                }
            }
        }

        final String beginACNo = sFis.getSAccChart(beginACKeyString).getNo();
        final String endACNo = sFis.getSAccChart(endACKeyString).getNo();

        if(beginDay == 0 && beginMonth == 0 && beginYear == 0 && endDay == 0
                && endMonth == 0 && endYear == 0){
            beginDay = 1;
            beginMonth = sFis.getBeginMonth();
            beginYear = sFis.getBeginYear();
            endDay = Utils.getLastDay(sFis.getEndMonth(), sFis.getEndYear());
            endMonth = sFis.getEndMonth();
            endYear = sFis.getEndYear();
        }else if(beginDay != 0 && beginMonth != 0 && beginYear != 0 && endDay != 0
                && endMonth != 0 && endYear != 0){
            // Do nothing thing
        }else{
            throw new AssertionError();
        }

        String url = Window.Location.createUrlBuilder().setPath(SConstants.PDF_PATH)
                .setParameter(SConstants.ACTION, SConstants.LEDGER_ACTION)
                .setParameter(SConstants.FIS_KEY_STRING, fisKeyString)
                .setParameter(SConstants.BEGIN_ACC_NO, beginACNo)
                .setParameter(SConstants.END_ACC_NO, endACNo)
                .setParameter(SConstants.BEGIN_DAY, beginDay + "")
                .setParameter(SConstants.BEGIN_MONTH, beginMonth + "")
                .setParameter(SConstants.BEGIN_YEAR, beginYear + "")
                .setParameter(SConstants.END_DAY, endDay + "")
                .setParameter(SConstants.END_MONTH, endMonth + "")
                .setParameter(SConstants.END_YEAR, endYear + "")
                .setParameter(SConstants.DO_SHOW_ALL, doShowAll ? SConstants.DO_SHOW_ALL : "n")
                .setParameter(SConstants.COM_NAME, comName)
                .setParameter(SConstants.LANG, LocaleInfo.getCurrentLocale().getLocaleName())
                .buildString();

        clientFactory.getPdfView().setData(url);
        clientFactory.getPdfView().addReadyStateChangeHandler(
                new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                // set Shell and actBtns
                // add Shell handlers via EventBus
                initShell();
            }
        });
    }
}
