package de.CB_SkinMaker;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import CB_Core.TranslationEngine.Translation;

import com.badlogic.gdx.Gdx;
import com.thebuzzmedia.sjxp.XMLParser;
import com.thebuzzmedia.sjxp.XMLParserException;
import com.thebuzzmedia.sjxp.rule.DefaultRule;
import com.thebuzzmedia.sjxp.rule.IRule;

public class lunch extends JFrame
{

	{
		// Set Look & Feel
		try
		{
			javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static lunch that;

	private JDesktopPane jDesktopPane1;
	private WorkFrame jWorkFrame;
	private JButton buttonLoad;
	private JButton buttonNew;
	private JLabel jLabel5;
	private JRadioButton jRadioButton2;
	private JRadioButton jRadioButton1;
	private JRadioButton jRadioButtonRelease;
	private JLabel jLabel8;
	private JLabel jLabel7;
	private JTextField jTextField1;
	private JLabel jLabel6;
	private JLabel jLabel4;
	private JButton jButton3;
	private JTextField jTextFieldVers;
	private JLabel jLabel3;
	private JTextField jTextFieldPackageName;
	private JPanel jPanel1;
	private JLabel jLabel2;
	private JButton jButton2;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					lunch frame = new lunch();
					frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public lunch()
	{
		that = this;

		setTitle("CB SkinMaker");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		setExtendedState(Frame.MAXIMIZED_BOTH);
		getContentPane().setLayout(new BorderLayout(0, 0));

		// initial GDX and ColorDialog
		ColorDialog cd = new ColorDialog();
		cd.setVisible(false);

		// Initial Translations
		Locale locale = Locale.getDefault();
		String lang = locale.getLanguage();
		new Translation("data", true);

		// only first two letters
		if (lang.length() > 2) lang = lang.substring(0, 1);

		if (Gdx.files.internal("data/lang/" + lang + "/skinMakerStrings.ini").exists())
		{
			Translation.LoadTranslation("data/lang/" + lang + "/skinMakerStrings.ini");
		}
		else
		{
			Translation.LoadTranslation("data/lang/en-GB/skinMakerStrings.ini");
		}

		{
			jDesktopPane1 = new JDesktopPane();
			getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
			{
				buttonLoad = new JButton();
				jDesktopPane1.add(buttonLoad, JLayeredPane.DEFAULT_LAYER);
				buttonLoad.setText(Translation.Get("loadProj"));
				buttonLoad.setBounds(162, 78, 150, 50);
				buttonLoad.addMouseListener(new MouseAdapter()
				{
					public void mouseClicked(MouseEvent evt)
					{
						selectDefault();
					}
				});

				buttonNew = new JButton();
				jDesktopPane1.add(buttonNew, JLayeredPane.DEFAULT_LAYER);
				buttonNew.setText(Translation.Get("createProj"));
				buttonNew.setBounds(162, 178, 150, 50);
				buttonNew.addMouseListener(new MouseAdapter()
				{
					public void mouseClicked(MouseEvent evt)
					{
						CreateProjekt CP = new CreateProjekt();
						CP.setVisible(true);
					}
				});

			}

		}

		{// Initial SkinMaker Label

			JLabel jLabel1 = new JLabel();
			jDesktopPane1.add(jLabel1, JLayeredPane.DEFAULT_LAYER);
			// create new Font
			Font font = new Font("Courier", Font.BOLD, 30);

			jLabel1.setFont(font);
			jLabel1.setText("Skin Maker");
			jLabel1.setBounds(12, 80, 400, 200);
			jLabel1.setForeground(new java.awt.Color(255, 255, 255));
		}
	}

	final String directoryPath = "Android_GUI/";
	private JButton jButton4;
	private JTextField jTextField2;
	// final String directoryPath = "C:/@Work/Cachebox/JAVA WorkSpace/droidcachebox/trunk/Android_GUI/";
	private JScrollPane jScrollPane1;
	private JTextArea jTextArea1;
	private static String currentPackageName;
	// private static String currentVerNumber;
	// private static String currentPrafix;
	private static String currentIcon;
	private static String currentMainfestVersionString;

	private static String newPackageName;
	// private static String newVerNumber;
	// private static String newPrafix;
	private static String newIcon;
	private static String newMainfestVersionString;

	private void init()
	{

		// read current Mainfest
		Map<String, String> values = new HashMap<String, String>();

		System.setProperty("sjxp.namespaces", "false");

		List<IRule<Map<String, String>>> ruleList = new ArrayList<IRule<Map<String, String>>>();

		ruleList.add(new DefaultRule<Map<String, String>>(com.thebuzzmedia.sjxp.rule.IRule.Type.ATTRIBUTE, "/manifest", "package",
				"android:versionName")
		{
			@Override
			public void handleParsedAttribute(XMLParser<Map<String, String>> parser, int index, String value, Map<String, String> values)
			{

				values.put("attribute_" + this.getAttributeNames()[index], value);
			}
		});

		ruleList.add(new DefaultRule<Map<String, String>>(com.thebuzzmedia.sjxp.rule.IRule.Type.ATTRIBUTE, "/manifest/application",
				"android:icon")
		{
			@Override
			public void handleParsedAttribute(XMLParser<Map<String, String>> parser, int index, String value, Map<String, String> values)
			{

				values.put("attribute_" + this.getAttributeNames()[index], value);
			}
		});

		@SuppressWarnings("unchecked")
		XMLParser<Map<String, String>> parserCache = new XMLParser<Map<String, String>>(ruleList.toArray(new IRule[0]));

		boolean error = false;

		try
		{
			// parserCache.parse(new FileInputStream("Android_GUI/AndroidManifest.xml"), values);
			parserCache.parse(new FileInputStream(directoryPath + "AndroidManifest.xml"), values);
		}
		catch (IllegalArgumentException e)
		{

			e.printStackTrace();
		}
		catch (XMLParserException e)
		{

			e.printStackTrace();
		}
		catch (FileNotFoundException e)
		{
			jLabel5.setText("Mainfest not found.");
			jLabel5.setForeground(Color.RED);
			jButton3.setEnabled(false);
			error = true;
			e.printStackTrace();
		}

		currentMainfestVersionString = values.get("attribute_android:versionName");
		currentPackageName = values.get("attribute_package");
		currentIcon = values.get("attribute_android:icon");

		if (!error)
		{
			jLabel5.setText(currentPackageName);
			jLabel6.setText(currentMainfestVersionString);
		}

	}

	private void jButton3MouseClicked(MouseEvent evt)
	{
		run();
	}

	private void run()
	{

	}

	private void addMsg(String msg)
	{
		// String alt = jTextArea1.getText();
		// alt += msg + String.format("%n");

		int x;
		jTextArea1.selectAll();
		x = jTextArea1.getSelectionEnd();
		jTextArea1.select(x, x);
		jTextArea1.invalidate();

		try
		{
			Thread.sleep(20);
		}
		catch (InterruptedException e)
		{

			e.printStackTrace();
		}

	}

	private void selectDefault()
	{
		final JFileChooser chooser = new JFileChooser("Verzeichnis w�hlen");
		chooser.setDialogType(JFileChooser.OPEN_DIALOG);
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		final File file = new File("/home");

		chooser.setCurrentDirectory(file);

		chooser.addPropertyChangeListener(new PropertyChangeListener()
		{
			public void propertyChange(PropertyChangeEvent e)
			{
				if (e.getPropertyName().equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)
						|| e.getPropertyName().equals(JFileChooser.DIRECTORY_CHANGED_PROPERTY))
				{
					final File f = (File) e.getNewValue();
				}
			}
		});

		chooser.setVisible(true);
		final int result = chooser.showOpenDialog(null);

		if (result == JFileChooser.APPROVE_OPTION)
		{
			File inputVerzFile = chooser.getSelectedFile();
			String inputVerzStr = inputVerzFile.getPath();
			System.out.println("Eingabepfad:" + inputVerzStr);
		}
		System.out.println("Abbruch");
		chooser.setVisible(false);
	}

	public void showWorkFrame(Settings settings)
	{

		jWorkFrame = new WorkFrame(settings);
		jDesktopPane1.add(jWorkFrame, BorderLayout.CENTER);

		jWorkFrame.setVisible(true);

	}

}
