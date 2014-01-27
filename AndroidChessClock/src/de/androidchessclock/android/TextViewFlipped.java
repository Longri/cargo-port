package de.androidchessclock.android;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewFlipped extends TextView {

	public TextViewFlipped(Context context) {
		super(context);
	}

	public TextViewFlipped(Context context, AttributeSet attributes)
	{
		super(context, attributes);
	}

	@Override
	public void onDraw(Canvas canvas) {
		//Save the current canvas
		canvas.save();
		//Rotate the canvas (around the center of the text)
		float f_py = this.getHeight()/2.0f;
		float f_px = this.getWidth()/2.0f;
		canvas.rotate(180, f_px, f_py);
		super.onDraw(canvas);
		//Restore the old canvas
		canvas.restore();
		}
	}
