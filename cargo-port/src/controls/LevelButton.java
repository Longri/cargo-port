package controls;

import CB_Core.GL_UI.Fonts;
import CB_Core.GL_UI.Controls.Button;
import CB_Core.Math.CB_RectF;
import de.gdxgame.Level;

public class LevelButton extends Button
{
	private Level mLevel;

	public LevelButton(CB_RectF rec, Level level)
	{
		super(rec, "LevelButton:" + level.getLevelNumber());
		mLevel = level;
		this.setText(String.valueOf(level.getLevelNumber()), Fonts.getCompass(), Fonts.getFontColor());
		if (level.getIsFreeToPlay()) this.enable();
		else
			this.disable();
	}

	public Level getLevel()
	{
		return mLevel;
	}

}
