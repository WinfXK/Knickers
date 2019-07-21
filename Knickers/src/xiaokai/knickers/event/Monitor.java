package xiaokai.knickers.event;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.response.FormResponseModal;
import cn.nukkit.form.response.FormResponseSimple;
import xiaokai.knickers.form.Dispose;
import xiaokai.knickers.form.OpenButton;
import xiaokai.knickers.form.man.AddButton;
import xiaokai.knickers.form.man.DelButton;
import xiaokai.knickers.mtp.FormID;
import xiaokai.knickers.mtp.Kick;

/**
 * @author Winfxk
 */
public class Monitor implements Listener {
	private Kick kick;

	public Monitor(Kick kick) {
		this.kick = kick;
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
	public void onPlayerForm(PlayerFormRespondedEvent e) {
		Player player = e.getPlayer();
		if (player == null || e.wasClosed() || e.getResponse() == null
				|| (!(e.getResponse() instanceof FormResponseCustom) && !(e.getResponse() instanceof FormResponseSimple)
						&& !(e.getResponse() instanceof FormResponseModal)))
			return;
		FormResponse data = e.getResponse();
		int ID = e.getFormID();
		FormID fId = kick.formID;
		if (ID == fId.getID(0))
			(new Dispose(kick, player)).Main((FormResponseSimple) data);
		else if (ID == fId.getID(1))
			(new AddButton.Dispose(kick, player)).start((FormResponseSimple) data);
		else if (ID == fId.getID(2))
			(new AddButton.Start(kick, player)).Switch((FormResponseCustom) data);
		else if (ID == fId.getID(3))
			DelButton.Del(player, (FormResponseSimple) data);
		else if (ID == fId.getID(4))
			DelButton.start(player, (FormResponseSimple) data);
		else if (ID == fId.getID(5))
			(new OpenButton.Tpa(player, kick)).start((FormResponseSimple) data);
		else if (ID == fId.getID(6))
			(new OpenButton.Tpa(kick.PlayerDataMap.get(player.getName()).TpaPlayer, kick))
					.isOK((FormResponseSimple) data, player);
		else if (ID == fId.getID(7))
			(new OpenButton.onCommand(player)).PY((FormResponseCustom) data);
	}
}
