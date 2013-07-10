package de.gdxgame;

import Enums.InstructionType;

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
	public GameFloor startFloor;
	public GameFloor targetFloor;
	public GameCrane startCrane;
	public GameFloor currentFloor;
	public GameCrane currentCrane;
	public GameInstructionPool mainInstructionPool;
	public GameInstructionPool func1InstructionPool;
	public GameInstructionPool func2InstructionPool;
	public GameCoord boxAnimationStartCoord;
	public GameCoord boxAnimationTargetCoord;
	public GameCoord craneAnimationStartCoord;
	public GameCoord craneAnimationTargetCoord;
	public GameCoord hookAnimationStartCoord;
	public GameCoord hookAnimationTargetCoord;
	public InstructionType currentInstruction;
	private int currentInstructionPool;
	private int func1Stack;
	private int func2Stack;
	private boolean gameAccomplished;

	public final static GameSet TestLevel = new GameSet(8, 6, 2)
	{
		protected void create()
		{
			super.create();
			this.mIsFreeToPlay = true;
			this.startFloor.setFloor(";;02020202;20202020");
			this.targetFloor.setFloor("11111111;;;;;11111111");
			this.startCrane.setPosition(0, 0);
			this.mainInstructionPool.setInstruction(0, 3);
			this.mainInstructionPool.setInstruction(1, 3);
			this.mainInstructionPool.setInstruction(2, 3);
			this.mainInstructionPool.setInstruction(3, 5);
			this.mainInstructionPool.setInstruction(4, 1);
			this.mainInstructionPool.setInstruction(5, 4); // booom!
			this.mainInstructionPool.setInstruction(6, 4);
			// this.mainInstructionPool.setInstruction(0, 6);
			// this.mainInstructionPool.setInstruction(1, 6);
			// this.mainInstructionPool.setInstruction(2, 6);
			// this.mainInstructionPool.setInstruction(3, 6);
			// this.mainInstructionPool.setInstruction(4, 7);
			// this.mainInstructionPool.setInstruction(5, 7);
			// this.mainInstructionPool.setInstruction(6, 7);
			// this.mainInstructionPool.setInstruction(7, 7);
			// this.func1InstructionPool.setInstruction(0, 3);
			// this.func1InstructionPool.setInstruction(1, 3);
			// this.func1InstructionPool.setInstruction(2, 3);
			// this.func1InstructionPool.setInstruction(3, 5);
			// this.func1InstructionPool.setInstruction(4, 3);
			// this.func1InstructionPool.setInstruction(5, 3);
			// this.func1InstructionPool.setInstruction(6, 5);
			// this.func1InstructionPool.setInstruction(7, 1);
			// this.func1InstructionPool.setInstruction(8, 4);
			// this.func1InstructionPool.setInstruction(9, 4);
			// this.func1InstructionPool.setInstruction(10, 4);
			// this.func1InstructionPool.setInstruction(11, 5);
			// this.func1InstructionPool.setInstruction(12, 4);
			// this.func1InstructionPool.setInstruction(13, 4);
			// this.func1InstructionPool.setInstruction(14, 5);
			// this.func1InstructionPool.setInstruction(15, 1);
			// this.func2InstructionPool.setInstruction(0, 3);
			// this.func2InstructionPool.setInstruction(1, 3);
			// this.func2InstructionPool.setInstruction(2, 5);
			// this.func2InstructionPool.setInstruction(3, 3);
			// this.func2InstructionPool.setInstruction(4, 3);
			// this.func2InstructionPool.setInstruction(5, 3);
			// this.func2InstructionPool.setInstruction(6, 5);
			// this.func2InstructionPool.setInstruction(7, 2);
			// this.func2InstructionPool.setInstruction(8, 4);
			// this.func2InstructionPool.setInstruction(9, 4);
			// this.func2InstructionPool.setInstruction(10, 5);
			// this.func2InstructionPool.setInstruction(11, 4);
			// this.func2InstructionPool.setInstruction(12, 4);
			// this.func2InstructionPool.setInstruction(13, 4);
			// this.func2InstructionPool.setInstruction(14, 5);
			// this.func2InstructionPool.setInstruction(15, 2);
		}

	};

	public GameSet(int x, int y, int z)
	{
		super();
		this.mLevelDimensions = new GameCoord(x, y, z);
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
		mainInstructionPool = new GameInstructionPool();
		func1InstructionPool = new GameInstructionPool();
		func2InstructionPool = new GameInstructionPool();
		boxAnimationStartCoord = new GameCoord();
		boxAnimationTargetCoord = new GameCoord();
		craneAnimationStartCoord = new GameCoord();
		craneAnimationTargetCoord = new GameCoord();
		hookAnimationStartCoord = new GameCoord();
		hookAnimationTargetCoord = new GameCoord();
		currentInstruction = InstructionType.Nop;
		currentInstructionPool = 0;
		func1Stack = 0;
		func2Stack = 0;
		gameAccomplished = false;
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
	public GameCoord getLeveDimensions()
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

	protected int mLevelNumber = -1;

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
		boxAnimationStartCoord.setNull();
		boxAnimationTargetCoord.setNull();
		craneAnimationStartCoord.setNull();
		craneAnimationTargetCoord.setNull();
		hookAnimationStartCoord.setNull();
		hookAnimationTargetCoord.setNull();
		currentInstruction = InstructionType.Nop;
		currentInstructionPool = 0;
		gameAccomplished = false;
	}

	/**
	 * Diese Methode f�hrt die n�chste Instruktion aus der Menge der Instruktionen aus. Sie aktualisiert die Objekte currentCrane (aktuelle
	 * Portalkranposition) und currentFloor (aktueller Zustand der Lagerfl�che)
	 * 
	 * @return 0 normaler Zug -1 Programmende erreicht (gameAccomplished true/false) -2 NOP-Code (runInstruction erneut aufrufen) -3 Randzug
	 *         -4 Aufnahme/Ablegen -5 leere Aufnahme -6 Zug l�st Kollision aus
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
		case 0:
			instructionCode = mainInstructionPool.getNextInstruction();
			if (instructionCode == InstructionType.nothing)
			{
				// end of mainInstructionPool
				// we finished, compare currentFloor and targetFloor
				if (currentFloor.isIdenticalTo(targetFloor)) gameAccomplished = true;
				returnCode = -1;
			}
			break;
		case 1:
			instructionCode = func1InstructionPool.getNextInstruction();
			if (instructionCode == InstructionType.nothing)
			{
				// end of func1InstructionPool
				currentInstructionPool = func1Stack;
				returnCode = -2;
			}
			break;
		case 2:
			instructionCode = func2InstructionPool.getNextInstruction();
			if (instructionCode == InstructionType.nothing)
			{
				// end of func2InstructionPool
				currentInstructionPool = func2Stack;
				returnCode = -2;
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
			currentInstructionPool = 1;
			func1InstructionPool.resetInstructionPointer();
			returnCode = -2;
			break;
		case Func2: // Funktion 2 aufrufen
			func2Stack = currentInstructionPool;
			func2InstructionPool.resetInstructionPointer();
			currentInstructionPool = 2;
			returnCode = -2;
			break;
		default:
			returnCode = -1;
		}
		currentInstruction = instructionCode;
		return returnCode;
	}

}
