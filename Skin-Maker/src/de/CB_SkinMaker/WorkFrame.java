package de.CB_SkinMaker;

import static java.lang.Math.random;

import javax.swing.JInternalFrame;

public class WorkFrame extends JInternalFrame
{

	private static final long serialVersionUID = 1L;

	public WorkFrame(Settings settings)
	{

		super(settings.getName(), // title
				true, // resizable
				true, // closeable
				true, // maximizable
				true); // iconifiable

		this.setBounds((int) (random() * 100), (int) (random() * 100), 100 + (int) (random() * 400), 100 + (int) (random() * 300));

		// JLabel jLabel1 = new JLabel();
		// this.add(jLabel1, JLayeredPane.DEFAULT_LAYER);
		// jLabel1.setText(Translation.Get("Work Frame"));
		// jLabel1.setBounds(12, 80, 170, 18);
		// jLabel1.setForeground(new java.awt.Color(255, 255, 255));

	}
}
