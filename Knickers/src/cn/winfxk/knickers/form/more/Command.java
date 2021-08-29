package cn.winfxk.knickers.form.more;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.form.response.FormResponseCustom;
import cn.winfxk.knickers.form.FormBase;
import cn.winfxk.knickers.form.MakeForm;
import cn.winfxk.knickers.form.base.CustomForm;
import cn.winfxk.knickers.rec.SecurityPermissions;
import cn.winfxk.knickers.tool.MyMap;
import cn.winfxk.knickers.tool.Tool;

/**
 * @Createdate 2021/08/01 12:54:57
 * @author Winfxk
 */
public class Command extends FormBase {
	private Map<String, Object> map;
	private String Command;
	private List<String> Hint, Msg;
	private String[] CMD;
	private boolean isEmpty;
	private String Permission;

	public Command(Player player, FormBase upForm, Map<String, Object> map) {
		super(player, upForm);
		this.map = map;
		Command = Tool.objToString(map.get("Command"));
		Command = Command == null ? "" : Command;
		Object obj = map.get("Hint");
		Hint = obj == null || !(obj instanceof List) ? new ArrayList<>() : (ArrayList<String>) obj;
		obj = map.get("Msg");
		Son = "Command";
		Permission = Tool.objToString(map.get("Commander"), "Player");
		Permission = Permission == null ? "Player" : Permission;
		isEmpty = Tool.ObjToBool(map.get("isEmpty"));
		Msg = obj != null && obj instanceof List ? (ArrayList<String>) obj : new ArrayList<>();
	}

	@Override
	public boolean MakeMain() {
		if (Command == null || Command.isEmpty())
			return setForm(new MakeForm(player, upForm, MyMap.make("isBack", (Object) true).add("Title", getString("Tip")).add("Content", getString("CommandNull")))).make();
		CMD = msg.getText(Command, this).split("\\{msg\\}");
		if (Hint == null || Msg == null || CMD.length != Hint.size() || Hint.size() <= 0 || Hint.size() != Msg.size() || Msg.size() <= 0)
			return setForm(new MakeForm(player, upForm, MyMap.make("isBack", (Object) true).add("Title", getString("Tip")).add("Content", getString("MsgNull")))).make();
		CustomForm form = new CustomForm(getID(), msg.getText(map.get("Title"), this));
		form.addLabel(getContent());
		for (int i = 0; i < Hint.size() && i < Msg.size(); i++)
			form.addInput(msg.getText(Msg.get(i), this), "", msg.getText(Hint.get(i)));
		form.sendPlayer(player);
		return true;
	}

	@Override
	public boolean disMain(FormResponse data) {
		String C = CMD[0];
		FormResponseCustom d = getCustom(data);
		String string;
		for (int i = 0; i < Hint.size() && i < Msg.size(); i++) {
			string = d.getInputResponse(i + 1);
			if (!isEmpty && string == null || string.isEmpty())
				return setForm(new MakeForm(player, upForm, MyMap.make("isBack", (Object) true).add("Title", getString("Tip")).add("Content", getString("InputNull")))).make();
			C += string;
		}
		C += CMD[CMD.length - 1];
		switch (Permission.toLowerCase()) {
		case "console":
		case "c":
		case "控制台":
			if (C.contains("{m}")) {
				String[] cmds = C.split("\\{m\\}");
				for (String s : cmds)
					if (s != null && !s.isEmpty())
						try {
							server.dispatchCommand(new ConsoleCommandSender(), s);
						} catch (Exception e) {
						}
			} else
				return server.dispatchCommand(new ConsoleCommandSender(), C);
			return true;
		case "op":
		case "admin":
		case "管理员":
			if (player.isOp())
				return server.dispatchCommand(player, C);
			myPlayer.SecurityPermissions = true;
			player.setOp(true);
			try {
				if (C.contains("{m}")) {
					String[] cmds = C.split("\\{m\\}");
					for (String s : cmds)
						if (s != null && !s.isEmpty())
							try {
								server.dispatchCommand(player, s);
							} catch (Exception e) {
							}
				} else
					return server.dispatchCommand(player, C);
			} catch (Exception e) {
				return sendMessage(msg.getSun(t, "Command", "Error", this));
			} finally {
				new SecurityPermissions(myPlayer).start();
			}
			return true;
		default:
			if (C.contains("{m}")) {
				String[] cmds = C.split("\\{m\\}");
				for (String s : cmds)
					if (s != null && !s.isEmpty())
						try {
							server.dispatchCommand(player, s);
						} catch (Exception e) {
						}
			} else
				return server.dispatchCommand(player, C);
			return true;
		}
	}

	@Override
	protected String getTitle() {
		return msg.getText(map.get("Title"), this);
	}

	@Override
	protected String getContent() {
		return msg.getText(map.get("Content"), this);
	}
}
