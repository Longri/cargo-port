package de.gdxgame;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import CB_UI_Base.Config;
import CB_UI_Base.Events.platformConector;
import CB_UI_Base.Events.platformConector.ICallUrl;
import CB_UI_Base.Events.platformConector.IHardwarStateListner;
import CB_UI_Base.Events.platformConector.IQuit;
import CB_UI_Base.Events.platformConector.IgetFileListner;
import CB_UI_Base.Events.platformConector.IgetFileReturnListner;
import CB_UI_Base.Events.platformConector.IgetFolderListner;
import CB_UI_Base.Events.platformConector.IgetFolderReturnListner;
import CB_UI_Base.GL_UI.GL_Listener.GL;
import CB_UI_Base.GL_UI.GL_Listener.GL_Listener_Interface;
import CB_UI_Base.Math.Size;
import CB_UI_Base.Math.devicesSizes;
import CB_Utils.Config_Core;
import CB_Utils.Plattform;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import de.gdxgame.Views.MainView;
import de.gdxgame.Views.splash;

public class Main
{
	public static void main(String[] args)
	{
		Plattform.used = Plattform.Desktop;

		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "cargo-port";
		cfg.width = 800;
		cfg.height = 600;
		cfg.resizable = false;
		cfg.samples = 4;

		// Read Config
		String workPath = "./cachebox";
		// Initial Config
		new Config(workPath);

		Config_Core.WorkPath = workPath;
		Config.Initialize(workPath, workPath + "/cachebox.config");

		Settings.SkinFolder.setValue("default");

		devicesSizes ui = new devicesSizes();

		ui.Window = new Size(cfg.width, cfg.height);
		ui.Density = 0.7f; // 1.5
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

		final LwjglApplication App = new LwjglApplication(Game, cfg);
		App.getGraphics().setContinuousRendering(false);

		GL.listenerInterface = new GL_Listener_Interface()
		{

			AtomicBoolean isContinous = new AtomicBoolean(false);

			@Override
			public void RenderDirty()
			{
				isContinous.set(false);
				App.getGraphics().setContinuousRendering(false);
			}

			@Override
			public void RenderContinous()
			{
				isContinous.set(true);
				App.getGraphics().setContinuousRendering(true);
			}

			@Override
			public boolean isContinous()
			{
				return isContinous.get();
			}

			@Override
			public void RequestRender()
			{
				App.getGraphics().requestRendering();
			}
		};

		Timer timer = new Timer();
		TimerTask task = new TimerTask()
		{
			@Override
			public void run()
			{
				Game.onStart();
			}
		};
		timer.schedule(task, 600);

		// ''''''''''''''''''''''
		platformConector.setisOnlineListner(new IHardwarStateListner()
		{

			@Override
			public boolean isOnline()
			{
				return true;
			}

			@Override
			public boolean isGPSon()
			{
				return true;
			}

			@Override
			public void vibrate()
			{

			}

		});

		platformConector.setGetFileListner(new IgetFileListner()
		{
			@Override
			public void getFile(String initialPath, final String extension, String TitleText, String ButtonText,
					IgetFileReturnListner returnListner)
			{

				final String ext = extension.replace("*", "");

				JFileChooser chooser = new JFileChooser();

				chooser.setCurrentDirectory(new java.io.File(initialPath));
				chooser.setDialogTitle(TitleText);

				FileFilter filter = new FileFilter()
				{

					@Override
					public String getDescription()
					{
						return extension;
					}

					@Override
					public boolean accept(File f)
					{
						if (f.getAbsolutePath().endsWith(ext)) return true;
						return false;
					}
				};

				chooser.setFileFilter(filter);

				int returnVal = chooser.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION)
				{
					if (returnListner != null) returnListner.getFieleReturn(chooser.getSelectedFile().getAbsolutePath());
					System.out.println("You chose to open this file: " + chooser.getSelectedFile().getName());
				}

			}
		});

		platformConector.setGetFolderListner(new IgetFolderListner()
		{

			@Override
			public void getfolder(String initialPath, String TitleText, String ButtonText, IgetFolderReturnListner returnListner)
			{

				JFileChooser chooser = new JFileChooser();

				chooser.setCurrentDirectory(new java.io.File(initialPath));
				chooser.setDialogTitle(TitleText);

				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = chooser.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION)
				{
					if (returnListner != null) returnListner.getFolderReturn(chooser.getSelectedFile().getAbsolutePath());
					System.out.println("You chose to open this file: " + chooser.getSelectedFile().getName());
				}

			}
		});

		platformConector.setQuitListner(new IQuit()
		{

			@Override
			public void Quit()
			{
				System.exit(0);

			}
		});

		// DesktopClipboard dcb = new DesktopClipboard();
		//
		// if (dcb != null) GlobalCore.setDefaultClipboard(dcb);

		platformConector.setCallUrlListner(new ICallUrl()
		{

			@Override
			public void call(String url)
			{
				java.awt.Desktop desktop = java.awt.Desktop.getDesktop();

				if (!desktop.isSupported(java.awt.Desktop.Action.BROWSE))
				{

					System.err.println("Desktop doesn't support the browse action (fatal)");
					System.exit(1);
				}

				try
				{

					java.net.URI uri = new java.net.URI(url);
					desktop.browse(uri);
				}
				catch (Exception e)
				{

					System.err.println(e.getMessage());
				}

			}
		});

	}
}
