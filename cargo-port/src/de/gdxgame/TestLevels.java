package de.gdxgame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import CB_Utils.Util.MoveableList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class TestLevels
{

	public MoveableList<GameSet> Levels;

	public TestLevels()
	{
		Levels = new MoveableList<GameSet>();
		loadLevels();
		// writeSavedGame();
		// readSavedGame();
	}

	public void loadLevels()
	{
		boolean ignorelines = true;
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
		int k = 7;
		if (Levels != null) Levels.clear();
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
				if (line.isEmpty()) continue;
				if (line.charAt(0) == ';') continue;
				temparray = line.split(";"); // ignore comments at the end of the line
				line = temparray[0];
				line = line.trim();
				if (line.startsWith("[level"))
				{
					ignorelines = false;
					if (k != 7) // new level starts, but last level is not complete, abort and ignore that level
					{
						k = 7;
					}
					temp = line.substring("[level".length());
					temparray = temp.split("]", -1);
					temparray[0] = temparray[0].trim();
					if (!temparray[0].isEmpty())
					{
						levelnumber = Integer.parseInt(temparray[0].trim());
						k = 0;
						continue;
					}
					else
					// this level has no ID, abort and ignore this level and ignore lines until next level
					{
						k = 7;
						ignorelines = true;
						continue;
					}
				}
				else if (ignorelines) continue;
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
					temparray = temp.split("/", -1);
					maindesign = temparray[0];
					func1design = temparray[1];
					func2design = temparray[2];
					k++;
					break;
				case 5: // we've read designer's solution, now read player's solution
					temp = line;
					temparray = temp.split("/", -1);
					main = temparray[0];
					func1 = temparray[1];
					func2 = temparray[2];
					k++;
					break;
				case 6: // we've read player's solution, now read wether level is solved
					temp = line;
					if (Integer.parseInt(temp) == 0) solved = false;
					else
						solved = true;
					// all read, let's create the GameSet if it is solvable
					GameSet level;
					level = new GameSet(dimensions, floor1, floor2, crane, main, func1, func2, maindesign, func1design, func2design);
					if (level.testGameSet())
					{
						level.mLevelNumber = levelnumber;
						level.mIsFreeToPlay = false;
						if (freetoplay)
						{
							level.mIsFreeToPlay = true;
							freetoplay = solved;
						}
						Levels.add(level);
					}
					k++;
					ignorelines = true;
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

	public void readSavedGame()
	{
		boolean ignorelines = true;
		int levelnumber = 0;
		String dimensions = "";
		String floor1 = "";
		String floor2 = "";
		String crane = "";
		String main = "";
		String func1 = "";
		String func2 = "";
		boolean freetoplay = true;
		String temp;
		String temparray[];
		int k = 6;
		Levels.clear();
		String FilePath = Gdx.files.getLocalStoragePath() + "./.cargoport/saved.lvl";
		FileHandle fileHandle = Gdx.files.absolute(FilePath);
		InputStream inputStream = fileHandle.read();
		String line = "";
		try
		{
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			while ((line = bufferedReader.readLine()) != null)
			{
				line = line.trim();
				line = line.toLowerCase();
				if (line.isEmpty()) continue;
				if (line.charAt(0) == ';') continue;
				temparray = line.split(";"); // ignore comments at the end of the line
				line = temparray[0];
				line = line.trim();
				if (line.startsWith("[level"))
				{
					ignorelines = false;
					if (k != 6) // new level starts, but last level is not complete, abort and ignore that level
					{
						k = 6;
					}
					temp = line.substring("[level".length());
					temparray = temp.split("]", -1);
					temparray[0] = temparray[0].trim();
					if (!temparray[0].isEmpty())
					{
						levelnumber = Integer.parseInt(temparray[0].trim());
						k = 0;
						continue;
					}
					else
					// this level has no ID, abort and ignore this level and ignore lines until next level
					{
						k = 6;
						ignorelines = true;
						continue;
					}
				}
				else if (ignorelines) continue;
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
				case 4: // // we've read crane, now read player's solution
					temp = line;
					temparray = temp.split("/", -1);
					main = temparray[0];
					func1 = temparray[1];
					func2 = temparray[2];
					k++;
					break;
				case 5: // we've read player's solution, now read wether level is solved
					temp = line;
					if (Integer.parseInt(temp) == 0) freetoplay = false;
					else
						freetoplay = true;
					// all read, let's create the GameSet if it is solvable
					GameSet level;
					level = new GameSet(dimensions, floor1, floor2, crane, main, func1, func2, "", "", "");
					level.mLevelNumber = levelnumber;
					level.mIsFreeToPlay = freetoplay;
					Levels.add(level);
					k++;
					ignorelines = true;
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

	public void writeSavedGame()
	{
		int x;
		int y;
		int i;
		int k;
		GameSet gs;
		String FilePath = Gdx.files.getLocalStoragePath() + "./.cargoport/saved.lvl";
		FileHandle fileHandle = Gdx.files.absolute(FilePath);
		fileHandle.writeString(";saved.lvl\n", false);
		for (k = 0; k < Levels.size(); k++)
		{
			gs = Levels.get(k);
			fileHandle.writeString("[level " + Integer.toString(gs.getLevelNumber()) + "]\n", true);
			fileHandle.writeString(Integer.toString(gs.getLevelDimensions().getX()) + "/", true);
			fileHandle.writeString(Integer.toString(gs.getLevelDimensions().getY()) + "/", true);
			fileHandle.writeString(Integer.toString(gs.getLevelDimensions().getZ()) + "\n", true);
			for (y = 0; y < gs.getLevelDimensions().getY(); y++)
			{
				for (x = 0; x < gs.getLevelDimensions().getX(); x++)
					fileHandle.writeString(Integer.toString(gs.startFloor.floorBoxes[x][y]), true);
				fileHandle.writeString("/", true);
			}
			fileHandle.writeString("\n", true);
			for (y = 0; y < gs.getLevelDimensions().getY(); y++)
			{
				for (x = 0; x < gs.getLevelDimensions().getX(); x++)
					fileHandle.writeString(Integer.toString(gs.targetFloor.floorBoxes[x][y]), true);
				fileHandle.writeString("/", true);
			}
			fileHandle.writeString("\n", true);
			fileHandle.writeString(Integer.toString(gs.startCrane.getXPosition()) + "/", true);
			fileHandle.writeString(Integer.toString(gs.startCrane.getYPosition()) + "\n", true);
			for (i = 0; i < 16; i++)
			{
				fileHandle.writeString(Integer.toString(gs.mainInstructionPool.get(i).ordinal()), true);

			}
			fileHandle.writeString("/", true);
			for (i = 0; i < 16; i++)
			{
				fileHandle.writeString(Integer.toString(gs.func1InstructionPool.get(i).ordinal()), true);

			}
			fileHandle.writeString("/", true);
			for (i = 0; i < 16; i++)
			{
				fileHandle.writeString(Integer.toString(gs.func2InstructionPool.get(i).ordinal()), true);

			}
			fileHandle.writeString("\n", true);
			if (gs.getIsFreeToPlay()) fileHandle.writeString("1\n", true);
			else
				fileHandle.writeString("0\n", true);
		}

	}
}
