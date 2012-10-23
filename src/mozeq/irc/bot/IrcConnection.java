package mozeq.irc.bot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class IrcConnection implements Runnable {

	String synchroObj = "";
	Socket sock;
	BufferedWriter ostream = null;
	BufferedReader istream = null;

	String nick = "mozeqbot";
	String ident = "mozeqbot";
	String realName = "Finisher Bot";
	String pass = "kokotice";
	boolean auth = false;

	String host;
	int port;
	String channel;
	PluginManager messageHandler;

	/* PING :irc.the.net
	 * :irc.the.net 366 mozeqbot #finishers :End of NAMES list
	 * :jmoskovc!~jmoskovc@localhost PRIVMSG #finishers :hohey
	 * :jmoskovc!~jmoskovc@localhost PRIVMSG mozeqbot :hohey
	 * :jmoskovc!~jmoskovc@localhost PRIVMSG #finishers :ahoj certe
	 * :sendak.freenode.net 372 mozeqbot :- server!
	 *
	 */

	IrcMessage parseMessage(String buffer) {
		IrcMessage im = new IrcMessage();

		if (buffer.startsWith(":"))
		{
			int endExclam = buffer.indexOf('!');
			int end = buffer.indexOf(' ');


			if (end > -1 && endExclam > -1 && endExclam < end) {
				im.user = buffer.substring(1, endExclam);
				buffer = buffer.substring(endExclam+1);
			}
			else { //didn't find '!' so we guess it's from the server
			    end = buffer.indexOf(' ');
				im.longSender = buffer.substring(1, end);
				buffer = buffer.substring(end+1);
			}

		}
		else //command
		{
		    int cmdend = buffer.indexOf(' ');
			im.command = buffer.substring(0, cmdend);
			buffer = buffer.substring(cmdend+1);
		}

		if (im.user == null) {
			if (im.longSender == null) {
				int end = buffer.indexOf(' ');
				if (end > -1)
                    im.longSender = buffer.substring(1, end);
                else
                    im.longSender = buffer.substring(1);
			}
			else {
			    int end = buffer.indexOf(' ');
			    if (end > -1)
                    im.command = buffer.substring(0, end);
			}

			int end = buffer.indexOf(' ', im.longSender.length()+2);
			if (im.command == null && end > -1) {
                im.command = buffer.substring(im.longSender.length()+2, end);
            }

		}
		else {
		    int end = buffer.indexOf(' ');
			im.longSender = buffer.substring(0, end);
			buffer = buffer.substring(end+1);
			end = buffer.indexOf(' ');
			im.command = buffer.substring(0, end);
			buffer = buffer.substring(end+1);
		}

		if (im.command != null && im.command.equalsIgnoreCase("PRIVMSG")) {
			int end = buffer.indexOf(' ');
			im.toUser = buffer.substring(0, end);
			buffer = buffer.substring(end+1);
		}

		im.body = buffer.substring(buffer.indexOf(':')+1);
		return im;
	}

	IrcConnection (String host, int port, String channel, PluginManager messageHandler, boolean auth) {
		this.host = host;
		this.port = port;
		this.channel = channel;
		this.messageHandler = messageHandler;
		this.auth = auth;

	}

	public void connect() {

		String line;

		try {
			sock = new Socket(host, port);
			//sock.setSoTimeout(5000);
			ostream = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
			istream = new BufferedReader (new InputStreamReader(sock.getInputStream()));

			//"USER %s %s bla :%s\r\n" % (IDENT, HOST, REALNAME)

			if (auth == true)
				ostream.write("PASS "+ pass +"\r\n");
			ostream.write("NICK " + nick +"\r\n");
			String identification = "USER " + ident + " 8 * : Finisher's bot\r\n";
			ostream.write(identification);
			ostream.flush();

			// Read lines from the server until it tells us we have connected.
	        while ((line = istream.readLine( )) != null) {
	            if (line.indexOf("004") >= 0) {
	                // We are now logged in.
	                break;
	            }
	            else if (line.indexOf("433") >= 0) {
	                //System.out.println("Nickname is already in use.");
	                //return;
	            }
	        }

	        // Join the channel.
	        ostream.write("JOIN " + channel + "\r\n");
	        ostream.flush();
	        ostream.write(":"+ nick + " NOTICE " + channel + " :I'M BACK!\r\n");
	        ostream.flush();

		} catch (IOException e) {
			System.out.println("Can't connect to: '" + host + "' " + e);
			return;
		}

		try {
			// Keep reading lines from the server.
	        while ((line = istream.readLine( )) != null) {
	        	IrcMessage im = parseMessage(line);

	        	//Ignore our messages, otherwise => endless loop :)
	        	if (im.user != null && im.user.equalsIgnoreCase(nick))
	        		continue;
	        	ArrayList<String> responses = messageHandler.runPluginActions(im);
	        	for (String s: responses)
	        		sendReply(s, im);
	        	//System.out.println("Message processed");

	            if (im.command != null && im.command.equalsIgnoreCase("PING")) {
	            	//System.out.println("got ping, sending PONG");
	                // We must respond to PINGs to avoid being disconnected.
	                ostream.write("PONG " + line.substring(5) + "\r\n");
	                //ostream.write("PRIVMSG " + channel + " :I got pinged!\r\n");
	                ostream.flush();
	            }

	        }

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void sendMessage(String message) {
    	String response = ":" + nick +" PRIVMSG "+ channel +" :" + message + "\r\n";


   		try {
			ostream.write(response);
			ostream.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendMessage(String message, String toUser) throws IOException {
		synchronized(synchroObj) {
			String response = ":" + nick +" PRIVMSG " + toUser + " :" + message + "\r\n";

   			ostream.write(response);
   			ostream.flush();
		}
	}

	public void sendReply(String reply, IrcMessage message) {

		synchronized(synchroObj) {
			String response = null;
			System.out.println(">>" + reply);
			if (message.toUser.equals(nick))
				response = ":" + nick +" PRIVMSG " + message.user + " :" + reply + "\r\n";
			else
				response = ":" + nick +" PRIVMSG " + message.toUser + " :" + reply + "\r\n";

			if (response != null) {
				try {
					ostream.write(response);
					ostream.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	void joinChannel (String channel) {
		//System.out.println("Connecting to: " + channel);
	}

	@Override
	public void run() {
		connect();

	}

}
