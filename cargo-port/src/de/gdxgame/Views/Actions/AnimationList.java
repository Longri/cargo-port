package de.gdxgame.Views.Actions;

import com.badlogic.gdx.math.Vector3;

import de.gdxgame.ThreadSafeList;

public class AnimationList extends ThreadSafeList<Animation<Vector3>> implements Animation<Vector3>
{

	public AnimationList()
	{
		super();

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean mIsplay = false;
	private ReadyHandler mReadyHandler;
	private int mAnimationCount;
	protected AnimationCallBack<Vector3> mAnimationCallBack;

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
		for (Animation<Vector3> ani : this)
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
		for (Animation<Vector3> ani : this)
		{
			if (ani != null) ani.calcPositions();
		}
	}

	@Override
	public void stop()
	{
		// call stop() on all Animations
		for (Animation<Vector3> ani : this)
		{
			if (ani != null) ani.stop();
		}
	}

	@Override
	public void setAnimationCallBack(AnimationCallBack<Vector3> callBack)
	{
		mAnimationCallBack = callBack;
	}

}
