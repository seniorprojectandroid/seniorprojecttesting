package edu.fiu.cs.seniorproject.test;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import edu.fiu.cs.seniorproject.MyPlacesActivity;
import edu.fiu.cs.seniorproject.data.MbGuideDB;
import android.app.Instrumentation;


public class MyPlaceActivityTest extends ActivityInstrumentationTestCase2<MyPlacesActivity>{
	
	MbGuideDB mb = null;
	ArrayList<String> pList = null;
	
	private MyPlacesActivity mMyPlacesActivity = null;
	
	public MyPlaceActivityTest() {
		super(MyPlacesActivity.class);
		// TODO Auto-generated constructor stub
	}// end MyPlaceActivityTest
	
	@Override
	protected void setUp() {
		setActivityInitialTouchMode(false);
		mMyPlacesActivity = this.getActivity();
		this.mb = new MbGuideDB(mMyPlacesActivity);
			
	}// end setUp
	
	
	public void testPreconditions() {
		assertNotNull(mMyPlacesActivity);
		assertNotNull(mb);
	}// end testPreconditions
	
	// test that the activity will destroy and recreate
	public void testStateDestroy() {
		mMyPlacesActivity.finish();
		mMyPlacesActivity = this.getActivity();
		assertNotNull(mMyPlacesActivity);
	}// end testStateDestroy
	
	@UiThreadTest
	public void testStatePause() {
		Instrumentation instr = this.getInstrumentation();
		assertNotNull(instr);		
		instr.callActivityOnPause(mMyPlacesActivity);
		instr.callActivityOnResume(mMyPlacesActivity);
		assertNotNull(mMyPlacesActivity);
	}// end testStatePause
	
		
	public void testDataSyncronous() {
		
		this.mb.openDatabase();	
		
		final ArrayList<String> List = mb.listPlaceNames();	
		
		this.mb.closeDatabase();
		
		final CountDownLatch latch = new CountDownLatch(1);
		
		mMyPlacesActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {				
				mMyPlacesActivity.showPlaceList(List);
				latch.countDown();
			}
		});
       
       	try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}// end testDataSyncronous

}// end class
