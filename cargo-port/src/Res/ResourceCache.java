package Res;

import CB_Core.GL_UI.SpriteCache;

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

	private static Model model_Box, model_Box2, model_field1, model_field2;
	private static float size, halfsize;

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

			instance = null;

		}

		ModelBuilder modelBuilder = new ModelBuilder();

		model_Box2 = modelBuilder.createBox(size, size, size, new Material(ColorAttribute.createDiffuse(Color.GREEN)), Usage.Position
				| Usage.Normal);

		model_field1 = modelBuilder.createBox(size, 0.1f, size, new Material(ColorAttribute.createDiffuse(Color.BLACK)), Usage.Position
				| Usage.Normal | Usage.TextureCoordinates);

		model_field2 = modelBuilder.createBox(size, 0.1f, size, new Material(ColorAttribute.createDiffuse(Color.GRAY)), Usage.Position
				| Usage.Normal | Usage.TextureCoordinates);

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

	public static float getModelSize()
	{
		return size;
	}

	public static float getFieldHalfSize()
	{
		return halfsize;
	}
}
