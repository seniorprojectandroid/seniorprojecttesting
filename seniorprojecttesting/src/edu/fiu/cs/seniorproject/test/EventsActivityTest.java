package edu.fiu.cs.seniorproject.test;

import java.util.List;

import edu.fiu.cs.seniorproject.EventsActivity;
import edu.fiu.cs.seniorproject.data.Event;
import edu.fiu.cs.seniorproject.manager.DataManager;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class EventsActivityTest extends
		ActivityInstrumentationTestCase2<EventsActivity> {

	private EventsActivity mActivity = null;
	private DataManager mDataManager = null;
	
	public EventsActivityTest() {
		super(EventsActivity.class);
	}
	
	@Override
	protected void setUp() {
		setActivityInitialTouchMode(false);
		mActivity = getActivity();
		mDataManager = DataManager.getSingleton();
	}
	
	public void testPreconditions() {
		assertNotNull(mActivity);
		assertNotNull(mDataManager);
	}
	
	public void testActivityUI() {
		ListView lv = (ListView)mActivity.findViewById(android.R.id.list);
		assertNotNull(lv);
		assertTrue(lv.getVisibility() == View.GONE);
		
		TextView tv = (TextView)mActivity.findViewById(android.R.id.empty);
		assertNotNull(tv);
		assertTrue(tv.getVisibility() == View.GONE);
		
		ProgressBar pb = (ProgressBar)mActivity.findViewById(android.R.id.progress);
		assertNotNull(pb);
		assertTrue(pb.getVisibility() == View.VISIBLE);
	}
	
	public void testDataProvider() {
		assertNotNull(mDataManager);
		final List<Event> eventList = mDataManager.getEventList(null, null, null, null);
		assertNotNull(eventList);
		assertTrue(eventList.size() > 0);
	}
	
	public void testBitmapDownload() {
		assertNotNull(mDataManager);
		mDataManager.downloadBitmap("http://www.miamibeachapi.com/resources/miami-vca.jpg", null);
	}
}
