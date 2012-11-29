package gwt.server.account.model;

import gwt.shared.model.SFinItem.CalCon;
import gwt.shared.model.SFinItem.Comm;
import gwt.shared.model.SFinItem.Operand;
import gwt.shared.model.SFinItem.PrintCon;
import gwt.shared.model.SFinItem.PrintStyle;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@PersistenceCapable
public class FinItem {
    
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
    
    @Persistent
    private FinHeader finHeader;
    
    @Persistent
    private int seq;
    
    @Persistent
    private Comm comm;
    
    @Persistent
    private String arg;
    
    @Persistent
    private CalCon calCon;
    
    @Persistent
    private PrintCon printCon;
    
    @Persistent
    private PrintStyle printStyle;
    
    @Persistent
    private Operand var1;
    
    @Persistent
    private Operand var2;
    
    @Persistent
    private Operand var3;
    
    @Persistent
    private Operand var4;

    public FinItem(int seq, Comm comm, String arg, CalCon calCon, PrintCon printCon, PrintStyle printStyle, 
            Operand var1, Operand var2, Operand var3, Operand var4) {
        this.seq = seq;
        this.comm = comm;
        this.arg = arg;
        this.calCon = calCon;
        this.printCon = printCon;
        this.printStyle = printStyle;
        this.var1 = var1;
        this.var2 = var2;
        this.var3 = var3;
        this.var4 = var4;
    }

    public Key getKey() {
        return key;
    }
    
    public String getKeyString(){
        return KeyFactory.keyToString(key);
    }
    
    public FinHeader getFinHeader(){
        return finHeader;
    }

    public int getSeq() {
        return seq;
    }

    public Comm getComm() {
        return comm;
    }

    public String getArg() {
        return arg;
    }

    public CalCon getCalCon() {
        return calCon;
    }

    public PrintCon getPrintCon() {
        return printCon;
    }
    
    public PrintStyle getPrintStyle() {
        return printStyle;
    }

    public Operand getVar1() {
        return var1;
    }

    public Operand getVar2() {
        return var2;
    }

    public Operand getVar3() {
        return var3;
    }

    public Operand getVar4() {
        return var4;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public void setComm(Comm comm) {
        this.comm = comm;
    }

    public void setArg(String arg) {
        this.arg = arg;
    }

    public void setCalCon(CalCon calCon) {
        this.calCon = calCon;
    }

    public void setPrintCon(PrintCon printCon) {
        this.printCon = printCon;
    }
    
    public void setPrintStyle(PrintStyle printStyle) {
        this.printStyle = printStyle;
    }

    public void setVar1(Operand var1) {
        this.var1 = var1;
    }

    public void setVar2(Operand var2) {
        this.var2 = var2;
    }

    public void setVar3(Operand var3) {
        this.var3 = var3;
    }

    public void setVar4(Operand var4) {
        this.var4 = var4;
    }
    
    

    
}
