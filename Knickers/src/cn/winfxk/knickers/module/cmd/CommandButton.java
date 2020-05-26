package cn.winfxk.knickers.module.cmd;

import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;
import cn.winfxk.knickers.Activate;
import cn.winfxk.knickers.form.ButtonBase;
import cn.winfxk.knickers.form.FormBase;
import cn.winfxk.knickers.module.FunctionBase;
import cn.winfxk.knickers.module.ModuleData;

/**
 * 命令按钮-点击后执行一个命令
 * 
 * @Createdate 2020/05/22 23:41:48
 * @author Winfxk
 */
public class CommandButton extends FunctionBase {

	public CommandButton(Activate ac) {
		super(ac, "Command");
	}

	@Override
	public boolean ClickButton(ButtonBase form, String Key) {
		return false;
	}

	@Override
	protected FormBase getForm(Player player, Config config, FormBase base) {
		return null;
	}

	@Override
	public ModuleData getModuleData(Map<String, Object> map) {
		return null;
	}
}
