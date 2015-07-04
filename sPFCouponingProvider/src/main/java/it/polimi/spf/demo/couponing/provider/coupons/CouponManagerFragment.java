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

package it.polimi.spf.demo.couponing.provider.coupons;

import java.util.List;

import android.content.Intent;
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
	private static final int UPDATECOUPONLIST = 123;
	private RecyclerView mRecyclerView;

	@Getter
	private CouponAdapter mAdapter;


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
			CouponList.getInstance().getCouponList().addAll(coupons);
			mAdapter.notifyDataSetChanged();
		}

		@Override
		public void onLoaderReset(Loader<List<Coupon>> loader) {
			// Do nothing
		}
	};

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

		return root;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

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
			startActivityForResult(i, UPDATECOUPONLIST);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}



	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("onActivityResult","size: " + CouponList.getInstance().getCouponList().size());
		if(requestCode==UPDATECOUPONLIST) {
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
			Coupon c = CouponList.getInstance().getCouponList().get(clickedPosition);
			Intent i = CouponDetailActivity.newIntent(getActivity(), c.getId());
			startActivity(i);
		}
	}
}
