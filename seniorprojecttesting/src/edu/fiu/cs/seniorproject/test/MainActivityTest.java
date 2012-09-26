package edu.fiu.cs.seniorproject.test;

import edu.fiu.cs.seniorproject.MainActivity;
import android.test.ActivityInstrumentationTestCase2;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

	private MainActivity mActivity = null;
	
	public MainActivityTest(Class<MainActivity> activityClass) {
		super(activityClass);
	}
	
	public MainActivityTest() {
		super(MainActivity.class);
	}
	
	@Override
	protected void setUp() {
		mActivity = getActivity();
		
	}
	
	public void testActivity() {
		assertNotNull(mActivity);
	}
}
