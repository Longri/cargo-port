package de.gdxgame;

/**
 * Enthällt alle Infos eines Game Levels<br>
 * * getLeveDimensions() gibt die Abmessungen dieses Levels zurück <br>
 * 
 * @author Longri
 */
public abstract class Level
{

	public final static Level TestLevel = new Level(6, 6, 6)
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
	 * Enthält die drei Dimensionen des Spielfelds. <br>
	 */
	private IntVector3 mLevelDemensions;

	/**
	 * gibt die drei Dimensionen des Spielfelds zurück. <br>
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
	 * Gibt TRUE zurück, wenn das Level zum Spielen freigeschalten ist.
	 * 
	 * @return boolean
	 */
	public boolean getIsFreeToPlay()
	{
		return mIsFreeToPlay;
	}

	protected int mLevelNumber = -1;

	/**
	 * Gibt die Level Nummer zurück
	 * 
	 * @return int
	 */
	public int getLevelNumber()
	{
		return mLevelNumber;
	}
}
