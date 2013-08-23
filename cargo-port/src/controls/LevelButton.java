package controls;

import CB_UI_Base.GL_UI.Fonts;
import CB_UI_Base.GL_UI.Controls.Button;
import CB_UI_Base.Math.CB_RectF;
import de.gdxgame.GameSet;

public class LevelButton extends Button
{
	private final GameSet mLevel;
	private final int mIndex;

	public LevelButton(int index, CB_RectF rec, GameSet level)
	{
		super(rec, "LevelButton:" + level.getLevelNumber());
		mIndex = index;
		mLevel = level;
		this.setText(String.valueOf(level.getLevelNumber()), Fonts.getCompass(), Fonts.getFontColor());
		if (level.getIsFreeToPlay()) this.enable();
		else
			this.disable();
	}

	public GameSet getLevel()
	{
		return mLevel;
	}

	public int getIndex()
	{
		return mIndex;
	}

}
