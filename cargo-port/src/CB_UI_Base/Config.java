package CB_UI_Base;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import CB_UI_Base.settings.CB_UI_Base_Settings;
import CB_Utils.Config_Core;
import CB_Utils.Log.Logger;
import de.gdxgame.Settings;

public class Config extends Config_Core implements Settings
{

	public Config(String workPath)
	{
		super(workPath);
		settings = new SettingsClass();
	}

	final public SettingsClass settings;

	public static String ConfigName = "";

	public static void Initialize(String workPath, String configName)
	{
		WorkPath = workPath;
		ConfigName = configName;

	}

	public static String GetString(String key)
	{
		checkInitialization();

		String value = keyLookup.get(key);
		if (value == null) return "";
		else
			return value;
	}

	public static double GetDouble(String key)
	{
		checkInitialization();

		String value = keyLookup.get(key);
		if (value == null) return 0;
		else
			return Double.parseDouble(value);
	}

	public static float GetFloat(String key)
	{
		checkInitialization();

		String value = keyLookup.get(key);
		if (value == null) return 0;
		else
			return Float.parseFloat(value);
	}

	public static Boolean GetBool(String key)
	{
		checkInitialization();

		String value = keyLookup.get(key);
		if (value == null) return false;
		else
			return Boolean.parseBoolean(value);
	}

	public static int GetInt(String key)
	{
		checkInitialization();

		String value = keyLookup.get(key);
		if (value == null)
		{
			return -1;
		}
		else
		{
			try
			{
				return Integer.parseInt(value);
			}
			catch (Exception e)
			{
			}
			return -1;
		}
	}

	public static void changeDayNight()
	{
		Boolean value = CB_UI_Base_Settings.nightMode.getValue();
		value = !value;
		CB_UI_Base_Settings.nightMode.setValue(value);
		Config.AcceptChanges();
	}

	static HashMap<String, String> keyLookup = null;

	static boolean initialized = false;

	public static void readConfigFile()
	{
		initialized = false;
		checkInitialization();
	}

	static void checkInitialization()
	{
		if (initialized) return;

		initialized = true;

		keyLookup = new HashMap<String, String>();

		BufferedReader Filereader;

		try
		{
			Filereader = new BufferedReader(new FileReader(ConfigName));
			String line;

			while ((line = Filereader.readLine()) != null)
			{
				int idx = line.indexOf('=');
				if (idx < 0)
				{
					continue;
				}

				String key = line.substring(0, idx);
				String value = line.substring(idx + 1)/* .replace("//","/" ) */;
				keyLookup.put(key, value);
			}

			Filereader.close();
		}
		catch (IOException e)
		{
			Logger.Error("ReadConfig", "Error when accessing cachebox.config!", e);
			e.printStackTrace();
		}

		validateDefaultConfigFile();
	}

	public static void validateDefaultConfigFile()
	{
		validateSetting("LanguagePath", WorkPath + "/data/lang");
		validateSetting("Sel_LanguagePath", WorkPath + "/data/lang/en.lan");
		validateSetting("DatabasePath", WorkPath + "/cachebox.db3");
		validateSetting("TileCacheFolder", WorkPath + "/cache");
		validateSetting("PocketQueryFolder", WorkPath + "/PocketQuery");
		validateSetting("DescriptionImageFolder", WorkPath + "/repository/images");
		validateSetting("MapPackFolder", WorkPath + "/repository/maps");
		validateSetting("SpoilerFolder", WorkPath + "/repository/spoilers");
		validateSetting("UserImageFolder", WorkPath + "/User/Media");
		validateSetting("TrackFolder", WorkPath + "/User/Tracks");

		validateSetting("FieldNotesGarminPath", WorkPath + "/User/geocache_visits.txt");

		validateSetting("SaveFieldNotesHtml", "true");

		validateSetting("Proxy", "");
		validateSetting("ProxyPort", "");
		validateSetting("DopMin", "0.2");
		validateSetting("DopWidth", "1");
		validateSetting("OsmDpiAwareRendering", "true");
		validateSetting("LogMaxMonthAge", "99999");
		validateSetting("LogMinCount", "99999");
		validateSetting("MapInitLatitude", "-1000");
		validateSetting("MapInitLongitude", "-1000");
		validateSetting("AllowInternetAccess", "true");
		validateSetting("AllowRouteInternet", "true");
		validateSetting("ImportGpx", "true");
		validateSetting("CacheMapData", "false");
		validateSetting("CacheImageData", "false");
		validateSetting("OsmMinLevel", "8");
		validateSetting("OsmMaxImportLevel", "16");
		validateSetting("OsmMaxLevel", "17");
		validateSetting("OsmCoverage", "1000");
		validateSetting("SuppressPowerSaving", "true");
		validateSetting("PlaySounds", "true");
		validateSetting("PopSkipOutdatedGpx", "true");
		validateSetting("MapHideMyFinds", "false");
		validateSetting("MapShowRating", "true");
		validateSetting("MapShowDT", "true");
		validateSetting("MapShowTitles", "true");
		validateSetting("ShowKeypad", "true");
		validateSetting("FoundOffset", "0");
		validateSetting("ImportLayerOsm", "true");
		validateSetting("CurrentMapLayer", "Mapnik");
		validateSetting("AutoUpdate", "http://www.getcachebox.net/latest-stable");
		validateSetting("NavigationProvider", "http://openrouteservice.org/php/OpenLSRS_DetermineRoute.php");
		validateSetting("TrackRecorderStartup", "false");
		validateSetting("MapShowCompass", "true");
		validateSetting("FoundTemplate", "<br>###finds##, ##time##, Found it with DroidCachebox!");
		validateSetting("DNFTemplate", "<br>##time##. Logged it with DroidCachebox!");
		validateSetting("NeedsMaintenanceTemplate", "Logged it with DroidCachebox!");
		validateSetting("AddNoteTemplate", "Logged it with DroidCachebox!");
		validateSetting("ResortRepaint", "false");
		validateSetting("TrackDistance", "3");
		validateSetting("MapMaxCachesLabel", "12");
		validateSetting("MapMaxCachesDisplay_config", "10000");
		validateSetting("SoundApproachDistance", "50");
		validateSetting("mapMaxCachesDisplayLarge_config", "75");
		// validateSetting("Filter", PresetListView.presets[0].toString());
		validateSetting("ZoomCross", "16");
		// validateSetting("TomTomExportFolder", Global.AppPath + "/user");
		validateSetting("GCAutoSyncCachesFound", "true");
		validateSetting("GCAdditionalImageDownload", "false");
		validateSetting("GCRequestDelay", "10");

		validateSetting("MultiDBAsk", "true");
		validateSetting("MultiDBAutoStartTime", "0");
		validateSetting("FieldnotesUploadAll", "false");

		validateSetting("SpoilersDescriptionTags", "");
		validateSetting("AutoResort", "false");

		validateSetting("HtcCompass", "false");
		validateSetting("HtcLevel", "30");
		validateSetting("SmoothScrolling", "none");

		validateSetting("DebugShowPanel", "false");
		validateSetting("DebugMemory", "false");
		validateSetting("DebugShowMsg", "false");

		validateSetting("LockM", "1");
		validateSetting("LockSec", "0");
		validateSetting("AllowLandscape", "false");
		validateSetting("MoveMapCenterWithSpeed", "false");
		validateSetting("MoveMapCenterMaxSpeed", "20");
		validateSetting("lastZoomLevel", "14");
		validateSetting("quickButtonShow", "true");
		validateSetting("quickButtonList", "5,0,1,3,2");
		validateSetting("PremiumMember", "false");
		validateSetting("SearchWithoutFounds", "true");
		validateSetting("SearchWithoutOwns", "true");

		// api search settings
		validateSetting("SearchWithoutFounds", "true");
		validateSetting("SearchWithoutOwns", "true");
		validateSetting("SearchOnlyAvible", "true");

		// validateSetting("OtherRepositoriesFolder", Global.AppPath +
		// "/Repositories");

		AcceptChanges();
	}

