package gwt.client.activity;

import gwt.client.ClientFactory;
import gwt.client.TCF;
import gwt.client.TConstants;
import gwt.client.event.ActionEvent;
import gwt.client.event.ActionNames;
import gwt.client.place.AllPlace;
import gwt.client.view.ListView;
import gwt.shared.model.SFiscalYear;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.ResettableEventBus;

public class BeginListActivity extends AbstractActivity implements ListView.Presenter {

    private static final TConstants constants = TCF.get();
    
    private AllPlace allPlace;
    private ClientFactory clientFactory;
    private ResettableEventBus eventBus;
    
    public BeginListActivity(AllPlace allPlace, ClientFactory clientFactory) {
        this.allPlace = allPlace;
        this.clientFactory = clientFactory;
        this.eventBus = new ResettableEventBus(clientFactory.getEventBus());
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus _eventBus) {
        clientFactory.getBeginListView().init(this);
        panel.setWidget(clientFactory.getBeginListView().asWidget());
        
        // 1. set Shell and actBtns
        // 2. add Shell handlers via EventBus
        initShell();
        
        // 3. Get data
        // 4. Update view
        getBeginList();
    }

    @Override
    public String mayStop() {
        clientFactory.getBeginListView().saveFirstVisibleIndex();
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
        //clientFactory.getPlaceController().goTo(new BeginPlace(BeginPlace.VIEW, allPlace.getComKeyString(), 
        //        allPlace.getFisKeyString(), keyString));
    }
    
    private void initShell(){
     
        clientFactory.getShell().reset();
        clientFactory.getShell().setHLb(constants.beginning());
        clientFactory.getShell().setActBtn(0, constants.createNew(), ActionNames.ADD, true);
        clientFactory.getShell().setActBtn(1, constants.view(), ActionNames.VIEW, false);
        clientFactory.getShell().setActBtn(2, constants.edit(), ActionNames.EDIT, false);
        clientFactory.getShell().setActBtn(3, constants.delete(), ActionNames.DELETE, false);
        clientFactory.getShell().setActBtn(4, constants.back(), ActionNames.BACK, true);
        
        
        ActionEvent.register(eventBus, ActionNames.ADD, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.BEGIN, AllPlace.NEW, allPlace.getComKeyString(), 
                        allPlace.getFisKeyString()));
            }
        });
        ActionEvent.register(eventBus, ActionNames.VIEW, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.BEGIN, AllPlace.VIEW, allPlace.getComKeyString(), 
                        allPlace.getFisKeyString(), clientFactory.getBeginListView().getSelectedItemKeyString()));
            }
        });
        ActionEvent.register(eventBus, ActionNames.EDIT, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.BEGIN, AllPlace.EDIT, allPlace.getComKeyString(),
                        allPlace.getFisKeyString(), clientFactory.getBeginListView().getSelectedItemKeyString()));
            }
        });
        ActionEvent.register(eventBus, ActionNames.DELETE, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                deleteBegin(allPlace.getComKeyString(), allPlace.getFisKeyString(),
                        clientFactory.getBeginListView().getSelectedItemKeyString());
            }
        });
        ActionEvent.register(eventBus, ActionNames.BACK, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.MENU, AllPlace.SETUP, allPlace.getComKeyString(), 
                        allPlace.getFisKeyString()));
            }
        });
    }
    
    private void switchActBtns(boolean visible){
        clientFactory.getShell().setActBtnVisible(0, visible);
        clientFactory.getShell().setActBtnVisible(1, !visible);
        clientFactory.getShell().setActBtnVisible(2, !visible);
        clientFactory.getShell().setActBtnVisible(3, !visible);
        clientFactory.getShell().setActBtnVisible(4, visible);
    }
    
    private void getBeginList(){
        clientFactory.getModel().getAccChart(allPlace.getComKeyString(), allPlace.getFisKeyString(), null,
                new AsyncCallback<SFiscalYear>(){
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
            }
            @Override
            public void onSuccess(SFiscalYear result) {
                clientFactory.getBeginListView().setData(result);
            }
        });
    }
    
    private void deleteBegin(String comKeyString, String fisKeyString, String accChartKeyString){
        
        if(Window.confirm(constants.confirmDelete())){
            clientFactory.getModel().setBeginning(comKeyString, fisKeyString, accChartKeyString, 0, new AsyncCallback<String>(){
                @Override
                public void onFailure(Throwable caught) {
                    Window.alert(caught.getMessage());
                }
                @Override
                public void onSuccess(String result) {
                    getBeginList();
                }
            });
        }
    }
}
