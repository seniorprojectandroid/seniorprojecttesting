package edu.fiu.cs.seniorproject.test;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import edu.fiu.cs.seniorproject.MyEventsActivity;
import edu.fiu.cs.seniorproject.data.MbGuideDB;

public class MyEventsActivityTest extends ActivityInstrumentationTestCase2<MyEventsActivity>{
	
	MbGuideDB mb = null;
	ArrayList<String> eList = null;
	
	private MyEventsActivity mEventsActivity = null;
	

	public MyEventsActivityTest() {
		super(MyEventsActivity.class);
		// TODO Auto-generated constructor stub
	}	
	
	@Override
	protected void setUp() {
		setActivityInitialTouchMode(false);
		mEventsActivity = this.getActivity();
		this.mb = new MbGuideDB(mEventsActivity);
		this.mb.openDatabase();		
	}// end setUp
	
		
	public void testPreconditions() {
		assertNotNull(mEventsActivity);
		assertNotNull(mb);
	}// end testPreconditions
	
	// test that the activity will destroy and recreate
	public void testStateDestroy() {
		mEventsActivity.finish();
		mEventsActivity = this.getActivity();
		assertNotNull(mEventsActivity);
	}// end testStateDestroy
	
	@UiThreadTest
	public void testStatePause() {
		Instrumentation instr = this.getInstrumentation();
		assertNotNull(instr);		
		instr.callActivityOnPause(mEventsActivity);
		instr.callActivityOnResume(mEventsActivity);
		assertNotNull(mEventsActivity);
	}// end testStatePause
	
		
	public void testDataSyncronous() {
		
		this.mb.openDatabase();	
		
		final ArrayList<String> List = mb.listEventNames();	
		
		this.mb.closeDatabase();
		
		assertNotNull(List);
		
		final CountDownLatch latch = new CountDownLatch(1);
		
		mEventsActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {				
				mEventsActivity.showEventsList(List);
				latch.countDown();
			}
		});
       
       	try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}// end testDataSyncronous

	

}
