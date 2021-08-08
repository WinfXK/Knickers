package cn.winfxk.knickers.module.add;

import java.io.File;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.level.Level;
import cn.winfxk.knickers.form.FormBase;
import cn.winfxk.knickers.module.BaseMake;
import cn.winfxk.knickers.tool.Tool;

/**
 * 创建一个点击后可以传送到定点的界面时显示的界面
 * 
 * @Createdate 2021/08/07 21:48:54
 * @author Winfxk
 */
public class TP extends BaseMake {
	public TP(Player player, File file, FormBase upForm, String Key) {
		super(player, file, upForm, Key);
	}

	public TP(Player player, File file, FormBase upForm) {
		super(player, file, upForm);
	}

	@Override
	public boolean MakeMain() {
		if (!super.MakeMain())
			return false;
		form.addInput(getString("X"), Key == null ? player.getX() : map.get("X"), getString("X"));
		form.addInput(getString("Z"), Key == null ? player.getZ() : map.get("Z"), getString("Z"));
		form.addInput(getString("Y"), Key == null ? player.getY() : map.get("Y"), getString("Y"));
		form.addInput(getString("World"), Key == null ? player.getLevel().getFolderName() : map.get("World"), getString("World"));
		form.sendPlayer(player);
		return true;
	}

	@Override
	public boolean disMain(FormResponse data) {
		if (!super.disMain(data))
			return false;
		double X, Y, Z;
		String string, World;
		string = d.getInputResponse(location + 1);
		if (string == null || string.isEmpty() || !Tool.isInteger(string))
			return Tip(getString("isInteger"), false);
		X = Tool.objToDouble(string);
		string = d.getInputResponse(location + 2);
		if (string == null || string.isEmpty() || !Tool.isInteger(string))
			return Tip(getString("isInteger"), false);
		Y = Tool.objToDouble(string);
		string = d.getInputResponse(location + 3);
		if (string == null || string.isEmpty() || !Tool.isInteger(string))
			return Tip(getString("isInteger"), false);
		Z = Tool.objToDouble(string);
		World = d.getInputResponse(location + 4);
		if (World == null || World.isEmpty())
			return sendMessage(getString("WorldEmpty"));
		Level level = server.getLevelByName(World);
		if (level == null)
			return sendMessage(getString("WorldEmpty"));
		map.put("X", X);
		map.put("Y", Y);
		map.put("Z", Z);
		map.put("World", World);
		save();
		sendMessage(getString("Succeed"));
		return isBack();
	}
}
