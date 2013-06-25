package de.gdxgame.Views.Actions;

import java.util.ArrayList;

public class AnimationList extends ArrayList<Animation>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean mIsplay = false;

	public void play()
	{
		mIsplay = true;
	}

	public boolean isPlaying()
	{
		return mIsplay;
	}

}
