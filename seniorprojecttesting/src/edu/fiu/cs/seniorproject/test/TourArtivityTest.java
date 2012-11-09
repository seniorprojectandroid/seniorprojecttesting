package edu.fiu.cs.seniorproject.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import android.app.Instrumentation;
import android.content.res.XmlResourceParser;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import edu.fiu.cs.seniorproject.R;
import edu.fiu.cs.seniorproject.TourActivity;
import edu.fiu.cs.seniorproject.utils.XMLParser;


public class TourArtivityTest extends ActivityInstrumentationTestCase2<TourActivity>{
	
	private TourActivity mTourActivity = null;
	XMLParser myparse = null;
	private List<String> tours = null;	
	XmlResourceParser stringXmlContent = null;

	public TourArtivityTest() {
		super(TourActivity.class);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void setUp() {
		setActivityInitialTouchMode(false);
		mTourActivity = this.getActivity();
	}
	
	public void testPreconditions() {
		assertNotNull(mTourActivity);
	}
	
	@UiThreadTest
	public void testUI() {
		assertNotNull(mTourActivity.findViewById(android.R.id.list));
		assertNotNull(mTourActivity.findViewById(android.R.id.empty));
		assertNotNull(mTourActivity.findViewById(android.R.id.progress));
	}
	
	public void testDataSyncronous() {
		
		tours = new ArrayList<String>();  
		myparse = new XMLParser();
   		stringXmlContent = myparse.getXMLFromSRC(mTourActivity,R.xml.tours);
   		tours = myparse.getTourName(stringXmlContent);  		
   		
   		
		assertNotNull(tours);
		assertTrue( tours.size() > 0 );
		
		final CountDownLatch latch = new CountDownLatch(1);
		
		mTourActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				mTourActivity.showEventList(tours);
				ListView lv = (ListView)mTourActivity.findViewById(android.R.id.list);
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
	
	
	
	@UiThreadTest
	public void testActivityUI() {
		ListView lv = (ListView)mTourActivity.findViewById(android.R.id.list);
		assertNotNull(lv);
		//assertTrue(lv.getVisibility() == View.GONE);
		
		TextView tv = (TextView)mTourActivity.findViewById(android.R.id.empty);
		assertNotNull(tv);
		//assertTrue(tv.getVisibility() == View.GONE);
		
		ProgressBar pb = (ProgressBar)mTourActivity.findViewById(android.R.id.progress);
		assertNotNull(pb);
		//assertTrue(pb.getVisibility() == View.VISIBLE);
				
	}	
	
	
	public void testStateDestroy() {
		mTourActivity.finish();
		mTourActivity = this.getActivity();
		assertNotNull(mTourActivity);
	}
	
	@UiThreadTest
	public void testStatePause() {
		Instrumentation instr = this.getInstrumentation();
		assertNotNull(instr);
		
		instr.callActivityOnPause(mTourActivity);
		instr.callActivityOnResume(mTourActivity);
	}
	

}
