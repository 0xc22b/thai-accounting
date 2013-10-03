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

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("rpcService")
public interface RpcService extends RemoteService {
	
	SComList getComList();
	
	String addCom(SCom sCom);
	String editCom(SCom sCom);
	String deleteCom(String keyString);
	
	String addFis(String comKeyString, int setupType, SFiscalYear sFis);
    String editFis(SFiscalYear sFis);
    String deleteFis(String keyString);
    
    SFiscalYear getSetup(String fisKeyString);
    
    String addJournalType(String fisKeyString, SJournalType sJournalType);
    String editJournalType(SJournalType sJournalType);
    String deleteJournalType(String keyString);
    
    String addDocType(String fisKeyString, SDocType sDocType);
    String editDocType(SDocType sDocType);
    String deleteDocType(String keyString);
    
    String addAccGrp(String fisKeyString, SAccGrp sAccGrp);
    String editAccGrp(SAccGrp sAccGrp);
    String deleteAccGrp(String keyString);
    
    String addAccChart(String fisKeyString, SAccChart sAccChart);
    String editAccChart(String fisKeyString, SAccChart sAccChart);
    String deleteAccChart(String keyString);
    
    String setBeginning(String accChartKeyString, double beginning);

    ArrayList<SJournalHeader> getJournalListWithJT(String fisKeyString,
            String journalTypeKeyString, int month, int year);

    String addJournal(String fisKeyString, SJournalHeader sJournal);
    String editJournal(String fisKeyString, SJournalHeader sJournal);
    String deleteJournal(String fisKeyString, String keyString);
    
    HashMap<String, SAccAmt> getAccAmtMap(String fisKeyString);
    String recalculateAccAmt(String fisKeyString);
}
