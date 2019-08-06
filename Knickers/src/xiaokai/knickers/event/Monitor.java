package xiaokai.knickers.event;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.response.FormResponseModal;
import cn.nukkit.form.response.FormResponseSimple;
import xiaokai.knickers.appliance.EstablishForm;
import xiaokai.knickers.appliance.Handle;
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

	@EventHandler
	public void onPlayerForm(PlayerFormRespondedEvent e) {
		FormResponse data = e.getResponse();
		int ID = e.getFormID();
		FormID fId = kick.formID;
		Player player = e.getPlayer();
		if (player != null && ID == fId.getID(6)) {
			(new OpenButton.Tpa(kick.PlayerDataMap.get(player.getName()).TpaPlayer, kick))
					.isOK(e.wasClosed() ? null : (FormResponseSimple) data, player);
			return;
		}
		if (player == null || e.wasClosed() || e.getResponse() == null
				|| (!(e.getResponse() instanceof FormResponseCustom) && !(e.getResponse() instanceof FormResponseSimple)
						&& !(e.getResponse() instanceof FormResponseModal)))
			return;
		if (ID == fId.getID(0) || ID == fId.getID(8) || ID == fId.getID(10) || ID == fId.getID(11)
				|| ID == fId.getID(12) || ID == fId.getID(13))
			(new Dispose(kick, player)).start((FormResponseSimple) data);
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
		else if (ID == fId.getID(7))
			(new OpenButton.onCommand(player)).PY((FormResponseCustom) data);
		else if (ID == fId.getID(9))
			(new Dispose(kick, player)).Setting((FormResponseCustom) data);
		else if (ID == fId.getID(14))
			Handle.Make.Switch(player, (FormResponseSimple) data);
		else if (ID == fId.getID(15))
			(new Handle.Make(player)).addOpen((FormResponseCustom) data);
		else if (ID == fId.getID(16))
			EstablishForm.ButtonByPlayer.add(player, (FormResponseCustom) data);
		else if (ID == fId.getID(17))
			(new Handle.Make(player)).addButton((FormResponseCustom) data);
		else if (ID == fId.getID(18))
			Handle.Switch(player, (FormResponseSimple) data);
		else if (ID == fId.getID(19))
			EstablishForm.disToolList(player, (FormResponseSimple) data);
		else if (ID == fId.getID(20))
			EstablishForm.disDelTool(player, (FormResponseSimple) data);
		
	}
}
