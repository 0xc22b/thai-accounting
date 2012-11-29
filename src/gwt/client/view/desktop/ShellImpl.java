package gwt.client.view.desktop;

import gwt.client.TCF;
import gwt.client.TConstants;
import gwt.client.event.ActionEvent;
import gwt.client.event.ActionNames;
import gwt.client.view.Shell;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

public class ShellImpl extends Composite implements Shell {

    private class ActBtn extends Button{
        String actionName;
    }
    
    @UiTemplate("ShellImpl.ui.xml")
    interface MyUiBinder extends UiBinder<Widget, ShellImpl> {
    }
    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
    
    interface Style extends CssResource {
        String actBtn();
    }
    @UiField
    Style style;
    @UiField
    Label hLb;
    @UiField
    FlowPanel actBar;
    @UiField
    Button logOutBtn;
    @UiField
    Button userBtn;
    @UiField
    ScrollPanel container;
    
    private static final TConstants constants = TCF.get();
    
    private EventBus eventBus;
        
    public ShellImpl(EventBus eventBus) {
        this.eventBus = eventBus;
        initWidget(uiBinder.createAndBindUi(this));
        
        logOutBtn.setText(constants.logOut());
        userBtn.setText(constants.user());
        
        initializeActBtns(6);
    }

    @Override
    public void setWidget(IsWidget w) {
        //Activity manager may give w=null to clear/kill the view first
        //Then assign the next view
        //like DeckLayoutPanel.setWidget() can receive null to clear all children
        
        container.clear();
        if(w != null){
            container.add(w);
        }
    }
    
    @Override
    public void reset(){
        hLb.setText("Thai SME Accounting");
        for(int i=0; i<actBar.getWidgetCount(); i++){
            actBar.getWidget(i).setVisible(false);
        }
    }
    
    @Override
    public void setHLb(String text) {
        hLb.setText(text);
    }
    
    @Override
    public void setActBtn(int i, String text, String actionName, boolean visible){
        try{
            actBar.getWidget(i);
        }catch(IndexOutOfBoundsException e){
            initializeActBtns(i + 1);
        }
        
        ActBtn actBtn = (ActBtn)actBar.getWidget(i);
        actBtn.setText(text);
        actBtn.actionName = actionName;
        actBtn.setVisible(visible);
    }
    
    @Override
    public void setActBtnVisible(int i, boolean visible) {
        ActBtn actBtn = (ActBtn)actBar.getWidget(i);
        actBtn.setVisible(visible);
    }
    
    @UiHandler("logOutBtn")
    void onLogOutBtnClicked(ClickEvent event) {
        ActionEvent.fire(eventBus, ActionNames.LOGOUT);
    }
    
    @UiHandler("userBtn")
    void onUserBtnClicked(ClickEvent event) {
        ActionEvent.fire(eventBus, ActionNames.USER);
    }
    
    private void initializeActBtns(int targetSize){
        
        int currentSize = actBar.getWidgetCount();
        
        for(int i = currentSize; i < targetSize; i++){
            ActBtn actBtn = new ActBtn();
            actBtn.addStyleName(style.actBtn());
            actBtn.addClickHandler(actBtnClickHandler);
            actBar.add(actBtn);
        }
    }
    
    private ClickHandler actBtnClickHandler = new ClickHandler(){
        @Override
        public void onClick(ClickEvent event) {
            ActBtn actBtn = (ActBtn)event.getSource();
            ActionEvent.fire(eventBus, actBtn.actionName);
        }  
    };
}
