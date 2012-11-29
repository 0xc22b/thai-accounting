package gwt.client.model;

import java.util.ArrayList;

import gwt.client.model.RpcService;
import gwt.client.model.RpcServiceAsync;
import gwt.shared.DataNotFoundException;
import gwt.shared.NotLoggedInException;
import gwt.shared.model.SAccChart;
import gwt.shared.model.SAccGrp;
import gwt.shared.model.SCom;
import gwt.shared.model.SComList;
import gwt.shared.model.SDocType;
import gwt.shared.model.SFinHeader;
import gwt.shared.model.SFinItem;
import gwt.shared.model.SJournalHeader;
import gwt.shared.model.SJournalType;
import gwt.shared.model.SFiscalYear;

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
    private ArrayList<String> fetchedJournalList = new ArrayList<String>();
    
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

    public void addFis(final String comKeyString, final SFiscalYear sFis,
            final boolean isSetup, final AsyncCallback<String> callback) {
        // Check and get login sessionID
        getSID(callback, new GetSIDCallback() {
            @Override
            public void onGet(String sSID, String sID) {
                // Send request to server 
                rpcService.addFis(sSID, sID, comKeyString, sFis, isSetup,
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
                        
                        // Updating AccChartNo to SFinItem here, addSFinItem,
                        // and editSFinItem because SFinHeader.addSFinItem method
                        // doesn't have access to AccChartNo in SFiscalYear.
                        // 
                        // Sorting is done in SFinHeader.addSFinItem and
                        // SFinHeader.editSFinItem.
                        // 
                        // Update AccChartNo to SFinItem
                        sFis.updateSFinItemAccChartNo();

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
                        sComList.getSCom(comKeyString).getSFis(fisKeyString).editSJournalType(sJournalType);

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
                        sComList.getSCom(comKeyString).getSFis(fisKeyString).editSAccGrp(sAccGrp);

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
                rpcService.editAccChart(sSID, sID, sAccChart, new AsyncCallback<String>() {
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
                        
                        // Update AccGrpName and ParentAccChartNo to SAccChart
                        sFis.updateSAccChartAccGrpName(editedSAccChart);
                        sFis.updateSAccChartParentAccChartNo(editedSAccChart);
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
    
    public void getFinHeader(String comKeyString, String fisKeyString,
            String finHeaderKeyString, final AsyncCallback<SFiscalYear> callback) {
        if (sComList != null) {
            SCom sCom = sComList.getSCom(comKeyString);
            if (sCom != null) {
                SFiscalYear sFis = sCom.getSFis(fisKeyString);
                if (sFis != null) {
                    if(finHeaderKeyString == null){
                        callback.onSuccess(sFis);
                        return;
                    }
                    
                    SFinHeader sFinHeader = sFis.getSFinHeader(finHeaderKeyString);
                    if(sFinHeader != null){
                        callback.onSuccess(sFis);
                        return;
                    }
                }
            }
        }
        callback.onFailure(new DataNotFoundException("getFinHeader: No data found!"));
    }
    
    public void addFinHeader(final String comKeyString, final String fisKeyString,
            final SFinHeader sFinHeader, final AsyncCallback<String> callback) {
        // Check and get login sessionID
        getSID(callback, new GetSIDCallback() {
            @Override
            public void onGet(String sSID, String sID) {
                // Send request to server 
                rpcService.addFinHeader(sSID, sID, fisKeyString, sFinHeader,
                        new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        callback.onFailure(caught);
                    }

                    @Override
                    public void onSuccess(String result) {
                        // Update local
                        sFinHeader.setKeyString(result);
                        
                        SFiscalYear sFis = sComList.getSCom(comKeyString)
                                .getSFis(fisKeyString);
                        sFis.addSFinHeader(sFinHeader);

                        // Update AccChartNo to SFinItem
                        for (SFinItem sFinItem : sFinHeader.getSFinItemList()) {
                            sFis.updateSFinItemAccChartNo(sFinItem);
                        }
                        
                        // Return data to caller
                        callback.onSuccess(result);
                    }
                });
            }
        });
    }
    
    public void editFinHeader(final String comKeyString, final String fisKeyString,
            final SFinHeader sFinHeader, final AsyncCallback<String> callback) {
        // Check and get login sessionID
        getSID(callback, new GetSIDCallback() {
            @Override
            public void onGet(String sSID, String sID) {
                // Send request to server 
                rpcService.editFinHeader(sSID, sID, sFinHeader, new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        callback.onFailure(caught);
                    }

                    @Override
                    public void onSuccess(String result) {
                        // Update local storage
                        SFiscalYear sFis = sComList.getSCom(comKeyString)
                                .getSFis(fisKeyString);
                        SFinHeader editedSFinHeader = sFis.editSFinHeader(sFinHeader);

                        // Update AccChartNo to SFinItem
                        for (SFinItem sFinItem : editedSFinHeader.getSFinItemList()) {
                            sFis.updateSFinItemAccChartNo(sFinItem);
                        }
                        
                        // Return data to caller
                        callback.onSuccess(result);
                    }
                });
            }
        });
    }
    
    public void deleteFinHeader(final String comKeyString, final String fisKeyString,
            final String finHeaderKeyString, final AsyncCallback<SFiscalYear> callback) {
        // Check and get login sessionID
        getSID(callback, new GetSIDCallback() {
            @Override
            public void onGet(String sSID, String sID) {
                // Send request to server 
                rpcService.deleteFinHeader(sSID, sID, finHeaderKeyString,
                        new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        callback.onFailure(caught);
                    }

                    @Override
                    public void onSuccess(String result) {
                        // Update local storage
                        SFiscalYear sFis = sComList.getSCom(
                                comKeyString).getSFis(fisKeyString);
                        sFis.removeSFinHeader(result);

                        // Return data to caller
                        callback.onSuccess(sFis);
                    }
                });
            }
        });
    }
    
    public void getFinItem(String comKeyString, String fisKeyString,
            String finHeaderKeyString, String finItemKeyString,
            final AsyncCallback<SFiscalYear> callback) {
        if (sComList != null) {
            SCom sCom = sComList.getSCom(comKeyString);
            if (sCom != null) {
                SFiscalYear sFis = sCom.getSFis(fisKeyString);
                if (sFis != null) {
                    SFinHeader sFinHeader = sFis.getSFinHeader(finHeaderKeyString);
                    if (sFinHeader != null) {
                        if(finItemKeyString == null){
                            callback.onSuccess(sFis);
                            return;
                        }
                        
                        SFinItem sFinItem = sFinHeader.getSFinItem(finItemKeyString);
                        if(sFinItem != null){
                            callback.onSuccess(sFis);
                            return;
                        }
                    }
                }
            }
        }
        callback.onFailure(new DataNotFoundException("getFinItem: No data found!"));
    }
    
    public void addFinItem(final String comKeyString, final String fisKeyString,
            final String finHeaderKeyString, final SFinItem sFinItem,
            final AsyncCallback<String> callback) {
        // Check and get login sessionID
        getSID(callback, new GetSIDCallback() {
            @Override
            public void onGet(String sSID, String sID) {
                // Send request to server 
                rpcService.addFinItem(sSID, sID, finHeaderKeyString, sFinItem,
                        new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        callback.onFailure(caught);
                    }

                    @Override
                    public void onSuccess(String result) {
                        // Update local
                        sFinItem.setKeyString(result);
                        
                        SFiscalYear sFis = sComList.getSCom(comKeyString)
                                .getSFis(fisKeyString);
                        SFinHeader sFinHeader = sFis.getSFinHeader(
                                finHeaderKeyString); 
                        sFinHeader.addSFinItem(sFinItem);

                        // Update AccChartNo to SFinItem
                        sFis.updateSFinItemAccChartNo(sFinItem);
                        
                        // Return data to caller
                        callback.onSuccess(result);
                    }
                });
            }
        });
    }
    
    public void editFinItem(final String comKeyString, final String fisKeyString,
            final String finHeaderKeyString, final SFinItem sFinItem,
            final AsyncCallback<String> callback) {
        // Check and get login sessionID
        getSID(callback, new GetSIDCallback() {
            @Override
            public void onGet(String sSID, String sID) {
                // Send request to server 
                rpcService.editFinItem(sSID, sID, sFinItem, new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        callback.onFailure(caught);
                    }

                    @Override
                    public void onSuccess(String result) {
                        // Update local storage
                        SFiscalYear sFis = sComList.getSCom(comKeyString)
                                .getSFis(fisKeyString);
                        SFinHeader sFinHeader = sFis.getSFinHeader(
                                finHeaderKeyString);
                        SFinItem editedSFinItem = sFinHeader.editSFinItem(sFinItem);
                        
                        // Update AccChartNo to SFinItem
                        sFis.updateSFinItemAccChartNo(editedSFinItem);

                        // Return data to caller
                        callback.onSuccess(result);
                    }
                });
            }
        });
    }
    
    public void deleteFinItem(final String comKeyString, final String fisKeyString,
            final String finHeaderKeyString, final String finItemKeyString,
            final AsyncCallback<SFiscalYear> callback) {
        // Check and get login sessionID
        getSID(callback, new GetSIDCallback() {
            @Override
            public void onGet(String sSID, String sID) {
                // Send request to server 
                rpcService.deleteFinItem(sSID, sID, finItemKeyString,
                        new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        callback.onFailure(caught);
                    }

                    @Override
                    public void onSuccess(String result) {
                        // Update local storage
                        SFiscalYear sFis = sComList.getSCom(
                                comKeyString).getSFis(fisKeyString);
                        sFis.getSFinHeader(finHeaderKeyString).removeSFinItem(result);

                        // Return data to caller
                        callback.onSuccess(sFis);
                    }
                });
            }
        });
    }

    public void getJournal(final String comKeyString, final String fisKeyString,
            final String journalKeyString, final AsyncCallback<SFiscalYear> callback){
        // Validate SComList
        if(sComList == null || sComList.getSCom(comKeyString) == null ||
                sComList.getSCom(comKeyString).getSFis(fisKeyString) == null) {
            callback.onFailure(new DataNotFoundException());
            return;
        }
        
        // Get from local first
        if(fetchedJournalList.contains(fisKeyString)){
            SFiscalYear sFis = sComList.getSCom(comKeyString).getSFis(fisKeyString);
            
            if(journalKeyString == null){
                callback.onSuccess(sFis);
                return;
            }
            SJournalHeader sJournal = sFis.getSJournal(journalKeyString);
            if(sJournal != null){
                callback.onSuccess(sFis);
                return;
            }
            callback.onFailure(new DataNotFoundException("getJournal: No data found! " + journalKeyString));
            return;
        }
        
        // Check and get login sessionID
        getSID(callback, new GetSIDCallback() {
            @Override
            public void onGet(String sSID, String sID) {
                // Send request to server 
                rpcService.getJournalList(sSID, sID, fisKeyString, new AsyncCallback<SFiscalYear>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        callback.onFailure(caught);
                    }

                    @Override
                    public void onSuccess(SFiscalYear result) {
                        // Update local
                        SFiscalYear sFis = sComList.getSCom(comKeyString)
                                .getSFis(fisKeyString);
                        sFis.setJournals(result);
                        
                        // Updating DocTypeCode, JournalTypeShortName, AccChartNo to SJournal
                        sFis.updateSJournalDocTypeCode();
                        sFis.updateSJournalJournalTypeShortName();
                        
                        // Update AccChartNo to SJournalItem
                        sFis.updateSJournalItemAccChartNo();
                        
                        // Return data to caller
                        callback.onSuccess(sFis);
                        
                        fetchedJournalList.add(fisKeyString);
                    }
                });
            }
        });    
    }
    
    public void addJournal(final String comKeyString, final String fisKeyString,
            final SJournalHeader sJournal, final AsyncCallback<String> callback) {
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
                        // Update local
                        sJournal.setKeyString(result);
                        
                        SFiscalYear sFis = sComList.getSCom(comKeyString)
                                .getSFis(fisKeyString);
                        sFis.addSJournal(sJournal);

                        // Updating DocTypeCode, JournalTypeShortName, AccChartNo to SJournal
                        sFis.updateSJournalDocTypeCode(sJournal);
                        sFis.updateSJournalJournalTypeShortName(sJournal);
                        
                        // Update AccChartNo to SJournalItem
                        sFis.updateSJournalItemAccChartNo(sJournal);
                        
                        // Return data to caller
                        callback.onSuccess(result);
                    }
                });
            }
        });
    }
    
    public void editJournal(final String comKeyString, final String fisKeyString,
            final SJournalHeader sJournal, final AsyncCallback<String> callback) {
        // Check and get login sessionID
        getSID(callback, new GetSIDCallback() {
            @Override
            public void onGet(String sSID, String sID) {
                // Send request to server 
                rpcService.editJournal(sSID, sID, sJournal, new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        callback.onFailure(caught);
                    }

                    @Override
                    public void onSuccess(String result) {
                        // Update local storage
                        SFiscalYear sFis = sComList.getSCom(comKeyString)
                                .getSFis(fisKeyString);
                        SJournalHeader editedSJournal = sFis.editSJournal(sJournal);

                        // Updating DocTypeCode, JournalTypeShortName, AccChartNo to SJournal
                        sFis.updateSJournalDocTypeCode(editedSJournal);
                        sFis.updateSJournalJournalTypeShortName(editedSJournal);
                        
                        // Update AccChartNo to SJournalItem
                        sFis.updateSJournalItemAccChartNo(editedSJournal);
                        
                        // Return data to caller
                        callback.onSuccess(result);
                    }
                });
            }
        });
    }
    
    public void deleteJournal(final String comKeyString, final String fisKeyString,
            final String journalKeyString, final AsyncCallback<SFiscalYear> callback) {
        // Check and get login sessionID
        getSID(callback, new GetSIDCallback() {
            @Override
            public void onGet(String sSID, String sID) {
                // Send request to server 
                rpcService.deleteJournal(sSID, sID, journalKeyString, new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        callback.onFailure(caught);
                    }

                    @Override
                    public void onSuccess(String result) {
                        // Update local storage
                        SFiscalYear sFis = sComList.getSCom(
                                comKeyString).getSFis(fisKeyString);
                        sFis.removeSJournal(result);

                        // Return data to caller
                        callback.onSuccess(sFis);
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
