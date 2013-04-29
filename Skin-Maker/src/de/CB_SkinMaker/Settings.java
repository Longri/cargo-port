package de.CB_SkinMaker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import CB_Core.FileUtil;

public class Settings
{

	private String mWorkpath = null;
	private String mDefaultSkin = null;
	private skinJson mSkinJsonDay, mSkinJsonNight;

	public Settings()
	{

	}

	public void save() throws JSONException, FileNotFoundException
	{
		JSONObject json = new JSONObject();

		json.put("mWorkpath", mWorkpath);
		json.put("mDefaultSkin", mDefaultSkin);

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

	public void saveSkinJasonToDefault() throws FileNotFoundException, JSONException
	{
		String genFolder = this.getWorkPath() + "/defaultskin";

		if (FileUtil.DirectoryExists(genFolder)) FileUtil.deleteFolder(new File(genFolder));

		// Create Folders
		new File(this.getWorkPath() + "/defaultskin/day/").mkdirs();
		new File(this.getWorkPath() + "/defaultskin/night/").mkdirs();

		this.getSkinJsonDay().Save(new File(this.getWorkPath() + "/defaultskin/day/skin.json"));
		this.getSkinJsonNight().Save(new File(this.getWorkPath() + "/defaultskin/night/skin.json"));
	}

	public void load(String WorkPath) throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(WorkPath + "/settings.lon"));
		StringBuilder sb = new StringBuilder();
		try
		{

			String line = br.readLine();

			while (line != null)
			{
				sb.append(line);
				sb.append("\n");
				line = br.readLine();
			}
		}
		finally
		{
			br.close();
		}
		JSONTokener tokener = new JSONTokener(sb.toString());
		try
		{
			JSONObject json = (JSONObject) tokener.nextValue();
			mWorkpath = json.getString("mWorkpath");
		}
		catch (Exception e)
		{
		}

		try
		{
			loadSkinJson();
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}

	}

	private void loadSkinJson() throws JSONException, IOException
	{
		this.setSkinJsonDay(new skinJson().Load(new File(this.getWorkPath() + "/defaultskin/day/skin.json")));
		this.setSkinJsonNight(new skinJson().Load(new File(this.getWorkPath() + "/defaultskin/night/skin.json")));
	}

	public void setWorkspace(String workpath)
	{
		mWorkpath = workpath;
	}

	public String getName()
	{
		return FileUtil.GetFileNameWithoutExtension(mWorkpath);
	}

	public String getWorkPath()
	{
		return mWorkpath;
	}

	public void setSkinJsonDay(skinJson sj)
	{
		mSkinJsonDay = sj;
	}

	public void setSkinJsonNight(skinJson sj)
	{
		mSkinJsonNight = sj;
	}

	public skinJson getSkinJsonDay()
	{
		return mSkinJsonDay;
	}

	public skinJson getSkinJsonNight()
	{
		return mSkinJsonNight;
	}
}
