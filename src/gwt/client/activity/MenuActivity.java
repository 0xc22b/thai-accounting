package gwt.client.activity;

import gwt.client.ClientFactory;
import gwt.client.TCF;
import gwt.client.TConstants;
import gwt.client.event.ActionEvent;
import gwt.client.event.ActionNames;
import gwt.client.place.AllPlace;
import gwt.client.view.MenuView;
import gwt.shared.model.SCom;
import gwt.shared.model.SFiscalYear;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.ResettableEventBus;

public class MenuActivity extends AbstractActivity implements MenuView.Presenter{

    private static final TConstants constants = TCF.get();
    
    private AllPlace place;
    private ClientFactory clientFactory;
    private ResettableEventBus eventBus;

    public MenuActivity(AllPlace place, ClientFactory clientFactory) {
        this.place = place;
        this.clientFactory = clientFactory;
        this.eventBus = new ResettableEventBus(clientFactory.getEventBus());
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus _eventBus) {
        clientFactory.getMenuView().init(this);
        panel.setWidget(clientFactory.getMenuView().asWidget());
        
        getSetup();
        
        Document.get().getBody().getStyle().setOverflowY(Style.Overflow.SCROLL);
    }

    @Override
    public String mayStop() {
        Document.get().getBody().getStyle().clearOverflowY();
        return null;
    }

    @Override
    public void onCancel() {
        
    }
    
    @Override
    public void onStop() {
        eventBus.removeHandlers();
    }
    
