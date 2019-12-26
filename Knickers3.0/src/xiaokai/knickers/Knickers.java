package xiaokai.knickers;

import xiaokai.knickers.tool.Tool;

import cn.nukkit.plugin.PluginBase;

/**
 * @author Winfxk
 */
public class Knickers extends PluginBase {
	private static Activate ac;

	@Override
	public void onDisable() {
		getLogger().info(getName() + " has stopped");
	}

	@Override
	public void onEnable() {
		ac = new Activate(this);
		getLogger().info(ac.message.getMessage("插件启动"));
	}

	public static Activate getInstance() {
		return ac;
	}

	@Override
	public void onLoad() {
		getLogger().info(Tool.getColorFont(getName() + " load..."));
		if (!getDataFolder().exists())
			getDataFolder().mkdirs();
	}
}
