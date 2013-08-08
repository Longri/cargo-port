package controls;

import CB_Core.GL_UI.CB_View_Base;
import CB_Core.GL_UI.GL_View_Base;
import CB_Core.GL_UI.Controls.Label;
import CB_Core.GL_UI.Controls.List.Adapter;
import CB_Core.GL_UI.Controls.List.H_ListView;
import CB_Core.GL_UI.Controls.List.ListViewItemBase;
import CB_Core.Math.CB_RectF;
import Enums.InstructionType;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.gdxgame.GameInstructionPool;

public class InstructionSliderContent extends CB_View_Base
{
	public static InstructionSliderContent that;
	private GameInstructionPool mActPool;
	private int mSelectedInstractionIndex = -1;
	private H_ListView mListView;
	private Label mPoolTypeLabel;

	public InstructionSliderContent(CB_RectF rec, String Name)
	{
		super(rec, Name);
		that = this;
		mPoolTypeLabel = new Label(0, 0, this.getHeight(), this.getHeight(), "");
		this.addChild(mPoolTypeLabel);

		mListView = new H_ListView(rec, "ListView_" + Name);
		mListView.setWidth(this.width - mPoolTypeLabel.getWidth());
		mListView.setZeroPos();
		mListView.setX(mPoolTypeLabel.getMaxX());

		this.addChild(mListView);
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

	public void setActInstractionPool(GameInstructionPool pool)
	{
		setActInstractionPool(pool, 0);
	}

	public void setActInstractionPool(GameInstructionPool pool, int selectedInstraction)
	{
		if (mActPool != pool)
		{
			mActPool = pool;
			mListView.setBaseAdapter(ListBaseAdapter);
			mListView.notifyDataSetChanged();
			mPoolTypeLabel.setText(mActPool.getPoolType().toString());
		}

		mSelectedInstractionIndex = selectedInstraction;
		setActInstractionIndex(mSelectedInstractionIndex);
	}

	public static class InstructionListViewItem extends ListViewItemBase
	{
		InstructionButton mButton;

		public static final CB_RectF mButtonRec = (new InstructionButton(InstructionType.Nop, null, -1).copy());

		public InstructionListViewItem(int Index, InstructionType instructionType)
		{
			super(mButtonRec, Index, instructionType.toString());
			mButton = new InstructionButton(instructionType, null, Index);
			mButton.disableDelete();
			mButton.setOnClickListener(click);
			this.addChild(mButton);
		}

		@Override
		public void render(SpriteBatch batch)
		{
			if (mButton.isFocused() != isSelected)
			{
				mButton.setFocus(isSelected);
			}

			super.render(batch);
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

	private static OnClickListener click = new OnClickListener()
	{

		@Override
		public boolean onClick(GL_View_Base v, int x, int y, int pointer, int button)
		{
			InstructionSliderContent.that.click(0, 0, 0, 0);
			return true;
		}
	};

	Adapter ListBaseAdapter = new Adapter()
	{

		@Override
		public ListViewItemBase getView(int Index)
		{
			if (mActPool == null) return null;
			return new InstructionListViewItem(Index, mActPool.getInstruction(Index));
		}

		@Override
		public float getItemSize(int arg0)
		{
			return InstructionListViewItem.mButtonRec.getWidth();
		}

		@Override
		public int getCount()
		{
			if (mActPool == null) return 0;
			return mActPool.size();
		}
	};

	public void setActInstractionIndex(int index)
	{
		mListView.setSelection(index);
		int last = mListView.getFirstVisiblePosition();
		int first = mListView.getLastVisiblePosition();

		if (!(first <= index && last >= index))
		{
			mListView.scrollToItem(ListBaseAdapter.getCount());
		}
		// {
		// ArrayList<Float> mPosDefault = mListView.getItemPosList();
		// if (mPosDefault == null) return;
		//
		// index = ListBaseAdapter.getCount() - index;
		//
		// if (index < mListView.getMaxItemCount()) index = mListView.getMaxItemCount();
		//
		// // if (index >= 0 && index < mPosDefault.size())
		// // {
		// // mListView.scrollTo(mPosDefault.get(index) - this.width);
		// // }
		// // else
		// // {
		// // mListView.scrollTo(mPosDefault.get(mPosDefault.size() - 1) - this.width);
		// // }
		// // float d = mListView.getDividerHeight();
		// // mListView.scrollTo(this.width - ((ListBaseAdapter.getItemSize(0) + d) * 2));
		// }

	}
}
