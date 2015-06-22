package it.polimi.spf.demo.couponing.provider;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

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
