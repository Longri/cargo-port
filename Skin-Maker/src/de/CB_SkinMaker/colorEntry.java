package de.CB_SkinMaker;

import com.badlogic.gdx.graphics.Color;

public class colorEntry
{
	private String mName;
	private Color mColor;

	public colorEntry(String Name, Color c)
	{
		mName = Name;
		mColor = c;
	}

	public String getName()
	{
		return mName;
	}

	public Color getColor()
	{
		return mColor;
	}
}
