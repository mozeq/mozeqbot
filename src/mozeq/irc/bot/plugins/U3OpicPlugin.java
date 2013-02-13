package mozeq.irc.bot.plugins;

import java.util.ArrayList;

import mozeq.irc.bot.IrcBotPlugin;
import mozeq.irc.bot.IrcMessage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class U3OpicPlugin extends IrcBotPlugin {

	String[] supportedCommands = {"opice", "u3opic"};
	String[] days = {"pondeli", "utery", "streda", "ctvrtek", "patek"};

	@Override
	public void init() {
		// TODO Auto-generated method stub
		this.commands = new ArrayList<String>();

		for (String s: supportedCommands) {
			commands.add(s);
		}

	}


	/*
	 * (non-Javadoc)
	 * @see mozeq.irc.bot.IrcBotPlugin#run(mozeq.irc.bot.IrcMessage, java.lang.String)
	 * <div id="menu">
						<h3>Dnešní menu 26. 10. 2012</h3>

			<p>Polévka:

				<span>

				Pórková polévka s vejcem
				</span>

			</p>

			<p class="dots">...........................................................................................................................................................................</p>

			<p>menu č.1:

				<span>

				Čevapčiči z mletého hovězího masa s cibulí a hořčicí, vařené brambory
				</span>

				<span class="cena">

					75,-

				</span>

			</p>

			<p class="dots">...........................................................................................................................................................................</p>

	 */

	@Override
	public ArrayList<String> run(IrcMessage message, String command) {
		clearResponses();
		try {
			Document doc = Jsoup.connect("http://u3opic.cz/").get();

			Elements menuElemenents = doc.select("div[id=menu]").get(0).getAllElements();
			for (Element line: menuElemenents) {

				if (line.tagName().equals("div") && line.attr("id").equals("menuAll"))
					break;

				//System.out.println(line.tagName());
				if(line.attr("class").equals("dots"))
					continue;
				if(line.attr("class").equals("cena"))
					continue;
				if(line.nodeName().equals("p")) {
					Elements els = line.getElementsByTag("span");
					String resp = "";
					for (Element e: els) {
						resp += " " + e.text();
					}
					if (resp.length() > 0)
						addResponse(resp.trim());
				}

			}
		}
		catch (Exception e) {
			System.out.println(e);
		}
		return responses;

	}

}
