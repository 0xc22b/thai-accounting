package gwt.client.activity;

import gwt.client.ClientFactory;
import gwt.client.TCF;
import gwt.client.TConstants;
import gwt.client.event.ActionEvent;
import gwt.client.event.ActionNames;
import gwt.client.place.AllPlace;
import gwt.client.view.AccChartView;
import gwt.shared.model.SAccChart;
import gwt.shared.model.SAccChart.AccType;
import gwt.shared.model.SFiscalYear;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.ResettableEventBus;

public class AccChartActivity extends AbstractActivity implements AccChartView.Presenter {

    private static final TConstants constants = TCF.get();
    
    private AllPlace place;
    private ClientFactory clientFactory;
    private ResettableEventBus eventBus;
    
    public AccChartActivity(AllPlace place, ClientFactory clientFactory) {
        this.place = place;
        this.clientFactory = clientFactory;
        this.eventBus = new ResettableEventBus(clientFactory.getEventBus());
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus _eventBus) {
        clientFactory.getAccChartView().init(this);
        panel.setWidget(clientFactory.getAccChartView().asWidget());
        
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
    public boolean isAccChartNoDuplicate(String keyString,
            String no) {
        return clientFactory.getModel().isAccChartNoDuplicate(
                place.getComKeyString(), place.getFisKeyString(),
                keyString, no);
    }

    @Override
    public boolean isAccChartNameDuplicate(String keyString,
            String name) {
        return clientFactory.getModel().isAccChartNameDuplicate(
                place.getComKeyString(), place.getFisKeyString(),
                keyString, name);
    }

    @Override
    public boolean isAccChartTypeValid(String keyString, AccType accType) {
        return clientFactory.getModel().isAccChartTypeValid(
                place.getComKeyString(), place.getFisKeyString(), keyString,
                accType);
    }

    @Override
    public boolean isAccChartLevelValid(String parentKeyString, int level) {
        return clientFactory.getModel().isAccChartLevelValid(
                place.getComKeyString(), place.getFisKeyString(),
                parentKeyString, level);
    }
    
    @Override
    public void addAccChart(String accGroupKeyString, String parentAccChartKeyString,
            String no, String name, AccType type, int level) {
        
        SAccChart sAccChart = new SAccChart(null, accGroupKeyString, parentAccChartKeyString, no, name, type, level, 0);
        
        clientFactory.getShell().setLoading();
        clientFactory.getModel().addAccChart(place.getComKeyString(), place.getFisKeyString(), sAccChart, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
                setAddAccChartShell();
            }
            @Override
            public void onSuccess(String result) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.CHART, AllPlace.LIST ,place.getComKeyString(), place.getFisKeyString()));
            }
        });
    }

    @Override
    public void editAccChart(String keyString, String accGroupKeyString,
            String parentAccChartKeyString, String no, String name,
            AccType type, int level) {
        
        SAccChart sAccChart = new SAccChart(keyString, accGroupKeyString,
                parentAccChartKeyString, no, name, type, level, 0);
        
        clientFactory.getShell().setLoading();
        clientFactory.getModel().editAccChart(place.getComKeyString(),
                place.getFisKeyString(), sAccChart, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
                setEditAccChartShell();
            }
            @Override
            public void onSuccess(String result) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.CHART, AllPlace.LIST, place.getComKeyString(), place.getFisKeyString()));
            }
        });
    }
    
    private void processToken(){
        if(!place.getAction().isEmpty()){
            if(place.getAction().equals(AllPlace.NEW)){
                setAddAccChart(place.getComKeyString(), place.getFisKeyString(),
                        place.getKeyString(), place.getKeyString2());
                return;
            }else if(place.getAction().equals(AllPlace.EDIT)){
                setEditAccChart(place.getComKeyString(), place.getFisKeyString(),
                        place.getKeyString());
                return;
            }else if(place.getAction().equals(AllPlace.VIEW)){
                setViewAccChart(place.getComKeyString(), place.getFisKeyString(),
                        place.getKeyString());
                return;
            }
        }
        clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.CHART,
                AllPlace.LIST, place.getComKeyString(), place.getFisKeyString()));
    }
    
    private void setAddAccChart(final String comKeyString, final String fisKeyString,
            final String accChartKeyString, final String newType){
        
        assert(comKeyString != null && fisKeyString != null);
        
        // 1. set Shell
        setAddAccChartShell();
        
        // 2. add Shell handlers via EventBus
        ActionEvent.register(eventBus, ActionNames.OK, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getAccChartView().addAccChartBtnClicked();
            }
        });
        
        ActionEvent.register(eventBus, ActionNames.CANCEL, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.CHART, AllPlace.LIST, comKeyString, fisKeyString));
            }
        });
        
        // 3. Update view
        clientFactory.getModel().getAccChart(comKeyString, fisKeyString, null,
                new AsyncCallback<SFiscalYear>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
                clientFactory.getPlaceController().goTo(new AllPlace(
                        AllPlace.CHART, AllPlace.LIST, comKeyString, fisKeyString));
            }
            @Override
            public void onSuccess(SFiscalYear result) {
                clientFactory.getAccChartView().setAccChart(result, AllPlace.NEW,
                        accChartKeyString, newType);
            }
        });
    }
    
    private void setAddAccChartShell() {
        clientFactory.getShell().reset();
        clientFactory.getShell().setHLb(constants.accChart() + ": " + constants.createNew());
        clientFactory.getShell().setActBtn(0, constants.save(), ActionNames.OK, true);
        clientFactory.getShell().setActBtn(1, constants.cancel(), ActionNames.CANCEL, true);
    }
    
    private void setEditAccChart(final String comKeyString, final String fisKeyString, final String accChartKeyString){
        
        assert(comKeyString != null && fisKeyString != null && accChartKeyString != null);
        
        // 1. set Shell
        setEditAccChartShell();
        
        // 2. add Shell handlers via EventBus
        ActionEvent.register(eventBus, ActionNames.OK, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getAccChartView().editAccChartBtnClicked();
            }
        });
        
        ActionEvent.register(eventBus, ActionNames.CANCEL, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.CHART, AllPlace.LIST, comKeyString, fisKeyString));
            }
        });
        
        // 3. Get data
        clientFactory.getModel().getAccChart(comKeyString, fisKeyString, accChartKeyString, new AsyncCallback<SFiscalYear>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.CHART, AllPlace.LIST, comKeyString, fisKeyString));
            }
            @Override
            public void onSuccess(SFiscalYear result) {
                // 4. Update view
                clientFactory.getAccChartView().setAccChart(result, AllPlace.EDIT,
                        accChartKeyString, null);
            }
        });
    }
    
    private void setEditAccChartShell() {
        clientFactory.getShell().reset();
        clientFactory.getShell().setHLb(constants.accChart() + ": " + constants.createNew());
        clientFactory.getShell().setActBtn(0, constants.save(), ActionNames.OK, true);
        clientFactory.getShell().setActBtn(1, constants.cancel(), ActionNames.CANCEL, true);
    }
    
    private void setViewAccChart(final String comKeyString, final String fisKeyString, final String accChartKeyString){
        
        assert(comKeyString != null && fisKeyString != null && accChartKeyString != null);
        
        // 1. set Shell
        clientFactory.getShell().reset();
        clientFactory.getShell().setHLb(constants.accChart() + ": " + constants.createNew());
        clientFactory.getShell().setActBtn(0, constants.edit(), ActionNames.EDIT, true);
        clientFactory.getShell().setActBtn(1, constants.back(), ActionNames.BACK, true);
        
        // 2. add Shell handlers via EventBus
        ActionEvent.register(eventBus, ActionNames.EDIT, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.CHART, AllPlace.EDIT, comKeyString, fisKeyString, accChartKeyString));
            }
        });
        
        ActionEvent.register(eventBus, ActionNames.BACK, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.CHART, AllPlace.LIST, comKeyString, fisKeyString));
            }
        });
        
        // 3. Get data
        clientFactory.getModel().getAccChart(comKeyString, fisKeyString, accChartKeyString, new AsyncCallback<SFiscalYear>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.CHART, AllPlace.LIST, comKeyString, fisKeyString));
            }
            @Override
            public void onSuccess(SFiscalYear result) {
                // 4. Update view
                clientFactory.getAccChartView().setAccChart(result, AllPlace.VIEW,
                        accChartKeyString, null);
            }
        });
    }
}
