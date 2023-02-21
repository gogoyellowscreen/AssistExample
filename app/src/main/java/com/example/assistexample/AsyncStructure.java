package com.example.assistexample;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewStructure;

/**
 * Test for asynchronously creating additional assist structure.
 */
public class AsyncStructure extends androidx.appcompat.widget.AppCompatTextView {
    public AsyncStructure(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    public void onProvideVirtualStructure(ViewStructure structure) {
        structure.setChildCount(1);
        final ViewStructure child = structure.asyncNewChild(0);
        final int width = getWidth();
        final int height = getHeight();
        (new Thread() {
            @Override
            public void run() {
                // Simulate taking a long time to build this.
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                }
                child.setClassName(AsyncStructure.class.getName());
                child.setVisibility(View.VISIBLE);
                child.setDimens(width / 4, height / 4, 0, 0, width / 2, height / 2);
                child.setEnabled(true);
                child.setContentDescription("This is some async content");
                child.setText("We could have lots and lots of async text!");
                child.asyncCommit();
            }
        }).start();
    }
}
