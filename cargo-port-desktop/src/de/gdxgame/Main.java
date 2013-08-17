package de.gdxgame;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import CB_Core.Config;
import CB_Core.GlobalCore;
import CB_Core.Plattform;
import CB_Core.Events.platformConector;
import CB_Core.Events.platformConector.ICallUrl;
import CB_Core.Events.platformConector.IHardwarStateListner;
import CB_Core.Events.platformConector.IQuit;
import CB_Core.Events.platformConector.IgetFileListner;
import CB_Core.Events.platformConector.IgetFileReturnListner;
import CB_Core.Events.platformConector.IgetFolderListner;
import CB_Core.Events.platformConector.IgetFolderReturnListner;
import CB_Core.GL_UI.GL_Listener.GL;
import CB_Core.GL_UI.GL_Listener.GL_Listener_Interface;
import CB_Core.Math.Size;
import CB_Core.Math.devicesSizes;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import de.gdxgame.Views.MainView;
import de.gdxgame.Views.splash;

public class Main
{
	public static void main(String[] args)
	{
		GlobalCore.platform = Plattform.Desktop;

		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "cargo-port";
		cfg.useGL20 = true;
		cfg.width = 1024;
		cfg.height = 768;

		// Initial Config
		String workPath = "./cargo_port";
		Config.Initialize(workPath, workPath + "/cachebox.config");

		Config.settings.SkinFolder.setValue("default");

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

		final LwjglApplication App = new LwjglApplication(Game, cfg);
		App.getGraphics().setContinuousRendering(false);

		GL.listenerInterface = new GL_Listener_Interface()
		{

			@Override
			public void RequestRender(String requestName)
			{
				App.getGraphics().requestRendering();
			}

			@Override
			public void RenderDirty()
			{
				App.getGraphics().setContinuousRendering(false);
			}

			@Override
			public void RenderContinous()
			{
				App.getGraphics().setContinuousRendering(true);
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

		DesktopClipboard dcb = new DesktopClipboard();

		if (dcb != null) GlobalCore.setDefaultClipboard(dcb);

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
