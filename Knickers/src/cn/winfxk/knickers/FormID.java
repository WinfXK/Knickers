package cn.winfxk.knickers;

import cn.nukkit.utils.Config;
import cn.winfxk.knickers.tool.Tool;

/**
 * @author Winfxk
 */
public class FormID {
	protected Config FormIDConfig;

	public Config getFormIDConfig() {
		return FormIDConfig;
	}

	/**
	 * 根据ID名称得到ID
	 *
	 * @param Key
	 * @return
	 */
	public int getID(String Key) {
		if (Key == null || Key.isEmpty())
			return getRand();
		if (!FormIDConfig.exists(Key))
			examine();
		return Tool.ObjToInt(FormIDConfig.get(Key), getRand());
	}

	/**
	 * 根据ID位置得到ID
	 *
	 * @param ID
	 * @return
	 */
	public int getID(int ID) {
		if (ID > Activate.FormIDs.length - 1)
			return getRand();
		String Key = Activate.FormIDs[ID];
		if (!FormIDConfig.exists(Key))
			examine();
		return FormIDConfig.getInt(Key);
	}

	/**
	 * 进行ID数据检查
	 */
	protected void examine() {
		for (String s : Activate.FormIDs)
			if (FormIDConfig.exists(s)) {
				Object obj = FormIDConfig.get(s);
				if (!Tool.isInteger(obj) || Tool.ObjToInt(obj, -1) < 0)
					FormIDConfig.set(s, Tool.getRand());
			} else
				FormIDConfig.set(s, getRand());
		FormIDConfig.save();
	}

	/**
	 * 返回一个大于零的随机数ID
	 *
	 * @return
	 */
	private int getRand() {
		return Tool.getRand(0, 2015641654);
	}
}
