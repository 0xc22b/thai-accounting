package gwt.client;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.web.bindery.event.shared.EventBus;

import gwt.client.place.AllPlace;
import gwt.client.place.AppPlaceHistoryMapper;
import gwt.client.view.Shell;

/**
 * The heart of the applicaiton, mainly concerned with bootstrapping.
 */
public class App {
    
    //private static final Logger log = Logger.getLogger(App.class.getName());

    private final EventBus eventBus;
    private final PlaceController placeController;
    private final ActivityManager activityManager;
    private final AppPlaceHistoryMapper historyMapper;
    private final PlaceHistoryHandler historyHandler;
    
    private final Shell shell;

    public App(ClientFactory clientFactory, EventBus eventBus, PlaceController placeController, ActivityManager activityManager,
            AppPlaceHistoryMapper historyMapper, PlaceHistoryHandler historyHandler, Shell shell) {
        this.eventBus = eventBus;
        this.placeController = placeController;
        this.activityManager = activityManager;
        this.historyMapper = historyMapper;
        this.historyHandler = historyHandler;
        this.shell = shell;
    }

    public void run(HasWidgets.ForIsWidget parentView) {
        activityManager.setDisplay(shell);
        parentView.add(shell);
        
        /*GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            @Override
            public void onUncaughtException(Throwable e) {
                while (e instanceof UmbrellaException) {
                    e = ((UmbrellaException) e).getCauses().iterator().next();
                }

                String message = e.getMessage();
                if (message == null) {
                    message = e.toString();
                }
                log.log(Level.SEVERE, "Uncaught exception", e);
                Window.alert("An unexpected error occurred: " + message);
            }
        });*/
        
        initBrowserHistory(historyMapper, historyHandler, new AllPlace(AllPlace.COM, AllPlace.LIST));
    }
    
    /**
     * Initialize browser history / bookmarking. If LocalStorage is available,
     * use it to make the user's default location in the app the last one seen.
     */
    private void initBrowserHistory(final AppPlaceHistoryMapper historyMapper, PlaceHistoryHandler historyHandler, AllPlace defaultPlace) {
        
        Place savedPlace = null;
        /*if (storage != null) {
            try {
                // wrap in try-catch in case stored value is invalid
                savedPlace = historyMapper.getPlace(storage.getItem(HISTORY_SAVE_KEY));
            } catch (Throwable t) {
                // ignore error and use the default-default
            }
        }*/
        if (savedPlace == null) {
            savedPlace = defaultPlace;
        }
        historyHandler.register(placeController, eventBus, savedPlace);
        historyHandler.handleCurrentHistory();
        
        /*
         * Monitor the eventbus for place changes and note them in LocalStorage
         * for the next launch.
         */
        /*if (storage != null) {
            eventBus.addHandler(PlaceChangeEvent.TYPE, new PlaceChangeEvent.Handler() {
                public void onPlaceChange(PlaceChangeEvent event) {
                    storage.setItem(HISTORY_SAVE_KEY, historyMapper.getToken(event.getNewPlace()));
                }
            });
        }*/ 
    }
}
