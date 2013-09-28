package gwt.client.activity;

import gwt.client.ClientFactory;
import gwt.client.place.AllPlace;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;

public class AppActivityMapper implements ActivityMapper {

	private ClientFactory clientFactory;

	/**
	 * AppActivityMapper associates each Place with its corresponding
	 * {@link Activity}
	 * 
	 * @param clientFactory Factory to be passed to activities
	 */
	public AppActivityMapper(ClientFactory clientFactory) {
		super();
		this.clientFactory = clientFactory;
	}

	/**
	 * Map each Place to its corresponding Activity. This would be a great use
	 * for GIN.
	 */
	@Override
	public Activity getActivity(final Place place) {
	    if (place instanceof AllPlace) {
	        AllPlace allPlace = (AllPlace)place;
	        if(allPlace.getPlace().equals(AllPlace.COM) && allPlace.getAction().equals(AllPlace.LIST)){
	            return new ComListActivity(allPlace, clientFactory);
	        }else if(allPlace.getPlace().equals(AllPlace.COM)){
	            return new ComActivity(allPlace, clientFactory);
	        }else if(allPlace.getPlace().equals(AllPlace.FIS) && allPlace.getAction().equals(AllPlace.LIST)){
                return new FisListActivity(allPlace, clientFactory);
            }else if(allPlace.getPlace().equals(AllPlace.FIS)){
                return new FisActivity(allPlace, clientFactory);
            }else if(allPlace.getPlace().equals(AllPlace.MENU)){
                return new MenuActivity(allPlace, clientFactory);
            }else if(allPlace.getPlace().equals(AllPlace.JOURT) && allPlace.getAction().equals(AllPlace.LIST)){
                return new JournalTypeListActivity(allPlace, clientFactory);
            }else if(allPlace.getPlace().equals(AllPlace.JOURT)){
                return new JournalTypeActivity(allPlace, clientFactory);   
            }else if(allPlace.getPlace().equals(AllPlace.DOC) && allPlace.getAction().equals(AllPlace.LIST)){
                return new DocTypeListActivity(allPlace, clientFactory);
            }else if(allPlace.getPlace().equals(AllPlace.DOC)){
                return new DocTypeActivity(allPlace, clientFactory);   
            }else if(allPlace.getPlace().equals(AllPlace.GRP) && allPlace.getAction().equals(AllPlace.LIST)){
                return new AccGrpListActivity(allPlace, clientFactory);
            }else if(allPlace.getPlace().equals(AllPlace.GRP)){
                return new AccGrpActivity(allPlace, clientFactory);   
            }else if(allPlace.getPlace().equals(AllPlace.CHART) && allPlace.getAction().equals(AllPlace.LIST)){
                return new AccChartListActivity(allPlace, clientFactory);
            }else if(allPlace.getPlace().equals(AllPlace.CHART)){
                return new AccChartActivity(allPlace, clientFactory);   
            }else if(allPlace.getPlace().equals(AllPlace.BEGIN) && allPlace.getAction().equals(AllPlace.LIST)){
                return new BeginListActivity(allPlace, clientFactory);
            }else if(allPlace.getPlace().equals(AllPlace.BEGIN)){
                return new BeginActivity(allPlace, clientFactory);   
            }else if(allPlace.getPlace().equals(AllPlace.JOUR) && allPlace.getAction().equals(AllPlace.LIST)){
                return new JournalListActivity(allPlace, clientFactory);
            }else if(allPlace.getPlace().equals(AllPlace.JOUR)){
                return new JournalActivity(allPlace, clientFactory);   
            }else if(allPlace.getPlace().equals(AllPlace.REPORT)){
                return new ReportActivity(allPlace, clientFactory);
            }
	    }
		return null;
	}

}
