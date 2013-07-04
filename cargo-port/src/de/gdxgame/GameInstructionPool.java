package de.gdxgame;

import Enums.InstructionType;

/**
 * Die Klasse GameInstructionPool beschreibt eine Menge von Instruktionen. Es sind maximal 16 Instruktionen möglich. Die Instruktionen
 * können sein: 0 NOP 1 in X-Richtung vor 2 in X-Richtung zurück 3 in Y-Richtung vor 4 in Y-Richtung zurück 5 Aufnehmen/Ablegen Portalkran 6
 * Aufruf Funktion 1 7 Aufruf Funktion 2
 * 
 * @author Lars Streblow
 */
public class GameInstructionPool
{
	private InstructionType[] gameInstructions;
	private int instructionPointer;

	public GameInstructionPool()
	{
		gameInstructions = new InstructionType[16];
		for (int i = 0; i < gameInstructions.length; i++)
		{
			gameInstructions[i] = InstructionType.Nop;
		}
		instructionPointer = 0;
	}

	public void setInstruction(int position, int instruction)
	{
		setInstruction(position, InstructionType.values()[instruction]);
	}

	public void setInstruction(int position, InstructionType instruction)
	{
		if (0 <= position && position < gameInstructions.length && 0 <= instruction.ordinal() && instruction.ordinal() <= 7)
		{
			gameInstructions[position] = instruction;
		}
	}

	public InstructionType getInstruction(int position)
	{
		if (0 <= position && position < gameInstructions.length) return gameInstructions[position];
		else
			return InstructionType.Nop;
	}

	public void resetInstructionPointer()
	{
		instructionPointer = 0;
	}

	public InstructionType getNextInstruction()
	{
		if (instructionPointer < gameInstructions.length) return getInstruction(instructionPointer++);
		else
			return InstructionType.nothing;
	}
}
