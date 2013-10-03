package gwt.client.model;

import gwt.shared.model.SAccAmt;
import gwt.shared.model.SAccChart;
import gwt.shared.model.SAccGrp;
import gwt.shared.model.SCom;
import gwt.shared.model.SComList;
import gwt.shared.model.SDocType;
import gwt.shared.model.SFiscalYear;
import gwt.shared.model.SJournalHeader;
import gwt.shared.model.SJournalType;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>RpcService</code>.
 */
public interface RpcServiceAsync {

	void getComList(AsyncCallback<SComList> callback);
	
	void addCom(SCom sCom, AsyncCallback<String> callback);
	void editCom(SCom sCom, AsyncCallback<String> callback);
	void deleteCom(String keyString, AsyncCallback<String> callback);
	
    void addFis(String comKeyString, int setupType, SFiscalYear sFis, AsyncCallback<String> callback);
    void editFis(SFiscalYear sFis, AsyncCallback<String> callback);
    void deleteFis(String keyString, AsyncCallback<String> callback);
    
    void getSetup(String fisKeyString, AsyncCallback<SFiscalYear> callback);
    
    void addJournalType(String fisKeyString, SJournalType sJournalType, AsyncCallback<String> callback);
    void editJournalType(SJournalType sJournalType, AsyncCallback<String> callback);
    void deleteJournalType(String keyString, AsyncCallback<String> callback);
    
    void addDocType(String fisKeyString, SDocType sDocType, AsyncCallback<String> callback);
    void editDocType(SDocType sDocType, AsyncCallback<String> callback);
    void deleteDocType(String keyString, AsyncCallback<String> callback);
    
    void addAccGrp(String fisKeyString, SAccGrp sAccGrp, AsyncCallback<String> callback);
    void editAccGrp(SAccGrp sAccGrp, AsyncCallback<String> callback);
    void deleteAccGrp(String keyString, AsyncCallback<String> callback);
    
    void addAccChart(String fisKeyString, SAccChart sAccChart, AsyncCallback<String> callback);
    void editAccChart(String fisKeyString, SAccChart sAccChart, AsyncCallback<String> callback);
    void deleteAccChart(String keyString, AsyncCallback<String> callback);
    
    void setBeginning(String accChartKeyString, double beginning, AsyncCallback<String> callback);

    void getJournalListWithJT(String fisKeyString,
            String journalTypeKeyString, int month, int year, AsyncCallback<ArrayList<SJournalHeader>> callback);

	void addJournal(String fisKeyString, SJournalHeader sJournal, AsyncCallback<String> callback);
    void editJournal(String fisKeyString, SJournalHeader sJournal, AsyncCallback<String> callback);
    void deleteJournal(String fisKeyString, String keyString, AsyncCallback<String> callback);

    void getAccAmtMap(String fisKeyString, AsyncCallback<HashMap<String, SAccAmt>> callback);
    void recalculateAccAmt(String fisKeyString, AsyncCallback<String> callback);
}
