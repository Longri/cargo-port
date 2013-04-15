package de.gdxFrame;

import CB_Core.GL_UI.GL_View_Base;
import CB_Core.GL_UI.Controls.Button;
import CB_Core.GL_UI.GL_Listener.GL;
import CB_Core.GL_UI.Main.MainViewBase;
import CB_Core.GL_UI.utils.ColorDrawable;

import com.badlogic.gdx.graphics.Color;

/**
 * @author Longri
 */
public class MainView extends MainViewBase
{

	public MainView(float X, float Y, float Width, float Height, String Name)
	{
		super(X, Y, Width, Height, Name);
		this.setBackground(new ColorDrawable(Color.DARK_GRAY));

		Button test = new Button("Test");

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
				return false;
			}
		});

		this.addChild(test);
	}
}
