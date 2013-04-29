package de.gdxFrame;

import java.io.File;
import java.io.IOException;

import CB_Core.Config;
import CB_Core.DB.Database;
import CB_Core.GL_UI.Fonts;
import CB_Core.GL_UI.SpriteCache;
import CB_Core.GL_UI.Activitys.SelectDB;
import CB_Core.GL_UI.Controls.Image;
import CB_Core.GL_UI.Controls.Label;
import CB_Core.GL_UI.Controls.ProgressBar;
import CB_Core.GL_UI.Controls.Animation.FrameAnimation;
import CB_Core.GL_UI.GL_Listener.GL;
import CB_Core.GL_UI.Main.TabMainView;
import CB_Core.Log.Logger;
import CB_Core.Math.CB_RectF;
import CB_Core.Math.UiSizes;
import CB_Core.Settings.SettingString;
import CB_Core.TranslationEngine.Translation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

public class splash extends TabMainView
{
	public splash(float X, float Y, float Width, float Height, String Name)
	{
		super(X, Y, Width, Height, Name);
		GL.that.addRenderView(this, GL.FRAME_RATE_FAST_ACTION);
	}

	TextureAtlas atlas;
	ProgressBar progress;
	Image CB_Logo, OSM_Logo, Route_Logo, Mapsforge_Logo, FX2_Logo, GC_Logo;
	FrameAnimation Lasifant;

	Label descTextView;
	SelectDB selectDBDialog;

	int step = 0;
	boolean switcher = false;
	boolean breakForWait = false;

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
				atlas = new TextureAtlas(Gdx.files.internal("data/skins/default/day/SplashPack.spp"));
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
				// ini_Sprites();
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

		if (step <= 100) resetInitial();
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
		CB_RectF CB_LogoRec = new CB_RectF(this.halfWidth - (ref * 2.5f), this.height - ((ref * 5) / 4.11f) - ref, ref * 5,
				(ref * 5) / 4.11f);

		// CB_Logo = new Image(CB_LogoRec, "CB_Logo");
		// CB_Logo.setDrawable(new SpriteDrawable(atlas.createSprite("cachebox-logo")));
		// this.addChild(CB_Logo);

		String VersionString = Global.getVersionString();
		TextBounds bounds = Fonts.getNormal().getMultiLineBounds(VersionString + Global.br + Global.br + Global.splashMsg);
		descTextView = new Label(0, CB_LogoRec.getY() - ref - bounds.height, this.width, bounds.height + 10, "DescLabel");
		// HAlignment.CENTER funktioniert (hier) (noch) nicht, es kommt rechtsbündig raus
		descTextView.setWrappedText(VersionString + Global.br + Global.br + Global.splashMsg, HAlignment.CENTER);
		this.addChild(descTextView);

		Drawable ProgressBack = new NinePatchDrawable(atlas.createPatch("btn-normal"));
		Drawable ProgressFill = new NinePatchDrawable(atlas.createPatch("progress"));

		float ProgressHeight = Math.max(ProgressBack.getBottomHeight() + ProgressBack.getTopHeight(), ref / 1.5f);

		progress = new ProgressBar(new CB_RectF(0, 0, this.width, ProgressHeight), "Splash.ProgressBar");

		progress.setBackground(ProgressBack);
		progress.setProgressFill(ProgressFill);
		this.addChild(progress);

		float logoCalcRef = ref * 1.5f;

		CB_RectF rec_GC_Logo = new CB_RectF(20, 50, logoCalcRef, logoCalcRef);
		CB_RectF rec_Mapsforge_Logo = new CB_RectF(200, 50, logoCalcRef, logoCalcRef / 1.142f);
		CB_RectF rec_FX2_Logo = new CB_RectF(rec_Mapsforge_Logo);
		float w = logoCalcRef * 2.892655367231638f;
		CB_RectF rec_LibGdx_Logo = new CB_RectF(this.halfWidth - (w / 2), 50, w, w);
		CB_RectF rec_OSM = new CB_RectF(rec_Mapsforge_Logo);
		CB_RectF rec_Route = new CB_RectF(rec_Mapsforge_Logo);

		rec_FX2_Logo.setX(400);

		Lasifant = new FrameAnimation(rec_LibGdx_Logo, "LibGdx_Logo");

