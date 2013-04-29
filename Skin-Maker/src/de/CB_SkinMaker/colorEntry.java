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

	public java.awt.Color getAwtColor()
	{
		int r = (int) (mColor.r * 255);
		int g = (int) (mColor.g * 255);
		int b = (int) (mColor.b * 255);
		int a = (int) (mColor.a * 255);

		java.awt.Color c = new java.awt.Color(r, g, b, a);

		return c;
	}

}
