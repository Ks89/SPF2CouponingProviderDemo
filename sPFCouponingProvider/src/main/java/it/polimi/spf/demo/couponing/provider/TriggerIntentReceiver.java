/*
 * Copyright 2014 Jacopo Aliprandi, Dario Archetti
 * Copyright 2015 Stefano Cappa
 *
 * This file is part of SPF.
 *
 * SPF is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * SPF is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with SPF.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package it.polimi.spf.demo.couponing.provider;

import it.polimi.spf.lib.SPF;
import it.polimi.spf.lib.SPFPerson;
import it.polimi.spf.lib.search.SPFSearch;
import it.polimi.spf.lib.services.ServiceInvocationException;
import it.polimi.spf.shared.model.SPFActionIntent;
import it.polimi.spf.shared.model.SPFError;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import java.util.List;

public class TriggerIntentReceiver extends BroadcastReceiver {

	protected static final String TAG = "TriggerIntentReceiver";

	protected static final int MESSAGE_SEARCH = 0;
	protected static final int MESSAGE_EXECUTE = 1;

	private Handler mHandler;

	private Handler.Callback mHandlerCallback = new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			HandlerData data = (HandlerData) msg.obj;

			Log.d(TAG, "handleMessage: " + msg.toString());
			Log.d(TAG, "handleMessage data id: " + data.extras.getLong(SPFActionIntent.ARG_LONG_TRIGGER_ID));

//			data.spf.setGoIntentValue(15, getTargetIdentifier(data.extras));

			List<Coupon> list = ProviderApplication.get().getCouponDatabase().getAllCoupons();

			for(Coupon coupon : list) {
				Log.d(TAG, "ELEMENT LISTA " + coupon.toString());
			}

			switch (msg.what) {
				case MESSAGE_SEARCH:
					Log.d(TAG, "MESSAGE_SEARCH");
					SPFSearch search = data.spf.getComponent(SPF.SEARCH);
					String identifier = getTargetIdentifier(data.extras);
					SPFPerson target = search.lookup(identifier);
					if (target == null) {
						Log.e(TAG, "Person " + identifier + " not found after second search");
						return true;
					}

					data.person = target;
					mHandler.obtainMessage(MESSAGE_EXECUTE, data).sendToTarget();
					return true;
				case MESSAGE_EXECUTE:
					Log.d(TAG, "MESSAGE_EXECUTE");
					CouponDeliveryService svc = data.person.getServiceInterface(CouponDeliveryService.class, data.spf);

					CouponDatabase db = ProviderApplication.get().getCouponDatabase();
					long triggerId = getTriggerId(data.extras);
					Coupon coupon = db.getCouponByTriggerId(triggerId);

					if(coupon == null){
						Log.e(TAG, "No coupon found for trigger id " + triggerId);
						return true;
					}

					coupon.setTriggerId(-1);
					coupon.setId(-1);

					try {
						//Couponing Provider sends this coupon to clients.
						svc.deliverCoupon(coupon);
					} catch(ServiceInvocationException e){
						Log.e(TAG, "Could not deliver coupon " + coupon + " to " + data.person.getIdentifier(), e);
					}

					Log.d(TAG, "Coupon " + coupon + " delivered to " + data.person.getIdentifier());
					data.spf.disconnect();
					return true;
				default:
					return false;
				}
		}
	};

	public TriggerIntentReceiver() {
		HandlerThread th = new HandlerThread("TriggerIntentReceiver");
		th.start();
		mHandler = new Handler(th.getLooper(), mHandlerCallback);
	}

	@Override
	public void onReceive(Context context, final Intent intent) {
		Log.d(TAG, "Intent received: " + intent);

		SPF.connect(ProviderApplication.get(), new SPF.ConnectionListener() {

			@Override
			public void onError(SPFError error) {
				Log.e(TAG, "Error in SPF: " + error);
			}

			@Override
			public void onDisconnected() {
				// Do nothing
			}

			@Override
			public void onConnected(SPF instance) {
				SPFSearch search = instance.getComponent(SPF.SEARCH);
				HandlerData data = new HandlerData();
				data.spf = instance;
				data.extras = intent.getExtras();

				SPFPerson target = search.lookup(getTargetIdentifier(data.extras));

				if (target == null) {
					// Try postponing the request
					Log.d(TAG,"Request postponed because target is null");
					Message message = mHandler.obtainMessage(MESSAGE_SEARCH, data);
					mHandler.sendMessageDelayed(message, 2000);
				} else {
					data.person = target;
					mHandler.obtainMessage(MESSAGE_EXECUTE, data).sendToTarget();
				}
			}
		});
	}

	private class HandlerData {
		public SPF spf;
		public Bundle extras;
		public SPFPerson person;
	}

	private long getTriggerId(Bundle data) {
		return data.getLong(SPFActionIntent.ARG_LONG_TRIGGER_ID);
	}

	private String getTargetIdentifier(Bundle data) {
		return data.getString(SPFActionIntent.ARG_STRING_TARGET);
	}

}
