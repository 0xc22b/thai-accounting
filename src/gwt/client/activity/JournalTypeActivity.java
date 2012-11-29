package gwt.client.activity;

import java.util.Date;

import gwt.client.ClientFactory;
import gwt.client.TCF;
import gwt.client.TConstants;
import gwt.client.event.ActionEvent;
import gwt.client.event.ActionNames;
import gwt.client.place.AllPlace;
import gwt.client.view.JournalTypeView;
import gwt.shared.model.SJournalType;
import gwt.shared.model.SFiscalYear;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.ResettableEventBus;

public class JournalTypeActivity extends AbstractActivity implements JournalTypeView.Presenter {

    private static final TConstants constants = TCF.get();
    
    private AllPlace place;
    private ClientFactory clientFactory;
    private ResettableEventBus eventBus;
    
    public JournalTypeActivity(AllPlace place, ClientFactory clientFactory) {
        this.place = place;
        this.clientFactory = clientFactory;
        this.eventBus = new ResettableEventBus(clientFactory.getEventBus());
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus _eventBus) {
        clientFactory.getJournalTypeView().init(this);
        panel.setWidget(clientFactory.getJournalTypeView().asWidget());
        
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
    public void addJournalType(String desc, String shortDesc) {
        
        SJournalType sJournalType = new SJournalType(null, desc, shortDesc, new Date());
        
        clientFactory.getModel().addJournalType(place.getComKeyString(), place.getFisKeyString(), sJournalType, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
            }
            @Override
            public void onSuccess(String result) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.JOURT, AllPlace.LIST, place.getComKeyString(), place.getFisKeyString()));
            }
        });
    }

    @Override
    public void editJournalType(String keyString, String desc, String shortDesc) {
        
        SJournalType sJournalType = new SJournalType(keyString, desc, shortDesc, null);
        
        clientFactory.getModel().editJournalType(place.getComKeyString(), place.getFisKeyString(), sJournalType, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
            }
            @Override
            public void onSuccess(String result) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.JOURT, AllPlace.LIST, place.getComKeyString(), place.getFisKeyString()));
            }
        });
    }
    
    private void processToken(){
        if(!place.getAction().isEmpty()){
            if(place.getAction().equals(AllPlace.NEW)){
                setAddJournalType(place.getComKeyString(), place.getFisKeyString());
                return;
            }else if(place.getAction().equals(AllPlace.EDIT)){
                setEditJournalType(place.getComKeyString(), place.getFisKeyString(), place.getKeyString());
                return;
            }else if(place.getAction().equals(AllPlace.VIEW)){
                setViewJournalType(place.getComKeyString(), place.getFisKeyString(), place.getKeyString());
                return;
            }
        }
        clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.JOURT, AllPlace.LIST, place.getComKeyString(), place.getFisKeyString()));
    }
    
    private void setAddJournalType(final String comKeyString, final String fisKeyString){
        
        assert(comKeyString != null && fisKeyString != null);
        
        // 1. set Shell
        clientFactory.getShell().reset();
        clientFactory.getShell().setHLb(constants.journalType() + ": " + constants.createNew());
        clientFactory.getShell().setActBtn(0, constants.save(), ActionNames.OK, true);
        clientFactory.getShell().setActBtn(1, constants.cancel(), ActionNames.CANCEL, true);
        
        // 2. add Shell handlers via EventBus
        ActionEvent.register(eventBus, ActionNames.OK, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getJournalTypeView().addJournalTypeBtnClicked();
            }
        });
        
        ActionEvent.register(eventBus, ActionNames.CANCEL, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.JOURT,
                        AllPlace.LIST, comKeyString, fisKeyString));
            }
        });
        
        // 3. Update view
        clientFactory.getModel().getJournalType(comKeyString, fisKeyString, null,
                new AsyncCallback<SFiscalYear>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.JOURT,
                        AllPlace.LIST, comKeyString, fisKeyString));
            }
            @Override
            public void onSuccess(SFiscalYear result) {
                clientFactory.getJournalTypeView().setJournalType(result, null, true);
            }
        });
    }
    
    private void setEditJournalType(final String comKeyString,
            final String fisKeyString, final String journalTypeKeyString){
        
        assert(comKeyString != null && fisKeyString != null
                && journalTypeKeyString != null);
        
        // 1. set Shell
        clientFactory.getShell().reset();
        clientFactory.getShell().setHLb(constants.journalType() + ": " + constants.edit());
        clientFactory.getShell().setActBtn(0, constants.save(), ActionNames.OK, true);
        clientFactory.getShell().setActBtn(1, constants.cancel(), ActionNames.CANCEL, true);
        
        // 2. add Shell handlers via EventBus
        ActionEvent.register(eventBus, ActionNames.OK, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getJournalTypeView().editJournalTypeBtnClicked();
            }
        });
        
        ActionEvent.register(eventBus, ActionNames.CANCEL, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.JOURT,
                        AllPlace.LIST, comKeyString, fisKeyString));
            }
        });
        
        // 3. Get data
        clientFactory.getModel().getJournalType(comKeyString, fisKeyString,
                journalTypeKeyString, new AsyncCallback<SFiscalYear>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.JOURT,
                        AllPlace.LIST, comKeyString, fisKeyString));
            }
            @Override
            public void onSuccess(SFiscalYear result) {
                // 4. Update view
                clientFactory.getJournalTypeView().setJournalType(result,
                        journalTypeKeyString, true);
            }
        });
    }
    
    private void setViewJournalType(final String comKeyString,
            final String fisKeyString, final String journalTypeKeyString){
        
        assert(comKeyString != null && fisKeyString != null
                && journalTypeKeyString != null);
        
        // 1. set Shell
        clientFactory.getShell().reset();
        clientFactory.getShell().setHLb(constants.journalType() + ": " + constants.view());
        clientFactory.getShell().setActBtn(0, constants.edit(), ActionNames.EDIT, true);
        clientFactory.getShell().setActBtn(1, constants.back(), ActionNames.BACK, true);
        
        // 2. add Shell handlers via EventBus
        ActionEvent.register(eventBus, ActionNames.EDIT, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.JOURT,
                        AllPlace.EDIT, comKeyString, fisKeyString, journalTypeKeyString));
            }
        });
        
        ActionEvent.register(eventBus, ActionNames.BACK, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.JOURT,
                        AllPlace.LIST, comKeyString, fisKeyString));
            }
        });
        
        // 3. Get data
        clientFactory.getModel().getJournalType(comKeyString, fisKeyString,
                journalTypeKeyString, new AsyncCallback<SFiscalYear>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.JOURT,
                        AllPlace.LIST, comKeyString, fisKeyString));
            }
            @Override
            public void onSuccess(SFiscalYear result) {
                // 4. Update view
                clientFactory.getJournalTypeView().setJournalType(result,
                        journalTypeKeyString, false);
            }
        });
    }
}
