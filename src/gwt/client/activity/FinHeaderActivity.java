package gwt.client.activity;

import java.util.Date;

import gwt.client.ClientFactory;
import gwt.client.TCF;
import gwt.client.TConstants;
import gwt.client.event.ActionEvent;
import gwt.client.event.ActionNames;
import gwt.client.place.AllPlace;
import gwt.client.view.FinHeaderView;
import gwt.shared.model.SFiscalYear;
import gwt.shared.model.SFinHeader;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.ResettableEventBus;

public class FinHeaderActivity extends AbstractActivity implements FinHeaderView.Presenter {

    private static final TConstants constants = TCF.get();
    
    private AllPlace allPlace;
    private ClientFactory clientFactory;
    private ResettableEventBus eventBus;
    
    public FinHeaderActivity(AllPlace allPlace, ClientFactory clientFactory) {
        this.allPlace = allPlace;
        this.clientFactory = clientFactory;
        this.eventBus = new ResettableEventBus(clientFactory.getEventBus());
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus _eventBus) {
        clientFactory.getFinHeaderView().init(this);
        panel.setWidget(clientFactory.getFinHeaderView().asWidget());
        
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
    public void addFinHeader(String name) {
        
        SFinHeader sFinHeader = new SFinHeader(null, name, new Date());
        
        clientFactory.getModel().addFinHeader(allPlace.getComKeyString(),
                allPlace.getFisKeyString(), sFinHeader, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                // TODO Auto-generated method stub
                Window.alert(caught.getMessage());
            }
            @Override
            public void onSuccess(String result) {
                clientFactory.getPlaceController().goTo(new AllPlace(
                        AllPlace.FIN_HEADER, AllPlace.LIST,
                        allPlace.getComKeyString(), allPlace.getFisKeyString()));
            }
        });
    }

    @Override
    public void editFinHeader(String keyString, String name) {
        
        SFinHeader sFinHeader = new SFinHeader(keyString, name, new Date());
        
        clientFactory.getModel().editFinHeader(allPlace.getComKeyString(),
                allPlace.getFisKeyString(), sFinHeader, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                // TODO Auto-generated method stub
                Window.alert(caught.getMessage());
            }
            @Override
            public void onSuccess(String result) {
                clientFactory.getPlaceController().goTo(new AllPlace(
                        AllPlace.FIN_HEADER, AllPlace.LIST,
                        allPlace.getComKeyString(), allPlace.getFisKeyString()));
            }
        });
    }
    
    private void processToken(){
        if(!allPlace.getAction().isEmpty()){
            if(allPlace.getAction().equals(AllPlace.NEW)){
                setAddFin(allPlace.getComKeyString(), allPlace.getFisKeyString());
                return;
            }else if(allPlace.getAction().equals(AllPlace.EDIT)){
                setEditFin(allPlace.getComKeyString(), allPlace.getFisKeyString(),
                        allPlace.getKeyString());
                return;
            }else if(allPlace.getAction().equals(AllPlace.VIEW)){
                setViewFin(allPlace.getComKeyString(), allPlace.getFisKeyString(),
                        allPlace.getKeyString());
                return;
            }
        }
        clientFactory.getPlaceController().goTo(new AllPlace(
                AllPlace.FIN_HEADER, AllPlace.LIST, allPlace.getComKeyString(),
                allPlace.getFisKeyString()));
    }
    
    private void setAddFin(final String comKeyString, final String fisKeyString){
        
        assert(comKeyString != null && fisKeyString != null);
        
        // 1. set Shell
        clientFactory.getShell().reset();
        clientFactory.getShell().setHLb(constants.fin() + ": " + constants.createNew());
        clientFactory.getShell().setActBtn(0, constants.save(), ActionNames.OK, true);
        clientFactory.getShell().setActBtn(1, constants.cancel(), ActionNames.CANCEL, true);
        
        // 2. add Shell handlers via EventBus
        ActionEvent.register(eventBus, ActionNames.OK, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getFinHeaderView().addFinHeaderBtnClicked();
            }
        });
        
        ActionEvent.register(eventBus, ActionNames.CANCEL, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(
                        AllPlace.FIN_HEADER, AllPlace.LIST, comKeyString, fisKeyString));
            }
        });
        
        // 3. Update view
        clientFactory.getModel().getFinHeader(comKeyString, fisKeyString, null,
                new AsyncCallback<SFiscalYear>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
                clientFactory.getPlaceController().goTo(new AllPlace(
                        AllPlace.FIN_HEADER, AllPlace.LIST, comKeyString, fisKeyString));
            }
            @Override
            public void onSuccess(SFiscalYear result) {
                clientFactory.getFinHeaderView().setFinHeader(result, null, true);
            }
        });
    }
    
    private void setEditFin(final String comKeyString, final String fisKeyString,
            final String finHeaderKeyString){
        
        assert(comKeyString != null && fisKeyString != null && finHeaderKeyString != null);
        
        // 1. set Shell
        clientFactory.getShell().reset();
        clientFactory.getShell().setHLb(constants.fin() + ": " + constants.edit());
        clientFactory.getShell().setActBtn(0, constants.save(), ActionNames.OK, true);
        clientFactory.getShell().setActBtn(1, constants.cancel(), ActionNames.CANCEL, true);
        
        // 2. add Shell handlers via EventBus
        ActionEvent.register(eventBus, ActionNames.OK, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getFinHeaderView().editFinHeaderBtnClicked();
            }
        });
        
        ActionEvent.register(eventBus, ActionNames.CANCEL, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(
                        AllPlace.FIN_HEADER, AllPlace.LIST, comKeyString, fisKeyString));
            }
        });
        
        // 3. Get data
        clientFactory.getModel().getFinHeader(comKeyString, fisKeyString,
                finHeaderKeyString, new AsyncCallback<SFiscalYear>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
                clientFactory.getPlaceController().goTo(new AllPlace(
                        AllPlace.FIN_HEADER, AllPlace.LIST, comKeyString, fisKeyString));
            }
            @Override
            public void onSuccess(SFiscalYear result) {
                // 4. Update view
                clientFactory.getFinHeaderView().setFinHeader(result,
                        finHeaderKeyString, true);
            }
        });
    }
    
    private void setViewFin(final String comKeyString, final String fisKeyString,
            final String finHeaderKeyString){
        
        assert(comKeyString != null && fisKeyString != null && finHeaderKeyString != null);

        // 1. set Shell
        clientFactory.getShell().reset();
        clientFactory.getShell().setHLb(constants.fin() + ": " + constants.view());
        clientFactory.getShell().setActBtn(0, constants.edit(), ActionNames.EDIT, true);
        clientFactory.getShell().setActBtn(1, constants.back(), ActionNames.BACK, true);
        
        // 2. add Shell handlers via EventBus
        ActionEvent.register(eventBus, ActionNames.EDIT, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(
                        AllPlace.FIN_HEADER, AllPlace.EDIT, comKeyString,
                        fisKeyString, finHeaderKeyString));
            }
        });
        
        ActionEvent.register(eventBus, ActionNames.BACK, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(
                        AllPlace.FIN_HEADER, AllPlace.LIST, comKeyString, fisKeyString));
            }
        });
        
        // 3. Get data
        clientFactory.getModel().getFinHeader(comKeyString,
                fisKeyString, finHeaderKeyString, new AsyncCallback<SFiscalYear>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
                clientFactory.getPlaceController().goTo(new AllPlace(
                        AllPlace.FIN_HEADER, AllPlace.LIST, comKeyString, fisKeyString));
            }
            @Override
            public void onSuccess(SFiscalYear result) {
                // 4. Update view
                clientFactory.getFinHeaderView().setFinHeader(result,
                        finHeaderKeyString, false);
            }
        });
    }
}
