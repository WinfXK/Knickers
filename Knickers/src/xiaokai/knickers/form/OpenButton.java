package xiaokai.knickers.form;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.utils.Config;
import xiaokai.knickers.mtp.Kick;
import xiaokai.knickers.mtp.Message;
import xiaokai.knickers.mtp.MyPlayer;
import xiaokai.tool.CustomForm;
import xiaokai.tool.SimpleForm;
import xiaokai.tool.Tool;

/**
 * @author Winfxk
 */
public class OpenButton {
	private Kick kick;
	private Player player;
	private File file;
	private String Key;
	private Map<String, Object> Item;
	private Config config;
	private Message msg;

	/**
	 * 打开一个按钮
	 * 
	 * @param k Kick对象
	 * @param p 玩家对象
	 * @param f 要打开的按钮的文件对象
	 * @param s 要打开的按钮的Key
	 */
	@SuppressWarnings("unchecked")
	public OpenButton(Kick k, Player p, File f, String s) {
		this.kick = k;
		this.player = p;
		this.file = f;
		this.Key = s;
		this.config = new Config(file, Config.YAML);
		Item = ((config.get("Buttons") != null && config.get("Buttons") instanceof Map)
				? (HashMap<String, Object>) config.get("Buttons")
				: new HashMap<String, Object>());
		Item = (Item.get(Key) != null && Item.get(Key) instanceof Map) ? (HashMap<String, Object>) Item.get(Key)
				: new HashMap<String, Object>();
		msg = kick.Message;
	}

	/**
	 * 开始处理玩家点击按钮的事件，这里是判断玩家点击跌是什么按钮类型
	 * 
	 * @return
	 */
	public boolean start() {
		if (Item.equals(new HashMap<String, Object>()) || Item.get("Type") == null
				|| String.valueOf(Item.get("Type")).isEmpty()) {
			player.sendMessage("§4按钮打开失败！");
			MakeForm.OpenMenu(player, file);
			return false;
		}
		switch (String.valueOf(Item.get("Type")).toLowerCase()) {
		case "command":
			return (new onCommand(player)).start(Item);
		case "open":
			return openUI();
		case "tp":
			return openTp();
		case "tpa":
			return (new Tpa(player, kick)).make(Key, config, Item);
		case "tip":
		default:
			return openTip();
		}
	}

	/**
	 * 当点击的按钮时点击后打开界面的按钮
	 * 
	 * @return
	 */
	private boolean openUI() {
		carryCommand(player, Item);
		String string = Item.get("Config") == null ? null : String.valueOf(Item.get("Config"));
		if (string == null || string.isEmpty())
			return MakeForm.Tip(player, Kick.kick.Message.getSon("界面", "打开按钮失败", new String[] { "{Player}", "{Error}" },
					new Object[] { player.getName(), "无法获取菜单配置文件" }));
		File file = new File(new File(kick.mis.getDataFolder(), Kick.MenuConfigPath), string);
		if (!file.exists())
			return MakeForm.Tip(player, Kick.kick.Message.getSon("界面", "打开按钮失败", new String[] { "{Player}", "{Error}" },
					new Object[] { player.getName(), "菜单配置文件不存在！" }));
		if (delMoney(player, Item))
			return MakeForm.OpenMenu(player, file, true);
		return false;
	}

	/**
	 * 处理玩家点击的按钮时Tp按钮的事件
	 * 
	 * @return
	 */
	private boolean openTp() {
		carryCommand(player, Item);
		if (Item.get("World") == null) {
			kick.mis.getLogger().error("一个按钮的数据可能发生了错误！按钮类型为：传送，数据错误项：无法获取目标世界名称！请检查");
			return MakeForm.Tip(player, Kick.kick.Message.getSon("界面", "打开按钮失败", new String[] { "{Player}", "{Error}" },
					new Object[] { player.getName(), "无法获取目标世界名称！" }));
		}
		Level level = getLevel(String.valueOf(Item.get("World")));
		if (level == null) {
			kick.mis.getLogger().error("一个按钮的数据可能发生了错误！按钮类型为：传送，数据错误项：无法获取目标世界对象！请检查");
			return MakeForm.Tip(player, Kick.kick.Message.getSon("界面", "打开按钮失败", new String[] { "{Player}", "{Error}" },
					new Object[] { player.getName(), "无法获取目标世界对象！" }));
		}
		if (delMoney(player, Item))
			player.teleport(
					new Position(ObjToDou(Item.get("X")), ObjToDou(Item.get("Y")), ObjToDou(Item.get("Z")), level));
		return true;
	}

