package de.CB_SkinMaker;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
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
	private JButton buttonCreate, btnSelect;
	private JLabel jLabel1;
	private JTextField jTextFieldWorkspace;

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
		buttonCreate.setBounds(600, 500, 150, 40);
		buttonCreate.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent evt)
			{
				new Settings();
				Settings.that.setWorkspace(jTextFieldWorkspace.getText());
				try
				{
					Settings.that.save();
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
		jLabel1.setBounds(12, 80, 170, 18);
		jLabel1.setForeground(new java.awt.Color(255, 255, 255));

		jTextFieldWorkspace = new JTextField();
		jDesktopPane1.add(jTextFieldWorkspace, JLayeredPane.DEFAULT_LAYER);
		jTextFieldWorkspace.setText("");
		jTextFieldWorkspace.setBounds((int) jLabel1.getBounds().getMaxX(), 75, 400, 30);
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
		btnSelect.setBounds((int) jTextFieldWorkspace.getBounds().getMaxX(), 75, 150, 30);
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

		buttonCreate.setEnabled(chk);
	}

}
