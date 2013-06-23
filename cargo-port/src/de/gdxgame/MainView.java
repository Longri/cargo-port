package de.gdxgame;

import CB_Core.GL_UI.GL_View_Base;
import CB_Core.GL_UI.Controls.Button;
import CB_Core.GL_UI.GL_Listener.GL;
import CB_Core.GL_UI.Main.TabMainView;
import CB_Core.GL_UI.utils.ColorDrawable;

import com.badlogic.gdx.graphics.Color;

/**
 * @author Longri
 */
public class MainView extends TabMainView
{

	private static MainView that;
	private GameView gameView;

	public static float Width()
	{
		if (that != null) return that.getWidth();
		return 0;
	}

	public static float Height()
	{
		if (that != null) return that.getHeight();
		return 0;
	}

	public MainView(float X, float Y, float Width, float Height, String Name)
	{
		super(X, Y, Width, Height, Name);
		that = this;
		gameView = new GameView();

	}

	@Override
	public void Initial()
	{
		// this.setBackground(new ColorDrawable(Color.DARK_GRAY));

		final Button test = new Button("Test");

		test.setText("Test");
		test.setPos(300, 500);

		test.setninePatch(new ColorDrawable(Color.GREEN));
		test.setninePatchPressed(new ColorDrawable(Color.RED));

		test.setOnClickListener(new OnClickListener()
		{

			@Override
			public boolean onClick(GL_View_Base arg0, int arg1, int arg2, int arg3, int arg4)
			{
				GL.that.Toast("Get Doch");
				test.setVisible(false);
				MainView.this.addChild(gameView);
				gameView.onShow();
				return true;
			}
		});

		this.addChild(test);

	}
}
