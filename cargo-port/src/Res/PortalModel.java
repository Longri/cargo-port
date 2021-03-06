package Res;

import CB_UI_Base.GL_UI.render3D;
import Res.ResourceCache.IResourceChanged;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import de.gdxgame.GameCoord;
import de.gdxgame.ThreadSafeList;
import de.gdxgame.Views.View_3D;
import de.gdxgame.Views.Actions.AnimationCallBack;
import de.gdxgame.Views.Actions.AnimationList;
import de.gdxgame.Views.Actions.AnimationVector3;

/**
 * Enth�llt die einzelnen Models des Portals<br>
 * * linker/rechter Fu�<br>
 * * Arn<br>
 * * Katze<br>
 * 
 * @author Longri
 */
public class PortalModel implements render3D, IResourceChanged
{

	ThreadSafeList<ModelInstance> mModelList, mLegCenterLeft, mLegCenterRight, mJibCenter;

	public PortalModel()
	{
		ResourceCache.Add(this);
	}

	private ModelInstance mLegBottomLeft, mLegTopLeft, mLegBottomRight, mLegTopRight, mJibLeft, mJibRight, mRunWay;

	private boolean is3DInitial = false;
	private float legOffset = 0;
	private float legWidth = 0;

	@Override
	public void render3d(ModelBatch modelBatch)
	{

		Environment lights = View_3D.that.getLights();

		for (ModelInstance inst : mModelList)
		{
			modelBatch.render(inst, lights);
		}
	}

	@Override
	public PerspectiveCamera get3DCamera(PerspectiveCamera cam3d)
	{
		return null;
	}

	@Override
	public boolean is3D_Initial()
	{
		return is3DInitial;
	}

	@Override
	public void Initial3D()
	{
		mModelList = new ThreadSafeList<ModelInstance>();

		int maxField = View_3D.that.getMaxGameFieldX();
		int maxFieldHeight = View_3D.that.getMaxGameFieldZ();

		mLegBottomLeft = new ModelInstance(ResourceCache.getPortalLegBottomModel());

		if (mLegCenterLeft == null) mLegCenterLeft = new ThreadSafeList<ModelInstance>();
		mLegCenterLeft.clear();
		for (int i = 0; i < maxFieldHeight - 2; i++)
		{
			mLegCenterLeft.add(new ModelInstance(ResourceCache.getPortalLegCenterModel()));
		}
		mLegCenterLeft.trimToSize();
		mLegTopLeft = new ModelInstance(ResourceCache.getPortalLegTopModel());

		mLegBottomRight = new ModelInstance(ResourceCache.getPortalLegBottomModel());

		if (mLegCenterRight == null) mLegCenterRight = new ThreadSafeList<ModelInstance>();
		mLegCenterRight.clear();
		for (int i = 0; i < maxFieldHeight - 2; i++)
		{
			mLegCenterRight.add(new ModelInstance(ResourceCache.getPortalLegCenterModel()));
		}
		mLegCenterRight.trimToSize();
		mLegTopRight = new ModelInstance(ResourceCache.getPortalLegTopModel());

		mJibLeft = new ModelInstance(ResourceCache.getPortalJibLeftModel());

		if (mJibCenter == null) mJibCenter = new ThreadSafeList<ModelInstance>();
		mJibCenter.clear();
		for (int i = 0; i < maxField; i++)
		{
			mJibCenter.add(new ModelInstance(ResourceCache.getPortalJibCenterModel()));
		}
		mLegCenterRight.trimToSize();
		mJibRight = new ModelInstance(ResourceCache.getPortalJibRightModel());
		mRunWay = new ModelInstance(ResourceCache.getPortalRunWay());

		mModelList.add(mLegBottomLeft);
		mModelList.addAll(mLegCenterLeft);
		mModelList.add(mLegTopLeft);

		mModelList.add(mLegBottomRight);
		mModelList.addAll(mLegCenterRight);
		mModelList.add(mLegTopRight);

		mModelList.add(mJibLeft);
		mModelList.addAll(mJibCenter);
		mModelList.add(mJibRight);

		mModelList.add(mRunWay);

		mModelList.trimToSize();

		// Calculate LegOffset
		BoundingBox bounds = new BoundingBox();
		mLegBottomLeft.calculateBoundingBox(bounds);
		legWidth = bounds.getDimensions().x;
		legOffset = legWidth + (ResourceCache.getSize() / 2);

		is3DInitial = true;
	}

