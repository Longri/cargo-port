package de.gdxgame;

import Enums.InstructionType;
import de.gdxgame.GameInstructionPool.PoolType;

/**
 * Enth?llt alle Infos eines Game Levels<br>
 * * getLeveDimensions() gibt die Abmessungen dieses Levels zur?ck <br>
 * 
 * @author Lars Streblow
 * @author Longri
 */
public class GameSet
{
	protected boolean mIsFreeToPlay = false;
	protected int mLevelNumber = 0;
	public GameFloor startFloor;
	public GameFloor targetFloor;
	public GameCrane startCrane;
	public GameFloor currentFloor;
	public GameCrane currentCrane;
	public GameInstructionPool mainInstructionPool;
	public GameInstructionPool func1InstructionPool;
	public GameInstructionPool func2InstructionPool;
	public GameInstructionPool mainInstructionPoolDesign;
	public GameInstructionPool func1InstructionPoolDesign;
	public GameInstructionPool func2InstructionPoolDesign;
	public GameCoord boxAnimationStartCoord;
	public GameCoord boxAnimationTargetCoord;
	public GameCoord craneAnimationStartCoord;
	public GameCoord craneAnimationTargetCoord;
	public GameCoord hookAnimationStartCoord;
	public GameCoord hookAnimationTargetCoord;
	public InstructionType currentInstruction;
	private PoolType currentInstructionPool;
	private PoolType func1Stack;
	private PoolType func2Stack;
	private boolean gameAccomplished;

	public GameSet(int x, int y, int z)
	{
		super();
		mLevelDimensions = new GameCoord(x, y, z);
		startFloor = new GameFloor(x, y, z);
		targetFloor = new GameFloor(x, y, z);
		create();
	}

	protected GameSet()
	{
		create();
	}

	protected void create()
	{

		startCrane = new GameCrane();
		currentFloor = new GameFloor();
		currentCrane = new GameCrane();
		mainInstructionPool = new GameInstructionPool(PoolType.main);
		func1InstructionPool = new GameInstructionPool(PoolType.func1);
		func2InstructionPool = new GameInstructionPool(PoolType.func2);
		mainInstructionPoolDesign = new GameInstructionPool(PoolType.main);
		func1InstructionPoolDesign = new GameInstructionPool(PoolType.func1);
		func2InstructionPoolDesign = new GameInstructionPool(PoolType.func2);
		boxAnimationStartCoord = new GameCoord();
		boxAnimationTargetCoord = new GameCoord();
		craneAnimationStartCoord = new GameCoord();
		craneAnimationTargetCoord = new GameCoord();
		hookAnimationStartCoord = new GameCoord();
		hookAnimationTargetCoord = new GameCoord();
		currentInstruction = InstructionType.Nop;
		currentInstructionPool = PoolType.main;
		func1Stack = PoolType.main;
		func2Stack = PoolType.main;
		gameAccomplished = false;
	}

	public GameSet(String dimensions, String floor1, String floor2, String crane, String main, String func1, String func2,
			String maindesign, String func1design, String func2design)
	{
		super();
		create();
		int x = 0;
		int y = 0;
		int z = 0;
		String str_dimensions[] = dimensions.split("/", -1);
		if (!str_dimensions[0].isEmpty()) x = Integer.parseInt(str_dimensions[0]);
		if (!str_dimensions[1].isEmpty()) y = Integer.parseInt(str_dimensions[1]);
		if (!str_dimensions[2].isEmpty()) z = Integer.parseInt(str_dimensions[2]);
		mLevelDimensions = new GameCoord(x, y, z);
		startFloor = new GameFloor(x, y, z);
		startFloor.setFloor(floor1);
		targetFloor = new GameFloor(x, y, z);
		targetFloor.setFloor(floor2);
		x = 0;
		y = 0;
		String str_crane[] = crane.split("/", -1);
		if (!str_crane[0].isEmpty()) x = Integer.parseInt(str_crane[0]);
		if (!str_crane[1].isEmpty()) y = Integer.parseInt(str_crane[1]);
		startCrane.setPosition(x, y);
		mainInstructionPool.setInstructionPool(main);
		func1InstructionPool.setInstructionPool(func1);
		func2InstructionPool.setInstructionPool(func2);
		mainInstructionPoolDesign.setInstructionPool(maindesign);
		func1InstructionPoolDesign.setInstructionPool(func1design);
		func2InstructionPoolDesign.setInstructionPool(func2design);
	}

