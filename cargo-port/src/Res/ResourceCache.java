package Res;

import java.util.ArrayList;

import CB_Core.GL_UI.ButtonSprites;
import CB_Core.GL_UI.SpriteCache;
import CB_Core.GL_UI.Skin.CB_Skin;
import CB_Core.GL_UI.Skin.SkinBase;
import CB_Core.GL_UI.utils.ColorDrawable;
import CB_Core.Math.UI_Size_Base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.materials.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.materials.Material;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.UBJsonReader;

public class ResourceCache extends SpriteCache
{

	private static Model model_Box, model_Box2, model_field1, model_field2, model_PortalLegBottom, model_PortalLegCenter,
			model_PortalLegTop, model_PortalJibLeft, model_PortalJibCenter, model_PortalJibRight, model_PortalRunWay;
	private static float size, halfsize, dpi;

	public static void LoadSprites(boolean reload)
	{
		if (!reload) setPath(CB_Skin.INSTANCE);

		loadDefaultUi();

		// Load Box Model and read Boundingbox for Size initialisations
		{

			// chk if g3db on root
			if (Gdx.files.absolute("./cube.g3db").exists())
			{
				model_Box = new G3dModelLoader(new UBJsonReader()).loadModel(Gdx.files.internal("./cube.g3db"));
			}
			else if (Gdx.files.absolute("./cube.obj").exists())
			{
				ObjLoader loader = new ObjLoader();
				model_Box = loader.loadModel(Gdx.files.internal("./cube.obj"));
			}
			else
			{
				model_Box = new G3dModelLoader(new UBJsonReader()).loadModel(Gdx.files.internal("skins/default/day/cube.g3db"));
			}

			ModelInstance instance = new ModelInstance(model_Box);
			BoundingBox box = new BoundingBox();
			instance.calculateBoundingBox(box);

			size = box.getDimensions().x;
			halfsize = size / 2;
			dpi = UI_Size_Base.that.getScale();
			instance = null;

		}

		ModelBuilder modelBuilder = new ModelBuilder();

		Color trans = new Color(0, 1, 0, 0.4f);

		model_Box2 = modelBuilder.createBox(size, size, size, new Material(ColorAttribute.createDiffuse(trans)), Usage.Position
				| Usage.Normal);

		model_field1 = modelBuilder.createBox(size, 0.1f, size, new Material(ColorAttribute.createDiffuse(Color.BLACK)), Usage.Position
				| Usage.Normal | Usage.TextureCoordinates);

		model_field2 = modelBuilder.createBox(size, 0.1f, size, new Material(ColorAttribute.createDiffuse(Color.GRAY)), Usage.Position
				| Usage.Normal | Usage.TextureCoordinates);

		initialPortalModels();

	}

	private static void loadDefaultUi()
	{
		if (ChkIcons == null) ChkIcons = new ArrayList<Sprite>();
		synchronized (ChkIcons)
		{

			ChkIcons.clear();
			ChkIcons.add(getThemedSprite("check-off"));
			ChkIcons.add(getThemedSprite("check-on"));

		}

		if (Dialog == null) Dialog = new ArrayList<Sprite>();
		synchronized (Dialog)
		{
			Dialog.clear();
			Dialog.add(getThemedSprite("dialog-header"));
			Dialog.add(getThemedSprite("dialog-center"));
			Dialog.add(getThemedSprite("dialog-footer"));
			Dialog.add(getThemedSprite("dialog-title"));
			Dialog.add(getThemedSprite("menu-divider"));

		}

		if (ToggleBtn == null) ToggleBtn = new ArrayList<Sprite>();
		synchronized (ToggleBtn)
		{
			ToggleBtn.clear();
			ToggleBtn.add(getThemedSprite("btn-normal"));
			ToggleBtn.add(getThemedSprite("btn-pressed"));
			ToggleBtn.add(getThemedSprite("toggle-led-gr"));

		}

		Progress = getThemedSprite("progress");

		if (ZoomBtn == null) ZoomBtn = new ArrayList<Sprite>();
		synchronized (ZoomBtn)
		{
			ZoomBtn.clear();
			ZoomBtn.add(getThemedSprite("day-btn-zoom-down-normal"));
			ZoomBtn.add(getThemedSprite("day-btn-zoom-down-pressed"));
			ZoomBtn.add(getThemedSprite("day-btn-zoom-down-disabled"));
			ZoomBtn.add(getThemedSprite("day-btn-zoom-up-normal"));
			ZoomBtn.add(getThemedSprite("day-btn-zoom-up-pressed"));
			ZoomBtn.add(getThemedSprite("day-btn-zoom-up-disabled"));

		}

		ZoomValueBack = getThemedSprite("zoom-back");

		loadButtnSprites();

		createDrawables();
	}

	protected static void loadButtnSprites()
	{
		CacheList = new ButtonSprites(getThemedSprite("db"), getThemedSprite("db-pressed"));

		Cache = new ButtonSprites(getThemedSprite("cache"), getThemedSprite("cache-pressed"));
		Nav = new ButtonSprites(getThemedSprite("Nav"), getThemedSprite("Nav-pressed"));
		Tool = new ButtonSprites(getThemedSprite("tool"), getThemedSprite("tool-pressed"));
		Misc = new ButtonSprites(getThemedSprite("misc"), getThemedSprite("misc-pressed"));
		// QuickButton = new ButtonSprites(getThemedSprite("button"), getThemedSprite("btn-pressed"));

	}

