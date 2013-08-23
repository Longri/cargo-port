package de.gdxgame.Views.Actions;

import CB_UI_Base.GL_UI.SpriteCacheBase;
import CB_UI_Base.GL_UI.SpriteCacheBase.IconName;
import CB_UI_Base.GL_UI.Main.Actions.CB_Action;

import com.badlogic.gdx.graphics.g2d.Sprite;

import de.gdxgame.Views.GameView;
import de.gdxgame.Views.ViewIDs;

public class Action_Fast_Play extends CB_Action
{
	public Action_Fast_Play()
	{
		super("PlayView", "", ViewIDs.PLAY_VIEW);
	}

	@Override
	public void Execute()
	{
		GameView.that.RunGameLoop(true);
	}

	@Override
	public boolean getEnabled()
	{
		return true;
	}

	@Override
	public Sprite getIcon()
	{
		return SpriteCacheBase.Icons.get(IconName.cacheList_7.ordinal());
	}

}