	/**
	 * Enth?lt die drei Dimensionen des Spielfelds. <br>
	 */
	private GameCoord mLevelDimensions;

	/**
	 * gibt die drei Dimensionen des Spielfelds zur?ck. <br>
	 * 
	 * @return GameCoord
	 */
	public GameCoord getLevelDimensions()
	{
		if (mLevelDimensions == null) mLevelDimensions = new GameCoord(0, 0, 0);
		return mLevelDimensions;
	}

	/**
	 * Gibt TRUE zur?ck, wenn das Level zum Spielen freigeschalten ist.
	 * 
	 * @return boolean
	 */
	public boolean getIsFreeToPlay()
	{
		return mIsFreeToPlay;
	}

	/**
	 * Gibt die Level Nummer zur?ck
	 * 
	 * @return int
	 */
	public int getLevelNumber()
	{
		return mLevelNumber;
	}

	public boolean gameAccomplished()
	{
		return gameAccomplished;
	}

	public void startGame()
	{
		currentFloor = new GameFloor(startFloor);
		currentCrane = new GameCrane(startCrane);
		mainInstructionPool.resetInstructionPointer();
		func1InstructionPool.resetInstructionPointer();
		func2InstructionPool.resetInstructionPointer();
		mainInstructionPoolDesign.resetInstructionPointer();
		func1InstructionPoolDesign.resetInstructionPointer();
		func2InstructionPoolDesign.resetInstructionPointer();
		boxAnimationStartCoord.setNull();
		boxAnimationTargetCoord.setNull();
		craneAnimationStartCoord.setNull();
		craneAnimationTargetCoord.setNull();
		hookAnimationStartCoord.setNull();
		hookAnimationTargetCoord.setNull();
		currentInstruction = InstructionType.Nop;
		currentInstructionPool = PoolType.main;
		gameAccomplished = false;
	}

