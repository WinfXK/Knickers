package cn.winfxk.knickers.module.say;

import cn.nukkit.Player;
import cn.winfxk.knickers.Activate;
import cn.winfxk.knickers.Config;
import cn.winfxk.knickers.form.FormBase;
import cn.winfxk.knickers.module.FunctionBase;
import cn.winfxk.knickers.module.ModuleData;

/**
 * 点击后说话的按钮
 * 
 * @Createdate 2020/05/30 19:08:21
 * @author Winfxk
 */
public class SayButton extends FunctionBase {
	public static final String SayKey = "Say";

	public SayButton(Activate ac) {
		super(ac, SayKey);
	}

	@Override
	public SayData getModuleData(Config config, String Key) {
		return new SayData(config, Key);
	}

	@Override
	protected FormBase getMakeForm(Player player, Config config, FormBase base) {
		return new MakeSay(player, base, config, this);
	}

	@Override
	public FormBase getAlterForm(FormBase form, ModuleData data) {
		return new AlterSay(form, SayData.getSayData(data));
	}

	@Override
	public boolean ClickButton(FormBase form, ModuleData dd) {
		SayData data = SayData.getSayData(dd);
		form.getPlayer().chat(data.getSay(form));
		return true;
	}
}
