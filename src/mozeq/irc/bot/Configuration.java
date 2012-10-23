package mozeq.irc.bot;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Configuration {

	HashMap<String, String> configOptions = null;
	Set<String> trueValues = new HashSet<String>(Arrays.asList(new String[]{"yes", "1"}));
	Set<String> falseValues = new HashSet<String>(Arrays.asList(new String[]{"no", "0"}));

	Configuration(){
		configOptions = new HashMap<String, String>();
	}

	public String get(String option) {
		String retval = configOptions.get(option);
		return retval != null ? retval : "";
	}

	public int getInt(String option) {
		String optVal = get(option);
		try {
			return Integer.parseInt(optVal);
		} catch (NumberFormatException ex){
			System.err.println("'" + optVal + "' is not a valid number, using 0 as default");
			return 0;
		}
	}

	public boolean getBool(String option) {
		String retval = get(option);
		if (retval == null)
			return false;

		if (trueValues.contains(option))
			return true;

		return false;
	}

	public void put(String option, String value) {
		configOptions.put(option, value);
	}
}
