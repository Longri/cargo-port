package controls;

import CB_Core.GL_UI.Controls.ImageButton;
import CB_Core.Math.UI_Size_Base;
import Enums.InstructionType;
import Res.ResourceCache;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

public class InstractionButton extends ImageButton
{
	InstructionType type;

	public InstractionButton(InstructionType instructionType)
	{
		super("");
		setWidth(UI_Size_Base.that.getButtonHeight());
		type = instructionType;

		this.setButtonSprites(InstBtnSprites.INSTANCE);

		Drawable TypeDrawable = null;

		switch (instructionType)
		{
		case xForward:
			TypeDrawable = new SpriteDrawable(ResourceCache.getThemedSprite("Inst1"));
			break;

		case xBack:
			TypeDrawable = new SpriteDrawable(ResourceCache.getThemedSprite("Inst2"));
			break;

		case yForward:
			TypeDrawable = new SpriteDrawable(ResourceCache.getThemedSprite("Inst3"));
			break;

		case yBack:
			TypeDrawable = new SpriteDrawable(ResourceCache.getThemedSprite("Inst4"));
			break;

		case grab:
			TypeDrawable = new SpriteDrawable(ResourceCache.getThemedSprite("Inst5"));
			break;

		case Func1:
			TypeDrawable = new SpriteDrawable(ResourceCache.getThemedSprite("Inst6"));
			break;

		case Func2:
			TypeDrawable = new SpriteDrawable(ResourceCache.getThemedSprite("Inst7"));
			break;
		}

		this.setImage(TypeDrawable);

	}

	public void render(SpriteBatch batch)
	{
		super.render(batch);
	}
}