		Lasifant.addFrame(atlas.createSprite("logo-1"));
		Lasifant.addFrame(atlas.createSprite("logo-2"));
		Lasifant.addFrame(atlas.createSprite("logo-3"));
		Lasifant.addFrame(atlas.createSprite("logo-4"));
		Lasifant.addFrame(atlas.createSprite("logo-5"));
		Lasifant.addFrame(atlas.createSprite("logo-6"));
		Lasifant.addFrame(atlas.createSprite("logo-7"));
		Lasifant.addLastFrame(atlas.createSprite("logo-8"));

		Lasifant.play(800);

		this.addChild(Lasifant);
		// this.addChild(Route_Logo);
		// this.addChild(OSM_Logo);

	}

	/**
	 * Step 2 <br>
	 * Load Config DB3
	 */
	private void ini_Config()
	{
		Logger.DEBUG("ini_Config");
		Database.Settings.StartUp(Config.WorkPath + "/User/Config.db3");
		Config.settings.ReadFromDB();
		Config.AcceptChanges();
	}

	/**
	 * Step 3 <br>
	 * Load Translations
	 */
	private void ini_Translations()
	{
		Logger.DEBUG("ini_Translations");
		new Translation("data", true);
		Translation.LoadTranslation("data/lang/en-GB/strings.ini");
	}

	/**
	 * Step 4 <br>
	 * Load Sprites
	 */
	private void ini_Sprites()
	{
		Logger.DEBUG("ini_Sprites");
		SpriteCache.LoadSprites(false);
	}

	/**
	 * Step 5 <br>
	 * chk directories
	 */
	private void ini_Dirs()
	{
		Logger.DEBUG("ini_Dirs");
		ini_Dir(Config.settings.PocketQueryFolder.getValue());
		ini_Dir(Config.settings.TileCacheFolder.getValue());
		ini_Dir(Config.WorkPath + "/User");
		ini_Dir(Config.settings.TrackFolder.getValue());
		ini_Dir(Config.settings.UserImageFolder.getValue());
		ini_Dir(Config.WorkPath + "/repository");
		ini_Dir(Config.settings.DescriptionImageFolder.getValue());
		ini_Dir(Config.settings.MapPackFolder.getValue());
		ini_Dir(Config.settings.SpoilerFolder.getValue());

		// prevent mediascanner to parse all the images in the cachebox folder
		File nomedia = new File(Config.WorkPath, ".nomedia");
		if (!nomedia.exists())
		{
			try
			{
				nomedia.createNewFile();
			}
			catch (IOException e)
			{

				e.printStackTrace();
			}
		}
	}

	private void ini_Dir(String Folder)
	{
		File ff = new File(Folder);
		if (!ff.exists())
		{
			ff.mkdir();
		}
	}

	/**
	 * Step 5 <br>
	 * show select DB Dialog
	 */
	private void ini_SelectDB()
	{

	}

	private void returnFromSelectDB()
	{
		breakForWait = false;
		switcher = true;
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

	private String ReplaceSplitter(String ConfigPreset)
	{
		try
		{
			int pos = ConfigPreset.indexOf("#");
			int pos2 = ConfigPreset.indexOf(";", pos);

			String PresetName = (String) ConfigPreset.subSequence(pos + 1, pos2);
			if (!PresetName.contains(","))
			{
				String s1 = (String) ConfigPreset.subSequence(0, pos);
				String s2 = (String) ConfigPreset.subSequence(pos2, ConfigPreset.length());

				ConfigPreset = s1 + SettingString.STRING_SPLITTER + PresetName + s2;
				return ConfigPreset;
			}
		}
		catch (Exception e)
		{
			return null;
		}

		return null;
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
		Logger.DEBUG("ini_TabMainView");
		GL.that.removeRenderView(this);
		((GdxFrame) GL.that).switchToMainView(new MainView(0f, 0f, width, height, "MainView"));

		GL.setIsInitial();
	}

	public void dispose()
	{
		this.removeChildsDirekt();

		if (selectDBDialog != null) selectDBDialog.dispose();
		if (descTextView != null) descTextView.dispose();
		if (GC_Logo != null) GC_Logo.dispose();
		if (FX2_Logo != null) FX2_Logo.dispose();
		if (Lasifant != null) Lasifant.dispose();
		if (Mapsforge_Logo != null) Mapsforge_Logo.dispose();
		if (CB_Logo != null) CB_Logo.dispose();
		if (progress != null) progress.dispose();
		if (atlas != null) atlas.dispose();

		selectDBDialog = null;
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
