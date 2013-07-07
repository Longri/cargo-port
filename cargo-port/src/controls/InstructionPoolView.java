package controls;

import java.util.ArrayList;

import CB_Core.GL_UI.CB_View_Base;
import CB_Core.Math.CB_RectF;
import CB_Core.Math.UI_Size_Base;
import Enums.InstructionType;
import Res.ResourceCache;
import de.gdxgame.GameInstructionPool;

public class InstructionPoolView extends CB_View_Base
{
	GameInstructionPool pool;
	ArrayList<InstructionBox> btn;
	float wh, margin;
	CB_RectF boxRec;

	public InstructionPoolView(CB_RectF rec, String Name, GameInstructionPool pool)
	{
		super(rec, Name);
		this.pool = pool;
		this.setBackground(ResourceCache.activityBackground);
		this.margin = UI_Size_Base.that.getMargin();
		this.setMargins(margin, margin);
		this.wh = (this.height / 2) - (margin * 3);
		boxRec = new CB_RectF(0, 0, wh, wh);
	}

	@Override
	protected void Initial()
	{
		this.initRow();
		int count = 1;
		for (InstructionType inst : pool)
		{
			if (count < 8)
			{
				this.addNext(new InstructionBox(boxRec, inst));
				count++;
			}
			else
			{
				this.addLast(new InstructionBox(boxRec, inst));
				count = 1;
			}

		}
	}

	@Override
	protected void SkinIsChanged()
	{
		// TODO Auto-generated method stub

	}

}
