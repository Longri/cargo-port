package de.gdxgame;

/**
 * Die Klasse GameCrane beschreibt den Zustand des Portalkrans.
 * 
 * @author Lars Streblow
 */
public class GameCrane
{
	private int xPosition;
	private int yPosition;
	private boolean isLoaded;

	public GameCrane()
	{
		xPosition = 0;
		yPosition = 0;
		isLoaded = false;
	}

	public GameCrane(int x, int y)
	{
		if (x < 0 || y < 0)
		{
			xPosition = 0;
			yPosition = 0;
		}
		else
		{
			xPosition = x;
			yPosition = y;
		}
		isLoaded = false;
	}

	public GameCrane(GameCrane crane)
	{
		xPosition = crane.xPosition;
		yPosition = crane.yPosition;
		isLoaded = crane.isLoaded;
	}

	public void setPosition(int x, int y)
	{
		xPosition = x;
		yPosition = y;
	}

	public int getXPosition()
	{
		return xPosition;
	}

	public int getYPosition()
	{
		return yPosition;
	}

	public boolean isLoaded()
	{
		return isLoaded;
	}

	public void toggleLoadState()
	{
		isLoaded = !isLoaded;
	}
}
