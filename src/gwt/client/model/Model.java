package gwt.client.model;

import gwt.shared.DataNotFoundException;
import gwt.shared.NotLoggedInException;
import gwt.shared.Utils;
import gwt.shared.model.SAccAmt;
import gwt.shared.model.SAccChart;
import gwt.shared.model.SAccChart.AccType;
import gwt.shared.model.SAccGrp;
import gwt.shared.model.SCom;
import gwt.shared.model.SComList;
import gwt.shared.model.SDocType;
import gwt.shared.model.SFiscalYear;
import gwt.shared.model.SJournalHeader;
import gwt.shared.model.SJournalType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class Model {

    private interface GetSIDCallback {
        void onGet(String sSID, String sID);
    }

    private final RpcServiceAsync rpcService = GWT.create(RpcService.class);
    private SComList sComList;
    private ArrayList<String> fetchedSetupList = new ArrayList<String>();

    public Model() {

    }

    public void getComList(final AsyncCallback<SComList> callback) {
        // 1. Get from local first
        if (sComList != null) {
            callback.onSuccess(sComList);
            return;
        }

        // 2. Check and get login sessionID
        getSID(callback, new GetSIDCallback() {
            @Override
            public void onGet(String sSID, String sID) {
                // 3. Send request to server
                rpcService.getComList(sSID, sID, new AsyncCallback<SComList>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        callback.onFailure(caught);
                    }

                    @Override
                    public void onSuccess(SComList result) {
                        // 4. Update local
                        sComList = result;

                        // 5. Return data to caller
                        callback.onSuccess(sComList);
                    }
                });
            }
        });
    }

    public void getCom(String comKeyString, final AsyncCallback<SCom> callback) {
        if (sComList != null) {
            SCom sCom = sComList.getSCom(comKeyString);
            if (sCom != null) {
                callback.onSuccess(sCom);
                return;
            }
        }
        callback.onFailure(new DataNotFoundException());
    }

    public void addCom(final SCom sCom, final AsyncCallback<String> callback) {
        // Check and get login sessionID
        getSID(callback, new GetSIDCallback() {
            @Override
            public void onGet(String sSID, String sID) {
                // Send request to server
                rpcService.addCom(sSID, sID, sCom, new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        callback.onFailure(caught);
                    }

                    @Override
                    public void onSuccess(String result) {
                        // Update local storage
                        sCom.setKeyString(result);
                        sComList.addSCom(sCom);

                        // Return data to caller
                        callback.onSuccess(result);
                    }
                });
            }
        });
    }

    public void editCom(final SCom sCom, final AsyncCallback<String> callback) {
        // Check and get login sessionID
        getSID(callback, new GetSIDCallback() {
            @Override
            public void onGet(String sSID, String sID) {
                // Send request to server
                rpcService.editCom(sSID, sID, sCom, new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        callback.onFailure(caught);
                    }

                    @Override
                    public void onSuccess(String result) {
                        // Update local storage
                        sComList.editSCom(sCom);

                        // Return data to caller
                        callback.onSuccess(result);
                    }
                });
            }
        });
    }

    public void deleteCom(final String keyString, final AsyncCallback<SComList> callback) {
        // Check and get login sessionID
        getSID(callback, new GetSIDCallback() {
            @Override
            public void onGet(String sSID, String sID) {
                // Send request to server
                rpcService.deleteCom(sSID, sID, keyString, new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        callback.onFailure(caught);
                    }

                    @Override
                    public void onSuccess(String result) {
                        // Update local storage
                        sComList.removeSCom(result);

                        // Return data to caller
                        callback.onSuccess(sComList);
                    }
                });
            }
        });
    }

    public void getFis(String comKeyString, String fisKeyString,
            final AsyncCallback<SFiscalYear> callback) {
        if (sComList != null) {
            SCom sCom = sComList.getSCom(comKeyString);
            if (sCom != null) {
                SFiscalYear sFis = sCom.getSFis(fisKeyString);
                if (sFis != null) {
                    callback.onSuccess(sFis);
                    return;
                }
            }
        }
        callback.onFailure(new DataNotFoundException());
    }

    public void addFis(final String comKeyString, final int setupType,
            final SFiscalYear sFis, final AsyncCallback<String> callback) {
        // Check and get login sessionID
        getSID(callback, new GetSIDCallback() {
            @Override
            public void onGet(String sSID, String sID) {
                // Send request to server
                rpcService.addFis(sSID, sID, comKeyString, setupType, sFis,
                        new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        callback.onFailure(caught);
                    }

                    @Override
                    public void onSuccess(String result) {
                        // Update local storage
                        sFis.setKeyString(result);
                        sComList.getSCom(comKeyString).addSFis(sFis);

                        // Return data to caller
                        callback.onSuccess(result);
                    }
                });
            }
        });
    }

    public void editFis(final String comKeyString, final SFiscalYear sFis,
            final AsyncCallback<String> callback) {
        // Check and get login sessionID
        getSID(callback, new GetSIDCallback() {
            @Override
            public void onGet(String sSID, String sID) {
                // Send request to server
                rpcService.editFis(sSID, sID, sFis, new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        callback.onFailure(caught);
                    }

                    @Override
                    public void onSuccess(String result) {
                        // Update local storage
                        sComList.getSCom(comKeyString).editSFis(sFis);

                        // Return data to caller
                        callback.onSuccess(result);
                    }
                });
            }
        });
    }

    public void deleteFis(final String comKeyString, final String fisKeyString,
            final AsyncCallback<SCom> callback) {
        // Check and get login sessionID
        getSID(callback, new GetSIDCallback() {
            @Override
            public void onGet(String sSID, String sID) {
                // Send request to server
                rpcService.deleteFis(sSID, sID, fisKeyString,
                        new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        callback.onFailure(caught);
                    }

                    @Override
                    public void onSuccess(String result) {
                        // Update local storage
                        SCom sCom = sComList.getSCom(comKeyString);
                        sCom.removeSFis(result);

                        // Return data to caller
                        callback.onSuccess(sCom);
                    }
                });
            }
        });
    }

    public void getSetup(final String comKeyString, final String fisKeyString,
            final AsyncCallback<SFiscalYear> callback){
        // Validate SComList
        if(sComList == null || sComList.getSCom(comKeyString) == null
                || sComList.getSCom(comKeyString).getSFis(fisKeyString) == null) {
            callback.onFailure(new DataNotFoundException());
            return;
        }

        // Get from local first
        if(fetchedSetupList.contains(fisKeyString)){
            SFiscalYear sFis = sComList.getSCom(comKeyString).getSFis(fisKeyString);
            callback.onSuccess(sFis);
            return;
        }

        // Check and get login sessionID
        getSID(callback, new GetSIDCallback() {
            @Override
            public void onGet(String sSID, String sID) {
                // Send request to server
                rpcService.getSetup(sSID, sID, fisKeyString, new AsyncCallback<SFiscalYear>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        callback.onFailure(caught);
                    }

                    @Override
                    public void onSuccess(SFiscalYear result) {
                        // Update local
                        SFiscalYear sFis = sComList.getSCom(comKeyString)
                                .getSFis(fisKeyString);
                        sFis.setSetup(result);

                        // Updating JournalTypeShortName to SDoctype
                        sFis.updateSDocTypeJournalTypeShortName();

                        // Updating AccGrpName and ParentAccChartNo to
                        // SAccChart here, addAccChart, and editAccChart
                        // because need all AccChart to be avaiable before
                        // being able to find the parent!!
                        //
                        // Also, sorting is a bit complicate to be executed
                        // when adding and editing.
                        //
                        // Update AccGrpName and ParentAccChartNo to SAccChart
                        sFis.updateSAccChartAccGrpName();
                        sFis.updateSAccChartParentAccChartNo();
                        // Sort SAccChart list
                        sFis.sortSAccChartList();

                        // Return data to caller
                        callback.onSuccess(result);

                        fetchedSetupList.add(fisKeyString);
                    }
                });
            }
        });
    }

    public void getJournalType(String comKeyString, String fisKeyString,
            String journalTypeKeyString, final AsyncCallback<SFiscalYear> callback) {
        if (sComList != null) {
            SCom sCom = sComList.getSCom(comKeyString);
            if (sCom != null) {
                SFiscalYear sFis = sCom.getSFis(fisKeyString);
                if (sFis != null) {
                    if(journalTypeKeyString == null){
                        callback.onSuccess(sFis);
                        return;
                    }

                    SJournalType sJournalType = sFis.getSJournalType(journalTypeKeyString);
                    if(sJournalType != null){
                        callback.onSuccess(sFis);
                        return;
                    }
                }
            }
        }
        callback.onFailure(new DataNotFoundException("getJournalType: No data found!"));
    }

    public boolean isJournalTypeNameDuplicate(final String comKeyString,
            final String fisKeyString, final String keyString,
            final String name) {
        SFiscalYear sFis = sComList.getSCom(comKeyString).getSFis(fisKeyString);
        for (int i = 0; i < sFis.getSJournalTypeList().size(); i++) {
            SJournalType sExistingJournalType = sFis.getSJournalTypeList().get(i);
            if (sExistingJournalType.getName().equals(name)
                    && !sExistingJournalType.getKeyString().equals(keyString)) {
                return true;
            }
        }
        return false;
    }

    public void addJournalType(final String comKeyString, final String fisKeyString,
            final SJournalType sJournalType, final AsyncCallback<String> callback) {
        // Check and get login sessionID
        getSID(callback, new GetSIDCallback() {
            @Override
            public void onGet(String sSID, String sID) {
                // Send request to server
                rpcService.addJournalType(sSID, sID, fisKeyString, sJournalType,
                        new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        callback.onFailure(caught);
                    }

                    @Override
                    public void onSuccess(String result) {
                        // Update local storage
                        sJournalType.setKeyString(result);
                        sComList.getSCom(comKeyString).getSFis(fisKeyString).addSJournalType(sJournalType);

                        // Return data to caller
                        callback.onSuccess(result);
                    }
                });
            }
        });
    }

    public void editJournalType(final String comKeyString, final String fisKeyString,
            final SJournalType sJournalType, final AsyncCallback<String> callback) {
        // Check and get login sessionID
        getSID(callback, new GetSIDCallback() {
            @Override
            public void onGet(String sSID, String sID) {
                // Send request to server
                rpcService.editJournalType(sSID, sID, sJournalType, new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        callback.onFailure(caught);
                    }

                    @Override
                    public void onSuccess(String result) {
                        // Update local storage
                        SFiscalYear sFis = sComList.getSCom(comKeyString).getSFis(fisKeyString);

                        sFis.editSJournalType(sJournalType);

                        // Update journal type name in doctype!
                        sFis.updateSDocTypeJournalTypeShortName();

                        // Return data to caller
                        callback.onSuccess(result);
                    }
                });
            }
        });
    }

    public void deleteJournalType(final String comKeyString,
            final String fisKeyString, final String journalTypeKeyString,
            final AsyncCallback<SFiscalYear> callback) {

        // Check and get login sessionID
        getSID(callback, new GetSIDCallback() {
            @Override
            public void onGet(String sSID, String sID) {
                // Send request to server
                rpcService.deleteJournalType(sSID, sID, journalTypeKeyString,
                        new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        callback.onFailure(caught);
                    }

                    @Override
                    public void onSuccess(String result) {
                        // Update local storage
                        SFiscalYear sFis = sComList.getSCom(comKeyString).getSFis(fisKeyString);
                        sFis.removeSJournalType(result);

                        // Return data to caller
                        callback.onSuccess(sFis);
                    }
                });
            }
        });
    }

    public void getDocType(String comKeyString, String fisKeyString,
            String docTypeKeyString, final AsyncCallback<SFiscalYear> callback) {
        if (sComList != null) {
            SCom sCom = sComList.getSCom(comKeyString);
            if (sCom != null) {
                SFiscalYear sFis = sCom.getSFis(fisKeyString);
                if (sFis != null) {
                    if(docTypeKeyString == null){
                        callback.onSuccess(sFis);
                        return;
                    }

                    SDocType sDocType = sFis.getSDocType(docTypeKeyString);
                    if(sDocType != null){
                        callback.onSuccess(sFis);
                        return;
                    }
                }
            }
        }
        callback.onFailure(new DataNotFoundException("getDocType: No data found!"));
    }

    public boolean isDocTypeCodeDuplicate(final String comKeyString,
            final String fisKeyString, final String keyString,
            final String code) {
        SFiscalYear sFis = sComList.getSCom(comKeyString).getSFis(fisKeyString);
        for (int i = 0; i < sFis.getSDocTypeList().size(); i++) {
            SDocType sExistingDocType = sFis.getSDocTypeList().get(i);
            if (sExistingDocType.getCode().equals(code)
                    && !sExistingDocType.getKeyString().equals(keyString)) {
                return true;
            }
        }
        return false;
    }

    public void addDocType(final String comKeyString, final String fisKeyString,
            final SDocType sDocType, final AsyncCallback<String> callback) {
        // Check and get login sessionID
        getSID(callback, new GetSIDCallback() {
            @Override
            public void onGet(String sSID, String sID) {
                // Send request to server
                rpcService.addDocType(sSID, sID, fisKeyString, sDocType,
                        new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        callback.onFailure(caught);
                    }

                    @Override
                    public void onSuccess(String result) {
                        // Update local storage
                        sDocType.setKeyString(result);

                        SFiscalYear sFis = sComList.getSCom(comKeyString)
                                .getSFis(fisKeyString);
                        sFis.addSDocType(sDocType);

                        // Updating JournalTypeShortName to SDoctype
                        sFis.updateSDocTypeJournalTypeShortName(sDocType);

                        // Return data to caller
                        callback.onSuccess(result);
                    }
                });
            }
        });
    }

    public void editDocType(final String comKeyString, final String fisKeyString,
            final SDocType sDocType, final AsyncCallback<String> callback) {
        // Check and get login sessionID
        getSID(callback, new GetSIDCallback() {
            @Override
            public void onGet(String sSID, String sID) {
                // Send request to server
                rpcService.editDocType(sSID, sID, sDocType, new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        callback.onFailure(caught);
                    }

                    @Override
                    public void onSuccess(String result) {
                        // Update local storage
                        SFiscalYear sFis = sComList.getSCom(comKeyString)
                                .getSFis(fisKeyString);
                        SDocType editedSDocType = sFis.editSDocType(sDocType);

                        // Updating JournalTypeShortName to SDoctype
                        sFis.updateSDocTypeJournalTypeShortName(editedSDocType);

                        // Return data to caller
                        callback.onSuccess(result);
                    }
                });
            }
        });
    }

    public void deleteDocType(final String comKeyString, final String fisKeyString,
            final String docTypeKeyString,
            final AsyncCallback<SFiscalYear> callback) {
        // Check and get login sessionID
        getSID(callback, new GetSIDCallback() {
            @Override
            public void onGet(String sSID, String sID) {
                // Send request to server
                rpcService.deleteDocType(sSID, sID, docTypeKeyString,
                        new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        callback.onFailure(caught);
                    }

                    @Override
                    public void onSuccess(String result) {
                        // Update local storage
                        SFiscalYear sFis = sComList.getSCom(comKeyString).getSFis(fisKeyString);
                        sFis.removeSDocType(result);

                        // Return data to caller
                        callback.onSuccess(sFis);
                    }
                });
            }
        });
    }

    public void getAccGrp(String comKeyString, String fisKeyString, String accGrpKeyString,
            final AsyncCallback<SFiscalYear> callback) {
        if (sComList != null) {
            SCom sCom = sComList.getSCom(comKeyString);
            if (sCom != null) {
                SFiscalYear sFis = sCom.getSFis(fisKeyString);
                if (sFis != null) {
                    if(accGrpKeyString == null){
                        callback.onSuccess(sFis);
                        return;
                    }

                    SAccGrp sAccGrp = sFis.getSAccGrp(accGrpKeyString);
                    if(sAccGrp != null){
                        callback.onSuccess(sFis);
                        return;
                    }
                }
            }
        }
        callback.onFailure(new DataNotFoundException("getAccGrp: No data found!"));
    }

    public boolean isAccGrpNameDuplicate(final String comKeyString,
            final String fisKeyString, final String keyString,
            final String name) {
        SFiscalYear sFis = sComList.getSCom(comKeyString).getSFis(fisKeyString);
        for (int i = 0; i < sFis.getSAccGrpList().size(); i++) {
            SAccGrp sExistingAccGrp = sFis.getSAccGrpList().get(i);
            if (sExistingAccGrp.getName().equals(name)
                    && !sExistingAccGrp.getKeyString().equals(keyString)) {
                return true;
            }
        }
        return false;
    }

    public void addAccGrp(final String comKeyString, final String fisKeyString,
            final SAccGrp sAccGrp, final AsyncCallback<String> callback) {
        // Check and get login sessionID
        getSID(callback, new GetSIDCallback() {
            @Override
            public void onGet(String sSID, String sID) {
                // Send request to server
                rpcService.addAccGrp(sSID, sID, fisKeyString, sAccGrp, new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        callback.onFailure(caught);
                    }

                    @Override
                    public void onSuccess(String result) {
                        // Update local storage
                        sAccGrp.setKeyString(result);
                        sComList.getSCom(comKeyString).getSFis(fisKeyString).addSAccGrp(sAccGrp);

                        // Return data to caller
                        callback.onSuccess(result);
                    }
                });
            }
        });
    }

    public void editAccGrp(final String comKeyString, final String fisKeyString,
            final SAccGrp sAccGrp, final AsyncCallback<String> callback) {
        // Check and get login sessionID
        getSID(callback, new GetSIDCallback() {
            @Override
            public void onGet(String sSID, String sID) {
                // Send request to server
                rpcService.editAccGrp(sSID, sID, sAccGrp, new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        callback.onFailure(caught);
                    }

                    @Override
                    public void onSuccess(String result) {
                        // Update local storage
                        SFiscalYear sFis = sComList.getSCom(comKeyString).getSFis(fisKeyString);

                        sFis.editSAccGrp(sAccGrp);

                        // Updating AccGrpName to SAccChart
                        sFis.updateSAccChartAccGrpName();

                        // Return data to caller
                        callback.onSuccess(result);
                    }
                });
            }
        });
    }

    public void deleteAccGrp(final String comKeyString, final String fisKeyString,
            final String accGrpKeyString, final AsyncCallback<SFiscalYear> callback) {

        // Check and get login sessionID
        getSID(callback, new GetSIDCallback() {
            @Override
            public void onGet(String sSID, String sID) {
                // Send request to server
                rpcService.deleteAccGrp(sSID, sID, accGrpKeyString, new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        callback.onFailure(caught);
                    }

                    @Override
                    public void onSuccess(String result) {
                        // Update local storage
                        SFiscalYear sFis = sComList.getSCom(comKeyString).getSFis(fisKeyString);
                        sFis.removeSAccGrp(result);

                        // Return data to caller
                        callback.onSuccess(sFis);
                    }
                });
            }
        });
    }

    public void getAccChart(String comKeyString, String fisKeyString,
            String accChartKeyString, final AsyncCallback<SFiscalYear> callback) {
        if (sComList != null) {
            SCom sCom = sComList.getSCom(comKeyString);
            if (sCom != null) {
                SFiscalYear sFis = sCom.getSFis(fisKeyString);
                if (sFis != null) {

                    if(accChartKeyString == null){
                        callback.onSuccess(sFis);
                        return;
                    }

                    SAccChart sAccChart = sFis.getSAccChart(accChartKeyString);
                    if(sAccChart != null){
                        callback.onSuccess(sFis);
                        return;
                    }
                }
            }
        }
        callback.onFailure(new DataNotFoundException("getAccChart: No data found!"));
    }

    public boolean isAccChartNoDuplicate(final String comKeyString,
            final String fisKeyString, final String accChartKeyString,
            final String accChartNo) {
        SFiscalYear sFis = sComList.getSCom(comKeyString).getSFis(fisKeyString);
        for (int i = 0; i < sFis.getSAccChartList().size(); i++) {
            SAccChart sExistingAccChart = sFis.getSAccChartList().get(i);
            if (sExistingAccChart.getNo().equals(accChartNo)
                    && !sExistingAccChart.getKeyString().equals(accChartKeyString)) {
                return true;
            }
        }
        return false;
    }

    public boolean isAccChartNameDuplicate(final String comKeyString,
            final String fisKeyString, final String keyString,
            final String name) {
        SFiscalYear sFis = sComList.getSCom(comKeyString).getSFis(fisKeyString);
        for (int i = 0; i < sFis.getSAccChartList().size(); i++) {
            SAccChart sExistingAccChart = sFis.getSAccChartList().get(i);
            if (sExistingAccChart.getName().equals(name)
                    && !sExistingAccChart.getKeyString().equals(keyString)) {
                return true;
            }
        }
        return false;
    }

    public boolean isAccChartTypeValid(String comKeyString,
            final String fisKeyString, String keyString, AccType accType) {
        if (keyString == null) {
            throw new IllegalArgumentException();
        }

        SFiscalYear sFis = sComList.getSCom(comKeyString).getSFis(fisKeyString);

        if (accType == AccType.CONTROL) {
            // Entry -> Control: not in use in beginning
            SAccChart sAccChart = sFis.getSAccChart(keyString);
            if (!Utils.isZero(sAccChart.getBeginning(), 2)) {
                return false;
            }

            // Entry -> Control: not in use in journals
            //     Need to do it in server!
        } else if (accType == AccType.ENTRY) {
            // Control -> Entry: No children
            for (int i = 0; i < sFis.getSAccChartList().size(); i++) {
                SAccChart sAccChart = sFis.getSAccChartList().get(i);
                if (keyString.equals(sAccChart.getParentAccChartKeyString())) {
                    return false;
                }
            }
        } else {
            throw new AssertionError();
        }

        return true;
    }

    public boolean isAccChartLevelValid(String comKeyString,
            String fisKeyString, String parentKeyString, int level) {
        SFiscalYear sFis = sComList.getSCom(comKeyString).getSFis(fisKeyString);
        SAccChart sParentAccChart = sFis.getSAccChart(parentKeyString);
        if (sParentAccChart != null && sParentAccChart.getLevel() >= level) {
            return false;
        }
        return true;
    }

    public void addAccChart(final String comKeyString, final String fisKeyString,
            final SAccChart sAccChart, final AsyncCallback<String> callback) {
        // Check and get login sessionID
        getSID(callback, new GetSIDCallback() {
            @Override
            public void onGet(String sSID, String sID) {
                // Send request to server
                rpcService.addAccChart(sSID, sID, fisKeyString, sAccChart,
                        new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        callback.onFailure(caught);
                    }

                    @Override
                    public void onSuccess(String result) {
                        // Update local storage
                        sAccChart.setKeyString(result);

                        SFiscalYear sFis = sComList.getSCom(comKeyString)
                                .getSFis(fisKeyString);
                        sFis.addSAccChart(sAccChart);

                        // Update AccGrpName and ParentAccChartNo to SAccChart
                        sFis.updateSAccChartAccGrpName(sAccChart);
                        sFis.updateSAccChartParentAccChartNo(sAccChart);

                        // Sort SAccChart list
                        sFis.sortSAccChartList();

                        // Return data to caller
                        callback.onSuccess(result);
                    }
                });
            }
        });
    }

    public void editAccChart(final String comKeyString, final String fisKeyString,
            final SAccChart sAccChart, final AsyncCallback<String> callback) {
        // Check and get login sessionID
        getSID(callback, new GetSIDCallback() {
            @Override
            public void onGet(String sSID, String sID) {
                // Send request to server
                rpcService.editAccChart(sSID, sID, fisKeyString, sAccChart, new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        callback.onFailure(caught);
                    }

                    @Override
                    public void onSuccess(String result) {
                        // Update local storage
                        SFiscalYear sFis = sComList.getSCom(comKeyString)
                                .getSFis(fisKeyString);
                        SAccChart editedSAccChart = sFis.editSAccChart(sAccChart);

                        // Update AccGrpName to SAccChart
                        sFis.updateSAccChartAccGrpName(editedSAccChart);
                        // Update ParentAccChartNo to SAccChart
                        //     Need to update all as this might be a parent
                        sFis.updateSAccChartParentAccChartNo();

                        // Sort SAccChart list
                        sFis.sortSAccChartList();

                        // Return data to caller
                        callback.onSuccess(result);
                    }
                });
            }
        });
    }

    public void deleteAccChart(final String comKeyString, final String fisKeyString,
            final String accChartKeyString, final AsyncCallback<SFiscalYear> callback) {
        // Check and get login sessionID
        getSID(callback, new GetSIDCallback() {
            @Override
            public void onGet(String sSID, String sID) {
                // Send request to server
                rpcService.deleteAccChart(sSID, sID, accChartKeyString,
                        new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        callback.onFailure(caught);
                    }

                    @Override
                    public void onSuccess(String result) {
                        // Update local storage
                        SFiscalYear sFis = sComList.getSCom(comKeyString).getSFis(fisKeyString);
                        sFis.removeSAccChart(result);

                        // Return data to caller
                        callback.onSuccess(sFis);
                    }
                });
            }
        });
    }

    public void setBeginning(final String comKeyString, final String fisKeyString,
            final String accChartKeyString,
            final double beginning, final AsyncCallback<String> callback) {
        // Check and get login sessionID
        getSID(callback, new GetSIDCallback() {
            @Override
            public void onGet(String sSID, String sID) {
                // Send request to server
                rpcService.setBeginning(sSID, sID, accChartKeyString, beginning,
                        new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        callback.onFailure(caught);
                    }

                    @Override
                    public void onSuccess(String result) {
                        // Update local storage
                        sComList.getSCom(comKeyString).getSFis(fisKeyString).getSAccChart(accChartKeyString).setBeginning(beginning);

                        // Return data to caller
                        callback.onSuccess(result);
                    }
                });
            }
        });
    }

    // As a list of journals will be very large, 300,000 records, should not and could not keep it.
    // Let JournalListActivity handle it whether to get, maintain, or set it to null.
    public List<SJournalHeader> sJournalList;

    public void getJournalListWithJT(final String comKeyString, final String fisKeyString,
            final String journalTypeKeyString, final int month, final int year,
            final AsyncCallback<ArrayList<SJournalHeader>> callback) {

        // Validate SComList
        if(sComList == null || sComList.getSCom(comKeyString) == null ||
                sComList.getSCom(comKeyString).getSFis(fisKeyString) == null) {
            callback.onFailure(new DataNotFoundException());
            return;
        }

        // Check and get login sessionID
        getSID(callback, new GetSIDCallback() {
            @Override
            public void onGet(String sSID, String sID) {
                // Send request to server
                rpcService.getJournalListWithJT(sSID, sID, fisKeyString, journalTypeKeyString,
                        month, year, new AsyncCallback<ArrayList<SJournalHeader>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        callback.onFailure(caught);
                    }

                    @Override
                    public void onSuccess(ArrayList<SJournalHeader> result) {
                        callback.onSuccess(result);
                    }
                });
            }
        });
    }

    public void getJournalListWithAC(final String comKeyString, final String fisKeyString,
            final String beginACNo, final String endACNo, final int[] dates,
            final AsyncCallback<HashMap<String, SJournalHeader>> callback) {

        // Validate SComList
        if(sComList == null || sComList.getSCom(comKeyString) == null ||
                sComList.getSCom(comKeyString).getSFis(fisKeyString) == null) {
            callback.onFailure(new DataNotFoundException());
            return;
        }

        // Check and get login sessionID
        getSID(callback, new GetSIDCallback() {
            @Override
            public void onGet(String sSID, String sID) {
                // Send request to server
                rpcService.getJournalListWithAC(sSID, sID, fisKeyString, beginACNo, endACNo,
                        dates, new AsyncCallback<HashMap<String, SJournalHeader>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        callback.onFailure(caught);
                    }

                    @Override
                    public void onSuccess(HashMap<String, SJournalHeader> result) {
                        callback.onSuccess(result);
                    }
                });
            }
        });
    }

    public void addJournal(final String comKeyString, final String fisKeyString,
            final SJournalHeader sJournal, final AsyncCallback<SJournalHeader> callback) {
        // Check and get login sessionID
        getSID(callback, new GetSIDCallback() {
            @Override
            public void onGet(String sSID, String sID) {
                // Send request to server
                rpcService.addJournal(sSID, sID, fisKeyString, sJournal, new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        callback.onFailure(caught);
                    }

                    @Override
                    public void onSuccess(String result) {
                        sJournal.setKeyString(result);

                        // Return data to caller
                        callback.onSuccess(sJournal);
                    }
                });
            }
        });
    }

    public void editJournal(final String comKeyString, final String fisKeyString,
            final SJournalHeader sJournal, final AsyncCallback<SJournalHeader> callback) {
        // Check and get login sessionID
        getSID(callback, new GetSIDCallback() {
            @Override
            public void onGet(String sSID, String sID) {
                // Send request to server
                rpcService.editJournal(sSID, sID, fisKeyString, sJournal, new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        callback.onFailure(caught);
                    }

                    @Override
                    public void onSuccess(String result) {
                        // Return data to caller
                        callback.onSuccess(sJournal);
                    }
                });
            }
        });
    }

    public void deleteJournal(final String comKeyString, final String fisKeyString,
            final String journalKeyString, final AsyncCallback<String> callback) {
        // Check and get login sessionID
        getSID(callback, new GetSIDCallback() {
            @Override
            public void onGet(String sSID, String sID) {
                // Send request to server
                rpcService.deleteJournal(sSID, sID, fisKeyString, journalKeyString,
                        new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        callback.onFailure(caught);
                    }

                    @Override
                    public void onSuccess(String result) {
                        // Return data to caller
                        callback.onSuccess(result);
                    }
                });
            }
        });
    }

    public void addToJournalList(List<SJournalHeader> sJournalList, SJournalHeader nSJH) {
        // Find the right place to insert this new journal
        //     Sorting by inserting a new item into the correct order, better in performance.
        boolean isPut = false;
        for (int i = 0; i < sJournalList.size(); i++) {
            SJournalHeader sJournal = sJournalList.get(i);
            if (sJournal.compareDate(nSJH.getDay(), nSJH.getMonth(), nSJH.getYear()) > 0) {
                sJournalList.add(i, nSJH);
                isPut = true;
                break;
            }
        }
        if (!isPut) {
            sJournalList.add(nSJH);
        }
    }

    public void removeFromJournalList(List<SJournalHeader> sJournalList, String keyString) {
        // TODO: bad for performance, use LinkedHashMap instead so that no need to loop
        Iterator<SJournalHeader> it = sJournalList.iterator();
        while (it.hasNext()) {
            SJournalHeader sJournal = it.next();
            if (sJournal.getKeyString().equals(keyString)) {
                it.remove();
                break;
            }
        }
    }

    public void getAccAmtMap(final String comKeyString, final String fisKeyString,
            final AsyncCallback<HashMap<String, SAccAmt>> callback) {

        // Validate SComList
        if(sComList == null || sComList.getSCom(comKeyString) == null ||
                sComList.getSCom(comKeyString).getSFis(fisKeyString) == null) {
            callback.onFailure(new DataNotFoundException());
            return;
        }

        // Check and get login sessionID
        getSID(callback, new GetSIDCallback() {
            @Override
            public void onGet(String sSID, String sID) {
                // Send request to server
                rpcService.getAccAmtMap(sSID, sID, fisKeyString,
                        new AsyncCallback<HashMap<String, SAccAmt>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        callback.onFailure(caught);
                    }

                    @Override
                    public void onSuccess(HashMap<String, SAccAmt> result) {
                        callback.onSuccess(result);
                    }
                });
            }
        });
    }
    
    public void recalculateAccAmt(final String comKeyString, final String fisKeyString,
            final AsyncCallback<String> callback) {

        // Validate SComList
        if(sComList == null || sComList.getSCom(comKeyString) == null ||
                sComList.getSCom(comKeyString).getSFis(fisKeyString) == null) {
            callback.onFailure(new DataNotFoundException());
            return;
        }

        // Check and get login sessionID
        getSID(callback, new GetSIDCallback() {
            @Override
            public void onGet(String sSID, String sID) {
                // Send request to server
                rpcService.recalculateAccAmt(sSID, sID, fisKeyString,
                        new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        callback.onFailure(caught);
                    }

                    @Override
                    public void onSuccess(String result) {
                        callback.onSuccess(result);
                    }
                });
            }
        });
    }

    private void getSID(@SuppressWarnings("rawtypes") AsyncCallback asyncCallback,
            GetSIDCallback getSIDCallback) {
        String sSID = Cookies.getCookie("SSID");
        String sID = Cookies.getCookie("SID");
        if (sSID == null || sID == null) {
            asyncCallback.onFailure(new NotLoggedInException());
        } else {
            getSIDCallback.onGet(sSID, sID);
        }
    }
}
