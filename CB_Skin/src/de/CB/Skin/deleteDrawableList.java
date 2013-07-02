package de.CB.Skin;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

/**
 * Abstract Class for holding of all Drawables
 * 
 * @author Longri
 */
public class deleteDrawableList
{

	private ArrayList<TextureAtlas> atlanten;
	private HashMap<String, Drawable> hashMap;

	/**
	 * Add Texture Atlas
	 * 
	 * @param atlas
	 */
	public void addAtlas(TextureAtlas atlas)
	{
		if (atlanten == null)
		{
			atlanten = new ArrayList<TextureAtlas>();
		}
		atlanten.add(atlas);
	}

	public Drawable getDrawable(String Name)
	{
		if (hashMap != null) return hashMap.get(Name);
		return null;
	}

	public void LoadSpriteDrawabe(String name)
	{
		chkHashMap();
		Sprite sp = getSprite(name);
		if (sp != null)
		{
			hashMap.put(name, new SpriteDrawable(sp));
		}
	}

	public void LoadPatchDrawabe(String name, float left, float right, float top, float bottom)
	{
		chkHashMap();
		NinePatch sp = getNinePatch(name);
		if (sp != null)
		{
			sp.setLeftWidth(left);
			sp.setRightWidth(right);
			sp.setTopHeight(top);
			sp.setBottomHeight(bottom);

			hashMap.put(name, new NinePatchDrawable(sp));
		}
	}

	private void chkHashMap()
	{
		if (hashMap == null) hashMap = new HashMap<String, Drawable>();
	}

	private Sprite getSprite(String name)
	{
		for (TextureAtlas atlas : atlanten)
		{
			Sprite sp = atlas.createSprite(name);
			if (sp != null) return sp;
		}

		return null;
	}

	private NinePatch getNinePatch(String name)
	{
		for (TextureAtlas atlas : atlanten)
		{
			NinePatch sp = atlas.createPatch(name);
			if (sp != null) return sp;
		}

		return null;
	}
}
