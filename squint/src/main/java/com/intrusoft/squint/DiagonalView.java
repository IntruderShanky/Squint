package com.intrusoft.squint;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.intrusoft.squint.Squint.Direction;
import com.intrusoft.squint.Squint.Gravity;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;
import static com.intrusoft.squint.Squint.DIRECTIONS;
import static com.intrusoft.squint.Squint.GRAVITIES;

public class DiagonalView extends ImageView {

    private final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    private final Paint fillPaint = new Paint(ANTI_ALIAS_FLAG);
    private final Paint tintPaint = new Paint(ANTI_ALIAS_FLAG);
    private final Paint bitmapPaint = new Paint(ANTI_ALIAS_FLAG);
    private final Paint solidPaint = new Paint(ANTI_ALIAS_FLAG);
    private final Matrix shaderMatrix = new Matrix();
    private final int DEFAULT_ALPHA = 50;
    private static int tintColor = Color.TRANSPARENT;
    private static int fillColor = Color.TRANSPARENT;
    private static int solidColor = Color.TRANSPARENT;
    private float width;
    private float height;
    private int angle = 10;
    private Gravity gravity = Gravity.BOTTOM;
    private Direction direction = Direction.LEFT_TO_RIGHT;
    private Bitmap bitmap;
    private BitmapShader bitmapShader;
    private ColorFilter colorFilter;
    private Path path = new Path();


    public DiagonalView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public DiagonalView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public DiagonalView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }


    private void init(Context context, AttributeSet attributeSet, int defStyleAttr) {
        if (attributeSet != null) {
            TypedArray array = context.obtainStyledAttributes(attributeSet, R.styleable.DiagonalView, defStyleAttr, 0);
            angle = array.getInt(R.styleable.DiagonalView_angle, angle);
            gravity = GRAVITIES[array.getInt(R.styleable.DiagonalView_gravity, 3)];
            tintColor = array.getColor(R.styleable.DiagonalView_tint, Color.TRANSPARENT);
            fillColor = array.getColor(R.styleable.DiagonalView_fillColor, Color.TRANSPARENT);
            solidColor = array.getColor(R.styleable.DiagonalView_solidColor, Color.TRANSPARENT);
            direction = DIRECTIONS[array.getInt(R.styleable.DiagonalView_diagonalDirection, 0)];
            array.recycle();
        }
        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setColor(fillColor);
        tintPaint.setStyle(Paint.Style.FILL);
        tintPaint.setColor(tintColor);
        solidPaint.setStyle(Paint.Style.FILL);
        solidPaint.setColor(solidColor);
        solidPaint.setAlpha(255);
        if (tintPaint.getAlpha() == 255)
            tintPaint.setAlpha(DEFAULT_ALPHA);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        initializeBitmap();

        PathHelper.calculatePath(path, getWidth(), getHeight(), angle, direction, gravity);
    }

    @Override
    public void draw(Canvas canvas) {
        if (bitmap != null) {
            if (fillColor != Color.TRANSPARENT)
                canvas.drawPath(path, fillPaint);
            canvas.drawPath(path, bitmapPaint);
            if (tintColor != Color.TRANSPARENT)
                canvas.drawPath(path, tintPaint);
        }
        if (solidColor != Color.TRANSPARENT)
            canvas.drawPath(path, solidPaint);
    }

    private void initializeBitmap() {
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        Drawable drawable = getDrawable();
        if (drawable != null) {
            if (drawable instanceof BitmapDrawable)
                bitmap = ((BitmapDrawable) drawable).getBitmap();
            else {
                try {
                    int COLOR_DRAWABLE_DIMENSIONS = 2;
                    if (drawable instanceof ColorDrawable)
                        bitmap = Bitmap.createBitmap(COLOR_DRAWABLE_DIMENSIONS, COLOR_DRAWABLE_DIMENSIONS, BITMAP_CONFIG);
                    else
                        bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);

                    Canvas canvas = new Canvas(bitmap);
                    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                    drawable.draw(canvas);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (bitmap == null) {
                invalidate();
                return;
            }
            if (bitmapPaint != null) {
                bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                bitmapPaint.setShader(bitmapShader);
            }
            if (this.getScaleType() != ScaleType.CENTER_CROP && this.getScaleType() != ScaleType.FIT_XY)
                this.setScaleType(ScaleType.CENTER_CROP);
            setUpScaleType();
            applyColorFilter();
            invalidate();
        }
    }

    private void applyColorFilter() {
        if (bitmapPaint != null) {
            bitmapPaint.setColorFilter(colorFilter);
        }
    }

    private void setUpScaleType() {
        float scaleX = 1, scaleY = 1, dx = 0, dy = 0;
        if (bitmap == null || shaderMatrix == null)
            return;
        shaderMatrix.set(null);
        if (getScaleType() == ScaleType.CENTER_CROP) {
            if (width != bitmap.getWidth()) {
                scaleX = width / bitmap.getWidth();
            }
            if (scaleX * bitmap.getHeight() < height) {
                scaleX = height / bitmap.getHeight();
            }
            dy = (height - bitmap.getHeight() * scaleX) * 0.5f;
            dx = (width - bitmap.getWidth() * scaleX) * 0.5f;
            shaderMatrix.setScale(scaleX, scaleX);
        } else {
            scaleX = width / bitmap.getWidth();
            scaleY = height / bitmap.getHeight();
            dy = (height - bitmap.getHeight() * scaleY) * 0.5f;
            dx = (width - bitmap.getWidth() * scaleX) * 0.5f;
            shaderMatrix.setScale(scaleX, scaleY);
        }
        shaderMatrix.postTranslate(dx + 0.5f, dy + 0.5f);
        bitmapShader.setLocalMatrix(shaderMatrix);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        if (colorFilter == this.colorFilter) {
            return;
        }
        this.colorFilter = colorFilter;
        applyColorFilter();
        invalidate();
    }

    @Override
    public ColorFilter getColorFilter() {
        return colorFilter;
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        if (scaleType == ScaleType.CENTER_CROP || scaleType == ScaleType.FIT_XY)
            super.setScaleType(scaleType);
        else
            throw new IllegalArgumentException(String.format("ScaleType %s not supported.", scaleType));
    }

    @Override
    public void setAdjustViewBounds(boolean adjustViewBounds) {
        if (adjustViewBounds) {
            throw new IllegalArgumentException("adjustViewBounds not supported.");
        }
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        initializeBitmap();
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        initializeBitmap();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        initializeBitmap();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        initializeBitmap();
    }

    public int getTintColor() {
        return tintColor;
    }

    public void setTintColor(int tintColor) {
        DiagonalView.tintColor = tintColor;
        tintPaint.setColor(tintColor);
        if (tintPaint.getAlpha() == 255)
            tintPaint.setAlpha(DEFAULT_ALPHA);
        invalidate();
    }

    public int getFillColor() {
        return fillColor;
    }

    public void setFillColor(int fillColor) {
        DiagonalView.fillColor = fillColor;
        fillPaint.setColor(fillColor);
        invalidate();
    }

    public int getSolidColor() {
        return solidColor;
    }

    public void setSolidColor(int solidColor) {
        DiagonalView.solidColor = solidColor;
        solidPaint.setColor(solidColor);
        solidPaint.setAlpha(255);
        invalidate();
    }

    public void setDiagonalGravity(Gravity gravity) {
        this.gravity = gravity;
        invalidate();
    }

    public void setDiagonalDirection(Direction direction) {
        this.direction = direction;
        invalidate();
    }

    public void setAngle(int angle) {
        this.angle = angle;
        invalidate();
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        invalidate();
    }
}
