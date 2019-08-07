package xiaokai.knickers.form;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.utils.Config;
import xiaokai.knickers.form.man.AddButton;
import xiaokai.knickers.form.man.DelButton;
import xiaokai.knickers.mtp.Kick;
import xiaokai.knickers.mtp.Message;
import xiaokai.knickers.mtp.MyPlayer;
import xiaokai.tool.ItemIDSunName;
import xiaokai.tool.Tool;

/**
 * @author Winfxk
 */
public class Dispose {
	private Player player;
	private Kick kick;

	/**
	 * 数据处理
	 * 
	 * @param kick   Kick对象
	 * @param player 触发事件的玩家对象
	 */
	public Dispose(Kick kick, Player player) {
		this.kick = kick;
		this.player = player;
	}

	/**
	 * 处理来至玩家设置数据的数据
	 * 
	 * @param data
	 * @return
	 */
	public boolean Setting(FormResponseCustom data) {
		String id = data.getInputResponse(0);
		if (id == null || id.isEmpty())
			return MakeForm.Tip(player, "§4请输入快捷工具的物品名称或物品ID!");
		id = ItemIDSunName.UnknownToID(id);
		String MoneyName = data.getInputResponse(1);
		if (MoneyName == null || MoneyName.isEmpty())
			return MakeForm.Tip(player, "§4请输入服务器货币的名称!");
		boolean isUpdate = data.getToggleResponse(2);
		String UpdateString = data.getInputResponse(3);
		if (UpdateString == null || UpdateString.isEmpty())
			return MakeForm.Tip(player, "§4请输入插件更新间隔时间！");
		int UpdateTime = 0;
		if (!Tool.isInteger(UpdateString) || !((UpdateTime = Float.valueOf(UpdateString).intValue()) > 0))
			return MakeForm.Tip(player, "§4插件更新间隔时间仅支持大于零的纯整数！！");
		String ClickTimeString = data.getInputResponse(4);
		if (ClickTimeString == null || ClickTimeString.isEmpty())
			return MakeForm.Tip(player, "§4请输入快捷工具屏蔽双击的时间！！");
		int ClickTime = 0;
		if (!Tool.isInteger(ClickTimeString) || !((ClickTime = Float.valueOf(ClickTimeString).intValue()) > 0))
			return MakeForm.Tip(player, "§4快捷工具屏蔽双击的时间仅支持大于零的纯整数！！");
		boolean isWh = data.getToggleResponse(5);
		List<String> list = new ArrayList<String>();
		String wsString = data.getInputResponse(6);
		if (wsString != null && !wsString.isEmpty())
			if (wsString.contains(";")) {
				String[] Ws = wsString.split(";");
				for (int i = 0; i < Ws.length; i++)
					if (Ws[i] != null && !Ws[i].isEmpty())
						list.add(Ws[i]);
			} else
				list = Arrays.asList(wsString);
		boolean isC = data.getToggleResponse(7);
		String sovString = data.getInputResponse(8);
		if (sovString == null || sovString.isEmpty())
			return MakeForm.Tip(player, "§4请输入自动检查快捷工具的间隔");
		int SovTime = 0;
		if (!Tool.isInteger(sovString) || (SovTime = Float.valueOf(sovString).intValue()) < 1)
			return MakeForm.Tip(player, "§4自动检查快捷工具的间隔仅支持大于等于0的纯整数！");
		Config config = kick.config;
		boolean isD = data.getToggleResponse(9);
		boolean isToolShowIcon = data.getToggleResponse(10);
		String aboutToolTimeString = data.getInputResponse(11);
		int aboutTime = 0;
		if (aboutToolTimeString == null || !Tool.isInteger(sovString) || aboutToolTimeString.isEmpty()
				|| (aboutTime = Float.valueOf(aboutToolTimeString).intValue()) < 1)
			return MakeForm.Tip(player, "§4自定义工具异步检查持有的间隔仅支持大于等于0的纯整数！");
		config.set("快捷工具", id);
		config.set("货币单位", MoneyName);
		config.set("检测更新", isUpdate);
		config.set("检测更新间隔", UpdateTime);
		config.set("屏蔽玩家双击间隔", ClickTime);
		config.set("仅允许白名单管理菜单", isWh);
		config.set("白名单", list);
		config.set("打开撤销", isC);
		config.set("定时检查快捷工具间隔", SovTime);
		config.set("是否允许玩家丢弃快捷工具", isD);
		config.set("自定义工具列表显示工具图标", isToolShowIcon);
		config.set("自定义工具异步检查持有间隔", aboutTime);
		kick.config = config;
		if (kick.config.save())
			return MakeForm.Tip(player, "§6数据保存正常！\n\n§a各类时间间隔将在检查一次后生效");
		else
			return MakeForm.Tip(player, "§4数据保存可能出现问题！");
	}

	/**
	 * 接收玩家点击主页控件的事件
	 * 
	 * @param data
	 * @return
	 */
	public boolean start(FormResponseSimple data) {
		System.err.println(10010);
		Message msg = kick.Message;
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		int ID = data.getClickedButtonId();
		if (ID >= myPlayer.Items.size())
			if (ID == myPlayer.Items.size()) {
				if (myPlayer.OpenMenuList == null || myPlayer.OpenMenuList.size() < 1
						|| (new File(kick.mis.getDataFolder(), kick.MainFileName).getAbsolutePath()
								.equals(myPlayer.BackFile.getAbsolutePath()))
						|| (myPlayer.OpenMenuList.size() >= 2 && myPlayer.BackFile.getAbsolutePath()
								.equals(myPlayer.OpenMenuList.get(myPlayer.OpenMenuList.size() - 2).getAbsolutePath())))
					return true;
				if (myPlayer.OpenMenuList.size() > 1) {
					File file = myPlayer.OpenMenuList.get(myPlayer.OpenMenuList.size() - 1);
					myPlayer.OpenMenuList.remove(myPlayer.OpenMenuList.size() - 1);
					while (file.getAbsolutePath().equals(myPlayer.BackFile.getAbsolutePath())
							&& myPlayer.OpenMenuList.size() > 0) {
						file = myPlayer.OpenMenuList.get(myPlayer.OpenMenuList.size() - 1);
						myPlayer.OpenMenuList.remove(myPlayer.OpenMenuList.size() - 1);
					}
					kick.PlayerDataMap.put(player.getName(), myPlayer);
					return MakeForm.OpenMenu(player, file, false, false);
				} else if (!myPlayer.isMain)
					return MakeForm.Main(player);
				else
					return true;
			} else if (Kick.isAdmin(player)) {
				if (ID == myPlayer.Items.size() + 1)
					return AddButton.addButton(player, myPlayer.BackFile);
				else if (ID == (myPlayer.Items.size() + 2))
					return DelButton.delButton(player, myPlayer.BackFile);
				else
					return MakeForm.Setting(player);
			} else
				return MakeForm.Tip(player, msg.getMessage("权限不足"));
		return new OpenButton(kick, player, myPlayer.BackFile, myPlayer.Items.get(ID).get("Key").toString()).start();
	}
}
