package gwt.client;

import gwt.client.activity.AppActivityMapper;
import gwt.client.def.AccChartListDefImpl;
import gwt.client.def.AccGrpListDefImpl;
import gwt.client.def.BeginListDefImpl;
import gwt.client.def.ComDefImpl;
import gwt.client.def.ComListDefImpl;
import gwt.client.def.DocTypeListDefImpl;
import gwt.client.def.FinHeaderListDefImpl;
import gwt.client.def.FinItemListDefImpl;
import gwt.client.def.JournalTypeListDefImpl;
import gwt.client.def.FisDefImpl;
import gwt.client.def.FisListDefImpl;
import gwt.client.def.JournalListDefImpl;
import gwt.client.model.Model;
import gwt.client.place.AppPlaceHistoryMapper;
import gwt.client.view.AccChartView;
import gwt.client.view.AccGrpView;
import gwt.client.view.BeginView;
import gwt.client.view.ComView;
import gwt.client.view.DocTypeView;
import gwt.client.view.FinHeaderView;
import gwt.client.view.FinItemView;
import gwt.client.view.JournalTypeView;
import gwt.client.view.FisView;
import gwt.client.view.JournalView;
import gwt.client.view.ListView;
import gwt.client.view.MenuView;
import gwt.client.view.ReportView;
import gwt.client.view.Shell;
import gwt.client.view.desktop.AccChartViewImpl;
import gwt.client.view.desktop.AccGrpViewImpl;
import gwt.client.view.desktop.BeginViewImpl;
import gwt.client.view.desktop.ComViewImpl;
import gwt.client.view.desktop.DocTypeViewImpl;
import gwt.client.view.desktop.FinHeaderViewImpl;
import gwt.client.view.desktop.FinItemViewImpl;
import gwt.client.view.desktop.JournalTypeViewImpl;
import gwt.client.view.desktop.FisViewImpl;
import gwt.client.view.desktop.JournalViewImpl;
import gwt.client.view.desktop.ListViewImpl;
import gwt.client.view.desktop.MenuViewImpl;
import gwt.client.view.desktop.ReportViewImpl;
import gwt.client.view.desktop.ShellImpl;
import gwt.shared.model.SAccChart;
import gwt.shared.model.SAccGrp;
import gwt.shared.model.SCom;
import gwt.shared.model.SComList;
import gwt.shared.model.SDocType;
import gwt.shared.model.SFinHeader;
import gwt.shared.model.SFinItem;
import gwt.shared.model.SJournalType;
import gwt.shared.model.SFiscalYear;
import gwt.shared.model.SJournalHeader;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

public class ClientFactoryImpl implements ClientFactory {

    private final EventBus eventBus = new SimpleEventBus();
    
    private final PlaceController placeController = new PlaceController(eventBus);
    private final AppPlaceHistoryMapper historyMapper = GWT.create(AppPlaceHistoryMapper.class);
    private final PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(historyMapper);
    
    private ActivityMapper activityMapper = new AppActivityMapper(this);
    private ActivityManager activityManager = new ActivityManager(activityMapper, eventBus);
    
    private Model model;
    
    private Shell shell;
    private ListView<SComList, SCom> comListView;
    private ComView<SCom> comView;
    private ListView<SCom, SFiscalYear> fisListView;
    private FisView<SFiscalYear> fisView;
    private MenuView<SFiscalYear> menuView;
    private ListView<SFiscalYear, SJournalType> journalTypeListView;
    private JournalTypeView<SFiscalYear> journalTypeView;
    private ListView<SFiscalYear, SDocType> docTypeListView;
    private DocTypeView<SFiscalYear> docTypeView;
    private ListView<SFiscalYear, SAccGrp> accGrpListView;
    private AccGrpView<SFiscalYear> accGrpView;
    private ListView<SFiscalYear, SAccChart> accChartListView;
    private AccChartView<SFiscalYear> accChartView;
    private ListView<SFiscalYear, SAccChart> beginListView;
    private BeginView<SFiscalYear> beginView;
    private ListView<SFiscalYear, SFinHeader> finHeaderListView;
    private FinHeaderView<SFiscalYear> finHeaderView;
    private ListView<SFinHeader, SFinItem> finItemListView;
    private FinItemView<SFiscalYear> finItemView;
    private ListView<SFiscalYear, SJournalHeader> journalListView;
    private JournalView<SFiscalYear> journalView;
    
    private ReportView<SFiscalYear> reportView;
    
    @Override
    public App getApp() {
        return new App(this,
                       getEventBus(), 
                       getPlaceController(), 
                       activityManager, 
                       historyMapper, 
                       historyHandler,
                       getShell());
    }

    @Override
    public PlaceController getPlaceController() {
        return placeController;
    }

    @Override
    public EventBus getEventBus() {
        return eventBus;
    }

    @Override
    public Model getModel() {
        if (model == null) {
            model = new Model();
        }
        return model;
    }
    
    @Override
    public Shell getShell() {
        if (shell == null) {
            shell = new ShellImpl(getEventBus());
        }
        return shell;
    }

    @Override
    public ListView<SComList, SCom> getComListView() {
        if (comListView == null) {
            comListView = new ListViewImpl<SComList, SCom>(ComListDefImpl.getInstance());
        }
        return comListView;
    }
    
