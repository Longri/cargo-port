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
public class InstructionSelect extends CB_View_Base
{
	public static InstructionSelect that;

	InstructionButton instBtnArray[] = new InstructionButton[7];

	InstructionType selectedType;

	public InstructionSelect(CB_RectF rec, String Name)
	{

		super(rec, Name);
		that = this;
		for (int i = 0; i < instBtnArray.length; i++)
		{
			instBtnArray[i] = new InstructionButton(InstructionType.values()[i + 1], null, -1);
			instBtnArray[i].setOnClickListener(click);
			instBtnArray[i].disableDelete();
			this.addChild(instBtnArray[i]);
		}
		instBtnArray[0].performClick();
		this.setWidth(instBtnArray[0].getWidth());
	}

	public InstructionType getSelectedInstructionType()
	{
		return selectedType;
	}

	private OnClickListener click = new OnClickListener()
	{

		@Override
		public boolean onClick(GL_View_Base v, int x, int y, int pointer, int button)
		{
			selectedType = ((InstructionButton) v).type;
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
	}

}