	protected static void createDrawables()
	{
		patch = (SpriteCache.getThemedSprite("activity-back").getWidth() > 60) ? 16 : 8;

		activityBackground = new NinePatchDrawable(new NinePatch(SpriteCache.getThemedSprite("activity-back"), patch, patch, patch, patch));
		activityBorderMask = new NinePatchDrawable(
				new NinePatch(SpriteCache.getThemedSprite("activity-border"), patch, patch, patch, patch));
		ListBack = new ColorDrawable(SkinBase.getThemedColor("background"));
		ButtonBack = new SpriteDrawable(getThemedSprite("button-list-back"));
		// AboutBack = new SpriteDrawable(getThemedSprite("splash-back"));
		// InfoBack = new NinePatchDrawable(new NinePatch(getThemedSprite("InfoPanelBack"), patch, patch, patch, patch));
		ProgressBack = new NinePatchDrawable(new NinePatch(ToggleBtn.get(0), patch, patch, patch, patch));
		ProgressFill = new NinePatchDrawable(new NinePatch(SpriteCache.Progress, patch - 1, patch - 1, patch - 1, patch - 1));
		btn = new NinePatchDrawable(new NinePatch(SpriteCache.getThemedSprite("btn-normal"), patch, patch, patch, patch));
		btnPressed = new NinePatchDrawable(new NinePatch(SpriteCache.getThemedSprite("btn-pressed"), patch, patch, patch, patch));
		btnDisabled = new NinePatchDrawable(new NinePatch(SpriteCache.getThemedSprite("btn-disabled"), patch, patch, patch, patch));

		chkOn = new SpriteDrawable(getThemedSprite("check-on"));
		chkOff = new SpriteDrawable(getThemedSprite("check-off"));
		chkOnDisabled = new SpriteDrawable(getThemedSprite("check-disable"));
		chkOffDisabled = new SpriteDrawable(getThemedSprite("check-off"));

		radioOn = new SpriteDrawable(getThemedSprite("RadioButtonSet"));
		radioBack = new SpriteDrawable(getThemedSprite("RadioButtonBack"));

		textFiledBackground = new NinePatchDrawable(new NinePatch(SpriteCache.getThemedSprite("text-field-back"), patch, patch, patch,
				patch));
		textFiledBackgroundFocus = new NinePatchDrawable(new NinePatch(SpriteCache.getThemedSprite("text-field-back-focus"), patch, patch,
				patch, patch));

		selection = new SpriteDrawable(getThemedSprite("Selection"));
		selection_set = new SpriteDrawable(getThemedSprite("Selection-set"));
		selection_left = new SpriteDrawable(getThemedSprite("Selection-Left"));
		selection_right = new SpriteDrawable(getThemedSprite("Selection-Right"));

		copy = new SpriteDrawable(getThemedSprite("tf-copy"));
		paste = new SpriteDrawable(getThemedSprite("tf-paste"));
		cut = new SpriteDrawable(getThemedSprite("tf-cut"));

		textFieldCursor = new NinePatchDrawable(new NinePatch(SpriteCache.getThemedSprite("selection-input-icon"), 1, 1, 2, 2));

	}

	private static void initialPortalModels()
	{
		ModelBuilder modelBuilder = new ModelBuilder();

		float dimens = size / 4;
		Material matBlue = new Material(ColorAttribute.createDiffuse(Color.BLUE));
		Material matGreen = new Material(ColorAttribute.createDiffuse(Color.GREEN));
		Material matRed = new Material(ColorAttribute.createDiffuse(Color.RED));
		long attributes = Usage.Position | Usage.Normal;

		model_PortalLegBottom = modelBuilder.createCylinder(dimens, size, dimens, 16, matBlue, attributes);
		model_PortalLegCenter = modelBuilder.createCylinder(dimens, size, dimens, 16, matGreen, attributes);
		model_PortalLegTop = modelBuilder.createCylinder(dimens, size, dimens, 16, matRed, attributes);
		model_PortalJibLeft = modelBuilder.createBox(size / 2, dimens, dimens, matBlue, attributes);
		model_PortalJibCenter = modelBuilder.createBox(size, dimens, dimens, matGreen, attributes);
		model_PortalJibRight = modelBuilder.createBox(size / 2, dimens, dimens, matRed, attributes);
		model_PortalRunWay = modelBuilder.createBox(dimens * 3, dimens * 2, dimens * 2,
				new Material(ColorAttribute.createDiffuse(Color.RED)), attributes);
	}

	public static Model getBoxModel()
	{
		return model_Box;
	}

	public static Model getBox2Model()
	{
		return model_Box2;
	}

	public static Model getField1Model()
	{
		return model_field1;
	}

	public static Model getField2Model()
	{
		return model_field2;
	}

	public static Model getPortalLegBottomModel()
	{
		return model_PortalLegBottom;
	}

	public static Model getPortalLegCenterModel()
	{
		return model_PortalLegCenter;
	}

	public static Model getPortalLegTopModel()
	{
		return model_PortalLegTop;
	}

	public static Model getPortalJibLeftModel()
	{
		return model_PortalJibLeft;
	}

	public static Model getPortalJibCenterModel()
	{
		return model_PortalJibCenter;
	}

	public static Model getPortalJibRightModel()
	{
		return model_PortalJibRight;
	}

	public static Model getPortalRunWay()
	{
		return model_PortalRunWay;
	}

	public static float getSize()
	{
		return size;
	}

	public static float getHalfSize()
	{
		return halfsize;
	}

	public static float getDpi()
	{
		return dpi;
	}

}