	private static void validateSetting(String key, String value)
	{
		String Lookupvalue = keyLookup.get(key);
		if (Lookupvalue == null) keyLookup.put(key, value);
	}

	public static void Set(String key, String value)
	{
		checkInitialization();
		keyLookup.put(key, value);
	}

	@Override
	protected void acceptChanges()
	{
		// TODO settings.WriteToDB();
	}

	public static String GetStringEncrypted(String key)
	{
		String s;
		boolean convert = false;
		if (ExistsKey(key + "Enc"))
		{
			s = GetString(key + "Enc");
			if (s != "")
			{
				// encrypted Key is found -> remove the old non encrypted
				if (ExistsKey(key))
				{
					keyLookup.remove(key);
					AcceptChanges();
				}
				s = decrypt(s);
			}
		}
		else
		{
			// no encrypted Key is found -> search for non encrypted
			s = GetString(key);
			if (s != "")
			{
				// remove the old non encrypted and insert a new encrypted
				keyLookup.remove(key);
				convert = true;
			}
		}

		if (convert)
		{
			SetEncrypted(key, s);
			AcceptChanges();
		}
		return s;
	}

	public static boolean ExistsKey(String key)
	{
		checkInitialization();
		return keyLookup.containsKey(key);
	}

	/*
	 * Nachfolgend die Getter von Einstellungen, welche sehr h�ufig abgerufen werden. Diese Einstellungen werden zwischen gespeichert und
	 * erst bei einer �nderung aktualisiert. Diese erspart das Parsen von Werten
	 */

	public static void SetEncrypted(String key, String value)
	{
		String encrypted = encrypt(value);
		if (ExistsKey(key)) keyLookup.remove(key); // remove non decrypted key
		// if exists
		Set(key + "Enc", encrypted);
	}

	// Read the encrypted AccessToken from the config and check wheter it is
	// correct for Andorid CB

	// /**
	// * Read the encrypted AccessToken from the config and check wheter it is correct for Andorid CB
	// *
	// * @return
	// */
	// public static String GetAccessToken()
	// {
	// return GetAccessToken(false);
	// }
	//
	// /**
	// * Read the encrypted AccessToken from the config and check wheter it is correct for Andorid CB </br> If Url_Codiert==true so the
	// * API-Key is URL-Codiert </br> Like replase '/' with '%2F'</br></br> This is essential for PQ-List
	// *
	// * @param boolean Url_Codiert
	// * @return
	// */
	// public static String GetAccessToken(boolean Url_Codiert)
	// {
	// String act = "";
	// if (CB_Core_Settings.StagingAPI.getValue())
	// {
	// act = CB_Core_Settings.GcAPIStaging.getValue();
	// }
	// else
	// {
	// act = CB_Core_Settings.GcAPI.getValue();
	// }
	//
	// // Pr�fen, ob das AccessToken f�r ACB ist!!!
	// if (!(act.startsWith("A"))) return "";
	// String result = act.substring(1, act.length());
	//
	// // URL encoder
	// if (Url_Codiert)
	// {
	// result = result.replace("/", "%2F");
	// result = result.replace("\\", "%5C");
	// result = result.replace("+", "%2B");
	// result = result.replace("=", "%3D");
	// }
	//
	// return result;
	// }

}