	public void setRunway2Vector(GameCoord vector)
	{

		// set Runway
		{
			Vector3 vec3 = View_3D.that.getVectorPosition(vector);
			if (vec3 != null) mRunWay.transform.setToTranslation(vec3);
		}

		// set Positions from Left lower corner
		{
			vector.setZ(0);
			vector.setX(0);
			Vector3 vec3 = View_3D.that.getVectorPosition(vector);
			setPositionDependencys(vec3);

		}
	}

	/**
	 * Setze alle Models in Abh�ngigkeit der unteren linken Ecke des Portals
	 * 
	 * @param vec
	 */
	private void setPositionDependencys(Vector3 vector)
	{

		Vector3 vec = vector.cpy();

		float zeroPoint = vec.y;

		if (vec != null)
		{
			{// Left Leg
				vec.x -= legOffset;
				mLegBottomLeft.transform.setToTranslation(vec);
				for (ModelInstance ins : mLegCenterLeft)
				{
					vec.y += ResourceCache.getSize();
					ins.transform.setToTranslation(vec);
				}

				vec.y += ResourceCache.getSize();
				mLegTopLeft.transform.setToTranslation(vec);
			}

			{// Jib
				mJibLeft.transform.setToTranslation(vec);

				vec.x = 0 - ResourceCache.getSize();

				for (ModelInstance ins : mJibCenter)
				{
					vec.x += ResourceCache.getSize();
					ins.transform.setToTranslation(vec);
				}

				vec.x += legWidth + (ResourceCache.getSize() / 2);
				mJibRight.transform.setToTranslation(vec);
			}

			{// Right Leg
				vec.y = zeroPoint;
				mLegBottomRight.transform.setToTranslation(vec);

				for (ModelInstance ins : mLegCenterRight)
				{
					vec.y += ResourceCache.getSize();
					ins.transform.setToTranslation(vec);
				}

				vec.y += ResourceCache.getSize();
				mLegTopRight.transform.setToTranslation(vec);
			}

		}
	}

	public AnimationList animatePortal(GameCoord start, GameCoord end)
	{
		if (start.getY() == end.getY()) return animateRunWay(start, end);
		return animateY(start, end);
	}

	private AnimationList animateRunWay(GameCoord start, GameCoord end)
	{
		AnimationVector3 ani = new AnimationVector3(mRunWay, View_3D.that.getVectorPosition(new GameCoord(start.getX(), start.getY(), start
				.getZ())), View_3D.that.getVectorPosition(new GameCoord(end.getX(), end.getY(), end.getZ())),
				View_3D.fastAnimation ? View_3D.FAST_ANIMATION_TIME : View_3D.ANIMATION_TIME);

		AnimationList list = new AnimationList();
		list.add(ani);
		list.trimToSize();
		return list;
	}

	private AnimationList animateY(GameCoord start, GameCoord end)
	{
		AnimationList list = new AnimationList();

		AnimationVector3 ani = new AnimationVector3(mRunWay, View_3D.that.getVectorPosition(start), View_3D.that.getVectorPosition(end),
				View_3D.fastAnimation ? View_3D.FAST_ANIMATION_TIME : View_3D.ANIMATION_TIME);
		list.add(ani);

		// Animate Left Lower corner and set Dependencys
		start.setZ(0);
		start.setX(0);
		Vector3 vecStart = View_3D.that.getVectorPosition(start);

		end.setZ(0);
		end.setX(0);
		Vector3 vecEnd = View_3D.that.getVectorPosition(end);

		AnimationVector3 ani2 = new AnimationVector3(mLegBottomLeft, vecStart, vecEnd, View_3D.fastAnimation ? View_3D.FAST_ANIMATION_TIME
				: View_3D.ANIMATION_TIME);

		ani2.setAnimationCallBack(new AnimationCallBack<Vector3>()
		{

			@Override
			public void calculatedNewPos(Vector3 pos)
			{
				setPositionDependencys(pos);
			}
		});

		list.add(ani2);

		list.trimToSize();
		return list;
	}

	@Override
	public void resourceChanged()
	{
		Initial3D();
	}

}
