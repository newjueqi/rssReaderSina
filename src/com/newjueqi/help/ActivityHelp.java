package com.newjueqi.help;

import com.newjueqi.R;
import com.newjueqi.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class ActivityHelp extends Activity {

	//���˳����˵���ID
	private final int ITEM_EXIT=Menu.FIRST;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
	}

	/**
	 * ���ò˵�
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		
		menu.add(0, ITEM_EXIT, 0, R.string.gloal_button_exit)
			.setIcon(R.drawable.exit);
		return true;
	}

	/**
	 * ��Ӧ�˵�
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch( item.getItemId() )
		{
		//���뿪���˵�
		case ITEM_EXIT:
			finish();
			break;
			
		}
		return super.onOptionsItemSelected(item);
	}

	
	
}