	/**
	 * 通过世界名字获取世界对象
	 * 
	 * @param World
	 * @return
	 */
	public static Level getLevel(String World) {
		String world = World.toLowerCase();
		Server server = Server.getInstance();
		Level level = server.getLevelByName(World);
		Map<Integer, Level> levels = server.getLevels();
		Set<Integer> Keys = levels.keySet();
		Level as;
		if (level == null) {
			for (Integer i : Keys) {
				as = levels.get(i);
				if (as.getFolderName().equals(World) || as.getName().equals(World))
					level = as;
			}
			if (level == null) {
				for (Integer i : Keys) {
					as = levels.get(i);
					if (as.getFolderName().toLowerCase().equals(world) || as.getName().toLowerCase().equals(world))
						level = as;
				}
				if (level == null) {
					File file = new File(server.getDataPath(), "worlds/");
					String[] files = file.list();
					for (String Fn : files)
						if (Fn.equals(World)) {
							server.loadLevel(Fn);
							level = server.getLevelByName(Fn);
						}
					if (level == null)
						for (String Fn : files)
							if (Fn.toLowerCase().equals(world)) {
								server.loadLevel(Fn);
								level = server.getLevelByName(Fn);
							}
				}
			}
		}
		return level;
	}

	/**
	 * 处理玩家点击的按钮为TIP按钮的时间
	 * 
	 * @return
	 */
	private boolean openTip() {
		carryCommand(player, Item);
		if (delMoney(player, Item))
			return MakeForm.Tip(player,
					msg.getText(Item.get("Title"), new String[] { "{Player}" }, new Object[] { player.getName() }),
					msg.getText(Item.get("Content"), new String[] { "{Player}" }, new Object[] { player.getName() }),
					true, !Item.get("TipType").equals("Simple"));
		return false;
	}

	/**
	 * 检查点击的按钮是否需要扣费，如果需要，则讲究扣费
	 * 
	 * @param player 要检查扣费的玩家对象
	 * @param Item   玩家点击的按钮数据对象
	 * @return
	 */
	private static boolean delMoney(Player player, Map<String, Object> Item) {
		double Money = 0;
		String mString = String.valueOf(Item.get("Money") == null ? 0 : Item.get("Money"));
		if (mString != null && !mString.isEmpty() && Tool.isInteger(mString))
			Money = Double.valueOf(mString);
		if (Money <= 0)
			return true;
		Plugin MoneyAPI = Server.getInstance().getPluginManager().getPlugin("EconomyAPI");
		if (MoneyAPI == null || !MoneyAPI.isEnabled()) {
			Kick.kick.mis.getLogger().warning("§6EconomyAPI§4未安装或未启用！忽略本次扣费。");
			return true;
		}
		if (me.onebone.economyapi.EconomyAPI.getInstance().myMoney(player) < Money)
			return MakeForm.Tip(player, "§4提示", Kick.kick.Message.getMessage("金币不足", new String[] { "{Player}" },
					new Object[] { player.getName() }), false, true);
		me.onebone.economyapi.EconomyAPI.getInstance().reduceMoney(player, Money);
		return true;
	}

	/**
	 * 设置玩家点击按钮后处罚的命令
	 * 
	 * @param player 点击按钮的玩家对象
	 * @param Item   玩家点击跌按钮的数据
	 * @return
	 */
	private static boolean carryCommand(Player player, Map<String, Object> Item) {
		String Command = String.valueOf(Item.get("Command") == null ? "" : Item.get("Command"));
		if (Command == null || Command.isEmpty())
			return false;
		return Server.getInstance().dispatchCommand(player, Command);
	}

