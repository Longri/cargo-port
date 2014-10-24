package de.gdxgame.Views;

import java.io.IOException;

import CB_Translation_Base.TranslationEngine.Translation;
import CB_UI_Base.GL_UI.Fonts;
import CB_UI_Base.GL_UI.Controls.Image;
import CB_UI_Base.GL_UI.Controls.Label;
import CB_UI_Base.GL_UI.Controls.ProgressBar;
import CB_UI_Base.GL_UI.GL_Listener.GL;
import CB_UI_Base.GL_UI.Main.MainViewBase;
import CB_UI_Base.Math.CB_RectF;
import CB_UI_Base.Math.UiSizes;
import CB_Utils.Log.Logger;
import Res.ResourceCache;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

import de.gdxgame.GdxGame;
import de.gdxgame.Global;
import de.gdxgame.LasifantAnimation;

public class splash extends MainViewBase
{
	private final long SPLASH_MIN_SHOW_TIME = 5000;

	private long splashEndTime = 0;

	public splash(float X, float Y, float Width, float Height, String Name)
	{
		super(X, Y, Width, Height, Name);

	}

	TextureAtlas atlas;
	ProgressBar progress;
	Image CB_Logo, OSM_Logo, Route_Logo, Mapsforge_Logo, FX2_Logo, GC_Logo;
	LasifantAnimation Lasifant;

	Label descTextView;

	int step = 0;
	boolean switcher = false;
	boolean breakForWait = false;

	@Override
	public void onShow()
	{
		GL.that.addRenderView(this, GL.FRAME_RATE_FAST_ACTION);
		splashEndTime = System.currentTimeMillis() + SPLASH_MIN_SHOW_TIME;
	}

	@Override
	protected void Initial()
	{
		switcher = !switcher;
		if (switcher && !breakForWait)
		{
			// in jedem Render Vorgang einen Step ausführen
			switch (step)
			{
			case 0:
				atlas = new TextureAtlas(Gdx.files.internal("skins/default/day/SplashPack.spp"));
				setBackground(new SpriteDrawable(atlas.createSprite("splash-back")));
				break;
			case 1:
				ini_Progressbar();
				progress.setProgress(10, "Read Config");
				break;
			case 2:

				// ini_Config();
				progress.setProgress(15, "Load Translations");
				break;
			case 3:
				ini_Translations();
				progress.setProgress(20, "Load Sprites");
				break;
			case 4:
				ini_Sprites();
				progress.setProgress(30, "check directoiries");
				break;
			case 5:
				// ini_Dirs();
				progress.setProgress(40, "Select DB");
				break;
			case 6:
				ini_SelectDB();
				progress.setProgress(60, "Load Caches");
				break;
			case 7:
				ini_CacheDB();
				progress.setProgress(80, "initial Map layer");
				break;
			case 8:
				ini_MapPaks();
				progress.setProgress(100, "Run");
				break;
			case 100:
				ini_TabMainView();
				break;
			default:
				step = 99;
			}
			step++;
		}

		if (step <= 101) resetInitial();
	}

	@Override
	protected void SkinIsChanged()
	{

	}

