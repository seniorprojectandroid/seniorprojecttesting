package edu.fiu.cs.seniorproject.test;

import java.util.concurrent.CountDownLatch;

import edu.fiu.cs.seniorproject.MainActivity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.Button;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

	private MainActivity mActivity = null;
	private Button mButton = null;
	
	public MainActivityTest(Class<MainActivity> activityClass) {
		super(activityClass);
	}
	
	public MainActivityTest() {
		super(MainActivity.class);
	}
	
	@Override
	protected void setUp() {
		setActivityInitialTouchMode(false);
		mActivity = getActivity();
		mButton = (Button)mActivity.findViewById(edu.fiu.cs.seniorproject.R.id.eventsImageButton);
	}
	
	public void testPreconditions() {
		assertNotNull(mActivity);
		assertNotNull(mButton);
	}
	
	public void testButtonUI() {
		final CountDownLatch latch = new CountDownLatch(1);
		
		mActivity.runOnUiThread(
	            new Runnable() {
	                public void run() {
	                    mButton.setText("Title!!");
	                    String buttonText = (String) mButton.getText();
	            		assertTrue( buttonText.equals("Title!!"));
	            		latch.countDown();
	                }
	            }
	        );
		
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
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
		String buttonText = (String) mButton.getText();
		assertTrue( buttonText.equals("Events") );
	}
}
