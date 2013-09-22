package gwt.shared.model;

import java.io.Serializable;

public class SFinItem implements Serializable{
    
    public enum Comm {
        TXT,
        ACCNO,
        PVAR1,
        PVAR2,
        PVAR3,
        PVAR4;
    }
    
    public enum CalCon {
        CAL,
        CALIFPOS,
        CALIFNE
    }
    
    public enum PrintCon {
        PRINT,
        NOPRINT,
        NOIFZERO
    }
    
    public enum PrintStyle {
        BLANK,
        CENTER,
        ULINE,
        DULINE,
        ADULINE
    }
    
    public enum Operand {
        BLANK,
        PLUS,
        MINUS,
        CLEAR
    }
    
    private static final long serialVersionUID = 1L;
    
    private String keyString;
    private int seq;
    private Comm comm;
    private String arg;
    private CalCon calCon;
    private PrintCon printCon;
    private PrintStyle printStyle;
    private Operand var1;
    private Operand var2;
    private Operand var3;
    private Operand var4;
    
    private String accChartNo;
    
    public SFinItem() {
    
    }

    public SFinItem(String keyString, int seq, Comm comm, String arg, CalCon calCon, PrintCon printCon, PrintStyle printStyle, 
            Operand var1, Operand var2, Operand var3, Operand var4) {
        this.keyString = keyString;
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

    public String getKeyString() {
        return keyString;
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
    
    public String getAccChartNo() {
        return accChartNo;
    }

    public void setKeyString(String keyString) {
        this.keyString = keyString;
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
    
    public void setAccChartNo(String accChartNo) {
        this.accChartNo = accChartNo;
    }
}
