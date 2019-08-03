package xiaokai.tool;

import cn.nukkit.plugin.PluginBase;

/**
 * @author Winfxk
 */
public class Update {
	private PluginBase mis;
	private static final String Http = "WinfXK/Knickers";

	public void start(boolean isd) {
		String Url = "https://github.com/" + Http + "/releases";
		try {
			mis.getLogger().info("§6正在检查更新...");
			String aString = Tool.getHttps(Url, "GET", null);
			if (aString != null) {
				String v = aString.split("<li class=\"d-block mb-1\">          <a href=\"/" + Http + "/tree/")[1]
						.split("\" class=\"muted-link css-truncate\" title=\"")[0];
				if (!v.equals("v" + mis.getDescription().getVersion())) {
					String Title = aString.split(
							"<div class=\"f1 flex-auto min-width-0 text-normal\">          <a href=\"/WinfXK/Knickers/releases/tag/v1.1.1\">")[1]
									.split("</a>")[0];
					String Msg = Tool.delHtmlString(aString.split("</div>      <div class=\"markdown-body\">    <p>")[1]
							.split("</p>  </div>  <details    class=\"details-reset Details-element border-top pt-3 mt-4 mb-2 mb-md-4\"    open        >")[0]);
					String DownUrl = "https://github.com" + aString.split(
							"<div class=\"d-flex flex-justify-between py-1 py-md-2 Box-body px-2\">            <a href=\"")[1]
									.split("\" rel=\"nofollow\" class=\"d-flex flex-items-center\">")[0];
					mis.getLogger()
							.info("§6已发现新版本！\n\n" + Tool.getColorFont(Title) + "§6\n\n" + Msg + "\n\n下载地址：" + DownUrl);
				} else
					mis.getLogger().info("§6暂无更新");
			}
		} catch (Exception e) {
			mis.getLogger().error("更新出现错误！请检查或联系作者！" + e.getMessage());
		}
	}

	public Update(PluginBase mis) {
		this.mis = mis;
	}

	public static String getHttp() {
		return Http;
	}
}
