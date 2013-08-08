package de.gdxgame.Views;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import CB_Core.GL_UI.CB_View_Base;
import CB_Core.GL_UI.Fonts;
import CB_Core.GL_UI.render3D;
import CB_Core.GL_UI.runOnGL;
import CB_Core.GL_UI.Controls.MessageBox.GL_MsgBox;
import CB_Core.GL_UI.Controls.MessageBox.GL_MsgBox.OnMsgBoxClickListener;
import CB_Core.GL_UI.GL_Listener.GL;
import CB_Core.Log.Logger;
import CB_Core.Map.Point;
import CB_Core.Math.CB_RectF;
import Res.PortalModel;
import Res.ResourceCache;
import Res.ResourceCache.IResourceChanged;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.lights.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.lights.Lights;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import controls.InstructionSliderContent;
import controls.InstructionView;
import de.gdxgame.GameCoord;
import de.gdxgame.GameInstructionPool.PoolType;
import de.gdxgame.GameSet;
import de.gdxgame.GdxGame;
import de.gdxgame.ThreadSafeList;
import de.gdxgame.Views.Actions.Animation;
import de.gdxgame.Views.Actions.AnimationList;
import de.gdxgame.Views.Actions.AnimationVector3;
import de.gdxgame.Views.Actions.ReadyHandler;

/**
 * @author Lars Streblow
 * @author Longri
 */
public class GameView extends CB_View_Base implements render3D, IResourceChanged
{
	/**
	 * Static zugriff auf die aktuelle Instance
	 */
	public static GameView that;

	/**
	 * Zeit f�r eine Animation von einem Vector n�chsten Vector
	 */
	public static final int ANIMATION_TIME = 300;
	public static final int ANIMATION_WAIT_TIME = 100;
	public static final int FAST_ANIMATION_TIME = 100;
	public static final int FAST_ANIMATION_WAIT_TIME = 25;

	public static boolean fastAnimation = false;

	private ThreadSafeList<ModelInstance> ModelList = new ThreadSafeList<ModelInstance>();
	private AtomicBoolean waitOfAnimationReady;
	private GameSet myGameSet;

	private PortalModel mPortalModel = new PortalModel();
	private ModelInstance[][] GameField = new ModelInstance[1][1];
	private Vector3[][][] GameFieldPositions = new Vector3[1][1][1];
	private ModelInstance[][][] GameVectorModels = new ModelInstance[1][1][1];
	private Lights lights;
	private AnimationList mAnimationList = new AnimationList();
	private GameCoord mGameFieldDimensions;
	private PoolType mLastPlayitPoolType;

	private float GameFieldWidth = 0;
	private float GameFieldHight = 0;
	private float GameFieldDepth = 0;

	// InputProcessor
	private enum InputState
	{
		Idle, IdleDown, Button, Pan, Zoom, PanAutomatic, ZoomAutomatic
	}

	private InputState inputState = InputState.Idle;
	// speicher, welche Finger-Pointer aktuell gedr�ckt sind
	private HashMap<Integer, Point> fingerDown = new LinkedHashMap<Integer, Point>();
	private KineticPan kineticPan = null;

	float viewAngle = 0;
	float zoom = 80;
	float lastZoom = 80;

	private InstructionView mInstractionView;

	/**
	 * Constructor
	 * 
	 * @param rec
	 */
	public GameView(CB_RectF rec)
	{
		super(rec, "GameView");
		ResourceCache.Add(this);
		that = this;
		mInstractionView = new InstructionView(rec, myGameSet);
		mInstractionView.setHeight(this.height + mInstractionView.getTopHeight());
		this.addChild(mInstractionView);
	}

	@Override
	protected void Initial()
	{
	}

	@Override
	protected void SkinIsChanged()
	{
	}

	@Override
	public void onShow()
	{
		super.onShow();
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
		synchronized (GameField)
		{
			if (font == null)
			{
				font = Fonts.getNormal();
				font.setColor(Color.GREEN);

			}
			String str = "fps: " + Gdx.graphics.getFramesPerSecond();
			String str2 = "GameField:" + GameFieldPositions.length + "*" + GameFieldPositions[0].length + "*"
					+ GameFieldPositions[0][0].length;

			font.draw(batch, str, 20 * ResourceCache.getDpi(), 40 * ResourceCache.getDpi());
			font.draw(batch, str2, 20 * ResourceCache.getDpi(), 60 * ResourceCache.getDpi());
		}

	}

