package de.gdxgame;

/**
 * structur eines 3 dimensionalen Vectors aus int Werten
 * 
 * @author Longri
 */
public class IntVector3
{
	private int x = 0;
	private int y = 0;
	private int z = 0;

	public IntVector3(int x, int y, int z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public IntVector3(IntVector3 vector)
	{
		this.x = vector.x;
		this.y = vector.y;
		this.z = vector.z;
	}

}
