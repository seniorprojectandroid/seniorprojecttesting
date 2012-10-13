package edu.fiu.cs.seniorproject.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import edu.fiu.cs.seniorproject.PlacesActivity;
import edu.fiu.cs.seniorproject.data.Location;
import edu.fiu.cs.seniorproject.data.Place;
import edu.fiu.cs.seniorproject.manager.AppLocationManager;
import edu.fiu.cs.seniorproject.manager.DataManager;
import edu.fiu.cs.seniorproject.manager.DataManager.ConcurrentPlaceListLoader;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.ListView;

public class PlacesActivityTest extends
		ActivityInstrumentationTestCase2<PlacesActivity> {

	private PlacesActivity mActivity = null;
	
	public PlacesActivityTest() {
		super(PlacesActivity.class);
	}
	
	@Override
	protected void setUp() {
		setActivityInitialTouchMode(false);
		mActivity = this.getActivity();
	}
	
	public void testPreconditions() {
		assertNotNull(mActivity);
	}
	
	// test that the activity will destroy and recreate
	public void testStateDestroy() {
		mActivity.finish();
		mActivity = this.getActivity();
		assertNotNull(mActivity);
	}
	
	@UiThreadTest
	public void testStatePause() {
		Instrumentation instr = this.getInstrumentation();
		assertNotNull(instr);
		
		instr.callActivityOnPause(mActivity);
		instr.callActivityOnResume(mActivity);
		assertNotNull(mActivity);
	}
	
	@UiThreadTest
	public void testUI() {
		assertNotNull(mActivity.findViewById(android.R.id.list));
		assertNotNull(mActivity.findViewById(android.R.id.empty));
		assertNotNull(mActivity.findViewById(android.R.id.progress));
	}
	
	public void testDataSyncronous() {
		android.location.Location currentLocation = AppLocationManager.getCurrentLocation();
		Location location = new Location( String.valueOf( currentLocation.getLatitude() ), String.valueOf(currentLocation.getLongitude()) );
		
		final List<Place> places = DataManager.getSingleton().getPlaceList(location, null, "8", null);
		assertNotNull(places);
		assertTrue( places.size() > 0 );
		
		final CountDownLatch latch = new CountDownLatch(1);
		
		mActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				mActivity.showPlaceList(places);
				ListView lv = (ListView)mActivity.findViewById(android.R.id.list);
				assertNotNull(lv);
				assertNotNull(lv.getAdapter());
				latch.countDown();
			}
		});
       
       	try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void testDataASyncronous() {
		android.location.Location currentLocation = AppLocationManager.getCurrentLocation();
		Location location = new Location( String.valueOf( currentLocation.getLatitude() ), String.valueOf(currentLocation.getLongitude()) );
		
		ConcurrentPlaceListLoader loader = DataManager.getSingleton().getConcurrentPlaceList(location, null, "1", null);
		assertNotNull(loader);
		
		List<Place> list = new ArrayList<Place>();
		List<Place> iter = null;
		
		while( ( iter = loader.getNext()) != null ) {
			list.addAll(iter);
		}
		
		final List<Place> places = list;
		assertNotNull(places);
		assertTrue(places.size() > 0 );
		
		final CountDownLatch latch = new CountDownLatch(1);
		
		mActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
								
			   assertNotNull(mActivity);
			   mActivity.showPlaceList(places);
			   ListView lv = (ListView)mActivity.findViewById(android.R.id.list);
			   assertNotNull(lv);
			   assertNotNull(lv.getAdapter());
			   latch.countDown();
			}
		});
		
	   try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