	/**
	 * Diese Methode f�hrt die n�chste Instruktion aus der Menge der Instruktionen aus. Sie aktualisiert die Objekte currentCrane (aktuelle
	 * Portalkranposition) und currentFloor (aktueller Zustand der Lagerfl�che)
	 * 
	 * @return 0 normaler Zug -1 Programmende erreicht (gameAccomplished true/false) -2 NOP-Code (runInstruction erneut aufrufen) -3 Randzug
	 *         -4 Aufnahme/Ablegen -5 leere Aufnahme -6 Zug l�st Kollision aus -7 Funktionsaufruf func1/func2 -8 Funktionsende func1/func2
	 *         erreicht
	 */
	public int runInstruction()
	{
		int returnCode = 0;
		InstructionType instructionCode = InstructionType.nothing;
		boxAnimationStartCoord.setNull();
		boxAnimationTargetCoord.setNull();
		craneAnimationStartCoord.setNull();
		craneAnimationTargetCoord.setNull();
		hookAnimationStartCoord.setNull();
		hookAnimationTargetCoord.setNull();
		switch (currentInstructionPool)
		{
		case main:
			instructionCode = mainInstructionPool.getNextInstruction();
			if (instructionCode == InstructionType.nothing)
			{
				// end of mainInstructionPool
				// we finished, compare currentFloor and targetFloor
				if (currentFloor.isIdenticalTo(targetFloor)) gameAccomplished = true;
				returnCode = -1;
			}
			break;
		case func1:
			instructionCode = func1InstructionPool.getNextInstruction();
			if (instructionCode == InstructionType.nothing)
			{
				// end of func1InstructionPool
				currentInstructionPool = func1Stack;
				returnCode = -8;
			}
			break;
		case func2:
			instructionCode = func2InstructionPool.getNextInstruction();
			if (instructionCode == InstructionType.nothing)
			{
				// end of func2InstructionPool
				currentInstructionPool = func2Stack;
				returnCode = -8;
			}
			break;
		default:
			returnCode = -1;
		}
		if (returnCode != 0) return returnCode;
		switch (instructionCode)
		{
		case Nop: // NOP
			returnCode = -2;
			break;
		case xForward: // x-Position vor
			if (currentCrane.getXPosition() < currentFloor.getWidth() - 1)
			{
				if (currentCrane.isLoaded())
				{
					if (currentFloor.getBoxes(currentCrane.getXPosition() + 1, currentCrane.getYPosition()) == currentFloor.getHeight())
					{
						// Kranposition auf Zielposition, Vernichtung der aufgenommenen Box, Vernichtung der obersten Box an Zielposition
						boxAnimationStartCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition(),
								currentFloor.getHeight() - 1);
						craneAnimationStartCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition(),
								currentFloor.getHeight());
						hookAnimationStartCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition(),
								currentFloor.getHeight() - 1);
						boxAnimationTargetCoord.setGameCoord(currentCrane.getXPosition() + 1, currentCrane.getYPosition(),
								currentFloor.getHeight() - 1);
						craneAnimationTargetCoord.setGameCoord(currentCrane.getXPosition() + 1, currentCrane.getYPosition(),
								currentFloor.getHeight());
						hookAnimationTargetCoord.setGameCoord(currentCrane.getXPosition() + 1, currentCrane.getYPosition(),
								currentFloor.getHeight() - 1);
						currentCrane.setPosition(currentCrane.getXPosition() + 1, currentCrane.getYPosition());
						currentFloor.setBoxes(currentCrane.getXPosition(), currentCrane.getYPosition(),
								currentFloor.getBoxes(currentCrane.getXPosition(), currentCrane.getYPosition()) - 1);
						currentCrane.toggleLoadState();
						returnCode = -6;
					}
					else
					{
						boxAnimationStartCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition(),
								currentFloor.getHeight() - 1);
						craneAnimationStartCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition(),
								currentFloor.getHeight());
						hookAnimationStartCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition(),
								currentFloor.getHeight() - 1);
						boxAnimationTargetCoord.setGameCoord(currentCrane.getXPosition() + 1, currentCrane.getYPosition(),
								currentFloor.getHeight() - 1);
						craneAnimationTargetCoord.setGameCoord(currentCrane.getXPosition() + 1, currentCrane.getYPosition(),
								currentFloor.getHeight());
						hookAnimationTargetCoord.setGameCoord(currentCrane.getXPosition() + 1, currentCrane.getYPosition(),
								currentFloor.getHeight() - 1);
						currentCrane.setPosition(currentCrane.getXPosition() + 1, currentCrane.getYPosition());
					}
				}
				else
				{
					craneAnimationStartCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition(),
							currentFloor.getHeight());
					hookAnimationStartCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition(),
							currentFloor.getHeight() - 1);
					craneAnimationTargetCoord.setGameCoord(currentCrane.getXPosition() + 1, currentCrane.getYPosition(),
							currentFloor.getHeight());
					hookAnimationTargetCoord.setGameCoord(currentCrane.getXPosition() + 1, currentCrane.getYPosition(),
							currentFloor.getHeight() - 1);
					currentCrane.setPosition(currentCrane.getXPosition() + 1, currentCrane.getYPosition());
				}
			}
			else
			{
				if (currentCrane.isLoaded())
				{
					boxAnimationStartCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition(),
							currentFloor.getHeight() - 1);
					boxAnimationTargetCoord.setGameCoord(currentCrane.getXPosition() + 1, currentCrane.getYPosition(),
							currentFloor.getHeight() - 1);
				}
				craneAnimationStartCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition(), currentFloor.getHeight());
				hookAnimationStartCoord
						.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition(), currentFloor.getHeight() - 1);
				craneAnimationTargetCoord.setGameCoord(currentCrane.getXPosition() + 1, currentCrane.getYPosition(),
						currentFloor.getHeight());
				hookAnimationTargetCoord.setGameCoord(currentCrane.getXPosition() + 1, currentCrane.getYPosition(),
						currentFloor.getHeight() - 1);
				returnCode = -3;
			}
			break;
		case xBack: // x-Position zur�ck
			if (currentCrane.getXPosition() > 0)
			{
				if (currentCrane.isLoaded())
				{
					if (currentFloor.getBoxes(currentCrane.getXPosition() - 1, currentCrane.getYPosition()) == currentFloor.getHeight())
					{
						// Kranposition auf Zielposition, Vernichtung der aufgenommenen Box, Vernichtung der obersten Box an Zielposition
						boxAnimationStartCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition(),
								currentFloor.getHeight() - 1);
						craneAnimationStartCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition(),
								currentFloor.getHeight());
						hookAnimationStartCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition(),
								currentFloor.getHeight() - 1);
						boxAnimationTargetCoord.setGameCoord(currentCrane.getXPosition() - 1, currentCrane.getYPosition(),
								currentFloor.getHeight() - 1);
						craneAnimationTargetCoord.setGameCoord(currentCrane.getXPosition() - 1, currentCrane.getYPosition(),
								currentFloor.getHeight());
						hookAnimationTargetCoord.setGameCoord(currentCrane.getXPosition() - 1, currentCrane.getYPosition(),
								currentFloor.getHeight() - 1);
						currentCrane.setPosition(currentCrane.getXPosition() - 1, currentCrane.getYPosition());
						currentFloor.setBoxes(currentCrane.getXPosition(), currentCrane.getYPosition(),
								currentFloor.getBoxes(currentCrane.getXPosition(), currentCrane.getYPosition()) - 1);
						currentCrane.toggleLoadState();
						returnCode = -6;
					}
					else
					{
						boxAnimationStartCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition(),
								currentFloor.getHeight() - 1);
						craneAnimationStartCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition(),
								currentFloor.getHeight());
						hookAnimationStartCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition(),
								currentFloor.getHeight() - 1);
						boxAnimationTargetCoord.setGameCoord(currentCrane.getXPosition() - 1, currentCrane.getYPosition(),
								currentFloor.getHeight() - 1);
						craneAnimationTargetCoord.setGameCoord(currentCrane.getXPosition() - 1, currentCrane.getYPosition(),
								currentFloor.getHeight());
						hookAnimationTargetCoord.setGameCoord(currentCrane.getXPosition() - 1, currentCrane.getYPosition(),
								currentFloor.getHeight() - 1);
						currentCrane.setPosition(currentCrane.getXPosition() - 1, currentCrane.getYPosition());
					}
				}
				else
				{
					craneAnimationStartCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition(),
							currentFloor.getHeight());
					hookAnimationStartCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition(),
							currentFloor.getHeight() - 1);
					craneAnimationTargetCoord.setGameCoord(currentCrane.getXPosition() - 1, currentCrane.getYPosition(),
							currentFloor.getHeight());
					hookAnimationTargetCoord.setGameCoord(currentCrane.getXPosition() - 1, currentCrane.getYPosition(),
							currentFloor.getHeight() - 1);
					currentCrane.setPosition(currentCrane.getXPosition() - 1, currentCrane.getYPosition());
				}
			}
			else
			{
				if (currentCrane.isLoaded())
				{
					boxAnimationStartCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition(),
							currentFloor.getHeight() - 1);
					boxAnimationTargetCoord.setGameCoord(currentCrane.getXPosition() - 1, currentCrane.getYPosition(),
							currentFloor.getHeight() - 1);
				}
				craneAnimationStartCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition(), currentFloor.getHeight());
				hookAnimationStartCoord
						.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition(), currentFloor.getHeight() - 1);
				craneAnimationTargetCoord.setGameCoord(currentCrane.getXPosition() - 1, currentCrane.getYPosition(),
						currentFloor.getHeight());
				hookAnimationTargetCoord.setGameCoord(currentCrane.getXPosition() - 1, currentCrane.getYPosition(),
						currentFloor.getHeight() - 1);
				returnCode = -3;
			}
			break;
		case yForward: // y-Position vor
			if (currentCrane.getYPosition() < currentFloor.getLength() - 1)
			{
				if (currentCrane.isLoaded())
				{
					if (currentFloor.getBoxes(currentCrane.getXPosition(), currentCrane.getYPosition() + 1) == currentFloor.getHeight())
					{
						// Kranposition auf Zielposition, Vernichtung der aufgenommenen Box, Vernichtung der obersten Box an Zielposition
						boxAnimationStartCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition(),
								currentFloor.getHeight() - 1);
						craneAnimationStartCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition(),
								currentFloor.getHeight());
						hookAnimationStartCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition(),
								currentFloor.getHeight() - 1);
						boxAnimationTargetCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition() + 1,
								currentFloor.getHeight() - 1);
						craneAnimationTargetCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition() + 1,
								currentFloor.getHeight());
						hookAnimationTargetCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition() + 1,
								currentFloor.getHeight() - 1);
						currentCrane.setPosition(currentCrane.getXPosition(), currentCrane.getYPosition() + 1);
						currentFloor.setBoxes(currentCrane.getXPosition(), currentCrane.getYPosition(),
								currentFloor.getBoxes(currentCrane.getXPosition(), currentCrane.getYPosition()) - 1);
						currentCrane.toggleLoadState();
						returnCode = -6;
					}
					else
					{
						boxAnimationStartCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition(),
								currentFloor.getHeight() - 1);
						craneAnimationStartCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition(),
								currentFloor.getHeight());
						hookAnimationStartCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition(),
								currentFloor.getHeight() - 1);
						boxAnimationTargetCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition() + 1,
								currentFloor.getHeight() - 1);
						craneAnimationTargetCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition() + 1,
								currentFloor.getHeight());
						hookAnimationTargetCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition() + 1,
								currentFloor.getHeight() - 1);
						currentCrane.setPosition(currentCrane.getXPosition(), currentCrane.getYPosition() + 1);
					}
				}
				else
				{
					craneAnimationStartCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition(),
							currentFloor.getHeight());
					hookAnimationStartCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition(),
							currentFloor.getHeight() - 1);
					craneAnimationTargetCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition() + 1,
							currentFloor.getHeight());
					hookAnimationTargetCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition() + 1,
							currentFloor.getHeight() - 1);
					currentCrane.setPosition(currentCrane.getXPosition(), currentCrane.getYPosition() + 1);
				}
			}
			else
			{
				if (currentCrane.isLoaded())
				{
					boxAnimationStartCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition(),
							currentFloor.getHeight() - 1);
					boxAnimationTargetCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition() + 1,
							currentFloor.getHeight() - 1);
				}
				craneAnimationStartCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition(), currentFloor.getHeight());
				hookAnimationStartCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition() + 1,
						currentFloor.getHeight() - 1);
				craneAnimationTargetCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition(), currentFloor.getHeight());
				hookAnimationTargetCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition() + 1,
						currentFloor.getHeight() - 1);
				returnCode = -3;
			}
			break;
		case yBack: // y-Position zur�ck
			if (currentCrane.getYPosition() > 0)
			{
				if (currentCrane.isLoaded())
				{
					if (currentFloor.getBoxes(currentCrane.getXPosition(), currentCrane.getYPosition() - 1) == currentFloor.getHeight())
					{
						// Kranposition auf Zielposition, Vernichtung der aufgenommenen Box, Vernichtung der obersten Box an Zielposition
						boxAnimationStartCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition(),
								currentFloor.getHeight() - 1);
						craneAnimationStartCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition(),
								currentFloor.getHeight());
						hookAnimationStartCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition(),
								currentFloor.getHeight() - 1);
						boxAnimationTargetCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition() - 1,
								currentFloor.getHeight() - 1);
						craneAnimationTargetCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition() - 1,
								currentFloor.getHeight());
						hookAnimationTargetCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition() - 1,
								currentFloor.getHeight() - 1);
						currentCrane.setPosition(currentCrane.getXPosition(), currentCrane.getYPosition() - 1);
						currentFloor.setBoxes(currentCrane.getXPosition(), currentCrane.getYPosition(),
								currentFloor.getBoxes(currentCrane.getXPosition(), currentCrane.getYPosition()) - 1);
						currentCrane.toggleLoadState();
						returnCode = -6;
					}
					else
					{
						boxAnimationStartCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition(),
								currentFloor.getHeight() - 1);
						craneAnimationStartCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition(),
								currentFloor.getHeight());
						hookAnimationStartCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition(),
								currentFloor.getHeight() - 1);
						boxAnimationTargetCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition() - 1,
								currentFloor.getHeight() - 1);
						craneAnimationTargetCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition() - 1,
								currentFloor.getHeight());
						hookAnimationTargetCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition() - 1,
								currentFloor.getHeight() - 1);
						currentCrane.setPosition(currentCrane.getXPosition(), currentCrane.getYPosition() - 1);
					}
				}
				else
				{
					craneAnimationStartCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition(),
							currentFloor.getHeight());
					hookAnimationStartCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition(),
							currentFloor.getHeight() - 1);
					craneAnimationTargetCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition() - 1,
							currentFloor.getHeight());
					hookAnimationTargetCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition() - 1,
							currentFloor.getHeight() - 1);
					currentCrane.setPosition(currentCrane.getXPosition(), currentCrane.getYPosition() - 1);
				}
			}
			else
			{
				if (currentCrane.isLoaded())
				{
					boxAnimationStartCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition(),
							currentFloor.getHeight() - 1);
					boxAnimationTargetCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition() - 1,
							currentFloor.getHeight() - 1);
				}
				craneAnimationStartCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition(), currentFloor.getHeight());
				hookAnimationStartCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition() - 1,
						currentFloor.getHeight() - 1);
				craneAnimationTargetCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition(), currentFloor.getHeight());
				hookAnimationTargetCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition() - 1,
						currentFloor.getHeight() - 1);
				returnCode = -3;
			}
			break;
		case grab: // Portalkran aufnehmen/ablegen
			if (currentCrane.isLoaded())
			{
				// ablegen
				boxAnimationStartCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition(), currentFloor.getHeight() - 1);
				hookAnimationStartCoord
						.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition(), currentFloor.getHeight() - 1);
				boxAnimationTargetCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition(),
						currentFloor.getBoxes(currentCrane.getXPosition(), currentCrane.getYPosition()));
				hookAnimationTargetCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition(),
						currentFloor.getBoxes(currentCrane.getXPosition(), currentCrane.getYPosition()));
				currentFloor.setBoxes(currentCrane.getXPosition(), currentCrane.getYPosition(),
						currentFloor.getBoxes(currentCrane.getXPosition(), currentCrane.getYPosition()) + 1);
				currentCrane.toggleLoadState();
				returnCode = -4;
			}
			else
			{
				// aufnehmen
				if (currentFloor.getBoxes(currentCrane.getXPosition(), currentCrane.getYPosition()) > 0)
				{
					boxAnimationStartCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition(),
							currentFloor.getBoxes(currentCrane.getXPosition(), currentCrane.getYPosition()) - 1);
					hookAnimationStartCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition(),
							currentFloor.getHeight() - 1);
					boxAnimationTargetCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition(),
							currentFloor.getHeight() - 1);
					hookAnimationTargetCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition(),
							currentFloor.getBoxes(currentCrane.getXPosition(), currentCrane.getYPosition()) - 1);
					currentFloor.setBoxes(currentCrane.getXPosition(), currentCrane.getYPosition(),
							currentFloor.getBoxes(currentCrane.getXPosition(), currentCrane.getYPosition()) - 1);
					currentCrane.toggleLoadState();
					returnCode = -4;
				}
				else
				{
					hookAnimationStartCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition(),
							currentFloor.getHeight() - 1);
					hookAnimationTargetCoord.setGameCoord(currentCrane.getXPosition(), currentCrane.getYPosition(), 0);
					returnCode = -5;
				}
			}
			break;
		case Func1: // Funktion 1 aufrufen
			func1Stack = currentInstructionPool;
			func1InstructionPool.resetInstructionPointer();
			currentInstructionPool = PoolType.func1;
			returnCode = -7;
			break;
		case Func2: // Funktion 2 aufrufen
			func2Stack = currentInstructionPool;
			func2InstructionPool.resetInstructionPointer();
			currentInstructionPool = PoolType.func2;
			returnCode = -7;
			break;
		default:
			returnCode = -1;
		}
		currentInstruction = instructionCode;
		return returnCode;
	}

	/**
	 * Diese Methode f�hrt die n�chste Instruktion aus der Menge der Instruktionen aus. Sie aktualisiert die Objekte currentCrane (aktuelle
	 * Portalkranposition) und currentFloor (aktueller Zustand der Lagerfl�che)
	 * 
	 * @return 0 normaler Zug -1 Programmende erreicht (gameAccomplished true/false) -2 NOP-Code (runInstruction erneut aufrufen) -3 Randzug
	 *         -4 Aufnahme/Ablegen -5 leere Aufnahme -6 Zug l�st Kollision aus -7 Funktionsaufruf func1/func2 -8 Funktionsende func1/func2
	 *         erreicht
	 */
	public int runInstructionDesign()
	{
		int returnCode = 0;
		InstructionType instructionCode = InstructionType.nothing;
		switch (currentInstructionPool)
		{
		case main:
			instructionCode = mainInstructionPoolDesign.getNextInstruction();
			if (instructionCode == InstructionType.nothing)
			{
				// end of mainInstructionPool
				// we finished, compare currentFloor and targetFloor
				if (currentFloor.isIdenticalTo(targetFloor)) gameAccomplished = true;
				returnCode = -1;
			}
			break;
		case func1:
			instructionCode = func1InstructionPoolDesign.getNextInstruction();
			if (instructionCode == InstructionType.nothing)
			{
				// end of func1InstructionPool
				currentInstructionPool = func1Stack;
				returnCode = -8;
			}
			break;
		case func2:
			instructionCode = func2InstructionPoolDesign.getNextInstruction();
			if (instructionCode == InstructionType.nothing)
			{
				// end of func2InstructionPool
				currentInstructionPool = func2Stack;
				returnCode = -8;
			}
			break;
		default:
			returnCode = -1;
		}
		if (returnCode != 0) return returnCode;
		switch (instructionCode)
		{
		case Nop: // NOP
			returnCode = -2;
			break;
		case xForward: // x-Position vor
			if (currentCrane.getXPosition() < currentFloor.getWidth() - 1)
			{
				if (currentCrane.isLoaded())
				{
					if (currentFloor.getBoxes(currentCrane.getXPosition() + 1, currentCrane.getYPosition()) == currentFloor.getHeight())
					{
						// Kranposition auf Zielposition, Vernichtung der aufgenommenen Box, Vernichtung der obersten Box an Zielposition
						currentCrane.setPosition(currentCrane.getXPosition() + 1, currentCrane.getYPosition());
						currentFloor.setBoxes(currentCrane.getXPosition(), currentCrane.getYPosition(),
								currentFloor.getBoxes(currentCrane.getXPosition(), currentCrane.getYPosition()) - 1);
						currentCrane.toggleLoadState();
						returnCode = -6;
					}
					else
					{
						currentCrane.setPosition(currentCrane.getXPosition() + 1, currentCrane.getYPosition());
					}
				}
				else
				{
					currentCrane.setPosition(currentCrane.getXPosition() + 1, currentCrane.getYPosition());
				}
			}
			else
			{
				returnCode = -3;
			}
			break;
		case xBack: // x-Position zur�ck
			if (currentCrane.getXPosition() > 0)
			{
				if (currentCrane.isLoaded())
				{
					if (currentFloor.getBoxes(currentCrane.getXPosition() - 1, currentCrane.getYPosition()) == currentFloor.getHeight())
					{
						// Kranposition auf Zielposition, Vernichtung der aufgenommenen Box, Vernichtung der obersten Box an Zielposition
						currentCrane.setPosition(currentCrane.getXPosition() - 1, currentCrane.getYPosition());
						currentFloor.setBoxes(currentCrane.getXPosition(), currentCrane.getYPosition(),
								currentFloor.getBoxes(currentCrane.getXPosition(), currentCrane.getYPosition()) - 1);
						currentCrane.toggleLoadState();
						returnCode = -6;
					}
					else
					{
						currentCrane.setPosition(currentCrane.getXPosition() - 1, currentCrane.getYPosition());
					}
				}
				else
				{
					currentCrane.setPosition(currentCrane.getXPosition() - 1, currentCrane.getYPosition());
				}
			}
			else
			{
				returnCode = -3;
			}
			break;
		case yForward: // y-Position vor
			if (currentCrane.getYPosition() < currentFloor.getLength() - 1)
			{
				if (currentCrane.isLoaded())
				{
					if (currentFloor.getBoxes(currentCrane.getXPosition(), currentCrane.getYPosition() + 1) == currentFloor.getHeight())
					{
						// Kranposition auf Zielposition, Vernichtung der aufgenommenen Box, Vernichtung der obersten Box an Zielposition
						currentCrane.setPosition(currentCrane.getXPosition(), currentCrane.getYPosition() + 1);
						currentFloor.setBoxes(currentCrane.getXPosition(), currentCrane.getYPosition(),
								currentFloor.getBoxes(currentCrane.getXPosition(), currentCrane.getYPosition()) - 1);
						currentCrane.toggleLoadState();
						returnCode = -6;
					}
					else
					{
						currentCrane.setPosition(currentCrane.getXPosition(), currentCrane.getYPosition() + 1);
					}
				}
				else
				{
					currentCrane.setPosition(currentCrane.getXPosition(), currentCrane.getYPosition() + 1);
				}
			}
			else
			{
				returnCode = -3;
			}
			break;
		case yBack: // y-Position zur�ck
			if (currentCrane.getYPosition() > 0)
			{
				if (currentCrane.isLoaded())
				{
					if (currentFloor.getBoxes(currentCrane.getXPosition(), currentCrane.getYPosition() - 1) == currentFloor.getHeight())
					{
						// Kranposition auf Zielposition, Vernichtung der aufgenommenen Box, Vernichtung der obersten Box an Zielposition
						currentCrane.setPosition(currentCrane.getXPosition(), currentCrane.getYPosition() - 1);
						currentFloor.setBoxes(currentCrane.getXPosition(), currentCrane.getYPosition(),
								currentFloor.getBoxes(currentCrane.getXPosition(), currentCrane.getYPosition()) - 1);
						currentCrane.toggleLoadState();
						returnCode = -6;
					}
					else
					{
						currentCrane.setPosition(currentCrane.getXPosition(), currentCrane.getYPosition() - 1);
					}
				}
				else
				{
					currentCrane.setPosition(currentCrane.getXPosition(), currentCrane.getYPosition() - 1);
				}
			}
			else
			{
				returnCode = -3;
			}
			break;
		case grab: // Portalkran aufnehmen/ablegen
			if (currentCrane.isLoaded())
			{
				// ablegen
				currentFloor.setBoxes(currentCrane.getXPosition(), currentCrane.getYPosition(),
						currentFloor.getBoxes(currentCrane.getXPosition(), currentCrane.getYPosition()) + 1);
				currentCrane.toggleLoadState();
				returnCode = -4;
			}
			else
			{
				// aufnehmen
				if (currentFloor.getBoxes(currentCrane.getXPosition(), currentCrane.getYPosition()) > 0)
				{
					currentFloor.setBoxes(currentCrane.getXPosition(), currentCrane.getYPosition(),
							currentFloor.getBoxes(currentCrane.getXPosition(), currentCrane.getYPosition()) - 1);
					currentCrane.toggleLoadState();
					returnCode = -4;
				}
				else
				{
					// nichts aufzunehmen
					returnCode = -5;
				}
			}
			break;
		case Func1: // Funktion 1 aufrufen
			func1Stack = currentInstructionPool;
			func1InstructionPoolDesign.resetInstructionPointer();
			currentInstructionPool = PoolType.func1;
			returnCode = -7;
			break;
		case Func2: // Funktion 2 aufrufen
			func2Stack = currentInstructionPool;
			func2InstructionPoolDesign.resetInstructionPointer();
			currentInstructionPool = PoolType.func2;
			returnCode = -7;
			break;
		default:
			returnCode = -1;
		}
		currentInstruction = instructionCode;
		return returnCode;
	}

	/**
	 * makes this level free to play
	 */
	public void unlock()
	{
		mIsFreeToPlay = true;
	}

	/**
	 * test wether the gameset is solvable by design
	 */
	public boolean testGameSet()
	{
		int maxInstructions = 16 * 16 * 16;
		int cntInstructions = 0;
		int returnCode = 0;
		startGame();
		while ((returnCode != -1) && (cntInstructions <= maxInstructions))
		{
			returnCode = runInstructionDesign();
			switch (returnCode)
			{
			case 0: // normaler Zug
				cntInstructions++;
				break;
			case -1: // Programmende erreicht
				if (gameAccomplished()) return true;
				break;
			case -2: // NOP-Code
				cntInstructions++;
				break;
			case -3: // Randzug
				cntInstructions++;
				break;
			case -4: // Aufnahme/Ablegen
				cntInstructions++;
				break;
			case -5: // leere Aufnahme
				cntInstructions++;
				break;
			case -6: // Zug l�st Kollision aus
				cntInstructions++;
				break;
			case -7: // func1/func2 called
				cntInstructions++;
				break;
			case -8: // return from func1/func2
				break;
			default:
			}
		}
		return false;
	}

	public PoolType getCurrentInstructionPoolType()
	{
		return currentInstructionPool;
	}

	public GameInstructionPool getPool(PoolType poolType)
	{
		switch (poolType)
		{
		case main:
			return mainInstructionPool;
		case func1:
			return func1InstructionPool;
		case func2:
			return func2InstructionPool;
		}
		return null;
	}

	public int getInstructionIndex()
	{
		switch (currentInstructionPool)
		{
		case main:
			return mainInstructionPool.getInstructionPointer();
		case func1:
			return func1InstructionPool.getInstructionPointer();
		case func2:
			return func2InstructionPool.getInstructionPointer();
		}
		return -1;
	}
}
