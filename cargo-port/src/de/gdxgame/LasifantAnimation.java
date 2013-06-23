package de.gdxgame;

import CB_Core.GL_UI.Controls.Animation.AnimationBase;
import CB_Core.GL_UI.Controls.Animation.FrameAnimation;
import CB_Core.Math.CB_RectF;

public class LasifantAnimation extends FrameAnimation
{

	public LasifantAnimation(CB_RectF rec, String Name)
	{
		super(rec, Name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void play()
	{
		play(800);
	}

	@Override
	public AnimationBase INSTANCE()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AnimationBase INSTANCE(CB_RectF rec)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
