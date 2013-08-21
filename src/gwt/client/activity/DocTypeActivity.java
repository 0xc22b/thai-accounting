package gwt.client.activity;

import java.util.Date;

import gwt.client.ClientFactory;
import gwt.client.TCF;
import gwt.client.TConstants;
import gwt.client.event.ActionEvent;
import gwt.client.event.ActionNames;
import gwt.client.place.AllPlace;
import gwt.client.view.DocTypeView;
import gwt.shared.model.SDocType;
import gwt.shared.model.SFiscalYear;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.ResettableEventBus;

public class DocTypeActivity extends AbstractActivity implements DocTypeView.Presenter {

    private static final TConstants constants = TCF.get();
    
    private AllPlace place;
    private ClientFactory clientFactory;
    private ResettableEventBus eventBus;
    
    public DocTypeActivity(AllPlace place, ClientFactory clientFactory) {
        this.place = place;
        this.clientFactory = clientFactory;
        this.eventBus = new ResettableEventBus(clientFactory.getEventBus());
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus _eventBus) {
        clientFactory.getDocTypeView().init(this);
        panel.setWidget(clientFactory.getDocTypeView().asWidget());
        
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
    public boolean isDocTypeCodeDuplicate(String keyString, String code) {
        return clientFactory.getModel().isDocTypeCodeDuplicate(
                place.getComKeyString(), place.getFisKeyString(),
                keyString, code);
    }
    
    @Override
    public void addDocType(String journalTypeKeyString, String code, String name, String journalDesc) {
        
        SDocType sDocType = new SDocType(null, journalTypeKeyString, code, name, journalDesc, new Date());

        clientFactory.getShell().setLoading();
        clientFactory.getModel().addDocType(place.getComKeyString(), place.getFisKeyString(), sDocType, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
                setAddDocTypeShell();
            }
            @Override
            public void onSuccess(String result) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.DOC, AllPlace.LIST, place.getComKeyString(), place.getFisKeyString()));
            }
        });
    }

    @Override
    public void editDocType(String keyString, String journalTypeKeyString, String code, String name, String journalDesc) {
        
        SDocType sDocType = new SDocType(keyString, journalTypeKeyString, code, name, journalDesc, null);
        
        clientFactory.getShell().setLoading();
        clientFactory.getModel().editDocType(place.getComKeyString(), place.getFisKeyString(), sDocType, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
                setEditDocTypeShell();
            }
            @Override
            public void onSuccess(String result) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.DOC, AllPlace.LIST, place.getComKeyString(), place.getFisKeyString()));
            }
        });
    }
    
    private void processToken(){
        if(!place.getAction().isEmpty()){
            if(place.getAction().equals(AllPlace.NEW)){
                setAddDocType(place.getComKeyString(), place.getFisKeyString());
                return;
            }else if(place.getAction().equals(AllPlace.EDIT)){
                setEditDocType(place.getComKeyString(), place.getFisKeyString(),
                        place.getKeyString());
                return;
            }else if(place.getAction().equals(AllPlace.VIEW)){
                setViewDocType(place.getComKeyString(), place.getFisKeyString(),
                        place.getKeyString());
                return;
            }
        }
        clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.DOC,
                AllPlace.LIST, place.getComKeyString(), place.getFisKeyString()));
    }
    
    private void setAddDocType(final String comKeyString, final String fisKeyString){
        
        assert(comKeyString != null && fisKeyString != null);
        
        // 1. set Shell
        setAddDocTypeShell();
        
        // 2. add Shell handlers via EventBus
        ActionEvent.register(eventBus, ActionNames.OK, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getDocTypeView().addDocTypeBtnClicked();
            }
        });
        
        ActionEvent.register(eventBus, ActionNames.CANCEL, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.DOC,
                        AllPlace.LIST, comKeyString, fisKeyString));
            }
        });
        
        // 3. Update view
        clientFactory.getModel().getDocType(comKeyString, fisKeyString, null,
                new AsyncCallback<SFiscalYear>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.DOC,
                        AllPlace.LIST, comKeyString, fisKeyString));
            }
            @Override
            public void onSuccess(SFiscalYear result) {
                clientFactory.getDocTypeView().setDocType(result, null, true);
            }
        });
    }
    
    private void setAddDocTypeShell() {
        clientFactory.getShell().reset();
        clientFactory.getShell().setHLb(constants.docType() + ": " + constants.createNew());
        clientFactory.getShell().setActBtn(0, constants.save(), ActionNames.OK, true);
        clientFactory.getShell().setActBtn(1, constants.cancel(), ActionNames.CANCEL, true);
    }
    
    private void setEditDocType(final String comKeyString, final String fisKeyString,
            final String docTypeKeyString){
        
        assert(comKeyString != null && fisKeyString != null && docTypeKeyString != null);
        
        // 1. set Shell
        setEditDocTypeShell();
        
        // 2. add Shell handlers via EventBus
        ActionEvent.register(eventBus, ActionNames.OK, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getDocTypeView().editDocTypeBtnClicked();
            }
        });
        
        ActionEvent.register(eventBus, ActionNames.CANCEL, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.DOC,
                        AllPlace.LIST, comKeyString, fisKeyString));
            }
        });
        
        // 3. Get data
        clientFactory.getModel().getDocType(comKeyString, fisKeyString,
                docTypeKeyString, new AsyncCallback<SFiscalYear>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.DOC,
                        AllPlace.LIST, comKeyString, fisKeyString));
            }
            @Override
            public void onSuccess(SFiscalYear result) {
                // 4. Update view
                clientFactory.getDocTypeView().setDocType(result,
                        docTypeKeyString, true);
            }
        });
    }
    
    private void setEditDocTypeShell() {
        clientFactory.getShell().reset();
        clientFactory.getShell().setHLb(constants.docType() + ": " + constants.edit());
        clientFactory.getShell().setActBtn(0, constants.save(), ActionNames.OK, true);
        clientFactory.getShell().setActBtn(1, constants.cancel(), ActionNames.CANCEL, true);
    }
    
    private void setViewDocType(final String comKeyString,
            final String fisKeyString, final String docTypeKeyString){
        
        assert(comKeyString != null && fisKeyString != null && docTypeKeyString != null);
        
        // 1. set Shell
        clientFactory.getShell().reset();
        clientFactory.getShell().setHLb(constants.docType() + ": " + constants.view());
        clientFactory.getShell().setActBtn(0, constants.edit(), ActionNames.EDIT, true);
        clientFactory.getShell().setActBtn(1, constants.back(), ActionNames.BACK, true);
        
        // 2. add Shell handlers via EventBus
        ActionEvent.register(eventBus, ActionNames.EDIT, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.DOC,
                        AllPlace.EDIT, comKeyString, fisKeyString, docTypeKeyString));
            }
        });
        
        ActionEvent.register(eventBus, ActionNames.BACK, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.DOC,
                        AllPlace.LIST, comKeyString, fisKeyString));
            }
        });
        
        // 3. Get data
        clientFactory.getModel().getDocType(comKeyString, fisKeyString,
                docTypeKeyString, new AsyncCallback<SFiscalYear>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.DOC,
                        AllPlace.LIST, comKeyString, fisKeyString));
            }
            @Override
            public void onSuccess(SFiscalYear result) {
                // 4. Update view
                clientFactory.getDocTypeView().setDocType(result, docTypeKeyString,
                        false);
            }
        });
    }
}
