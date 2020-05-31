package cn.winfxk.knickers.module.ptp;

import cn.nukkit.Player;
import cn.winfxk.knickers.Activate;
import cn.winfxk.knickers.Config;
import cn.winfxk.knickers.form.FormBase;
import cn.winfxk.knickers.module.FunctionBase;
import cn.winfxk.knickers.module.ModuleData;

/**
 * 点击后获取一个列表 传送到在线玩家处的按钮
 * 
 * @Createdate 2020/05/31 19:07:30
 * @author Winfxk
 */
public class PtPlayerButton extends FunctionBase {
	public static final String PtPlayerKey = "PtPlayer";

	public PtPlayerButton(Activate ac) {
		super(ac, PtPlayerKey);
	}

	@Override
	public PtPData getModuleData(Config config, String Key) {
		return new PtPData(config, Key);
	}

	@Override
	protected FormBase getMakeForm(Player player, Config config, FormBase base) {
		return new MakePtP(player, base, config, this);
	}

	@Override
	protected FormBase getAlterForm(FormBase form, ModuleData data) {
		return new AlterPtP(form, PtPData.getData(data));
	}

	@Override
	protected boolean ClickButton(FormBase form, ModuleData sb) {
		return (form.getMyPlayer().form = new ClickPtP(form.getPlayer(), form, PtPData.getData(sb))).MakeMain();
	}
}
