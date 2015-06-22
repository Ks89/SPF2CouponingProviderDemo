package it.polimi.spf.demo.couponing.provider.coupons;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import it.polimi.spf.demo.couponing.provider.Coupon;
import it.polimi.spf.demo.couponing.provider.ProviderApplication;
import it.polimi.spf.demo.couponing.provider.R;
import it.polimi.spf.demo.couponing.provider.creation.CouponCreationActivity;
import it.polimi.spf.demo.couponing.provider.detail.CouponDetailActivity;
import lombok.Getter;

public class CouponManagerFragment extends Fragment implements
		//ItemClickListener is the interface in the adapter to intercept item's click events.
		//I use this to call itemClicked(v) in this class from CouponAdapter.
		CouponAdapter.ItemClickListener {

	private static final int LOADER_COUPON_ID = 0;
	private RecyclerView mRecyclerView;

	//	private ListView mCouponList;
	@Getter
	private CouponAdapter mAdapter;
	private TextView mEmpty;


	public static CouponManagerFragment newInstance() {
		return new CouponManagerFragment();
	}

	private LoaderManager.LoaderCallbacks<List<Coupon>> mLoaderCallbacks = new LoaderManager.LoaderCallbacks<List<Coupon>>() {

		@Override
		public Loader<List<Coupon>> onCreateLoader(int id, Bundle args) {
			return new AsyncTaskLoader<List<Coupon>>(getActivity()) {

				@Override
				public List<Coupon> loadInBackground() {
					return ProviderApplication.get().getCouponDatabase().getAllCoupons();
				}
			};
		}

		@Override
		public void onLoadFinished(Loader<List<Coupon>> loader, List<Coupon> coupons) {
			CouponList.getInstance().getCouponList().clear();
//			mAdapter.clear();
//			if (coupons.size() == 0) {
//				mEmpty.setText(R.string.coupon_list_empty);
//			} else {
				CouponList.getInstance().getCouponList().addAll(coupons);
				mAdapter.notifyDataSetChanged();
//			}
		}

		@Override
		public void onLoaderReset(Loader<List<Coupon>> loader) {
			// Do nothing
		}
	};

//	/**
//	 * Listener for click on list's items.
//	 */
//	private final OnItemClickListener mCouponClickListener = new OnItemClickListener() {
//
//		@Override
//		public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
//			Coupon c = (Coupon) parent.getItemAtPosition(position);
//			Intent i = CouponDetailActivity.newIntent(getActivity(), c.getId());
//			startActivity(i);
//		}
//	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_coupon_recyclerview, container, false);

		mRecyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);

		LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
		mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mRecyclerView.setLayoutManager(mLayoutManager);

		// allows for optimizations if all item views are of the same size:
		mRecyclerView.setHasFixedSize(true);

		mAdapter = new CouponAdapter(this);
		mRecyclerView.setAdapter(mAdapter);
		mRecyclerView.setItemAnimator(new DefaultItemAnimator());


		//		mCouponList = (ListView) root.findViewById(R.id.coupon_list);
//		mEmpty = (TextView) root.findViewById(R.id.coupon_list_empty);
//		mCouponList.setEmptyView(mEmpty);
//		mCouponList.setOnItemClickListener(mCouponClickListener );


		return root;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

//		mAdapter = new CouponAdapterOld(getActivity());
//		mCouponList.setAdapter(mAdapter);
		
		setHasOptionsMenu(true);
	}

	@Override
	public void onResume() {
		super.onResume();
		getLoaderManager().initLoader(LOADER_COUPON_ID, null, mLoaderCallbacks).forceLoad();
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_coupon_list, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_coupon_add) {
			Intent i = CouponCreationActivity.newIntent(getActivity());
			startActivityForResult(i, 123);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("onActivityResult","size: " + CouponList.getInstance().getCouponList().size());
		if(requestCode==123) {
			getLoaderManager().initLoader(LOADER_COUPON_ID, null, mLoaderCallbacks).forceLoad();
			Log.d("onActivityResult","size: " + CouponList.getInstance().getCouponList().size());

		}
	}

	/**
	 * Method called by {@link it.polimi.spf.demo.couponing.provider.coupons.CouponAdapter}
	 * with the {@link it.polimi.spf.demo.couponing.provider.coupons.CouponAdapter.ItemClickListener}
	 * interface, when the user click on an element of the {@link android.support.v7.widget.RecyclerView}.
	 * @param view The clicked view.
	 */
	@Override
	public void itemClicked(View view) {
		int clickedPosition = mRecyclerView.getChildLayoutPosition(view);

		if(clickedPosition>=0) { //a little check :)
//			((DeviceClickListener) getActivity()).tryToConnectToAService(clickedPosition);
			Coupon c = CouponList.getInstance().getCouponList().get(clickedPosition);
			Intent i = CouponDetailActivity.newIntent(getActivity(), c.getId());
			startActivity(i);
		}
	}
}
