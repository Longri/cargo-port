package de.CB_SkinMaker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.badlogic.gdx.graphics.Color;

public class skinJson
{
	ArrayList<colorEntry> colors;

	public skinJson()
	{
		colors = new ArrayList<colorEntry>();
	}

	public void Load(File f)
	{

	}

	public void Save(File f) throws JSONException, FileNotFoundException
	{
		JSONObject json = new JSONObject();

		JSONObject colorArray = new JSONObject();

		for (colorEntry ce : colors)
		{
			colorArray.put(ce.getName(), getValueArrayFromColor(ce.getColor()));
		}

		json.put("com.badlogic.gdx.graphics.Color", colorArray);

		PrintStream out = null;
		try
		{
			out = new PrintStream(new FileOutputStream(f.getAbsolutePath()));
			out.print(json.toString());
		}
		finally
		{
			if (out != null) out.close();
		}
	}

	private JSONObject getValueArrayFromColor(Color c) throws JSONException
	{
		JSONObject json = new JSONObject();
		json.put("r", (double) c.r);
		json.put("g", (double) c.g);
		json.put("b", (double) c.b);
		json.put("a", (double) c.a);

		return json;
	}

	public void addDefaultColorsDay()
	{
		colors.add(new colorEntry("font-color", new Color(0, 0, 0, 1)));
		colors.add(new colorEntry("font-color-disable", new Color(0.2f, 0.1f, 0.1f, 0.5f)));
		colors.add(new colorEntry("font-color-highlight", new Color(0, 1, 0, 1)));
		colors.add(new colorEntry("font-color-link", new Color(0, 0, 1, 1)));
		colors.add(new colorEntry("background", new Color(1, 1, 1, 1)));
	}

	public void addDefaultColorsNight()
	{
		colors.add(new colorEntry("font-color", new Color(0.7f, 0, 0, 1)));
		colors.add(new colorEntry("font-color-disable", new Color(0.4f, 0.13f, 0.13f, 0.1f)));
		colors.add(new colorEntry("font-color-highlight", new Color(0, 1, 0, 1)));
		colors.add(new colorEntry("font-color-link", new Color(0, 0, 1, 1)));
		colors.add(new colorEntry("background", new Color(0, 0, 0, 1)));
	}
}
