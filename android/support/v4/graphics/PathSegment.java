package android.support.v4.graphics;

import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.v4.util.Preconditions;

public final class PathSegment {
  private final PointF mEnd;
  
  private final float mEndFraction;
  
  private final PointF mStart;
  
  private final float mStartFraction;
  
  public PathSegment(@NonNull PointF paramPointF1, float paramFloat1, @NonNull PointF paramPointF2, float paramFloat2) {
    this.mStart = (PointF)Preconditions.checkNotNull(paramPointF1, "start == null");
    this.mStartFraction = paramFloat1;
    this.mEnd = (PointF)Preconditions.checkNotNull(paramPointF2, "end == null");
    this.mEndFraction = paramFloat2;
  }
  
  public boolean equals(Object paramObject) {
    if (this == paramObject)
      return true; 
    if (!(paramObject instanceof PathSegment))
      return false; 
    paramObject = (PathSegment)paramObject;
    return (Float.compare(this.mStartFraction, paramObject.mStartFraction) == 0 && Float.compare(this.mEndFraction, paramObject.mEndFraction) == 0 && this.mStart.equals(paramObject.mStart) && this.mEnd.equals(paramObject.mEnd));
  }
  
  @NonNull
  public PointF getEnd() { return this.mEnd; }
  
  public float getEndFraction() { return this.mEndFraction; }
  
  @NonNull
  public PointF getStart() { return this.mStart; }
  
  public float getStartFraction() { return this.mStartFraction; }
  
  public int hashCode() {
    int i;
    int k = this.mStart.hashCode();
    float f = this.mStartFraction;
    int j = 0;
    if (f != 0.0F) {
      i = Float.floatToIntBits(f);
    } else {
      i = 0;
    } 
    int m = this.mEnd.hashCode();
    f = this.mEndFraction;
    if (f != 0.0F)
      j = Float.floatToIntBits(f); 
    return ((k * 31 + i) * 31 + m) * 31 + j;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("PathSegment{start=");
    stringBuilder.append(this.mStart);
    stringBuilder.append(", startFraction=");
    stringBuilder.append(this.mStartFraction);
    stringBuilder.append(", end=");
    stringBuilder.append(this.mEnd);
    stringBuilder.append(", endFraction=");
    stringBuilder.append(this.mEndFraction);
    stringBuilder.append('}');
    return stringBuilder.toString();
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v4\graphics\PathSegment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */