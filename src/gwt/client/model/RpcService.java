package gwt.client.model;

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

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("rpcService")
public interface RpcService extends RemoteService {
	
	SComList getComList(String sSID, String sID) throws NotLoggedInException;
	
	String addCom(String sSID, String sID, SCom sCom) throws NotLoggedInException;
	String editCom(String sSID, String sID, SCom sCom) throws NotLoggedInException;
	String deleteCom(String sSID, String sID, String keyString) throws NotLoggedInException;
	
	String addFis(String sSID, String sID, String comKeyString, SFiscalYear sFis, boolean isSetup) throws NotLoggedInException;
    String editFis(String sSID, String sID, SFiscalYear sFis) throws NotLoggedInException, IllegalArgumentException;
    String deleteFis(String sSID, String sID, String keyString) throws NotLoggedInException;
    
    SFiscalYear getSetup(String sSID, String sID, String fisKeyString) throws NotLoggedInException;
    
    String addJournalType(String sSID, String sID, String fisKeyString, SJournalType sJournalType) throws NotLoggedInException;
    String editJournalType(String sSID, String sID, SJournalType sJournalType) throws NotLoggedInException;
    String deleteJournalType(String sSID, String sID, String keyString) throws NotLoggedInException, IllegalArgumentException;
    
    String addDocType(String sSID, String sID, String fisKeyString, SDocType sDocType) throws NotLoggedInException;
    String editDocType(String sSID, String sID, SDocType sDocType) throws NotLoggedInException;
    String deleteDocType(String sSID, String sID, String keyString) throws NotLoggedInException, IllegalArgumentException;
    
    String addAccGrp(String sSID, String sID, String fisKeyString, SAccGrp sAccGrp) throws NotLoggedInException;
    String editAccGrp(String sSID, String sID, SAccGrp sAccGrp) throws NotLoggedInException;
    String deleteAccGrp(String sSID, String sID, String keyString) throws NotLoggedInException, IllegalArgumentException;
    
    String addAccChart(String sSID, String sID, String fisKeyString, SAccChart sAccChart) throws NotLoggedInException;
    String editAccChart(String sSID, String sID, SAccChart sAccChart) throws NotLoggedInException;
    String deleteAccChart(String sSID, String sID, String keyString) throws NotLoggedInException, IllegalArgumentException;
    
    String setBeginning(String sSID, String sID, String accChartKeyString, double beginning) throws NotLoggedInException;
    
    String addFinHeader(String sSID, String sID, String fisKeyString, SFinHeader sFinHeader) throws NotLoggedInException;
    String editFinHeader(String sSID, String sID, SFinHeader sFinHeader) throws NotLoggedInException;
    String deleteFinHeader(String sSID, String sID, String keyString) throws NotLoggedInException;
    
    String addFinItem(String sSID, String sID, String finHeaderKeyString, SFinItem sFinItem) throws NotLoggedInException;
    String editFinItem(String sSID, String sID, SFinItem sFinItem) throws NotLoggedInException;
    String deleteFinItem(String sSID, String sID, String keyString) throws NotLoggedInException;
    
    SFiscalYear getJournalList(String sSID, String sID, String fisKeyString) throws NotLoggedInException;
    
    String addJournal(String sSID, String sID, String fisKeyString, SJournalHeader sJournal) throws NotLoggedInException;
    String editJournal(String sSID, String sID, SJournalHeader sJournal) throws NotLoggedInException;
    String deleteJournal(String sSID, String sID, String keyString) throws NotLoggedInException;
}