	@Override
	public void Initial3D()
	{

		lights = new Lights();
		lights.ambientLight.set(0.4f, 0.4f, 0.4f, 1f);
		lights.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		is3DInitial = true;
	}

	private void createGameField(final int countX, final int countY, final int countZ, final ReadyHandler handler)
	{
		mGameFieldDimensions = new GameCoord(countX - 1, countX - 1, countZ - 1);

		GL.that.RunOnGL(new runOnGL()
		{

			@Override
			public void run()
			{
				if (!is3DInitial) Initial3D();
				synchronized (GameField)
				{
					boolean lineGray = true;
					boolean rowGray = true;
					GameField = new ModelInstance[countX][countY];
					GameFieldPositions = new Vector3[countX][countY][countZ + 1];
					GameVectorModels = new ModelInstance[countX][countY][countZ + 1];
					float x = 0;
					float y = 0;
					float z = 0;

					for (int h = 0; h < countZ + 1; h++)
					{

						for (int i = 0; i < countX; i++)
						{
							lineGray = rowGray = !lineGray;
							for (int j = 0; j < countY; j++)
							{
								Vector3 vec = new Vector3(x, y, z);
								if (y == 0)
								{
									ModelInstance inst = new ModelInstance(rowGray ? ResourceCache.getField1Model() : ResourceCache
											.getField2Model());

									inst.transform.setToTranslation(vec);
									GameField[i][j] = inst;
								}
								vec = new Vector3(x, y + (ResourceCache.getHalfSize()), z);

								GameFieldPositions[i][j][h] = vec;
								rowGray = !rowGray;
								z += ResourceCache.getSize();
							}
							x += ResourceCache.getSize();
							GameFieldDepth = Math.max(GameFieldDepth, z - ResourceCache.getSize());
							z = 0;
						}
						GameFieldWidth = Math.max(GameFieldWidth, x - ResourceCache.getSize());

						z = 0;
						x = 0;
						y += ResourceCache.getSize();
					}

					GameFieldHight = y;

				}

				mPortalModel.Initial3D();

				handler.ready();
			}
		});
	}

	@Override
	public boolean is3D_Initial()
	{
		return is3DInitial;
	}

	@Override
	public void render3d(ModelBatch modelBatch)
	{

		synchronized (mAnimationList)
		{
			if (mAnimationList.isPlaying() && mAnimationList.size() > 0)
			{
				for (Animation<Vector3> ani : mAnimationList)
				{
					if (ani != null) ani.calcPositions();
				}
			}
		}

		synchronized (GameField)
		{
			// Render Game Field
			for (int i = 0; i < GameField.length; i++)
			{
				for (int j = 0; j < GameField[i].length; j++)
				{
					modelBatch.render(GameField[i][j], lights);
				}
			}
		}

		synchronized (ModelList)
		{
			if (ModelList.size() > 0)
			{
				for (ModelInstance inst : ModelList)
				{
					modelBatch.render(inst, lights);
				}
			}
		}

		if (mPortalModel != null) mPortalModel.render3d(modelBatch);

		GL.that.renderOnce("Test");

	}

	private PerspectiveCamera myCam;
	private boolean is3DInitial = false;

	// Zufallszahl von "min"(einschlie�lich) bis "max"(einschlie�lich)
	// Beispiel: zufallszahl(4,10);
	// M�gliche Zufallszahlen 4,5,6,7,8,9,10
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
			myCam = new PerspectiveCamera(zoom, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			myCam.position.set(GameFieldWidth / 2, GameFieldHight * 1.75f, GameFieldDepth * 2);
			myCam.lookAt(GameFieldWidth / 2, GameFieldHight * 0.25f, GameFieldDepth / 2);
			myCam.near = 0.1f;
			myCam.far = 300;
			myCam.rotateAround(new Vector3(GameFieldWidth / 2, 0, GameFieldDepth / 2), Vector3.Y, viewAngle);
			myCam.update();
		}
		if (cam3d != myCam) return myCam;

		if (viewAngle == 0 && lastZoom == zoom) return null;

		lastZoom = zoom;

		myCam.fieldOfView = zoom;
		myCam.rotateAround(new Vector3(GameFieldWidth / 2, 0, GameFieldDepth / 2), Vector3.Y, viewAngle);
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

			zoom += div / 100f;

