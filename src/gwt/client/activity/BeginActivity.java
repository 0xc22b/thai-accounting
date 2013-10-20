package gwt.client.activity;

import gwt.client.ClientFactory;
import gwt.client.TCF;
import gwt.client.TConstants;
import gwt.client.event.ActionEvent;
import gwt.client.event.ActionNames;
import gwt.client.place.AllPlace;
import gwt.client.view.BeginView;
import gwt.shared.model.SFiscalYear;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.ResettableEventBus;

public class BeginActivity extends AbstractActivity implements BeginView.Presenter {

    private static final TConstants constants = TCF.get();
    
    private AllPlace place;
    private ClientFactory clientFactory;
    private ResettableEventBus eventBus;
    
    public BeginActivity(AllPlace place, ClientFactory clientFactory) {
        this.place = place;
        this.clientFactory = clientFactory;
        this.eventBus = new ResettableEventBus(clientFactory.getEventBus());
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus _eventBus) {
        clientFactory.getBeginView().init(this);
        panel.setWidget(clientFactory.getBeginView().asWidget());
        
        processToken();
        
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
    public boolean isBeginningAlreadySet(String accChartKeyString) {
        return clientFactory.getModel().isBeginningAlreadySet(place.getComKeyString(),
                place.getFisKeyString(), accChartKeyString);
    }

    @Override
    public void addBegin(String keyString, double beginning) {

        clientFactory.getShell().setLoading();
        clientFactory.getModel().setBeginning(place.getComKeyString(),
                place.getFisKeyString(), keyString, beginning, 
                new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
                setAddBeginShell();
            }
            @Override
            public void onSuccess(String result) {
                clientFactory.getPlaceController().goTo(new AllPlace(
                        AllPlace.BEGIN, AllPlace.LIST, place.getComKeyString(), place.getFisKeyString()));
            }
        });
    }
    
    @Override
    public void editBegin(String keyString, double beginning) {

        clientFactory.getShell().setLoading();
        clientFactory.getModel().setBeginning(place.getComKeyString(),
                place.getFisKeyString(), keyString, beginning, 
                new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
                setEditBeginShell();
            }
            @Override
            public void onSuccess(String result) {
                clientFactory.getPlaceController().goTo(new AllPlace(
                        AllPlace.BEGIN, AllPlace.LIST, place.getComKeyString(), place.getFisKeyString()));
            }
        });
    }
    
    private void processToken(){
        if(!place.getAction().isEmpty()){
            if(place.getAction().equals(AllPlace.NEW)){
                setAddBegin(place.getComKeyString(), place.getFisKeyString());
                return;
            }else if(place.getAction().equals(AllPlace.EDIT)){
                setEditBegin(place.getComKeyString(), place.getFisKeyString(),
                        place.getKeyString());
                return;
            }else if(place.getAction().equals(AllPlace.VIEW)){
                setViewBegin(place.getComKeyString(), place.getFisKeyString(),
                        place.getKeyString());
                return;
            }
        }
        clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.BEGIN,
                AllPlace.LIST, place.getComKeyString(), place.getFisKeyString()));
    }
    
    private void setAddBegin(final String comKeyString, final String fisKeyString){
        
        assert(comKeyString != null && fisKeyString != null);
        
        // 1. set Shell
        setAddBeginShell();
        
        // 2. add Shell handlers via EventBus
        ActionEvent.register(eventBus, ActionNames.OK, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getBeginView().addBeginBtnClicked();
            }
        });
        
        ActionEvent.register(eventBus, ActionNames.CANCEL, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.BEGIN,
                        AllPlace.LIST, comKeyString, fisKeyString));
            }
        });
        
        // 3. Update view
        clientFactory.getModel().getAccChart(comKeyString, fisKeyString, null,
                new AsyncCallback<SFiscalYear>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.BEGIN,
                        AllPlace.LIST, comKeyString, fisKeyString));
            }
            @Override
            public void onSuccess(SFiscalYear result) {
                clientFactory.getBeginView().setBegin(result, null, true);
            }
        });
    }
    
    private void setAddBeginShell() {
        clientFactory.getShell().reset();
        clientFactory.getShell().setHLb(constants.beginning() + ": " + constants.createNew());
        clientFactory.getShell().setActBtn(0, constants.save(), ActionNames.OK, true);
        clientFactory.getShell().setActBtn(1, constants.cancel(), ActionNames.CANCEL, true);
    }
    
    private void setEditBegin(final String comKeyString, final String fisKeyString,
            final String accChartKeyString){
        
        assert(comKeyString != null && fisKeyString != null && accChartKeyString != null);
        
        // 1. set Shell
        setEditBeginShell();
        
        // 2. add Shell handlers via EventBus
        ActionEvent.register(eventBus, ActionNames.OK, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getBeginView().editBeginBtnClicked();
            }
        });
        
        ActionEvent.register(eventBus, ActionNames.CANCEL, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.BEGIN,
                        AllPlace.LIST, comKeyString, fisKeyString));
            }
        });
        
        // 3. Get data
        clientFactory.getModel().getAccChart(comKeyString, fisKeyString,
                accChartKeyString, new AsyncCallback<SFiscalYear>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.BEGIN,
                        AllPlace.LIST, comKeyString, fisKeyString));
            }
            @Override
            public void onSuccess(SFiscalYear result) {
                // 4. Update view
                clientFactory.getBeginView().setBegin(result, accChartKeyString,
                        true);
            }
        });
    }
    
    private void setEditBeginShell() {
        clientFactory.getShell().reset();
        clientFactory.getShell().setHLb(constants.beginning() + ": " + constants.edit());
        clientFactory.getShell().setActBtn(0, constants.save(), ActionNames.OK, true);
        clientFactory.getShell().setActBtn(1, constants.cancel(), ActionNames.CANCEL, true);
    }
    
    private void setViewBegin(final String comKeyString, final String fisKeyString,
            final String accChartKeyString){
        
        assert(comKeyString != null && fisKeyString != null && accChartKeyString != null);
        
        // 1. set Shell
        clientFactory.getShell().reset();
        clientFactory.getShell().setHLb(constants.beginning() + ": " + constants.view());
        clientFactory.getShell().setActBtn(0, constants.edit(), ActionNames.EDIT, true);
        clientFactory.getShell().setActBtn(1, constants.back(), ActionNames.BACK, true);
        
        // 2. add Shell handlers via EventBus
        ActionEvent.register(eventBus, ActionNames.EDIT, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.BEGIN,
                        AllPlace.EDIT, comKeyString, fisKeyString, accChartKeyString));
            }
        });
        
        ActionEvent.register(eventBus, ActionNames.BACK, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.BEGIN,
                        AllPlace.LIST, comKeyString, fisKeyString));
            }
        });
        
        // 3. Get data
        clientFactory.getModel().getAccChart(comKeyString, fisKeyString,
                accChartKeyString, new AsyncCallback<SFiscalYear>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.BEGIN,
                        AllPlace.LIST, comKeyString, fisKeyString));
            }
            @Override
            public void onSuccess(SFiscalYear result) {
                // 4. Update view
                clientFactory.getBeginView().setBegin(result, accChartKeyString,
                        false);
            }
        });
    }
}
