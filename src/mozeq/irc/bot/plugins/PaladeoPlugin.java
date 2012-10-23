package mozeq.irc.bot.plugins;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import mozeq.irc.bot.IrcBotPlugin;
import mozeq.irc.bot.IrcMessage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PaladeoPlugin extends IrcBotPlugin {

	String[] supportedCommands = {"paladeo"};
	String[] days = {"pondeli", "utery", "streda", "ctvrtek", "patek"};

	@Override
	public void init() {
		this.commands = new ArrayList<String>();

		for (String s: supportedCommands) {
			commands.add(s);
		}

	}

	@Override
	public ArrayList<String> run(IrcMessage message, String command) {

		clearResponses();

		try {
            Document doc = Jsoup.connect("http://www.paladeo.cz/menu").get();

            Elements menus = doc.select("ul");
            int day = 2;
            Calendar date = Calendar.getInstance();
        	int today = date.get(Calendar.DAY_OF_WEEK);

        	System.out.println(date.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, new Locale("cs")));

        	if (today > 6 || today < 2) {
        		addResponse("Sorry, no menu for day: '" + date.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, new Locale("cs")) +"'");
        		return responses;
        	}


            for (Element lunchmenu : menus) {
                Elements lines = lunchmenu.getElementsByTag("li");
                System.out.println(day + "=" + today);
                if (day == today) {
                	addResponse(date.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, new Locale("cs")));
                	for (Element line: lines) {
                		System.out.println(line.text());
                		addResponse(line.text());
                	}
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
