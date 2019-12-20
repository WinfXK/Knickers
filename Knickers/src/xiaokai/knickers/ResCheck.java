package xiaokai.knickers;

import xiaokai.knickers.tool.Tool;

import java.io.File;
import java.io.IOException;

import cn.nukkit.Server;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.Utils;

/**
 * @author Winfxk
 */
public class ResCheck {
	private Activate ac;

	public ResCheck(Activate activate) {
		this.ac = activate;

	}

	public void start() {
		File file = Message.getFile();
		if (!file.exists()) {
			String lang = Tool.getLanguages().get(Server.getInstance().getLanguage().getName());
			if (lang != null && getClass().getResource("/language/" + lang + ".yml") != null) {
				try {
					ac.getKnickers().getLogger().info("Writing to the default language:" + lang);
					Utils.writeFile(Message.getFile(),
							Utils.readFile(getClass().getResourceAsStream("/language/" + lang + ".yml")));
				} catch (IOException e) {
					e.printStackTrace();
					ac.getKnickers().getLogger().error("§4The default language could not be initialized！");
					try {
						Utils.writeFile(Message.getFile(),
								Utils.readFile(getClass().getResourceAsStream("/resources/Message.yml")));
					} catch (IOException e1) {
						e1.printStackTrace();
						ac.getKnickers().getLogger().error("§4The default language could not be initialized！");
						ac.getKnickers().setEnabled(false);
						return;
					}
				}
			} else
				try {
					Utils.writeFile(Message.getFile(),
							Utils.readFile(getClass().getResourceAsStream("/resources/Message.yml")));
				} catch (IOException e1) {
					e1.printStackTrace();
					ac.getKnickers().getLogger().error("§4The default language could not be initialized！");
					ac.getKnickers().setEnabled(false);
					return;
				}
		}
		file = new File(ac.getKnickers().getDataFolder(), Activate.ConfigFileName);
		if (!file.exists()) {
			try {
				Utils.writeFile(file,
						Utils.readFile(getClass().getResourceAsStream("/resources/" + Activate.ConfigFileName)));
			} catch (IOException e) {
				e.printStackTrace();
				ac.getKnickers().getLogger().error("§4Error initializing default configuration!");
				ac.getKnickers().setEnabled(false);
				return;
			}
		}
		ac.config = new Config(new File(ac.getKnickers().getDataFolder(), Activate.ConfigFileName), 2);
		ac.message = new Message(ac);
		
	}
}
