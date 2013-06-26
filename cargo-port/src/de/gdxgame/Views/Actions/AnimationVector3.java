package de.gdxgame.Views.Actions;

import CB_Core.GL_UI.GL_Listener.GL;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

public class AnimationVector3 implements Animation
{
	private ModelInstance mModelInstance;
	private Vector3 mStart, mEnd, mAct;
	private int mDuration;
	private ReadyHandler mReadyHandler;

	private boolean mPlay = false;
	private boolean mstop = false;

	private float mStatrTime = 0;
	private float mTargetTime = 0;

	private float xDiv;
	private float yDiv;
	private float zDiv;

	public AnimationVector3(ModelInstance model, Vector3 start, Vector3 end, int duration)
	{
		mModelInstance = model;
		mStart = new Vector3(start);
		mEnd = new Vector3(end);
		mAct = new Vector3(start);
		mDuration = duration;
		xDiv = mEnd.x - mStart.x;
		yDiv = mEnd.y - mStart.y;
		zDiv = mEnd.z - mStart.z;
	}

	@Override
	public void calcPositions()
	{
		if (mPlay && !mstop)
		{
			float actTime = (GL.that.getStateTime() * 1000) - mStatrTime;

			if (xDiv != 0) mAct.x = mStart.x + ((actTime % this.mDuration) / (this.mDuration / xDiv));
			if (yDiv != 0) mAct.y = mStart.y + ((actTime % this.mDuration) / (this.mDuration / yDiv));
			if (zDiv != 0) mAct.z = mStart.z + ((actTime % this.mDuration) / (this.mDuration / zDiv));

			// chk Ready
			if (actTime + mStatrTime >= mTargetTime)
			{
				stop();
				if (mReadyHandler != null) mReadyHandler.ready();
			}
			else
			{
				mModelInstance.transform.setToTranslation(mAct);
			}

		}

	}

	@Override
	public void play()
	{
		play(null);
	}

	@Override
	public void play(ReadyHandler handler)
	{
		mReadyHandler = handler;
		mStatrTime = GL.that.getStateTime() * 1000;
		mTargetTime = mStatrTime + mDuration;
		mPlay = true;
		mstop = false;
	}

	@Override
	public void stop()
	{
		mstop = true;
	}

	@Override
	public boolean isPlaying()
	{
		return mPlay;
	}

}