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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.polimi.spf.lib.profile.SPFLocalProfile;
import it.polimi.spf.shared.model.ProfileField;
import it.polimi.spf.shared.model.ProfileFieldContainer;
import it.polimi.spf.shared.model.SPFError;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CategoryFragment extends Fragment {

	public static CategoryFragment newInstance() {
		return new CategoryFragment();
	}
	
    private static final String TAG = "CategoryFragment";
    private SPFLocalProfile mLocalProfile;
    private ProfileFieldContainer mContainer;

    private ListView mList;

    private final SPFLocalProfile.Callback mProfileCallbacks = new SPFLocalProfile.Callback() {

        @Override
        public void onServiceReady(SPFLocalProfile service) {
            mLocalProfile = service;
            mContainer = mLocalProfile.getValueBulk(ProfileField.INTERESTS);
            onDataReady();
        }

        @Override
        public void onError(SPFError errorMsg) {
            Log.e(TAG, "Error in local profile: " + errorMsg);
            mLocalProfile = null;
        }

        @Override
        public void onDisconnect() {
            mLocalProfile = null;
        }
    };

    private final AdapterView.OnItemClickListener mCategoryListener = new AdapterView.OnItemClickListener() {
    	
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            SparseBooleanArray checkedPosititions = mList.getCheckedItemPositions();
            String[] interestsArray = mContainer.getFieldValue(ProfileField.INTERESTS);
            if(interestsArray == null){
            	interestsArray = new String[0];
            }
            
            List<String> interests = new ArrayList<String>(Arrays.asList(interestsArray));
            String newInterest = (String) parent.getItemAtPosition(position);
            
            if(checkedPosititions.get(position)){
            	interests.add(newInterest);
            } else {
            	interests.remove(newInterest);
            }

            mContainer.setFieldValue(ProfileField.INTERESTS, interests.toArray(new String[interests.size()]));
            getActivity().invalidateOptionsMenu();
        }
    };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_category_list, container, false);

        TextView mEmptyView = (TextView) root.findViewById(R.id.category_list_empty);
		mList = (ListView) root.findViewById(R.id.category_list);
        mList.setEmptyView(mEmptyView);
		
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
        SPFLocalProfile.load(getActivity(), mProfileCallbacks);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mLocalProfile != null) {
            mLocalProfile.disconnect();
        }
    }

    private void onDataReady() {
    	String[] categories = ProviderApplication.get().getCouponDatabase().getCategories();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
        		getActivity(),
        		android.R.layout.simple_list_item_multiple_choice,
        		categories);
        mList.setAdapter(adapter);

        //FIXME nullpointerexception somewhere, probably on mContainer,
        //FIXME please fix this if you found this bug
        String[] interests = mContainer.getFieldValue(ProfileField.INTERESTS);
        if(interests == null){
            interests = new String[0];
        }

        for (String interest : interests) {
            int i = adapter.getPosition(interest);
            if (i > -1) {
                mList.setItemChecked(i, true);
            }
        }

        mList.setOnItemClickListener(mCategoryListener);
    }

    public void onSave() {
        if (!mContainer.isModified()) {
            return;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    	if(mContainer != null && mContainer.isModified()){
    		inflater.inflate(R.menu.menu_category, menu);
    	}
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_category_save && mContainer.isModified()) {
            mLocalProfile.setValueBulk(mContainer);
            mContainer.clearModified();
            getActivity().invalidateOptionsMenu();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
