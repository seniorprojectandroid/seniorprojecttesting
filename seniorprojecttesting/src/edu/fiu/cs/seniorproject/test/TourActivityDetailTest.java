package edu.fiu.cs.seniorproject.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import android.app.Instrumentation;
import android.content.res.XmlResourceParser;
import android.support.v4.view.ViewPager;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import edu.fiu.cs.seniorproject.R;
import edu.fiu.cs.seniorproject.TourActivityDetails;
import edu.fiu.cs.seniorproject.data.Place;
import edu.fiu.cs.seniorproject.utils.XMLParser;

public class TourActivityDetailTest extends ActivityInstrumentationTestCase2<TourActivityDetails>{
	
	private TourActivityDetails mTourActivityDetails = null;
	XMLParser myparse = null;
	
	XmlResourceParser stringXmlContent = null;

	public TourActivityDetailTest() {
		super(TourActivityDetails.class);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void setUp() {
		setActivityInitialTouchMode(false);
		mTourActivityDetails = getActivity();		
	}// end setUp
	
	public void testPreconditions() {
		assertNotNull(mTourActivityDetails);		
	}// testPreconditions
	
	//@UiThreadTest
	public void testActivityUI() {
		assertNotNull(mTourActivityDetails);		
		ViewPager pager = (ViewPager)mTourActivityDetails.findViewById(edu.fiu.cs.seniorproject.R.id.pager);
		assertNotNull(pager);
		
	}// end testActivityUI
	
public void testDataSyncronous() {		
		
		myparse = new XMLParser();
   		stringXmlContent = myparse.getXMLFromSRC(mTourActivityDetails,R.xml.tours);
   		final List<Place> placeList = myparse.getTourByName(stringXmlContent, "Restaurants");  		
   		
   		
		assertNotNull(placeList);
		assertTrue( placeList.size() > 0 );
		
		final CountDownLatch latch = new CountDownLatch(1);	
		
		
		mTourActivityDetails.runOnUiThread(new Runnable() {		
						
			@Override
			public void run() {
				
				final Place tourname = placeList.get(0);
				
				final List<Place> testlist = new ArrayList<Place>();
						
				testlist.add(tourname);
				
				mTourActivityDetails.testActivity(testlist);				
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
		mTourActivityDetails.finish();
		mTourActivityDetails = this.getActivity();
		assertNotNull(mTourActivityDetails);
	}
	
	@UiThreadTest
	public void testStatePause() {
		Instrumentation instr = this.getInstrumentation();
		assertNotNull(instr);
		
		instr.callActivityOnPause(mTourActivityDetails);
		instr.callActivityOnResume(mTourActivityDetails);
	}


}
