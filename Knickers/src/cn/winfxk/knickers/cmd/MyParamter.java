package cn.winfxk.knickers.cmd;

import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;

/**
 * @Createdate 2021/08/29 13:20:00
 * @author Winfxk
 */
public class MyParamter extends CommandParameter {
	public MyParamter(String name, boolean optional, String[] enumValues) {
		super(name, optional, enumValues);
		this.type = CommandParamType.STRING;
	}
}
