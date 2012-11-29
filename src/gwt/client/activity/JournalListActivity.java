package gwt.client.activity;

import gwt.client.ClientFactory;
import gwt.client.TCF;
import gwt.client.TConstants;
import gwt.client.event.ActionEvent;
import gwt.client.event.ActionNames;
import gwt.client.place.AllPlace;
import gwt.client.view.ListView;
import gwt.shared.model.SDocType;
import gwt.shared.model.SFiscalYear;
import gwt.shared.model.SJournalHeader;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.ResettableEventBus;

public class JournalListActivity extends AbstractActivity implements ListView.Presenter {

    private static final TConstants constants = TCF.get();
    
    private AllPlace place;
    private ClientFactory clientFactory;
    private ResettableEventBus eventBus;
    
    public JournalListActivity(AllPlace place, ClientFactory clientFactory) {
        this.place = place;
        this.clientFactory = clientFactory;
        this.eventBus = new ResettableEventBus(clientFactory.getEventBus());
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus _eventBus) {
        clientFactory.getJournalListView().init(this);
        panel.setWidget(clientFactory.getJournalListView().asWidget());
        
        getJournalList();
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
    
    @Override
    public void selectionChanged(boolean isSelected) {
        switchActBtns(!isSelected);
    }

    @Override
    public void itemClicked(String keyString) {
        //clientFactory.getPlaceController().goTo(new MenuPlace(MenuPlace.SETUP, place.getToken(), keyString));
    }
    
    private void getJournalList(){
        // 1. Waiting for getting data
        clientFactory.getShell().reset();
        clientFactory.getShell().setHLb(constants.loading());
        
        // 2. Get data
        clientFactory.getModel().getJournal(place.getComKeyString(), place.getFisKeyString(), null,
                new AsyncCallback<SFiscalYear>(){
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
            }
            @Override
            public void onSuccess(SFiscalYear result) {
                // 3. set Shell and actBtns
                // 4. add Shell handlers via EventBus
                initShell(result.getSJournalType(place.getKeyString()).getName());
                
                // 5. Filter journalType
                SFiscalYear sFis = filterJournalList(result);
                
                // 6. Update view
                clientFactory.getJournalListView().setData(sFis);
            }
        });
    }
    
    private void initShell(String journalTypeName){
     
        clientFactory.getShell().reset();
        clientFactory.getShell().setHLb(constants.addJournals() + ": " + journalTypeName);
        clientFactory.getShell().setActBtn(0, constants.createNew(), ActionNames.ADD, true);
        clientFactory.getShell().setActBtn(1, constants.view(), ActionNames.VIEW, false);
        clientFactory.getShell().setActBtn(2, constants.edit(), ActionNames.EDIT, false);
        clientFactory.getShell().setActBtn(3, constants.delete(), ActionNames.DELETE, false);
        clientFactory.getShell().setActBtn(4, constants.back(), ActionNames.BACK, true);
        
        
        ActionEvent.register(eventBus, ActionNames.ADD, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.JOUR, AllPlace.NEW, place.getComKeyString(), 
                        place.getFisKeyString(), place.getKeyString()));
            }
        });
        ActionEvent.register(eventBus, ActionNames.VIEW, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.JOUR, AllPlace.VIEW, place.getComKeyString(), 
                        place.getFisKeyString(), place.getKeyString(), clientFactory.getJournalListView().getSelectedItemKeyString()));
            }
        });
        ActionEvent.register(eventBus, ActionNames.EDIT, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.JOUR, AllPlace.EDIT, place.getComKeyString(),
                        place.getFisKeyString(), place.getKeyString(), clientFactory.getJournalListView().getSelectedItemKeyString()));
            }
        });
        ActionEvent.register(eventBus, ActionNames.DELETE, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                deleteJournal(place.getComKeyString(), place.getFisKeyString(), 
                        clientFactory.getJournalListView().getSelectedItemKeyString());
            }
        });
        ActionEvent.register(eventBus, ActionNames.BACK, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.MENU, AllPlace.JOUR, place.getComKeyString(), place.getFisKeyString()));
            }
        });
    }
    
    private void deleteJournal(String comKeyString, String fisKeyString, String journalKeyString){
        
        if(Window.confirm(constants.confirmDelete())){
            clientFactory.getModel().deleteJournal(comKeyString, fisKeyString, journalKeyString, new AsyncCallback<SFiscalYear>(){
                @Override
                public void onFailure(Throwable caught) {
                    Window.alert(caught.getMessage());
                }
                @Override
                public void onSuccess(SFiscalYear result) {
                    // Filter journalType
                    SFiscalYear sFis = filterJournalList(result);
                    
                    clientFactory.getJournalListView().setData(sFis);
                }
            });
        }
    }
    
    private SFiscalYear filterJournalList(SFiscalYear sFis){
        SFiscalYear filteredSFis = new SFiscalYear();
        for(SJournalHeader sJournal : sFis.getSJournalList()){
            SDocType sDocType = sFis.getSDocType(sJournal.getDocTypeKeyString());
            if(sDocType.getJournalTypeKeyString().equals(place.getKeyString())){
                filteredSFis.addSJournal(sJournal);
            }
        }
        return filteredSFis;
    }
    
    private void switchActBtns(boolean visible){
        clientFactory.getShell().setActBtnVisible(0, visible);
        clientFactory.getShell().setActBtnVisible(1, !visible);
        clientFactory.getShell().setActBtnVisible(2, !visible);
        clientFactory.getShell().setActBtnVisible(3, !visible);
        clientFactory.getShell().setActBtnVisible(4, visible);
    }
}
