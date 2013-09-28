package gwt.client.activity;

import gwt.client.ClientFactory;
import gwt.client.TCF;
import gwt.client.TConstants;
import gwt.client.event.ActionEvent;
import gwt.client.event.ActionNames;
import gwt.client.place.AllPlace;
import gwt.client.view.JournalView;
import gwt.client.view.JournalView.ItemData;
import gwt.shared.model.SFiscalYear;
import gwt.shared.model.SJournalHeader;
import gwt.shared.model.SJournalItem;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.ResettableEventBus;

public class JournalActivity extends AbstractActivity implements JournalView.Presenter {

    private static final TConstants constants = TCF.get();

    private AllPlace allPlace;
    private ClientFactory clientFactory;
    private ResettableEventBus eventBus;

    public JournalActivity(AllPlace allPlace, ClientFactory clientFactory) {
        this.allPlace = allPlace;
        this.clientFactory = clientFactory;
        this.eventBus = new ResettableEventBus(clientFactory.getEventBus());
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus _eventBus) {
        clientFactory.getJournalView().init(this);
        panel.setWidget(clientFactory.getJournalView().asWidget());

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
    public boolean isJournalNoDuplicate(String keyString, String docTypeKeyString,
            String no) {
        List<SJournalHeader> sJournalList = clientFactory.getModel().sJournalList;
        for (SJournalHeader sJournal : sJournalList) {
            if (sJournal.getNo().equals(no)
                    && sJournal.getDocTypeKeyString().equals(docTypeKeyString)
                    && !sJournal.getKeyString().equals(keyString)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void addJournal(String docTypeKeyString, String no, int day,
            int month, int year, String desc, ArrayList<ItemData> itemDataList) {

        SJournalHeader sJournal = genSJournal(null, docTypeKeyString, no, day,
                month, year, desc, itemDataList);

        clientFactory.getShell().setLoading();
        clientFactory.getModel().addJournal(allPlace.getComKeyString(),
                allPlace.getFisKeyString(), sJournal, new AsyncCallback<SJournalHeader>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
                setAddJournalShell();
            }
            @Override
            public void onSuccess(SJournalHeader result) {
                List<SJournalHeader> sJournalList = clientFactory.getModel().sJournalList;


                clientFactory.getModel().addToJournalList(sJournalList, result);

                clientFactory.getPlaceController().goTo(new AllPlace(
                        AllPlace.JOUR, AllPlace.LIST, allPlace.getComKeyString(),
                        allPlace.getFisKeyString(), allPlace.getKeyString(),
                        allPlace.getKeyString2(), allPlace.getKeyString3()));
            }
        });
    }

    @Override
    public void editJournal(String keyString, String docTypeKeyString, String no,
            int day, int month, int year, String desc,
            ArrayList<ItemData> itemDataList) {

        SJournalHeader sJournal = genSJournal(keyString, docTypeKeyString, no,
                day, month, year, desc, itemDataList);

        clientFactory.getShell().setLoading();
        clientFactory.getModel().editJournal(allPlace.getComKeyString(),
                allPlace.getFisKeyString(), sJournal, new AsyncCallback<SJournalHeader>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
                setEditJournalShell();
            }
            @Override
            public void onSuccess(SJournalHeader result) {

                List<SJournalHeader> sJournalList = clientFactory.getModel().sJournalList;

                // Remove the old journal
                clientFactory.getModel().removeFromJournalList(sJournalList, result.getKeyString());

                // Add the new one
                clientFactory.getModel().addToJournalList(sJournalList, result);

                clientFactory.getPlaceController().goTo(new AllPlace(
                        AllPlace.JOUR, AllPlace.LIST, allPlace.getComKeyString(),
                        allPlace.getFisKeyString(), allPlace.getKeyString(),
                        allPlace.getKeyString2(), allPlace.getKeyString3()));
            }
        });
    }

