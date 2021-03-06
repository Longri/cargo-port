package controls;

import CB_UI_Base.GL_UI.CB_View_Base;
import CB_UI_Base.GL_UI.GL_View_Base;
import CB_UI_Base.Math.CB_RectF;
import Enums.InstructionType;
import Res.ResourceCache;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import controls.InstructionButton.IDelClicked;

public class InstructionBox extends CB_View_Base
{

	protected static Drawable boxDrawable;
	protected static Drawable boxDrawableSelected;

	private boolean isSelected = false;
	private InstructionType type;
	private final IDelClicked mHandler;
	int mPoolIndex;
	private final IinstructionChanged mChangedHandler;

	public interface IinstructionChanged
	{
		public void changed();
	}

	public InstructionBox(CB_RectF rec, InstructionType type, IDelClicked handler, int PoolIndex, IinstructionChanged changedHandler)
	{
		super(rec, "");
		mHandler = handler;
		mPoolIndex = PoolIndex;
		mChangedHandler = changedHandler;
		this.type = type;
		this.setOnClickListener(clickListner);
		this.setClickable(true);
	}

	OnClickListener clickListner = new OnClickListener()
	{

		@Override
		public boolean onClick(GL_View_Base v, int x, int y, int pointer, int button)
		{
			type = InstructionSelect.that.getSelectedInstructionType();
			addInstructionButton();
			if (mChangedHandler != null) mChangedHandler.changed();
			return true;
		}
	};

	public void setSelection(boolean value)
	{
		isSelected = value;
	}

	@Override
	protected void Initial()
	{
		boxDrawable = ResourceCache.textFiledBackground;
		boxDrawableSelected = ResourceCache.textFiledBackgroundFocus;
		addInstructionButton();
	}

	private void addInstructionButton()
	{
		this.removeChilds();
		float btH = this.getHeight() * 0.85f;
		float btHHalf = btH / 2;

		InstructionButton btn = new InstructionButton(type, delClicked, mPoolIndex, btH);
		btn.setPos(this.getHalfWidth() - btHHalf, this.getHalfHeight() - btHHalf);
		btn.setOnClickListener(clickListner);
		this.addChild(btn);
	}

	private final IDelClicked delClicked = new IDelClicked()
	{

		@Override
		public void delClickeded(int PoolIndex)
		{
			if (mHandler != null) mHandler.delClickeded(PoolIndex);
		}
	};

	@Override
	protected void SkinIsChanged()
	{
	}

	@Override
	public void render(Batch batch)
	{
		super.render(batch);
		if (boxDrawable == null || boxDrawableSelected == null) Initial();
		if (isSelected)
		{
			boxDrawableSelected.draw(batch, 0, 0, getWidth(), getHeight());
		}
		else
		{
			boxDrawable.draw(batch, 0, 0, getWidth(), getHeight());
		}
	}

	public InstructionType getType()
	{
		return type;
	}
}
