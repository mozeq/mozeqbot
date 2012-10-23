package mozeq.irc.bot.plugins;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import mozeq.irc.bot.ConfigLoader;
import mozeq.irc.bot.Configuration;
import mozeq.irc.bot.IrcBotPlugin;
import mozeq.irc.bot.IrcMessage;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

class Ticket {
	int id = 0;
	Date time_created = null;
	Date time_changed = null;
	HashMap<String, String> attributes = null;

@SuppressWarnings("unchecked")
Ticket(Object[] o){
	   //[id, time_created, time_changed, attributes].
	   int i;
	   for(i=0; i < o.length; i++) {
		   if (i == 0) id = (Integer)o[i];
		   if (i == 1) time_created = (Date) o[i];
		   if (i == 2) time_changed = (Date) o[i];
		   if (i == 3) attributes = (HashMap<String, String>)o[i];
	   }
   }

   public String toString() {
	   return "id: " +id +"\ntime_created: " + time_created + "\nsummary: " + attributes.get("summary");
   }

   String getSummary() {
	   return attributes.get("summary");
   }

   public String getComponent() {
	   return attributes.get("component");
   }

}


class TracProxy {
	XmlRpcClient client = null;
	String projectURL = null;
	Configuration pluginConfig = null;

	TracProxy(String serverURL) {
		pluginConfig = ConfigLoader.getConfiguration("tracplugin.conf");
		String project = pluginConfig.get("project");
		this.projectURL = serverURL + (serverURL.endsWith("/") ? "" : "/") + project;
	}

	public void connect() throws MalformedURLException, XmlRpcException {

		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
    	String url = projectURL+"/login/xmlrpc";
		config.setServerURL(new URL(url));
		config.setBasicUserName(pluginConfig.get("username"));
		config.setBasicPassword(pluginConfig.get("password"));
	    client = new XmlRpcClient();
	    client.setConfig(config);

	}

	Ticket getTicket(int ticketID) throws XmlRpcException {
		Object[] params = { ticketID };
		//Returns [id, time_created, time_changed, attributes].
		Object[] result = (Object[])client.execute("ticket.get", params);

		return new Ticket(result);
	}

}


public class TracPlugin extends IrcBotPlugin {
	@Override
	public void init() {
		this.commands = new ArrayList<String>();

		commands.add(".trac#");
	}

	@Override
	public ArrayList<String> run(IrcMessage message, String command) {
		clearResponses();
		String[] params = message.body.split("#");
		if (params.length < 2) {
			System.err.println("Can't parse the ticket id from the message");
			return responses;
		}

		TracProxy trac = new TracProxy("https://fedorahosted.org");
		try {
			trac.connect();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlRpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Ticket t = null;
		try {
			t = trac.getTicket(Integer.parseInt(params[1]));
		} catch (XmlRpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		addResponse("trac#"+t.id +": ["+ t.getComponent() + "] " + t.getSummary() + " <" + trac.projectURL + "/ticket/" + t.id + ">");
		return responses;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}
