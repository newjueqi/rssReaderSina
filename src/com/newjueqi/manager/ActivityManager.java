package com.newjueqi.manager;

import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.newjueqi.R;
import com.newjueqi.dao.ChannelsDbAdapter;


public class ActivityManager extends ListActivity {
		private static final int ACTIVITY_CREATE=0;
	    private static final int ACTIVITY_EDIT=1;
	    
	    
	    //菜单的ID
	    private static final int MENU_ADD = Menu.FIRST;
	    private static final int MENU_UPDATE = Menu.FIRST + 1;
	    private static final int MENU_DEL = Menu.FIRST + 2;
	    
	    //对话框的类别
	    
	    //提示用户必须选择一个列表项才能进行删除操作
	    private static final int DIALOG_DEL_SELECTONE = 1;
	    
	    //询问用户是否确定删除
	    private static final int DIALOG_DEL_CONFIRM = 2;
	    
	    
	    private ChannelsDbAdapter mDbHelper;

	    //存储键值对（键：id，值：title），以减少查询数据库的次数，显示
	    //删除确认对话框用
	    private Map<Long,String> dataMap=new HashMap<Long,String>();

	    
	    //弹出删除对话的提示信息
	    private StringBuilder delMessage=new StringBuilder(" ");
	    
	    
	    /** Called when the activity is first created. */
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.manager);
	        mDbHelper = new ChannelsDbAdapter(this);
	        mDbHelper.open();
	        fillData();
	        registerForContextMenu(getListView());
	    }
	    
	    //更新列表的数据
	    private void fillData() {
	        Cursor cursor = mDbHelper.fetchAllChannels();
	        startManagingCursor(cursor);
	        
	        //检测数据库没数据的情况
	        if( cursor!=null && cursor.getCount()!=0 )
	        {
	        	 //把里面的ID和链接放到map集合中
	            cursor.moveToFirst();
	            do
	            {
	             	dataMap.put( cursor.getLong(0), cursor.getString(1));
	                 	
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
	    
	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        super.onCreateOptionsMenu(menu);
	        
	        //添加频道
	        menu.add(0, MENU_ADD, 0, R.string.manager_button_add)
	        	.setIcon(R.drawable.edit_menu_add);
	        
	        /*
	         * 这个菜单是增加测试数据用的
	         */
	        menu.add(0, 100, 0, "增加测试数据");
	        return true;
	    }

		@Override
		/*
		 * 在每一次menu生成的时候前都会调用这个方法，在这个方法里边可以动态的修改生成的menu。
		 */
		public boolean onPrepareOptionsMenu(Menu menu) {
			super.onPrepareOptionsMenu(menu);
			boolean haveItems = getListAdapter().getCount() > 0;
			menu.removeGroup(1);
			if (haveItems) {
				// 如果选中一个Item的话
				if (getListView().getSelectedItemId() > 0) {

					Intent intent = new Intent(this, ActivityChannelEdit.class);
					
					//进入更新频道信息界面
					menu.add(1, MENU_UPDATE, 1, R.string.manager_button_update)
						.setIcon(R.drawable.manager_menu_edit);
					
					//删除频道
					menu.add(1, MENU_DEL, 1, R.string.manage_button_del)
						.setIcon(R.drawable.manager_menu_del);
				}

			}
			return true;
		}

	    @Override
	    public boolean onMenuItemSelected(int featureId, MenuItem item) {
	        switch(item.getItemId()) {
	        case MENU_ADD:
	            createChannel(item);
	            break;
	        case MENU_UPDATE:
	            updateChannel(item);
	            break;
	        case MENU_DEL:
	            delChannel(item);
	            break;
	            
	        //增加测试数据用的
	        case 100:
	        	addTestDate(item);
	        	
	        }
	       
	        return super.onMenuItemSelected(featureId, item);
	    }
		//////////////////////////////////////////////////////
	    ////增加测试数据用的
	    ///////////////////////////////////////////////////////
	    private void addTestDate(MenuItem item) {
	    	mDbHelper.createChannel("国内要闻",
	    			"http://rss.sina.com.cn/news/china/focus15.xml", 
	    			null );
	    	mDbHelper.createChannel("国际要闻",
	    			"http://rss.sina.com.cn/news/world/focus15.xml", 
	    			null );
	    	mDbHelper.createChannel("社会新闻",
	    			"http://rss.sina.com.cn/news/society/focus15.xml", 
	    			null );
	    	
	    	
	    	
	    	fillData();
		}

		/**
	     * 删除频道信息,弹出一个确认对话框询问是否要删除该频道信息
	     * @param item
	     */
	    private void delChannel(MenuItem item) {
	    		//询问用户是否确定删除对话框
	    		showDialog(DIALOG_DEL_CONFIRM);
		}

	    /**
	     * 更新频道信息
	     * @param item
	     */
	    private void updateChannel(MenuItem item) {
			Intent intent=new Intent(this,ActivityChannelEdit.class);
			intent.putExtra(ChannelsDbAdapter.KEY_ROWID, getListView().getSelectedItemId());
			
			startActivity(intent);
			
		}

		/**
		 * 添加频道信息
		 * @param item
		 */
		private void createChannel(MenuItem item) {
			Intent intent=new Intent(this,ActivityChannelEdit.class);

			startActivity(intent );
		}

		@Override
	    protected void onListItemClick(ListView l, View v, int position, long id) {
	        super.onListItemClick(l, v, position, id);
	        Intent intent = new Intent(this, ActivityChannelEdit.class);
	        intent.putExtra(ChannelsDbAdapter.KEY_ROWID, id);
			startActivity(intent);
		}

		//创建对话框的回调函数
		@Override
		protected Dialog onCreateDialog(int id) {
			Dialog dialog=null;
			switch(id)
			{
			case DIALOG_DEL_CONFIRM:
				dialog= confirmDialog();
				
			}
			
			return dialog;
		}


		//询问用户是否确定删除对话框
		private Dialog confirmDialog() {
			AlertDialog.Builder builder=new AlertDialog.Builder(this);
			builder.setIcon(R.drawable.alert_dialog_icon);
			
			//设置对话框的信息信息
			
			//获取单击列表项的标题信息 
			String title=dataMap.get( getSelectedItemId());
			
			//如果字符数组不为空就删除旧的字符资料
			if( delMessage.length()>0 )
			{
				delMessage.delete(0, delMessage.length()-1);
				
			}
			delMessage.append(getString(R.string.manager_delmenu_confirm_essage_1))
					  .append(title)
					  .append(getString(R.string.manager_delmenu_confirm_essage_2));
			builder.setMessage(delMessage.toString());
			
			//删除对话框的确认按钮，点击删除该项
			builder.setPositiveButton(R.string.manager_delmenu_confirm_yes,
					new OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which) {
							
							mDbHelper.deleteChannel(getSelectedItemId());
							
							//如果用户点击了“确认”对话框,更新列表框的内容
							fillData();
							
						}
					
			});
			
			//删除对话框的取消按钮，点击取消对话框
			builder.setNegativeButton(R.string.manager_delmenu_confirm_no,
					new OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					
			});

			
			return builder.create();
		}

		
		
		
}
