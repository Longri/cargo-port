package de.gdxgame.Views.Actions;

import CB_Core.GL_UI.SpriteCache;
import CB_Core.GL_UI.SpriteCache.IconName;
import CB_Core.GL_UI.Main.Actions.CB_Action;

import com.badlogic.gdx.graphics.g2d.Sprite;

import de.gdxgame.Views.GameView;
import de.gdxgame.Views.ViewIDs;

public class Action_Debug_Play extends CB_Action
{
	public Action_Debug_Play()
	{
		super("PlayView", "", ViewIDs.PLAY_VIEW);
	}

	@Override
	public void Execute()
	{
		GameView.that.RunGameLoop();
	}

	@Override
	public boolean getEnabled()
	{
		return true;
	}

	@Override
	public Sprite getIcon()
	{
		return SpriteCache.Icons.get(IconName.cacheList_7.ordinal());
	}

}
