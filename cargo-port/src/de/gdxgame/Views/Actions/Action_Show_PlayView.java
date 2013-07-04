package de.gdxgame.Views.Actions;

import CB_Core.GL_UI.CB_View_Base;
import CB_Core.GL_UI.GL_View_Base;
import CB_Core.GL_UI.GL_View_Base.OnClickListener;
import CB_Core.GL_UI.Main.Actions.CB_Action_ShowView;
import CB_Core.GL_UI.Menu.Menu;
import CB_Core.GL_UI.Menu.MenuID;
import CB_Core.GL_UI.Menu.MenuItem;

import com.badlogic.gdx.graphics.g2d.Sprite;

import de.gdxgame.Views.GameView;
import de.gdxgame.Views.MainView;
import de.gdxgame.Views.PlayView;
import de.gdxgame.Views.ViewIDs;

public class Action_Show_PlayView extends CB_Action_ShowView
{

	public Action_Show_PlayView()
	{
		super("PlayView", "", ViewIDs.PLAY_VIEW);
	}

	@Override
	public void Execute()
	{
		if ((MainView.mPlayView == null) && (tabMainView != null) && (tab != null)) MainView.mPlayView = new PlayView(tab.getContentRec());

		if ((MainView.mPlayView != null) && (tab != null)) tab.ShowView(MainView.mPlayView);
	}

	@Override
	public CB_View_Base getView()
	{
		return MainView.mPlayView;
	}

	@Override
	public boolean getEnabled()
	{
		return true;
	}

	@Override
	public Sprite getIcon()
	{
		return null;// SpriteCache.Icons.get(IconName.cacheList_7.ordinal());
	}

	@Override
	public boolean HasContextMenu()
	{
		return false;
	}

	@Override
	public Menu getContextMenu()
	{
		Menu cm = new Menu("PlayViewContextMenu");
		cm.addItemClickListner(new OnClickListener()
		{
			@Override
			public boolean onClick(GL_View_Base v, int x, int y, int pointer, int button)
			{
				switch (((MenuItem) v).getMenuItemId())
				{
				case MenuID.MI_RESORT:
					GameView.that.RunGameLoop();
					return true;
				}
				return false;
			}
		});

		MenuItem mi;
		cm.addItem(MenuID.MI_RESORT, "ResortList");

		return cm;
	}

	public void setName(String newName)
	{
		this.name = newName;
	}

	public void setNameExtention(String newExtention)
	{
		this.nameExtention = newExtention;
	}
}
