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

public class FinItemListActivity extends AbstractActivity implements ListView.Presenter {

    private static final TConstants constants = TCF.get();
    
    private AllPlace place;
    private ClientFactory clientFactory;
    private ResettableEventBus eventBus;
    
    public FinItemListActivity(AllPlace place, ClientFactory clientFactory) {
        this.place = place;
        this.clientFactory = clientFactory;
        this.eventBus = new ResettableEventBus(clientFactory.getEventBus());
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus _eventBus) {
        clientFactory.getFinItemListView().init(this);
        panel.setWidget(clientFactory.getFinItemListView().asWidget());
        
        getFinItemList();
    }

    @Override
    public String mayStop() {
        clientFactory.getFinItemListView().saveFirstVisibleIndex();
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
        
    }
    
    private void getFinItemList(){
        // 1. Waiting for getting data
        clientFactory.getShell().setLoading();
        
        // 2. Get data
        clientFactory.getModel().getFinItem(place.getComKeyString(),
                place.getFisKeyString(), place.getKeyString(), null,
                new AsyncCallback<SFiscalYear>(){
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
            }
            @Override
            public void onSuccess(SFiscalYear result) {
                // 3. set Shell and actBtns
                // 4. add Shell handlers via EventBus
                initShell();
                
                // 6. Update view
                clientFactory.getFinItemListView().setData(result.getSFinHeader(
                        place.getKeyString()));
            }
        });
    }
    
    private void initShell(){
     
        clientFactory.getShell().reset();
        clientFactory.getShell().setHLb(constants.finItem());
        clientFactory.getShell().setActBtn(0, constants.createNew(), ActionNames.ADD, true);
        clientFactory.getShell().setActBtn(1, constants.view(), ActionNames.VIEW, false);
        clientFactory.getShell().setActBtn(2, constants.edit(), ActionNames.EDIT, false);
        clientFactory.getShell().setActBtn(3, constants.delete(), ActionNames.DELETE, false);
        clientFactory.getShell().setActBtn(4, constants.back(), ActionNames.BACK, true);
        
        
        ActionEvent.register(eventBus, ActionNames.ADD, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(
                        AllPlace.FIN_ITEM, AllPlace.NEW, place.getComKeyString(),
                        place.getFisKeyString(), place.getKeyString()));
            }
        });
        ActionEvent.register(eventBus, ActionNames.VIEW, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(
                        AllPlace.FIN_ITEM, AllPlace.VIEW, place.getComKeyString(), 
                        place.getFisKeyString(), place.getKeyString(),
                        clientFactory.getFinItemListView().getSelectedItemKeyString()));
            }
        });
        ActionEvent.register(eventBus, ActionNames.EDIT, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(
                        AllPlace.FIN_ITEM, AllPlace.EDIT, place.getComKeyString(),
                        place.getFisKeyString(), place.getKeyString(),
                        clientFactory.getFinItemListView().getSelectedItemKeyString()));
            }
        });
        ActionEvent.register(eventBus, ActionNames.DELETE, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                deleteFinItem(place.getComKeyString(), place.getFisKeyString(),
                        place.getKeyString(), clientFactory.getFinItemListView().getSelectedItemKeyString());
            }
        });
        ActionEvent.register(eventBus, ActionNames.BACK, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(
                        AllPlace.FIN_HEADER, AllPlace.LIST,
                        place.getComKeyString(), place.getFisKeyString()));
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
    
    private void deleteFinItem(String comKeyString, String fisKeyString,
            final String finHeaderKeyString, String finItemKeyString){
        
        if(Window.confirm(constants.confirmDelete())){
            clientFactory.getModel().deleteFinItem(comKeyString, fisKeyString,
                    finHeaderKeyString, finItemKeyString,
                    new AsyncCallback<SFiscalYear>(){
                @Override
                public void onFailure(Throwable caught) {
                    Window.alert(caught.getMessage());
                }
                @Override
                public void onSuccess(SFiscalYear result) {
                    clientFactory.getFinItemListView().setData(
                            result.getSFinHeader(finHeaderKeyString));
                }
            });
        }
    }
}
