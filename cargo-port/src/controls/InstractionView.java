package controls;

import CB_Core.GL_UI.CB_View_Base;
import CB_Core.GL_UI.GL_View_Base;
import CB_Core.GL_UI.Handler;
import CB_Core.GL_UI.SpriteCache;
import CB_Core.GL_UI.runOnGL;
import CB_Core.GL_UI.Controls.Button;
import CB_Core.GL_UI.GL_Listener.GL;
import CB_Core.Math.CB_RectF;
import CB_Core.Math.UI_Size_Base;
import CB_Core.Math.UiSizes;

import com.badlogic.gdx.graphics.Color;

import controls.InstructionBox.ISelected;
import de.gdxgame.GameSet;

public class InstractionView extends CB_View_Base
{
	private final int ANIMATION_TIME = 50;// 50;

	private Handler handler = new Handler();
	private Button slideButton;
	private boolean swipeUp = false;
	private boolean swipeDown = false;

	private boolean AnimationIsRunning = false;
	private final double AnimationMulti = 1.4;
	private int AnimationDirection = -1;
	private float AnimationTarget = 0;
	private boolean isKinetigPan = false;
	private float touchYoffset = 0;
	private float yPos = 0;
	private boolean isShowing = false;
	private GameSet myGameSet;
	private InstractionSelect sel;
	private int mSelectedPool = -1;

	private InstructionPoolView main, func1, func2;

