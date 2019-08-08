package android.support.v4.view.animation;

import android.view.animation.Interpolator;

abstract class LookupTableInterpolator implements Interpolator {
  private final float mStepSize;
  
  private final float[] mValues;
  
  protected LookupTableInterpolator(float[] paramArrayOfFloat) {
    this.mValues = paramArrayOfFloat;
    this.mStepSize = 1.0F / (this.mValues.length - 1);
  }
  
  public float getInterpolation(float paramFloat) {
    if (paramFloat >= 1.0F)
      return 1.0F; 
    if (paramFloat <= 0.0F)
      return 0.0F; 
    float[] arrayOfFloat = this.mValues;
    int i = Math.min((int)((arrayOfFloat.length - 1) * paramFloat), arrayOfFloat.length - 2);
    float f1 = i;
    float f2 = this.mStepSize;
    paramFloat = (paramFloat - f1 * f2) / f2;
    arrayOfFloat = this.mValues;
    return arrayOfFloat[i] + (arrayOfFloat[i + 1] - arrayOfFloat[i]) * paramFloat;
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v4\view\animation\LookupTableInterpolator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */