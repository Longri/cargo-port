package de.gdxgame.Views.Actions;

import CB_UI_Base.Events.platformConector;
import CB_UI_Base.Events.platformConector.IgetFileReturnListner;
import CB_UI_Base.GL_UI.GL_View_Base;
import CB_UI_Base.GL_UI.GL_View_Base.OnClickListener;
import CB_UI_Base.GL_UI.SpriteCacheBase;
import CB_UI_Base.GL_UI.SpriteCacheBase.IconName;
import CB_UI_Base.GL_UI.Main.Actions.CB_Action;
import CB_UI_Base.GL_UI.Menu.Menu;
import CB_UI_Base.GL_UI.Menu.MenuItem;
import Res.ResourceCache;

import com.badlogic.gdx.graphics.g2d.Sprite;

import de.gdxgame.Views.ViewIDs;

public class Action_Load_Model extends CB_Action
{

	public Action_Load_Model()
	{
		super("LoadModel", "", ViewIDs.PLAY_VIEW);
	}

	@Override
	public void Execute()
	{
		Menu cm = new Menu("ModdelLoad");

		cm.addItemClickListner(MenuItemClickListner);

		cm.addItem(0, "", "Load Box");
		cm.addItem(1, "", "reset Box");

		cm.addItem(2, "", "Load Katze");
		cm.addItem(3, "", "reset Katze");

		cm.addItem(4, "", "Load Fuﬂ unten");
		cm.addItem(5, "", "reset Fuﬂ unten");

		cm.addItem(6, "", "Load Fuﬂ center");
		cm.addItem(7, "", "reset Fuﬂ center");

		cm.addItem(8, "", "Load Fuﬂ oben");
		cm.addItem(9, "", "reset Fuﬂ oben");

		cm.addItem(10, "", "Load Arm links");
		cm.addItem(11, "", "reset Arm links");

		cm.addItem(12, "", "Load Arm center");
		cm.addItem(13, "", "reset Arm center");

		cm.addItem(14, "", "Load Arm rechts");
		cm.addItem(15, "", "reset Arm rechts");

		cm.Show();
	}

	OnClickListener MenuItemClickListner = new OnClickListener()
	{

		@Override
		public boolean onClick(GL_View_Base v, int x, int y, int pointer, int button)
		{
			int index = ((MenuItem) v).getMenuItemId();

			switch (index)
			{
			case 0:
				platformConector.getFile("", "", "Load", "Load", new IgetFileReturnListner()
				{

					@Override
					public void getFieleReturn(String Path)
					{
						ResourceCache.loadBoxModel(Path);
					}
				});
				break;
			case 1:
				ResourceCache.resetBoxModel();
				break;

			case 2:
				platformConector.getFile("", "", "Load", "Load", new IgetFileReturnListner()
				{
					@Override
					public void getFieleReturn(String Path)
					{
						ResourceCache.loadRunWayModel(Path);
					}
				});
				break;
			case 3:
				ResourceCache.resetRunWayModel();
				break;

			case 4:
				platformConector.getFile("", "", "Load", "Load", new IgetFileReturnListner()
				{
					@Override
					public void getFieleReturn(String Path)
					{
						ResourceCache.loadPortalLegBottomModel(Path);
					}
				});
				break;
			case 5:
				ResourceCache.resetPortalLegBottomModel();
				break;
			case 6:
				platformConector.getFile("", "", "Load", "Load", new IgetFileReturnListner()
				{
					@Override
					public void getFieleReturn(String Path)
					{
						ResourceCache.loadPortalLegCenterModel(Path);
					}
				});
				break;
			case 7:
				ResourceCache.resetPortalLegCenterModel();
				break;
			case 8:
				platformConector.getFile("", "", "Load", "Load", new IgetFileReturnListner()
				{
					@Override
					public void getFieleReturn(String Path)
					{
						ResourceCache.loadPortalLegTopModel(Path);
					}
				});
				break;
			case 9:
				ResourceCache.resetPortalLegTopModel();
				break;
			case 10:
				platformConector.getFile("", "", "Load", "Load", new IgetFileReturnListner()
				{
					@Override
					public void getFieleReturn(String Path)
					{
						ResourceCache.loadPortalJibLeftModel(Path);
					}
				});
				break;
			case 11:
				ResourceCache.resetPortalJibLeftModel();
				break;
			case 12:
				platformConector.getFile("", "", "Load", "Load", new IgetFileReturnListner()
				{
					@Override
					public void getFieleReturn(String Path)
					{
						ResourceCache.loadPortalJibCenterModel(Path);
					}
				});
				break;
			case 13:
				ResourceCache.resetPortalJibCenterModel();
				break;
			case 14:
				platformConector.getFile("", "", "Load", "Load", new IgetFileReturnListner()
				{
					@Override
					public void getFieleReturn(String Path)
					{
						ResourceCache.loadPortalJibRightModel(Path);
					}
				});
				break;
			case 15:
				ResourceCache.resetPortalJibRightModel();
				break;

			}

			return false;
		}
	};

	@Override
	public boolean getEnabled()
	{
		return true;
	}

	@Override
	public Sprite getIcon()
	{
		return SpriteCacheBase.Icons.get(IconName.cacheList_7.ordinal());
	}

}
