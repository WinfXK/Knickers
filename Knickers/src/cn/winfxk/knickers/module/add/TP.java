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

	public TP(Player player, File file, FormBase upForm) {
		super(player, file, upForm);
	}

	@Override
	public boolean MakeMain() {
		if (!super.MakeMain())
			return false;
		form.addInput(getString("X"), player.getX(), getString("X"));
		form.addInput(getString("Z"), player.getZ(), getString("Z"));
		form.addInput(getString("Y"), player.getY(), getString("Y"));
		form.addInput(getString("World"), player.getLevel().getFolderName(), getString("World"));
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
		if (!Tool.isInteger(string))
			return Tip(getString("isInteger"), false);
		X = Tool.objToDouble(string);
		string = d.getInputResponse(location + 2);
		if (!Tool.isInteger(string))
			return Tip(getString("isInteger"), false);
		Y = Tool.objToDouble(string);
		string = d.getInputResponse(location + 3);
		if (!Tool.isInteger(string))
			return Tip(getString("isInteger"), false);
		Z = Tool.objToDouble(string);
		World = d.getInputResponse(location + 4);
		Level level = server.getLevelByName(World);
		if (level == null)
			sendMessage(getString("WorldEmpty"));
		map.put("X", X);
		map.put("Y", Y);
		map.put("Z", Z);
		map.put("World", World);
		save();
		sendMessage(getString("Succeed"));
		return isBack();
	}
}
