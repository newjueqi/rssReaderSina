package com.newjueqi.itemlist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.newjueqi.R;


public class ActivityShowItemDescription extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_description);
		
		StringBuilder content=new StringBuilder();
		Intent intent=getIntent();
		
		//�������ŵ���ϸ��Ϣ
		setContent( intent, content);
		
		TextView textView=(TextView)findViewById(R.id.item_descritption);
		
		//����TextView��ʾ������
		textView.setText(content.toString());
		
		//ʹ��ť�߱����ع���
		Button button=(Button)findViewById( R.id.item_descritption_backbutton );
		button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				finish();
			}
			
		});
		
		
	}

	//�������ŵ���ϸ��Ϣ
	public void setContent(Intent intent,StringBuilder content)
	{
		if( intent!=null )
		{
			//��ȡ���ݹ�����Bundle������ΪItemDescription
			Bundle bundle=intent.getBundleExtra("ItemDescription");
			
			if(  bundle==null )
			{
				//��ʾ�������
				content.append(getString(R.string.item_app_error));
			}
			else
			{
				//���ŵ���ϸ��Ϣ
				content.append(bundle.getString("title") )
						.append("\n\n")
						.append(bundle.getString("pubdate"))
						.append("\n\n")
						.append(bundle.getString("description"))
						.append("\n\n")
						.append(getString(R.string.item_description_link))
						.append(bundle.getString("link"))
						.append("\n\n");
						
			}
			
		}
		else
		{
			//��ʾ�������
			content.append(getString(R.string.item_app_error));
		
		}
		
	}
	
}
