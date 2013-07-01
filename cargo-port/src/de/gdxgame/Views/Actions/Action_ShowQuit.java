package de.gdxgame.Views.Actions;

import CB_Core.Events.platformConector;
import CB_Core.GL_UI.Controls.MessageBox.GL_MsgBox;
import CB_Core.GL_UI.Controls.MessageBox.GL_MsgBox.OnMsgBoxClickListener;
import CB_Core.GL_UI.Controls.MessageBox.MessageBoxButtons;
import CB_Core.GL_UI.Controls.MessageBox.MessageBoxIcon;
import CB_Core.GL_UI.GL_Listener.GL;
import CB_Core.GL_UI.Main.Actions.CB_Action_ShowQuit;
import CB_Core.Log.Logger;
import CB_Core.TranslationEngine.Translation;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class Action_ShowQuit extends CB_Action_ShowQuit
{
	static GL_MsgBox msg;

	@Override
	public void Execute()
	{
		// if (askIsShown) return;

		if (msg != null && GL.that.actDialog == msg) return;

		String msgStr = Translation.Get("QuitReally").replace("Cachebox", "Cargo-Port");
		String title = Translation.Get("Quit?").replace("Cachebox", "Cargo-Port");

		msg = GL_MsgBox.Show(msgStr, title, MessageBoxButtons.OKCancel, MessageBoxIcon.Stop, new OnMsgBoxClickListener()
		{

			@Override
			public boolean onClick(int which, Object data)
			{
				if (which == GL_MsgBox.BUTTON_POSITIVE)
				{
					Logger.DEBUG("\r\n Quit");
					platformConector.callQuitt();
				}
				return true;
			}
		});
	}

	@Override
	public Sprite getIcon()
	{
		return null;// SpriteCache.Icons.get(IconName.close_31.ordinal());
	}
}
