package mozeq.irc.bot;

public class IrcMessage {
	public String user = null;
	public String longSender = null;
	public String body = null;
	public String command = null;
	public String toUser = null;
	
	IrcMessage() {
	}
	
	public String toString() {
		return "u:'" + user + "' b:'" + body + "' c:'" + command + "' to:'" + toUser + "' ls:'" + longSender + "'";
	}
}
