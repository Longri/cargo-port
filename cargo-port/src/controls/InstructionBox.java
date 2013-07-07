package controls;

import CB_Core.GL_UI.CB_View_Base;
import CB_Core.Math.CB_RectF;
import CB_Core.Math.UI_Size_Base;
import Enums.InstructionType;
import Res.ResourceCache;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class InstructionBox extends CB_View_Base
{

	protected static Drawable boxDrawable;
	protected static Drawable boxDrawableSelected;

	private boolean isSelected = false;
	private InstructionType type;

	public InstructionBox(CB_RectF rec, InstructionType type)
	{
		super(rec, "");
		this.type = type;
	}

	@Override
	protected void Initial()
	{
		boxDrawable = ResourceCache.textFiledBackground;
		boxDrawableSelected = ResourceCache.textFiledBackgroundFocus;

		float btH = UI_Size_Base.that.getButtonHeight() / 2;

		this.addChild(new InstractionButton(type)).setPos(this.halfWidth - btH, this.halfHeight - btH);
	}

	@Override
	protected void SkinIsChanged()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void render(SpriteBatch batch)
	{
		super.render(batch);
		if (boxDrawable == null || boxDrawableSelected == null) Initial();
		if (isSelected)
		{
			boxDrawableSelected.draw(batch, 0, 0, width, height);
		}
		else
		{
			boxDrawable.draw(batch, 0, 0, width, height);
		}
	}
}