    @Override
    public void goToJournalType() {
        clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.JOURT,
                AllPlace.LIST, place.getComKeyString(), place.getFisKeyString()));
    }
    
    @Override
    public void goToDocType() {
        clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.DOC,
                AllPlace.LIST, place.getComKeyString(), place.getFisKeyString()));
    }
    
    @Override
    public void goToAccGrp() {
        clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.GRP,
                AllPlace.LIST, place.getComKeyString(), place.getFisKeyString()));
    }

    @Override
    public void goToAccChart() {
        clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.CHART,
                AllPlace.LIST, place.getComKeyString(), place.getFisKeyString()));
    }

    @Override
    public void goToBeginning() {
        clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.BEGIN,
                AllPlace.LIST, place.getComKeyString(), place.getFisKeyString()));
    }
    
    @Override
    public void goToJournal(String journalTypeKeyString, int month, int year) {
        clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.JOUR,
                AllPlace.LIST, place.getComKeyString(), place.getFisKeyString(),
                journalTypeKeyString, month + "", year + ""));
    }

    @Override
    public void goToAccChartRep() {
        clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.REPORT,
                AllPlace.CHART, place.getComKeyString(), place.getFisKeyString()));
    }

    @Override
    public void goToJournalRep(final String journalTypeKeyString, final int beginDay,
            final int beginMonth, final int beginYear, final int endDay, final int endMonth,
            final int endYear) {
        clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.REPORT,
                AllPlace.JOUR, place.getComKeyString(), place.getFisKeyString(),
                journalTypeKeyString, beginDay + "", beginMonth + "",
                beginYear + "", endDay + "", endMonth + "", endYear + ""));
    }

    @Override
    public void goToLedgerRep(final String beginACKeyString, final String endACKeyString,
            final int beginDay, final int beginMonth, final int beginYear, 
            final int endDay, final int endMonth, final int endYear, final boolean doShowAll) {
        String doShowAllString = doShowAll ? AllPlace.SHOW_ALL : null;
        clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.REPORT,
                AllPlace.LEDGER, place.getComKeyString(), place.getFisKeyString(),
                beginACKeyString, endACKeyString, beginDay + "", beginMonth + "",
                beginYear + "", endDay + "", endMonth + "", endYear + "",
                doShowAllString));
    }

    @Override
    public void goToTrialRep(boolean doShowAll) {
        String doShowAllString = doShowAll ? AllPlace.SHOW_ALL : null;
        clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.REPORT,
                AllPlace.TRIAL, place.getComKeyString(), place.getFisKeyString(),
                doShowAllString));
    }
    
    @Override
    public void goToBalanceRep(String assetACKeyString, String debtACKeyString,
            String shareholderACKeyString, String accruedProfitACKeyString,
            String incomeACKeyString, String expenseACKeyString, boolean doShowAll,
            boolean doesSplit) {
        String doShowAllString = doShowAll ? AllPlace.SHOW_ALL : null;
        String doesSplitString = doesSplit ? AllPlace.SPLIT : null;
        clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.REPORT,
                AllPlace.BALANCE, place.getComKeyString(), place.getFisKeyString(),
                assetACKeyString, debtACKeyString, shareholderACKeyString,
                accruedProfitACKeyString, incomeACKeyString, expenseACKeyString,
                doShowAllString, doesSplitString));
    }

    @Override
    public void goToProfitRep(String incomeACKeyString,
            String expenseACKeyString, boolean doShowAll, boolean doesSplit) {
        String doShowAllString = doShowAll ? AllPlace.SHOW_ALL : null;
        String doesSplitString = doesSplit ? AllPlace.SPLIT : null;
        clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.REPORT,
                AllPlace.PROFIT, place.getComKeyString(), place.getFisKeyString(),
                incomeACKeyString, expenseACKeyString, doShowAllString, doesSplitString));
    }

    @Override
    public void goToCostRep(String costACKeyString, boolean doShowAll) {
        String doShowAllString = doShowAll ? AllPlace.SHOW_ALL : null;
        clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.REPORT,
                AllPlace.COST, place.getComKeyString(), place.getFisKeyString(),
                costACKeyString, doShowAllString));
    }

    @Override
    public void recalAccAmt() {
        clientFactory.getModel().recalculateAccAmt(place.getComKeyString(), place.getFisKeyString(),
                new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
            }

            @Override
            public void onSuccess(String result) {
                clientFactory.getMenuView().setRecalAccAmtBtnText(result);
            }
        });
    }

    private void getSetup(){
        // 1. Waiting for getting data
        clientFactory.getShell().setLoading();
        
        // 2. Get data
        clientFactory.getModel().getSetup(place.getComKeyString(),
                place.getFisKeyString(), new AsyncCallback<SFiscalYear>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
            }

            @Override
            public void onSuccess(SFiscalYear result) {
                // 3. Set Shell and actBtns
                // 4. Add Shell handlers via EventBus
                initShell();
                
                // 5. Update view
                clientFactory.getMenuView().setMenu(result, place.getAction());
            }
        });
    }
    
    private void initShell(){
        
        clientFactory.getShell().reset();
        setShellHLb();
        clientFactory.getShell().setActBtn(0, constants.setup(), ActionNames.SETUP, true);
        clientFactory.getShell().setActBtn(1, constants.addJournals(), ActionNames.JOURNAL, true);
        clientFactory.getShell().setActBtn(2, constants.reports(), ActionNames.REPORT, true);
        clientFactory.getShell().setActBtn(3, constants.back(), ActionNames.BACK, true);
        
        ActionEvent.register(eventBus, ActionNames.SETUP, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.MENU,
                        AllPlace.SETUP, place.getComKeyString(), place.getFisKeyString()));
            }
        });
        
        ActionEvent.register(eventBus, ActionNames.JOURNAL, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.MENU,
                        AllPlace.JOUR, place.getComKeyString(), place.getFisKeyString()));
            }
        });
        
        ActionEvent.register(eventBus, ActionNames.REPORT, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.MENU,
                        AllPlace.REPORT, place.getComKeyString(), place.getFisKeyString()));
            }
        });
        
        ActionEvent.register(eventBus, ActionNames.BACK, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.FIS,
                        AllPlace.LIST, place.getComKeyString()));
            }
        });
        
    }
    
    private void setShellHLb(){
        clientFactory.getModel().getCom(place.getComKeyString(),
                new AsyncCallback<SCom>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
            }
            
            @Override
            public void onSuccess(SCom result) {
                final String comName = result.getName();
                clientFactory.getModel().getFis(place.getComKeyString(),
                        place.getFisKeyString(), new AsyncCallback<SFiscalYear>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert(caught.getMessage());
                    }
                    
                    @Override
                    public void onSuccess(SFiscalYear result) {
                        clientFactory.getShell().setHLb(comName + " "
                                + result.getBeginMonth() + "/" + result.getBeginYear()
                                + " - " + result.getEndMonth() + "/" + result.getEndYear());
                    }
                });
            }
        });
    }
}