    @Override
    public void onDocTypeChanged() {
        clientFactory.getModel().getAccChart(allPlace.getComKeyString(),
                allPlace.getFisKeyString(), null, new AsyncCallback<SFiscalYear>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
            }
            @Override
            public void onSuccess(SFiscalYear sFis) {
                clientFactory.getJournalView().updateJournalDesc(sFis);
            }
        });
    }

    private void processToken(){
        if(!allPlace.getAction().isEmpty()){

            String comKeyString = allPlace.getComKeyString();
            String fisKeyString = allPlace.getFisKeyString();
            String journalTypeKeyString = allPlace.getKeyString();

            int month = Integer.parseInt(allPlace.getKeyString2());
            int year = Integer.parseInt(allPlace.getKeyString3());

            String journalKeyString = allPlace.getKeyString4();

            if(allPlace.getAction().equals(AllPlace.NEW)){
                setAddJournal(comKeyString, fisKeyString, journalTypeKeyString, month, year);
                return;
            }else if(allPlace.getAction().equals(AllPlace.EDIT)){
                setEditJournal(comKeyString, fisKeyString, journalTypeKeyString, month, year,
                        journalKeyString);
                return;
            }else if(allPlace.getAction().equals(AllPlace.VIEW)){
                setViewJournal(comKeyString, fisKeyString, journalTypeKeyString, month, year,
                        journalKeyString);
                return;
            }
        }

        clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.JOUR,
                AllPlace.LIST, allPlace.getComKeyString(), allPlace.getFisKeyString(),
                allPlace.getKeyString(), allPlace.getKeyString2(), allPlace.getKeyString3()));
    }

    private void setAddJournal(final String comKeyString, final String fisKeyString,
            final String journalTypeKeyString, final int month, final int year){

        assert(comKeyString != null && fisKeyString != null && journalTypeKeyString != null);

        // 1. set Shell
        setAddJournalShell();

        // 2. add Shell handlers via EventBus
        ActionEvent.register(eventBus, ActionNames.OK, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getJournalView().addJournalBtnClicked();
            }
        });

        ActionEvent.register(eventBus, ActionNames.CANCEL, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.JOUR,
                        AllPlace.LIST, comKeyString, fisKeyString, journalTypeKeyString,
                        allPlace.getKeyString2(), allPlace.getKeyString3()));
            }
        });

        ActionEvent.register(eventBus, ActionNames.ITEM, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                addItemBtnClicked();
            }
        });

        // 3. Update view
        clientFactory.getModel().getAccChart(comKeyString, fisKeyString, null,
                new AsyncCallback<SFiscalYear>() {

            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.JOUR,
                        AllPlace.LIST, comKeyString, fisKeyString, journalTypeKeyString,
                        allPlace.getKeyString2(), allPlace.getKeyString3()));
            }

            @Override
            public void onSuccess(SFiscalYear result) {
                clientFactory.getJournalView().setJournal(result,
                        journalTypeKeyString, month, year, null, true);
            }
        });
    }

    private void setAddJournalShell() {
        clientFactory.getShell().reset();
        clientFactory.getShell().setHLb(constants.addJournals() + ": " + constants.createNew());
        clientFactory.getShell().setActBtn(0, constants.save(), ActionNames.OK, true);
        clientFactory.getShell().setActBtn(1, constants.cancel(), ActionNames.CANCEL, true);
        clientFactory.getShell().setActBtn(2, constants.addNewItem(), ActionNames.ITEM, true);
    }

    private void setEditJournal(final String comKeyString, final String fisKeyString,
            final String journalTypeKeyString, final int month, final int year,
            final String journalKeyString){

        assert(comKeyString != null && fisKeyString != null
                && journalTypeKeyString != null && journalKeyString != null);

        // 1. set Shell
        setEditJournalShell();

        // 2. add Shell handlers via EventBus
        ActionEvent.register(eventBus, ActionNames.OK, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getJournalView().editJournalBtnClicked();
            }
        });

        ActionEvent.register(eventBus, ActionNames.CANCEL, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.JOUR,
                        AllPlace.LIST, comKeyString, fisKeyString, journalTypeKeyString,
                        allPlace.getKeyString2(), allPlace.getKeyString3()));
            }
        });

        ActionEvent.register(eventBus, ActionNames.ITEM, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                addItemBtnClicked();
            }
        });

        // 3. Get data
        clientFactory.getModel().getAccChart(comKeyString, fisKeyString,
                null, new AsyncCallback<SFiscalYear>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.JOUR,
                        AllPlace.LIST, comKeyString, fisKeyString, journalTypeKeyString,
                        allPlace.getKeyString2(), allPlace.getKeyString3()));
            }
            @Override
            public void onSuccess(SFiscalYear sFis) {

                List<SJournalHeader> sJournalList = clientFactory.getModel().sJournalList;

                // TODO: bad for performance
                SJournalHeader sJournal = null;
                for (SJournalHeader sJ : sJournalList) {
                    if (sJ.getKeyString().equals(journalKeyString)) {
                        sJournal = sJ;
                        break;
                    }
                }

                // 4. Update view
                clientFactory.getJournalView().setJournal(sFis, journalTypeKeyString, month, year,
                        sJournal, true);
            }
        });
    }

    private void setEditJournalShell() {
        clientFactory.getShell().reset();
        clientFactory.getShell().setHLb(constants.addJournals() + ": " + constants.edit());
        clientFactory.getShell().setActBtn(0, constants.save(), ActionNames.OK, true);
        clientFactory.getShell().setActBtn(1, constants.cancel(), ActionNames.CANCEL, true);
        clientFactory.getShell().setActBtn(2, constants.addNewItem(), ActionNames.ITEM, true);
    }

    private void setViewJournal(final String comKeyString, final String fisKeyString,
            final String journalTypeKeyString, final int month, final int year,
            final String journalKeyString){

        assert(comKeyString != null && fisKeyString != null
                && journalTypeKeyString != null && journalKeyString != null);

        // 1. set Shell
        clientFactory.getShell().reset();
        clientFactory.getShell().setHLb(constants.addJournals() + ": " + constants.view());
        clientFactory.getShell().setActBtn(0, constants.edit(), ActionNames.EDIT, true);
        clientFactory.getShell().setActBtn(1, constants.back(), ActionNames.BACK, true);

        // 2. add Shell handlers via EventBus
        ActionEvent.register(eventBus, ActionNames.EDIT, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.JOUR,
                        AllPlace.EDIT, comKeyString, fisKeyString, journalTypeKeyString,
                        allPlace.getKeyString2(), allPlace.getKeyString3(),
                        journalKeyString));
            }
        });

        ActionEvent.register(eventBus, ActionNames.BACK, new ActionEvent.Handler(){
            @Override
            public void onAction(ActionEvent event) {
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.JOUR,
                        AllPlace.LIST, comKeyString, fisKeyString, journalTypeKeyString,
                        allPlace.getKeyString2(), allPlace.getKeyString3()));
            }
        });

        // 3. Get data
        clientFactory.getModel().getAccChart(comKeyString, fisKeyString,
                null, new AsyncCallback<SFiscalYear>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
                clientFactory.getPlaceController().goTo(new AllPlace(AllPlace.JOUR,
                        AllPlace.LIST, comKeyString, fisKeyString, journalTypeKeyString,
                        allPlace.getKeyString2(), allPlace.getKeyString3()));
            }
            @Override
            public void onSuccess(SFiscalYear result) {

                List<SJournalHeader> sJournalList = clientFactory.getModel().sJournalList;

                // TODO: bad for performance
                SJournalHeader sJournal = null;
                for (SJournalHeader sJ : sJournalList) {
                    if (sJ.getKeyString().equals(journalKeyString)) {
                        sJournal = sJ;
                        break;
                    }
                }

                // 4. Update view
                clientFactory.getJournalView().setJournal(result, journalTypeKeyString, month,
                        year, sJournal, false);
            }
        });
    }

    private SJournalHeader genSJournal(String keyString, String docTypeKeyString,
            String no, int day, int month, int year, String desc,
            ArrayList<ItemData> itemDataList){
        SJournalHeader sJournal = new SJournalHeader(keyString, docTypeKeyString,
                no, day, month, year, desc);
        for(ItemData itemData : itemDataList){
            SJournalItem sItem = new SJournalItem(null, itemData.accChartKeyString,
                    itemData.amt, itemData.date);
            sJournal.addItem(sItem);
        }
        return sJournal;
    }

    private void addItemBtnClicked(){
        clientFactory.getModel().getAccChart(allPlace.getComKeyString(),
                allPlace.getFisKeyString(), null, new AsyncCallback<SFiscalYear>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
            }
            @Override
            public void onSuccess(SFiscalYear result) {
                clientFactory.getJournalView().addItemBtnClicked(result);
            }
        });
    }
}
