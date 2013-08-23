package de.gdxgame;

import CB_Utils.Settings.SettingBool;
import CB_Utils.Settings.SettingCategory;
import CB_Utils.Settings.SettingStoreType;
import CB_Utils.Settings.SettingsList;

public interface Settings extends CB_UI_Base.settings.CB_UI_Base_Settings
{

	public static final SettingBool test = (SettingBool) SettingsList.addSetting(new SettingBool("test", SettingCategory.Internal, NEVER,
			false, SettingStoreType.Global));

}
