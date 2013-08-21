package gwt.client.activity;

import gwt.client.ClientFactory;
import gwt.client.TCF;
import gwt.client.TConstants;
import gwt.client.event.ActionEvent;
import gwt.client.event.ActionNames;
import gwt.client.place.AllPlace;
import gwt.client.view.ReportView;
import gwt.shared.model.SCom;
import gwt.shared.model.SFiscalYear;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
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
    }
    
    private void getData(){
        // 1. Waiting for getting data
        clientFactory.getShell().setLoading();
        
        // 2. Get data
        clientFactory.getModel().getCom(place.getComKeyString(),
                new AsyncCallback<SCom>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
            }

            @Override
            public void onSuccess(final SCom sCom) {
                clientFactory.getModel().getJournal(place.getComKeyString(),
                        place.getFisKeyString(), null, new AsyncCallback<SFiscalYear>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert(caught.getMessage());
                    }

                    @Override
                    public void onSuccess(SFiscalYear sFis) {
                        // 3. set Shell and actBtns
                        // 4. add Shell handlers via EventBus
                        initShell();
                        
                        // 5. Update view
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
    
    private void initView(SFiscalYear sFis, String comName){
        // case action
        // some cases ask period!
        String action = place.getAction();
        
        if (action.equals(AllPlace.CHART)) {
            clientFactory.getReportView().setChartData(sFis, comName);
        } else if (action.equals(AllPlace.JOUR)) {
            int[] date = extractJournalDate();
            clientFactory.getReportView().setJourData(sFis, comName,
                    place.getKeyString(), date[0], date[1], date[2], date[3],
                    date[4], date[5]);
        } else if (action.equals(AllPlace.LEDGER)) {
            int[] date = extractLedgerDate();
            boolean doShowAll = place.getKeyString9().equals(AllPlace.SHOW_ALL);
            clientFactory.getReportView().setLedgerData(sFis, comName,
                    place.getKeyString(), place.getKeyString2(), date[0],
                    date[1], date[2], date[3], date[4], date[5], doShowAll);
        } else if (action.equals(AllPlace.TRIAL)){
            boolean doShowAll = place.getKeyString().equals(AllPlace.SHOW_ALL);
            clientFactory.getReportView().setTrialData(sFis, comName,
                    doShowAll);
        } else if (action.equals(AllPlace.BALANCE)) {
            boolean doShowAll = place.getKeyString7().equals(AllPlace.SHOW_ALL);
            clientFactory.getReportView().setBalanceData(sFis, comName,
                    place.getKeyString(), place.getKeyString2(),
                    place.getKeyString3(), place.getKeyString4(),
                    place.getKeyString5(), place.getKeyString6(), doShowAll);
        } else if (action.equals(AllPlace.PROFIT)) {
            boolean doShowAll = place.getKeyString3().equals(AllPlace.SHOW_ALL);
            clientFactory.getReportView().setProfitData(sFis, comName,
                    place.getKeyString(), place.getKeyString2(), doShowAll);
        } else if (action.equals(AllPlace.COST)) {
            boolean doShowAll = place.getKeyString2().equals(AllPlace.SHOW_ALL);
            clientFactory.getReportView().setCostData(sFis, comName,
                    place.getKeyString(), doShowAll);
        } else if (action.equals(AllPlace.WORK_SHEET)) {
            
        } else if (action.equals(AllPlace.FIN)) {
            clientFactory.getReportView().setFinData(sFis, comName,
                    place.getKeyString());
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
