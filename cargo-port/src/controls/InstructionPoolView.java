package controls;

import java.util.ArrayList;

import CB_Core.GL_UI.CB_View_Base;
import CB_Core.GL_UI.GL_View_Base;
import CB_Core.GL_UI.runOnGL;
import CB_Core.Math.CB_RectF;
import CB_Core.Math.UI_Size_Base;
import Enums.InstructionType;
import Res.ResourceCache;
import controls.InstractionButton.IDelClicked;
import controls.InstructionBox.ISelected;
import de.gdxgame.GameInstructionPool;

public class InstructionPoolView extends CB_View_Base
{
	GameInstructionPool pool;
	ArrayList<InstructionBox> btn;
	float wh, margin;
	CB_RectF boxRec;
	private int mSelectedIndex = -1;

	private ISelected mSelectHandler;
	int mPoolIndex;

	public InstructionPoolView(CB_RectF rec, String Name, GameInstructionPool pool, ISelected selectHandler, int PoolIndex)
	{
		super(rec, Name);
		this.pool = pool;
		this.setBackground(ResourceCache.activityBackground);
		this.margin = UI_Size_Base.that.getMargin();
		this.setMargins(margin, margin);
		this.wh = (this.height / 2) - (margin * 3);
		boxRec = new CB_RectF(0, 0, wh, wh);
		this.setClickable(true);
		this.mPoolIndex = PoolIndex;
		this.mSelectHandler = selectHandler;
	}

	@Override
	protected void Initial()
	{

		this.initRow();
		int count = 1;
		int Index = 0;
		for (InstructionType inst : pool)
		{
			if (count < 8)
			{
				this.addNext(new InstructionBox(boxRec, inst, delClicked, Index, selectHandler));
				count++;
			}
			else
			{
				this.addLast(new InstructionBox(boxRec, inst, delClicked, Index, selectHandler));
				count = 1;
			}
			Index++;
		}
	}

	ISelected selectHandler = new ISelected()
	{

		@Override
		public void selected(int PoolIndex)
		{
			mSelectedIndex = PoolIndex;
			int index = 0;
			for (GL_View_Base child : childs)
			{
				((InstructionBox) child).setSelection(index == mSelectedIndex);
				index++;
			}
			if (mSelectHandler != null) mSelectHandler.selected(PoolIndex);
		}
	};

	IDelClicked delClicked = new IDelClicked()
	{

		@Override
		public void delClickeded(int PoolIndex)
		{
			pool.set(PoolIndex, InstructionType.Nop);
			InstructionPoolView.this.removeChilds();
			InstructionPoolView.this.RunOnGL(new runOnGL()
			{

				@Override
				public void run()
				{
					Initial();
				}
			});

		}
	};

	@Override
	protected void SkinIsChanged()
	{
		// TODO Auto-generated method stub

	}

	public void deselect()
	{
		selectHandler.selected(-1);
	}

}
