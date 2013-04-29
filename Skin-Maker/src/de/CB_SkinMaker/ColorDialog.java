package de.CB_SkinMaker;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.gdxFrame.GdxFrame;

public class ColorDialog extends JFrame
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	LwjglAWTCanvas canvas1;

	public ColorDialog()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Container container = getContentPane();
		LwjglAWTCanvas canvas3 = new LwjglAWTCanvas(new WindowCreator(), false, canvas1);

		canvas3.getCanvas().setSize(200, 480);

		container.add(canvas3.getCanvas(), BorderLayout.LINE_END);

		pack();
		setVisible(true);
		setSize(800, 480);
	}

	class WindowCreator extends ApplicationAdapter
	{
		SpriteBatch batch;
		BitmapFont font;

		@Override
		public void create()
		{
			batch = new SpriteBatch();
			font = new BitmapFont();
		}

		@Override
		public void render()
		{
			Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
			batch.begin();
			font.draw(batch, "Click to create a new window", 10, 100);
			batch.end();

			if (Gdx.input.justTouched())
			{
				createWindow();
			}
		}

		private void createWindow()
		{
			JFrame window = new JFrame();
			LwjglAWTCanvas canvas = new LwjglAWTCanvas(new GdxFrame(200, 200), false, canvas1);
			window.getContentPane().add(canvas.getCanvas(), BorderLayout.CENTER);
			window.pack();
			window.setVisible(true);
			window.setSize(200, 200);
		}
	}

}
