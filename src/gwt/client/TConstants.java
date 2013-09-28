package gwt.client;

import com.google.gwt.i18n.client.Constants;

public interface TConstants extends Constants{

    
    @DefaultStringValue("Log out")
    String logOut();
    
    @DefaultStringValue("My account")
    String user();
    
    @DefaultStringValue("No data.")
    String noData();
    
    @DefaultStringValue("Company List")
    String comList();
    
    @DefaultStringValue("Company")
    String com();
    
    @DefaultStringValue("Fiscal year List")
    String fisList();
    
    @DefaultStringValue("Create New")
    String createNew();
    
    @DefaultStringValue("View")
    String view();
    
    @DefaultStringValue("Edit")
    String edit();
    
    @DefaultStringValue("Delete")
    String delete();
    
    @DefaultStringValue("Back")
    String back();
    
    @DefaultStringValue("Save")
    String save();
    
    @DefaultStringValue("Save and create default setup")
    String saveAndDefaultSetup();
    
    @DefaultStringValue("Save and copy previous setup")
    String saveAndPrevSetup();
    
    @DefaultStringValue("OK")
    String ok();
    
    @DefaultStringValue("Cancel")
    String cancel();
    
    @DefaultStringValue("Loading...")
    String loading();
    
    @DefaultStringValue("Are you sure you want to delete?")
    String confirmDelete();
    
    @DefaultStringValue("Remove")
    String remove();
    
    @DefaultStringValue("Begin")
    String begin();
    
    @DefaultStringValue("End")
    String end();
    
    @DefaultStringValue("Day")
    String day();
    
    @DefaultStringValue("Month (1-12)")
    String month();
    
    @DefaultStringValue("Year")
    String year();
    
    @DefaultStringValue("Name")
    String name();
    
    @DefaultStringValue("Short name")
    String shortName();
    
    @DefaultStringValue("Setup ")
    String setup();
    
    @DefaultStringValue("Add journals")
    String addJournals();
    
    @DefaultStringValue("Reports")
    String reports();
    
    @DefaultStringValue("Journal type")
    String journalType();
    
    @DefaultStringValue("Doc type")
    String docType();
    
    @DefaultStringValue("Acc group")
    String accGrp();
    
    @DefaultStringValue("Acc chart")
    String accChart();
    
    @DefaultStringValue("Financial statement")
    String fin();
    
    @DefaultStringValue("Financial statement item")
    String finItem();
    
    @DefaultStringValue("Beginning")
    String beginning();
    
    @DefaultStringValue("Remaining")
    String remaining();
    
    @DefaultStringValue("Ledger")
    String ledger();
    
    @DefaultStringValue("Trial balance sheet")
    String trial();
    
    @DefaultStringValue("Balance sheet")
    String balanceSheet();
    
    @DefaultStringValue("Profit and loss")
    String profitReport();
    
    @DefaultStringValue("Cost")
    String costReport();
    
    @DefaultStringValue("Wrong numbers? Force to recalculate data for all these reports")
    String recalAccAmt();
    
    @DefaultStringValue("Acc no.")
    String accNo();
    
    @DefaultStringValue("Acc name")
    String accName();
    
    @DefaultStringValue("Level")
    String level();
    
    @DefaultStringValue("Acc type")
    String accType();
    
    @DefaultStringValue("Control")
    String control();
    
    @DefaultStringValue("Entry")
    String entry();
    
    @DefaultStringValue("Parent acc no.")
    String parentAccNo();
    
    @DefaultStringValue("Debit")
    String debit();
    
    @DefaultStringValue("Credit")
    String credit();
    
    @DefaultStringValue("Date")
    String date();
    
    @DefaultStringValue("Doc no.")
    String journalNo();
    
    @DefaultStringValue("Description")
    String desc();
    
    @DefaultStringValue("New child")
    String newChild();
    
    @DefaultStringValue("New sibling")
    String newSibling();
    
    @DefaultStringValue("Add new item")
    String addNewItem();
    
    @DefaultStringValue("Print")
    String print();
    
    @DefaultStringValue("Code")
    String code();
    
    @DefaultStringValue("Desc in Journals")
    String journalDesc();
    
    @DefaultStringValue("Total")
    String total();
    
    @DefaultStringValue("Whole total")
    String wholeTotal();

    @DefaultStringValue("Asset account no.")
    String assetAccNo();
    
    @DefaultStringValue("Debt account no.")
    String debtAccNo();
    
    @DefaultStringValue("Shareholder account no.")
    String shareholderAccNo();
    
    @DefaultStringValue("Income account no.")
    String incomeAccNo();
    
    @DefaultStringValue("Expense account no.")
    String expenseAccNo();
    
    @DefaultStringValue("Cost account no.")
    String costAccNo();
    
    @DefaultStringValue("Show all")
    String showAll();
    
    @DefaultStringValue("Does split")
    String doesSplit();

    @DefaultStringValue("Jan")
    String jan();
    
    @DefaultStringValue("Feb")
    String feb();
    
    @DefaultStringValue("Mar")
    String mar();
    
    @DefaultStringValue("Apr")
    String apr();
    
    @DefaultStringValue("May")
    String may();
    
    @DefaultStringValue("Jun")
    String jun();
    
    @DefaultStringValue("Jul")
    String jul();
    
    @DefaultStringValue("Aug")
    String aug();
    
    @DefaultStringValue("Sep")
    String sep();
    
    @DefaultStringValue("Oct")
    String oct();
    
    @DefaultStringValue("Nov")
    String nov();
    
    @DefaultStringValue("Dec")
    String dec();
    
    @DefaultStringValue("or")
    String or();
    
    @DefaultStringValue("Seq.")
    String seq();
    
    @DefaultStringValue("Command")
    String comm();
    
    @DefaultStringValue("Argument")
    String arg();
    
    @DefaultStringValue("Cal. condition")
    String calCon();
    
    @DefaultStringValue("Print condition")
    String printCon();
    
    @DefaultStringValue("Print style")
    String printStyle();
    
    @DefaultStringValue("var1")
    String var1();
    
    @DefaultStringValue("var2")
    String var2();
    
    @DefaultStringValue("var3")
    String var3();
    
    @DefaultStringValue("var4")
    String var4();
    
    @DefaultStringValue("This field is invalid.")
    String invalidMsg();
    
    @DefaultStringValue("This field is invalid. Must be a number, no space.")
    String invalidNumberMsg();
    
    @DefaultStringValue("This acc no. is already in use.")
    String duplicateAccChartNoMsg();
    
    @DefaultStringValue("This journal no. already in use.")
    String duplicateJournalNoMsg();

    @DefaultStringValue("This name is already in use.")
    String duplicateNameMsg();
    
    @DefaultStringValue("Items are required.")
    String itemRequiredMsg();
    
    @DefaultStringValue("Debt and shareholder")
    String debtAndShareholder();
    
    @DefaultStringValue("accrued profit (loss)")
    String accruedProfit();
    
    @DefaultStringValue("profit (loss)")
    String profit();
}
