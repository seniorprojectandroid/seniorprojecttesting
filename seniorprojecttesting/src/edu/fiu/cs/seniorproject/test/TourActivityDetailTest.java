package edu.fiu.cs.seniorproject.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import android.content.res.XmlResourceParser;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.view.View;

import android.widget.TextView;

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
	
	@UiThreadTest
	public void testActivityUI() {
		
		TextView name = (TextView)mTourActivityDetails.findViewById(edu.fiu.cs.seniorproject.R.id.tour_name);
		assertNotNull(name);
				
		TextView tvn = (TextView)mTourActivityDetails.findViewById(edu.fiu.cs.seniorproject.R.id.tour_telephone);
		assertNotNull(tvn);
		
		TextView tvloc = (TextView)mTourActivityDetails.findViewById(edu.fiu.cs.seniorproject.R.id.tour_address);
		assertNotNull(tvloc);
		
		TextView tvdist = (TextView)mTourActivityDetails.findViewById(edu.fiu.cs.seniorproject.R.id.tour_category);
		assertNotNull(tvdist);		
		
		View vw = (View)mTourActivityDetails.findViewById(edu.fiu.cs.seniorproject.R.id.mapview);
		assertNotNull(vw);		
		
	}// end testActivityUI
	
public void testDataSyncronous() {		
		
		myparse = new XMLParser();
   		stringXmlContent = myparse.getXMLFromSRC(mTourActivityDetails,R.xml.tours);
   		final List<Place> placeList = myparse.getTourByName(stringXmlContent, "restaurants");  		
   		
   		
		assertNotNull(placeList);
		assertTrue( placeList.size() > 0 );
		
		final CountDownLatch latch = new CountDownLatch(1);
		
		mTourActivityDetails.runOnUiThread(new Runnable() {
			
			final Place tourname = placeList.get(0);
			
			
			@Override
			public void run() {
				mTourActivityDetails.showPlace(tourname);				
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
