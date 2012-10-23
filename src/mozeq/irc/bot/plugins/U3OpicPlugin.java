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

	@Override
	public ArrayList<String> run(IrcMessage message, String command) {
		clearResponses();
		try {
            Document doc = Jsoup.connect("http://u3opic.cz/").get();

            Elements menus = doc.select("div[id=menuAll]");
            int day = 0;
            for (Element lunchmenu : menus) {
            	System.out.println("Day: " + days[day]);
                Elements lines = lunchmenu.getAllElements();
                for (Element line: lines) {

                	if(line.attr("class").equals("dots"))
                		continue;
                	if(line.attr("class").equals("cena"))
                		continue;
                	if(line.nodeName().equals("p"))
                		continue;

                    System.out.println(line.text());
                }
                day++;
            }
        }
        catch (Exception e) {
        	System.out.println(e);
        }
		return responses;

	}

}
