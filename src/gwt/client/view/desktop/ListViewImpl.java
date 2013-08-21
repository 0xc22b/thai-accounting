package gwt.client.view.desktop;

import gwt.client.TCF;
import gwt.client.TConstants;
import gwt.client.def.ListDef;
import gwt.client.view.ListView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.SafeHtmlHeader;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class ListViewImpl<L, E> extends Composite implements ListView<L, E> {

    public interface DataGridResource extends DataGrid.Resources {
        @Source({ DataGrid.Style.DEFAULT_CSS, "DataGridOverride.css" })
        DataGrid.Style dataGridStyle();
    }
    
    private static DataGridResource dataGridResource = GWT.create(DataGridResource.class);
    
    @SuppressWarnings("rawtypes")
    @UiTemplate("ListViewImpl.ui.xml")
    interface MyUiBinder extends UiBinder<Widget, ListViewImpl> {
    }
    
    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
    
    interface Style extends CssResource {
        String clickColumn();
    }

    @UiField
    Style style;

    @UiField(provided = true)
    DataGrid<E> dataGrid;
    
    @UiField(provided = true)
    SimplePager pager;

    private static final TConstants constants = TCF.get();
    
    private Presenter presenter;
    private ListDef<L, E> listDef;
    private SingleSelectionModel<E> selectionModel;
    private ListDataProvider<E> dataProvider;

    private int firstVisibleIndex;

    public ListViewImpl(ListDef<L, E> listDef) {
        this.listDef = listDef;
        initialize(); 
    }

    @Override
    public Widget asWidget() {
        return this;
    }

    @Override
    public void init(Presenter presenter) {
        this.presenter = presenter;
        dataGrid.setVisible(false);
    }

    @Override
    public void setData(L l) {
        
        List<E> list = listDef.getList(l);  
        dataProvider.getList().clear();
        dataProvider.getList().addAll(list);
        dataProvider.refresh();
        
        // Clear selected item
        if(selectionModel.getSelectedObject()!=null){
            selectionModel.setSelected(selectionModel.getSelectedObject(), false);
        }
        
        // Clear sort
        dataGrid.getColumnSortList().clear();
        
        dataGrid.setVisible(true);

        if (firstVisibleIndex > 0) {
            new Timer() {
                public void run() {
                    Element el = dataGrid.getRowContainer();
                    el = el.getParentElement().getParentElement().getParentElement();
                    el.setScrollTop(firstVisibleIndex);
                    // Prevent scrolling after delete.
                    firstVisibleIndex = 0;
                }
            }.schedule(1000);
        }
    }
    
    @Override
    public String getSelectedItemKeyString() {
        return selectionModel.getSelectedObject() == null ? null : listDef.getKeyString(selectionModel.getSelectedObject());
    }

    @Override
    public void saveFirstVisibleIndex() {
        Element el = dataGrid.getRowContainer();
        for (int i = 0; i < 3; i++) {
            el = el.getParentElement();
            if (el == null) return;
        }
        firstVisibleIndex = el.getScrollTop();
    }
    
    private void initialize() {
        // Create a DataGrid.

        // Set a key provider that provides a unique key for each contact. If key is
        // used to identify contacts when fields (such as the name and address)
        // change.
        dataGrid = new DataGrid<E>(200, dataGridResource, listDef.getKeyProvider(), null);
        
        // Set the message to display when the table is empty.
        dataGrid.setEmptyTableWidget(new Label(constants.noData()));
        
        // Attach a column sort handler to sort the list.
        dataProvider = new ListDataProvider<E>();
        dataProvider.setList(new ArrayList<E>());
        ListHandler<E> sortHandler = new ListHandler<E>(dataProvider.getList());
        dataGrid.addColumnSortHandler(sortHandler);
        dataProvider.addDataDisplay(dataGrid);
        
        // Create a Pager to control the table.
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        pager.setDisplay(dataGrid);
        
        // Add a selection model so we can select cells.
        selectionModel = new SingleSelectionModel<E>(listDef.getKeyProvider());
        dataGrid.setSelectionModel(selectionModel, DefaultSelectionEventManager.<E> createCheckboxManager(0));
        selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                presenter.selectionChanged(!(selectionModel.getSelectedObject()==null));
            }
        });

        // Initialize the columns.
        
        // Checkbox column. This table will uses a checkbox column for selection.
        // Alternatively, you can call dataGrid.setSelectionEnabled(true) to enable
        // mouse selection.
        Column<E, Boolean> checkColumn = new Column<E, Boolean>(new CheckboxCell(true, false)) {
            @Override
            public Boolean getValue(E object) {
                // Get the value from the selection model.
                return selectionModel.isSelected(object);
            }
        };
        dataGrid.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
        dataGrid.setColumnWidth(checkColumn, 60, Unit.PX);
        
        // Clickable column
        Column<E, String> clickColumn = new Column<E, String>(new ClickableTextCell()){
            @Override
            public String getValue(E object) {
                return listDef.getColumnValue(object, 0);
            }
        };
        clickColumn.setFieldUpdater(new FieldUpdater<E, String>(){
            @Override
            public void update(int index, E object, String value) {
                presenter.itemClicked(listDef.getKeyString(object));
            }
        });
        if(listDef.hasSort(0)){
            clickColumn.setSortable(true);
            sortHandler.setComparator(clickColumn, new Comparator<E>() {
                public int compare(E e1, E e2) {
                    return listDef.getCompare(e1, e2, 0);
                }
            });
        }
        if(listDef.hasFooter(0)){
            Header<String> footer = new Header<String>(new TextCell()) {
                @Override
                public String getValue() {
                    List<E> items = dataGrid.getVisibleItems();
                    return listDef.getFooterValue(items, 0);
                }
            };
            
            dataGrid.addColumn(clickColumn, new SafeHtmlHeader(SafeHtmlUtils.fromSafeConstant(listDef.getColumnName(0))), footer);
        }else{
            dataGrid.addColumn(clickColumn, listDef.getColumnName(0));
        }
        if(listDef.hasWidth(0)){
            dataGrid.setColumnWidth(clickColumn, listDef.getWidthValue(0),
                    listDef.getWidthUnit(0));
        }
        
        for (int i = 1; i < listDef.getColumnSize(); i++) {
            
            final int fI = i;
            
            Column<E, SafeHtml> column = new Column<E, SafeHtml>(new SafeHtmlCell()) {
                @Override
                public SafeHtml getValue(E object) {
                    SafeHtmlBuilder sb = new SafeHtmlBuilder();
                    sb.appendHtmlConstant(listDef.getColumnValue(object, fI) == null ? "" : listDef.getColumnValue(object, fI));
                    return sb.toSafeHtml();
                }
            };
            if(listDef.hasSort(fI)){
                column.setSortable(true);
                sortHandler.setComparator(column, new Comparator<E>() {
                    public int compare(E e1, E e2) {
                        return listDef.getCompare(e1, e2, fI);
                    }
                });
            }
            if(listDef.hasFooter(fI)){
                Header<String> footer = new Header<String>(new TextCell()) {
                    @Override
                    public String getValue() {
                        List<E> items = dataGrid.getVisibleItems();
                        return listDef.getFooterValue(items, fI);
                    }
                };
                
                dataGrid.addColumn(column, new SafeHtmlHeader(SafeHtmlUtils.fromSafeConstant(listDef.getColumnName(fI))), footer);
            }else{
                dataGrid.addColumn(column, listDef.getColumnName(fI));
            }
            if(listDef.hasWidth(fI)){
                dataGrid.setColumnWidth(column, listDef.getWidthValue(fI),
                        listDef.getWidthUnit(fI));
            }
        }

        // Create the UiBinder.
        initWidget(uiBinder.createAndBindUi(this));
        
        dataGrid.getColumn(1).setCellStyleNames(style.clickColumn());
    }
}
