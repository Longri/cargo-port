package controls;

import CB_Core.GL_UI.GL_View_Base;
import CB_Core.GL_UI.Controls.Image;
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
	boolean markToDelete;
	Image deleteImage;

	public InstractionButton(InstructionType instructionType)
	{
		super("");
		setWidth(UI_Size_Base.that.getButtonHeight());
		type = instructionType;

		float wh = this.width / 3.6f;

		deleteImage = new Image(this.width - wh, this.height - wh, wh, wh, "");
		deleteImage.setDrawable(new SpriteDrawable(ResourceCache.getThemedSprite("InstClose")));
		this.addChild(deleteImage);
		deleteImage.setVisible(false);
		deleteImage.setOnClickListener(deleteClick);

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
		this.setOnLongClickListener(longClick);

	}

	private OnClickListener deleteClick = new OnClickListener()
	{

		@Override
		public boolean onClick(GL_View_Base v, int x, int y, int pointer, int button)
		{

			return true;
		}
	};

	private OnClickListener longClick = new OnClickListener()
	{

		@Override
		public boolean onClick(GL_View_Base v, int x, int y, int pointer, int button)
		{
			markToDelete = true;
			InstractionButton.this.deleteImage.setVisible();
			return true;
		}
	};

	public void render(SpriteBatch batch)
	{
		if (type == InstructionType.Nop || type == InstructionType.nothing) return;
		super.render(batch);
	}
}
