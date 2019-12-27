package xiaokai.knickers;

import xiaokai.knickers.tool.Tool;

import cn.nukkit.utils.Config;

/**
 * @author Winfxk
 */
public class FormID {
	protected Config FormIDConfig;
	private int MainMenuID = 0;

	public FormID() {
		examine();
	}

	public Config getFormIDConfig() {
		return FormIDConfig;
	}

	public int getID(String Key) {
		if (Key == null || Key.isEmpty())
			return getRand();
		if (!FormIDConfig.exists(Key))
			examine();
		return Tool.ObjectToInt(FormIDConfig.get(Key), getRand());
	}

	public int getMenu() {
		MainMenuID = MainMenuID == 0 ? 1 : 0;
		return getID(MainMenuID);
	}

	public int getID(int ID) {
		if (ID > Activate.FormIDs.length - 1)
			return getRand();
		String Key = Activate.FormIDs[ID];
		if (!FormIDConfig.exists(Key))
			examine();
		return FormIDConfig.getInt(Key);
	}

	private void examine() {
		for (String s : Activate.FormIDs)
			if (FormIDConfig.exists(s)) {
				Object obj = FormIDConfig.get(s);
				if (!Tool.isInteger(obj) || Tool.ObjectToInt(obj, -1) < 0)
					FormIDConfig.set(s, Tool.getRand());
			} else
				FormIDConfig.set(s, getRand());
		FormIDConfig.save();
	}

	private int getRand() {
		return Tool.getRand(0, 2015641654);
	}
}
