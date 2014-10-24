package de.gdxgame.Views;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import CB_UI_Base.GL_UI.CB_View_Base;
import CB_UI_Base.GL_UI.Fonts;
import CB_UI_Base.GL_UI.Controls.MessageBox.GL_MsgBox;
import CB_UI_Base.GL_UI.Controls.MessageBox.GL_MsgBox.OnMsgBoxClickListener;
import CB_UI_Base.GL_UI.GL_Listener.GL;
import CB_UI_Base.Math.CB_RectF;
import Res.ResourceCache;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import controls.InstructionView;
import de.gdxgame.GameSet;
import de.gdxgame.GdxGame;

/**
 * @author Lars Streblow
 * @author Longri
 */
public class GameView extends CB_View_Base
{
	/**
	 * Static zugriff auf die aktuelle Instance
	 */
	public static GameView that;

	private final InstructionView mInstractionView;

	private final static View_3D view3D = new View_3D(new CB_RectF(), "3D View");

	/**
	 * Constructor
	 * 
	 * @param rec
	 */
	public GameView(CB_RectF rec)
	{
		super(rec, "GameView");
		that = this;
		mInstractionView = new InstructionView(rec, view3D.myGameSet);
		mInstractionView.setHeight(this.getHeight() + mInstractionView.getTopHeight());
		this.addChild(mInstractionView);

		view3D.setRec(this);
		this.addChild(view3D);
		view3D.setZeroPos();
		// view3D.setHeight(this.getHeight() - mInstractionView.getY());
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

		GL.that.addRenderView(this, GL.FRAME_RATE_FAST_ACTION);
		mInstractionView.onShow();
		mInstractionView.ActionUp();
	}

	@Override
	public void onHide()
	{
		GL.that.unregister3D();
	}

	BitmapFont font;

	@Override
	protected void render(Batch batch)
	{
		// synchronized (GameField)
		// {
		if (font == null)
		{
			font = Fonts.getNormal();
			font.setColor(Color.GREEN);

		}
		String str = "fps: " + Gdx.graphics.getFramesPerSecond();
		String str2 = "GameField:" + view3D.GameFieldPositions.length + "*" + view3D.GameFieldPositions[0].length + "*"
				+ view3D.GameFieldPositions[0][0].length;

		font.draw(batch, str, 20 * ResourceCache.getDpi(), 40 * ResourceCache.getDpi());
		font.draw(batch, str2, 20 * ResourceCache.getDpi(), 60 * ResourceCache.getDpi());
		// }
		super.render(batch);
	}

	// Zufallszahl von "min"(einschlie�lich) bis "max"(einschlie�lich)
	// Beispiel: zufallszahl(4,10);
	// M�gliche Zufallszahlen 4,5,6,7,8,9,10
	public int zufallszahl(int min, int max)
	{
		Random random = new Random();
		return random.nextInt(max - min + 1) + min;
	}

	public void RunGameLoop(boolean FastAnimation)
	{
		// FIXME replace with LOOP Class from CB

		View_3D.fastAnimation = FastAnimation;
		Thread loop = new Thread(new Runnable()
		{

			@Override
			public void run()
			{
				// get gameSet from InstructionView
				view3D.myGameSet = mInstractionView.getGameSet();
				view3D.setLevel(view3D.myGameSet);

				view3D.myGameSet.startGame();
				int returnCode = 0;
				view3D.waitOfAnimationReady = new AtomicBoolean(false);
				System.out.println("Ausgangslage:");
				printGameSet();
				while (returnCode != -1)
				{
					if (view3D.waitOfAnimationReady.get())
					{
						try
						{
							Thread.sleep(view3D.fastAnimation ? view3D.FAST_ANIMATION_WAIT_TIME : view3D.ANIMATION_WAIT_TIME);
							continue;
						}
						catch (InterruptedException e)
						{
							e.printStackTrace();
						}
					}
					returnCode = view3D.myGameSet.runInstruction();

					{// set visual of instraction on InstractionPoolView-Slider
						// if (mLastPlayitPoolType != myGameSet.getCurrentInstructionPoolType())
						{// change PoolType
							// mLastPlayitPoolType = myGameSet.getCurrentInstructionPoolType();
							// InstructionSliderContent.that.setActInstractionPool(myGameSet.getPool(mLastPlayitPoolType));
						}
						// InstructionSliderContent.that.setActInstractionIndex(myGameSet.getInstructionIndex());
					}

					System.out.println("instructionCode: " + view3D.myGameSet.currentInstruction);
					System.out.println("returnCode: " + returnCode);
					switch (returnCode)
					{
					case 0: // normaler Zug
						view3D.normalMove();
						printGameSet();
						break;
					case -1: // Programmende erreicht
						if (view3D.myGameSet.gameAccomplished())
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
						if (view3D.myGameSet.currentCrane.isLoaded())
						{ // es wurde gerade aufgenommen
							// leere Hookanimation
							view3D.grabMove();
						}
						else
						{ // es wurde gerade abgelegt
							view3D.grabMove();
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
						view3D.clashMove();
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

	public static void printGameSet()
	{
		int x = 0;
		int y = 0;
		for (y = 0; y < view3D.myGameSet.currentFloor.getLength(); y++)
		{
			for (x = 0; x < view3D.myGameSet.currentFloor.getWidth(); x++)
			{
				System.out.print(view3D.myGameSet.currentFloor.getBoxes(x, y));
				if (view3D.myGameSet.currentCrane.getXPosition() == x && view3D.myGameSet.currentCrane.getYPosition() == y) if (view3D.myGameSet.currentCrane
						.isLoaded()) System.out.print("1");
				else
					System.out.print("0");
				else
					System.out.print(" ");
				if (x < view3D.myGameSet.currentFloor.getLength() - 1) System.out.print(" ");
			}
			System.out.println();
		}
		System.out.println();
	}

	public void setLevel(GameSet Level)
	{
		view3D.setLevel(Level);
	}

}
