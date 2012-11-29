package gwt.server;

import gwt.client.model.RpcService;
import gwt.server.account.CreateSetup;
import gwt.server.account.Db;
import gwt.server.account.Db.DbAddCallback;
import gwt.server.account.Db.DbDeleteCallback;
import gwt.server.account.Db.DbEditCallback;
import gwt.server.account.Db.DbGetCallback;
import gwt.server.account.model.AccChart;
import gwt.server.account.model.AccGroup;
import gwt.server.account.model.Com;
import gwt.server.account.model.DocType;
import gwt.server.account.model.FinHeader;
import gwt.server.account.model.FinItem;
import gwt.server.account.model.FiscalYear;
import gwt.server.account.model.JournalHeader;
import gwt.server.account.model.JournalItem;
import gwt.server.account.model.JournalType;
import gwt.server.user.UserManager;
import gwt.server.user.model.User;
import gwt.server.user.model.UserData;
import gwt.shared.NotLoggedInException;
import gwt.shared.Utils;
import gwt.shared.model.SAccChart;
import gwt.shared.model.SAccGrp;
import gwt.shared.model.SCom;
import gwt.shared.model.SComList;
import gwt.shared.model.SDocType;
import gwt.shared.model.SFinHeader;
import gwt.shared.model.SFinItem;
import gwt.shared.model.SFiscalYear;
import gwt.shared.model.SJournalHeader;
import gwt.shared.model.SJournalItem;
import gwt.shared.model.SJournalType;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class RpcServiceImpl extends RemoteServiceServlet implements RpcService {

    @Override
    public SComList getComList(String sSID, String sID) throws NotLoggedInException {
        SComList sComList = new SComList();
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try{
            User user = UserManager.checkLoggedInAndGetUser(sSID, sID);
            Db<Com> db = new Db<Com>();
            List<Com> comList = db.get(pm, Com.class, user.getKeyString(), new DbGetCallback(){
                @Override
                public void setQuery(Query query) {
                    query.setFilter("userKey == userKeyParam");
                    query.declareParameters("com.google.appengine.api.datastore.Key userKeyParam");
                    query.setOrdering("createDate asc");
                }
            });
            if(comList.size() > 0){
                ArrayList<Key> comKeyList = new ArrayList<Key>();
                for(Com com : comList){
                    comKeyList.add(com.getKey());
                }
                
                List<FiscalYear> fisList = Db.getFiscalYears(pm, comKeyList);
                
                for(Com com : comList){
                    SCom sCom = new SCom(com.getKeyString(), com.getName(), com.getAddress(),
                            com.getTelNo(), com.getComType(), com.getTaxID(), com.getMerchantID(), com.getYearType(), com.getVatRate(), 
                            com.getCreateDate());
                    for(FiscalYear fis : fisList){
                        if(fis.getComKey().equals(com.getKey())){
                            SFiscalYear sFis = new SFiscalYear(fis.getKeyString(), fis.getBeginMonth(), fis.getBeginYear(), fis.getEndMonth(), 
                                    fis.getEndYear());
                            sCom.addSFis(sFis);
                        }
                    }
                    sComList.addSCom(sCom);
                }
            }
        }finally{
            pm.close();
        }
        return sComList;
    }

    @Override
    public String addCom(String sSID, String sID, final SCom sCom) throws NotLoggedInException {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try{
            final User user = UserManager.checkLoggedInAndGetUser(sSID, sID);
            Db<Com> db = new Db<Com>();
            Com com = db.add(pm, new DbAddCallback<Com>(){
                @Override
                public Com construct() {
                    return new Com(user.getKey(), sCom.getName(), sCom.getAddress(), sCom.getTelNo(), sCom.getComType(),
                            sCom.getTaxID(), sCom.getMerchantID(), sCom.getYearType(), sCom.getVatRate(), sCom.getCreateDate());
                }
            });
            return KeyFactory.keyToString(com.getKey());
        }finally{
            pm.close();
        }
    }

    @Override
    public String editCom(String sSID, String sID, final SCom sCom) throws NotLoggedInException {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try{
            final User user = UserManager.checkLoggedInAndGetUser(sSID, sID);
            Db<Com> db = new Db<Com>();
            Com com = db.edit(pm, Com.class, sCom.getKeyString(), new DbEditCallback<Com>(){
                @Override
                public void edit(Com t) {
                    if(!t.getUserKey().equals(user.getKey())){
                        throw new IllegalArgumentException();
                    }
                    
                    t.setName(sCom.getName());
                    t.setAddress(sCom.getAddress());
                    t.setTelNo(sCom.getTelNo());
                    t.setComType(sCom.getComType());
                    t.setTaxID(sCom.getTaxID());
                    t.setMerchantID(sCom.getMerchantID());
                    t.setYearType(sCom.getYearType());
                    t.setVatRate(sCom.getVatRate());
                }
            });
            return KeyFactory.keyToString(com.getKey());
        }finally{
            pm.close();
        }
    }

    @Override
    public String deleteCom(String sSID, String sID, String keyString) throws NotLoggedInException {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try{
            final User user = UserManager.checkLoggedInAndGetUser(sSID, sID);
            Db<Com> db = new Db<Com>();
            db.delete(pm, Com.class, keyString, new DbDeleteCallback<Com>(){
                @Override
                public void check(Com t) {
                    if(!t.getUserKey().equals(user.getKey())){
                        throw new IllegalArgumentException();
                    }
                }
            });
            
            //TODO Delete all children - fiscal year, doc type, acc grp, acc chart, journal
            
            
            return keyString;
        }finally{
            pm.close();
        }
    }

    @Override
    public String addFis(String sSID, String sID, final String comKeyString, final SFiscalYear sFis, final boolean isSetup) throws NotLoggedInException {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try{
            User user = UserManager.checkLoggedInAndGetUser(sSID, sID);
            Db<FiscalYear> db = new Db<FiscalYear>();
            FiscalYear fis =  db.add(pm, new DbAddCallback<FiscalYear>() {
                @Override
                public FiscalYear construct() {
                    return new FiscalYear(comKeyString, sFis.getBeginMonth(), sFis.getBeginYear(), sFis.getEndMonth(), sFis.getEndYear());
                }
            });
            
            if(isSetup){
                UserData userData = UserManager.getUserData(user.getKey());
                if (userData.getLang().equals("th")) {
                    CreateSetup.createInThai(pm, fis.getKeyString());
                } else {
                    CreateSetup.createInEnglish(pm, fis.getKeyString());
                }
            }
            return KeyFactory.keyToString(fis.getKey());
        }finally{
            pm.close();
        }
    }

    @Override
    public String editFis(String sSID, String sID, final SFiscalYear sFis) throws NotLoggedInException {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try{
            UserManager.checkLoggedInAndGetUser(sSID, sID);
            
            // Can be edited? Check journal date!! 
            Db<JournalHeader> jDb = new Db<JournalHeader>();
            List<JournalHeader> journalList = jDb.get(pm, JournalHeader.class, sFis.getKeyString(), new DbGetCallback(){
                @Override
                public void setQuery(Query query) {
                    query.setFilter("fisKey == fisKeyParam");
                    query.declareParameters("com.google.appengine.api.datastore.Key fisKeyParam");
                    query.setOrdering("no asc");
                }
            });
            for(JournalHeader journal : journalList){
                if(journal.compareDate(1, sFis.getBeginMonth(), sFis.getBeginYear()) < 0 || 
                        journal.compareDate(Utils.getLastDay(sFis.getEndMonth(), sFis.getEndYear()), sFis.getEndMonth(), sFis.getEndYear()) > 0){
                    throw new IllegalArgumentException("Edit aborted! Some journals are not in the new range of fiscal year!");
                }
            }
            
            Db<FiscalYear> db = new Db<FiscalYear>();
            FiscalYear fis = db.edit(pm, FiscalYear.class, sFis.getKeyString(), new DbEditCallback<FiscalYear>() {
                @Override
                public void edit(FiscalYear fis) {
                    fis.setBeginMonth(sFis.getBeginMonth());
                    fis.setBeginYear(sFis.getBeginYear());
                    fis.setEndMonth(sFis.getEndMonth());
                    fis.setEndYear(sFis.getEndYear());
                }
            });
            return KeyFactory.keyToString(fis.getKey());
        }finally{
            pm.close();
        }
    }

    @Override
    public String deleteFis(String sSID, String sID, String keyString) throws NotLoggedInException {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try{
            UserManager.checkLoggedInAndGetUser(sSID, sID);
            Db<FiscalYear> db = new Db<FiscalYear>();
            db.delete(pm, FiscalYear.class, keyString, new DbDeleteCallback<FiscalYear>() {
                @Override
                public void check(FiscalYear t) {
                    
                }
            });
            return keyString;
        }finally{
            pm.close();
        }
        
        //TODO Delete all children - doc type, acc grp, acc chart, journal
        
    }

    @Override
    public SFiscalYear getSetup(String sSID, String sID, final String fisKeyString) throws NotLoggedInException {
        SFiscalYear sFis = new SFiscalYear();
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try{
            UserManager.checkLoggedInAndGetUser(sSID, sID);
            Db<JournalType> dbJour = new Db<JournalType>();
            List<JournalType> journalTypeList = dbJour.get(pm, JournalType.class, fisKeyString, new DbGetCallback() {
                @Override
                public void setQuery(Query query) {
                    query.setFilter("fisKey == fisKeyParam");
                    query.declareParameters("com.google.appengine.api.datastore.Key fisKeyParam");
                    query.setOrdering("createDate asc");
                }
            });
            if(journalTypeList.size() > 0){
                for(JournalType journalType : journalTypeList){
                    SJournalType sJournalType = new SJournalType(journalType.getKeyString(), journalType.getName(), 
                            journalType.getShortName(), journalType.getCreateDate());
                    sFis.addSJournalType(sJournalType);
                }
            }
            
            Db<DocType> dbDocType = new Db<DocType>();
            List<DocType> docTypeList = dbDocType.get(pm, DocType.class, fisKeyString, new DbGetCallback() {
                @Override
                public void setQuery(Query query) {
                    query.setFilter("fisKey == fisKeyParam");
                    query.declareParameters("com.google.appengine.api.datastore.Key fisKeyParam");
                    query.setOrdering("createDate asc");
                }
            });
            if(docTypeList.size() > 0){
                for(DocType docType : docTypeList){
                    SDocType sDocType = new SDocType(docType.getKeyString(), docType.getJournalTypeKeyString(), docType.getCode(), docType.getName(), 
                            docType.getJournalDesc(), docType.getCreateDate());
                    sFis.addSDocType(sDocType);
                    
                }
            }
            
            Db<AccGroup> dbAccGrp = new Db<AccGroup>();
            List<AccGroup> accGrpList = dbAccGrp.get(pm, AccGroup.class, fisKeyString, new DbGetCallback() {
                @Override
                public void setQuery(Query query) {
                    query.setFilter("fisKey == fisKeyParam");
                    query.declareParameters("com.google.appengine.api.datastore.Key fisKeyParam");
                    query.setOrdering("createDate asc");
                }
            });
            if(accGrpList.size() > 0){
                for(AccGroup accGrp : accGrpList){
                    SAccGrp sAccGrp = new SAccGrp(accGrp.getKeyString(), accGrp.getName(), accGrp.getCreateDate());
                    sFis.addSAccGrp(sAccGrp);
                }
            }
            
            Db<AccChart> dbAccChart = new Db<AccChart>();
            List<AccChart> accChartList = dbAccChart.get(pm, AccChart.class, fisKeyString, new DbGetCallback() {
                @Override
                public void setQuery(Query query) {
                    query.setFilter("fisKey == fisKeyParam");
                    query.declareParameters("com.google.appengine.api.datastore.Key fisKeyParam");
                }
            });
            if(accChartList.size() > 0){
                for(AccChart accChart : accChartList){
                    SAccChart sAccChart = new SAccChart(accChart.getKeyString(), accChart.getAccGroupKeyString(), 
                            accChart.getParentAccChartKeyString(), accChart.getNo(), accChart.getName(), accChart.getType(), accChart.getLevel(), 
                            accChart.getBeginning());
                    sFis.addSAccChart(sAccChart);
                }
            }
            
            Db<FinHeader> db = new Db<FinHeader>();
            List<FinHeader> finList = db.get(pm, FinHeader.class, fisKeyString, new DbGetCallback() {
                @Override
                public void setQuery(Query query) {
                    query.setFilter("fisKey == fisKeyParam");
                    query.declareParameters("com.google.appengine.api.datastore.Key fisKeyParam");
                    query.setOrdering("createDate asc");
                }
            });
            if(finList.size() > 0){
                for(FinHeader finHeader : finList){
                    SFinHeader sFinHeader = new SFinHeader(finHeader.getKeyString(),
                            finHeader.getName(), finHeader.getCreateDate());
                    
                    for(FinItem finItem : finHeader.getItemSet()){
                        SFinItem sFinItem = new SFinItem(finItem.getKeyString(),
                                finItem.getSeq(), finItem.getComm(), finItem.getArg(), 
                                finItem.getCalCon(), finItem.getPrintCon(),
                                finItem.getPrintStyle(), finItem.getVar1(), 
                                finItem.getVar2(), finItem.getVar3(), finItem.getVar4());
                        sFinHeader.addSFinItem(sFinItem);
                    }
                    
                    sFis.addSFinHeader(sFinHeader);
                }
            }
        }finally{
            pm.close();
        }
        return sFis;
    }

    @Override
    public String addJournalType(String sSID, String sID, final String fisKeyString, final SJournalType sJournalType) throws NotLoggedInException {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try{
            UserManager.checkLoggedInAndGetUser(sSID, sID);
            Db<JournalType> db = new Db<JournalType>();
            JournalType journalType = db.add(pm, new DbAddCallback<JournalType>() {
                @Override
                public JournalType construct() {
                    return new JournalType(fisKeyString, sJournalType.getName(), sJournalType.getShortName(), sJournalType.getCreateDate());
                }
            });
            return KeyFactory.keyToString(journalType.getKey());
        }finally{
            pm.close();
        }
    }

    @Override
    public String editJournalType(String sSID, String sID, final SJournalType sJournalType) throws NotLoggedInException {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try{
            UserManager.checkLoggedInAndGetUser(sSID, sID);
            Db<JournalType> db = new Db<JournalType>();
            JournalType journalType = db.edit(pm, JournalType.class, sJournalType.getKeyString(), new DbEditCallback<JournalType>() {
                @Override
                public void edit(JournalType t) {
                    t.setName(sJournalType.getName());
                    t.setShortName(sJournalType.getShortName());
                }
            });
            return KeyFactory.keyToString(journalType.getKey());
        }finally{
            pm.close();
        }
    }

    @Override
    public String deleteJournalType(String sSID, String sID, String keyString) throws NotLoggedInException {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try{
            UserManager.checkLoggedInAndGetUser(sSID, sID);
            
            // Check can be deleted.
            Db<DocType> dDb = new Db<DocType>();
            Key dKey = dDb.getSingleKey(pm, DocType.class, keyString, new DbGetCallback() {
                @Override
                public void setQuery(Query query) {
                    query.setFilter("journalTypeKey == journalTypeKeyParam");
                    query.declareParameters("com.google.appengine.api.datastore.Key journalTypeKeyParam");
                }
            });
            if(dKey != null){
                throw new IllegalArgumentException("Deletion abort! This journal type is still used by some doc types.");
            }
            
            // Delete
            Db<JournalType> db = new Db<JournalType>();
            db.delete(pm, JournalType.class, keyString, new DbDeleteCallback<JournalType>() {
                @Override
                public void check(JournalType t) {

                }
            });
            
            return keyString;
        }finally{
            pm.close();
        }
    }
    
    @Override
    public String addDocType(String sSID, String sID, final String fisKeyString, final SDocType sDocType) throws NotLoggedInException {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try{
            UserManager.checkLoggedInAndGetUser(sSID, sID);
            Db<DocType> db = new Db<DocType>();
            DocType docType = db.add(pm, new DbAddCallback<DocType>() {
                @Override
                public DocType construct() {
                    return new DocType(fisKeyString, sDocType.getJournalTypeKeyString(), sDocType.getCode(),
                            sDocType.getName(), sDocType.getJournalDesc(), sDocType.getCreateDate());
                }
            });
            return KeyFactory.keyToString(docType.getKey());
        }finally{
            pm.close();
        }
    }

    @Override
    public String editDocType(String sSID, String sID, final SDocType sDocType) throws NotLoggedInException {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try{
            UserManager.checkLoggedInAndGetUser(sSID, sID);
            Db<DocType> db = new Db<DocType>();
            DocType docType = db.edit(pm, DocType.class, sDocType.getKeyString(), new DbEditCallback<DocType>() {
                @Override
                public void edit(DocType docType) {
                    docType.setJournalTypeKey(sDocType.getJournalTypeKeyString());
                    docType.setCode(sDocType.getCode());
                    docType.setName(sDocType.getName());
                    docType.setJournalDesc(sDocType.getJournalDesc());
                }
            });
            return KeyFactory.keyToString(docType.getKey());
        }finally{
            pm.close();
        }
    }

    @Override
    public String deleteDocType(String sSID, String sID, final String keyString) throws NotLoggedInException {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try{
            UserManager.checkLoggedInAndGetUser(sSID, sID);
            
            // Check can be deleted.
            Db<JournalHeader> jDb = new Db<JournalHeader>();
            Key jKey = jDb.getSingleKey(pm, JournalHeader.class, keyString, new DbGetCallback() {
                @Override
                public void setQuery(Query query) {
                    query.setFilter("docTypeKey == docTypeKeyParam");
                    query.declareParameters("com.google.appengine.api.datastore.Key docTypeKeyParam");
                }
            });
            if(jKey != null){
                throw new IllegalArgumentException("Deletion abort! This doc type is still used by some journals.");
            }
            
            // Delete
            Db<DocType> db = new Db<DocType>();
            db.delete(pm, DocType.class, keyString, new DbDeleteCallback<DocType>() {
                @Override
                public void check(DocType t) {
                    
                }
            });
            return keyString;
        }finally{
            pm.close();
        }
    }

    @Override
    public String addAccGrp(String sSID, String sID, final String fisKeyString, final SAccGrp sAccGrp) throws NotLoggedInException {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try{
            UserManager.checkLoggedInAndGetUser(sSID, sID);
            Db<AccGroup> db = new Db<AccGroup>();
            AccGroup accGrp = db.add(pm, new DbAddCallback<AccGroup>() {
                @Override
                public AccGroup construct() {
                    return new AccGroup(fisKeyString, sAccGrp.getName(), sAccGrp.getCreateDate());
                }
            });
            return KeyFactory.keyToString(accGrp.getKey());
        }finally{
            pm.close();
        }
    }

    @Override
    public String editAccGrp(String sSID, String sID, final SAccGrp sAccGrp) throws NotLoggedInException {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try{
            UserManager.checkLoggedInAndGetUser(sSID, sID);
            Db<AccGroup> db = new Db<AccGroup>();
            AccGroup accGrp = db.edit(pm, AccGroup.class, sAccGrp.getKeyString(), new DbEditCallback<AccGroup>() {
                @Override
                public void edit(AccGroup t) {
                    t.setName(sAccGrp.getName());
                }
            });
            return KeyFactory.keyToString(accGrp.getKey());
        }finally{
            pm.close();
        }
    }

    @Override
    public String deleteAccGrp(String sSID, String sID, String keyString) throws NotLoggedInException {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try{
            UserManager.checkLoggedInAndGetUser(sSID, sID);
            
            // Check can be deleted.
            Db<AccChart> aDb = new Db<AccChart>();
            Key aKey = aDb.getSingleKey(pm, AccChart.class, keyString, new DbGetCallback() {
                @Override
                public void setQuery(Query query) {
                    query.setFilter("accGroupKey == accGroupKeyParam");
                    query.declareParameters("com.google.appengine.api.datastore.Key accGroupKeyParam");
                }
            });
            if(aKey != null){
                throw new IllegalArgumentException("Deletion abort! This acc group is still used in acc chart.");
            }
            
            // Delete
            Db<AccGroup> db = new Db<AccGroup>();
            db.delete(pm, AccGroup.class, keyString, new DbDeleteCallback<AccGroup>() {
                @Override
                public void check(AccGroup t) {
                    
                }
            });
            return keyString;
        }finally{
            pm.close();
        }
    }

    @Override
    public String addAccChart(String sSID, String sID, final String fisKeyString, final SAccChart sAccChart) throws NotLoggedInException {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try{
            UserManager.checkLoggedInAndGetUser(sSID, sID);
            Db<AccChart> db = new Db<AccChart>();
            AccChart accChart = db.add(pm, new DbAddCallback<AccChart>() {
                @Override
                public AccChart construct() {
                    return new AccChart(fisKeyString, sAccChart.getAccGroupKeyString(), sAccChart.getParentAccChartKeyString(), sAccChart.getNo(), 
                            sAccChart.getName(), sAccChart.getType(), sAccChart.getLevel(), sAccChart.getBeginning());
                }
            });
            return KeyFactory.keyToString(accChart.getKey());
        }finally{
            pm.close();
        }
    }

    @Override
    public String editAccChart(String sSID, String sID, final SAccChart sAccChart) throws NotLoggedInException {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try{
            UserManager.checkLoggedInAndGetUser(sSID, sID);
            Db<AccChart> db = new Db<AccChart>();
            AccChart accChart = db.edit(pm, AccChart.class, sAccChart.getKeyString(), new DbEditCallback<AccChart>() {
                @Override
                public void edit(AccChart accChart) {
                    accChart.setAccGroupKey(sAccChart.getAccGroupKeyString());
                    accChart.setParentAccChartKey(sAccChart.getParentAccChartKeyString());
                    accChart.setNo(sAccChart.getNo());
                    accChart.setName(sAccChart.getName());
                    accChart.setType(sAccChart.getType());
                    accChart.setLevel(sAccChart.getLevel());
                    accChart.setBeginning(sAccChart.getBeginning());
                }
            });
            return KeyFactory.keyToString(accChart.getKey());
        }finally{
            pm.close();
        }
    }

    @Override
    public String deleteAccChart(String sSID, String sID, String keyString) throws NotLoggedInException {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try{
            UserManager.checkLoggedInAndGetUser(sSID, sID);
            
            // Check can be deleted.
            Db<JournalItem> jDb = new Db<JournalItem>();
            Key jKey = jDb.getSingleKey(pm, JournalItem.class, keyString, new DbGetCallback() {
                @Override
                public void setQuery(Query query) {
                    query.setFilter("accChartKey == accChartKeyParam");
                    query.declareParameters("com.google.appengine.api.datastore.Key accChartKeyParam");
                }
            });
            if(jKey != null){
                throw new IllegalArgumentException("Deletion abort! This acc chart is still used by some journals.");
            }
            
            // Delete
            Db<AccChart> db = new Db<AccChart>();
            db.delete(pm, AccChart.class, keyString, new DbDeleteCallback<AccChart>() {
                @Override
                public void check(AccChart t) {
                    
                }
            });
            return keyString;
        }finally{
            pm.close();
        }
    }

    @Override
    public String setBeginning(String sSID, String sID, String accChartKeyString, double beginning) throws NotLoggedInException {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try{
            UserManager.checkLoggedInAndGetUser(sSID, sID);
            AccChart accChart = Db.setBeginning(pm, accChartKeyString, beginning);
            return KeyFactory.keyToString(accChart.getKey());
        }finally{
            pm.close();
        }
    }
    
    @Override
    public String addFinHeader(String sSID, String sID, String fisKeyString, SFinHeader sFinHeader) throws NotLoggedInException {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try{
            UserManager.checkLoggedInAndGetUser(sSID, sID);
            FinHeader finHeader = Db.addFinHeader(pm, fisKeyString, sFinHeader);
            return KeyFactory.keyToString(finHeader.getKey());
        }finally{
            pm.close();
        }
    }

    @Override
    public String editFinHeader(String sSID, String sID, SFinHeader sFinHeader) throws NotLoggedInException {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try{
            UserManager.checkLoggedInAndGetUser(sSID, sID);
            FinHeader finHeader = Db.editFinHeader(pm, sFinHeader);
            return KeyFactory.keyToString(finHeader.getKey());
        }finally{
            pm.close();
        }
    }

    @Override
    public String deleteFinHeader(String sSID, String sID, String keyString) throws NotLoggedInException {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try{
            UserManager.checkLoggedInAndGetUser(sSID, sID);
            Db<FinHeader> db = new Db<FinHeader>();
            db.delete(pm, FinHeader.class, keyString, new DbDeleteCallback<FinHeader>() {
                @Override
                public void check(FinHeader t) {
                    
                }
            });
            return keyString;
        }finally{
            pm.close();
        }
    }
    
    @Override
    public String addFinItem(String sSID, String sID, String finHeaderKeyString, SFinItem sFinItem) throws NotLoggedInException {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try{
            UserManager.checkLoggedInAndGetUser(sSID, sID);
            FinItem finItem= Db.addFinItem(pm, finHeaderKeyString, sFinItem);
            return KeyFactory.keyToString(finItem.getKey());
        }finally{
            pm.close();
        }
    }

    @Override
    public String editFinItem(String sSID, String sID, SFinItem sFinItem) throws NotLoggedInException {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try{
            UserManager.checkLoggedInAndGetUser(sSID, sID);
            FinItem finItem = Db.editFinItem(pm, sFinItem);
            return KeyFactory.keyToString(finItem.getKey());
        }finally{
            pm.close();
        }
    }

    @Override
    public String deleteFinItem(String sSID, String sID, String keyString) throws NotLoggedInException {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try{
            UserManager.checkLoggedInAndGetUser(sSID, sID);
            Db<FinItem> db = new Db<FinItem>();
            db.delete(pm, FinItem.class, keyString, new DbDeleteCallback<FinItem>() {
                @Override
                public void check(FinItem t) {
                    
                }
            });
            return keyString;
        }finally{
            pm.close();
        }
    }

    @Override
    public SFiscalYear getJournalList(String sSID, String sID, final String fisKeyString) throws NotLoggedInException {
        SFiscalYear sFis = new SFiscalYear();
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try{
            UserManager.checkLoggedInAndGetUser(sSID, sID);
            Db<JournalHeader> db = new Db<JournalHeader>();
            List<JournalHeader> journalList = db.get(pm, JournalHeader.class, fisKeyString, new DbGetCallback() {
                @Override
                public void setQuery(Query query) {
                    query.setFilter("fisKey == fisKeyParam");
                    query.declareParameters("com.google.appengine.api.datastore.Key fisKeyParam");
                    query.setOrdering("no asc");
                }
            });
            if(journalList.size() > 0){
                for(JournalHeader journal : journalList){
                    SJournalHeader sJournal = new SJournalHeader(journal.getKeyString(), journal.getDocTypeKeyString(), journal.getNo(), 
                            journal.getDay(), journal.getMonth(), journal.getYear(), journal.getDesc());
                    
                    for(JournalItem journalItem : journal.getItemSet()){
                        SJournalItem sJournalItem = new SJournalItem(journalItem.getKeyString(), journalItem.getAccChartKeyString(),
                                journalItem.getAmt(), journalItem.getCreateDate());
                        sJournal.addItem(sJournalItem);
                    }
                    
                    sFis.addSJournal(sJournal);
                }
            }
        }finally{
            pm.close();
        }
        return sFis;
    }

    @Override
    public String addJournal(String sSID, String sID, String fisKeyString, SJournalHeader sJournal) throws NotLoggedInException {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try{
            UserManager.checkLoggedInAndGetUser(sSID, sID);
            JournalHeader journal = Db.addJournal(pm, fisKeyString, sJournal);
            return KeyFactory.keyToString(journal.getKey());
        }finally{
            pm.close();
        }
    }

    @Override
    public String editJournal(String sSID, String sID, SJournalHeader sJournal) throws NotLoggedInException {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try{
            UserManager.checkLoggedInAndGetUser(sSID, sID);
            JournalHeader journal = Db.editJournal(pm, sJournal);
            return KeyFactory.keyToString(journal.getKey());
        }finally{
            pm.close();
        }
    }

    @Override
    public String deleteJournal(String sSID, String sID, String keyString) throws NotLoggedInException {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try{
            UserManager.checkLoggedInAndGetUser(sSID, sID);
            Db<JournalHeader> db = new Db<JournalHeader>();
            db.delete(pm, JournalHeader.class, keyString, new DbDeleteCallback<JournalHeader>() {
                @Override
                public void check(JournalHeader t) {
                    
                }
            });
            return keyString;
        }finally{
            pm.close();
        }
    }

}
