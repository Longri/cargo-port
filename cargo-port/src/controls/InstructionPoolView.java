package controls;

import java.util.ArrayList;

import CB_Core.GL_UI.CB_View_Base;
import CB_Core.Math.CB_RectF;
import de.gdxgame.GameInstructionPool;

public class InstructionPoolView extends CB_View_Base
{
	GameInstructionPool pool;
	ArrayList<InstructionPoolView> btn;

	public InstructionPoolView(CB_RectF rec, String Name, GameInstructionPool pool)
	{
		super(rec, Name);
		this.pool = pool;
	}

	@Override
	protected void Initial()
	{

	}

	@Override
	protected void SkinIsChanged()
	{
		// TODO Auto-generated method stub

	}

}
