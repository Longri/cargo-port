package de.gdxgame;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;

import CB_Core.GL_UI.CB_View_Base;
import CB_Core.GL_UI.Fonts;
import CB_Core.GL_UI.render3D;
import CB_Core.GL_UI.GL_Listener.GL;
import CB_Core.Log.Logger;
import CB_Core.Map.Point;
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
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.materials.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.materials.Material;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.UBJsonReader;

public class GameView extends CB_View_Base implements render3D
{
	private final float SIZE_CONST = 5f;
	private float size = 1f;

	private float dpi = 1;

	private Model model, field1, field2;
	private ModelInstance instance, instance2;
	private ModelInstance[][] GameField;
	private Vector3[][] GameFieldPositions;
	private Lights lights;

	// InputProcessor
	public enum InputState
	{
		Idle, IdleDown, Button, Pan, Zoom, PanAutomatic, ZoomAutomatic
	}

	private InputState inputState = InputState.Idle;
	// speicher, welche Finger-Pointer aktuell gedrückt sind
	private HashMap<Integer, Point> fingerDown = new LinkedHashMap<Integer, Point>();
	private KineticPan kineticPan = null;

	float viewAngle = 0;
	float zoom = 60;

	public GameView()
	{
		super(0, 0, MainView.Width(), MainView.Height(), "GameView");
		dpi = UI_Size_Base.that.getScale();
		size = dpi * SIZE_CONST;
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
			font.setColor(Color.GREEN);

		}
		String str = "fps: " + Gdx.graphics.getFramesPerSecond();
		String str2 = "GameField:" + GameField.length + "*" + GameField[0].length;

