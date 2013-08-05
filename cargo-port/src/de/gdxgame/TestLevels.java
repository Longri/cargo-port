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
			this.mainInstructionPool.setInstructionPool("66667777");
			this.func1InstructionPool.setInstructionPool("3335335144454451");
			this.func2InstructionPool.setInstructionPool("3353335244544452");
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
			this.targetFloor.setFloor(";;01010101;10101010");
			this.startCrane.setPosition(0, 0);
			this.mainInstructionPool.setInstructionPool("666");
			this.func1InstructionPool.setInstructionPool("3335331444441");
			this.mainInstructionPoolDesign.setInstructionPool("6666");
			this.func1InstructionPoolDesign.setInstructionPool("3335331444441");

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
			this.mainInstructionPoolDesign.setInstructionPool("6666");
			this.func1InstructionPoolDesign.setInstructionPool("3335331444441");
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
