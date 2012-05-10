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
	    
	    
	    //�˵���ID
	    private static final int MENU_ADD = Menu.FIRST;
	    private static final int MENU_UPDATE = Menu.FIRST + 1;
	    private static final int MENU_DEL = Menu.FIRST + 2;
	    
	    //�Ի�������
	    
	    //��ʾ�û�����ѡ��һ���б�����ܽ���ɾ������
	    private static final int DIALOG_DEL_SELECTONE = 1;
	    
	    //ѯ���û��Ƿ�ȷ��ɾ��
	    private static final int DIALOG_DEL_CONFIRM = 2;
	    
	    
	    private ChannelsDbAdapter mDbHelper;

	    //�洢��ֵ�ԣ�����id��ֵ��title�����Լ��ٲ�ѯ���ݿ�Ĵ�������ʾ
	    //ɾ��ȷ�϶Ի�����
	    private Map<Long,String> dataMap=new HashMap<Long,String>();

	    
	    //����ɾ���Ի�����ʾ��Ϣ
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
	    
	    //�����б������
	    private void fillData() {
	        Cursor cursor = mDbHelper.fetchAllChannels();
	        startManagingCursor(cursor);
	        
	        //������ݿ�û���ݵ����
	        if( cursor!=null && cursor.getCount()!=0 )
	        {
	        	 //�������ID�����ӷŵ�map������
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
	        
	        //���Ƶ��
	        menu.add(0, MENU_ADD, 0, R.string.manager_button_add)
	        	.setIcon(R.drawable.edit_menu_add);
	        
	        /*
	         * ����˵������Ӳ��������õ�
	         */
	        menu.add(0, 100, 0, "���Ӳ�������");
	        return true;
	    }

		@Override
		/*
		 * ��ÿһ��menu���ɵ�ʱ��ǰ���������������������������߿��Զ�̬���޸����ɵ�menu��
		 */
		public boolean onPrepareOptionsMenu(Menu menu) {
			super.onPrepareOptionsMenu(menu);
			boolean haveItems = getListAdapter().getCount() > 0;
			menu.removeGroup(1);
			if (haveItems) {
				// ���ѡ��һ��Item�Ļ�
				if (getListView().getSelectedItemId() > 0) {

					Intent intent = new Intent(this, ActivityChannelEdit.class);
					
					//�������Ƶ����Ϣ����
					menu.add(1, MENU_UPDATE, 1, R.string.manager_button_update)
						.setIcon(R.drawable.manager_menu_edit);
					
					//ɾ��Ƶ��
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
	            
	        //���Ӳ��������õ�
	        case 100:
	        	addTestDate(item);
	        	
	        }
	       
	        return super.onMenuItemSelected(featureId, item);
	    }
		//////////////////////////////////////////////////////
	    ////���Ӳ��������õ�
	    ///////////////////////////////////////////////////////
	    private void addTestDate(MenuItem item) {
	    	mDbHelper.createChannel("����Ҫ��",
	    			"http://rss.sina.com.cn/news/china/focus15.xml", 
	    			null );
	    	mDbHelper.createChannel("����Ҫ��",
	    			"http://rss.sina.com.cn/news/world/focus15.xml", 
	    			null );
	    	mDbHelper.createChannel("�������",
	    			"http://rss.sina.com.cn/news/society/focus15.xml", 
	    			null );
	    	
	    	
	    	
	    	fillData();
		}

		/**
	     * ɾ��Ƶ����Ϣ,����һ��ȷ�϶Ի���ѯ���Ƿ�Ҫɾ����Ƶ����Ϣ
	     * @param item
	     */
	    private void delChannel(MenuItem item) {
	    		//ѯ���û��Ƿ�ȷ��ɾ���Ի���
	    		showDialog(DIALOG_DEL_CONFIRM);
		}

	    /**
	     * ����Ƶ����Ϣ
	     * @param item
	     */
	    private void updateChannel(MenuItem item) {
			Intent intent=new Intent(this,ActivityChannelEdit.class);
			intent.putExtra(ChannelsDbAdapter.KEY_ROWID, getListView().getSelectedItemId());
			
			startActivity(intent);
			
		}

		/**
		 * ���Ƶ����Ϣ
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

		//�����Ի���Ļص�����
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


		//ѯ���û��Ƿ�ȷ��ɾ���Ի���
		private Dialog confirmDialog() {
			AlertDialog.Builder builder=new AlertDialog.Builder(this);
			builder.setIcon(R.drawable.alert_dialog_icon);
			
			//���öԻ������Ϣ��Ϣ
			
			//��ȡ�����б���ı�����Ϣ 
			String title=dataMap.get( getSelectedItemId());
			
			//����ַ����鲻Ϊ�վ�ɾ���ɵ��ַ�����
			if( delMessage.length()>0 )
			{
				delMessage.delete(0, delMessage.length()-1);
				
			}
			delMessage.append(getString(R.string.manager_delmenu_confirm_essage_1))
					  .append(title)
					  .append(getString(R.string.manager_delmenu_confirm_essage_2));
			builder.setMessage(delMessage.toString());
			
			//ɾ���Ի����ȷ�ϰ�ť�����ɾ������
			builder.setPositiveButton(R.string.manager_delmenu_confirm_yes,
					new OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which) {
							
							mDbHelper.deleteChannel(getSelectedItemId());
							
							//����û�����ˡ�ȷ�ϡ��Ի���,�����б�������
							fillData();
							
						}
					
			});
			
			//ɾ���Ի����ȡ����ť�����ȡ���Ի���
			builder.setNegativeButton(R.string.manager_delmenu_confirm_no,
					new OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					
			});

			
			return builder.create();
		}

		
		
		
}
