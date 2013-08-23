package de.gdxgame;

import CB_UI_Base.GL_UI.Main.MainViewBase;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.InputProcessor;

import de.efects.redFireWorks;

public class GdxGame extends CB_UI_Base.GL_UI.GL_Listener.GL implements ApplicationListener, InputProcessor
{

	public GdxGame(int initalWidth, int initialHeight, MainViewBase splash, MainViewBase mainView)
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
