package cn.winfxk.knickers.module.alter;

import java.util.Map;

import cn.winfxk.knickers.tool.Config;

/**
 * 封装数据类
 * 
 * @Createdate 2021/08/09 18:37:36
 * @author Winfxk
 */
public class Data {

	public String Key;
	public Map<String, Object> map, Buttons;
	public Config config;

	public Data(String Key, Map<String, Object> map, Map<String, Object> Buttons, Config config) {
		this.Key = Key;
		this.map = map;
		this.Buttons = Buttons;
		this.config = config;
	}
}
