package de.gdxgame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import CB_Core.Util.MoveableList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public final class TestLevels
{

	public TestLevels()
	{
		Levels = getLevels();
	}

	private final static GameSet TestLevel1 = new GameSet(8, 6, 2)
	{

		@Override
		protected void create()
		{
			super.create();
			this.mIsFreeToPlay = true;
			this.mLevelNumber = -1;
			this.startFloor.setFloor("//02020202/20202020");
			this.targetFloor.setFloor("11111111/////11111111");
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
			this.startFloor.setFloor("//02020202/20202020");
			this.targetFloor.setFloor("//01010101/10101010");
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
			this.startFloor.setFloor("//02020202/20202020");
			this.targetFloor.setFloor("//01010101/10101010");
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

	public static void loadLevels()
	{
		int levelnumber = 0;
		String dimensions = "";
		String floor1 = "";
		String floor2 = "";
		String crane = "";
		String main = "";
		String func1 = "";
		String func2 = "";
		String maindesign = "";
		String func1design = "";
		String func2design = "";
		boolean solved = false;
		boolean freetoplay = true;
		String temp;
		String temparray[];
		int k = 8;
		FileHandle fileHandle = Gdx.files.classpath("standard.lvl");
		InputStream inputStream = fileHandle.read();
		String line = "";
		try
		{
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			while ((line = bufferedReader.readLine()) != null)
			{
				line = line.trim();
				line = line.toLowerCase();
				if (line.charAt(0) == ';') continue;
				temparray = line.split(";");
				line = temparray[0];
				line = line.trim();
				if (line.startsWith("[level"))
				{
					if (k != 8) // last level not complete, abort this level
					{
						k = 8;
						continue;
					}
					temp = line.substring(6);
					temparray = temp.split("]");
					temparray[0] = temparray[0].trim();
					if (!temparray[0].isEmpty())
					{
						levelnumber = Integer.parseInt(temparray[0].trim());
						k = 0;
						continue;
					}
					else
					// this level has no ID, abort this level and read until next level
					{
						k = 8;
						// ###todo read to next "["
						continue;
					}
				}
				switch (k)
				{
				case 0: // we've read levelnumber, now read dimensions x/y/z
					dimensions = line;
					k++;
					break;
				case 1: // we've read dimensions, now read startfloor
					floor1 = line;
					k++;
					break;
				case 2: // we've read startfloor, now read targetfloor
					floor2 = line;
					k++;
					break;
				case 3: // we've read targetfloor, now read crane
					crane = line;
					k++;
					break;
				case 4: // we've read crane, now read designer's solution
					temp = line;
					temparray = temp.split("/");
					maindesign = temparray[0];
					func1design = temparray[1];
					func2design = temparray[2];
					k++;
					break;
				case 5: // we've read designer's solution, now read player's solution
					temp = line;
					temparray = temp.split("/");
					main = temparray[0];
					func1 = temparray[1];
					func2 = temparray[2];
					k++;
					break;
				case 6: // we've read player's solution, now read wether level is solved
					temp = line;
					if (Integer.parseInt(temp) == 0)
					;
					else
						;
					k++;
					break;
				case 7:
					GameSet level;
					level = new GameSet(dimensions, floor1, floor2, crane, main, func1, func2, maindesign, func1design, func2design);
					level.mLevelNumber = levelnumber;
					level.mIsFreeToPlay = false;
					if (freetoplay)
					{
						level.mIsFreeToPlay = true;
						freetoplay = solved;
					}
					Levels.add(level);
					k++;
					break;
				default:
					break;
				}
			}
		}
		catch (IOException e)
		{
			System.err.println("Error: " + e);
		}
	}
}
