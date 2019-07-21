package xiaokai.knickers.form;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponseSimple;
import xiaokai.knickers.form.man.AddButton;
import xiaokai.knickers.form.man.DelButton;
import xiaokai.knickers.mtp.Kick;
import xiaokai.knickers.mtp.Message;
import xiaokai.knickers.mtp.MyPlayer;

/**
 * @author Winfxk
 */
public class Dispose {
	private Player player;
	private Kick kick;

	public Dispose(Kick kick, Player player) {
		this.kick = kick;
		this.player = player;
	}

	/**
	 * 接收玩家点击主页控件的事件
	 * 
	 * @param data
	 * @return
	 */
	public boolean Main(FormResponseSimple data) {
		Message msg = kick.Message;
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		int ID = data.getClickedButtonId();
		if (ID >= myPlayer.Items.size())
			if (Kick.isAdmin(player)) {
				if (ID == myPlayer.Items.size())
					return AddButton.addButton(player, myPlayer.BackFile);
				else if (ID == (myPlayer.Items.size() + 1))
					return DelButton.delButton(player, myPlayer.BackFile);
				else
					return MakeForm.Setting(player);
			} else
				return MakeForm.Tip(player, msg.getMessage("权限不足"));
		return new OpenButton(kick, player, myPlayer.BackFile, myPlayer.Items.get(0).get("Key").toString()).start();
	}
}
