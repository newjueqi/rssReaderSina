package com.newjueqi.itemlist;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

import com.newjueqi.R;
import com.newjueqi.dao.ChannelsDbAdapter;
import com.newjueqi.rss.bean.RSSFeed;
import com.newjueqi.rss.bean.RSSItem;
import com.newjueqi.rss.handler.RssHandler;

/**
 *��ʾÿ��Ƶ���е������б�
 */
public class ActivityItemList extends Activity {

	private RSSFeed rssFeed;
	private Long mId; //��MyRssReader���ݹ�����ID
	private String mLink; //��MyRssReader���ݹ�����link
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.itemlist);
		
		//��ȡ���ݵĲ���Id��link
		Bundle parm=getIntent().getExtras();
		mId=parm.getLong(ChannelsDbAdapter.KEY_ROWID);
		mLink=parm.getString(ChannelsDbAdapter.KEY_LINK);
		
		rssFeed=getFeed(mLink);
		showListView();
		
		
	}

	//��ʾItem���б���Ϣ
	private void showListView() {
		
		ListView listView=(ListView)findViewById(R.id.item_row_itemlist);
		if( rssFeed==null )
		{
			//���ʵ�Rss��Ч
			setTitle(R.string.item_app_error);
			return ;
		}
		
		SimpleAdapter adapter=new SimpleAdapter(
				this,rssFeed.getAllItemsForListView(),
				R.layout.item_row,
				new String[]{RSSItem.TITLE,RSSItem.PUBDATE},
				new int[]{R.id.item_row_text_title,
					R.id.item_row_text_pubdate});
		
		listView.setAdapter(adapter);
		
		//����б��е�һ���������ʾ��ϸ��Ϣ��Activity
		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int postion,
					long id) {
				Intent intent=new Intent(ActivityItemList.this,
						ActivityShowItemDescription.class);
				Bundle bundle=new Bundle();
				bundle.putString("title", rssFeed.getItem(postion).getTitle());
				bundle.putString("description", rssFeed.getItem(postion).getDescription());
				bundle.putString("link", rssFeed.getItem(postion).getLink());
				bundle.putString("pubdate", rssFeed.getItem(postion).getPubDate());
				intent.putExtra("ItemDescription",bundle);
				startActivity(intent);
			}
			
		});
	}

	
	private RSSFeed getFeed(String link) {
		
		RSSFeed feed=null;
		try {
			SAXParserFactory factory=SAXParserFactory.newInstance();
			SAXParser parser=factory.newSAXParser();
			XMLReader xmlReader=parser.getXMLReader();
			
			RssHandler rssHandler=new RssHandler();
			xmlReader.setContentHandler(rssHandler);
			
			/*
			 * �������������ļ��͸�����δ���
			 */
			URL url = new URL(link);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.connect();
	
			InputStream inputStream = conn.getInputStream();
	
	//		InputStream inputStream=getResources().openRawResource(R.raw.focus15);
			
			InputSource is=new InputSource(inputStream);
			xmlReader.parse(is);
			feed= rssHandler.getRssFeed();
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return feed;
	}

	
	
}
