package xiaokai.knickers.module.base;

import xiaokai.knickers.module.Module;

import cn.nukkit.form.response.FormResponseCustom;

/**
 * @author Winfxk
 */
public class Opendialog extends Module {
	private final static String Type = "Tip", ModuleName = "提示一个窗口";

	@Override
	public String getButtonText() {
		return msg.getText(map.get("Text"), player);
	}

	@Override
	public String getModuleName() {
		return ModuleName;
	}

	@Override
	public String getType() {
		return Type;
	}

	@Override
	public boolean MakeButtonsave(FormResponseCustom data) {
		return false;
	}

	@Override
	public boolean Altersave(FormResponseCustom data) {
		return false;
	}

	@Override
	public boolean MakeButton() {
		return false;
	}

	@Override
	public boolean onClick() {
		return false;
	}

	@Override
	public boolean Alter() {
		return false;
	}

}