package de.gdxgame;

import CB_Core.Util.MoveableList;
import Enums.InstructionType;

/**
 * Die Klasse GameInstructionPool beschreibt eine Menge von Instruktionen. Es sind maximal 16 Instruktionen m�glich. Die Instruktionen
 * k�nnen sein: 0 NOP 1 in X-Richtung vor 2 in X-Richtung zur�ck 3 in Y-Richtung vor 4 in Y-Richtung zur�ck 5 Aufnehmen/Ablegen Portalkran 6
 * Aufruf Funktion 1 7 Aufruf Funktion 2
 * 
 * @author Lars Streblow
 */
public class GameInstructionPool extends MoveableList<InstructionType>
{

	private static final long serialVersionUID = -2685722265871835712L;
	private int instructionPointer;

	public GameInstructionPool()
	{
		for (int i = 0; i < 16; i++)
		{
			this.add(InstructionType.Nop);
		}
		instructionPointer = 0;
		this.trimToSize();
	}

	public void setInstruction(int position, int instruction)
	{
		setInstruction(position, InstructionType.values()[instruction]);
	}

	public void setInstruction(int position, InstructionType instruction)
	{
		if (0 <= position && position < this.size() && 0 <= instruction.ordinal() && instruction.ordinal() <= 7)
		{
			this.set(position, instruction);
		}
	}

	public InstructionType getInstruction(int position)
	{
		return this.get(position);
	}

	public void resetInstructionPointer()
	{
		instructionPointer = 0;
	}

	public InstructionType getNextInstruction()
	{
		if (instructionPointer < this.size()) return getInstruction(instructionPointer++);
		else
			return InstructionType.nothing;
	}

	public void setInstructionPool(String values)
	{
		int k = 0;
		int i = 0;
		for (i = 0; i < 16; i++)
		{
			this.setInstruction(i, InstructionType.Nop);
		}
		i = 0;
		while (k < values.length())
		{
			if ('0' <= values.charAt(k) && values.charAt(k) <= '7')
			{
				setInstruction(i, Character.getNumericValue(values.charAt(k)));
				i++;
				if (i >= 16) return;
			}
			k++;
		}

	}
}
