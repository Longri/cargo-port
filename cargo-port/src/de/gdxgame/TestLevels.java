package de.gdxgame;

import CB_Core.Util.MoveableList;

public class TestLevels
{

	private final static GameSet TestLevel1 = new GameSet(8, 6, 2)
	{
		protected void create()
		{
			super.create();
			this.mIsFreeToPlay = true;
			this.mLevelNumber = -1;
			this.startFloor.setFloor(";;02020202;20202020");
			this.targetFloor.setFloor("11111111;;;;;11111111");
			this.startCrane.setPosition(0, 0);
			this.mainInstructionPool.setInstruction(0, 3);
			this.mainInstructionPool.setInstruction(1, 3);
			this.mainInstructionPool.setInstruction(2, 3);
			this.mainInstructionPool.setInstruction(3, 5);
			this.mainInstructionPool.setInstruction(0, 6);
			this.mainInstructionPool.setInstruction(1, 6);
			this.mainInstructionPool.setInstruction(2, 6);
			this.mainInstructionPool.setInstruction(3, 6);
			this.mainInstructionPool.setInstruction(4, 7);
			this.mainInstructionPool.setInstruction(5, 7);
			this.mainInstructionPool.setInstruction(6, 7);
			this.mainInstructionPool.setInstruction(7, 7);
			this.func1InstructionPool.setInstruction(0, 3);
			this.func1InstructionPool.setInstruction(1, 3);
			this.func1InstructionPool.setInstruction(2, 3);
			this.func1InstructionPool.setInstruction(3, 5);
			this.func1InstructionPool.setInstruction(4, 3);
			this.func1InstructionPool.setInstruction(5, 3);
			this.func1InstructionPool.setInstruction(6, 5);
			this.func1InstructionPool.setInstruction(7, 1);
			this.func1InstructionPool.setInstruction(8, 4);
			this.func1InstructionPool.setInstruction(9, 4);
			this.func1InstructionPool.setInstruction(10, 4);
			this.func1InstructionPool.setInstruction(11, 5);
			this.func1InstructionPool.setInstruction(12, 4);
			this.func1InstructionPool.setInstruction(13, 4);
			this.func1InstructionPool.setInstruction(14, 5);
			this.func1InstructionPool.setInstruction(15, 1);
			this.func2InstructionPool.setInstruction(0, 3);
			this.func2InstructionPool.setInstruction(1, 3);
			this.func2InstructionPool.setInstruction(2, 5);
			this.func2InstructionPool.setInstruction(3, 3);
			this.func2InstructionPool.setInstruction(4, 3);
			this.func2InstructionPool.setInstruction(5, 3);
			this.func2InstructionPool.setInstruction(6, 5);
			this.func2InstructionPool.setInstruction(7, 2);
			this.func2InstructionPool.setInstruction(8, 4);
			this.func2InstructionPool.setInstruction(9, 4);
			this.func2InstructionPool.setInstruction(10, 5);
			this.func2InstructionPool.setInstruction(11, 4);
			this.func2InstructionPool.setInstruction(12, 4);
			this.func2InstructionPool.setInstruction(13, 4);
			this.func2InstructionPool.setInstruction(14, 5);
			this.func2InstructionPool.setInstruction(15, 2);
		}
	};

	private final static GameSet TestLevel2 = new GameSet(8, 6, 2)
	{
		protected void create()
		{
			super.create();
			this.mIsFreeToPlay = true;
			this.mLevelNumber = -2;
			this.startFloor.setFloor(";;02020202;20202020");
			this.targetFloor.setFloor("11111111;;;;;11111111");
			this.startCrane.setPosition(0, 0);
			this.mainInstructionPool.setInstruction(0, 3);
			this.mainInstructionPool.setInstruction(1, 3);
			this.mainInstructionPool.setInstruction(2, 3);
			this.mainInstructionPool.setInstruction(3, 5);
			this.mainInstructionPool.setInstruction(4, 3);
			this.mainInstructionPool.setInstruction(5, 3);
			this.mainInstructionPool.setInstruction(6, 1);
			this.mainInstructionPool.setInstruction(7, 4);
			this.mainInstructionPool.setInstruction(8, 4);
			this.mainInstructionPool.setInstruction(9, 4); // booom!
			this.mainInstructionPool.setInstruction(10, 4);
			this.mainInstructionPool.setInstruction(11, 4);
			this.mainInstructionPool.setInstruction(12, 2);

		}

	};

	private final static GameSet TestLevel3 = new GameSet(8, 6, 2)
	{
		protected void create()
		{
			super.create();
			this.mIsFreeToPlay = true;
			this.mLevelNumber = -3;
			this.startFloor.setFloor(";;02020202;20202020");
			this.targetFloor.setFloor(";;01010101;10101010");
			this.startCrane.setPosition(0, 0);
			this.mainInstructionPool.setInstructionPool("6666");
			this.func1InstructionPool.setInstructionPool("3335331444441");
			this.startCrane.setPosition(0, 0);

		}

	};

	public static MoveableList<GameSet> Levels = getLevels();

	public static MoveableList<GameSet> getLevels()
	{
		MoveableList<GameSet> levels = new MoveableList<GameSet>();

		TestLevel1.mIsFreeToPlay = true;
		levels.add(TestLevel1);

		TestLevel2.mIsFreeToPlay = false;
		levels.add(TestLevel2);

		TestLevel3.mIsFreeToPlay = false;
		levels.add(TestLevel3);
		return levels;
	}

	public void loadLevels()
	{
		// TODO load
	}

}
