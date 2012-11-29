package gwt.server.account;

import gwt.server.account.model.AccChart;
import gwt.server.account.model.FinHeader;
import gwt.server.account.model.FinItem;
import gwt.server.account.model.FiscalYear;
import gwt.server.account.model.JournalHeader;
import gwt.server.account.model.JournalItem;
import gwt.shared.model.SFinHeader;
import gwt.shared.model.SFinItem;
import gwt.shared.model.SJournalHeader;
import gwt.shared.model.SJournalItem;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class Db<T> {
    
    public interface DbGetCallback {
        void setQuery(Query query);
    }
    
    @SuppressWarnings("unchecked")
    public List<T> get(PersistenceManager pm, Class<T> cl, String keyString, DbGetCallback callback){
        Key key = KeyFactory.stringToKey(keyString);
        List<T> list = null;
        Query query = pm.newQuery(cl);
        callback.setQuery(query);
        try {
            list = (List<T>) query.execute(key);
        } finally {
            query.closeAll();
        }
        return list;
    }
    
    @SuppressWarnings("unchecked")
    public Key getSingleKey(PersistenceManager pm, Class<T> cl, String keyString, DbGetCallback callback){
        Key key = KeyFactory.stringToKey(keyString);
        List<Key> list = null;
        Query query = pm.newQuery("select key from " + cl.getName());
        callback.setQuery(query);
        try {
            list = (List<Key>)query.execute(key);
        } finally {
            query.closeAll();
        }
        return list.isEmpty() ? null : list.get(0);
    }
    
    public interface DbAddCallback<T> {
        T construct();
    }
    
    public T add(PersistenceManager pm, DbAddCallback<T> callback){
        T t = callback.construct();
        pm.makePersistent(t);
        return t;
    }
    
    public interface DbEditCallback<T> {
        void edit(T t);
    }
    
    public T edit(PersistenceManager pm, Class<T> cl, String keyString, DbEditCallback<T> callback){
        Key key = KeyFactory.stringToKey(keyString);
        T t = pm.getObjectById(cl, key);
        callback.edit(t);
        return t;
    }
    
    public interface DbDeleteCallback<T>{
        void check(T t);
    }
    
    public String delete(PersistenceManager pm, Class<T> cl, String keyString, DbDeleteCallback<T> callback){
        Key key = KeyFactory.stringToKey(keyString);
        T t = pm.getObjectById(cl, key);
        callback.check(t);
        pm.deletePersistent(t);
        return keyString;
    }
    
    @SuppressWarnings("unchecked")
    public static List<FiscalYear> getFiscalYears(PersistenceManager pm, List<Key> comKeyList){
        List<FiscalYear> fisList = null;        
        Query query = pm.newQuery(FiscalYear.class);
        query.setFilter(":p.contains(comKey)");
        query.setOrdering("beginYear beginMonth asc");
        try {
            fisList = (List<FiscalYear>)query.execute(comKeyList);
        } finally {
            query.closeAll();
        }
        return fisList;
    }
    
    public static AccChart setBeginning(PersistenceManager pm, String accChartKeyString, double beginning){
        Key key = KeyFactory.stringToKey(accChartKeyString);
        AccChart accChart = pm.getObjectById(AccChart.class, key);
        
        accChart.setBeginning(beginning);
        
        return accChart;
    }
    
    public static FinHeader addFinHeader(PersistenceManager pm,
            String fisKeyString, SFinHeader sFinHeader){
        FinHeader finHeader = new FinHeader(fisKeyString, sFinHeader.getName(),
                sFinHeader.getCreateDate());
        for(SFinItem sFinItem : sFinHeader.getSFinItemList()){
            FinItem finItem = new FinItem(sFinItem.getSeq(), sFinItem.getComm(),
                    sFinItem.getArg(), sFinItem.getCalCon(), sFinItem.getPrintCon(),
                    sFinItem.getPrintStyle(), sFinItem.getVar1(), sFinItem.getVar2(),
                    sFinItem.getVar3(), sFinItem.getVar4());
                    finHeader.addItem(finItem);
        }
        pm.makePersistent(finHeader);
        return finHeader;
    }
    
    public static FinHeader editFinHeader(PersistenceManager pm,
            SFinHeader sFinHeader){
        Key key = KeyFactory.stringToKey(sFinHeader.getKeyString());
        FinHeader finHeader = pm.getObjectById(FinHeader.class, key);
        finHeader.setName(sFinHeader.getName());
        return finHeader;
    }
    
    public static FinItem addFinItem(PersistenceManager pm,
            String finHeaderKeyString, SFinItem sFinItem){
        Key finHeaderKey = KeyFactory.stringToKey(finHeaderKeyString);
        FinHeader finHeader = pm.getObjectById(FinHeader.class, finHeaderKey);
        FinItem finItem = new FinItem(sFinItem.getSeq(), sFinItem.getComm(),
                sFinItem.getArg(), sFinItem.getCalCon(), sFinItem.getPrintCon(),
                sFinItem.getPrintStyle(), sFinItem.getVar1(), sFinItem.getVar2(),
                sFinItem.getVar3(), sFinItem.getVar4()); 
        finHeader.addItem(finItem);
        return finItem;
    }
    
    public static FinItem editFinItem(PersistenceManager pm, SFinItem sFinItem){
        Key key = KeyFactory.stringToKey(sFinItem.getKeyString());
        FinItem finItem = pm.getObjectById(FinItem.class, key);
        finItem.setSeq(sFinItem.getSeq());
        finItem.setComm(sFinItem.getComm());
        finItem.setArg(sFinItem.getArg());
        finItem.setCalCon(sFinItem.getCalCon());
        finItem.setPrintCon(sFinItem.getPrintCon());
        finItem.setPrintStyle(sFinItem.getPrintStyle());
        finItem.setVar1(sFinItem.getVar1());
        finItem.setVar2(sFinItem.getVar2());
        finItem.setVar3(sFinItem.getVar3());
        finItem.setVar4(sFinItem.getVar4());
        return finItem;
    }
    
    public static JournalHeader addJournal(PersistenceManager pm, String fisKeyString, SJournalHeader sJournal){
        JournalHeader journal = new JournalHeader(fisKeyString, sJournal.getDocTypeKeyString(), sJournal.getNo(), 
                sJournal.getDay(), sJournal.getMonth(), sJournal.getYear(), sJournal.getDesc());
        
        for(SJournalItem sJournalItem : sJournal.getItemList()){
            JournalItem journalItem = new JournalItem(sJournalItem.getAccChartKeyString(), sJournalItem.getAmt(),
                    sJournalItem.getCreateDate());
            journal.addItem(journalItem);
        }
        
        pm.makePersistent(journal);
        return journal;
    }
    
    public static JournalHeader editJournal(PersistenceManager pm, SJournalHeader sJournal){
        Key key = KeyFactory.stringToKey(sJournal.getKeyString());
        JournalHeader journal = pm.getObjectById(JournalHeader.class, key);
        
        journal.setDocTypeKey(sJournal.getDocTypeKeyString());
        journal.setNo(sJournal.getNo());
        journal.setDay(sJournal.getDay());
        journal.setMonth(sJournal.getMonth());
        journal.setYear(sJournal.getYear());
        journal.setDesc(sJournal.getDesc());
        
        journal.clearItemSet();
        for(SJournalItem sJournalItem : sJournal.getItemList()){
            JournalItem journalItem = new JournalItem(sJournalItem.getAccChartKeyString(), sJournalItem.getAmt(),
                    sJournalItem.getCreateDate());
            journal.addItem(journalItem);
        }
        
        return journal;
    }
}
