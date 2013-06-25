package de.gdxgame;

import CB_Core.Math.UI_Size_Base;

public class UiSizes extends CB_Core.Math.UiSizes
{
	public static UI_Size_Base that;

	public UiSizes()
	{
		super();
		that = this;
	}

	@Override
	public void instanzeInitial()
	{
		super.instanzeInitial();
	}
}
