package controls;

import CB_Core.GL_UI.CB_View_Base;
import CB_Core.GL_UI.GL_View_Base;
import CB_Core.Math.CB_RectF;
import CB_Core.Math.UI_Size_Base;
import Enums.InstructionType;
import Res.ResourceCache;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import controls.InstructionButton.IDelClicked;

public class InstructionBox extends CB_View_Base
{

	protected static Drawable boxDrawable;
	protected static Drawable boxDrawableSelected;

	private boolean isSelected = false;
	private InstructionType type;
	private IDelClicked mHandler;
	int mPoolIndex;
	private ISelected mSelectedHandler;
	private IinstructionChanged mChangedHandler;

	public interface ISelected
	{
		public void selected(int PoolIndex);
	}

	public interface IinstructionChanged
	{
		public void changed();
	}

	public InstructionBox(CB_RectF rec, InstructionType type, IDelClicked handler, int PoolIndex, ISelected selectedHandler,
			IinstructionChanged changedHandler)
	{
		super(rec, "");
		mHandler = handler;
		mPoolIndex = PoolIndex;
		mSelectedHandler = selectedHandler;
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
			if (mSelectedHandler != null) mSelectedHandler.selected(mPoolIndex);
			isSelected = true;
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
		float btH = UI_Size_Base.that.getButtonHeight() / 2;

		InstructionButton btn = new InstructionButton(type, delClicked, mPoolIndex);
		btn.setPos(this.halfWidth - btH, this.halfHeight - btH);
		btn.setOnClickListener(clickListner);
		this.addChild(btn);
	}

	private IDelClicked delClicked = new IDelClicked()
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

	public InstructionType getType()
	{
		return type;
	}
}