	/**
	 * 将Object对象转换为double对象
	 * 
	 * @param d
	 * @return
	 */
	private static double ObjToDou(Object d) {
		try {
			return Double.valueOf(String.valueOf(d));
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 处理玩家点击的是Tpa类型的按钮
	 * 
	 * @author Winfxk
	 *
	 */
	public static class Tpa {
		private Player player;
		private Kick kick;

		/**
		 * 创建玩家列表
		 * 
		 * @param player 要显示这个列表的玩家对象
		 * @param kick   Kick对象
		 */
		public Tpa(Player player, Kick kick) {
			this.player = player;
			this.kick = kick;
		}

		/**
		 * 处理玩家点击Tpa按钮后，但是需要请求，发送请求给玩家，玩家点击后的处理时间
		 * 
		 * @param data    返回的数据
		 * @param player2 处理请求的玩家对象
		 * @return
		 */
		public boolean isOK(FormResponseSimple data, Player player2) {
			if (data != null && data.getClickedButtonId() == 0)
				return Tp(player2);
			return MakeForm.Tip(player,
					kick.Message.getSun("界面", "Tpa界面", "传送请求被拒绝", new String[] { "{Player}", "{TpaPlayer}", "{Msg}" },
							new Object[] { player2.getName(), player.getName(), data != null ? "§4拒绝" : "§7取消" }));
		}

		/**
		 * 处理玩家点击的是哪一个玩家
		 * 
		 * @param data
		 * @return
		 */
		public boolean start(FormResponseSimple data) {
			MyPlayer myPlayer = Kick.kick.PlayerDataMap.get(player.getName());
			List<Player> Plist = myPlayer.players;
			Player player2 = Plist.size() > data.getClickedButtonId() ? Plist.get(data.getClickedButtonId()) : null;
			carryCommand(player, myPlayer.Item);
			if (!Tool.ObjToBool(myPlayer.Item.get("isAffirm")))
				return Tp(player2);
			SimpleForm form = new SimpleForm(kick.formID.getID(6),
					kick.Message.getSun("界面", "Tpa界面", "发请求的标题", new String[] { "{Player}", "{TpaPlayer}" },
							new Object[] { player2.getName(), player.getName() }),
					kick.Message.getSun("界面", "Tpa界面", "发请求的内容", new String[] { "{Player}", "{TpaPlayer}" },
							new Object[] { player2.getName(), player.getName() }));
			form.addButton(kick.Message.getSun("界面", "Tpa界面", "发请求确认按钮", new String[] { "{Player}", "{TpaPlayer}" },
					new Object[] { player2.getName(), player.getName() }));
			form.addButton(kick.Message.getSun("界面", "Tpa界面", "发请求取消按钮", new String[] { "{Player}", "{TpaPlayer}" },
					new Object[] { player2.getName(), player.getName() }));
			MyPlayer myPlayer2player2 = Kick.kick.PlayerDataMap.get(player2.getName());
			myPlayer2player2.TpaPlayer = player;
			Kick.kick.PlayerDataMap.put(player2.getName(), myPlayer2player2);
			form.sendPlayer(player2);
			return false;
		}

		/**
		 * 开始传送玩家
		 * 
		 * @param player2 要传送到的玩家坐标
		 */
		public boolean Tp(Player player2) {
			if (player2 != null && player2.isOnline()) {
				player.teleport(new Position(player2.getX(), player2.getY(), player2.getZ(), player2.getLevel()));
				player.sendMessage(
						kick.Message.getSun("界面", "Tpa界面", "传送提示", new String[] { "{Player}", "{TpaPlayer}" },
								new Object[] { player2.getName(), player.getName() }));
				return true;
			} else
				MakeForm.Tip(player,
						kick.Message.getSun("界面", "Tpa界面", "玩家不在线", new String[] { "{Player}", "{TpaPlayer}" },
								new Object[] { player2.getName(), player.getName() }));
			return true;
		}

		/**
		 * 创建按钮给玩家点击
		 * 
		 * @param Key    按钮的Key
		 * @param config 按钮所在的Config对象
		 * @return
		 */
		public boolean make(String Key, Config config, Map<String, Object> Item) {
			MyPlayer myPlayer = Kick.kick.PlayerDataMap.get(player.getName());
			myPlayer.Key = Key;
			Map<UUID, Player> Players = Server.getInstance().getOnlinePlayers();
			if (Players.size() < 2)
				return MakeForm.Tip(player, kick.Message.getSun("界面", "Tpa界面", "无玩家在线", new String[] { "{Player}" },
						new Object[] { player.getName() }));
			List<Player> Plist = new ArrayList<Player>();
			List<String> kList = new ArrayList<String>();
			SimpleForm form = new SimpleForm(kick.formID.getID(5),
					kick.Message.getSun("界面", "Tpa界面", "玩家列表标题", new String[] { "{Player}", "{BackTitle}" },
							new Object[] { player.getName(),
									kick.Message.getText(config.getString("Title"), new String[] { "{Player}" },
											new Object[] { player.getName() }) }),
					kick.Message.getSun("界面", "Tpa界面", "玩家列表内容", new String[] { "{Player}" },
							new Object[] { player.getName() }));
			for (UUID uuid : Players.keySet()) {
				Player player2 = Players.get(uuid);
				if (player2.getName().equals(player.getName()))
					continue;
				Plist.add(player2);
				kList.add(player2.getName());
				form.addButton(Tool.getRandColor() + player2.getName());
			}
			if (Plist.size() < 1)
				return MakeForm.Tip(player, kick.Message.getSun("界面", "Tpa界面", "无玩家在线", new String[] { "{Player}" },
						new Object[] { player.getName() }));
			carryCommand(player, Item);
			if (delMoney(player, Item)) {
				myPlayer.keyList = kList;
				myPlayer.players = Plist;
				myPlayer.Item = Item;
				kick.PlayerDataMap.put(player.getName(), myPlayer);
				form.sendPlayer(player);
				return true;
			}
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public static class onCommand {
		private Player player;

		/**
		 * 处理玩家点击是执行命令的按钮的事件
		 * 
		 * @param player
		 */
		public onCommand(Player player) {
			this.player = player;
		}

		/**
		 * 开始处理玩家输入命令的点击事件
		 * 
		 * @param data
		 * @return
		 */
		public boolean PY(FormResponseCustom data) {
			MyPlayer myPlayer = Kick.kick.PlayerDataMap.get(player.getName());
			List<String> Commands = myPlayer.Commnds;
			String string = "";
			for (int i = 0; i < Commands.size() - 1; i++)
				string += Commands.get(i) + data.getInputResponse(i);
			string += Commands.get(Commands.size() - 1);
			return carryCommand(player, string, myPlayer.Commander);
		}

		/**
		 * 如果点击的是带{msg}的按钮，那么创建界面
		 * 
		 * @param Command   按钮的命令
		 * @param Commander 按钮的命令的执行对象
		 * @param Item      按钮的数据
		 * @return
		 */
		private boolean make(String Command, String Commander, Map<String, Object> Item) {
			MyPlayer myPlayer = Kick.kick.PlayerDataMap.get(player.getName());
			Kick kick = Kick.kick;
			String[] Commands = Command.split("\\{msg\\}");
			CustomForm form = new CustomForm(kick.formID.getID(7), kick.Message.getSun("界面", "执行命令", "标题",
					new String[] { "{Player}" }, new Object[] { player.getName() }));
			List<String> def = (Item.get("Msg") == null || !(Item.get("Msg") instanceof List)) ? new ArrayList<String>()
					: (ArrayList<String>) Item.get("Msg");
			List<String> Hints = (Item.get("Hint") == null || !(Item.get("Hint") instanceof List))
					? new ArrayList<String>()
					: (ArrayList<String>) Item.get("Hint");
			for (int i = 0; i < Commands.length - 1; i++)
				form.addInput(def.size() < i + 1 ? "" : def.get(i), "", Hints.size() < i + 1 ? "" : Hints.get(i));
			myPlayer.Commnds = Arrays.asList(Commands);
			myPlayer.Commander = Commander;
			kick.PlayerDataMap.put(player.getName(), myPlayer);
			form.sendPlayer(player);
			return true;
		}

		/**
		 * 开始处理
		 * 
		 * @param Item 玩家点击的按钮的数据对象
		 * @return
		 */
		public boolean start(Map<String, Object> Item) {
			String Command = String.valueOf(Item.get("Command"));
			String Commander = String.valueOf(Item.get("Commander"));
			if (Command != null && !Command.isEmpty() && Commander != null && !Commander.isEmpty()
					&& delMoney(player, Item)) {
				Command = Kick.kick.Message.getText(Command, new String[] { "{Player}" },
						new Object[] { player.getName() });
				Command = (Command.lastIndexOf("}") == Command.length() - 1) ? Command + " " : Command;
				System.out.println(Command);
				if (Command.contains("{msg}")) {
					return make(Command, Commander, Item);
				} else
					return carryCommand(player, Command, Commander);
			}
			return MakeForm.Tip(player, Kick.kick.Message.getSun("界面", "执行命令", "打开失败",
					new String[] { "{Player}", "{Error}" }, new Object[] { player.getName(), "获取的命令为空！" }));
		}

		/**
		 * 执行命令
		 * 
		 * @param player    触发这个事件的玩家对象
		 * @param Command   要执行的命令
		 * @param Commander 要执行命令的对象[<b>Console</b>|<b>PlayerByOp</b>|<b>Player</b>]
		 * @return
		 */
		private static boolean carryCommand(Player player, String Command, String Commander) {
			boolean isOP = player.isOp();
			if (Commander.toLowerCase().equals("playerbyop") && !isOP)
				player.setOp(true);
			boolean cmd = Server.getInstance().dispatchCommand(
					(Commander.toLowerCase().equals("player") || Commander.toLowerCase().equals("playerbyop")) ? player
							: new ConsoleCommandSender(),
					Command);
			if (Commander.toLowerCase().equals("playerbyop") && !isOP)
				player.setOp(false);
			return cmd;
		}
	}
}
