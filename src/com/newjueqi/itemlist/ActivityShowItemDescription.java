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
		
		//设置新闻的详细信息
		setContent( intent, content);
		
		TextView textView=(TextView)findViewById(R.id.item_descritption);
		
		//设置TextView显示的内容
		textView.setText(content.toString());
		
		//使按钮具备返回功能
		Button button=(Button)findViewById( R.id.item_descritption_backbutton );
		button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				finish();
			}
			
		});
		
		
	}

	//设置新闻的详细信息
	public void setContent(Intent intent,StringBuilder content)
	{
		if( intent!=null )
		{
			//获取传递过来的Bundle，名称为ItemDescription
			Bundle bundle=intent.getBundleExtra("ItemDescription");
			
			if(  bundle==null )
			{
				//显示程序出错
				content.append(getString(R.string.item_app_error));
			}
			else
			{
				//新闻的详细信息
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
			//显示程序出错
			content.append(getString(R.string.item_app_error));
		
		}
		
	}
	
}
