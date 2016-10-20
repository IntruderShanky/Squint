package com.intrusoft.squint;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Displays an arbitrary image with diagonal cut.
 * The ImageView class can load images from various sources (such as resources or bitmaps),
 * takes care of computing its measurement from the image so that it can be used in any layout manager,
 * and provides various display options such as scaling and tinting.
 *
 * @author Intruder Shanky
 * @since October 2016
 */
public class DiagonalView extends View {

    private ScaleType scaleType;
    private Gravity gravity;
    private float angle;
    private float width;
    private float height;
    private float requiredWidth;
    private float requiredHeight;
    private Path path;
    private RectF viewBounds, scaleRect;
    private int colorTint;
    private int imageSource;
    private int x;
    private int y;
    private Paint paint;
    private Bitmap bitmap;
    private Context context;
    private final String LOG_TAG = "SQUINT_LOG";
    private int solidColor;

    public enum ScaleType {
        CENTRE_CROP(0),
        FIT_XY(1);
        final int value;

        ScaleType(int value) {
            this.value = value;
        }
    }

    private static final ScaleType[] scaleTypeArray = {ScaleType.CENTRE_CROP, ScaleType.FIT_XY};

    public enum Gravity {
        LEFT(0),
        RIGHT(1);
        final int value;

        Gravity(int value) {
            this.value = value;
        }
    }

    private static final Gravity[] gravityArray = {Gravity.LEFT, Gravity.RIGHT};

    public DiagonalView(Context context) {
        super(context);
        init(context, null);
    }

    public DiagonalView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DiagonalView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DiagonalView);
            try {
                imageSource = typedArray.getResourceId(R.styleable.DiagonalView_src, -1);
                angle = typedArray.getFloat(R.styleable.DiagonalView_angle, 18);
                int g = typedArray.getInt(R.styleable.DiagonalView_gravity, 0);
                gravity = gravityArray[g];
                int scale = typedArray.getInt(R.styleable.DiagonalView_scaleType, 0);
                scaleType = scaleTypeArray[scale];
                colorTint = typedArray.getColor(R.styleable.DiagonalView_tint, 0);
                solidColor = typedArray.getColor(R.styleable.DiagonalView_solidColor, 0);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                typedArray.recycle();
            }
        } else {
            angle = 18;
            gravity = Gravity.LEFT;
            scaleType = ScaleType.CENTRE_CROP;
        }
        path = new Path();
        viewBounds = new RectF();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        scaleRect = new RectF();
        this.context = context;
        if (imageSource != -1)
            try {
                bitmap = BitmapFactory.decodeResource(context.getResources(), imageSource);
            } catch (OutOfMemoryError error) {
                bitmap = null;
                Log.e(LOG_TAG, "Image is too large to process. " + error.getMessage());
            } catch (Exception e) {
                Log.e(LOG_TAG, e.getMessage());
            }
        if (colorTint != 0) {
            if (Color.alpha(colorTint) == 255)
                colorTint = Color.argb(55, Color.red(colorTint), Color.green(colorTint), Color.blue(colorTint));
            paint.setColor(colorTint);
        }
        if (solidColor != 0) {
            solidColor = Color.rgb(Color.red(solidColor), Color.green(solidColor), Color.blue(solidColor));
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        if (bitmap != null && scaleType == ScaleType.CENTRE_CROP) {
            float ratioChange = 1;
            if (width != bitmap.getWidth()) {
                ratioChange = width / bitmap.getWidth();
            }
            if (ratioChange * bitmap.getHeight() < height) {
                ratioChange = height / bitmap.getHeight();
            }
            requiredHeight = bitmap.getHeight() * ratioChange;
            requiredWidth = bitmap.getWidth() * ratioChange;
            y = (int) ((requiredHeight / 2) - (height / 2));
            x = (int) ((requiredWidth / 2) - (width / 2));
            if (x > 0) x = -x;
            if (y > 0) y = -y;
        }
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        float dxHeight = (float) (width * Math.tan(Math.toRadians(angle)));
        path.moveTo(0, 0);
        path.lineTo(width, 0);
        if (gravity == Gravity.LEFT) {
            path.lineTo(width, height - dxHeight);
            path.lineTo(0, height);
        } else {
            path.lineTo(width, height);
            path.lineTo(0, height - dxHeight);
        }
        path.close();
        viewBounds.set(0, 0, width, height);
        canvas.clipPath(path);
        canvas.clipRect(viewBounds);
        if (solidColor != 0) {
            paint.setColor(solidColor);
            canvas.drawRect(viewBounds, paint);
        }
        if (bitmap != null)
            if (scaleType == ScaleType.CENTRE_CROP) {
                scaleRect.set(x, y, x + requiredWidth, y + requiredHeight);
                canvas.clipRect(scaleRect);
                canvas.drawBitmap(bitmap, null, scaleRect, paint);
            } else {
                canvas.drawBitmap(bitmap, null, viewBounds, paint);
            }
        if (colorTint != 0)
            canvas.drawRect(viewBounds, paint);
        // Log.d("LOG", "Drawing Canvas");
    }

    /**
     * @param scaleType scaleType of the image on {@link DiagonalView}
     */
    public void setScaleType(@NonNull ScaleType scaleType) {
        this.scaleType = scaleType;
        invalidate();
    }

    /**
     * @param gravity is RIGHT or LEFT.
     *                if diagonalGravity is LEFT then diagonal will start from left
     *                and start increasing to RIGHT and reverse if gravity is RIGHT
     */
    public void setGravity(@NonNull Gravity gravity) {
        this.gravity = gravity;
        invalidate();
    }


    /**
     * @param bitmap is object of Scaled Bitmap
     */
    public void setBitmap(@NonNull Bitmap bitmap) {
        this.bitmap = bitmap;
        invalidate();
    }

    /**
     * @param resId is drawable resource Id of image
     */
    public void setImageSource(@DrawableRes int resId) {
        this.imageSource = resId;
        try {
            bitmap = BitmapFactory.decodeResource(context.getResources(), imageSource);
        } catch (OutOfMemoryError error) {
            bitmap = null;
            Log.e(LOG_TAG, "Image is too large to process. " + error.getMessage());
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
        }
        invalidate();
    }

    /**
     * @param color is image tint to provide theme effect. This is optional.
     */
    public void setColorTint(@ColorInt int color) {
        if (color != 0 && Color.alpha(color) == 255) {
            colorTint = Color.argb(55, Color.red(color), Color.green(color), Color.blue(color));
            paint.setColor(colorTint);
        }
        invalidate();
    }


    /**
     * @param angle is the angle at which the diagonal
     *              is to be made
     */
    public void setAngle(float angle) {
        this.angle = angle;
        invalidate();
    }

    /**
     * To make diagonal of solid color, alpha of color will automatically removed
     *
     * @param color is solid color
     */
    public void setSolidColor(int color) {
        if (color != 0) {
            solidColor = Color.rgb(Color.red(color), Color.green(color), Color.blue(color));
        }
        invalidate();
    }
}
