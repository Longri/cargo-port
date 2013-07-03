package de.gdxgame;

/**
 * Enth�llt alle Infos eines Game Levels<br>
 * * getLeveDimensions() gibt die Abmessungen dieses Levels zur�ck <br>
 * 
 * @author Longri
 */
public abstract class Level
{

	public final static Level TestLevel = new Level(8, 8, 4)
	{

		@Override
		public void create()
		{
			this.mIsFreeToPlay = true;

		}

	};

	public Level(int x, int y, int z)
	{
		this.mLevelDemensions = new IntVector3(x, y, z);

		create();
	}

	public abstract void create();

	/**
	 * Enth�lt die drei Dimensionen des Spielfelds. <br>
	 */
	private IntVector3 mLevelDemensions;

	/**
	 * gibt die drei Dimensionen des Spielfelds zur�ck. <br>
	 * 
	 * @return IntVector3
	 */
	public IntVector3 getLeveDimensions()
	{
		if (mLevelDemensions == null) mLevelDemensions = new IntVector3(0, 0, 0);
		return mLevelDemensions;
	}

	protected boolean mIsFreeToPlay = false;

	/**
	 * Gibt TRUE zur�ck, wenn das Level zum Spielen freigeschalten ist.
	 * 
	 * @return boolean
	 */
	public boolean getIsFreeToPlay()
	{
		return mIsFreeToPlay;
	}

	protected int mLevelNumber = -1;

	/**
	 * Gibt die Level Nummer zur�ck
	 * 
	 * @return int
	 */
	public int getLevelNumber()
	{
		return mLevelNumber;
	}
}
