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
 * ����ǰXML����
 * 			<title>
				<![CDATA[���������������ȥ������115��(ͼ)]]>
			</title>
			<link>http://go.rss.sina.com.cn/redirect.php?url=http://news.sina.com.cn/s/2009-09-12/151318637149.shtml</link>
			<author>WWW.SINA.COM.CN</author>
	
 * 
 * ���ͺ�
 * 			69.<title>70.Text=
				71.Text=���������������ȥ������115��(ͼ)72.Text=
			73.</title>74.Text=
			75.<link>76.Text=http://go.rss.sina.com.cn/redirect.php?url=http://news.sina.com.cn/s/2009-09-12/151318637149.shtml77.</link>78.Text=
			79.<author>80.Text=WWW.SINA.COM.CN81.</author>82.Text=

 */

/**
 * ����XML�ļ�������
 */
public class RssReaderDemo {
	/** ����SAX��ʽ����XML�ĵ� */
	public static void main(String[] args) throws SAXException,
			ParserConfigurationException, IOException {
		SAXParserFactory factory = SAXParserFactory.newInstance(); // ����SAX����������
		factory.setValidating(true); // ��error������Ч
		SAXParser parser = factory.newSAXParser();
		// ����һ�������SAX������
		parser.parse("focus.xml", new XMLreader()); // ��ʼ����
	}
}

class XMLreader extends DefaultHandler {
	// ֻ�踲�����Ǹ���Ȥ�ķ���
	private int counter = 0;// ����һ��������������XML�ĵ������¼��Ĵ���

	@Override
	// �ĵ���ʼ�¼�����
	public void startDocument() throws SAXException {
		counter++;
		System.out.println(counter + ".����XML�ļ���ʼ...");
	}

	@Override
	// �ĵ������¼�����
	public void endDocument() throws SAXException {
		counter++;
		System.out.println("\r\n" + counter + ".����XML�ļ�����...");
	}

	@Override
	// Ԫ�ؿ�ʼ�¼�����
	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {
		counter++;
		System.out.print(counter + ".<" + qName);
		for (int i = 0; i < atts.getLength(); i++) { // ��ȡ��־����������
			System.out.print(" " + atts.getLocalName(i) + "="
					+ atts.getValue(i));
		}
		System.out.print(">");
	}

	@Override
	// Ԫ�ؽ����¼�����
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		counter++;
		System.out.print(counter + ".</" + qName + ">");
	}

	@Override
	// �ı��¼����� ��ӡʱ������Ҫ���У�������ѿ�
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		counter++;
		String text = new String(ch, start, length); // ��ǰԪ�ص��ı�ֵ
		System.out.print(counter + ".Text=" + text);
	}

	@Override
	// ���ǿɻָ���������SAXParserFactory������Ч�Դ��������Ч
	public void error(SAXParseException e) throws SAXException {
		System.out.println("xml�ĵ���Ч�Դ���" + e);
	}

	@Override
	// ���ش���
	public void fatalError(SAXParseException e) throws SAXException {
		System.out.println("xml�ĵ����ص���Ч�Դ���" + e);
	}
}