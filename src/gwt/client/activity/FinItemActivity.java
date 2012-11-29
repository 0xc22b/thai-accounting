package gwt.client.activity;

import gwt.client.ClientFactory;
import gwt.client.TCF;
import gwt.client.TConstants;
import gwt.client.event.ActionEvent;
import gwt.client.event.ActionNames;
import gwt.client.place.AllPlace;
import gwt.client.view.FinItemView;
import gwt.shared.model.SFinItem;
import gwt.shared.model.SFinItem.CalCon;
import gwt.shared.model.SFinItem.Comm;
import gwt.shared.model.SFinItem.Operand;
import gwt.shared.model.SFinItem.PrintCon;
import gwt.shared.model.SFinItem.PrintStyle;
import gwt.shared.model.SFiscalYear;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.ResettableEventBus;

public class FinItemActivity extends AbstractActivity implements FinItemView.Presenter {

    private static final TConstants constants = TCF.get();
    
    private AllPlace allPlace;
    private ClientFactory clientFactory;
    private ResettableEventBus eventBus;
    
    public FinItemActivity(AllPlace allPlace, ClientFactory clientFactory) {
        this.allPlace = allPlace;
        this.clientFactory = clientFactory;
        this.eventBus = new ResettableEventBus(clientFactory.getEventBus());
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus _eventBus) {
        clientFactory.getFinItemView().init(this);
        panel.setWidget(clientFactory.getFinItemView().asWidget());
        
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
    public void addFinItem(int seq, Comm comm, String arg, CalCon calCon,
                PrintCon printCon, PrintStyle printStyle, Operand var1,
                Operand var2, Operand var3, Operand var4) {
        
        SFinItem sFinItem = new SFinItem(null, seq, comm, arg, calCon, printCon,
                printStyle, var1, var2, var3, var4);
        
        clientFactory.getModel().addFinItem(allPlace.getComKeyString(),
                allPlace.getFisKeyString(), allPlace.getKeyString(), sFinItem,
                new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
            }
            @Override
            public void onSuccess(String result) {
                clientFactory.getPlaceController().goTo(new AllPlace(
                        AllPlace.FIN_ITEM, AllPlace.LIST, allPlace.getComKeyString(), 
                        allPlace.getFisKeyString(), allPlace.getKeyString()));
            }
        });
    }

    @Override
    public void editFinItem(String keyString, int seq, Comm comm, String arg, CalCon calCon,
                PrintCon printCon, PrintStyle printStyle, Operand var1,
                Operand var2, Operand var3, Operand var4) {
        
        SFinItem sFinItem = new SFinItem(keyString, seq, comm, arg, calCon, printCon,
                printStyle, var1, var2, var3, var4);
        
        clientFactory.getModel().editFinItem(allPlace.getComKeyString(), 
                allPlace.getFisKeyString(), allPlace.getKeyString(), sFinItem,
                new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
            }
            @Override
            public void onSuccess(String result) {
                clientFactory.getPlaceController().goTo(new AllPlace(
                        AllPlace.FIN_ITEM, AllPlace.LIST, allPlace.getComKeyString(), 
                        allPlace.getFisKeyString(), allPlace.getKeyString()));
            }
        });
    }
    
    private void processToken(){
        if(!allPlace.getAction().isEmpty()){
            if(allPlace.getAction().equals(AllPlace.NEW)){
                setAddFinItem(allPlace.getComKeyString(), allPlace.getFisKeyString(),
                        allPlace.getKeyString());
                return;
            }else if(allPlace.getAction().equals(AllPlace.EDIT)){
                setEditFinItem(allPlace.getComKeyString(), allPlace.getFisKeyString(),
                        allPlace.getKeyString(), allPlace.getKeyString2());
                return;
            }else if(allPlace.getAction().equals(AllPlace.VIEW)){
                setViewFinItem(allPlace.getComKeyString(), allPlace.getFisKeyString(),
                        allPlace.getKeyString(), allPlace.getKeyString2());
                return;
            }
        }
        clientFactory.getPlaceController().goTo(new AllPlace(
                AllPlace.FIN_ITEM, AllPlace.LIST, allPlace.getComKeyString(),
                allPlace.getFisKeyString(), allPlace.getKeyString()));
    }
    
    private void setAddFinItem(final String comKeyString, final String fisKeyString, 
            final String finHeaderKeyString){
        
        assert(comKeyString != null && fisKeyString != null && finHeaderKeyString != null);
        
        // 1. set Shell
        clientFactory.getShell().reset();
        clientFactory.getShell().setHLb(constants.finItem() + ": " + constants.createNew());
        clientFactory.getShell().setActBtn(0, constants.save(), ActionNames.OK, true);
        clientFactory.getShell().setActBtn(1, constants.cancel(), ActionNames.CANCEL, true);
        
        // 2. add Shell handlers via EventBus
        ActionEvent.register(eventBus, ActionNames.OK, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getFinItemView().addFinItemBtnClicked();
            }
        });
        
        ActionEvent.register(eventBus, ActionNames.CANCEL, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(
                        AllPlace.FIN_ITEM, AllPlace.LIST, comKeyString,
                        fisKeyString, finHeaderKeyString));
            }
        });
        
        // 3. Update view
        clientFactory.getModel().getFinItem(comKeyString, fisKeyString,
                finHeaderKeyString, null, new AsyncCallback<SFiscalYear>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
                clientFactory.getPlaceController().goTo(new AllPlace(
                        AllPlace.FIN_ITEM, AllPlace.LIST, comKeyString,
                        fisKeyString, finHeaderKeyString));
            }
            @Override
            public void onSuccess(SFiscalYear result) {
                clientFactory.getFinItemView().setFinItem(result,
                        finHeaderKeyString, null, true);
            }
        });
    }
    
    private void setEditFinItem(final String comKeyString, final String fisKeyString,
            final String finHeaderKeyString, final String finItemKeyString){
        
        assert(comKeyString != null && fisKeyString != null && finHeaderKeyString != null
                && finItemKeyString != null);
        
        // 1. set Shell
        clientFactory.getShell().reset();
        clientFactory.getShell().setHLb(constants.finItem() + ": " + constants.edit());
        clientFactory.getShell().setActBtn(0, constants.save(), ActionNames.OK, true);
        clientFactory.getShell().setActBtn(1, constants.cancel(), ActionNames.CANCEL, true);
        
        // 2. add Shell handlers via EventBus
        ActionEvent.register(eventBus, ActionNames.OK, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getFinItemView().editFinItemBtnClicked();
            }
        });
        
        ActionEvent.register(eventBus, ActionNames.CANCEL, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(
                        AllPlace.FIN_ITEM, AllPlace.LIST, comKeyString,
                        fisKeyString, finHeaderKeyString));
            }
        });
        
        // 3. Get data
        clientFactory.getModel().getFinItem(comKeyString, fisKeyString, 
                finHeaderKeyString, finItemKeyString, new AsyncCallback<SFiscalYear>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
                clientFactory.getPlaceController().goTo(new AllPlace(
                        AllPlace.FIN_ITEM, AllPlace.LIST, comKeyString,
                        fisKeyString, finHeaderKeyString));
            }
            @Override
            public void onSuccess(SFiscalYear result) {
                // 4. Update view
                clientFactory.getFinItemView().setFinItem(result,
                        finHeaderKeyString, finItemKeyString, true);
            }
        });
    }
    
    private void setViewFinItem(final String comKeyString, final String fisKeyString,
            final String finHeaderKeyString, final String finItemKeyString){
        
        assert(comKeyString != null && fisKeyString != null && finHeaderKeyString != null
                && finItemKeyString != null);
        
        // 1. set Shell
        clientFactory.getShell().reset();
        clientFactory.getShell().setHLb(constants.finItem() + ": " + constants.view());
        clientFactory.getShell().setActBtn(0, constants.edit(), ActionNames.EDIT, true);
        clientFactory.getShell().setActBtn(1, constants.back(), ActionNames.BACK, true);
        
        // 2. add Shell handlers via EventBus
        ActionEvent.register(eventBus, ActionNames.EDIT, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(
                        AllPlace.FIN_ITEM, AllPlace.EDIT, comKeyString, fisKeyString,
                        finHeaderKeyString, finItemKeyString));
            }
        });
        
        ActionEvent.register(eventBus, ActionNames.BACK, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(
                        AllPlace.FIN_ITEM, AllPlace.LIST, comKeyString, fisKeyString,
                        finHeaderKeyString));
            }
        });
        
        // 3. Get data
        clientFactory.getModel().getFinItem(comKeyString, fisKeyString,
                finHeaderKeyString, finItemKeyString, new AsyncCallback<SFiscalYear>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
                clientFactory.getPlaceController().goTo(new AllPlace(
                        AllPlace.FIN_ITEM, AllPlace.LIST, comKeyString, fisKeyString,
                        finHeaderKeyString));
            }
            @Override
            public void onSuccess(SFiscalYear result) {
                // 4. Update view
                clientFactory.getFinItemView().setFinItem(result,
                        finHeaderKeyString, finItemKeyString, false);
            }
        });
    }
}
