package com.example.listviewdemo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends Activity implements OnScrollListener{

	private ListView listView;
	private List<String> cacheList 		= new ArrayList<String>();
	private List<String> contentList 	= new ArrayList<String>();
	private int start 					= 0;
	private int count 					= 90;
	private boolean isReload 			= true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		for (int i = 0; i < 300; i ++) {
			cacheList.add(i + "");
		}
		init();
		reLoadData();
	}

	
	private void init() {
		
		Button btn = (Button) findViewById(R.id.btn);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				listView.post(new Runnable() {
					
					@Override
					public void run() {
						listView.smoothScrollToPosition(0);
					}
				});
			}
		});
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contentList);
		listView = (ListView) findViewById(R.id.list);
		listView.setAdapter(adapter);
		listView.setOnScrollListener(this);
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		if (isReload){
			isReload = false;
			return;
		}
		
		if (OnScrollListener.SCROLL_STATE_IDLE == scrollState) {
			if (view.getFirstVisiblePosition() == 0 && start > 0) {
				start -= 30;
				if (start < 0)
					start = 0;
				reLoadData();
				listView.setSelection(30);
				return;
			}
			if (view.getLastVisiblePosition() == view.getCount() - 1 && start + count < cacheList.size()) {
				start += view.getFirstVisiblePosition() - 60;
				if (start + count > cacheList.size())
					start = cacheList.size() - 90;
				reLoadData();
				listView.setSelection(60);
				return;
			}
		}
	}


	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
	}
	
	private void reLoadData() {
		contentList.clear();
		int len = start + count;
		for (int i = start; i < len; i++) {
			String data = cacheList.get(i);
			if (null != data) {
				contentList.add(data);
			}
		}
		notifyListView(listView);
	}
	
	@SuppressWarnings("unchecked")
	private void notifyListView(ListView listView) {
		if (null == listView || listView.getAdapter() == null)
			return;
		ArrayAdapter<String> adapter = (ArrayAdapter<String>) listView.getAdapter();
		adapter.notifyDataSetChanged();
	}

}
