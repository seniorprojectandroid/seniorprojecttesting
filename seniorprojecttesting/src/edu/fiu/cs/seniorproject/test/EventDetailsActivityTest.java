package edu.fiu.cs.seniorproject.test;


import java.util.List;
import java.util.concurrent.CountDownLatch;

import edu.fiu.cs.seniorproject.EventDetailsActivity;
import edu.fiu.cs.seniorproject.data.DateFilter;
import edu.fiu.cs.seniorproject.data.Event;
import edu.fiu.cs.seniorproject.data.EventCategoryFilter;
import edu.fiu.cs.seniorproject.data.Location;
import edu.fiu.cs.seniorproject.manager.AppLocationManager;
import edu.fiu.cs.seniorproject.manager.DataManager;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class EventDetailsActivityTest extends ActivityInstrumentationTestCase2<EventDetailsActivity>{

	private EventDetailsActivity mEventActivity = null;
	private DataManager mDataManager = null;
	
	public EventDetailsActivityTest() {
		super(EventDetailsActivity.class);
	}
	
	@Override
	protected void setUp() {
		setActivityInitialTouchMode(false);
		mEventActivity = getActivity();
		mDataManager = DataManager.getSingleton();
	}
	
	public void testPreconditions() {
		assertNotNull(mEventActivity);
		assertNotNull(mDataManager);
	}
	
	@UiThreadTest
	public void testActivityUI() {
		ImageView img = (ImageView)mEventActivity.findViewById(edu.fiu.cs.seniorproject.R.id.image);
		assertNotNull(img);
				
		TextView tvn = (TextView)mEventActivity.findViewById(edu.fiu.cs.seniorproject.R.id.event_name);
		assertNotNull(tvn);
		
		TextView tvp = (TextView)mEventActivity.findViewById(edu.fiu.cs.seniorproject.R.id.event_place);
		assertNotNull(tvp);
		
		TextView tvt = (TextView)mEventActivity.findViewById(edu.fiu.cs.seniorproject.R.id.event_time);
		assertNotNull(tvt);
		
		TextView tvd = (TextView)mEventActivity.findViewById(edu.fiu.cs.seniorproject.R.id.event_distance);
		assertNotNull(tvd);
		
		TextView tvds = (TextView)mEventActivity.findViewById(edu.fiu.cs.seniorproject.R.id.event_description);
		assertNotNull(tvds);
		
		
		View vw = (View)mEventActivity.findViewById(edu.fiu.cs.seniorproject.R.id.mapview);
		assertNotNull(vw);
				
	}
	
	public void testDataProvider() {
		assertNotNull(mDataManager);
		
		android.location.Location currentLocation = AppLocationManager.getCurrentLocation();
		Location location = new Location( String.valueOf( currentLocation.getLatitude() ), String.valueOf(currentLocation.getLongitude()) );
		
		final List<Event> eventList = mDataManager.getEventList(location, EventCategoryFilter.Music, "10", null,DateFilter.THIS_WEEK);
		assertNotNull(eventList);
		assertTrue(eventList.size() > 0);
		
		final Event event = eventList.get(0);
		
		final CountDownLatch latch = new CountDownLatch(1);
		
		mEventActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				mEventActivity.showEvent(event);
				latch.countDown();
			}
		});
       
       	try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	public void testStateDestroy() {
		mEventActivity.finish();
		mEventActivity = this.getActivity();
		assertNotNull(mEventActivity);
	}
	
	@UiThreadTest
	public void testStatePause() {
		Instrumentation instr = this.getInstrumentation();
		assertNotNull(instr);
		
		instr.callActivityOnPause(mEventActivity);
		instr.callActivityOnResume(mEventActivity);
	}
	
	
	
	
}// end EventDetailsActivity