	/**
	 * Step 1 <br>
	 * add Progressbar
	 */
	private void ini_Progressbar()
	{

		float ref = UiSizes.that.getWindowHeight() / 13;
		CB_RectF CB_LogoRec = new CB_RectF(this.getHalfWidth() - (ref * 2.5f), this.getHeight() - ((ref * 5) / 4.11f) - ref, ref * 5,
				(ref * 5) / 4.11f);

		// CB_Logo = new Image(CB_LogoRec, "CB_Logo");
		// CB_Logo.setDrawable(new SpriteDrawable(atlas.createSprite("cachebox-logo")));
		// this.addChild(CB_Logo);

		String VersionString = Global.getVersionString();
		TextBounds bounds = Fonts.getNormal().getMultiLineBounds(VersionString + Global.br + Global.br + Global.splashMsg);
		descTextView = new Label(0, CB_LogoRec.getY() - ref - bounds.height, this.getWidth(), bounds.height + 10, "DescLabel");
		// HAlignment.CENTER funktioniert (hier) (noch) nicht, es kommt rechtsbündig raus
		descTextView.setWrappedText(VersionString + Global.br + Global.br + Global.splashMsg);
		descTextView.setHAlignment(HAlignment.CENTER);
		this.addChild(descTextView);

		Drawable ProgressBack = new NinePatchDrawable(atlas.createPatch("btn-normal"));
		Drawable ProgressFill = new NinePatchDrawable(atlas.createPatch("progress"));

		float ProgressHeight = Math.max(ProgressBack.getBottomHeight() + ProgressBack.getTopHeight(), ref / 1.5f);

		progress = new ProgressBar(new CB_RectF(0, 0, this.getWidth(), ProgressHeight), "Splash.ProgressBar");

		progress.setBackground(ProgressBack);
		progress.setProgressFill(ProgressFill);
		this.addChild(progress);

		float logoCalcRef = ref * 1.5f;

		CB_RectF rec_Mapsforge_Logo = new CB_RectF(200, 50, logoCalcRef, logoCalcRef / 1.142f);
		CB_RectF rec_FX2_Logo = new CB_RectF(rec_Mapsforge_Logo);
		float w = logoCalcRef * 2.892655367231638f;
		CB_RectF rec_LibGdx_Logo = new CB_RectF(this.getHalfWidth() - (w / 2), 50, w, w);

		rec_FX2_Logo.setX(400);

		Lasifant = new LasifantAnimation(rec_LibGdx_Logo, "LibGdx_Logo");

		Lasifant.addFrame(atlas.createSprite("logo-1"));
		Lasifant.addFrame(atlas.createSprite("logo-2"));
		Lasifant.addFrame(atlas.createSprite("logo-3"));
		Lasifant.addFrame(atlas.createSprite("logo-4"));
		Lasifant.addFrame(atlas.createSprite("logo-5"));
		Lasifant.addFrame(atlas.createSprite("logo-6"));
		Lasifant.addFrame(atlas.createSprite("logo-7"));
		Lasifant.addLastFrame(atlas.createSprite("logo-8"));

		Lasifant.play();

		this.addChild(Lasifant);

	}

	/**
	 * Step 3 <br>
	 * Load Translations
	 */
	private void ini_Translations()
	{
		Logger.DEBUG("ini_Translations");
		new Translation("data", FileType.Internal);
		try
		{
			Translation.LoadTranslation("data/lang/en-GB/strings.ini");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Step 4 <br>
	 * Load Sprites
	 */
	private void ini_Sprites()
	{
		Logger.DEBUG("ini_Sprites");
		ResourceCache.LoadSprites(false);
	}

	/**
	 * Step 5 <br>
	 * show select DB Dialog
	 */
	private void ini_SelectDB()
	{

	}

	/**
	 * Step 6<br>
	 * Load Cache DB3
	 */
	private void ini_CacheDB()
	{
		Logger.DEBUG("ini_CacheDB");
		// chk if exist filter preset splitter "#" and Replace

	}

	/**
	 * Step 7 <br>
	 * chk installed map packs/layers
	 */
	private void ini_MapPaks()
	{

	}

	/**
	 * Last Step <br>
	 * Show TabMainView
	 */
	private void ini_TabMainView()
	{

		if (splashEndTime > System.currentTimeMillis()) return;

		Logger.DEBUG("ini_TabMainView");
		GL.that.removeRenderView(this);
		((GdxGame) GL.that).switchToMainView();

		GL.setIsInitial();
	}

	@Override
	public void dispose()
	{
		this.removeChildsDirekt();

		if (descTextView != null) descTextView.dispose();
		if (GC_Logo != null) GC_Logo.dispose();
		if (FX2_Logo != null) FX2_Logo.dispose();
		if (Lasifant != null) Lasifant.dispose();
		if (Mapsforge_Logo != null) Mapsforge_Logo.dispose();
		if (CB_Logo != null) CB_Logo.dispose();
		if (progress != null) progress.dispose();
		if (atlas != null) atlas.dispose();

		descTextView = null;
		GC_Logo = null;
		FX2_Logo = null;
		Lasifant = null;
		Mapsforge_Logo = null;
		CB_Logo = null;
		progress = null;
		atlas = null;

	}

}
