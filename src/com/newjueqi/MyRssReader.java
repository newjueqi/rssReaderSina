package com.newjueqi;

import java.util.HashMap;
import java.util.Map;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.newjueqi.dao.ChannelsDbAdapter;
import com.newjueqi.help.ActivityHelp;
import com.newjueqi.itemlist.ActivityItemList;
import com.newjueqi.manager.ActivityManager;
import com.newjueqi.setting.ActivitySetting;

/**
 * 主界面
 * @author itcast
 *
 */
public class MyRssReader extends ListActivity {
	
	//管理菜单的ID
	private final int ITEM_MANAGER=Menu.FIRST;
	
	//设置菜单的ID
	private final  int ITEM_SETTING=Menu.FIRST+1;
	
	//帮助菜单的ID
	private  final int ITEM_HELP=Menu.FIRST+2;
	
	//菜单的显示顺序变量
	private int MENU_ORDER=0; 
	
	//操作数据库
    private ChannelsDbAdapter mDbHelper;

    //存储键值对（键：id，值：链接），以减少查询数据库的次数，传递参数到
    //ActivityItemList时根据ID查链接传参数
    private Map<Long,String> dataMap=new HashMap<Long,String>();
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setTitle(R.string.main_app_name);
        mDbHelper = new ChannelsDbAdapter(this);
        mDbHelper.open();
        fillData();

    }
    
    //更新列表的数据
    private void fillData() {
        Cursor cursor = mDbHelper.fetchAllChannels();
        startManagingCursor(cursor);
        
   /*     
        //检测数据库没数据的情况
        if( cursor!=null && cursor.getCount()!=0 )
        {
        	 //把里面的ID和链接放到map集合中
            cursor.moveToFirst();
            while( !cursor.isLast() )
            {
            	dataMap.put( cursor.getLong(0), cursor.getString(2));
            	cursor.moveToNext();
            }
            
        
        }
     */
        //检测数据库没数据的情况
        if( cursor!=null && cursor.getCount()!=0 )
        {
        	 //把里面的ID和链接放到map集合中
            cursor.moveToFirst();
            do
            {
             	dataMap.put( cursor.getLong(0), cursor.getString(2));
                 	
            }while( cursor.moveToNext());
        }
        
        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[]{ChannelsDbAdapter.KEY_TITLE};
        
        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[]{R.id.manager_delmenu_dialog};
        
        // Now create a simple cursor adapter and set it to display
        SimpleCursorAdapter adapter = 
        	    new SimpleCursorAdapter(this, R.layout.manager_row, cursor, from, to);
        setListAdapter(adapter);
    }

    /**
     * 点击列表中的其中一项后转到显示频道详细内容的界面ActivityChannel
     */
	@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent intent = new Intent(this, ActivityItemList.class);
        
        //把ID和链接作为参数传递,从dataMap中取值
        intent.putExtra(ChannelsDbAdapter.KEY_ROWID, id);
        intent.putExtra(ChannelsDbAdapter.KEY_LINK, dataMap.get(id));
   //    setTitle(dataMap.get(id));
        	startActivity(intent);
	}

    
    /**
     * 显示菜单信息
     */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		super.onCreateOptionsMenu(menu);
		
		//"管理"菜单
		menu.add(0,ITEM_MANAGER,MENU_ORDER++,getString(R.string.main_menu_manager))
			.setIcon(R.drawable.main_menu_manager);
			
		//"设置"菜单
//		menu.add(0,ITEM_SETTING,MENU_ORDER++,getString(R.string.main_menu_setting))
//			.setIcon(R.drawable.main_menu_setting);

		//"帮助"菜单
		menu.add(0,ITEM_HELP,MENU_ORDER++,getString(R.string.main_menu_help))
			.setIcon(R.drawable.main_menu_help);
		
		return true;
		
	}

	/**
	 * 响应菜单消息
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch( item.getItemId())
		{
		//响应"管理"菜单
		case ITEM_MANAGER:
			startManager();
			break;
			
		//响应"设置"菜单
		case ITEM_SETTING:
			startSetting();
			break;
			
		//响应"帮助"菜单
		case ITEM_HELP:
			startHelp();
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 响应帮助菜单函数
	 */
	private void startHelp() {
		Intent intent=new Intent(this,ActivityHelp.class);
		startActivity(intent);
	}

	/**
	 * 响应设置菜单函数
	 */
	private void startSetting() {
		Intent intent=new Intent(this,ActivitySetting.class);
		startActivity(intent);
		
	}

	/**
	 * 响应管理菜单
	 	 */
	private void startManager() {
		Intent intent=new Intent(this,ActivityManager.class);
		startActivity(intent);
		
	}
    
    
    
    
}