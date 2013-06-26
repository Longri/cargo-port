package de.gdxgame.client;

import de.gdxgame.GdxGame;
import de.gdxgame.Views.MainView;
import de.gdxgame.Views.splash;
import CB_Core.Config;
import CB_Core.Math.Size;
import CB_Core.Math.UiSizes;
import CB_Core.Math.devicesSizes;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

public class GwtLauncher extends GwtApplication {
	@Override
	public GwtApplicationConfiguration getConfig () {
		GwtApplicationConfiguration cfg = new GwtApplicationConfiguration(1024, 768);
		
		return cfg;
	}

	@Override
	public ApplicationListener getApplicationListener () {
		GwtApplicationConfiguration cfg=getConfig();
		
		// Initial Config
				String workPath = "./cargo_port";
				Config.Initialize(workPath, workPath + "/cachebox.config");

			
				devicesSizes ui = new devicesSizes();

				ui.Window = new Size(cfg.width, cfg.height);
				ui.Density = 1.5f;
				ui.RefSize = 64;
				ui.TextSize_Normal = 52;
				ui.ButtonTextSize = 50;
				ui.IconSize = 13;
				ui.Margin = 4;
				ui.ArrowSizeList = 11;
				ui.ArrowSizeMap = 18;
				ui.TB_IconSize = 8;
				ui.isLandscape = true;

				new UiSizes();
				UiSizes.that.initial(ui);

				// create new splash
				splash sp = new splash(0, 0, cfg.width, cfg.height, "Splash");

				// create new mainView
				MainView ma = new MainView(0, 0, cfg.width, cfg.height, "mainView");

				final GdxGame Game = new GdxGame(cfg.width, cfg.height, sp, ma);
		
								
		return Game;
	}
}