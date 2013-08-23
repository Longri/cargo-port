package de.gdxgame.Views.Actions;

import CB_UI_Base.GL_UI.CB_View_Base;
import CB_UI_Base.GL_UI.Main.Actions.CB_Action_ShowView;
import de.gdxgame.GameSet;
import de.gdxgame.Views.GameView;
import de.gdxgame.Views.MainView;
import de.gdxgame.Views.ViewIDs;

public class Action_Show_GameView extends CB_Action_ShowView
{

	private final GameSet mLevel;

	public Action_Show_GameView(GameSet level)
	{
		super("showGameView", ViewIDs.PLAY_VIEW);
		this.mLevel = level;
	}

	@Override
	public void Execute()
	{
		if (MainView.gameView == null)
		{
			// Create Game View
			MainView.gameView = new GameView(tab.getContentRec());
		}

		// set Level to GameView ->Create GameFiled and fill
		MainView.gameView.setLevel(mLevel);

		if ((MainView.gameView != null) && (tab != null)) tab.ShowView(MainView.gameView);
	}

	@Override
	public CB_View_Base getView()
	{

		// set Level to GameView ->Create GameFiled and fill
		MainView.gameView.setLevel(mLevel);
		return MainView.gameView;
	}

	@Override
	public boolean getEnabled()
	{
		return true;
	}

}
