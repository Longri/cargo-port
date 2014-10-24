package controls;

import java.util.ArrayList;

import CB_UI_Base.GL_UI.CB_View_Base;
import CB_UI_Base.GL_UI.GL_View_Base;
import CB_UI_Base.GL_UI.IRunOnGL;
import CB_UI_Base.GL_UI.GL_Listener.GL;
import CB_UI_Base.Math.CB_RectF;
import CB_UI_Base.Math.UI_Size_Base;
import Enums.InstructionType;
import Res.ResourceCache;
import controls.InstructionBox.IinstructionChanged;
import controls.InstructionButton.IDelClicked;
import de.gdxgame.GameInstructionPool;
import de.gdxgame.GameInstructionPool.PoolType;

public class InstructionPoolView extends CB_View_Base
{
	GameInstructionPool pool;
	ArrayList<InstructionBox> btn;
	float wh, margin;
	CB_RectF boxRec;
	int mPoolIndex;
	private final PoolType mPoolType;
	private final IinstructionChanged mChangedHandler;

	public InstructionPoolView(CB_RectF rec, String Name, GameInstructionPool pool, int PoolIndex, IinstructionChanged changedHandler)
	{
		super(rec, Name);
		mPoolType = pool.getPoolType();
		this.pool = pool;
		this.setBackground(ResourceCache.activityBackground);
		this.margin = UI_Size_Base.that.getMargin() / 2;
		this.setMargins(margin, margin);
		this.wh = (this.getHeight() / 2) - (margin * 2);
		boxRec = new CB_RectF(0, 0, wh, wh);
		this.setClickable(true);
		this.mPoolIndex = PoolIndex;
		mChangedHandler = changedHandler;
	}

	@Override
	protected void Initial()
	{
		// this.bottomBorder = this.margin;
		// this.initRow();
		this.initRow(true, this.getHeight());
		int count = 1;
		int Index = 0;
		for (int i = 0; i < pool.size(); i++)
		{
			InstructionType inst = pool.get(i);
			if (count < 8)
			{
				this.addNext(new InstructionBox(boxRec, inst, delClicked, Index, changedHandler));
				count++;
			}
			else
			{
				this.addLast(new InstructionBox(boxRec, inst, delClicked, Index, changedHandler));
				count = 1;
			}
			Index++;
		}
	}

	IinstructionChanged changedHandler = new IinstructionChanged()
	{

		@Override
		public void changed()
		{
			// Read Instructions
			GameInstructionPool newPool = new GameInstructionPool(mPoolType);
			newPool.clear(); // Constructor beffüllt den Pool schon mit 16 NOP
			for (int i = 0; i < childs.size(); i++)
			{
				GL_View_Base view = childs.get(i);
				InstructionType type = ((InstructionBox) view).getType();
				newPool.add(type);
				newPool.trimToSize();
				pool = newPool;
			}

			if (mChangedHandler != null) mChangedHandler.changed();
		}
	};

	IDelClicked delClicked = new IDelClicked()
	{

		@Override
		public void delClickeded(int PoolIndex)
		{
			pool.set(PoolIndex, InstructionType.Nop);
			InstructionPoolView.this.removeChildsDirekt();

			GL.that.RunOnGL(new IRunOnGL()
			{

				@Override
				public void run()
				{
					GL.that.RunOnGL(new IRunOnGL()
					{

						@Override
						public void run()
						{
							InstructionPoolView.this.resetInitial();
						}
					});
				}
			});

		}
	};

	@Override
	protected void SkinIsChanged()
	{
	}

}
