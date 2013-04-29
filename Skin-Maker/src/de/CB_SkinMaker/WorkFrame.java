package de.CB_SkinMaker;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.json.JSONException;

import CB_Core.FileUtil;
import CB_Core.TranslationEngine.Translation;

public class WorkFrame extends JInternalFrame implements ComponentListener
{

	private static final long serialVersionUID = 1L;
	private Settings mSettings;

	private JButton btnGen;

	private JDesktopPane jDesktopPane1;
	private JTabbedPane jtp;
	private JScrollPane jsp;

	public WorkFrame(Settings settings)
	{

		super(settings.getName(), // title
				true, // resizable
				true, // closeable
				true, // maximizable
				true); // iconifiable
		mSettings = settings;
		mSettings = settings;

		mdir();

		this.setBounds(0, 0, 500, 300);
		this.addComponentListener(this);

		jDesktopPane1 = new JDesktopPane();
		jDesktopPane1.setVisible(true);
		getContentPane().add(jDesktopPane1, BorderLayout.CENTER);

		jtp = new JTabbedPane();
		jtp.setVisible(true);
		jtp.setBounds(0, 0, 100, 100);
		jDesktopPane1.add(jtp, BorderLayout.CENTER);
		JPanel jp1 = new JPanel();
		JPanel jp2 = new JPanel();

		jp1.setLayout(new GridLayout(1, 1));
		jp2.setLayout(new GridLayout(1, 1));

		jp1.setVisible(true);
		jp2.setVisible(true);

		JLabel label2 = new JLabel();
		label2.setText("You are in area of Tab2");
		jsp = initColorTab(WorkFrame.this.getWidth(), WorkFrame.this.getHeight());
		jp1.add(jsp);
		jp2.add(label2);
		jtp.addTab("Colors", jp1);
		jtp.addTab("Drawables", jp2);

		//

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

		JButton buttonLoad = new JButton();
		jDesktopPane1.add(buttonLoad, JLayeredPane.DEFAULT_LAYER);
		buttonLoad.setText(Translation.Get("save SVG Test"));
		buttonLoad.setBounds(162, 78, 150, 50);
		buttonLoad.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent evt)
			{

				try
				{
					Svg2Png("CARGO-PORT.svg", "CARGO-PORT.png", 317, 46);
					Svg2Png("CARGO-PORT.svg", "CARGO-PORT2.png", 634, 92);
					Svg2Png("CARGO-PORT.svg", "CARGO-PORT3.png", 951, 138);
					Svg2Png("CARGO-PORT.svg", "CARGO-PORT4.png", 1268, 184);

				}
				catch (TranscoderException | IOException e)
				{

					e.printStackTrace();
				}

			}

		});
	}

	private JScrollPane initColorTab(int width, int height)
	{

		Object[] dayColors = mSettings.getSkinJsonDay().colors.toArray();
		Object[] nightColors = mSettings.getSkinJsonNight().colors.toArray();
		int rowCount = dayColors.length + 1;

		JButton form[][] = new JButton[rowCount][3];

		String counts[] =
			{ "Color-Name", "Day-Value", "Night-Value", "" };

		JPanel p = new JPanel();

		GridLayout l = new GridLayout(rowCount, 5, 0, 0);
		p.setLayout(l);
		for (int row = 0; row < rowCount - 1; row++)
		{
			for (int col = 0; col < 4; col++)
			{
				if (row == 0)
				{
					p.add(new JLabel(counts[col]));
				}
				else
				{
					try
					{
						if (col == 3)
						{
							p.add(new JLabel(""));
						}
						else if (col == 0)
						{
							p.add(new JLabel(((colorEntry) dayColors[row - 1]).getName()));
						}
						else
						{
							JButton b = new JButton();
							if (col == 1)
							{
								b.setBackground(((colorEntry) dayColors[row - 1]).getAwtColor());
							}
							else
							{
								b.setBackground(((colorEntry) nightColors[row - 1]).getAwtColor());
							}

							form[row - 1][col - 1] = b;
							p.add(form[row - 1][col - 1]);
						}
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		}

		p.setSize(width + 200, height + 100);

		return new JScrollPane(p);
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

	private void Svg2Png(String SvgInputName, String PngOutputName, float OutputWidth, float OutputHeight) throws FileNotFoundException,
			TranscoderException, IOException
	{
		// Create a JPEG transcoder
		PNGTranscoder t = new PNGTranscoder();

		// Create the transcoder input.
		String svgURI = mSettings.getWorkPath() + "/" + SvgInputName;

		if (FileUtil.FileExists(svgURI))
		{
			// TranscoderInput input = new TranscoderInput(svgURI);
			InputStream istream = new FileInputStream(svgURI);
			TranscoderInput input = new TranscoderInput(istream);

			// Create the transcoder output.
			OutputStream ostream = new FileOutputStream(mSettings.getWorkPath() + "/" + PngOutputName);
			TranscoderOutput output = new TranscoderOutput(ostream);

			// Set output size
			t.addTranscodingHint(JPEGTranscoder.KEY_WIDTH, OutputWidth);
			t.addTranscodingHint(JPEGTranscoder.KEY_HEIGHT, OutputHeight);

			// Save the image.
			t.transcode(input, output);

			// Flush and close the stream.
			ostream.flush();
			ostream.close();
		}
	}

	@Override
	public void componentHidden(ComponentEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void componentMoved(ComponentEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void componentResized(ComponentEvent e)
	{
		maLayout();
	}

	@Override
	public void componentShown(ComponentEvent e)
	{
		maLayout();
	}

	private void maLayout()
	{
		jtp.setBounds(0, 0, WorkFrame.this.getWidth(), WorkFrame.this.getHeight());
		jsp = initColorTab(WorkFrame.this.getWidth(), WorkFrame.this.getHeight());
	}

}
