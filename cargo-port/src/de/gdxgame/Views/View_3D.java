package de.gdxgame.Views;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import CB_UI_Base.GL_UI.CB_View_Base;
import CB_UI_Base.GL_UI.IRunOnGL;
import CB_UI_Base.GL_UI.render3D;
import CB_UI_Base.GL_UI.GL_Listener.GL;
import CB_UI_Base.Math.CB_RectF;
import CB_Utils.Log.Logger;
import CB_Utils.Math.Point;
import Res.PortalModel;
import Res.ResourceCache;
import Res.ResourceCache.IResourceChanged;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import de.gdxgame.GameCoord;
import de.gdxgame.GameInstructionPool.PoolType;
import de.gdxgame.GameSet;
import de.gdxgame.ThreadSafeList;
import de.gdxgame.Views.Actions.Animation;
import de.gdxgame.Views.Actions.AnimationList;
import de.gdxgame.Views.Actions.AnimationVector3;
import de.gdxgame.Views.Actions.ReadyHandler;

public class View_3D extends CB_View_Base implements render3D, IResourceChanged
{

	public static View_3D that;

	// InputProcessor
	private enum InputState
	{
		Idle, IdleDown, Button, Pan, Zoom, PanAutomatic, ZoomAutomatic
	}

	private InputState inputState = InputState.Idle;
	// speicher, welche Finger-Pointer aktuell gedr�ckt sind
	private final HashMap<Integer, Point> fingerDown = new LinkedHashMap<Integer, Point>();
	private KineticPan kineticPan = null;

	private PerspectiveCamera myCam;
	private boolean is3DInitial = false;

	private PoolType mLastPlayitPoolType;

	private final PortalModel mPortalModel = new PortalModel();
	private ModelInstance[][] GameField = new ModelInstance[1][1];
	Vector3[][][] GameFieldPositions = new Vector3[1][1][1];
	private ModelInstance[][][] GameVectorModels = new ModelInstance[1][1][1];
	private Environment lights;
	private final AnimationList mAnimationList = new AnimationList();
	private GameCoord mGameFieldDimensions;

	private final ThreadSafeList<ModelInstance> ModelList = new ThreadSafeList<ModelInstance>();
	AtomicBoolean waitOfAnimationReady;

	/**
	 * Zeit f�r eine Animation von einem Vector n�chsten Vector
	 */
	public static final int ANIMATION_TIME = 300;
	public static final int ANIMATION_WAIT_TIME = 100;
	public static final int FAST_ANIMATION_TIME = 100;
	public static final int FAST_ANIMATION_WAIT_TIME = 25;

	public static boolean fastAnimation = false;

	private float GameFieldWidth = 0;
	private float GameFieldHight = 0;
	private float GameFieldDepth = 0;

	GameSet myGameSet;

	float viewAngle = 0;
	float zoom = 80;
	float lastZoom = 80;

	public View_3D(CB_RectF rec, String Name)
	{
		super(rec, Name);
		ResourceCache.Add(this);
		that = this;
	}

	@Override
	public void Initial3D()
	{

		lights = new Environment();

		lights.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		is3DInitial = true;
	}

	@Override
	public void onShow()
	{
		super.onShow();
		GL.that.register3D(this);
	}

	void createGameField(final int countX, final int countY, final int countZ, final ReadyHandler handler)
	{
		mGameFieldDimensions = new GameCoord(countX - 1, countX - 1, countZ - 1);

		GL.that.RunOnGL(new IRunOnGL()
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

		GL.that.renderOnce();

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
	public void resourceChanged()
	{
		setLevel(myGameSet);
	}

	@Override
	public boolean onTouchDown(int x, int y, int pointer, int button)
	{

		if (pointer == MOUSE_WHEEL_POINTER_UP || pointer == MOUSE_WHEEL_POINTER_DOWN)
		{
			lastTouchPos = new Vector2(x, y);
			return true;
		}

		y = (int) (this.getHeight() - y);
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
			y = (int) (this.getHeight() - y);
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
						GL.that.renderOnce();
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

				GL.that.renderOnce();

				Point lastPoint = (Point) fingerDown.values().toArray()[0];

				float dx = (lastPoint.x - x);
				float dy = (y - lastPoint.y);

				// x Wert f�r Rotation

				if (y > this.getHalfHeight())
				{
					viewAngle += (dx / 10);
				}
				else
				{
					viewAngle -= (dx / 10);
				}

				if (x > this.getHalfWidth())
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

		y = (int) (this.getHeight() - y);

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
			GL.that.renderOnce();

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
		private final int[] x = new int[anzPoints];
		private final int[] y = new int[anzPoints];
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
		// mInstractionView.setGameSet(myGameSet);
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

	public Environment getLights()
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

	void normalMove()
	{
		synchronized (mAnimationList)
		{
			mAnimationList.clear();

			if (!myGameSet.boxAnimationStartCoord.isNull() && !myGameSet.boxAnimationTargetCoord.isNull())
			{
				Animation<Vector3> ani = animateBox(myGameSet.boxAnimationStartCoord, myGameSet.boxAnimationTargetCoord);
				mAnimationList.add(ani);
			}

			if (!myGameSet.craneAnimationStartCoord.isNull() && !myGameSet.craneAnimationTargetCoord.isNull())
			{
				Animation<Vector3> ani = animatePortal(myGameSet.craneAnimationStartCoord, myGameSet.craneAnimationTargetCoord);
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

	void grabMove()
	{
		synchronized (mAnimationList)
		{
			mAnimationList.clear();

			if (!myGameSet.boxAnimationStartCoord.isNull() && !myGameSet.boxAnimationTargetCoord.isNull())
			{
				Animation<Vector3> ani = animateBox(myGameSet.boxAnimationStartCoord, myGameSet.boxAnimationTargetCoord);
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

	void clashMove()
	{
		synchronized (mAnimationList)
		{
			mAnimationList.clear();

			if (!myGameSet.boxAnimationStartCoord.isNull() && !myGameSet.boxAnimationTargetCoord.isNull())
			{
				Animation<Vector3> ani = animateClashBox(myGameSet.boxAnimationStartCoord, myGameSet.boxAnimationTargetCoord);
				mAnimationList.add(ani);
			}

			if (!myGameSet.craneAnimationStartCoord.isNull() && !myGameSet.craneAnimationTargetCoord.isNull())
			{
				Animation<Vector3> ani = animatePortal(myGameSet.craneAnimationStartCoord, myGameSet.craneAnimationTargetCoord);
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

}
