package gwt.shared.model;

import java.io.Serializable;

public class SAccChart implements Serializable{

    public enum AccType {
        CONTROL,
        ENTRY;
    }
    
	private static final long serialVersionUID = 1L;
	
	private String keyString;
	private String accGroupKeyString;
    private String parentAccChartKeyString;
    private String no;
    private String name;
    private AccType type;
    private int level;
    private double beginning;
    
    private String accGroupName;
    private String parentAccChartNo;
	
	public SAccChart(){
		
	}

    public SAccChart(String keyString, String accGroupKeyString, String parentAccChartKeyString, String no, String name, AccType type,
            int level, double beginning) {
        this.keyString = keyString;
        this.accGroupKeyString = accGroupKeyString;
        this.parentAccChartKeyString = parentAccChartKeyString;
        this.no = no;
        this.name = name;
        this.type = type;
        this.level = level;
        this.beginning = beginning;
    }

    public String getKeyString() {
        return keyString;
    }

    public String getAccGroupKeyString() {
        return accGroupKeyString;
    }

    public String getParentAccChartKeyString() {
        return parentAccChartKeyString;
    }

    public String getNo() {
        return no;
    }

    public String getName() {
        return name;
    }

    public AccType getType() {
        return type;
    }

    public int getLevel() {
        return level;
    }

    public double getBeginning() {
        return beginning;
    }
    
    public String getAccGroupName() {
        return accGroupName;
    }

    public String getParentAccChartNo() {
        return parentAccChartNo;
    }

    public void setKeyString(String keyString) {
        this.keyString = keyString;
    }

    public void setAccGroupKeyString(String accGroupKeyString) {
        this.accGroupKeyString = accGroupKeyString;
    }

    public void setParentAccChartKeyString(String parentAccChartKeyString) {
        this.parentAccChartKeyString = parentAccChartKeyString;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(AccType type) {
        this.type = type;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setBeginning(double beginning) {
        this.beginning = beginning;
    }
	
    public void setAccGroupName(String accGroupName) {
        this.accGroupName = accGroupName;
    }

    public void setParentAccChartNo(String parentAccChartNo) {
        this.parentAccChartNo = parentAccChartNo;
    }
}
