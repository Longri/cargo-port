package controls;

import CB_Core.GL_UI.ButtonSprites;
import Res.ResourceCache;

public class InstBtnSprites extends ButtonSprites
{

	public static final InstBtnSprites INSTANCE = new InstBtnSprites();

	public InstBtnSprites()
	{
		super(ResourceCache.getThemedSprite("InstBack-Normal"), ResourceCache.getThemedSprite("InstBack-Pressed"), ResourceCache
				.getThemedSprite("InstBack-Normal"), ResourceCache.getThemedSprite("InstBack-Focus"));

	}

}
