package com.newjueqi.rss.handler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.newjueqi.rss.bean.RSSFeed;
import com.newjueqi.rss.bean.RSSItem;

public class RssHandler extends DefaultHandler {
	//根据检测到的节点类型切换不同的状态，存放节点的内容
	private ElementState state;
	
	//RSS的主题
	private RSSFeed rssFeed;
	
	//RSS的Item
	private RSSItem rssItem;

	/*
	 * 因为新浪新闻的XML文件中有下面三个特殊的节点
	 * 		<title>
				<![CDATA[世界最长寿者在美国去世享年115岁(图)]]>
			</title>
			<category>
				<![CDATA[社会新闻-焦点新闻]]>
			</category>
			<description>
				<![CDATA[　　本报洛杉矶今日电(驻美记者高兴) 地球上最长寿的人贝恩斯今天在洛杉矶一家医院逝世，享年115岁。

　　贝恩斯8年前摔断股骨后住进了医院，从此再也没有回家，今天在睡梦中与世长辞。长年为她服务的医生威特表示，她可能死于心脏病发作，“我两天前看到她时，她还好好的”。....]]>
			</description>

		而解释后的结果类似于			
			69.<title>70.Text=71.Text=
				72.Text=世界最长寿者在美国去世享年115岁(图)73.Text=74.Text=
			75.</title>76.Text=
		
		所以对于以上这三种节点，必须要用记数器计数才能正确获取解释的内容
		
		注意了，这个测试结果在(String uri, String name, String qname)后两个String不一致
	
	 */
	private int titleNum=0;
	private int categoryNum=0;
	private int descriptionNum=0;
	
	//读取新的节点前把所有的计数变量清0
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
		//根据不同的状态节点获取不同的值
		
		if( state==null )
		{
			return ;
		}
		
		switch( state )
		{
			
		//由于读取到title节点时会读到三个数据节点，所以需要计数读取第二个节点的数据值
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

	//获取RSS的主题
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
		
		//读取新的节点前把所有的计数变量清0
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
		
		//新增一个新闻项
		if( name.equals("item") )
		{
			rssItem=new RSSItem();
			return ;
		}
		
		//新闻的标题
		if( name.equals("title") )
		{
			state=ElementState.title;
			return ;
		}
		
		//新闻的网络链接
		if( name.equals("link") )
		{
			state=ElementState.link;
			return ;
		}
		
		//新闻的发布日期
		if( name.equals("pubDate") )
		{
			state=ElementState.pubDate;
			return ;
		}
		
		//新闻的种类
		if( name.equals("category") )
		{
			state=ElementState.category;
			return ;
		}
		
		//新闻内容的简介
		if( name.equals("description") )
		{
			state=ElementState.description;
			return ;
		}
		
	}
}
