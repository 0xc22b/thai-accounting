package gwt.client;

import gwt.client.model.Model;
import gwt.client.view.AccChartView;
import gwt.client.view.AccGrpView;
import gwt.client.view.BeginView;
import gwt.client.view.ComView;
import gwt.client.view.DocTypeView;
import gwt.client.view.FisView;
import gwt.client.view.JournalTypeView;
import gwt.client.view.JournalView;
import gwt.client.view.ListView;
import gwt.client.view.MenuView;
import gwt.client.view.ReportView;
import gwt.client.view.Shell;
import gwt.shared.model.SAccAmt;
import gwt.shared.model.SAccChart;
import gwt.shared.model.SAccGrp;
import gwt.shared.model.SCom;
import gwt.shared.model.SComList;
import gwt.shared.model.SDocType;
import gwt.shared.model.SFiscalYear;
import gwt.shared.model.SJournalHeader;
import gwt.shared.model.SJournalType;

import java.util.List;

import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;

public interface ClientFactory {
    App getApp();
	
    EventBus getEventBus();
	PlaceController getPlaceController();
	
	Model getModel();
	
	Shell getShell();
	ListView<SComList, SCom> getComListView();
	ComView<SCom> getComView();
	ListView<SCom, SFiscalYear> getFisListView();
    FisView<SFiscalYear> getFisView();
	MenuView<SFiscalYear> getMenuView();
	ListView<SFiscalYear, SJournalType> getJournalTypeListView();
	JournalTypeView<SFiscalYear> getJournalTypeView();
	ListView<SFiscalYear, SDocType> getDocTypeListView();
    DocTypeView<SFiscalYear> getDocTypeView();
	ListView<SFiscalYear, SAccGrp> getAccGrpListView();
	AccGrpView<SFiscalYear> getAccGrpView();
	ListView<SFiscalYear, SAccChart> getAccChartListView();
    AccChartView<SFiscalYear> getAccChartView();
    ListView<SFiscalYear, SAccChart> getBeginListView();
    BeginView<SFiscalYear> getBeginView();

    ListView<List<SJournalHeader>, SJournalHeader> getJournalListView();
    JournalView<SFiscalYear, SJournalHeader> getJournalView();

    ReportView<SFiscalYear, SJournalHeader, SAccAmt> getReportView();
}
