package de.gdxgame.Views;

import CB_Core.GL_UI.CB_View_Base;
import CB_Core.GL_UI.GL_View_Base;
import CB_Core.GL_UI.SpriteCache;
import CB_Core.GL_UI.Controls.Box;
import CB_Core.GL_UI.Controls.ScrollBox;
import CB_Core.Math.CB_RectF;
import CB_Core.Math.UI_Size_Base;
import controls.LevelButton;
import de.gdxgame.Level;
import de.gdxgame.Views.Actions.Action_Show_GameView;

public class PlayView extends CB_View_Base
{
	public static final int MAX_LEVEL_COUNT = 20;

	ScrollBox scrollBox;
	Box content;

	public PlayView(CB_RectF rec)
	{
		super(rec, "PlayView");
		this.setBackground(SpriteCache.activityBackground);
	}

	@Override
	protected void Initial()
	{
		CB_RectF buttonRec = new CB_RectF(0, 0, UI_Size_Base.that.getButtonWidthWide(), UI_Size_Base.that.getButtonWidthWide());

		int ButtonRowCount = (int) (this.getWidth() / (buttonRec.getWidth() + UI_Size_Base.that.getMargin()));
		int ButtonLineCount = MAX_LEVEL_COUNT / ButtonRowCount;

		scrollBox = new ScrollBox(this);
		content = new Box(this, "");
		content.setHeight(ButtonLineCount * (buttonRec.getHeight() + UI_Size_Base.that.getMargin()));
		scrollBox.addChild(content);
		this.addChild(scrollBox);

		content.initRow(true);

		float btnMargin = ((this.width - (ButtonRowCount * buttonRec.getWidth())) / (ButtonRowCount + 1));
		content.setMargins(btnMargin, btnMargin);

		for (int i = 0; i < ButtonLineCount; i++)
		{

			for (int j = 0; j < ButtonRowCount - 1; j++)
			{
				LevelButton bt = new LevelButton(buttonRec, Level.TestLevel);
				bt.setOnClickListener(onClick);
				content.addNext(bt, -1);
			}
			LevelButton bt = new LevelButton(buttonRec, Level.TestLevel);
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
			return false;
		}
	};

	@Override
	protected void SkinIsChanged()
	{
	}

}