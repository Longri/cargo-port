package de.gdxgame.Views.Actions;

public interface Animation<t>
{
	public void calcPositions();

	public void play();

	public void play(ReadyHandler handler);

	public void stop();

	public boolean isPlaying();

	public void setAnimationCallBack(AnimationCallBack<t> callBack);
}
