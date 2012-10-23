package mozeq.irc.bot.plugins;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import mozeq.irc.bot.IrcBotPlugin;
import mozeq.irc.bot.IrcMessage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LunchTimePlugin extends IrcBotPlugin {

	String[] supportedCommands = {"opice", "paladeo", "kanas", "čo obed", "co obed"};

	Hashtable<String, String> restaurants;
	Hashtable<String, List<String>> menus = null;
	Date lastUpdate = null;

	@Override
	public void init() {
		this.commands = new ArrayList<String>();

		restaurants = new Hashtable<String, String>();
		restaurants.put("paladeo", "http://m.lunchtime.cz/cs/paladeo-restaurante/dailyMenu");
		restaurants.put("opice", "http://m.lunchtime.cz/cs/u-3-opic/dailyMenu");
		restaurants.put("kanas", "http://m.lunchtime.cz/cs/kanas/dailyMenu");


		for (String s: supportedCommands) {
			commands.add(s);
		}

		menus = new Hashtable<String, List<String>>();

	}

	List<String> getMenu(String restaurant) {

		/* cache, coz lunchtime doesn't like repeated queries :-/ */
		if (menus != null && menus.containsKey(restaurant))
			return menus.get(restaurant);

		List<String> menu = new ArrayList<String>();

		try {
            Document doc = Jsoup.connect(restaurants.get(restaurant)).get();

            Elements menus = doc.select("table[class=meal-menu]");
            for (Element lunchmenu : menus) {
                Elements lines = lunchmenu.getElementsByTag("th");
                for (Element line: lines) {
                	menu.add(line.text());
                    System.out.println(line.text());
                }
            }
        }
        catch (Exception e) {
        	menu = null;
            System.out.println("Can't load url: " + restaurants.get(restaurant) + ":" + e);
        }

		if (menu != null)
			menus.put(restaurant, menu);
		return menu;
	}

	Hashtable<String, List<String>> getAllMenus() {

		Hashtable<String, List<String>> menus = new Hashtable<String, List<String>>();
		for (String s : supportedCommands) {
			if (s.equals("co obed") || s.equals("čo obed"))
				continue;

			menus.put(s, getMenu(s));
		}
		return menus;
	}

	@Override
	public ArrayList<String> run(IrcMessage message, String command) {
		clearResponses();

		String[] wantedRest = null;
		if (command.equals("co obed") || command.equals("čo obed")) {
			wantedRest = supportedCommands.clone();
			wantedRest[wantedRest.length - 1] = null;
			wantedRest[wantedRest.length - 2] = null;
		}
		else {
			if (!message.body.contains("mozeqbot"))
				return responses;
			wantedRest = new String[1];
			wantedRest[0] = command;
		}


		for (String s : wantedRest) {
			if (s == null) {
				continue;
			}

			List<String> menu = getMenu(s);

			if (menu == null) {
				addResponse("Can't parse menu for: '" + s + "'");
				continue;
			}


			addResponse("==" + s + "==");
			for (String entry : menu) {
				addResponse(entry);
			}
			try {
				Thread.sleep(1000);//give IRC a while to process the text, otherwise some of it get lost..
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return responses;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
