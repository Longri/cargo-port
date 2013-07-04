package de.gdxgame;

/**
 * Die Klasse GameFloor beschreibt den Zustand der Lagerfläche.
 * 
 * @author Lars Streblow
 */
public class GameFloor
{
	private int gameWidth; // x-Ausdehnung
	private int gameLength; // y-Ausdehnung
	private int gameHeight; // z-Ausdehnung

	public int[][] floorBoxes;

	public GameFloor()
	{
		gameWidth = 0;
		gameLength = 0;
		gameHeight = 0;
		floorBoxes = null;
	}

	public GameFloor(int width, int length, int height)
	{
		if (width <= 0 || length <= 0 || height <= 0)
		{
			gameWidth = 0;
			gameLength = 0;
			gameHeight = 0;
			floorBoxes = null;
		}
		else
		{
			gameWidth = width;
			gameLength = length;
			gameHeight = height;
			floorBoxes = new int[width][length];
			for (int i = 0; i < width; i++)
			{
				for (int j = 0; j < length; j++)
				{
					floorBoxes[i][j] = 0;
				}
			}
		}
	}

	public GameFloor(GameFloor floor)
	{
		gameWidth = floor.gameWidth;
		gameLength = floor.gameLength;
		gameHeight = floor.gameHeight;
		floorBoxes = new int[gameWidth][gameLength];
		for (int i = 0; i < gameWidth; i++)
		{
			for (int j = 0; j < gameLength; j++)
			{
				floorBoxes[i][j] = floor.floorBoxes[i][j];
			}
		}
	}

	public boolean isIdenticalTo(GameFloor floor)
	{
		if (this.gameWidth != floor.gameWidth) return false;
		if (this.gameLength != floor.gameLength) return false;
		if (this.gameHeight != floor.gameHeight) return false;
		for (int i = 0; i < this.gameWidth; i++)
		{
			for (int j = 0; j < this.gameLength; j++)
			{
				if (this.floorBoxes[i][j] != floor.floorBoxes[i][j]) return false;
			}
		}
		return true;
	}

	public void setBoxes(int x, int y, int n)
	{
		if (0 <= x && x < gameWidth && 0 <= y && y < gameLength && 0 <= n && n <= gameHeight)
		{
			floorBoxes[x][y] = n;
		}
	}

	public int getBoxes(int x, int y)
	{
		if (0 <= x && x < gameWidth && 0 <= y && y < gameLength)
		{
			return floorBoxes[x][y];
		}
		else
			return -1;
	}

	public int getWidth()
	{
		return gameWidth;
	}

	public int getLength()
	{
		return gameLength;
	}

	public int getHeight()
	{
		return gameHeight;
	}

	public void setFloor(String values)
	{
		int i = 0;
		int j = 0;
		int k = 0;
		for (i = 0; i < gameWidth; i++)
		{
			for (j = 0; j < gameLength; j++)
			{
				floorBoxes[i][j] = 0;
			}
		}
		i = 0;
		j = 0;
		while (k < values.length())
		{
			if (values.charAt(k) == ';')
			{
				i++;
				if (i >= gameWidth) return;
				j = 0;
			}
			else if ('0' <= values.charAt(k) && values.charAt(k) <= '9')
			{
				if (j < gameLength)
				{
					floorBoxes[i][j] = Character.getNumericValue(values.charAt(k));
					if (floorBoxes[i][j] > gameHeight) gameHeight = floorBoxes[i][j];
					j++;
				}
			}
			k++;
		}
	}
}
