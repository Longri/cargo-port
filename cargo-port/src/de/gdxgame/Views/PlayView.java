package de.gdxgame.Views;

import CB_Core.GL_UI.CB_View_Base;
import CB_Core.GL_UI.Fonts;
import CB_Core.GL_UI.SpriteCache;
import CB_Core.GL_UI.Controls.Box;
import CB_Core.GL_UI.Controls.Button;
import CB_Core.GL_UI.Controls.ScrollBox;
import CB_Core.Math.CB_RectF;
import CB_Core.Math.UI_Size_Base;

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
		int Count = 1;
		for (int i = 0; i < ButtonLineCount; i++)
		{

			for (int j = 0; j < ButtonRowCount - 1; j++)
			{
				Button bt = new Button(buttonRec, "");
				bt.setText(String.valueOf(Count), Fonts.getCompass(), Fonts.getFontColor());
				bt.disable();
				content.addNext(bt, -1);
				Count++;
			}
			Button bt = new Button(buttonRec, "");
			bt.setText(String.valueOf(Count), Fonts.getCompass(), Fonts.getFontColor());
			bt.disable();
			content.addLast(bt, -1);
			Count++;
		}
	}

	@Override
	protected void SkinIsChanged()
	{
	}

}
