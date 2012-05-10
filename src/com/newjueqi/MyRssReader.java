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
 * ������
 * @author itcast
 *
 */
public class MyRssReader extends ListActivity {
	
	//����˵���ID
	private final int ITEM_MANAGER=Menu.FIRST;
	
	//���ò˵���ID
	private final  int ITEM_SETTING=Menu.FIRST+1;
	
	//�����˵���ID
	private  final int ITEM_HELP=Menu.FIRST+2;
	
	//�˵�����ʾ˳�����
	private int MENU_ORDER=0; 
	
	//�������ݿ�
    private ChannelsDbAdapter mDbHelper;

    //�洢��ֵ�ԣ�����id��ֵ�����ӣ����Լ��ٲ�ѯ���ݿ�Ĵ��������ݲ�����
    //ActivityItemListʱ����ID�����Ӵ�����
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
    
    //�����б������
    private void fillData() {
        Cursor cursor = mDbHelper.fetchAllChannels();
        startManagingCursor(cursor);
        
   /*     
        //������ݿ�û���ݵ����
        if( cursor!=null && cursor.getCount()!=0 )
        {
        	 //�������ID�����ӷŵ�map������
            cursor.moveToFirst();
            while( !cursor.isLast() )
            {
            	dataMap.put( cursor.getLong(0), cursor.getString(2));
            	cursor.moveToNext();
            }
            
        
        }
     */
        //������ݿ�û���ݵ����
        if( cursor!=null && cursor.getCount()!=0 )
        {
        	 //�������ID�����ӷŵ�map������
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
     * ����б��е�����һ���ת����ʾƵ����ϸ���ݵĽ���ActivityChannel
     */
	@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent intent = new Intent(this, ActivityItemList.class);
        
        //��ID��������Ϊ��������,��dataMap��ȡֵ
        intent.putExtra(ChannelsDbAdapter.KEY_ROWID, id);
        intent.putExtra(ChannelsDbAdapter.KEY_LINK, dataMap.get(id));
   //    setTitle(dataMap.get(id));
        	startActivity(intent);
	}

    
    /**
     * ��ʾ�˵���Ϣ
     */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		super.onCreateOptionsMenu(menu);
		
		//"����"�˵�
		menu.add(0,ITEM_MANAGER,MENU_ORDER++,getString(R.string.main_menu_manager))
			.setIcon(R.drawable.main_menu_manager);
			
		//"����"�˵�
//		menu.add(0,ITEM_SETTING,MENU_ORDER++,getString(R.string.main_menu_setting))
//			.setIcon(R.drawable.main_menu_setting);

		//"����"�˵�
		menu.add(0,ITEM_HELP,MENU_ORDER++,getString(R.string.main_menu_help))
			.setIcon(R.drawable.main_menu_help);
		
		return true;
		
	}

	/**
	 * ��Ӧ�˵���Ϣ
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch( item.getItemId())
		{
		//��Ӧ"����"�˵�
		case ITEM_MANAGER:
			startManager();
			break;
			
		//��Ӧ"����"�˵�
		case ITEM_SETTING:
			startSetting();
			break;
			
		//��Ӧ"����"�˵�
		case ITEM_HELP:
			startHelp();
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}

	/**
	 * ��Ӧ�����˵�����
	 */
	private void startHelp() {
		Intent intent=new Intent(this,ActivityHelp.class);
		startActivity(intent);
	}

	/**
	 * ��Ӧ���ò˵�����
	 */
	private void startSetting() {
		Intent intent=new Intent(this,ActivitySetting.class);
		startActivity(intent);
		
	}

	/**
	 * ��Ӧ����˵�
	 	 */
	private void startManager() {
		Intent intent=new Intent(this,ActivityManager.class);
		startActivity(intent);
		
	}
    
    
    
    
}