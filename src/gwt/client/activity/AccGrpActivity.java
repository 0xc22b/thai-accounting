package gwt.client.activity;

import java.util.Date;

import gwt.client.ClientFactory;
import gwt.client.TCF;
import gwt.client.TConstants;
import gwt.client.event.ActionEvent;
import gwt.client.event.ActionNames;
import gwt.client.place.AllPlace;
import gwt.client.view.AccGrpView;
import gwt.shared.model.SAccGrp;
import gwt.shared.model.SFiscalYear;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.ResettableEventBus;

public class AccGrpActivity extends AbstractActivity implements AccGrpView.Presenter {

    private static final TConstants constants = TCF.get();
    
    private AllPlace place;
    private ClientFactory clientFactory;
    private ResettableEventBus eventBus;
    
    public AccGrpActivity(AllPlace place, ClientFactory clientFactory) {
        this.place = place;
        this.clientFactory = clientFactory;
        this.eventBus = new ResettableEventBus(clientFactory.getEventBus());
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus _eventBus) {
        clientFactory.getAccGrpView().init(this);
        panel.setWidget(clientFactory.getAccGrpView().asWidget());
        
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
    public boolean isAccGrpNameDuplicate(String keyString, String name) {
        return clientFactory.getModel().isAccGrpNameDuplicate(
                place.getComKeyString(), place.getFisKeyString(),
                keyString, name);
    }
    
    @Override
    public void addAccGrp(String name) {
        
        SAccGrp sAccGrp = new SAccGrp(null, name, new Date());

        clientFactory.getShell().setLoading();
        clientFactory.getModel().addAccGrp(place.getComKeyString(),
                place.getFisKeyString(), sAccGrp, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
                setAddAccGrpShell();
            }
            @Override
            public void onSuccess(String result) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.GRP,
                        AllPlace.LIST, place.getComKeyString(), place.getFisKeyString()));
            }
        });
    }

    @Override
    public void editAccGrp(String keyString,  String name) {
        
        SAccGrp sAccGrp = new SAccGrp(keyString, name, null);

        clientFactory.getShell().setLoading();
        clientFactory.getModel().editAccGrp(place.getComKeyString(),
                place.getFisKeyString(), sAccGrp, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
                setEditAccGrpShell();
            }
            @Override
            public void onSuccess(String result) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.GRP,
                        AllPlace.LIST, place.getComKeyString(), place.getFisKeyString()));
            }
        });
    }
    
    private void processToken(){
        if(!place.getAction().isEmpty()){
            if(place.getAction().equals(AllPlace.NEW)){
                setAddAccGrp(place.getComKeyString(), place.getFisKeyString());
                return;
            }else if(place.getAction().equals(AllPlace.EDIT)){
                setEditAccGrp(place.getComKeyString(), place.getFisKeyString(),
                        place.getKeyString());
                return;
            }else if(place.getAction().equals(AllPlace.VIEW)){
                setViewAccGrp(place.getComKeyString(), place.getFisKeyString(),
                        place.getKeyString());
                return;
            }
        }
        clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.GRP,
                AllPlace.LIST, place.getComKeyString(), place.getFisKeyString()));
    }
    
    private void setAddAccGrp(final String comKeyString, final String fisKeyString){
        
        assert(comKeyString != null && fisKeyString != null);
        
        // 1. set Shell
        setAddAccGrpShell();
        
        // 2. add Shell handlers via EventBus
        ActionEvent.register(eventBus, ActionNames.OK, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getAccGrpView().addAccGrpBtnClicked();
            }
        });
        
        ActionEvent.register(eventBus, ActionNames.CANCEL, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.GRP,
                        AllPlace.LIST, comKeyString, fisKeyString));
            }
        });
        
        // 3. Update view
        clientFactory.getModel().getAccGrp(comKeyString, fisKeyString, null,
                new AsyncCallback<SFiscalYear>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.GRP,
                        AllPlace.LIST, comKeyString, fisKeyString));
            }
            @Override
            public void onSuccess(SFiscalYear result) {
                clientFactory.getAccGrpView().setAccGrp(result, null, true);
            }
        });
    }
    
    private void setAddAccGrpShell() {
        clientFactory.getShell().reset();
        clientFactory.getShell().setHLb(constants.accGrp() + ": " + constants.createNew());
        clientFactory.getShell().setActBtn(0, constants.save(), ActionNames.OK, true);
        clientFactory.getShell().setActBtn(1, constants.cancel(), ActionNames.CANCEL, true);
    }
    
    private void setEditAccGrp(final String comKeyString, final String fisKeyString,
            final String accGrpKeyString){
        
        assert(comKeyString != null && fisKeyString != null && accGrpKeyString != null);
        
        // 1. set Shell
        setEditAccGrpShell();
        
        // 2. add Shell handlers via EventBus
        ActionEvent.register(eventBus, ActionNames.OK, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getAccGrpView().editAccGrpBtnClicked();
            }
        });
        
        ActionEvent.register(eventBus, ActionNames.CANCEL, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.GRP,
                        AllPlace.LIST, comKeyString, fisKeyString));
            }
        });
        
        // 3. Get data
        clientFactory.getModel().getAccGrp(comKeyString, fisKeyString,
                accGrpKeyString, new AsyncCallback<SFiscalYear>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.GRP,
                        AllPlace.LIST, comKeyString, fisKeyString));
            }
            @Override
            public void onSuccess(SFiscalYear result) {
                // 4. Update view
                clientFactory.getAccGrpView().setAccGrp(result, accGrpKeyString,
                        true);
            }
        });
    }
    
    private void setEditAccGrpShell() {
        clientFactory.getShell().reset();
        clientFactory.getShell().setHLb(constants.accGrp() + ": " + constants.edit());
        clientFactory.getShell().setActBtn(0, constants.save(), ActionNames.OK, true);
        clientFactory.getShell().setActBtn(1, constants.cancel(), ActionNames.CANCEL, true);
    }
    
    private void setViewAccGrp(final String comKeyString, final String fisKeyString,
            final String accGrpKeyString){
        
        assert(comKeyString != null && fisKeyString != null && accGrpKeyString != null);
        
        // 1. set Shell
        clientFactory.getShell().reset();
        clientFactory.getShell().setHLb(constants.accGrp() + ": " + constants.view());
        clientFactory.getShell().setActBtn(0, constants.edit(), ActionNames.EDIT, true);
        clientFactory.getShell().setActBtn(1, constants.back(), ActionNames.BACK, true);
        
        // 2. add Shell handlers via EventBus
        ActionEvent.register(eventBus, ActionNames.EDIT, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.GRP,
                        AllPlace.EDIT, comKeyString, fisKeyString, accGrpKeyString));
            }
        });
        
        ActionEvent.register(eventBus, ActionNames.BACK, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.GRP,
                        AllPlace.LIST, comKeyString, fisKeyString));
            }
        });
        
        // 3. Get data
        clientFactory.getModel().getAccGrp(comKeyString, fisKeyString, accGrpKeyString,
                new AsyncCallback<SFiscalYear>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.GRP,
                        AllPlace.LIST, comKeyString, fisKeyString));
            }
            @Override
            public void onSuccess(SFiscalYear result) {
                // 4. Update view
                clientFactory.getAccGrpView().setAccGrp(result, accGrpKeyString,
                        false);
            }
        });
    }
}
