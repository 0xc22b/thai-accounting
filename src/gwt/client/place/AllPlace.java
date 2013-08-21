package gwt.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class AllPlace extends Place {

    public static final String COM = "com";
    public static final String FIS = "fis";
    public static final String MENU = "menu";
    public static final String JOURT = "jourt";
    public static final String DOC = "doc";
    public static final String GRP = "grp";
    public static final String CHART = "chart";
    public static final String BEGIN = "begin";
    public static final String FIN_HEADER = "fin_header";
    public static final String FIN_ITEM = "fin_item";
    public static final String JOUR = "jour";
    
    public static final String NEW = "new";
    public static final String EDIT = "edit";
    public static final String VIEW = "view";
    public static final String LIST = "list";
    
    public static final String SETUP = "setup";
    //public static final String JOUR = "jour";
    public static final String REPORT = "report";
    
    //public static final String CHART = "chart";
    //public static final String JOUR = "jour";
    public static final String LEDGER = "ledger";
    public static final String TRIAL = "trial";
    public static final String BALANCE = "balance";
    public static final String PROFIT = "profit";
    public static final String COST = "cost";
    public static final String WORK_SHEET = "work_sheet";
    public static final String FIN = "fin";

    // Used by AccChartView
    //     whether to create a new account chart as a child or a sibling.
    public static final String CHILD = "child";
    public static final String SIBLING = "sibling";

    // Used by ReportView
    //     whether to begin acc no. from first and end acc no. to last.
    public static final String FIRST = "first";
    public static final String LAST = "last";
    
    // Used by ReportView
    //     whether to show all.
    public static final String SHOW_ALL = "show_all";
    
    public static class Tokenizer implements PlaceTokenizer<AllPlace> {

        @Override
        public String getToken(AllPlace place) {
            return place.getToken();
        }

        @Override
        public AllPlace getPlace(String token) {
            return new AllPlace(token);
        }
    }
    
    private String token;

    public AllPlace(String token) {
        this.token = token;
    }
    
    public AllPlace(String place, String action){
        this.token = place + "/" + action;
    }
    
    public AllPlace(String place, String action, String comKeyString){
        this.token = place + "/" + action + "/" + comKeyString;
    }
    
    public AllPlace(String place, String action, String comKeyString, String fisKeyString){
        this.token = place + "/" + action + "/" + comKeyString + "/" + fisKeyString;
    }
    
    public AllPlace(String place, String action, String comKeyString, String fisKeyString, String keyString){
        this.token = place + "/" + action + "/" + comKeyString + "/" + fisKeyString + "/" + keyString;
    }
    
    public AllPlace(String place, String action, String comKeyString, String fisKeyString, String keyString, String keyString2){
        this.token = place + "/" + action + "/" + comKeyString + "/" + fisKeyString + "/" + keyString + "/" + keyString2;
    }
    
    public AllPlace(String place, String action, String comKeyString,
            String fisKeyString, String keyString, String keyString2,
            String keyString3){
        this.token = place + "/" + action + "/" + comKeyString + "/"
                + fisKeyString + "/" + keyString + "/" + keyString2 + "/"
                + keyString3;
    }
    
    public AllPlace(String place, String action, String comKeyString, String fisKeyString, String keyString, String keyString2, String keyString3,
            String keyString4, String keyString5, String keyString6){
        this.token = place + "/" + action + "/" + comKeyString + "/" + fisKeyString + "/" + keyString + "/" + keyString2 + "/" + keyString3 +
                "/" + keyString4 + "/" + keyString5 + "/" + keyString6;
    }
    
    public AllPlace(String place, String action, String comKeyString, String fisKeyString, String keyString, String keyString2, String keyString3,
            String keyString4, String keyString5, String keyString6, String keyString7){
        this.token = place + "/" + action + "/" + comKeyString + "/" + fisKeyString + "/" + keyString + "/" + keyString2 + "/" + keyString3 +
                "/" + keyString4 + "/" + keyString5 + "/" + keyString6 + "/" + keyString7;
    }
    
    public AllPlace(String place, String action, String comKeyString, String fisKeyString, String keyString, String keyString2, String keyString3,
            String keyString4, String keyString5, String keyString6, String keyString7, String keyString8){
        this.token = place + "/" + action + "/" + comKeyString + "/" + fisKeyString + "/" + keyString + "/" + keyString2 + "/" + keyString3 +
                "/" + keyString4 + "/" + keyString5 + "/" + keyString6 + "/" + keyString7 + "/" + keyString8;
    }
    
    public AllPlace(String place, String action, String comKeyString,
            String fisKeyString, String keyString, String keyString2,
            String keyString3, String keyString4, String keyString5,
            String keyString6, String keyString7, String keyString8,
            String keyString9){
        this.token = place + "/" + action + "/" + comKeyString + "/" + fisKeyString + "/" + keyString + "/" + keyString2 + "/" + keyString3 +
                "/" + keyString4 + "/" + keyString5 + "/" + keyString6 + "/" + keyString7 + "/" + keyString8 + "/" + keyString9;
    }

    public String getToken() {
        return this.token;
    }
    
    public String getPlace(){
        return token.split("/")[0]; 
    }
    
    public String getAction(){
        return token.split("/")[1];
    }
    
    public String getComKeyString(){
        return token.split("/")[2];
    }
    
    public String getFisKeyString(){
        return token.split("/")[3];
    }
    
    public String getKeyString(){
        try{
            return token.split("/")[4];
        }catch(ArrayIndexOutOfBoundsException e){
            return null;
        }
    }
    
    public String getKeyString2(){
        try{
            return token.split("/")[5];
        }catch(ArrayIndexOutOfBoundsException e){
            return null;
        }
    }
    
    public String getKeyString3(){
        try{
            return token.split("/")[6];
        }catch(ArrayIndexOutOfBoundsException e){
            return null;
        }
    }
    
    public String getKeyString4(){
        try{
            return token.split("/")[7];
        }catch(ArrayIndexOutOfBoundsException e){
            return null;
        }
    }
    
    public String getKeyString5(){
        try{
            return token.split("/")[8];
        }catch(ArrayIndexOutOfBoundsException e){
            return null;
        }
    }
    
    public String getKeyString6(){
        try{
            return token.split("/")[9];
        }catch(ArrayIndexOutOfBoundsException e){
            return null;
        }
    }
    
    public String getKeyString7(){
        try{
            return token.split("/")[10];
        }catch(ArrayIndexOutOfBoundsException e){
            return null;
        }
    }
    
    public String getKeyString8(){
        try{
            return token.split("/")[11];
        }catch(ArrayIndexOutOfBoundsException e){
            return null;
        }
    }
    
    public String getKeyString9(){
        try{
            return token.split("/")[12];
        }catch(ArrayIndexOutOfBoundsException e){
            return null;
        }
    }
}
