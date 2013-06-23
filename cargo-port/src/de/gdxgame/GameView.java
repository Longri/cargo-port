package de.gdxgame;

import java.util.Random;

import CB_Core.GL_UI.CB_View_Base;
import CB_Core.GL_UI.Fonts;
import CB_Core.GL_UI.render3D;
import CB_Core.GL_UI.GL_Listener.GL;
import CB_Core.Math.UI_Size_Base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.lights.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.lights.Lights;
import com.badlogic.gdx.graphics.g3d.materials.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.materials.Material;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

public class GameView extends CB_View_Base implements render3D
{
	private final float SIZE_CONST = 5f;
	private float size = 1f;

	private Model model, field1, field2;
	private ModelInstance instance;
	private ModelInstance[][] GameField;
	private Vector3[][] GameFieldPositions;
	private Lights lights;
	float rotValue = 0;

	public GameView()
	{
		super(0, 0, MainView.Width(), MainView.Height(), "GameView");
		size = UI_Size_Base.that.getScale() * SIZE_CONST;
	}

	@Override
	protected void Initial()
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void SkinIsChanged()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onShow()
	{
		GL.that.register3D(this);
		GL.that.addRenderView(this, GL.FRAME_RATE_FAST_ACTION);
	}

	@Override
	public void onHide()
	{
		GL.that.unregister3D();
	}

	BitmapFont font;

	@Override
	protected void render(SpriteBatch batch)
	{
		if (font == null)
		{
			font = Fonts.getNormal();
			font.setColor(Color.BLACK);

		}
		String str = "fps: " + Gdx.graphics.getFramesPerSecond();
		String str2 = "GameField:" + GameField.length + "*" + GameField[0].length;

		font.setColor(Color.BLACK);
		font.draw(batch, str, 20, 40);
		font.draw(batch, str2, 20, 60);

		font.setColor(Color.WHITE);
		font.draw(batch, str, 100, 40);
		font.draw(batch, str2, 100, 60);

	}

	@Override
	public void Initial3D()
	{
		ModelBuilder modelBuilder = new ModelBuilder();
		model = modelBuilder.createBox(size, size, size, new Material(ColorAttribute.createDiffuse(Color.GREEN)), Usage.Position
				| Usage.Normal | Usage.TextureCoordinates);

		field1 = modelBuilder.createBox(size, 0.1f, size, new Material(ColorAttribute.createDiffuse(Color.BLACK)), Usage.Position
				| Usage.Normal | Usage.TextureCoordinates);

		field2 = modelBuilder.createBox(size, 0.1f, size, new Material(ColorAttribute.createDiffuse(Color.GRAY)), Usage.Position
				| Usage.Normal | Usage.TextureCoordinates);

		instance = new ModelInstance(model);
		instance.transform.setToTranslation(0, size / 2, 0);

		lights = new Lights();
		lights.ambientLight.set(0.4f, 0.4f, 0.4f, 1f);
		lights.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		createGameField(5, 5);

		is3DInitial = true;
	}

	private void createGameField(int countX, int countY)
	{
		boolean lineGray = true;
		boolean rowGray = true;
		GameField = new ModelInstance[countX][countY];
		GameFieldPositions = new Vector3[countX][countY];
		float x = 0;
		float y = 0;
		float z = 0;

		for (int i = 0; i < countX; i++)
		{
			lineGray = rowGray = !lineGray;
			for (int j = 0; j < countY; j++)
			{
				ModelInstance inst = new ModelInstance(rowGray ? field1 : field2);
				Vector3 vec = new Vector3(x, y, z);
				inst.transform.setToTranslation(vec);

				vec = new Vector3(x, size / 2, z);

				GameField[i][j] = inst;
				GameFieldPositions[i][j] = vec;
				rowGray = !rowGray;
				z += size;
			}
			x += size;
			z = 0;
		}
	}

	@Override
	public boolean is3D_Initial()
	{
		return is3DInitial;
	}

	@Override
	public void render3d(ModelBatch modelBatch)
	{

		// Render Game Field
		for (int i = 0; i < GameField.length; i++)
		{
			for (int j = 0; j < GameField[i].length; j++)
			{
				modelBatch.render(GameField[i][j], lights);
			}
		}

		if (instance != null)
		{
			++rotValue;
			if (rotValue >= 360)
			{
				createGameField(zufallszahl(5, 25), zufallszahl(5, 25));
				rotValue = 0;
			}

			modelBatch.render(instance, lights);

			poscount++;
			if (poscount > 20)
			{

				int zufallX = zufallszahl(0, GameField.length - 1);
				int zufallY = zufallszahl(0, GameField[0].length - 1);
				instance.transform.setToTranslation(GameFieldPositions[zufallX][zufallY]);
				poscount = 0;
			}

		}
		GL.that.renderOnce("Test");

	}

	private PerspectiveCamera myCam;
	private boolean is3DInitial = false;

	float value = 0;
	float zoom = 100;
	int poscount = 0;

	// Zufallszahl von "min"(einschließlich) bis "max"(einschließlich)
	// Beispiel: zufallszahl(4,10);
	// Mögliche Zufallszahlen 4,5,6,7,8,9,10
	public int zufallszahl(int min, int max)
	{
		Random random = new Random();
		return random.nextInt(max - min + 1) + min;
	}

	@Override
	public PerspectiveCamera get3DCamera(PerspectiveCamera cam3d)
	{
		if (myCam == null)
		{
			myCam = new PerspectiveCamera(20, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			myCam.position.set(100f, 40f, 100f);
			myCam.lookAt(0, 0, 0);
			myCam.near = 0.1f;
			myCam.far = 300;
			myCam.update();
		}
		// if (cam3d != myCam) return myCam;

		myCam.position.set(zoom, value, zoom);
		myCam.update();
		value += 0.3;
		zoom += 0.3;
		if (value > 70) value = 20;
		if (zoom > 200) zoom = 40;
		return myCam;
	}

}
