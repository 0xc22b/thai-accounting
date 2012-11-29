package gwt.client.activity;

import gwt.client.ClientFactory;
import gwt.client.TCF;
import gwt.client.TConstants;
import gwt.client.event.ActionEvent;
import gwt.client.event.ActionNames;
import gwt.client.place.AllPlace;
import gwt.client.view.FisView;
import gwt.shared.model.SFiscalYear;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.ResettableEventBus;

public class FisActivity extends AbstractActivity implements FisView.Presenter {

    private static final TConstants constants = TCF.get();
    
    private AllPlace place;
    private ClientFactory clientFactory;
    private ResettableEventBus eventBus;
    
    public FisActivity(AllPlace place, ClientFactory clientFactory) {
        this.place = place;
        this.clientFactory = clientFactory;
        this.eventBus = new ResettableEventBus(clientFactory.getEventBus());
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus _eventBus) {
        clientFactory.getFisView().init(this);
        panel.setWidget(clientFactory.getFisView().asWidget());
        
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
    public void addFis(int beginMonth, int beginYear, int endMonth, int endYear,
            boolean isSetup) {
        
        SFiscalYear sFis = new SFiscalYear(null, beginMonth, beginYear, endMonth,
                endYear);
        
        clientFactory.getModel().addFis(place.getComKeyString(), sFis, isSetup,
                new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
            }
            @Override
            public void onSuccess(String result) {
                clientFactory.getPlaceController().goTo(new AllPlace(
                        AllPlace.FIS, AllPlace.LIST, place.getComKeyString()));
            }
        });
    }

    @Override
    public void editFis(String keyString, int beginMonth, int beginYear,
            int endMonth, int endYear) {
        
        SFiscalYear sFis = new SFiscalYear(keyString, beginMonth, beginYear,
                endMonth, endYear);
        
        clientFactory.getModel().editFis(place.getComKeyString(), sFis,
                new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
            }
            @Override
            public void onSuccess(String result) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.FIS,
                        AllPlace.LIST, place.getComKeyString()));
            }
        });
    }
    
    private void processToken(){
        if(!place.getAction().isEmpty()){
            if(place.getAction().equals(AllPlace.NEW)){
                setAddFis(place.getComKeyString());
                return;
            }else if(place.getAction().equals(AllPlace.EDIT)){
                setEditFis(place.getComKeyString(), place.getFisKeyString());
                return;
            }else if(place.getAction().equals(AllPlace.VIEW)){
                setViewFis(place.getComKeyString(), place.getFisKeyString());
                return;
            }
        }
        clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.FIS,
                AllPlace.LIST, place.getComKeyString()));
    }
    
    private void setAddFis(final String comKeyString){
        
        assert(comKeyString != null);
        
        // 1. set Shell
        clientFactory.getShell().reset();
        clientFactory.getShell().setHLb(constants.fisList() + ": " + constants.createNew());
        clientFactory.getShell().setActBtn(0, constants.save(), ActionNames.OK, true);
        
        clientFactory.getShell().setActBtn(1, constants.saveAndSetup(), ActionNames.OKANDSETUP, true);
        
        clientFactory.getShell().setActBtn(2, constants.cancel(), ActionNames.CANCEL, true);
        
        // 2. add Shell handlers via EventBus
        ActionEvent.register(eventBus, ActionNames.OK, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getFisView().addFisBtnClicked(false);
            }
        });
        
        ActionEvent.register(eventBus, ActionNames.OKANDSETUP,
                new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getFisView().addFisBtnClicked(true);
            }
        });
        
        ActionEvent.register(eventBus, ActionNames.CANCEL, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.FIS,
                        AllPlace.LIST, comKeyString));
            }
        });
        
        // 3. Update view
        clientFactory.getFisView().setFis(null, true);
    }
    
    private void setEditFis(final String comKeyString, final String fisKeyString){
        
        assert(comKeyString != null && fisKeyString != null);
        
        // 1. set Shell
        clientFactory.getShell().reset();
        clientFactory.getShell().setHLb(constants.fisList() + ": " + constants.edit());
        clientFactory.getShell().setActBtn(0, constants.save(), ActionNames.OK, true);
        clientFactory.getShell().setActBtn(1, constants.cancel(), ActionNames.CANCEL, true);
        
        // 2. add Shell handlers via EventBus
        ActionEvent.register(eventBus, ActionNames.OK, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getFisView().editFisBtnClicked();
            }
        });
        
        ActionEvent.register(eventBus, ActionNames.CANCEL, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.FIS,
                        AllPlace.LIST, comKeyString));
            }
        });
        
        // 3. Get data
        clientFactory.getModel().getFis(comKeyString, fisKeyString,
                new AsyncCallback<SFiscalYear>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.FIS,
                        AllPlace.LIST, comKeyString));
            }
            @Override
            public void onSuccess(SFiscalYear result) {
                // 4. Update view
                clientFactory.getFisView().setFis(result, true);
            }
        });
    }
    
    private void setViewFis(final String comKeyString, final String fisKeyString){
        
        assert(comKeyString != null && fisKeyString != null);
        
        // 1. set Shell
        clientFactory.getShell().reset();
        clientFactory.getShell().setHLb(constants.fisList() + ": " + constants.view());
        clientFactory.getShell().setActBtn(0, constants.edit(), ActionNames.EDIT, true);
        clientFactory.getShell().setActBtn(1, constants.back(), ActionNames.BACK, true);
        
        // 2. add Shell handlers via EventBus
        ActionEvent.register(eventBus, ActionNames.EDIT, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.FIS,
                        AllPlace.EDIT, comKeyString, fisKeyString));
            }
        });
        
        ActionEvent.register(eventBus, ActionNames.BACK, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.FIS,
                        AllPlace.LIST, comKeyString));
            }
        });
        
        // 3. Get data
        clientFactory.getModel().getFis(comKeyString, fisKeyString,
                new AsyncCallback<SFiscalYear>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.FIS,
                        AllPlace.LIST, comKeyString));
            }
            @Override
            public void onSuccess(SFiscalYear result) {
                // 4. Update view
                clientFactory.getFisView().setFis(result, false);
            }
        });
    }
}