			return true;
		}

		try
		{
			y = (int) (this.height - y);
			// debugString = "touchDragged: " + x + " - " + y;
			// debugString = "touchDragged " + inputState.toString();
			if (inputState == InputState.IdleDown)
			{
				// es wurde 1x gedr�ckt -> testen, ob ein gewisser Minimum Bereich verschoben wurde
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
				// wenn ein Button gedr�ckt war -> beim Verschieben nichts machen!!!
				return false;
			}

			if ((inputState == InputState.Pan) && (fingerDown.size() == 1))
			{

				GL.that.renderOnce(this.getName() + " Pan");

				Point lastPoint = (Point) fingerDown.values().toArray()[0];

				float dx = (lastPoint.x - x);
				float dy = (y - lastPoint.y);

				// x Wert f�r Rotation

				if (y > this.halfHeight)
				{
					viewAngle += (dx / 10);
				}
				else
				{
					viewAngle -= (dx / 10);
				}

				if (x > this.halfWidth)
				{
					viewAngle += (dy / 10);
				}
				else
				{
					viewAngle -= (dy / 10);
				}

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

				zoom += (originalDistance - currentDistance) / 20;

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
			// es wurde gedr�ckt, aber nich verschoben
			fingerDown.remove(pointer);
			inputState = InputState.Idle;
			// -> Buttons testen

			// auf Button Clicks nur reagieren, wenn aktuell noch kein Finger gedr�ckt ist!!!
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
		// benutze den Abstand der letzten 5 Positions�nderungen
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

	public void setLevel(GameSet level)
	{

		myGameSet = level;

		// create game field
		createGameField(level.getLevelDimensions().getX(), level.getLevelDimensions().getY(), level.getLevelDimensions().getZ(),
				new ReadyHandler()
				{

					@Override
					public void ready()
					{
						for (int i = 0; i < myGameSet.startFloor.getWidth(); i++)
						{
							for (int j = 0; j < myGameSet.startFloor.getLength(); j++)
							{
								for (int k = 0; k < myGameSet.startFloor.floorBoxes[i][j]; k++)
								{
									ModelInstance inst = new ModelInstance(ResourceCache.getBoxModel());
									inst.transform.setToTranslation(GameFieldPositions[i][j][k]);
									GameVectorModels[i][j][k] = inst;
									ModelList.add(inst);
								}
							}
						}

						mPortalModel.setRunway2Vector(new GameCoord(myGameSet.startCrane.getXPosition(), myGameSet.startCrane
								.getYPosition(), mGameFieldDimensions.getZ() + 1));

					}
				});

		// clear Animations
		synchronized (mAnimationList)
		{
			mAnimationList.clear();
		}

		// clear ModelList
		synchronized (level)
		{
			ModelList.clear();
		}
		mInstractionView.setGameSet(myGameSet);
	}

	public Vector3 getVectorPosition(GameCoord vector)
	{
		try
		{
			return GameFieldPositions[vector.getX()][vector.getY()][vector.getZ()].cpy();
		}
		catch (Exception e)
		{
			return null;
		}
	}

	public int getMaxGameFieldX()
	{
		return GameFieldPositions.length;
	}

	public int getMaxGameFieldZ()
	{
		return GameFieldPositions[0][0].length;
	}

	public Lights getLights()
	{
		return lights;
	}

	public GameCoord getGameFieldDimensions()
	{
		return mGameFieldDimensions;
	}

	public AnimationList animateBox(GameCoord start, GameCoord end)
	{
		try
		{
			// get Box
			ModelInstance box = GameVectorModels[start.getX()][start.getY()][start.getZ()];
			if (box != null)
			{
				AnimationVector3 ani = new AnimationVector3(box, GameFieldPositions[start.getX()][start.getY()][start.getZ()],
						GameFieldPositions[end.getX()][end.getY()][end.getZ()], fastAnimation ? FAST_ANIMATION_TIME : ANIMATION_TIME);

				// verschiebe GameVectorModel zur ZielPosition, wenn es etwas zu verschieben gibt
				if (end.getX() != start.getX() || end.getY() != start.getY() || end.getZ() != start.getZ())
				{
					GameVectorModels[end.getX()][end.getY()][end.getZ()] = box;
					GameVectorModels[start.getX()][start.getY()][start.getZ()] = null;
				}
				AnimationList list = new AnimationList();
				list.add(ani);
				list.trimToSize();
				return list;
			}
		}
		catch (java.lang.ArrayIndexOutOfBoundsException e)
		{
			Logger.Error("GameView", "ArrayIndexOutOfBoundsException", e);
		}
		return null;
	}

	public AnimationList animatePortal(GameCoord start, GameCoord end)
	{
		return mPortalModel.animatePortal(start, end);
	}

	public AnimationList animateClashBox(GameCoord start, GameCoord end)
	{
		try
		{
			// get Box
			ModelInstance box = GameVectorModels[start.getX()][start.getY()][start.getZ()];
			if (box != null)
			{
				// verschiebe Box von Start auf Ziel und lasse beide verschwinden
				AnimationVector3 ani = new AnimationVector3(box, GameFieldPositions[start.getX()][start.getY()][start.getZ()],
						GameFieldPositions[end.getX()][end.getY()][end.getZ()], fastAnimation ? FAST_ANIMATION_TIME : ANIMATION_TIME);
				ModelList.remove(GameVectorModels[start.getX()][start.getY()][start.getZ()]);
				GameVectorModels[start.getX()][start.getY()][start.getZ()] = null;
				ModelList.remove(GameVectorModels[end.getX()][end.getY()][end.getZ()]);
				GameVectorModels[end.getX()][end.getY()][end.getZ()] = null;
				AnimationList list = new AnimationList();
				list.add(ani);
				list.trimToSize();
				return list;
			}
		}
		catch (java.lang.ArrayIndexOutOfBoundsException e)
		{
			Logger.Error("GameView", "ArrayIndexOutOfBoundsException", e);
		}
		return null;
	}

	public void RunGameLoop(boolean FastAnimation)
	{
		fastAnimation = FastAnimation;
		Thread loop = new Thread(new Runnable()
		{

			@Override
			public void run()
			{
				// get gameSet from InstructionView
				myGameSet = mInstractionView.getGameSet();
				setLevel(myGameSet);

				myGameSet.startGame();
				int returnCode = 0;
				waitOfAnimationReady = new AtomicBoolean(false);
				System.out.println("Ausgangslage:");
				printGameSet();
				while (returnCode != -1)
				{
					if (waitOfAnimationReady.get())
					{
						try
						{
							Thread.sleep(fastAnimation ? FAST_ANIMATION_WAIT_TIME : ANIMATION_WAIT_TIME);
							continue;
						}
						catch (InterruptedException e)
						{
							e.printStackTrace();
						}
					}
					returnCode = myGameSet.runInstruction();

					{// set visual of instraction on InstractionPoolView-Slider
						if (mLastPlayitPoolType != myGameSet.getCurrentInstructionPoolType())
						{// change PoolType
							mLastPlayitPoolType = myGameSet.getCurrentInstructionPoolType();
							InstructionSliderContent.that.setActInstractionPool(myGameSet.getPool(mLastPlayitPoolType));
						}
						InstructionSliderContent.that.setActInstractionIndex(myGameSet.getInstructionIndex());
					}

					System.out.println("instructionCode: " + myGameSet.currentInstruction);
					System.out.println("returnCode: " + returnCode);
					switch (returnCode)
					{
					case 0: // normaler Zug
						normalMove();
						printGameSet();
						break;
					case -1: // Programmende erreicht
						if (myGameSet.gameAccomplished())
						{
							// show fireWork
							((GdxGame) (GdxGame.that)).showFireWork();
							GL_MsgBox.Show("Level gel�st", new OnMsgBoxClickListener()
							{

								@Override
								public boolean onClick(int which, Object data)
								{
									((GdxGame) (GdxGame.that)).disposeFireWork();
									// show LevelSelect and unlock next
									PlayView.that.unlockNextLevel();
									MainView.actionShowPlayView.Execute();
									return true;
								}
							});

							System.out.println("Level gel�st");
						}
						else
						{
							GL_MsgBox.Show("Level nicht gel�st");
							System.out.println("Level nicht gel�st");
						}

						printGameSet();
						break;
					case -2: // NOP-Code
						break;
					case -3: // Randzug
						System.out.println("versuchte Spielfeldrand�berschreitung verhindert");
						printGameSet();
						break;
					case -4: // Aufnahme/Ablegen
						if (myGameSet.currentCrane.isLoaded())
						{ // es wurde gerade aufgenommen
							// leere Hookanimation
							grabMove();
						}
						else
						{ // es wurde gerade abgelegt
							grabMove();
							// leere Hookanimation
						}
						System.out.println("Box aufgenommen/abgelegt");
						printGameSet();
						break;
					case -5: // leere Aufnahme
						System.out.println("Es gibt nichts aufzunehmen");
						printGameSet();
						break;
					case -6: // Zug l�st Kollision aus
						clashMove();
						System.out.println("Kollision ausgel�st");
						printGameSet();
						break;
					case -7: // func1/func2 called
						break;
					case -8: // return from func1/func2
						break;
					default:
					}
				}
			}
		});

		loop.start();

	}

	private void normalMove()
	{
		synchronized (mAnimationList)
		{
			mAnimationList.clear();

			if (!that.myGameSet.boxAnimationStartCoord.isNull() && !that.myGameSet.boxAnimationTargetCoord.isNull())
			{
				Animation<Vector3> ani = animateBox(that.myGameSet.boxAnimationStartCoord, that.myGameSet.boxAnimationTargetCoord);
				mAnimationList.add(ani);
			}

			if (!that.myGameSet.craneAnimationStartCoord.isNull() && !that.myGameSet.craneAnimationTargetCoord.isNull())
			{
				Animation<Vector3> ani = animatePortal(that.myGameSet.craneAnimationStartCoord, that.myGameSet.craneAnimationTargetCoord);
				mAnimationList.add(ani);
			}

			if (mAnimationList.size() > 0)
			{
				waitOfAnimationReady.set(true);
				mAnimationList.play(new ReadyHandler()
				{

					@Override
					public void ready()
					{
						waitOfAnimationReady.set(false);
					}
				});
			}
		}
	}

	private void grabMove()
	{
		synchronized (mAnimationList)
		{
			mAnimationList.clear();

			if (!that.myGameSet.boxAnimationStartCoord.isNull() && !that.myGameSet.boxAnimationTargetCoord.isNull())
			{
				Animation<Vector3> ani = animateBox(that.myGameSet.boxAnimationStartCoord, that.myGameSet.boxAnimationTargetCoord);
				mAnimationList.add(ani);
			}

			if (mAnimationList.size() > 0)
			{
				waitOfAnimationReady.set(true);
				mAnimationList.play(new ReadyHandler()
				{

					@Override
					public void ready()
					{
						waitOfAnimationReady.set(false);
					}
				});
			}
		}
	}

	private void clashMove()
	{
		synchronized (mAnimationList)
		{
			mAnimationList.clear();

			if (!that.myGameSet.boxAnimationStartCoord.isNull() && !that.myGameSet.boxAnimationTargetCoord.isNull())
			{
				Animation<Vector3> ani = animateClashBox(that.myGameSet.boxAnimationStartCoord, that.myGameSet.boxAnimationTargetCoord);
				mAnimationList.add(ani);
			}

			if (!that.myGameSet.craneAnimationStartCoord.isNull() && !that.myGameSet.craneAnimationTargetCoord.isNull())
			{
				Animation<Vector3> ani = animatePortal(that.myGameSet.craneAnimationStartCoord, that.myGameSet.craneAnimationTargetCoord);
				mAnimationList.add(ani);
			}

			if (mAnimationList.size() > 0)
			{
				waitOfAnimationReady.set(true);
				mAnimationList.play(new ReadyHandler()
				{

					@Override
					public void ready()
					{
						waitOfAnimationReady.set(false);
					}
				});
			}
		}
	}

	public static void printGameSet()
	{
		int x = 0;
		int y = 0;
		for (y = 0; y < that.myGameSet.currentFloor.getLength(); y++)
		{
			for (x = 0; x < that.myGameSet.currentFloor.getWidth(); x++)
			{
				System.out.print(that.myGameSet.currentFloor.getBoxes(x, y));
				if (that.myGameSet.currentCrane.getXPosition() == x && that.myGameSet.currentCrane.getYPosition() == y) if (that.myGameSet.currentCrane
						.isLoaded()) System.out.print("1");
				else
					System.out.print("0");
				else
					System.out.print(" ");
				if (x < that.myGameSet.currentFloor.getLength() - 1) System.out.print(" ");
			}
			System.out.println();
		}
		System.out.println();
	}

	@Override
	public void resourceChanged()
	{
		setLevel(myGameSet);
	}

}
