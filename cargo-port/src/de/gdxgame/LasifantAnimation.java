package de.gdxgame;

import CB_UI_Base.GL_UI.Controls.Animation.AnimationBase;
import CB_UI_Base.GL_UI.Controls.Animation.FrameAnimation;
import CB_UI_Base.Math.CB_RectF;

public class LasifantAnimation extends FrameAnimation
{

	public LasifantAnimation(CB_RectF rec, String Name)
	{
		super(rec, Name);
	}

	@Override
	public void play()
	{
		play(800);
	}

	@Override
	public AnimationBase INSTANCE()
	{
		return null;
	}

	@Override
	public AnimationBase INSTANCE(CB_RectF rec)
	{
		return null;
	}

}
