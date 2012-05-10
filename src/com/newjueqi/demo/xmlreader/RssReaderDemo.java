package com.newjueqi.demo.xmlreader;


import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/*
 * 解释前XML例子
 * 			<title>
				<![CDATA[世界最长寿者在美国去世享年115岁(图)]]>
			</title>
			<link>http://go.rss.sina.com.cn/redirect.php?url=http://news.sina.com.cn/s/2009-09-12/151318637149.shtml</link>
			<author>WWW.SINA.COM.CN</author>
	
 * 
 * 解释后：
 * 			69.<title>70.Text=
				71.Text=世界最长寿者在美国去世享年115岁(图)72.Text=
			73.</title>74.Text=
			75.<link>76.Text=http://go.rss.sina.com.cn/redirect.php?url=http://news.sina.com.cn/s/2009-09-12/151318637149.shtml77.</link>78.Text=
			79.<author>80.Text=WWW.SINA.COM.CN81.</author>82.Text=

 */

/**
 * 解释XML文件的例子
 */
public class RssReaderDemo {
	/** 基于SAX方式解析XML文档 */
	public static void main(String[] args) throws SAXException,
			ParserConfigurationException, IOException {
		SAXParserFactory factory = SAXParserFactory.newInstance(); // 创建SAX解析器工厂
		factory.setValidating(true); // 让error方法生效
		SAXParser parser = factory.newSAXParser();
		// 生成一个具体的SAX解析器
		parser.parse("focus.xml", new XMLreader()); // 开始解析
	}
}

class XMLreader extends DefaultHandler {
	// 只需覆盖我们感兴趣的方法
	private int counter = 0;// 定义一个计数器，保存XML文档触发事件的次数

	@Override
	// 文档开始事件触发
	public void startDocument() throws SAXException {
		counter++;
		System.out.println(counter + ".解析XML文件开始...");
	}

	@Override
	// 文档结束事件触发
	public void endDocument() throws SAXException {
		counter++;
		System.out.println("\r\n" + counter + ".解析XML文件结束...");
	}

	@Override
	// 元素开始事件触发
	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {
		counter++;
		System.out.print(counter + ".<" + qName);
		for (int i = 0; i < atts.getLength(); i++) { // 读取标志的所有属性
			System.out.print(" " + atts.getLocalName(i) + "="
					+ atts.getValue(i));
		}
		System.out.print(">");
	}

	@Override
	// 元素结束事件触发
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		counter++;
		System.out.print(counter + ".</" + qName + ">");
	}

	@Override
	// 文本事件触发 打印时尽量不要换行，否则很难看
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		counter++;
		String text = new String(ch, start, length); // 当前元素的文本值
		System.out.print(counter + ".Text=" + text);
	}

	@Override
	// 这是可恢复错误。需在SAXParserFactory设置有效性错误才能生效
	public void error(SAXParseException e) throws SAXException {
		System.out.println("xml文档有效性错误：" + e);
	}

	@Override
	// 严重错误
	public void fatalError(SAXParseException e) throws SAXException {
		System.out.println("xml文档严重的有效性错误：" + e);
	}
}