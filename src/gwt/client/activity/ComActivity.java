package gwt.client.activity;

import java.util.Date;

import gwt.client.ClientFactory;
import gwt.client.TCF;
import gwt.client.TConstants;
import gwt.client.event.ActionEvent;
import gwt.client.event.ActionNames;
import gwt.client.place.AllPlace;
import gwt.client.view.ComView;
import gwt.shared.model.SCom;
import gwt.shared.model.SCom.ComType;
import gwt.shared.model.SCom.YearType;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.ResettableEventBus;

public class ComActivity extends AbstractActivity implements ComView.Presenter {

    private static final TConstants constants = TCF.get();
    
    private AllPlace place;
    private ClientFactory clientFactory;
    private ResettableEventBus eventBus;
    
    public ComActivity(AllPlace place, ClientFactory clientFactory) {
        this.place = place;
        this.clientFactory = clientFactory;
        this.eventBus = new ResettableEventBus(clientFactory.getEventBus());
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus _eventBus) {
        clientFactory.getComView().init(this);
        panel.setWidget(clientFactory.getComView().asWidget());
        
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
    public void addCom(String name, String address, String telNo, ComType comType, String taxID, String merchantID,
            YearType yearType, Double vatRate) {
        
        SCom sCom = new SCom(null, name, address, telNo, comType, taxID, merchantID, yearType, vatRate, new Date());

        clientFactory.getShell().setLoading();
        clientFactory.getModel().addCom(sCom, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
                setAddComShell();
            }
            @Override
            public void onSuccess(String result) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.COM, AllPlace.LIST));
            }
        });
    }

    @Override
    public void editCom(String keyString, String name, String address, String telNo, ComType comType, 
            String taxID, String merchantID, YearType yearType, Double vatRate) {

        SCom sCom = new SCom(keyString, name, address, telNo, comType, taxID, merchantID, yearType, vatRate, null);

        clientFactory.getShell().setLoading();
        clientFactory.getModel().editCom(sCom, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
                setEditComShell();
            }
            @Override
            public void onSuccess(String result) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.COM, AllPlace.LIST));
            }
        });
    }
    
    private void processToken(){
        if(!place.getAction().isEmpty()){
            if(place.getAction().equals(AllPlace.NEW)){
                setAddCom();
                return;
            }else if(place.getAction().equals(AllPlace.EDIT)){
                setEditCom(place.getComKeyString());
                return;
            }else if(place.getAction().equals(AllPlace.VIEW)){
                setViewCom(place.getComKeyString());
                return;
            }
        }
        clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.COM, AllPlace.LIST));
    }
    
    private void setAddCom(){
        // 1. set Shell
        setAddComShell();
        
        // 2. add Shell handlers via EventBus
        ActionEvent.register(eventBus, ActionNames.OK, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getComView().addComBtnClicked();
            }
        });
        
        ActionEvent.register(eventBus, ActionNames.CANCEL, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.COM, AllPlace.LIST));
            }
        });
        
        // 3. Update view
        clientFactory.getComView().setCom(null, true);
    }
    
    private void setAddComShell() {
        clientFactory.getShell().reset();
        clientFactory.getShell().setHLb(constants.com() + ": " + constants.createNew());
        clientFactory.getShell().setActBtn(0, constants.save(), ActionNames.OK, true);
        clientFactory.getShell().setActBtn(1, constants.cancel(), ActionNames.CANCEL, true);
    }
    
    private void setEditCom(String comKeyString){
        
        assert(comKeyString != null);
        
        // 1. set Shell
        setEditComShell();
        
        // 2. add Shell handlers via EventBus
        ActionEvent.register(eventBus, ActionNames.OK, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getComView().editComBtnClicked();
            }
        });
        
        ActionEvent.register(eventBus, ActionNames.CANCEL, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.COM, AllPlace.LIST));
            }
        });
        
        // 3. Get data
        clientFactory.getModel().getCom(comKeyString, new AsyncCallback<SCom>() {
            @Override
            public void onFailure(Throwable caught) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.COM, AllPlace.LIST));
            }
            @Override
            public void onSuccess(SCom result) {
                // 4. Update view
                clientFactory.getComView().setCom(result, true);
            }
        });
    }
    
    private void setEditComShell() {
        clientFactory.getShell().reset();
        clientFactory.getShell().setHLb(constants.com() + ": " + constants.edit());
        clientFactory.getShell().setActBtn(0, constants.save(), ActionNames.OK, true);
        clientFactory.getShell().setActBtn(1, constants.cancel(), ActionNames.CANCEL, true);
    }
    
    private void setViewCom(final String comKeyString){
        
        assert(comKeyString != null);
        
        // 1. set Shell
        clientFactory.getShell().reset();
        clientFactory.getShell().setHLb(constants.com() + ": " + constants.view());
        clientFactory.getShell().setActBtn(0, constants.edit(), ActionNames.EDIT, true);
        clientFactory.getShell().setActBtn(1, constants.back(), ActionNames.BACK, true);
        
        // 2. add Shell handlers via EventBus
        ActionEvent.register(eventBus, ActionNames.EDIT, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.COM,
                        AllPlace.EDIT, comKeyString));
            }
        });
        
        ActionEvent.register(eventBus, ActionNames.BACK, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.COM,
                        AllPlace.LIST));
            }
        });
        
        // 3. Get data
        clientFactory.getModel().getCom(comKeyString, new AsyncCallback<SCom>() {
            @Override
            public void onFailure(Throwable caught) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.COM,
                        AllPlace.LIST));
            }
            @Override
            public void onSuccess(SCom result) {
                // 4. Update view
                clientFactory.getComView().setCom(result, false);
            }
        });
    }
}
