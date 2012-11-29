package gwt.server.account.model;

import gwt.shared.model.SAccChart.AccType;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@PersistenceCapable
public class AccChart {
    
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
    
    @Persistent
    private Key fisKey;
    
    @Persistent
    private Key accGroupKey;
    
    @Persistent
    private Key parentAccChartKey;
    
    @Persistent
    private String no;
    
    @Persistent
    private String name;
    
    @Persistent
    private AccType type;
    
    @Persistent
    private int level;
    
    @Persistent
    private double beginning;

    public AccChart(String fisKeyString, String accGroupKeyString, String parentAccChartKeyString, String no, String name, 
            AccType type, int level, double beginning) {
        this.fisKey = KeyFactory.stringToKey(fisKeyString);
        this.accGroupKey = accGroupKeyString == null ? null : KeyFactory.stringToKey(accGroupKeyString);
        this.parentAccChartKey = parentAccChartKeyString ==null ? null : KeyFactory.stringToKey(parentAccChartKeyString);
        this.no = no;
        this.name = name;
        this.type = type;
        this.level = level;
        this.beginning = beginning;
    }

    public Key getKey() {
        return key;
    }
    
    public String getKeyString() {
        return KeyFactory.keyToString(key);
    }
    
    public Key getFisKey() {
        return fisKey;
    }

    public Key getAccGroupKey() {
        return accGroupKey;
    }
    
    public String getAccGroupKeyString() {
        return accGroupKey == null ? null : KeyFactory.keyToString(accGroupKey);
    }

    public Key getParentAccChartKey() {
        return parentAccChartKey;
    }
    
    public String getParentAccChartKeyString() {
        return parentAccChartKey == null ? null : KeyFactory.keyToString(parentAccChartKey);
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

    public void setAccGroupKey(String accGrpKeyString) {
        this.accGroupKey = accGrpKeyString == null ? null : KeyFactory.stringToKey(accGrpKeyString);
    }

    public void setParentAccChartKey(String parentAccChartKeyString) {
        this.parentAccChartKey = parentAccChartKeyString == null ? null : KeyFactory.stringToKey(parentAccChartKeyString);
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
    
    
}
