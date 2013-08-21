package gwt.client.ui;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

public class CustomFlexTable extends FlexTable {

    private Element tHeadElement; 
    
    public CustomFlexTable(){
        
    }
    
    /**
     * This method sets a widget for the specified header cell.
     *
     * @param column is a column number.
     * @param widget is a widget to be added to the cell.
     */
    public void setHeaderWidget(int row, int column, int colSpan, Widget... widgets) {
        prepareHeaderCell(row, column);

        if (widgets != null) {
            for(Widget widget : widgets){
                widget.removeFromParent();
            }
            
            Element tr = DOM.getChild(getTHeadElement(), row);
            Element th = DOM.getChild(tr, column);
            internalClearCell(th, true);
            
            if(colSpan >= 2){
                th.setAttribute("colSpan", colSpan + "");
            }
            
            for(Widget widget : widgets){
                // Physical attach.
                DOM.appendChild(th, widget.getElement());
            
                adopt(widget);
            }
        }
    }
    
    public void setHeaderHTML(int row, int column, int colSpan, String html) {
        prepareHeaderCell(row, column);

        if (html != null) {

            Element tr = DOM.getChild(getTHeadElement(), row);
            Element th = DOM.getChild(tr, column);
            internalClearCell(th, true);
            
            if(colSpan >= 2){
                th.setAttribute("colSpan", colSpan + "");
            }
            
            th.setInnerHTML(html);
        }
    }
    
    public void addTHeadClassName(String className){
        getTHeadElement().addClassName(className);
    }
    
    public void addTRClassName(int row, String className){
        Element tr = DOM.getChild(getTHeadElement(), row);
        tr.addClassName(className);
    }
    
    public void addTHClassName(int row, int column, String className){
        Element tr = DOM.getChild(getTHeadElement(), row);
        Element th = DOM.getChild(tr, column);
        th.addClassName(className);
    }

    public void clear(){
        super.removeAllRows();
        if(tHeadElement != null){
            tHeadElement.removeFromParent();
            tHeadElement = null;
        }
    }
    
    /**
     * Getter for property 'tFootElement'.
     *
     * @return Value for property 'tFootElement'.
     */
    private Element getTHeadElement() {
        return tHeadElement;
    }
    
    /**
     * This method prepares the header cell to be used.
     *
     * @param column is a column number.
     */
    private void prepareHeaderCell(int row, int column) {
        if (row < 0 || column < 0) {
            throw new IndexOutOfBoundsException("Cannot create a row or a column with a negative index: " + row + ", " + column);
        }
        
        if (tHeadElement == null) {
            tHeadElement = DOM.createElement("thead");
            DOM.insertChild(getElement(), getTHeadElement(), 0);
        }

        // Prepare rows
        int existingRow = DOM.getChildCount(getTHeadElement());
        if(existingRow <= row){
            int required = row + 1 - existingRow;
            if (required > 0) {
                addHeaderRows(getTHeadElement(), required);
            }
        }
        
        // Prepare columns
        int existingColumn = DOM.getChildCount(DOM.getChild(getTHeadElement(), row));
        if (existingColumn <= column) {
            int required = column + 1 - existingColumn;
            if (required > 0) {
                addHeaderCells(getTHeadElement(), row, required);
            }    
        }
    }
    
    /**
     * This native method is used to create TR tags.
     *
     * @param tHead is a grid thead element.
     * @param num   is a number of rows to create.
     */
    protected native void addHeaderRows(Element tHead, int num)/*-{
        for(var i = 0; i < num; i++){
          var row = $doc.createElement("tr");
          tHead.appendChild(row);
        }
    }-*/;
    
    /**
     * This native method is used to create TH tags instead of TD tags.
     *
     * @param tHead is a grid thead element.
     * @param row   is the index of the TR element
     * @param num   is a number of columns to create.
     */
    protected native void addHeaderCells(Element tHead, int row, int num)/*-{
        var rowElem = tHead.rows[row];
        for(var i = 0; i < num; i++){
          var cell = $doc.createElement("th");
          rowElem.appendChild(cell);
        }
    }-*/;
}
