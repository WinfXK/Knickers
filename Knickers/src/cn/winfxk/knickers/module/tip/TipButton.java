package cn.winfxk.knickers.module.tip;

import java.util.ArrayList;
import java.util.List;

import cn.nukkit.Player;
import cn.winfxk.knickers.Activate;
import cn.winfxk.knickers.Config;
import cn.winfxk.knickers.form.FormBase;
import cn.winfxk.knickers.module.FunctionBase;
import cn.winfxk.knickers.module.ModuleData;

/**
 * 点击后会弹出一个提示窗口的按钮
 * 
 * @Createdate 2020/05/31 23:01:43
 * @author Winfxk
 */
public class TipButton extends FunctionBase {
	public static final String TipKey = "Tip";
	public static final List<String> TipType = new ArrayList<>(), TypeKey = new ArrayList<>();
	static {
		TypeKey.add("Simple");
		TypeKey.add("Modal");
	}

	public TipButton(Activate ac) {
		super(ac, TipKey);
		TipType.add(getString("TipType1"));
		TipType.add(getString("TipType2"));
	}

	@Override
	public TipData getModuleData(Config config, String Key) {
		return new TipData(config, Key);
	}

	@Override
	protected FormBase getMakeForm(Player player, Config config, FormBase base) {
		return new MakeTip(player, base, config, this);
	}

	@Override
	protected FormBase getAlterForm(FormBase form, ModuleData data) {
		return new AlterTip(form, TipData.geTipData(data));
	}

	@Override
	protected boolean ClickButton(FormBase form, ModuleData xx) {
		return ((form.getMyPlayer()).form = new ClickTip(form, TipData.geTipData(xx))).MakeMain();
	}
}
