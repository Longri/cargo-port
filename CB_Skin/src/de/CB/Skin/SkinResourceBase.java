package de.CB.Skin;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class SkinResourceBase
{
	protected Drawable mDrawable;
	protected String mName;

	public SkinResourceBase(String name)
	{
		this.mName = name;
	}

	public Drawable get()
	{
		return mDrawable;
	}
}
