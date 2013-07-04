package controls;

import CB_Core.GL_UI.Controls.Button;
import CB_Core.Math.UI_Size_Base;
import Enums.InstructionType;

public class InstractionButton extends Button
{
	InstructionType type;

	public InstractionButton(InstructionType instructionType)
	{
		super("");
		setWidth(UI_Size_Base.that.getButtonHeight());
		type = instructionType;
	}

}
