package de.gdxgame.Views;

import java.util.ArrayList;

import CB_Core.GL_UI.CB_View_Base;
import CB_Core.GL_UI.GL_View_Base;
import CB_Core.GL_UI.SpriteCache;
import CB_Core.GL_UI.Controls.Box;
import CB_Core.GL_UI.Controls.ScrollBox;
import CB_Core.Math.CB_RectF;
import CB_Core.Math.UI_Size_Base;
import controls.LevelButton;
import de.gdxgame.TestLevels;
import de.gdxgame.Views.Actions.Action_Show_GameView;

public class PlayView extends CB_View_Base
{
	public static PlayView that;
	ScrollBox scrollBox;
	Box content;
	private int mLastPlayedIndex = -1;
	private ArrayList<LevelButton> mLevelButtonList;

	public PlayView(CB_RectF rec)
	{
		super(rec, "PlayView");
		this.setBackground(SpriteCache.activityBackground);
		that = this;
	}

	@Override
	protected void Initial()
	{
		CB_RectF buttonRec = new CB_RectF(0, 0, UI_Size_Base.that.getButtonWidthWide(), UI_Size_Base.that.getButtonWidthWide());

		int ButtonRowCount = (int) (this.getWidth() / (buttonRec.getWidth() + UI_Size_Base.that.getMargin()));
		int ButtonLineCount = (TestLevels.Levels.size() - 1) / ButtonRowCount;
		if (ButtonLineCount == 0)
		{
			ButtonLineCount = 1;
			ButtonRowCount = TestLevels.Levels.size() - 1;
		}

		scrollBox = new ScrollBox(this);
		content = new Box(this, "");
		content.setHeight(ButtonLineCount * (buttonRec.getHeight() + UI_Size_Base.that.getMargin()));
		scrollBox.addChild(content);
		this.addChild(scrollBox);

		content.initRow(true);

		float btnMargin = ((this.width - (ButtonRowCount * buttonRec.getWidth())) / (ButtonRowCount + 1));
		content.setMargins(btnMargin, btnMargin);
		mLevelButtonList = new ArrayList<LevelButton>();
		int Index = 0;
		for (int i = 0; i < ButtonLineCount; i++)
		{

			for (int j = 0; j < ButtonRowCount; j++)
			{
				LevelButton bt = new LevelButton(Index, buttonRec, TestLevels.Levels.get(Index++));
				mLevelButtonList.add(bt);
				bt.setOnClickListener(onClick);
				content.addNext(bt, -1);
			}
			LevelButton bt = new LevelButton(Index, buttonRec, TestLevels.Levels.get(Index++));
			mLevelButtonList.add(bt);
			bt.setOnClickListener(onClick);
			content.addLast(bt, -1);
		}
	}

	private OnClickListener onClick = new OnClickListener()
	{

		@Override
		public boolean onClick(GL_View_Base arg0, int arg1, int arg2, int arg3, int arg4)
		{
			LevelButton bt = (LevelButton) arg0;
			Action_Show_GameView action = new Action_Show_GameView(bt.getLevel());
			action.setTab(MainView.that, MainView.TAB);
			action.CallExecute();
			mLastPlayedIndex = bt.getIndex();
			return false;
		}
	};

	@Override
	protected void SkinIsChanged()
	{
	}

	public void unlockNextLevel()
	{
		int nextLevel = mLastPlayedIndex + 1;
		if (TestLevels.Levels.size() >= nextLevel)
		{
			TestLevels.Levels.get(nextLevel).unlock();
			mLevelButtonList.get(nextLevel).enable();
		}
		else
		{
			// TODO Msg (sorry no more levels)
		}

	}
}
