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

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import lombok.Getter;
import lombok.Setter;

public class MainActivity extends AppCompatActivity {


	private TabFragment tabFragment;
	@Getter
	@Setter
	private Toolbar toolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		this.setupToolBar();

		tabFragment = TabFragment.newInstance();

		this.getSupportFragmentManager().beginTransaction()
				.replace(R.id.container_root, tabFragment, "tabfragment")
				.commit();

		this.getSupportFragmentManager().executePendingTransactions();



//		ViewPager pager = (ViewPager) findViewById(R.id.main_pager);
//		pager.setAdapter(new PagerConfigurator(this, getSupportFragmentManager()));
	}

//	private static class PagerConfigurator extends FragmentPagerAdapter {
//
//		private final static int PAGE_COUNT = 3;
//
//		private final String[] mPageTitles;
//
//		private PagerConfigurator(Context c, FragmentManager fm) {
//			super(fm);
//			this.mPageTitles = c.getResources().getStringArray(R.array.main_tabs_titles);
//		}
//
//		@Override
//		public Fragment getItem(int i) {
//			switch (i) {
//			case 0:
//				return CouponManagerFragment.newInstance();
//			case 1:
//				return CategoryFragment.newInstance();
//			case 2:
//				return WelcomeMessageFragment.newInstance();
//			default:
//				throw new IndexOutOfBoundsException("Requested page " + i + ", total " + PAGE_COUNT);
//			}
//		}
//
//		@Override
//		public CharSequence getPageTitle(int position) {
//			return mPageTitles[position];
//		}
//
//		@Override
//		public int getCount() {
//			return PAGE_COUNT;
//		}
//	}


	/**
	 * Method to setup the {@link android.support.v7.widget.Toolbar}
	 * as supportActionBar in this {@link android.support.v7.app.ActionBarActivity}.
	 */
	private void setupToolBar() {
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		if (toolbar != null) {
			toolbar.setTitle(getResources().getString(R.string.app_name));
			toolbar.setTitleTextColor(Color.WHITE);
			toolbar.inflateMenu(R.menu.menu_category);
			this.setSupportActionBar(toolbar);
		}
	}
}
