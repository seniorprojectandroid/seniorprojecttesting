package edu.fiu.cs.seniorproject.test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import org.json.JSONException;
import org.json.JSONObject;

import edu.fiu.cs.seniorproject.FacebookLoginActivity;
import edu.fiu.cs.seniorproject.manager.FacebookManager;
import edu.fiu.cs.seniorproject.manager.FacebookManager.IRequestResult;
import android.test.ActivityInstrumentationTestCase2;

public class FacebookLoginActivityTest extends ActivityInstrumentationTestCase2<FacebookLoginActivity> {

	private FacebookLoginActivity mActivity = null;
	
	public FacebookLoginActivityTest() {
		super(FacebookLoginActivity.class);
	}

	@Override
	protected void setUp() {
		mActivity = getActivity();
	}
	
	public void testPreconditions() {
		assertNotNull(mActivity);
	}
	
	public void testLogin() {
		final CountDownLatch latch = new CountDownLatch(1);
		final AtomicBoolean loginResult = new AtomicBoolean(false);
		final FacebookManager fManager = new FacebookManager();
		
		mActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {				
				
		        fManager.login(mActivity, new String[] {"publish_stream"}, new IRequestResult() {
					
					@Override
					public void onComplete(boolean success) {
						loginResult.set(success);
						latch.countDown();
					}
		        });
			}
		});
		
		// wait for the login to complete
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		assertTrue(loginResult.get());
		
		// get user info
		String userInfo = null;
		try {
			userInfo = fManager.getFacebookClient().request("me");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		assertNotNull(userInfo);
		assertTrue(!userInfo.isEmpty());
		
		JSONObject info = null;
		try {
			info = new JSONObject(userInfo);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		assertTrue(info != null && info.length() > 0 );
		assertTrue(info.has("id"));
	}
}
