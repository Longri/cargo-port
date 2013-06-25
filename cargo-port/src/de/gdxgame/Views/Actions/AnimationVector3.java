package de.gdxgame.Views.Actions;

import CB_Core.GL_UI.GL_Listener.GL;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

public class AnimationVector3 implements Animation
{
	private ModelInstance mModelInstance;
	private Vector3 mStart, mEnd, mAct;
	private int mDuration;

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
	public void render3d(ModelBatch modelBatch)
	{
		if (xDiv != 0) mAct.x = mStart.x + (((GL.that.getStateTime() * 1000) % this.mDuration) / (this.mDuration / xDiv));
		if (yDiv != 0) mAct.y = mStart.y + (((GL.that.getStateTime() * 1000) % this.mDuration) / (this.mDuration / yDiv));
		if (zDiv != 0) mAct.z = mStart.z + (((GL.that.getStateTime() * 1000) % this.mDuration) / (this.mDuration / zDiv));

		mModelInstance.transform.setToTranslation(mAct);

	}
}
