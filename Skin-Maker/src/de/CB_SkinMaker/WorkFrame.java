package de.CB_SkinMaker;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

import org.json.JSONException;

import CB_Core.FileUtil;
import CB_Core.TranslationEngine.Translation;

public class WorkFrame extends JInternalFrame
{

	private static final long serialVersionUID = 1L;

	private Settings mSettings;
	private JButton btnGen;

	private JDesktopPane jDesktopPane1;

	public WorkFrame(Settings settings)
	{

		super(settings.getName(), // title
				true, // resizable
				true, // closeable
				true, // maximizable
				true); // iconifiable

		mSettings = settings;

		mdir();

		// this.setBounds((int) (random() * 100), (int) (random() * 100), 100 + (int) (random() * 400), 100 + (int) (random() * 300));
		jDesktopPane1 = new JDesktopPane();
		getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
		this.setBounds(0, 0, 500, 300);
		setResizable(true);
		setClosable(true);
		setMaximizable(true);
		setIconifiable(true);

		btnGen = new JButton();
		btnGen.setText(Translation.Get("createProj"));
		btnGen.setBounds(0, 0, 100, 30);
		jDesktopPane1.add(btnGen, BorderLayout.CENTER);
		btnGen.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent evt)
			{
				try
				{
					generate();
				}
				catch (FileNotFoundException e)
				{
					e.printStackTrace();
				}
				catch (JSONException e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	@SuppressWarnings("deprecation")
	public void layout()
	{
		super.layout();
		btnGen.setBounds(jDesktopPane1.bounds().width - 120, jDesktopPane1.bounds().height - 50, 100, 30);
	}

	private void mdir()
	{
		// make folder for Default Skin
		new File(mSettings.getWorkPath() + "/defaultskin/day/").mkdirs();
		new File(mSettings.getWorkPath() + "/defaultSkin/night/").mkdirs();

	}

	private void generate() throws FileNotFoundException, JSONException
	{
		String genFolder = mSettings.getWorkPath() + "/gen";

		if (FileUtil.DirectoryExists(genFolder)) FileUtil.deleteFolder(new File(genFolder));

		// Create Folders
		new File(mSettings.getWorkPath() + "/gen/day/").mkdirs();
		new File(mSettings.getWorkPath() + "/gen/night/").mkdirs();

		mSettings.getSkinJsonDay().Save(new File(mSettings.getWorkPath() + "/gen/day/skin.json"));
		mSettings.getSkinJsonNight().Save(new File(mSettings.getWorkPath() + "/gen/night/skin.json"));

	}
}
