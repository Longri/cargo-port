package controls;

import CB_UI_Base.GL_UI.CB_View_Base;
import CB_UI_Base.GL_UI.GL_View_Base;
import CB_UI_Base.Math.CB_RectF;
import CB_UI_Base.Math.UiSizes;
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
	float margin;

	public InstructionSelect(CB_RectF rec, String Name)
	{

		super(rec, Name);
		that = this;
		margin = UiSizes.that.getMargin();
		int Anzahl = instBtnArray.length;
		float btnHeight = (this.height - ((Anzahl + 2) * margin)) / Anzahl;

		for (int i = 0; i < instBtnArray.length; i++)
		{
			instBtnArray[i] = new InstructionButton(InstructionType.values()[i + 1], null, -1, btnHeight);
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

	private final OnClickListener click = new OnClickListener()
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
		this.setWidth(instBtnArray[0].getWidth() + margin);

		float yPos = margin;
		for (int i = 0; i < instBtnArray.length; i++)
		{
			instBtnArray[i].setX(margin);
			instBtnArray[i].setY(yPos);
			yPos += instBtnArray[i].getHeight() + margin;
		}
	}

	@Override
	protected void SkinIsChanged()
	{
	}

}
