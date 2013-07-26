package de.efects;

import CB_Core.GL_UI.CB_View_Base;
import CB_Core.GL_UI.GL_Listener.GL;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class Explosion extends CB_View_Base
{
	ParticleEffectPool bombEffectPool;
	Array<PooledEffect> effects = new Array<PooledEffect>();

	public Explosion()
	{

		super(300, 300, 300, 300, "Efect");
		ParticleEffect bombEffect = new ParticleEffect();
		bombEffect.load(Gdx.files.classpath("explosion.p"), Gdx.files.classpath(""));
		bombEffectPool = new ParticleEffectPool(bombEffect, 1, 2);

		// Create effect:
		PooledEffect effect = bombEffectPool.obtain();
		effect.setPosition(100, 100);
		effects.add(effect);

	}

	@Override
	public void render(SpriteBatch batch)
	{

		float delta = Gdx.graphics.getDeltaTime();

		// Update and draw effects:
		for (int i = effects.size - 1; i >= 0; i--)
		{
			PooledEffect effect = effects.get(i);
			effect.draw(batch, delta);
			GL.that.renderOnce("efect");
			if (effect.isComplete())
			{
				effect.free();
				effects.removeIndex(i);
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
