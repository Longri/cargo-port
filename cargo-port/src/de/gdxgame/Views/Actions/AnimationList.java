package de.gdxgame.Views.Actions;

import java.util.ArrayList;

public class AnimationList extends ArrayList<Animation> implements Animation
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean mIsplay = false;
	private ReadyHandler mReadyHandler;
	private int mAnimationCount;

	public void play()
	{
		play(null);
	}

	@Override
	public void play(ReadyHandler handler)
	{
		mAnimationCount = 0;
		mReadyHandler = handler;
		mIsplay = true;
		// call play() on all Animations
		for (Animation ani : this)
		{
			if (ani != null)
			{
				mAnimationCount++;
				ani.play(allReadyHandler);
			}
		}
		if (mAnimationCount == 0)
		{
			allReadyHandler.ready();
		}
	}

	ReadyHandler allReadyHandler = new ReadyHandler()
	{
		@Override
		public void ready()
		{
			mAnimationCount--;
			if (mAnimationCount <= 0)
			{
				mIsplay = false;
				if (mReadyHandler != null) mReadyHandler.ready();
			}
		}
	};

	public boolean isPlaying()
	{
		return mIsplay;
	}

	@Override
	public void calcPositions()
	{
		// call calcPositions() on all Animations
		for (Animation ani : this)
		{
			if (ani != null) ani.calcPositions();
		}
	}

	@Override
	public void stop()
	{
		// call stop() on all Animations
		for (Animation ani : this)
		{
			if (ani != null) ani.stop();
		}
	}

}
