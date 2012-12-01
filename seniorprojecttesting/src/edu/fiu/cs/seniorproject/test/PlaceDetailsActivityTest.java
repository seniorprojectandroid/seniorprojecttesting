package edu.fiu.cs.seniorproject.test;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import edu.fiu.cs.seniorproject.PlaceDetailsActivity;
import edu.fiu.cs.seniorproject.data.Location;
import edu.fiu.cs.seniorproject.data.Place;
import edu.fiu.cs.seniorproject.data.PlaceCategoryFilter;
import edu.fiu.cs.seniorproject.manager.AppLocationManager;
import edu.fiu.cs.seniorproject.manager.DataManager;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class PlaceDetailsActivityTest extends ActivityInstrumentationTestCase2<PlaceDetailsActivity> {
	
	private PlaceDetailsActivity mPlaceActivity = null;
	private DataManager mDataManager = null;

	public PlaceDetailsActivityTest() {
		super(PlaceDetailsActivity.class);
		// TODO Auto-generated constructor stub
	}// end PlaceDetailsActivityTest	
	
	@Override
	protected void setUp() {
		setActivityInitialTouchMode(false);
		mPlaceActivity = getActivity();
		mDataManager = DataManager.getSingleton();
	}// end setUp
	
	public void testPreconditions() {
		assertNotNull(mPlaceActivity);
		assertNotNull(mDataManager);
	}// testPreconditions
	
	@UiThreadTest
	public void testActivityUI() {
		
		ImageView img = (ImageView)mPlaceActivity.findViewById(edu.fiu.cs.seniorproject.R.id.place_image);
		assertNotNull(img);
				
		TextView tvn = (TextView)mPlaceActivity.findViewById(edu.fiu.cs.seniorproject.R.id.place_name);
		assertNotNull(tvn);
		
		TextView tvloc = (TextView)mPlaceActivity.findViewById(edu.fiu.cs.seniorproject.R.id.place_location);
		assertNotNull(tvloc);
		
		TextView tvdist = (TextView)mPlaceActivity.findViewById(edu.fiu.cs.seniorproject.R.id.place_distance);
		assertNotNull(tvdist);
		
		TextView tvdesc = (TextView)mPlaceActivity.findViewById(edu.fiu.cs.seniorproject.R.id.place_description);
		assertNotNull(tvdesc);		
		
		TextView tvevent = (TextView)mPlaceActivity.findViewById(android.R.id.empty);
		assertNotNull(tvevent);
		
		View vw = (View)mPlaceActivity.findViewById(edu.fiu.cs.seniorproject.R.id.mapview);
		assertNotNull(vw);		
		
	}// end testActivityUI
	
	public void testDataProvider() {
		assertNotNull(mDataManager);
		
		android.location.Location currentLocation = AppLocationManager.getCurrentLocation();
		Location location = new Location( String.valueOf( currentLocation.getLatitude() ), String.valueOf(currentLocation.getLongitude()) );
		
		final List<Place> eventList = mDataManager.getPlaceList(location, PlaceCategoryFilter.HOTEL, "8","");// see parameters
		assertNotNull(eventList);
		assertTrue(eventList.size() > 0);
		
		final Place place = eventList.get(0);
		
		final CountDownLatch latch = new CountDownLatch(1);
		
		mPlaceActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				mPlaceActivity.showPlace(place);
				latch.countDown();
			}
		});
       
       	try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
		
	}// end testDataProvider
	
	public void testStateDestroy() {
		mPlaceActivity.finish();
		mPlaceActivity = this.getActivity();
		assertNotNull(mPlaceActivity);
	}// end testStateDestroy
	
	@UiThreadTest
	public void testStatePause() {
		Instrumentation instr = this.getInstrumentation();
		assertNotNull(instr);
		
		instr.callActivityOnPause(mPlaceActivity);
		instr.callActivityOnResume(mPlaceActivity);
	}// end testStatePause	

}// end PlaceDetailsActivityTest
