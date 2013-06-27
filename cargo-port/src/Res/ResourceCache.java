package Res;

import CB_Core.GL_UI.SpriteCache;
import CB_Core.Math.UI_Size_Base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.materials.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.materials.Material;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.UBJsonReader;

public class ResourceCache extends SpriteCache
{

	private static Model model_Box, model_Box2, model_field1, model_field2, model_PortalLegBottom, model_PortalLegCenter,
			model_PortalLegTop, model_PortalJibLeft, model_PortalJibCenter, model_PortalJibRight, model_PortalRunWay;
	private static float size, halfsize, dpi;

	public static void LoadSprites(boolean reload)
	{
		SpriteCache.LoadSprites(reload);

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
