package de.gdxgame;

import CB_Core.GL_UI.Main.TabMainView;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.InputProcessor;

import de.efects.redFireWorks;

public class GdxGame extends CB_Core.GL_UI.GL_Listener.GL implements ApplicationListener, InputProcessor
{

	public GdxGame(int initalWidth, int initialHeight, TabMainView splash, TabMainView mainView)
	{
		super(initalWidth, initialHeight, splash, mainView);

	}

	@Override
	public void create()
	{
		super.create();
	}

	@Override
	public void Initialize()
	{
		super.Initialize();
	}

	redFireWorks fireW;

	public void showFireWork()
	{
		if (fireW == null) fireW = new redFireWorks();
		mDialog.addChildDirekt(fireW);
	}

	public void disposeFireWork()
	{
		fireW.dispose();
		fireW = null;

	}

}
