package cn.winfxk.knickers.module;

import java.util.Map;

import cn.winfxk.knickers.Activate;
import cn.winfxk.knickers.MyMap;
import cn.winfxk.knickers.module.cmd.CommandButton;

/**
 * 管理菜单拥有的功能的管理器
 * 
 * @Createdate 2020/05/22 19:26:43
 * @author Winfxk
 */
public class FunctionMag {
	private final MyMap<String, FunctionBase> Function = new MyMap<>();

	public FunctionMag(Activate activate) {
		addFunction(new CommandButton(activate));
	}

	/**
	 * 删除一个已经支持的功能
	 * 
	 * @param FunctionKey
	 * @return
	 */
	public boolean removeFunction(FunctionBase function) {
		if (Function.containsValue(function))
			Function.removeValues(function);
		return !Function.containsKey(function.getKey()) && !Function.containsValue(function);
	}

	/**
	 * 删除一个已经支持的功能
	 * 
	 * @param FunctionKey 功能的Key
	 * @return
	 */
	public boolean removeFunction(String FunctionKey) {
		if (!Function.containsKey(FunctionKey))
			return false;
		Function.remove(FunctionKey);
		return !Function.containsKey(FunctionKey);
	}

	/**
	 * 添加一个功能
	 * 
	 * @param function
	 */
	public void addFunction(FunctionBase function) {
		Function.put(function.getKey(), function);
	}

	/**
	 * 返回功能的列表
	 * 
	 * @return
	 */
	public Map<String, FunctionBase> getFunction() {
		return Function;
	}
}
