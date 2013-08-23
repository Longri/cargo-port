package de.gdxgame.Views;

import CB_UI_Base.GL_UI.Main.CB_ActionButton;
import CB_UI_Base.GL_UI.Main.CB_ActionButton.GestureDirection;
import CB_UI_Base.GL_UI.Main.CB_Button;
import CB_UI_Base.GL_UI.Main.CB_ButtonList;
import CB_UI_Base.GL_UI.Main.CB_TabView;
import CB_UI_Base.GL_UI.Main.MainViewBase;
import CB_UI_Base.Math.CB_RectF;
import CB_UI_Base.Math.GL_UISizes;
import Res.ResourceCache;
import de.gdxgame.Views.Actions.Action_Fast_Play;
import de.gdxgame.Views.Actions.Action_Load_Model;
import de.gdxgame.Views.Actions.Action_Play;
import de.gdxgame.Views.Actions.Action_Show_PlayView;

/**
 * @author Longri
 */
public class MainView extends MainViewBase
{

	public static MainView that;
	public static GameView gameView;
	public static CB_TabView TAB;

	// ######## Button Actions ###########
	public static Action_Show_PlayView actionShowPlayView = new Action_Show_PlayView();
	private final Action_Play actionPlay = new Action_Play();
	private final Action_Fast_Play actionFastPlay = new Action_Fast_Play();
	private final Action_Load_Model actionLoadModel = new Action_Load_Model();

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

		CB_Button btn1 = new CB_Button(btnRec, "Button1", ResourceCache.btnSpritesHome);
		CB_Button btn2 = new CB_Button(btnRec, "Button2", ResourceCache.btnSpritesHome);
		CB_Button btn3 = new CB_Button(btnRec, "Button3", ResourceCache.btnSpritesHome);
		CB_Button btn4 = new CB_Button(btnRec, "Button4", ResourceCache.btnSpritesHome);
		CB_Button btn5 = new CB_Button(btnRec, "Button5", ResourceCache.btnSpritesHome);
		CB_Button btn6 = new CB_Button(btnRec, "Button6", ResourceCache.btnSpritesHome);
		CB_Button btn7 = new CB_Button(btnRec, "Button7", ResourceCache.Misc);

		// set Button Overlays
		{
			btn1.addOverlayDrawable(ResourceCache.getSpriteDrawable("Home"));
			btn2.addOverlayDrawable(ResourceCache.getSpriteDrawable("Play"));
			btn3.addOverlayDrawable(ResourceCache.getSpriteDrawable("FastPlay"));
			btn4.addOverlayDrawable(ResourceCache.getSpriteDrawable("Pause"));
			btn5.addOverlayDrawable(ResourceCache.getSpriteDrawable("Skip"));
			btn6.addOverlayDrawable(ResourceCache.getSpriteDrawable("DelAll"));

		}

		CB_ButtonList btnList = new CB_ButtonList();
		btnList.addButton(btn1);
		btnList.addButton(btn2);
		btnList.addButton(btn3);
		btnList.addButton(btn4);
		btnList.addButton(btn5);
		btnList.addButton(btn6);
		btnList.addButton(btn7);

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
