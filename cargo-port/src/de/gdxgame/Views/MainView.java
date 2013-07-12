package de.gdxgame.Views;

import CB_Core.GL_UI.SpriteCache;
import CB_Core.GL_UI.Main.CB_ActionButton;
import CB_Core.GL_UI.Main.CB_ActionButton.GestureDirection;
import CB_Core.GL_UI.Main.CB_Button;
import CB_Core.GL_UI.Main.CB_ButtonList;
import CB_Core.GL_UI.Main.CB_TabView;
import CB_Core.GL_UI.Main.TabMainView;
import CB_Core.GL_UI.Main.Actions.CB_Action_ShowQuit;
import CB_Core.Math.CB_RectF;
import CB_Core.Math.GL_UISizes;
import de.gdxgame.Views.Actions.Action_Fast_Play;
import de.gdxgame.Views.Actions.Action_Load_Model;
import de.gdxgame.Views.Actions.Action_Play;
import de.gdxgame.Views.Actions.Action_ShowQuit;
import de.gdxgame.Views.Actions.Action_Show_PlayView;

/**
 * @author Longri
 */
public class MainView extends TabMainView
{

	public static MainView that;
	public static GameView gameView;
	public static CB_TabView TAB;

	// ######## Button Actions ###########
	private Action_Show_PlayView actionShowPlayView = new Action_Show_PlayView();
	public static CB_Action_ShowQuit actionClose = new Action_ShowQuit();
	private Action_Play actionPlay = new Action_Play();
	private Action_Fast_Play actionFastPlay = new Action_Fast_Play();
	private Action_Load_Model actionLoadModel = new Action_Load_Model();

	// ######## Views ###########
	public static PlayView mPlayView;

	public static float Width()
	{
		if (that != null) return that.getWidth();
		return 0;
	}

	public static float Height()
	{
		if (that != null) return that.getHeight();
		return 0;
	}

	public MainView(float X, float Y, float Width, float Height, String Name)
	{
		super(X, Y, Width, Height, Name);
		that = this;
	}

	@Override
	public void Initial()
	{
		// mit fünf Buttons
		CB_RectF btnRec = new CB_RectF(0, 0, GL_UISizes.BottomButtonHeight, GL_UISizes.BottomButtonHeight);

		CB_RectF rec = this.copy();
		rec.setWidth(GL_UISizes.UI_Left.getWidth());

		rec.setHeight(this.height);
		rec.setPos(0, 0);

		TAB = new CB_TabView(rec, "Phone Tab");

		CB_Button btn1 = new CB_Button(btnRec, "Button1", SpriteCache.CacheList);
		CB_Button btn2 = new CB_Button(btnRec, "Button2", SpriteCache.Cache);
		CB_Button btn3 = new CB_Button(btnRec, "Button3", SpriteCache.Nav);
		CB_Button btn4 = new CB_Button(btnRec, "Button4", SpriteCache.Tool);
		CB_Button btn5 = new CB_Button(btnRec, "Button5", SpriteCache.Misc);

		CB_ButtonList btnList = new CB_ButtonList();
		btnList.addButton(btn1);
		btnList.addButton(btn2);
		btnList.addButton(btn3);
		btnList.addButton(btn4);
		btnList.addButton(btn5);

		TAB.addButtonList(btnList);

		this.addChild(TAB);

		// Tab den entsprechneden Actions zuweisen
		actionShowPlayView.setTab(this, TAB);
		// Actions den Buttons zuweisen

		btn1.addAction(new CB_ActionButton(actionShowPlayView, true, GestureDirection.Up));

		btn2.addAction(new CB_ActionButton(actionPlay, true));
		btn3.addAction(new CB_ActionButton(actionFastPlay, true));

		btn4.addAction(new CB_ActionButton(actionLoadModel, true));
		// btn5.addAction(new CB_ActionButton(actionShowSettings, false, GestureDirection.Left));
		// btn5.addAction(new CB_ActionButton(actionDayNight, false));
		// btn5.addAction(new CB_ActionButton(actionScreenLock, false));
		btn5.addAction(new CB_ActionButton(actionClose, false, GestureDirection.Down));

	}

}
