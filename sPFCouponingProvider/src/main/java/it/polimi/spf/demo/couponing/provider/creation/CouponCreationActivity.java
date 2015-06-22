package it.polimi.spf.demo.couponing.provider.creation;

import it.polimi.spf.demo.couponing.provider.Coupon;
import it.polimi.spf.demo.couponing.provider.ProviderApplication;
import it.polimi.spf.demo.couponing.provider.R;
import it.polimi.spf.lib.notification.SPFNotification;
import it.polimi.spf.shared.model.SPFAction;
import it.polimi.spf.shared.model.SPFActionIntent;
import it.polimi.spf.shared.model.SPFError;
import it.polimi.spf.shared.model.SPFQuery;
import it.polimi.spf.shared.model.SPFTrigger;
import lombok.Getter;
import lombok.Setter;

import java.io.ByteArrayOutputStream;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;

public class CouponCreationActivity extends AppCompatActivity {

	private static final String TAG = "CouponCreationActivity";
	private static final String TRIGGER_INTENT_ACTION = "it.polimi.spf.demo.couponing.COUPON_TRIGGERED";
	private static final long SLEEP_PERIOD = 60 * 1000;

	@Getter
	@Setter
	private Toolbar toolbar;

	private CouponCreationFragment couponCreationFragment;
	private SPFNotification mNotificationService;

	public static Intent newIntent(Context context) {
		Intent i = new Intent(context, CouponCreationActivity.class);
		return i;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent result) {
		Log.d(TAG, "Crop image calle by ProfileEditActivity");
		if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
			couponCreationFragment.beginCrop(result.getData());
		} else if (requestCode == Crop.REQUEST_CROP) {
			couponCreationFragment.handleCrop(resultCode, result);
		}
	}

	private SPFNotification.Callback mNotificationServiceCallback = new SPFNotification.Callback() {

		@Override
		public void onServiceReady(SPFNotification componentInstance) {
			mNotificationService = componentInstance;
			Log.d(TAG, "SPFNotification.Callback: onServiceReady");
		}

		@Override
		public void onError(SPFError err) {
			mNotificationService = null;
			Log.e(TAG, "SPFNotification.Callback: Error from notification service: " + err);
		}

		@Override
		public void onDisconnect() {
			mNotificationService = null;
			Log.d(TAG, "SPFNotification.Callback: Disconnected");
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_creation_2);

		this.setupToolBar();


		this.couponCreationFragment = CouponCreationFragment.newInstance();

		this.getSupportFragmentManager().beginTransaction()
				.replace(R.id.container_creation_root, this.couponCreationFragment , "couponCreationFragment")
				.commit();

		this.getSupportFragmentManager().executePendingTransactions();

//		if(mNotificationService == null) {
//			SPFNotification.load(this, mNotificationServiceCallback);
//		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		if(mNotificationService == null) {
			SPFNotification.load(this, mNotificationServiceCallback);
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();

		if(mNotificationService != null){
			mNotificationService.disconnect();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();

//		if(mNotificationService != null){
//			mNotificationService.disconnect();
//		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_coupon_creation, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		case R.id.action_coupon_save:
			onSave();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void onSave() {
		try {
//			checkInput();
			this.couponCreationFragment.checkInput();
//		} catch (InputException e) {
//			toast(e.getMessageResId());
//			return;
		} catch (CouponCreationFragment.InputException e) {
			e.printStackTrace();
		}

		Coupon coupon = new Coupon();
        coupon.setTitle(couponCreationFragment.getMTitleInput().getText().toString());
        coupon.setText(couponCreationFragment.getMTextInput().getText().toString());
        coupon.setCategory((String) couponCreationFragment.getMCategoryInput().getSelectedItem());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
		couponCreationFragment.getMPhoto().compress(Bitmap.CompressFormat.JPEG, 50, baos);
        coupon.setPhoto(baos.toByteArray());

        if(mNotificationService == null){
            toast(R.string.error_notification_service_unavailable);
            return;
        }

        SPFQuery query = new SPFQuery.Builder()
                .setTag(coupon.getCategory())
                .setAppIdentifier("it.polimi.spf.demo.couponing.client")
                .build();

        SPFAction action = new SPFActionIntent(TRIGGER_INTENT_ACTION);
        SPFTrigger trigger;
        try {
            trigger = new SPFTrigger("Coupon " + coupon.getTitle() + " trigger", query, action, SLEEP_PERIOD);
        } catch (SPFTrigger.IllegalTriggerException e) {
            toast(R.string.error_trigger_invalid);
            return;
        }

        if(!mNotificationService.saveTrigger(trigger)){
            toast(R.string.error_trigger_not_saved);
            return;
        }

        coupon.setTriggerId(trigger.getId());
        ProviderApplication.get().getCouponDatabase().saveCoupon(coupon);

		finish();
	}


	private void toast(int messageResId) {
		Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
	}


	/**
	 * Method to setup the {@link android.support.v7.widget.Toolbar}
	 * as supportActionBar in this {@link android.support.v7.app.ActionBarActivity}.
	 */
	private void setupToolBar() {
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		if (toolbar != null) {
			toolbar.setTitle(getResources().getString(R.string.app_name));
			toolbar.setTitleTextColor(Color.WHITE);
			toolbar.inflateMenu(R.menu.menu_coupon_creation);
			this.setSupportActionBar(toolbar);
		}
	}
}
