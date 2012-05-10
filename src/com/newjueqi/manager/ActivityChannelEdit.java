package com.newjueqi.manager;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.newjueqi.R;
import com.newjueqi.dao.ChannelsDbAdapter;


public class ActivityChannelEdit extends Activity {

	//����༭��
	private EditText mTitleText;
	
	//���ӱ༭��
	private EditText mLinkText;
	
	//�����༭��
	private EditText mDescriptionText;
	private Button mConfirmButton;
	
    private ChannelsDbAdapter mDbHelper;

	
	//��¼��ID��
	private Long mRowId;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manager_edit);
		
	    mDbHelper = new ChannelsDbAdapter(this);
	    mDbHelper.open();

	   //��ȡ�����༭��Ķ���
		mTitleText=(EditText)findViewById(R.id.manager_edit_title);
		mLinkText=(EditText)findViewById(R.id.manager_edit_link);
		mDescriptionText=(EditText)findViewById(R.id.manager_edit_description);
		mConfirmButton=(Button)findViewById(R.id.manager_edit_confirm);
		
		mRowId=savedInstanceState!=null
			?savedInstanceState.getLong(ChannelsDbAdapter.KEY_ROWID):null;
			
		if( mRowId==null )
		{
			Bundle extras = getIntent().getExtras();            
			mRowId = extras != null ? extras.getLong(ChannelsDbAdapter.KEY_ROWID) 
									: null;
		}
		
		populateFields();
		
		
		mConfirmButton.setOnClickListener(new View.OnClickListener() {

        	public void onClick(View view) {
        	    finish();
           	}
          
        });

	}

	//ÿ�λָ�ʱ�����ݴ����ݿ���ȡ��
    private void populateFields() {
        if (mRowId != null) {
        	setTitle(R.string.manager_edit_lable); //lableΪ�޸�Ƶ����Ϣ
            Cursor note = mDbHelper.fetchChannel(mRowId);
            startManagingCursor(note);
            mTitleText.setText(note.getString(
    	            note.getColumnIndexOrThrow(ChannelsDbAdapter.KEY_TITLE)));
            mLinkText.setText(note.getString(
                    note.getColumnIndexOrThrow(ChannelsDbAdapter.KEY_LINK)));
            mDescriptionText.setText(note.getString(
                    note.getColumnIndexOrThrow(ChannelsDbAdapter.KEY_DESCRIPTION)));
            
        }
        else{
        	setTitle(R.string.manager_add_lable); //lableΪ���Ƶ����Ϣ
        }
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(mDbHelper.KEY_ROWID, mRowId);
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        populateFields();
    }
    
    private void saveState() {
        String title = mTitleText.getText().toString().trim();
        String link = mLinkText.getText().toString().trim();
        String description = mDescriptionText.getText().toString().trim();

        //�������ֵ�������ݿ�
        if( title!=null && !title.equals("")
        		&& link!=null & !link.equals("") )
        {
        	if (mRowId == null) {
                long id = mDbHelper.createChannel(title, link, description);
                if (id > 0) {
                    mRowId = id;
                }
            } else {
                mDbHelper.updateChannel(mRowId, title, link, description);
            }

        }
    }
    
}
