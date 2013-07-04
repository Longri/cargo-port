package de.gdxgame;

/**
 * Die Klasse GameCoord beschreibt eine dreidimensionale Index-Koordinate.
 * 
 * @author Lars Streblow
 * @author Longri
 */
public class GameCoord
{
	private int mX = 0;
	private int mY = 0;
	private int mZ = 0;

	public GameCoord(int x, int y, int z)
	{
		this.mX = x;
		this.mY = y;
		this.mZ = z;
	}

	public GameCoord(GameCoord vector)
	{
		this.mX = vector.mX;
		this.mY = vector.mY;
		this.mZ = vector.mZ;
	}

	public GameCoord()
	{
		mX = -1;
		mY = -1;
		mZ = -1;
	}

	public void setGameCoord(int x, int y, int z)
	{
		this.mX = x;
		this.mY = y;
		this.mZ = z;
	}

	public void setNull()
	{
		mX = -1;
		mY = -1;
		mZ = -1;
	}

	public boolean isNull()
	{
		return (mX == -1 && mY == -1 && mZ == -1);
	}

	public int getX()
	{
		return mX;
	}

	public void setX(int x)
	{
		this.mX = x;
	}

	public int getY()
	{
		return mY;
	}

	public void setY(int y)
	{
		this.mY = y;
	}

	public int getZ()
	{
		return mZ;
	}

	public void setZ(int z)
	{
		this.mZ = z;
	}

}
