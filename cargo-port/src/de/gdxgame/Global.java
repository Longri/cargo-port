package de.gdxgame;

/**
 * Enth�lt die Globalen Statichen Member
 * 
 * @author Longri
 */
public class Global extends CB_UI_Base.Global
{
	public static final int CurrentRevision = 1;
	public static final String CurrentVersion = "0.1.";
	public static final String VersionPrefix = "Test";

	public static final String br = System.getProperty("line.separator");
	public static final String fs = System.getProperty("file.separator");

	public static final String AboutMsg = "XXXXXXXXX (2013)" + br + "www.XXXXXXXXX.de" + br + "XXXXXXXXXXXX" + br + "XXXXXXXXXXXXXX";

	public static final String splashMsg = AboutMsg + br + br + br + "POWERED BY:";

	public static String getVersionString()
	{
		final String ret = "Version: " + CurrentVersion + String.valueOf(CurrentRevision) + "  "
				+ (VersionPrefix.equals("") ? "" : "(" + VersionPrefix + ")");
		return ret;
	}

	@Override
	protected String getVersionPrefix()
	{
		return VersionPrefix;
	}

}
