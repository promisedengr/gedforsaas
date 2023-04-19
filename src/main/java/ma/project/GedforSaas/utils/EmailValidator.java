package ma.project.GedforSaas.utils;

import java.util.regex.Pattern;

public class EmailValidator {

	public static boolean patternMatches(String emailAddress) {

		// Validation of Non-Latin or Unicode Characters Email
		String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
				+ "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

		return Pattern.compile(regexPattern).matcher(emailAddress).matches();
	}

}
