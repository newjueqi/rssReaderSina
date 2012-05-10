package com.newjueqi.rss.handler;

//XML节点的状态转换枚举变量
public enum ElementState {
	title,	//标题
	link,	//链接
	pubDate,	//发布日期
	category,	//种类
	description,	//新闻内容的简介
	none		//缺省的其它状态
}
