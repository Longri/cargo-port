package de.CB_SkinMaker;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.json.JSONException;

import CB_Core.TranslationEngine.Translation;

public class CreateProjekt extends JFrame
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JDesktopPane jDesktopPane1;
	private JButton buttonCreate, btnSelect, btnSelect2;
	private JLabel jLabel1;
	private JTextField jTextFieldWorkspace, jTextFieldDefaultSkin;

	private int margin = 20;
	private int left = 20;
	private int top = 20;
	private int cHeight = 30;// ControlHeight

	public CreateProjekt()
	{
		setTitle("Create Projekt");

		setBounds(100, 100, 800, 600);
		setResizable(false);
		getContentPane().setLayout(new BorderLayout(0, 0));

		jDesktopPane1 = new JDesktopPane();
		getContentPane().add(jDesktopPane1, BorderLayout.CENTER);

		buttonCreate = new JButton();
		jDesktopPane1.add(buttonCreate, JLayeredPane.DEFAULT_LAYER);
		buttonCreate.setText(Translation.Get("createProj"));
		buttonCreate.setBounds(600, 500, 150, cHeight);
		buttonCreate.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent evt)
			{
				Settings set = new Settings();
				set.setWorkspace(jTextFieldWorkspace.getText());
				try
				{
					if (jRadioButton1.isSelected())
					{
						// Create new Skin
						skinJson sj = new skinJson();
						sj.addDefaultColorsDay();
						set.setSkinJsonDay(sj);

						skinJson sjn = new skinJson();
						sjn.addDefaultColorsNight();
						set.setSkinJsonNight(sjn);
					}
					else
					{
						// Load defaultSkin
					}

					set.save();
					lunch.that.showWorkFrame(set);

					CreateProjekt.this.setVisible(false);
				}
				catch (FileNotFoundException | JSONException e)
				{
					e.printStackTrace();
				}
			}
		});
		buttonCreate.setEnabled(false);

		jLabel1 = new JLabel();
		jDesktopPane1.add(jLabel1, JLayeredPane.DEFAULT_LAYER);
		jLabel1.setText(Translation.Get("selectWorkspace"));
		jLabel1.setBounds(left, top, 170, cHeight);
		jLabel1.setForeground(new java.awt.Color(255, 255, 255));

		jTextFieldWorkspace = new JTextField();
		jDesktopPane1.add(jTextFieldWorkspace, JLayeredPane.DEFAULT_LAYER);
		jTextFieldWorkspace.setText("");
		jTextFieldWorkspace.setBounds((int) jLabel1.getBounds().getMaxX(), top, 400, cHeight);
		jTextFieldWorkspace.getDocument().addDocumentListener(new DocumentListener()
		{

			@Override
			public void removeUpdate(DocumentEvent arg0)
			{
				chkIsCreationPosible();
			}

			@Override
			public void insertUpdate(DocumentEvent arg0)
			{
				chkIsCreationPosible();
			}

			@Override
			public void changedUpdate(DocumentEvent arg0)
			{
				chkIsCreationPosible();
			}
		});

		btnSelect = new JButton();
		jDesktopPane1.add(btnSelect, JLayeredPane.DEFAULT_LAYER);
		btnSelect.setText(Translation.Get("select"));
		btnSelect.setBounds((int) jTextFieldWorkspace.getBounds().getMaxX(), top, 150, cHeight);
		btnSelect.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent evt)
			{
				File f = selectFolder();
				if (!f.exists())

				{
					f.mkdir();
				}

				jTextFieldWorkspace.setText(f.getPath());
			}
		});

		addNewOption();
	}

	private File selectFolder()
	{
		final JFileChooser chooser = new JFileChooser("Verzeichnis wählen");
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
			return inputVerzFile;
		}
		System.out.println("Abbruch");
		chooser.setVisible(false);
		return null;
	}

	private void chkIsCreationPosible()
	{
		boolean chk = true;

		// chk Workspace
		File f = new File(jTextFieldWorkspace.getText());
		if (!f.exists()) chk = false;
		if (!jRadioButton1.isSelected())
		{
			// chk Default Skin
			chk = false;
		}

		buttonCreate.setEnabled(chk);
	}

	private JRadioButton jRadioButton2;
	private JRadioButton jRadioButton1;

	private void addNewOption()
	{
		JLabel jLabel2 = new JLabel();
		jDesktopPane1.add(jLabel2, JLayeredPane.DEFAULT_LAYER);
		jLabel2.setText(Translation.Get("CreateEmptySkinOr"));
		jLabel2.setBounds(left, jLabel1.getY() + margin + cHeight, 300, cHeight);
		jLabel2.setForeground(new java.awt.Color(255, 255, 255));

		jRadioButton2 = new JRadioButton();
		jDesktopPane1.add(jRadioButton2);
		jRadioButton2.setText(Translation.Get("LoadDefaultSkin"));
		jRadioButton2.setBounds(left, jLabel2.getY() + margin + cHeight, 170, cHeight);
		jRadioButton2.setForeground(new java.awt.Color(255, 255, 255));

		jTextFieldDefaultSkin = new JTextField();
		jDesktopPane1.add(jTextFieldDefaultSkin, JLayeredPane.DEFAULT_LAYER);
		jTextFieldDefaultSkin.setText("");
		jTextFieldDefaultSkin.setBounds((int) jRadioButton2.getBounds().getMaxX(), jRadioButton2.getY(), 400, cHeight);
		jTextFieldDefaultSkin.getDocument().addDocumentListener(new DocumentListener()
		{

			@Override
			public void removeUpdate(DocumentEvent arg0)
			{
				chkIsCreationPosible();
			}

			@Override
			public void insertUpdate(DocumentEvent arg0)
			{
				chkIsCreationPosible();
			}

			@Override
			public void changedUpdate(DocumentEvent arg0)
			{
				chkIsCreationPosible();
			}
		});

		btnSelect2 = new JButton();
		jDesktopPane1.add(btnSelect2, JLayeredPane.DEFAULT_LAYER);
		btnSelect2.setText(Translation.Get("select"));
		btnSelect2.setBounds((int) jTextFieldDefaultSkin.getBounds().getMaxX(), jRadioButton2.getY(), 170, cHeight);
		btnSelect2.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent evt)
			{
				File f = selectFolder();
				if (!f.exists())

				{
					f.mkdir();
				}

				jTextFieldWorkspace.setText(f.getPath());
			}
		});

		jRadioButton1 = new JRadioButton();
		jDesktopPane1.add(jRadioButton1);
		jRadioButton1.setText(Translation.Get("CreateEmptySkin"));
		jRadioButton1.setBounds(left, jRadioButton2.getBounds().y + margin + cHeight, 300, cHeight);
		jRadioButton1.setForeground(new java.awt.Color(255, 255, 255));

		ButtonGroup group = new ButtonGroup();
		group.add(jRadioButton1);
		group.add(jRadioButton2);

		jRadioButton1.setSelected(true);

		jRadioButton2.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				chkIsCreationPosible();
			}
		});
		jRadioButton1.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				chkIsCreationPosible();
			}
		});
	}

}
