package com.newjueqi.rss.handler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.newjueqi.rss.bean.RSSFeed;
import com.newjueqi.rss.bean.RSSItem;

public class RssHandler extends DefaultHandler {
	//���ݼ�⵽�Ľڵ������л���ͬ��״̬����Žڵ������
	private ElementState state;
	
	//RSS������
	private RSSFeed rssFeed;
	
	//RSS��Item
	private RSSItem rssItem;

	/*
	 * ��Ϊ�������ŵ�XML�ļ�����������������Ľڵ�
	 * 		<title>
				<![CDATA[���������������ȥ������115��(ͼ)]]>
			</title>
			<category>
				<![CDATA[�������-��������]]>
			</category>
			<description>
				<![CDATA[����������ɼ���յ�(פ�����߸���) ��������ٵ��˱���˹��������ɼ�һ��ҽԺ����������115�ꡣ

��������˹8��ǰˤ�ϹɹǺ�ס����ҽԺ���Ӵ���Ҳû�лؼң�������˯�����������ǡ�����Ϊ�������ҽ�����ر�ʾ���������������ಡ��������������ǰ������ʱ�������úõġ���....]]>
			</description>

		�����ͺ�Ľ��������			
			69.<title>70.Text=71.Text=
				72.Text=���������������ȥ������115��(ͼ)73.Text=74.Text=
			75.</title>76.Text=
		
		���Զ������������ֽڵ㣬����Ҫ�ü���������������ȷ��ȡ���͵�����
		
		ע���ˣ�������Խ����(String uri, String name, String qname)������String��һ��
	
	 */
	private int titleNum=0;
	private int categoryNum=0;
	private int descriptionNum=0;
	
	//��ȡ�µĽڵ�ǰ�����еļ���������0
	public void resetNum()
	{
		titleNum=0;
		categoryNum=0;
		descriptionNum=0;
	}
	
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String str=new String(ch,start,length);
		//���ݲ�ͬ��״̬�ڵ��ȡ��ͬ��ֵ
		
		if( state==null )
		{
			return ;
		}
		
		switch( state )
		{
			
		//���ڶ�ȡ��title�ڵ�ʱ������������ݽڵ㣬������Ҫ������ȡ�ڶ����ڵ������ֵ
		case title :
			if(titleNum==2)
			{
				rssItem.setTitle(str);
				state=ElementState.none;
			}
			titleNum++;
			break;
			
		case link  :
			rssItem.setLink(str);
			state=ElementState.none;
			break;
			
		case pubDate  :
			rssItem.setPubDate(str);
			state=ElementState.none;
			break;
			  
		case category  :
			if( categoryNum==2 )
			{
				rssItem.setCategory(str);
				state=ElementState.none;
			}
			categoryNum++;
			break;
			
		case description :
			if( descriptionNum==2 )
			{
				rssItem.setDescription(str);
				state=ElementState.none;
			}
			descriptionNum++;
			break;
		default:
			return;
		}
		
	}

	//��ȡRSS������
	public RSSFeed getRssFeed() {
		return rssFeed;
	}

	@Override
	public void endDocument() throws SAXException {
	
	}

	@Override
	public void endElement(String uri, String name, String qname)
			throws SAXException {
		if( name.equals("item") )
		{
			rssFeed.addItem(rssItem);
		}
		
		//��ȡ�µĽڵ�ǰ�����еļ���������0
		resetNum();
		
	}

	@Override
	public void startDocument() throws SAXException {
		rssFeed=new RSSFeed();
		rssItem=new RSSItem();
	}

	@Override
	public void startElement(String uri, String name, String qname,
			Attributes attributes) throws SAXException {
		
		//����һ��������
		if( name.equals("item") )
		{
			rssItem=new RSSItem();
			return ;
		}
		
		//���ŵı���
		if( name.equals("title") )
		{
			state=ElementState.title;
			return ;
		}
		
		//���ŵ���������
		if( name.equals("link") )
		{
			state=ElementState.link;
			return ;
		}
		
		//���ŵķ�������
		if( name.equals("pubDate") )
		{
			state=ElementState.pubDate;
			return ;
		}
		
		//���ŵ�����
		if( name.equals("category") )
		{
			state=ElementState.category;
			return ;
		}
		
		//�������ݵļ��
		if( name.equals("description") )
		{
			state=ElementState.description;
			return ;
		}
		
	}
}
