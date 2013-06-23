package de.gdxgame;

import CB_Core.Config;
import CB_Core.GlobalCore;
import CB_Core.Events.platformConector;
import CB_Core.Events.platformConector.IQuit;
import CB_Core.GL_UI.GL_Listener.GL;
import CB_Core.Math.Size;
import CB_Core.Math.UiSizes;
import CB_Core.Math.devicesSizes;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class MainActivity extends AndroidApplication
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		GL.resetIsInitial();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
		cfg.useGL20 = true;

		Resources res = this.getResources();
		devicesSizes ui = new devicesSizes();

		DisplayMetrics displaymetrics = res.getDisplayMetrics();

		int h = displaymetrics.heightPixels;
		int w = displaymetrics.widthPixels;

		ui.Window = new Size(w, h);
		ui.Density = res.getDisplayMetrics().density;
		ui.RefSize = res.getDimensionPixelSize(R.dimen.RefSize);
		ui.TextSize_Normal = res.getDimensionPixelSize(R.dimen.TextSize_normal);
		ui.ButtonTextSize = res.getDimensionPixelSize(R.dimen.BtnTextSize);
		ui.IconSize = res.getDimensionPixelSize(R.dimen.IconSize);
		ui.Margin = res.getDimensionPixelSize(R.dimen.Margin);
		ui.ArrowSizeList = res.getDimensionPixelSize(R.dimen.ArrowSize_List);
		ui.ArrowSizeMap = res.getDimensionPixelSize(R.dimen.ArrowSize_Map);
		ui.TB_IconSize = res.getDimensionPixelSize(R.dimen.TB_icon_Size);
		ui.isLandscape = false;

		GlobalCore.displayDensity = ui.Density;

		// Initial Config
		String workPath = "./cargo_port";
		Config.Initialize(workPath, workPath + "/cachebox.config");

		new UiSizes();
		UiSizes.that.initial(ui);

		// create new splash
		splash sp = new splash(0, 0, w, h, "Splash");

		// create new mainView
		MainView ma = new MainView(0, 0, w, h, "mainView");

		final GdxGame Game = new GdxGame(ui.Window.width, ui.Window.height, sp, ma);

		initialize(Game, cfg);
		GL.that.onStart();

		platformConector.setQuitListner(new IQuit()
		{
			@Override
			public void Quit()
			{
				finish();
			}
		});
	}
}