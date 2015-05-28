/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sgf.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.sgf.adapter.MusicAdapter;
import com.sgf.adapter.SongListAdapter;
import com.sgf.helper.MediaUtil;
import com.sgf.helper.SonglistDB;
import com.sgf.model.Music;
import com.sgf.model.SongList;
import com.sgf.mymusic.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the three primary sections of the app. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	AppSectionsPagerAdapter mAppSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will display the three primary sections of the
	 * app, one at a time.
	 */
	ViewPager mViewPager;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		// Create the adapter that will return a fragment for each of the three
		// primary sections
		// of the app.
		mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();

		// Specify that the Home/Up button should not be enabled, since there is
		// no hierarchical
		// parent.
		actionBar.setHomeButtonEnabled(false);

		// Specify that we will be displaying tabs in the action bar.
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Set up the ViewPager, attaching the adapter and setting up a listener
		// for when the
		// user swipes between sections.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mAppSectionsPagerAdapter);
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						// When swiping between different app sections, select
						// the corresponding tab.
						// We can also use ActionBar.Tab#select() to do this if
						// we have a reference to the
						// Tab.
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter.
			// Also specify this Activity object, which implements the
			// TabListener interface, as the
			// listener for when this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mAppSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}

	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the primary sections of the app.
	 */
	public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

		String str[] = { "音乐", "播放列表", "在线" };

		public AppSectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			switch (i) {
			case 0:
				// The first section of the app is the most interesting -- it
				// offers
				// a launchpad into the other demonstrations in this example
				// application.
				return new LaunchpadSectionFragment();
			case 1:
				return new DummySectionFragment1();
			default:
				return new DummySectionFragment2();
			}
		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			// return "Section " + (position + 1);
			return str[position];
		}
	}

	/**
	 * A fragment that launches other parts of the demo application.
	 */
	public static class LaunchpadSectionFragment extends Fragment {

		List<Music> musicList = new ArrayList<Music>();

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			final View rootView = inflater.inflate(
					R.layout.fragment_section_listview, container, false);
			musicList = MediaUtil.getMusicList(rootView.getContext());
			MusicAdapter musicAdapter = new MusicAdapter(rootView.getContext(),
					R.layout.music_item, musicList);
			ListView listView = (ListView) rootView
					.findViewById(R.id.musicList);
			listView.setAdapter(musicAdapter);

			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Music music = musicList.get(position);

					Intent intent = new Intent(rootView.getContext(),
							PlayMusicActivity.class);

					intent.putExtra("musicArtist", music.getArtist());
					intent.putExtra("url", music.getUrl());
					intent.putExtra("title", music.getTitle());
					intent.putExtra("position", position);
					startActivity(intent);
				}
			});

			return rootView;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment1 extends Fragment {

		private List<SongList> songlists = SonglistDB.init();

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_section_songlist, container, false);
			final SongListAdapter adapter = new SongListAdapter(
					rootView.getContext(), songlists);
			ListView listView = (ListView) rootView.findViewById(R.id.songlist);
			LinearLayout footView = (LinearLayout) inflater.inflate(
					R.layout.songlist_footer, null);
			footView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					AlertDialog.Builder builder = new AlertDialog.Builder(v
							.getContext());
					builder.setTitle("播放列表名称");
					LayoutInflater layoutInflater = LayoutInflater.from(v
							.getContext());
					final View view = layoutInflater.inflate(
							R.layout.custom_dialoglayout, null);
					builder.setView(view);

					builder.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									EditText songlistTitle = (EditText) view
											.findViewById(R.id.songlistTitle);
									String listTitle = songlistTitle.getText()
											.toString();

									SongList songList = new SongList(listTitle,
											null);
									songlists.add(songList);
									adapter.notifyDataSetChanged();

//									List<Music> musicList = new ArrayList<Music>();
//									musicList = MediaUtil.getMusicList(view
//											.getContext());

									Intent intent = new Intent(view
											.getContext(),
											AddMusicActivity.class);
									Bundle bundle = new Bundle();
									bundle.putSerializable("musiclist",
											(Serializable)MediaUtil.musicList);
									intent.putExtras(bundle);
									startActivity(intent);
								}
							});
					builder.setNeutralButton("取消",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub

								}
							});

					AlertDialog dialog = builder.create();
					dialog.show();
				}
			});
			listView.addFooterView(footView);
			listView.setAdapter(adapter);

			return rootView;
		}
	}

	public static class DummySectionFragment2 extends Fragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_section_dummy,
					container, false);
			return rootView;
		}
	}
}
