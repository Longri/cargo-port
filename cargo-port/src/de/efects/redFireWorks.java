package de.efects;

import java.util.Random;

import CB_UI_Base.GL_UI.CB_View_Base;
import CB_UI_Base.GL_UI.IRunOnGL;
import CB_UI_Base.GL_UI.GL_Listener.GL;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.utils.Array;

public class redFireWorks extends CB_View_Base
{
	ParticleEffectPool redEffectPool, blueEffectPool;
	Array<PooledEffect> effects = new Array<PooledEffect>();
	long nextEffect;
	boolean isDisposed = false;

	public redFireWorks()
	{

		super(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), "Efect");

		GL.that.RunOnGL(new IRunOnGL()
		{

			@Override
			public void run()
			{
				ParticleEffect redFireEffect = new ParticleEffect();
				redFireEffect.load(Gdx.files.classpath("redFirework.p"), Gdx.files.classpath(""));
				redEffectPool = new ParticleEffectPool(redFireEffect, 5, 20);

				ParticleEffect blueFireEffect = new ParticleEffect();
				blueFireEffect.load(Gdx.files.classpath("blueFirework.p"), Gdx.files.classpath(""));
				blueEffectPool = new ParticleEffectPool(blueFireEffect, 5, 20);

				addEffect();
				GL.that.addRenderView(redFireWorks.this, GL.FRAME_RATE_FAST_ACTION);
			}
		});

	}

	private void addEffect()
	{
		if (isDisposed) return;
		nextEffect = System.currentTimeMillis() + zufallszahl(10, 2000);
		int minH = Gdx.graphics.getHeight() / 2;
		int maxH = Gdx.graphics.getHeight() - (minH / 5);

		if (zufallszahl(0, 1) == 0)
		{
			// Create effect:
			PooledEffect effect = redEffectPool.obtain();
			effect.setPosition(zufallszahl(0, Gdx.graphics.getWidth()), zufallszahl(minH, maxH));
			effects.add(effect);
		}
		else
		{
			// Create effect:
			PooledEffect effect = blueEffectPool.obtain();
			effect.setPosition(zufallszahl(0, Gdx.graphics.getWidth()), zufallszahl(minH, maxH));
			effects.add(effect);
		}

	}

	// Zufallszahl von "min"(einschlie�lich) bis "max"(einschlie�lich)
	// Beispiel: zufallszahl(4,10);
	// M�gliche Zufallszahlen 4,5,6,7,8,9,10
	public int zufallszahl(int min, int max)
	{
		Random random = new Random();
		return random.nextInt(max - min + 1) + min;
	}

	@Override
	public void render(Batch batch)
	{
		if (isDisposed) return;
		float delta = Gdx.graphics.getDeltaTime();

		try
		{
			if (System.currentTimeMillis() > nextEffect) addEffect();

			// Update and draw effects:
			for (int i = effects.size - 1; i >= 0; i--)
			{
				PooledEffect effect = effects.get(i);
				effect.draw(batch, delta);
				GL.that.renderOnce();
				if (effect.isComplete())
				{
					effect.free();
					effects.removeIndex(i);
				}
			}
		}
		catch (Exception e)
		{
			// if effectpool not initial try next
		}

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
	public void dispose()
	{
		super.dispose();
		GL.that.removeRenderView(this);
		for (int i = effects.size - 1; i >= 0; i--)
		{
			PooledEffect effect = effects.get(i);
			effect.free();
			effect = null;
		}
		effects.clear();
		effects = null;
		redEffectPool.clear();
		redEffectPool = null;
		blueEffectPool.clear();
		blueEffectPool = null;

	}
}
