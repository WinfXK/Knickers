package cn.winfxk.knickers.module.tp;

import cn.nukkit.Player;
import cn.winfxk.knickers.Activate;
import cn.winfxk.knickers.Config;
import cn.winfxk.knickers.form.FormBase;
import cn.winfxk.knickers.module.FunctionBase;
import cn.winfxk.knickers.module.ModuleData;

/**
 * @Createdate 2020/05/30 22:04:21
 * @author Winfxk
 */
public class TransferButton extends FunctionBase {
	public static final String TransferKey = "Transfer";

	public TransferButton(Activate ac) {
		super(ac, TransferKey);
	}

	@Override
	protected FormBase getMakeForm(Player player, Config config, FormBase base) {
		return new MakeTransfer(player, base, config, this);
	}

	@Override
	protected FormBase getAlterForm(FormBase form, ModuleData data) {
		return new AlterTransfer(form, TransferData.getTransferData(data));
	}

	@Override
	public TransferData getModuleData(Config config, String Key) {
		return new TransferData(config, Key);
	}

	@Override
	protected boolean ClickButton(FormBase form, ModuleData dd) {
		TransferData data = TransferData.getTransferData(dd);
		Player player = form.getPlayer();
		if (data.getLevel() == null) {
			player.sendMessage(getString("LevelError"));
			ac.getPluginBase().getLogger().error(getString("LevelError"));
			return form.MakeMain();
		}
		return player.teleport(data.getLocation());
	}
}
