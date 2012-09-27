package edu.fiu.cs.seniorproject.test;

import edu.fiu.cs.seniorproject.MainActivity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
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
		mButton = (Button)mActivity.findViewById(edu.fiu.cs.seniorproject.R.id.events_button);
	}
	
	public void testPreconditions() {
		assertNotNull(mActivity);
		assertTrue(mButton != null);
	}
	
	public void testButtonUI() {
		
		mActivity.runOnUiThread(
	            new Runnable() {
	                public void run() {
	                    mButton.setText("Title!!");
	                    String buttonText = (String) mButton.getText();
	            		assertTrue( buttonText.equals("Title!!"));
	                }
	            }
	        );
	}
	
	public void testStateDestroy() {
		mActivity.finish();
		mActivity = this.getActivity();
		assertNotNull(mActivity);
	}
	
	public void testStatePause() {
		Instrumentation instr = this.getInstrumentation();
		assertNotNull(instr);
		
		instr.callActivityOnPause(mActivity);
		//mButton.setText("NewTitle");
		instr.callActivityOnResume(mActivity);
		String buttonText = (String) mButton.getText();
		assertTrue( buttonText.equals("Events") );
	}
}