	public InstractionView(CB_RectF rec, GameSet level)
	{
		super(rec, "InstractionView");
		this.setBackground(SpriteCache.activityBackground);
		this.setColorFilter(new Color(1f, 1f, 1f, 0.6f));
		this.myGameSet = level;

		slideButton = new Button("");
		slideButton.setWidth(this.width);
		slideButton.setHeight(UiSizes.that.getButtonHeight() / 2);
		slideButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public boolean onClick(GL_View_Base v, int x, int y, int pointer, int button)
			{
				ToggleView();
				return true;
			}
		});
		this.addChild(slideButton);

		rec.setHeight(this.height - slideButton.getHeight());
		sel = new InstractionSelect(this, "select");
		sel.setZeroPos();
		sel.setY(slideButton.getMaxY());
		this.addChild(sel);
		setGameSet(myGameSet);
	}

	@Override
	protected void Initial()
	{

	}

	public void setGameSet(GameSet gameSet)
	{
		myGameSet = gameSet;
		float margin = UI_Size_Base.that.getMargin();

		this.removeChild(main);
		this.removeChild(func1);
		this.removeChild(func2);

		if (myGameSet != null)
		{
			float instractionPoolHeight = (this.height - slideButton.getMaxY()) / 3 - (margin * 4);
			float instructionPoolWidth = this.width - sel.getMaxX() - (margin * 4);

			CB_RectF instructionPoolRec = new CB_RectF(0, 0, instructionPoolWidth, instractionPoolHeight);
			main = new InstructionPoolView(instructionPoolRec, "mainPool", myGameSet.mainInstructionPool, selectHandler, 0);
			func1 = new InstructionPoolView(instructionPoolRec, "mainPool", myGameSet.func1InstructionPool, selectHandler, 1);
			func2 = new InstructionPoolView(instructionPoolRec, "mainPool", myGameSet.func2InstructionPool, selectHandler, 2);

			this.addChild(main);
			this.addChild(func1);
			this.addChild(func2);

			func2.setPos(sel.getMaxX() + margin, slideButton.getMaxY() + margin);
			func1.setPos(sel.getMaxX() + margin, func2.getMaxY() + margin);
			main.setPos(sel.getMaxX() + margin, func1.getMaxY() + margin);
		}
	}

	ISelected selectHandler = new ISelected()
	{

		@Override
		public void selected(int PoolIndex)
		{
			mSelectedPool = PoolIndex;

			// deselect other
			switch (PoolIndex)
			{
			case 0:
				func1.deselect();
				func2.deselect();
				break;
			case 1:
				main.deselect();
				func2.deselect();
				break;
			case 2:
				main.deselect();
				func1.deselect();
				break;
			}

		}
	};

	@Override
	public void onShow()
	{
		setSliderPos(height - slideButton.getHeight() - this.getTopHeight());
		isShowing = false;
	}

	private void ToggleView()
	{
		if (isShowing)
		{
			swipeDown = true;
			swipeUp = false;
		}
		else
		{
			swipeDown = false;
			swipeUp = true;
		}
		ActionUp();
	}

	@Override
	protected void SkinIsChanged()
	{
	}

	@Override
	public boolean onTouchDragged(int x, int y, int pointer, boolean KineticPan)
	{
		if (KineticPan)
		{
			GL.that.StopKinetic(x, y, pointer, true);
			isKinetigPan = true;
			return onTouchUp(x, y, pointer, 0);
		}

		float newY = y - this.getHeight() - touchYoffset;
		setSliderPos(newY);
		return true;
	}

	@Override
	public boolean onTouchDown(int x, int y, int pointer, int button)
	{
		isKinetigPan = false;
		oneTouchUP = false;
		AnimationIsRunning = false;
		if (this.contains(x, y))
		{
			touchYoffset = y - this.getMaxY();
			return true;
		}

		return false;
	}

	private boolean oneTouchUP = false;

	@Override
	public boolean onTouchUp(int x, int y, int pointer, int button)
	{
		if (isKinetigPan)
		{
			if (oneTouchUP) return true;
			oneTouchUP = true;
		}

		ActionUp();
		return true;
	}

	public void ActionUp() // Slider zurück scrolllen lassen
	{

		if (swipeUp || swipeDown)
		{
			if (swipeUp)
			{
				startAnimationTo(0);
			}
			else
			{
				startAnimationTo((int) (height - slideButton.getHeight() - this.getTopHeight()));
			}
			swipeUp = swipeDown = false;

		}
		else
		{
			if (yPos > height * 0.7)
			{
				startAnimationTo(height - slideButton.getHeight() - this.getTopHeight());
			}
			else
			{
				startAnimationTo(0);

			}
		}
	}

	private void startAnimationTo(float newYPos)
	{
		if (yPos == newYPos) return; // wir brauchen nichts Animieren

		// Logger.LogCat("Start Animation To " + newYPos);

		AnimationIsRunning = true;
		AnimationTarget = newYPos;
		if (yPos > newYPos) AnimationDirection = -1;
		else
			AnimationDirection = 1;
		handler.postDelayed(AnimationTask, ANIMATION_TIME);
	}

	Runnable AnimationTask = new Runnable()
	{

		@Override
		public void run()
		{

			if (!AnimationIsRunning) return; // Animation wurde abgebrochen

			int newValue = 0;
			if (AnimationDirection == -1)
			{
				isShowing = true;
				float tmp = yPos - AnimationTarget;
				if (tmp <= 0)// Div 0 vehindern
				{
					setPos_onUI(AnimationTarget);
					AnimationIsRunning = false;
				}

				newValue = (int) (yPos - (tmp / AnimationMulti));
				if (newValue <= AnimationTarget)
				{
					setPos_onUI(AnimationTarget);
					AnimationIsRunning = false;
				}
				else
				{
					setPos_onUI(newValue);
					handler.postDelayed(AnimationTask, ANIMATION_TIME);
				}
			}
			else
			{
				isShowing = false;
				float tmp = AnimationTarget - yPos;
				if (tmp <= 0)// Div 0 vehindern
				{
					setPos_onUI(AnimationTarget);
					AnimationIsRunning = false;
				}
				else
				{
					newValue = (int) (yPos + (tmp / AnimationMulti));
					if (newValue >= AnimationTarget)
					{
						setPos_onUI(AnimationTarget);
						AnimationIsRunning = false;
					}
					else
					{
						setPos_onUI(newValue);
						handler.postDelayed(AnimationTask, ANIMATION_TIME);
					}
				}

			}

		}

	};

	private void setPos_onUI(final float newValue)
	{

		this.RunOnGL(new runOnGL()
		{

			@Override
			public void run()
			{
				setSliderPos(newValue);
			}
		});

	}

	private void setSliderPos(float value)
	{
		if (value == yPos) return;

		yPos = value;
		this.setY(value);

		GL.that.renderOnce(this.name);

	}

}
