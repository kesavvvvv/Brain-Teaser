package android.support.graphics.drawable;

import android.animation.TypeEvaluator;
import android.support.annotation.RestrictTo;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class ArgbEvaluator implements TypeEvaluator {
  private static final ArgbEvaluator sInstance = new ArgbEvaluator();
  
  public static ArgbEvaluator getInstance() { return sInstance; }
  
  public Object evaluate(float paramFloat, Object paramObject1, Object paramObject2) {
    int i = ((Integer)paramObject1).intValue();
    float f1 = (i >> 24 & 0xFF) / 255.0F;
    float f4 = (i >> 16 & 0xFF) / 255.0F;
    float f5 = (i >> 8 & 0xFF) / 255.0F;
    float f6 = (i & 0xFF) / 255.0F;
    i = ((Integer)paramObject2).intValue();
    float f2 = (i >> 24 & 0xFF) / 255.0F;
    float f8 = (i >> 16 & 0xFF) / 255.0F;
    float f7 = (i >> 8 & 0xFF) / 255.0F;
    float f3 = (i & 0xFF) / 255.0F;
    f4 = (float)Math.pow(f4, 2.2D);
    f5 = (float)Math.pow(f5, 2.2D);
    f6 = (float)Math.pow(f6, 2.2D);
    f8 = (float)Math.pow(f8, 2.2D);
    f7 = (float)Math.pow(f7, 2.2D);
    f3 = (float)Math.pow(f3, 2.2D);
    f4 = (float)Math.pow(((f8 - f4) * paramFloat + f4), 0.45454545454545453D);
    f5 = (float)Math.pow(((f7 - f5) * paramFloat + f5), 0.45454545454545453D);
    f3 = (float)Math.pow(((f3 - f6) * paramFloat + f6), 0.45454545454545453D);
    return Integer.valueOf(Math.round(((f2 - f1) * paramFloat + f1) * 255.0F) << 24 | Math.round(f4 * 255.0F) << 16 | Math.round(f5 * 255.0F) << 8 | Math.round(f3 * 255.0F));
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\graphics\drawable\ArgbEvaluator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */