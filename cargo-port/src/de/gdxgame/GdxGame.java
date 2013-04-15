package de.gdxgame;

import CB_Core.Config;
import CB_Core.Energy;
import CB_Core.GlobalCore;
import CB_Core.Events.KeyCodes;
import CB_Core.GL_UI.Fonts;
import CB_Core.GL_UI.Fonts.FileType;
import CB_Core.GL_UI.Fonts.Settings;
import CB_Core.GL_UI.GL_View_Base;
import CB_Core.GL_UI.SpriteCache;
import CB_Core.GL_UI.runOnGL;
import CB_Core.GL_UI.Main.MainViewBase;
import CB_Core.Map.MapTileLoader;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GdxGame extends CB_Core.GL_UI.GL_Listener.GL implements ApplicationListener, InputProcessor
{

	private boolean nightMode = false;

	public GdxGame(int initalWidth, int initialHeight)
	{
		super(initalWidth, initialHeight);

	}

	@Override
	public void create()
	{
		Initialize();
		Gdx.input.setInputProcessor(this);

	}

	public void Initialize()
	{
		// Logger.LogCat("GL_Listner => Initialize");

		if (batch == null)
		{

			batch = new SpriteBatch(SPRITE_BATCH_BUFFER);

		}

		if (child == null)
		{
			child = new splash(0, 0, width, height, "Splash");
			// child = new MainViewBase(0, 0, width, height, "MainView");
			child.setClickable(true);
		}

		if (mDialog == null)
		{
			mDialog = new MainViewBase(0, 0, width, height, "Dialog");
			mDialog.setClickable(true);
		}

		if (mActivity == null)
		{
			mActivity = new MainViewBase(0, 0, width, height, "Dialog");
			mActivity.setClickable(true);
		}

		// Initial Fonts
		Settings cfgFont = new Fonts().new Settings();
		cfgFont.SkinFolder = "data/skins/default";
		cfgFont.DefaultSkinFolder = "data/skins/default";

		cfgFont.SizeBiggest = 27;
		cfgFont.SizeBig = 18;
		cfgFont.SizeNormal = 15;
		cfgFont.SizeNormalbubble = 14;
		cfgFont.SizeSmall = 13;
		cfgFont.SizeSmallBubble = 11;

		cfgFont.fileType = FileType.internal;
		cfgFont.InternalFont = "data/skins/default/DroidSans-Bold.ttf";
		Fonts.loadFonts(cfgFont);
	}

	@Override
	public void render()
	{

		if (Energy.DisplayOff()) return;

		if (!started.get() || stopRender) return;

		stateTime += Gdx.graphics.getDeltaTime();
		lastRenderBegin = System.currentTimeMillis();

		if (renderStartetListner != null)
		{
			renderStartetListner.renderIsStartet();
			renderStartetListner = null;
			removeRenderView(child);
		}

		if (!ShaderSetted) setShader();

		synchronized (runOnGL_List)
		{
			if (runOnGL_List.size() > 0)
			{
				for (runOnGL run : runOnGL_List)
				{
					if (run != null) run.run();
				}

				runOnGL_List.clear();
			}
		}

		if (ifAllInitial)
		{
			synchronized (runIfInitial)
			{
				if (runIfInitial.size() > 0)
				{
					for (runOnGL run : runIfInitial)
					{
						if (run != null) run.run();
					}

					runIfInitial.clear();
				}
			}
		}

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (nightMode)
		{
			Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		}
		else
		{
			Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
		}

		try
		{
			batch.begin();
		}
		catch (java.lang.IllegalStateException e)
		{
			batch.flush();
			batch.end();
			batch.begin();
		}
		batch.setProjectionMatrix(prjMatrix.Matrix());

		// if Tablet, so the Activity is smaller the screen size
		// render childs and darkness Sprite
		if (GlobalCore.isTab)
		{
			child.renderChilds(batch, prjMatrix);
			if (ActivityIsShown && mActivity.getCildCount() > 0)
			{
				// Zeichne Transparentes Rec um den Hintergrund abzudunkeln.
				drawDarknessSprite();
				mActivity.renderChilds(batch, prjMatrix);
			}
		}

		if (ActivityIsShown && !GlobalCore.isTab)
		{
			drawDarknessSprite();
			mActivity.renderChilds(batch, prjMatrix);
		}

		if (!ActivityIsShown)
		{
			child.renderChilds(batch, prjMatrix);
		}

		if (DialogIsShown && mDialog.getCildCount() > 0)
		{
			// Zeichne Transparentes Rec um den Hintergrund abzudunkeln.
			drawDarknessSprite();
			mDialog.renderChilds(batch, prjMatrix);
		}

		if (ToastIsShown)
		{
			mToastOverlay.renderChilds(batch, prjMatrix);
		}

		if (MarkerIsShown)
		{
			mMarkerOverlay.renderChilds(batch, prjMatrix);
		}

		GL_View_Base.debug = Config.settings.DebugMode.getValue();

		if (GL_View_Base.debug && misTouchDown)
		{
			Sprite point = SpriteCache.LogIcons.get(14);
			TouchDownPointer first = touchDownPos.get(0);

			if (first != null)
			{
				int x = first.point.x;
				int y = this.height - first.point.y;
				int pointSize = 20;

				batch.draw(point, x - (pointSize / 2), y - (pointSize / 2), pointSize, pointSize);
			}
		}

		if (true)
		{

			float FpsInfoSize = MapTileLoader.queueProcessorLifeCycle ? 4 : 8;

			if (FpsInfoSprite != null)
			{
				batch.draw(FpsInfoSprite, FpsInfoPos, 2, FpsInfoSize, FpsInfoSize);
			}
			else
			{
				if (SpriteCache.day_skin != null)// SpriteCache is initial
				{
					FpsInfoSprite = new Sprite(SpriteCache.getThemedSprite("pixel2x2"));
					FpsInfoSprite.setColor(1.0f, 1.0f, 0.0f, 1.0f);
					FpsInfoSprite.setSize(4, 4);
				}
			}

			FpsInfoPos++;
			if (FpsInfoPos > 60)
			{
				FpsInfoPos = 0;
			}

			if (debugWriteSpriteCount)
			{
				renderTime = ((System.currentTimeMillis() - lastRenderBegin) + renderTime) / 2;
				Fonts.getBubbleSmall().draw(batch,
						"Max Sprites on Batch:" + String.valueOf(debugSpritebatchMaxCount) + "/" + String.valueOf(renderTime), width / 4,
						20);
				debugSpritebatchMaxCount = Math.max(debugSpritebatchMaxCount, batch.maxSpritesInBatch);
			}
			batch.end();
		}

		Gdx.gl.glFlush();
		Gdx.gl.glFinish();

	}

	public void switchToMainView(de.gdxgame.MainView mainView)
	{
		MainViewBase altSplash = child;
		child = mainView;
		altSplash.dispose();
		altSplash = null;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button)
	{
		return this.onTouchDownBase(x, y, pointer, button);

	}

	@Override
	public boolean touchDragged(int x, int y, int pointer)
	{
		// Events vom Listener nicht behandeln, wir haben unsere eigenes
		// Eventhandling
		return onTouchDraggedBase(x, y, pointer);
	}

	private int MouseX = 0;
	private int MouseY = 0;

	@Override
	public boolean mouseMoved(int x, int y)
	{
		MouseX = x;
		MouseY = y;
		return onTouchDraggedBase(x, y, -1);
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button)
	{
		return onTouchUpBase(x, y, pointer, button);

	}

	// private Stage mStage;

	// private void chkStageInitial()
	// {
	// if (mStage == null)
	// {// initial a virtual stage
	// mStage = new Stage(UiSizes.getWindowWidth(), UiSizes.getWindowHeight(), false);
	// }
	// }

	@Override
	public boolean keyTyped(char character)
	{
		if (DialogIsShown && character == KeyCodes.KEYCODE_BACK)
		{
			closeDialog(mDialog);
			return true; // behandelt!
		}

		if (ActivityIsShown && character == KeyCodes.KEYCODE_BACK)
		{
			closeActivity();
			return true; // behandelt!
		}

		// WeiterLeiten an EditTextView, welches den Focus Hat
		if (keyboardFocus != null && keyboardFocus.keyTyped(character)) return true;

		return false;

	}

	@Override
	public boolean keyUp(int KeyCode)
	{
		// WeiterLeiten an EditTextView, welches den Focus Hat
		if (keyboardFocus != null && keyboardFocus.keyUp(KeyCode)) return true;
		return false;
	}

	@Override
	public boolean keyDown(int keycode)
	{
		// WeiterLeiten an EditTextView, welches den Focus Hat
		if (keyboardFocus != null && keyboardFocus.keyDown(keycode)) return true;
		return false;
	}

	@Override
	public boolean scrolled(int amount)
	{

		// int scrollSize = (UiSizes.that.getClickToleranz() + 10) * amount;
		int scrollSize = 20 * amount;

		int Pointer = (scrollSize > 0) ? GL_View_Base.MOUSE_WHEEL_POINTER_UP : GL_View_Base.MOUSE_WHEEL_POINTER_DOWN;

		this.onTouchDownBase(MouseX, MouseY, Pointer, -1);

		this.onTouchDraggedBase(MouseX - scrollSize, MouseY - scrollSize, Pointer);

		this.onTouchUpBase(MouseX - scrollSize, MouseY - scrollSize, Pointer, -1);

		return true;
	}

}
