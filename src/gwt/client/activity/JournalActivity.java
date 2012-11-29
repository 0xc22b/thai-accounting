package gwt.client.activity;

import java.util.ArrayList;

import gwt.client.ClientFactory;
import gwt.client.TCF;
import gwt.client.TConstants;
import gwt.client.event.ActionEvent;
import gwt.client.event.ActionNames;
import gwt.client.place.AllPlace;
import gwt.client.view.JournalView;
import gwt.client.view.JournalView.ItemData;
import gwt.shared.model.SFiscalYear;
import gwt.shared.model.SJournalHeader;
import gwt.shared.model.SJournalItem;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.ResettableEventBus;

public class JournalActivity extends AbstractActivity implements JournalView.Presenter {

    private static final TConstants constants = TCF.get();
    
    private AllPlace allPlace;
    private ClientFactory clientFactory;
    private ResettableEventBus eventBus;
    
    public JournalActivity(AllPlace allPlace, ClientFactory clientFactory) {
        this.allPlace = allPlace;
        this.clientFactory = clientFactory;
        this.eventBus = new ResettableEventBus(clientFactory.getEventBus());
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus _eventBus) {
        clientFactory.getJournalView().init(this);
        panel.setWidget(clientFactory.getJournalView().asWidget());
        
        processToken();
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
    public void addJournal(String docTypeKeyString, String no, int day,
            int month, int year, String desc, ArrayList<ItemData> itemDataList) {
        
        SJournalHeader sJournal = genSJournal(null, docTypeKeyString, no, day,
                month, year, desc, itemDataList);
        
        clientFactory.getModel().addJournal(allPlace.getComKeyString(),
                allPlace.getFisKeyString(), sJournal, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
            }
            @Override
            public void onSuccess(String result) {
                clientFactory.getPlaceController().goTo(new AllPlace(
                        AllPlace.JOUR, AllPlace.LIST, allPlace.getComKeyString(), 
                        allPlace.getFisKeyString(), allPlace.getKeyString()));
            }
        });
    }

    @Override
    public void editJournal(String keyString, String docTypeKeyString, String no,
            int day, int month, int year, String desc, 
            ArrayList<ItemData> itemDataList) {
        
        SJournalHeader sJournal = genSJournal(keyString, docTypeKeyString, no,
                day, month, year, desc, itemDataList);
        
        clientFactory.getModel().editJournal(allPlace.getComKeyString(),
                allPlace.getFisKeyString(), sJournal, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
            }
            @Override
            public void onSuccess(String result) {
                clientFactory.getPlaceController().goTo(new AllPlace(
                        AllPlace.JOUR, AllPlace.LIST, allPlace.getComKeyString(), 
                        allPlace.getFisKeyString(), allPlace.getKeyString()));
            }
        });
    }
    
    @Override
    public void onDocTypeChanged() {
        clientFactory.getModel().getJournal(allPlace.getComKeyString(),
                allPlace.getFisKeyString(), null, new AsyncCallback<SFiscalYear>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
            }
            @Override
            public void onSuccess(SFiscalYear result) {
                clientFactory.getJournalView().updateJournalDesc(result);
            }
        });
    }
    
    private void processToken(){
        if(!allPlace.getAction().isEmpty()){
            if(allPlace.getAction().equals(AllPlace.NEW)){
                setAddJournal(allPlace.getComKeyString(), allPlace.getFisKeyString(),
                        allPlace.getKeyString());
                return;
            }else if(allPlace.getAction().equals(AllPlace.EDIT)){
                setEditJournal(allPlace.getComKeyString(), allPlace.getFisKeyString(),
                        allPlace.getKeyString(), 
                        allPlace.getKeyString2());
                return;
            }else if(allPlace.getAction().equals(AllPlace.VIEW)){
                setViewJournal(allPlace.getComKeyString(), allPlace.getFisKeyString(),
                        allPlace.getKeyString(), 
                        allPlace.getKeyString2());
                return;
            }
        }
        clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.JOUR, 
                AllPlace.LIST, allPlace.getComKeyString(), allPlace.getFisKeyString(),
                allPlace.getKeyString()));
    }
    
    private void setAddJournal(final String comKeyString, final String fisKeyString,
            final String journalTypeKeyString){
        
        assert(comKeyString != null && fisKeyString != null && journalTypeKeyString != null);
        
        // 1. set Shell
        clientFactory.getShell().reset();
        clientFactory.getShell().setHLb(constants.addJournals() + ": " + constants.createNew());
        clientFactory.getShell().setActBtn(0, constants.save(), ActionNames.OK, true);
        clientFactory.getShell().setActBtn(1, constants.cancel(), ActionNames.CANCEL, true);
        clientFactory.getShell().setActBtn(2, constants.addNewItem(), ActionNames.ITEM, true);
        
        // 2. add Shell handlers via EventBus
        ActionEvent.register(eventBus, ActionNames.OK, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getJournalView().addJournalBtnClicked();
            }
        });
        
        ActionEvent.register(eventBus, ActionNames.CANCEL, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.JOUR,
                        AllPlace.LIST, comKeyString, fisKeyString, journalTypeKeyString));
            }
        });
        
        ActionEvent.register(eventBus, ActionNames.ITEM, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                addItemBtnClicked();
            }
        });
        
        // 3. Update view
        clientFactory.getModel().getJournal(comKeyString, fisKeyString, null,
                new AsyncCallback<SFiscalYear>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.JOUR,
                        AllPlace.LIST, comKeyString, fisKeyString, journalTypeKeyString));
            }
            @Override
            public void onSuccess(SFiscalYear result) {
                clientFactory.getJournalView().setJournal(result,
                        journalTypeKeyString, null, true);
            }
        });
    }
    
    private void setEditJournal(final String comKeyString, final String fisKeyString,
            final String journalTypeKeyString, final String journalKeyString){
        
        assert(comKeyString != null && fisKeyString != null
                && journalTypeKeyString != null && journalKeyString != null);
        
        // 1. set Shell
        clientFactory.getShell().reset();
        clientFactory.getShell().setHLb(constants.addJournals() + ": " + constants.edit());
        clientFactory.getShell().setActBtn(0, constants.save(), ActionNames.OK, true);
        clientFactory.getShell().setActBtn(1, constants.cancel(), ActionNames.CANCEL, true);
        clientFactory.getShell().setActBtn(2, constants.addNewItem(), ActionNames.ITEM, true);
        
        // 2. add Shell handlers via EventBus
        ActionEvent.register(eventBus, ActionNames.OK, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getJournalView().editJournalBtnClicked();
            }
        });
        
        ActionEvent.register(eventBus, ActionNames.CANCEL, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.JOUR,
                        AllPlace.LIST, comKeyString, fisKeyString, journalTypeKeyString));
            }
        });
        
        ActionEvent.register(eventBus, ActionNames.ITEM, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                addItemBtnClicked();
            }
        });
        
        // 3. Get data
        clientFactory.getModel().getJournal(comKeyString, fisKeyString,
                journalKeyString, new AsyncCallback<SFiscalYear>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.JOUR,
                        AllPlace.LIST, comKeyString, fisKeyString, journalTypeKeyString));
            }
            @Override
            public void onSuccess(SFiscalYear result) {
                // 4. Update view
                clientFactory.getJournalView().setJournal(result, journalTypeKeyString,
                        journalKeyString, true);
            }
        });
    }
    
    private void setViewJournal(final String comKeyString, final String fisKeyString,
            final String journalTypeKeyString, final String journalKeyString){
        
        assert(comKeyString != null && fisKeyString != null
                && journalTypeKeyString != null && journalKeyString != null);
        
        // 1. set Shell
        clientFactory.getShell().reset();
        clientFactory.getShell().setHLb(constants.addJournals() + ": " + constants.view());
        clientFactory.getShell().setActBtn(0, constants.edit(), ActionNames.EDIT, true);
        clientFactory.getShell().setActBtn(1, constants.back(), ActionNames.BACK, true);
        
        // 2. add Shell handlers via EventBus
        ActionEvent.register(eventBus, ActionNames.EDIT, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.JOUR,
                        AllPlace.EDIT, comKeyString, fisKeyString, journalTypeKeyString, 
                        journalKeyString));
            }
        });
        
        ActionEvent.register(eventBus, ActionNames.BACK, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.JOUR,
                        AllPlace.LIST, comKeyString, fisKeyString, journalTypeKeyString));
            }
        });
        
        // 3. Get data
        clientFactory.getModel().getJournal(comKeyString, fisKeyString,
                journalKeyString, new AsyncCallback<SFiscalYear>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.JOUR,
                        AllPlace.LIST, comKeyString, fisKeyString, journalTypeKeyString));
            }
            @Override
            public void onSuccess(SFiscalYear result) {
                // 4. Update view
                clientFactory.getJournalView().setJournal(result, journalTypeKeyString,
                        journalKeyString, false);
            }
        });
    }
    
    private SJournalHeader genSJournal(String keyString, String docTypeKeyString,
            String no, int day, int month, int year, String desc,
            ArrayList<ItemData> itemDataList){
        SJournalHeader sJournal = new SJournalHeader(keyString, docTypeKeyString,
                no, day, month, year, desc);
        for(ItemData itemData : itemDataList){
            SJournalItem sItem = new SJournalItem(null, itemData.accChartKeyString,
                    itemData.amt, itemData.date);
            sJournal.addItem(sItem);
        }
        return sJournal;
    }
    
    private void addItemBtnClicked(){
        clientFactory.getModel().getJournal(allPlace.getComKeyString(),
                allPlace.getFisKeyString(), null, new AsyncCallback<SFiscalYear>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
            }
            @Override
            public void onSuccess(SFiscalYear result) {
                clientFactory.getJournalView().addItemBtnClicked(result);
            }
        });
    }
}
