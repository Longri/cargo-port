package de.gdxgame;

/**
 * structur eines 3 dimensionalen Vectors aus int Werten
 * 
 * @author Longri
 */
public class IntVector3
{
	private int mX = 0;
	private int mY = 0;
	private int mZ = 0;

	public IntVector3(int x, int y, int z)
	{
		this.mX = x;
		this.mY = y;
		this.mZ = z;
	}

	public IntVector3(IntVector3 vector)
	{
		this.mX = vector.mX;
		this.mY = vector.mY;
		this.mZ = vector.mZ;
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