		font.draw(batch, str, 20 * dpi, 40 * dpi);
		font.draw(batch, str2, 20 * dpi, 60 * dpi);
	}

	@Override
	public void Initial3D()
	{
		ModelBuilder modelBuilder = new ModelBuilder();
		// model = modelBuilder.createBox(size, size, size, new Material(ColorAttribute.createDiffuse(Color.GREEN)), Usage.Position
		// | Usage.Normal | Usage.TextureCoordinates);

		model = new G3dModelLoader(new UBJsonReader()).loadModel(Gdx.files.internal("skins/default/day/cube.g3db"));

		instance = new ModelInstance(model);

		BoundingBox box = new BoundingBox();
		instance.calculateBoundingBox(box);

		size = box.getDimensions().x;

		instance.transform.setToScaling(dpi, dpi, dpi);
		instance.calculateTransforms();

		instance.transform.setToTranslation(0, size / 2, 0);
		instance.calculateTransforms();

		model = modelBuilder.createBox(size, size, size, new Material(ColorAttribute.createDiffuse(Color.GREEN)), Usage.Position
				| Usage.Normal);
		instance2 = new ModelInstance(model);

		field1 = modelBuilder.createBox(size, 0.1f, size, new Material(ColorAttribute.createDiffuse(Color.BLACK)), Usage.Position
				| Usage.Normal | Usage.TextureCoordinates);

		field2 = modelBuilder.createBox(size, 0.1f, size, new Material(ColorAttribute.createDiffuse(Color.GRAY)), Usage.Position
				| Usage.Normal | Usage.TextureCoordinates);

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

		instance2.transform.translate(GameFieldPositions[1][2]);
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
			modelBatch.render(instance, lights);
		}

		if (instance2 != null)
		{
			modelBatch.render(instance2, lights);
		}

		GL.that.renderOnce("Test");

	}

	private PerspectiveCamera myCam;
	private boolean is3DInitial = false;

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
			myCam.position.set(100f, 100f, 100f);
			myCam.lookAt(2.5f * size, 0, 2.5f * size);
			myCam.near = 0.1f;
			myCam.far = 300;
			myCam.rotateAround(new Vector3(50f, 20f, 50f), Vector3.Y, viewAngle);
			myCam.update();
		}
		if (cam3d != myCam) return myCam;

		if (viewAngle == 0) return null;

		myCam.fieldOfView = zoom;
		myCam.rotateAround(new Vector3(2.5f * size, 0, 2.5f * size), Vector3.Y, viewAngle);
		myCam.update();
		viewAngle = 0;
		return myCam;
	}

	@Override
	public boolean onTouchDown(int x, int y, int pointer, int button)
	{

		if (pointer == MOUSE_WHEEL_POINTER_UP || pointer == MOUSE_WHEEL_POINTER_DOWN)
		{
			lastTouchPos = new Vector2(x, y);
			return true;
		}

		y = (int) (this.height - y);
		// debugString = "touchDown " + x + " - " + y;
		if (inputState == InputState.Idle)
		{
			fingerDown.clear();
			inputState = InputState.IdleDown;
			fingerDown.put(pointer, new Point(x, y));
		}
		else
		{
			fingerDown.put(pointer, new Point(x, y));
			if (fingerDown.size() == 2) inputState = InputState.Zoom;
		}

		return true;
	}

	@Override
	public boolean onTouchDragged(int x, int y, int pointer, boolean KineticPan)
	{

		if (pointer == MOUSE_WHEEL_POINTER_UP || pointer == MOUSE_WHEEL_POINTER_DOWN)
		{
			// Mouse wheel scrolling => Zoom in/out

			float div = lastTouchPos.y - y;

			zoom = div / 100f;

			return true;
		}

		try
		{
			y = (int) (this.height - y);
			// debugString = "touchDragged: " + x + " - " + y;
			// debugString = "touchDragged " + inputState.toString();
			if (inputState == InputState.IdleDown)
			{
				// es wurde 1x gedrückt -> testen, ob ein gewisser Minimum Bereich verschoben wurde
				Point p = fingerDown.get(pointer);
				if (p != null)
				{
					// if ((Math.abs(p.x - x) > 10) || (Math.abs(p.y - y) > 10)) // this check is not necessary because this is already
					// checked in GL.java
					{
						inputState = InputState.Pan;
						// GL_Listener.glListener.addRenderView(this, frameRateAction);
						GL.that.renderOnce(this.getName() + " Dragged");
						// xxx startTimer(frameRateAction);
						// xxx ((GLSurfaceView) MapViewGL.ViewGl).requestRender();
					}
					return false;
				}
			}
			if (inputState == InputState.Button)
			{
				// wenn ein Button gedrückt war -> beim Verschieben nichts machen!!!
				return false;
			}

			if ((inputState == InputState.Pan) && (fingerDown.size() == 1))
			{

				GL.that.renderOnce(this.getName() + " Pan");

				Point lastPoint = (Point) fingerDown.values().toArray()[0];

				float dx = (lastPoint.x - x);
				float dy = (y - lastPoint.y);

				// x Wert für Rotation
				viewAngle = viewAngle + (dx / 10);

				// y Wert für Zoom
				zoom = zoom + (dy / 10);

				lastPoint.x = x;
				lastPoint.y = y;
			}
			else if ((inputState == InputState.Zoom) && (fingerDown.size() == 2))
			{
				Point p1 = (Point) fingerDown.values().toArray()[0];
				Point p2 = (Point) fingerDown.values().toArray()[1];
				float originalDistance = (float) Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2));

				if (fingerDown.containsKey(pointer))
				{
					// neue Werte setzen
					fingerDown.get(pointer).x = x;
					fingerDown.get(pointer).y = y;
					p1 = (Point) fingerDown.values().toArray()[0];
					p2 = (Point) fingerDown.values().toArray()[1];
				}
				float currentDistance = (float) Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2));
				float ratio = originalDistance / currentDistance;

				return true;
			}

			// debugString = "State: " + inputState;
			return true;
		}
		catch (Exception ex)
		{
			Logger.Error("MapView", "-onTouchDragged Error", ex);
		}
		return false;
	}

	@Override
	public boolean onTouchUp(int x, int y, int pointer, int button)
	{

		if (pointer == MOUSE_WHEEL_POINTER_UP || pointer == MOUSE_WHEEL_POINTER_DOWN)
		{
			return true;
		}

		y = (int) (this.height - y);

		if (inputState == InputState.IdleDown)
		{
			// es wurde gedrückt, aber nich verschoben
			fingerDown.remove(pointer);
			inputState = InputState.Idle;
			// -> Buttons testen

			// auf Button Clicks nur reagieren, wenn aktuell noch kein Finger gedrückt ist!!!
			if (kineticPan != null)
			// bei FingerKlick (wenn Idle) sofort das kinetische Scrollen stoppen
			kineticPan = null;

			inputState = InputState.Idle;
			return false;
		}

		fingerDown.remove(pointer);
		if (fingerDown.size() == 1) inputState = InputState.Pan;
		else if (fingerDown.size() == 0)
		{
			inputState = InputState.Idle;
			// wieder langsam rendern
			GL.that.renderOnce(this.getName() + " touchUp");

			if (kineticPan != null) kineticPan.start();
		}

		// debugString = "State: " + inputState;

		return true;
	}

	protected class KineticPan
	{
		private boolean started;
		private boolean fertig;
		// benutze den Abstand der letzten 5 Positionsänderungen
		final int anzPoints = 3;
		private int[] x = new int[anzPoints];
		private int[] y = new int[anzPoints];
		private int diffX;
		private int diffY;
		private long startTs;
		private long endTs;

		public KineticPan()
		{
			fertig = false;
			started = false;
			diffX = 0;
			diffY = 0;
			for (int i = 0; i < anzPoints; i++)
			{
				x[i] = 0;
				y[i] = 0;
			}
		}

		public void setLast(long aktTs, int aktX, int aktY)
		{
			for (int i = anzPoints - 2; i >= 0; i--)
			{
				x[i + 1] = x[i];
				y[i + 1] = y[i];
			}
			x[0] = aktX;
			y[0] = aktY;

			for (int i = 1; i < anzPoints; i++)
			{
				if (x[i] == 0) x[i] = x[i - 1];
				if (y[i] == 0) y[i] = y[i - 1];
			}
			diffX = x[anzPoints - 1] - aktX;
			diffY = aktY - y[anzPoints - 1];

			// debugString = x[2] + " - " + x[1] + " - " + x[0];
		}

		public boolean getFertig()
		{
			return fertig;
		}

		public boolean getStarted()
		{
			return started;
		}

		public void start()
		{
			startTs = System.currentTimeMillis();
			int abstand = (int) Math.sqrt(Math.pow(diffX, 2) + Math.pow(diffY, 2));

			endTs = startTs + 2000 + abstand * 50 / anzPoints;
			started = true;
		}

		public Point getAktPan()
		{
			Point result = new Point(0, 0);

			long aktTs = System.currentTimeMillis();
			float faktor = (float) (aktTs - startTs) / (float) (endTs - startTs);
			faktor = com.badlogic.gdx.math.Interpolation.exp10Out.apply(faktor);
			if (faktor >= 1)
			{
				fertig = true;
				faktor = 1;
			}

			result.x = (int) (diffX / anzPoints * (1 - faktor));
			result.y = (int) (diffY / anzPoints * (1 - faktor));
			return result;
		}
	}

}
