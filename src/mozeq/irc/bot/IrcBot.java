package mozeq.irc.bot;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class IrcBot {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String pluginDir = "bin/mozeq/irc/bot/plugins";
		PluginManager pm = new PluginManager(pluginDir);

		String configFile = "mozeqbot.conf";
		Configuration conf = ConfigLoader.getConfiguration(configFile);
		if (conf== null) {
			//System.err.println("Can't read: " + configFile);
			return;
		}

		//IrcConnection irc = new IrcConnection("irc.eng.brq.redhat.com", 6667, "#abrt", pm, false);
		//IrcConnection irc = new IrcConnection("irc.freenode.net", 6667, "#finishers", pm, true);

		String hostname = conf.get("hostname");
		int port = conf.getInt("port");
		String channel = conf.get("channel");
		boolean authenticate = conf.getBool("authenticate");
		int timeout = conf.getInt("reconnect_after");

		IrcConnection irc = new IrcConnection(hostname, port, channel, pm, authenticate, timeout);

		pm.loadPlugins();

		new Thread(irc).start();

		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

		String line = "";
		while(true) {
			try {
				line = input.readLine();
				if (line == null || line.equals("quit"))
						break;
				if (line.length() < 1)
					continue;

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(line);
			irc.sendMessage(line);
		}

		System.out.println("WTF, ending??");
	}

}
