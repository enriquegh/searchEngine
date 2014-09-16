import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Parses command-line arguments into flag/value pairs for easy access.
 */
public class ArgumentParser {

	/** Stores arguments in a map, where the key is a flag. */
	private final Map<String, String> argumentMap;

	/**
	 * Initializes an empty argument map. The method {@link #parseArguments(String[])}
	 * should eventually be called to populate the map.
	 */
	public ArgumentParser() {
		// TODO
	    argumentMap = new HashMap<String, String>();
	}

	/**
	 * Initializes the argument map with the provided command-line arguments.
	 * Uses {@link #parseArguments(String[])} to populate the map.
	 *
	 * @param args - command-line arguments
	 * @see #parseArguments(String[])
	 */
	public ArgumentParser(String[] args) {
		this();
		parseArguments(args);
	}

	/**
	 * Iterates through the array of command-line arguments. If a flag is
	 * found, will attempt to see if it is followed by a value. If so, the
	 * flag/value pair is added to the map. If the flag is followed by
	 * another flag, then the first flag is added to the map with a null
	 * value.
	 *
	 * @param args - command-line arguments
	 *
	 * @see #isFlag(String)
	 * @see #isValue(String)
	 */
	public void parseArguments(String[] args) {
		// TODO
		for (int i = 0; i < args.length; i++){
			if (isFlag(args[i])){
				if ((i+1 < args.length) &&  isValue(args[i+1]) ){
					argumentMap.put(args[i], args[i+1]);
				}
				else{
					argumentMap.put(args[i], null);
				}
			}
			else{
				
			}
		}

	}

	/**
	 * Tests if the provided argument is a flag by checking that it starts with
	 * a "-" dash symbol, and is followed by at least one non-whitespace
	 * character. For example, "-a" and "-1" are valid flags, but "-" and "- "
	 * are not valid flags.
	 *
	 * @param arg - command-line argument
	 * @return true if the argument is a flag
	 */
	public static boolean isFlag(String arg) {
		// TODO
		char[] argArray = arg.toCharArray();
		
		if (argArray.length >= 1){
			if (argArray[0] == '-'){		
				if ((argArray.length == 1) || (argArray[1] == ' ')){
					return false;
				}
				else{
					return true;
				}
			}
			
			else{
				return false;
			}
				
		}
		
		
		else {
			return false;
		}
	}

	/**
	 * Tests if the provided argument is a value by checking that it does not
	 * start with a "-" dash symbol, and contains at least one non-whitespace
	 * character. For example, "a" and "1" are valid values, but "-" and " "
	 * are not valid values.
	 *
	 * @param arg - command-line argument
	 * @return true if the argument is a value
	 */
	
	public static boolean isValue(String arg) {
		// TODO
		
		char[] argArray = arg.toCharArray();
		if (argArray.length >= 1){

			if ( (argArray[0] != '-') ){
				if (arg.equals(" ")){
					return false;
				}
				else{
					return true;
				}
			}
			else{
				return false;
			}
				
		}
		
		
		else {
			return false;
		}

	}

	/**
	 * Returns the number of flags stored.
	 *
	 * @return number of flags
	 */
	public int numFlags() {
		// TODO
		return argumentMap.size();
	}

	/**
	 * Tests if the provided flag is stored in the map.
	 *
	 * @param flag - flag to check
	 * @return true if the flag exists and false otherwise
	 */
	public boolean hasFlag(String flag) {
		// TODO
		if (argumentMap.containsKey(flag)){
			return true;

		}
		else{
			return false;
		}
	}

	/**
	 * Tests if the provided flag has a value.
	 *
	 * @param flag - flag to check
	 * @return true if the flag exists and has a non-null value
	 */
	public boolean hasValue(String flag) {
		// TODO
		if (argumentMap.containsKey(flag)){
			if (argumentMap.get(flag) != null){
				return true;
			}
			else{
				return false;
			}
		}
		else{
			return false;
		}
	}

	/**
	 * Returns the value of a flag if it exists, and null otherwise.
	 *
	 * @param flag - flag to check
	 * @return value of flag or null if flag does not exist or has no value
	 */
	public String getValue(String flag) {
		// TODO

		if (hasFlag(flag)){
			return argumentMap.get(flag);
		}
		else{
			return null;
		}

	}

	@Override
	public String toString() {
		return argumentMap.toString();
	}
}
