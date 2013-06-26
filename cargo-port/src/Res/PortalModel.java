package Res;

import CB_Core.GL_UI.render3D;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import de.gdxgame.IntVector3;
import de.gdxgame.Views.GameView;

/**
 * Enth‰llt die einzelnen Models des Portals<br>
 * * linker/rechter Fuﬂ<br>
 * * Arn<br>
 * * Katze<br>
 * 
 * @author Longri
 */
public class PortalModel implements render3D
{

	private ModelInstance mLegLeft, mLegRight, mJib, mRunWay;
	private boolean is3DInitial = false;
	private IntVector3 actPos;
	private float legOffset = 0;

	@Override
	public void render3d(ModelBatch modelBatch)
	{
		modelBatch.render(mRunWay, GameView.that.getLights());
		modelBatch.render(mLegLeft, GameView.that.getLights());
		modelBatch.render(mLegRight, GameView.that.getLights());
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
		mLegLeft = new ModelInstance(ResourceCache.getPortalLegLeftModel());
		mLegRight = new ModelInstance(ResourceCache.getPortalLegRightModel());
		mJib = new ModelInstance(ResourceCache.getPortalJibModel());
		mRunWay = new ModelInstance(ResourceCache.getPortalRunWay());

		// Calculate LegOffset
		BoundingBox bounds = new BoundingBox();
		mLegLeft.calculateBoundingBox(bounds);
		legOffset = bounds.getDimensions().x + (ResourceCache.getSize() / 2);

		// Calculat and set Leg z-scale
		Vector3 vec3 = GameView.that.getVectorPosition(GameView.that.getGameFieldDimensions());

		float zScale = vec3.z / bounds.getDimensions().y;
		mRunWay.transform.setToScaling(new Vector3(2, zScale, 3));
		mRunWay.calculateTransforms();
		is3DInitial = true;
	}

	public void setRunway2Vector(IntVector3 vector)
	{
		// set Runway
		{
			Vector3 vec3 = GameView.that.getVectorPosition(vector);
			if (vec3 != null) mRunWay.transform.setToTranslation(vec3);
		}

		// set legs
		{
			// set left
			{
				vector.setZ(0);
				vector.setX(0);
				Vector3 vec3 = GameView.that.getVectorPosition(vector);
				if (vec3 != null)
				{
					vec3.x -= legOffset;
					mLegLeft.transform.setToTranslation(vec3);
				}
			}

			// set right
			{
				int maxField = GameView.that.getMaxGameFieldX();

				vector.setZ(0);
				vector.setX(maxField - 1);
				Vector3 vec3 = GameView.that.getVectorPosition(vector);
				if (vec3 != null)
				{
					vec3.x += legOffset;
					mLegRight.transform.setToTranslation(vec3);
				}
			}

		}

	}
}
