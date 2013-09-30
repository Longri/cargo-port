package de.gdxgame.Views;

import java.util.ArrayList;

import CB_UI_Base.GL_UI.CB_View_Base;
import CB_UI_Base.GL_UI.GL_View_Base;
import CB_UI_Base.GL_UI.SpriteCacheBase;
import CB_UI_Base.GL_UI.runOnGL;
import CB_UI_Base.GL_UI.Controls.Box;
import CB_UI_Base.GL_UI.Controls.ScrollBox;
import CB_UI_Base.GL_UI.GL_Listener.GL;
import CB_UI_Base.Math.CB_RectF;
import CB_UI_Base.Math.UI_Size_Base;
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
		this.setBackground(SpriteCacheBase.activityBackground);
		that = this;
	}

	private TestLevels levels;

	@Override
	protected void Initial()
	{

		levels = new TestLevels();

		CB_RectF buttonRec = new CB_RectF(0, 0, UI_Size_Base.that.getButtonWidthWide(), UI_Size_Base.that.getButtonWidthWide());

		int ButtonRowCount = (int) (this.getWidth() / (buttonRec.getWidth() + UI_Size_Base.that.getMargin()));
		float lineCount = ((float) levels.Levels.size() - 1) / ButtonRowCount;
		lineCount += (lineCount % 2);
		int ButtonLineCount = (int) lineCount;
		if (ButtonLineCount == 0)
		{
			ButtonLineCount = 1;
			ButtonRowCount = levels.Levels.size() - 1;
		}

		scrollBox = new ScrollBox(this);
		scrollBox.setZeroPos();
		content = new Box(this, "");
		content.setHeight(ButtonLineCount * (buttonRec.getHeight() + UI_Size_Base.that.getMargin()));
		content.setZeroPos();
		scrollBox.addChild(content);
		this.addChild(scrollBox);

		// content.initRow(true);
		content.initRow();

		float btnMargin = ((this.width - (ButtonRowCount * buttonRec.getWidth())) / (ButtonRowCount + 1));
		content.setMargins(btnMargin, btnMargin);
		content.setBorders(btnMargin, 0);
		mLevelButtonList = new ArrayList<LevelButton>();
		int Index = -1;
		for (int i = 0; i < ButtonLineCount; i++)
		{

			for (int j = 0; j < ButtonRowCount - 1; j++)
			{
				if (levels.Levels.size() - 1 <= Index++) break;
				LevelButton bt = new LevelButton(Index, buttonRec, levels.Levels.get(Index));
				mLevelButtonList.add(bt);
				bt.setOnClickListener(onClick);
				content.addNext(bt, -1);
			}
			if (levels.Levels.size() - 1 <= Index++)
			{
				if (!content.RowIsFinalise()) content.FinaliseRow(-1);
				break;
			}
			LevelButton bt = new LevelButton(Index, buttonRec, levels.Levels.get(Index));
			mLevelButtonList.add(bt);
			bt.setOnClickListener(onClick);
			content.addLast(bt, -1);
		}

		GL.that.RunOnGL(new runOnGL()
		{

			@Override
			public void run()
			{
				GL.that.renderOnce("PlayView inital()");
			}
		});
		GL.that.renderOnce("PlayView inital()");
	}

	private final OnClickListener onClick = new OnClickListener()
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
		if (levels.Levels.size() > nextLevel)
		{
			levels.Levels.get(nextLevel).unlock();
			mLevelButtonList.get(nextLevel).enable();
		}
		else
		{
			// TODO Msg (ooooooohh, sorry no more levels)
		}

	}
}
