package gwt.client.activity;

import gwt.client.ClientFactory;
import gwt.client.TCF;
import gwt.client.TConstants;
import gwt.client.event.ActionEvent;
import gwt.client.event.ActionNames;
import gwt.client.place.AllPlace;
import gwt.client.view.ListView;
import gwt.shared.model.SFiscalYear;
import gwt.shared.model.SJournalHeader;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.ResettableEventBus;

public class JournalListActivity extends AbstractActivity implements ListView.Presenter {

    private static final TConstants constants = TCF.get();

    private AllPlace place;
    private ClientFactory clientFactory;
    private ResettableEventBus eventBus;

    private boolean doKeepJournalList = false;

    public JournalListActivity(AllPlace place, ClientFactory clientFactory) {
        this.place = place;
        this.clientFactory = clientFactory;
        this.eventBus = new ResettableEventBus(clientFactory.getEventBus());
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus _eventBus) {
        clientFactory.getJournalListView().init(this);
        panel.setWidget(clientFactory.getJournalListView().asWidget());

        getJournalList();
    }

    @Override
    public String mayStop() {
        if (!doKeepJournalList) {
            clientFactory.getModel().sJournalList = null;
        }

        doKeepJournalList = false;

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
        //clientFactory.getPlaceController().goTo(new MenuPlace(MenuPlace.SETUP, place.getToken(), keyString));
    }

    private void getJournalList(){
        // 1. Waiting for getting data
        clientFactory.getShell().setLoading();

        // 2. Get data
        final String comKeyString = place.getComKeyString();
        final String fisKeyString = place.getFisKeyString();
        final String journalTypeKeyString = place.getKeyString();

        clientFactory.getModel().getJournalType(comKeyString, fisKeyString, journalTypeKeyString,
                new AsyncCallback<SFiscalYear>() {

            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
            }

            @Override
            public void onSuccess(SFiscalYear result) {

                final String journalTypeName = result.getSJournalType(journalTypeKeyString).getName();

                if (clientFactory.getModel().sJournalList != null) {
                    getJournalListCallback(journalTypeName, clientFactory.getModel().sJournalList);
                } else {

                    int month = Integer.parseInt(place.getKeyString2());
                    int year = Integer.parseInt(place.getKeyString3());

                    clientFactory.getModel().getJournalListWithJT(comKeyString, fisKeyString,
                            journalTypeKeyString, month, year,
                            new AsyncCallback<ArrayList<SJournalHeader>>(){

                        @Override
                        public void onFailure(Throwable caught) {
                            Window.alert(caught.getMessage());
                        }

                        @Override
                        public void onSuccess(ArrayList<SJournalHeader> result) {

                            clientFactory.getModel().sJournalList = result;

                            getJournalListCallback(journalTypeName, result);
                        }
                    });
                }
            }
        });
    }

    private void getJournalListCallback(String journalTypeName, List<SJournalHeader> sJournalList) {
        // 3. set Shell and actBtns
        // 4. add Shell handlers via EventBus
        initShell(journalTypeName);

        // 5. Update view
        clientFactory.getJournalListView().setData(sJournalList);
    }

    private void initShell(String journalTypeName){

        clientFactory.getShell().reset();
        clientFactory.getShell().setHLb(constants.addJournals() + ": " + journalTypeName);
        clientFactory.getShell().setActBtn(0, constants.createNew(), ActionNames.ADD, true);
        clientFactory.getShell().setActBtn(1, constants.view(), ActionNames.VIEW, false);
        clientFactory.getShell().setActBtn(2, constants.edit(), ActionNames.EDIT, false);
        clientFactory.getShell().setActBtn(3, constants.delete(), ActionNames.DELETE, false);
        clientFactory.getShell().setActBtn(4, constants.back(), ActionNames.BACK, true);


        ActionEvent.register(eventBus, ActionNames.ADD, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {

                doKeepJournalList = true;
                clientFactory.getJournalListView().keepState(27);

                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.JOUR, AllPlace.NEW,
                        place.getComKeyString(), place.getFisKeyString(), place.getKeyString(),
                        place.getKeyString2(), place.getKeyString3()));
            }
        });
        ActionEvent.register(eventBus, ActionNames.VIEW, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {

                doKeepJournalList = true;
                clientFactory.getJournalListView().keepState(0);

                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.JOUR, AllPlace.VIEW,
                        place.getComKeyString(), place.getFisKeyString(), place.getKeyString(),
                        place.getKeyString2(), place.getKeyString3(),
                        clientFactory.getJournalListView().getSelectedItemKeyString()));
            }
        });
        ActionEvent.register(eventBus, ActionNames.EDIT, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {

                doKeepJournalList = true;
                clientFactory.getJournalListView().keepState(0);

                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.JOUR, AllPlace.EDIT,
                        place.getComKeyString(), place.getFisKeyString(), place.getKeyString(),
                        place.getKeyString2(), place.getKeyString3(),
                        clientFactory.getJournalListView().getSelectedItemKeyString()));
            }
        });
        ActionEvent.register(eventBus, ActionNames.DELETE, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                deleteJournal(place.getComKeyString(), place.getFisKeyString(),
                        clientFactory.getJournalListView().getSelectedItemKeyString());
            }
        });
        ActionEvent.register(eventBus, ActionNames.BACK, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.MENU, AllPlace.JOUR,
                        place.getComKeyString(), place.getFisKeyString()));
            }
        });
    }

    private void deleteJournal(String comKeyString, String fisKeyString, String journalKeyString){

        if (Window.confirm(constants.confirmDelete())) {
            
            clientFactory.getModel().deleteJournal(comKeyString, fisKeyString,
                    journalKeyString, new AsyncCallback<String>() {
                @Override
                public void onFailure(Throwable caught) {
                    Window.alert(caught.getMessage());
                }
                @Override
                public void onSuccess(String result) {
                    List<SJournalHeader> sJournalList = clientFactory.getModel().sJournalList;

                    clientFactory.getModel().removeFromJournalList(sJournalList, result);

                    clientFactory.getJournalListView().keepState(0);
                    clientFactory.getJournalListView().setData(sJournalList);
                }
            });
        }
    }

    private void switchActBtns(boolean visible){
        clientFactory.getShell().setActBtnVisible(0, visible);
        clientFactory.getShell().setActBtnVisible(1, !visible);
        clientFactory.getShell().setActBtnVisible(2, !visible);
        clientFactory.getShell().setActBtnVisible(3, !visible);
        clientFactory.getShell().setActBtnVisible(4, visible);
    }
}
