package android.support.v4.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.view.animation.Animation;
import android.widget.ImageView;

class CircleImageView extends ImageView {
  private static final int FILL_SHADOW_COLOR = 1023410176;
  
  private static final int KEY_SHADOW_COLOR = 503316480;
  
  private static final int SHADOW_ELEVATION = 4;
  
  private static final float SHADOW_RADIUS = 3.5F;
  
  private static final float X_OFFSET = 0.0F;
  
  private static final float Y_OFFSET = 1.75F;
  
  private Animation.AnimationListener mListener;
  
  int mShadowRadius;
  
  CircleImageView(Context paramContext, int paramInt) {
    super(paramContext);
    float f = (getContext().getResources().getDisplayMetrics()).density;
    int i = (int)(1.75F * f);
    int j = (int)(0.0F * f);
    this.mShadowRadius = (int)(3.5F * f);
    if (elevationSupported()) {
      shapeDrawable = new ShapeDrawable(new OvalShape());
      ViewCompat.setElevation(this, 4.0F * f);
    } else {
      shapeDrawable = new ShapeDrawable(new OvalShadow(this, this.mShadowRadius));
      setLayerType(1, shapeDrawable.getPaint());
      shapeDrawable.getPaint().setShadowLayer(this.mShadowRadius, j, i, 503316480);
      i = this.mShadowRadius;
      setPadding(i, i, i, i);
    } 
    shapeDrawable.getPaint().setColor(paramInt);
    ViewCompat.setBackground(this, shapeDrawable);
  }
  
  private boolean elevationSupported() { return (Build.VERSION.SDK_INT >= 21); }
  
  public void onAnimationEnd() {
    super.onAnimationEnd();
    Animation.AnimationListener animationListener = this.mListener;
    if (animationListener != null)
      animationListener.onAnimationEnd(getAnimation()); 
  }
  
  public void onAnimationStart() {
    super.onAnimationStart();
    Animation.AnimationListener animationListener = this.mListener;
    if (animationListener != null)
      animationListener.onAnimationStart(getAnimation()); 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    super.onMeasure(paramInt1, paramInt2);
    if (!elevationSupported())
      setMeasuredDimension(getMeasuredWidth() + this.mShadowRadius * 2, getMeasuredHeight() + this.mShadowRadius * 2); 
  }
  
  public void setAnimationListener(Animation.AnimationListener paramAnimationListener) { this.mListener = paramAnimationListener; }
  
  public void setBackgroundColor(int paramInt) {
    if (getBackground() instanceof ShapeDrawable)
      ((ShapeDrawable)getBackground()).getPaint().setColor(paramInt); 
  }
  
  public void setBackgroundColorRes(int paramInt) { setBackgroundColor(ContextCompat.getColor(getContext(), paramInt)); }
  
  private class OvalShadow extends OvalShape {
    private RadialGradient mRadialGradient;
    
    private Paint mShadowPaint = new Paint();
    
    OvalShadow(int param1Int) {
      CircleImageView.this.mShadowRadius = param1Int;
      updateRadialGradient((int)rect().width());
    }
    
    private void updateRadialGradient(int param1Int) {
      float f1 = (param1Int / 2);
      float f2 = (param1Int / 2);
      float f3 = CircleImageView.this.mShadowRadius;
      Shader.TileMode tileMode = Shader.TileMode.CLAMP;
      this.mRadialGradient = new RadialGradient(f1, f2, f3, new int[] { 1023410176, 0 }, null, tileMode);
      this.mShadowPaint.setShader(this.mRadialGradient);
    }
    
    public void draw(Canvas param1Canvas, Paint param1Paint) {
      int i = CircleImageView.this.getWidth();
      int j = CircleImageView.this.getHeight();
      param1Canvas.drawCircle((i / 2), (j / 2), (i / 2), this.mShadowPaint);
      param1Canvas.drawCircle((i / 2), (j / 2), (i / 2 - CircleImageView.this.mShadowRadius), param1Paint);
    }
    
    protected void onResize(float param1Float1, float param1Float2) {
      super.onResize(param1Float1, param1Float2);
      updateRadialGradient((int)param1Float1);
    }
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v4\widget\CircleImageView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */