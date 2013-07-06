package controls;

import CB_Core.GL_UI.CB_View_Base;
import CB_Core.GL_UI.GL_View_Base;
import CB_Core.Math.CB_RectF;
import CB_Core.Math.UI_Size_Base;
import Enums.InstructionType;

/**
 * Control mit allen möglichen Instruction Buttons
 * 
 * @author Longri
 */
public class InstractionSelect extends CB_View_Base
{
	InstractionButton instBtnArray[] = new InstractionButton[7];

	InstructionType selectedType;

	public InstractionSelect(CB_RectF rec, String Name)
	{
		super(rec, Name);
		for (int i = 0; i < instBtnArray.length; i++)
		{
			instBtnArray[i] = new InstractionButton(InstructionType.values()[i + 1]);
			instBtnArray[i].setOnClickListener(click);
			this.addChild(instBtnArray[i]);
		}
		instBtnArray[0].performClick();
	}

	private OnClickListener click = new OnClickListener()
	{

		@Override
		public boolean onClick(GL_View_Base v, int x, int y, int pointer, int button)
		{
			selectedType = ((InstractionButton) v).type;
			for (int i = 0; i < instBtnArray.length; i++)
			{
				if (instBtnArray[i].type == selectedType) instBtnArray[i].setFocus(true);
				else
					instBtnArray[i].setFocus(false);
			}
			return true;
		}
	};

	@Override
	protected void Initial()
	{
		this.setWidth(instBtnArray[0].getWidth() + UI_Size_Base.that.getMargin());
		float horiMargin = (this.getHeight() - (instBtnArray[0].getHeight() * (instBtnArray.length + 1))) / (instBtnArray.length + 1);
		float yPos = horiMargin * 2;
		for (int i = 0; i < instBtnArray.length; i++)
		{
			instBtnArray[i].setX(UI_Size_Base.that.getMargin());
			instBtnArray[i].setY(yPos);
			yPos += horiMargin + instBtnArray[i].getHeight();
		}
	}

	@Override
	protected void SkinIsChanged()
	{
		// TODO Auto-generated method stub

	}

}
