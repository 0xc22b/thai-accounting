package gwt.client.activity;

import gwt.client.ClientFactory;
import gwt.client.TCF;
import gwt.client.TConstants;
import gwt.client.event.ActionEvent;
import gwt.client.event.ActionNames;
import gwt.client.place.AllPlace;
import gwt.client.view.ReportView;
import gwt.shared.Utils;
import gwt.shared.model.SAccAmt;
import gwt.shared.model.SAccChart;
import gwt.shared.model.SAccChart.AccType;
import gwt.shared.model.SCom;
import gwt.shared.model.SFiscalYear;

import java.util.HashMap;
import java.util.List;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.ResettableEventBus;

public class ReportActivity extends AbstractActivity implements ReportView.Presenter {

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
        clientFactory.getReportView().init(this);
        panel.setWidget(clientFactory.getReportView().asWidget());

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
        clientFactory.getReportView().clearBodyInnerHTML();
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
                clientFactory.getReportView().onPrintBtnClicked();
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
        } else if (action.equals(AllPlace.JOUR)) {

            final String comKeyString = place.getComKeyString();
            final String fisKeyString = place.getFisKeyString();
            final String journalTypeKeyString = place.getKeyString();

            final String journalTypeName = sFis.getSJournalType(journalTypeKeyString).getName();

            final int[] dates = extractJournalDate();
            if (dates[0] == 0) {
                dates[0] = 1;
                dates[1] = sFis.getBeginMonth();
                dates[2] = sFis.getBeginYear();
                dates[3] = Utils.getLastDay(sFis.getEndMonth(), sFis.getEndYear());
                dates[4] = sFis.getEndMonth();
                dates[5] = sFis.getEndYear();
            }

            clientFactory.getModel().getJournalBodyHtml(comKeyString, fisKeyString,
                    journalTypeKeyString, dates, constants.total(), constants.wholeTotal(),
                    new AsyncCallback<String>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            Window.alert(caught.getMessage());
                        }

                        @Override
                        public void onSuccess(String result) {

                            // set Shell and actBtns
                            // add Shell handlers via EventBus
                            initShell();

                            clientFactory.getReportView().setJourData(sFis, dates, comName,
                                    journalTypeName, result);
                        }
                    });
        } else if (action.equals(AllPlace.LEDGER)) {

            String comKeyString = place.getComKeyString();
            String fisKeyString = place.getFisKeyString();

            String beginACKeyString = place.getKeyString();
            String endACKeyString = place.getKeyString2();

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

            // Begin and end dates
            final int[] dates = extractLedgerDate();
            if (dates[0] == 0) {
                dates[0] = 1;
                dates[1] = sFis.getBeginMonth();
                dates[2] = sFis.getBeginYear();
                dates[3] = Utils.getLastDay(sFis.getEndMonth(), sFis.getEndYear());
                dates[4] = sFis.getEndMonth();
                dates[5] = sFis.getEndYear();
            }

            final boolean doShowAll = place.getKeyString9().equals(AllPlace.SHOW_ALL);

            clientFactory.getModel().getLedgerBodyHtml(comKeyString, fisKeyString, beginACNo,
                    endACNo, dates, doShowAll, constants.total(), constants.wholeTotal(),
                    new AsyncCallback<String>() {

                @Override
                public void onFailure(Throwable caught) {
                    Window.alert(caught.getMessage());
                }

                @Override
                public void onSuccess(String result) {

                    // set Shell and actBtns
                    // add Shell handlers via EventBus
                    initShell();

                    clientFactory.getReportView().setLedgerData(sFis, dates, comName, beginACNo,
                            endACNo, result);
                }
            });

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

    private int[] extractJournalDate(){
        int[] date = new int[6];
        if(place.getKeyString2() != null){
            date[0] = Integer.parseInt(place.getKeyString2());
            date[1] = Integer.parseInt(place.getKeyString3());
            date[2] = Integer.parseInt(place.getKeyString4());
            date[3] = Integer.parseInt(place.getKeyString5());
            date[4] = Integer.parseInt(place.getKeyString6());
            date[5] = Integer.parseInt(place.getKeyString7());
        }
        return date;
    }

    private int[] extractLedgerDate(){
        int[] date = new int[6];
        date[0] = Integer.parseInt(place.getKeyString3());
        date[1] = Integer.parseInt(place.getKeyString4());
        date[2] = Integer.parseInt(place.getKeyString5());
        date[3] = Integer.parseInt(place.getKeyString6());
        date[4] = Integer.parseInt(place.getKeyString7());
        date[5] = Integer.parseInt(place.getKeyString8());
        return date;
    }
}