    @Override
    public ComView<SCom> getComView() {
        if (comView == null) {
            comView = new ComViewImpl<SCom>(ComDefImpl.getInstance());
        }
        return comView;
    }
    
    @Override
    public ListView<SCom, SFiscalYear> getFisListView() {
        if (fisListView == null) {
            fisListView = new ListViewImpl<SCom, SFiscalYear>(FisListDefImpl.getInstance());
        }
        return fisListView;
    }

    @Override
    public FisView<SFiscalYear> getFisView() {
        if (fisView == null) {
            fisView = new FisViewImpl<SFiscalYear>(FisDefImpl.getInstance());
        }
        return fisView;
    }
    
    @Override
    public MenuView<SFiscalYear> getMenuView() {
        if (menuView == null) {
            menuView = new MenuViewImpl<SFiscalYear>(FisDefImpl.getInstance());
        }
        return menuView;
    }
    
    @Override
    public ListView<SFiscalYear, SJournalType> getJournalTypeListView() {
        if (journalTypeListView == null) {
            journalTypeListView = new ListViewImpl<SFiscalYear, SJournalType>(JournalTypeListDefImpl.getInstance());
        }
        return journalTypeListView;
    }
    
    @Override
    public JournalTypeView<SFiscalYear> getJournalTypeView() {
        if (journalTypeView == null) {
            journalTypeView = new JournalTypeViewImpl<SFiscalYear>(FisDefImpl.getInstance());
        }
        return journalTypeView;
    }
    
    @Override
    public ListView<SFiscalYear, SDocType> getDocTypeListView() {
        if (docTypeListView == null) {
            docTypeListView = new ListViewImpl<SFiscalYear, SDocType>(DocTypeListDefImpl.getInstance());
        }
        return docTypeListView;
    }

    @Override
    public DocTypeView<SFiscalYear> getDocTypeView() {
        if (docTypeView == null) {
            docTypeView = new DocTypeViewImpl<SFiscalYear>(FisDefImpl.getInstance());
        }
        return docTypeView;
    }

    @Override
    public ListView<SFiscalYear, SAccGrp> getAccGrpListView() {
        if (accGrpListView == null) {
            accGrpListView = new ListViewImpl<SFiscalYear, SAccGrp>(AccGrpListDefImpl.getInstance());
        }
        return accGrpListView;
    }

    @Override
    public AccGrpView<SFiscalYear> getAccGrpView() {
        if (accGrpView == null) {
            accGrpView = new AccGrpViewImpl<SFiscalYear>(FisDefImpl.getInstance());
        }
        return accGrpView;
    }

    @Override
    public ListView<SFiscalYear, SAccChart> getAccChartListView() {
        if (accChartListView == null) {
            accChartListView = new ListViewImpl<SFiscalYear, SAccChart>(AccChartListDefImpl.getInstance());
        }
        return accChartListView;
    }

    @Override
    public AccChartView<SFiscalYear> getAccChartView() {
        if (accChartView == null) {
            accChartView = new AccChartViewImpl<SFiscalYear>(FisDefImpl.getInstance());
        }
        return accChartView;
    }

    @Override
    public ListView<SFiscalYear, SAccChart> getBeginListView() {
        if (beginListView == null) {
            beginListView = new ListViewImpl<SFiscalYear, SAccChart>(BeginListDefImpl.getInstance());
        }
        return beginListView;
    }

    @Override
    public BeginView<SFiscalYear> getBeginView() {
        if (beginView == null) {
            beginView = new BeginViewImpl<SFiscalYear>(FisDefImpl.getInstance());
        }
        return beginView;
    }
    
    @Override
    public ListView<SFiscalYear, SFinHeader> getFinHeaderListView() {
        if (finHeaderListView == null) {
            finHeaderListView = new ListViewImpl<SFiscalYear, SFinHeader>(FinHeaderListDefImpl.getInstance());
        }
        return finHeaderListView;
    }

    @Override
    public FinHeaderView<SFiscalYear> getFinHeaderView() {
        if (finHeaderView == null) {
            finHeaderView = new FinHeaderViewImpl<SFiscalYear>(FisDefImpl.getInstance());
        }
        return finHeaderView;
    }
    
    @Override
    public ListView<SFinHeader, SFinItem> getFinItemListView() {
        if (finItemListView == null) {
            finItemListView = new ListViewImpl<SFinHeader, SFinItem>(FinItemListDefImpl.getInstance());
        }
        return finItemListView;
    }

    @Override
    public FinItemView<SFiscalYear> getFinItemView() {
        if (finItemView == null) {
            finItemView = new FinItemViewImpl<SFiscalYear>(FisDefImpl.getInstance());
        }
        return finItemView;
    }

    @Override
    public ListView<SFiscalYear, SJournalHeader> getJournalListView() {
        if (journalListView == null) {
            journalListView = new ListViewImpl<SFiscalYear, SJournalHeader>(JournalListDefImpl.getInstance());
        }
        return journalListView;
    }

    @Override
    public JournalView<SFiscalYear> getJournalView() {
        if (journalView == null) {
            journalView = new JournalViewImpl<SFiscalYear>(FisDefImpl.getInstance());
        }
        return journalView;
    }

    @Override
    public ReportView<SFiscalYear> getReportView() {
        if (reportView == null) {
            reportView = new ReportViewImpl<SFiscalYear>(FisDefImpl.getInstance());
        }
        return reportView;
    }

    

    
    
    
    

    

    

}
