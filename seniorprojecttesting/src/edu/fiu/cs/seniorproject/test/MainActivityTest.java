package edu.fiu.cs.seniorproject.test;

import java.util.concurrent.CountDownLatch;

import edu.fiu.cs.seniorproject.MainActivity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.TextView;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

	private MainActivity mActivity = null;
	private TextView mButton = null;
	
	public MainActivityTest() {
		super(MainActivity.class);
	}
	
	@Override
	protected void setUp() {
		setActivityInitialTouchMode(false);
		mActivity = this.getActivity();		
		mButton = (TextView)mActivity.findViewById(edu.fiu.cs.seniorproject.R.id.facebookImageButton);
		mButton = (TextView)mActivity.findViewById(edu.fiu.cs.seniorproject.R.id.mapImageButton);
		mButton = (TextView)mActivity.findViewById(edu.fiu.cs.seniorproject.R.id.eventsImageButton);
		mButton = (TextView)mActivity.findViewById(edu.fiu.cs.seniorproject.R.id.placesImageButton);
		mButton = (TextView)mActivity.findViewById(edu.fiu.cs.seniorproject.R.id.my_eventsImageButton);
		mButton = (TextView)mActivity.findViewById(edu.fiu.cs.seniorproject.R.id.my_placesImageButton);
		
	}
	
	public void testPreconditions() {
		assertNotNull(mActivity);
		assertNotNull(mButton);
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
//		String buttonText = (String) mButton.getText();
//		assertTrue( buttonText.equals("Events") );
	}
}
