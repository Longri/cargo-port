package de.CB_SkinMaker;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import org.json.JSONException;
import org.json.JSONObject;

import CB_Core.FileUtil;

public class Settings
{

	private String mWorkpath = null;
	private String mDefaultSkin = null;
	private int Test = 10;

	public Settings()
	{

	}

	public void save() throws JSONException, FileNotFoundException
	{
		JSONObject json = new JSONObject();

		json.put("Test", Test);

		PrintStream out = null;
		try
		{
			out = new PrintStream(new FileOutputStream(mWorkpath + "/settings.lon"));
			out.print(json.toString());
		}
		finally
		{
			if (out != null) out.close();
		}
	}

	public void setWorkspace(String workpath)
	{
		mWorkpath = workpath;
	}

	public String getName()
	{
		return FileUtil.GetFileNameWithoutExtension(mWorkpath);
	}
}
