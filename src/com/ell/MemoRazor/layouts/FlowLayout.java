package com.ell.MemoRazor.layouts;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class FlowLayout extends ViewGroup {
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    private int horizontalSpacing = 0;
    private int verticalSpacing = 0;
    private int orientation = 0;

    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public FlowLayout(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec) - this.getPaddingRight() - this.getPaddingLeft();
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec) - this.getPaddingRight() - this.getPaddingLeft();

        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        int size;
        int mode;

        if (orientation == HORIZONTAL) {
            size = sizeWidth;
            mode = modeWidth;
        } else {
            size = sizeHeight;
            mode = modeHeight;
        }

        int lineThicknessWithSpacing = 0;
        int lineThickness = 0;
        int lineLengthWithSpacing = 0;
        int lineLength;

        int prevLinePosition = 0;

        int controlMaxLength = 0;
        int controlMaxThickness = 0;

        final int count = getChildCount();

        LineInformation lineInformation = new LineInformation();
        lineInformation.setTotalLength(size);
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }

            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            child.measure(
                    MeasureSpec.makeMeasureSpec(lp.width, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(lp.height, MeasureSpec.EXACTLY)
            );

            int hSpacing = this.horizontalSpacing;
            int vSpacing = this.verticalSpacing;

            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            int childLength;
            int childThickness;
            int spacingLength;
            int spacingThickness;

            if (orientation == HORIZONTAL) {
                childLength = childWidth;
                childThickness = childHeight;
                spacingLength = hSpacing;
                spacingThickness = vSpacing;
            } else {
                childLength = childHeight;
                childThickness = childWidth;
                spacingLength = vSpacing;
                spacingThickness = hSpacing;
            }

            lineLength = lineLengthWithSpacing + childLength;
            lineLengthWithSpacing = lineLength + spacingLength;

            boolean newLine = (mode != MeasureSpec.UNSPECIFIED && lineLength > size);
            if (newLine) {
                prevLinePosition = prevLinePosition + lineThicknessWithSpacing;

                lineThickness = childThickness;
                lineLength = childLength;
                lineThicknessWithSpacing = childThickness + spacingThickness;
                lineLengthWithSpacing = lineLength + spacingLength;

                lineInformation = new LineInformation();
                lineInformation.setTotalLength(size);
            }
            lineInformation.setLineLength(lineLength);

            lineThicknessWithSpacing = Math.max(lineThicknessWithSpacing, childThickness + spacingThickness);
            lineThickness = Math.max(lineThickness, childThickness);

            int posX;
            int posY;
            if (orientation == HORIZONTAL) {
                posX = getPaddingLeft() + lineLength - childLength;
                posY = getPaddingTop() + prevLinePosition;
            } else {
                posX = getPaddingLeft() + prevLinePosition;
                posY = getPaddingTop() + lineLength - childHeight;
            }
            lp.setPosition(posX, posY);
            lp.setLineInformation(lineInformation);

            controlMaxLength = Math.max(controlMaxLength, lineLength);
            controlMaxThickness = prevLinePosition + lineThickness;
        }

        /* need to take paddings into account */
        if (orientation == HORIZONTAL) {
            controlMaxLength += getPaddingLeft() + getPaddingRight();
            controlMaxThickness += getPaddingBottom() + getPaddingTop();
        } else {
            controlMaxLength += getPaddingBottom() + getPaddingTop();
            controlMaxThickness += getPaddingLeft() + getPaddingRight();
        }

        if (orientation == HORIZONTAL) {
            this.setMeasuredDimension(resolveSize(controlMaxLength, widthMeasureSpec), resolveSize(controlMaxThickness, heightMeasureSpec));
        } else {
            this.setMeasuredDimension(resolveSize(controlMaxThickness, widthMeasureSpec), resolveSize(controlMaxLength, heightMeasureSpec));
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();

            int delta = (lp.lineInformation.totalLength - lp.lineInformation.lineLength) / 2;
            if (orientation == HORIZONTAL) {
                child.layout(delta + lp.x, lp.y, delta + lp.x + child.getMeasuredWidth(), lp.y + child.getMeasuredHeight());
            } else {
                child.layout(lp.x, delta + lp.y, lp.x + child.getMeasuredWidth(), delta + lp.y + child.getMeasuredHeight());
            }

        }
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        boolean more = super.drawChild(canvas, child, drawingTime);
        return more;
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    public static class LineInformation {
        private int lineLength;
        private int totalLength;

        public int getLineLength() {
            return lineLength;
        }

        public void setLineLength(int lineLength) {
            this.lineLength = lineLength;
        }

        public int getTotalLength() {
            return totalLength;
        }

        public void setTotalLength(int totalLength) {
            this.totalLength = totalLength;
        }
    }
    public static class LayoutParams extends ViewGroup.LayoutParams {
        private int x;
        private int y;
        private LineInformation lineInformation;

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }

        public void setPosition(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void setLineInformation(LineInformation lineInformation) {
            this.lineInformation = lineInformation;
        }
    }
}