package de.gdxgame.Views.Actions;

public interface Animation
{
	public void calcPositions();

	public void play();

	public void play(ReadyHandler handler);

	public void stop();

	public boolean isPlaying();

}
