package android.support.v4.widget;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class FocusStrategy {
  private static boolean beamBeats(int paramInt, @NonNull Rect paramRect1, @NonNull Rect paramRect2, @NonNull Rect paramRect3) {
    boolean bool2 = beamsOverlap(paramInt, paramRect1, paramRect2);
    boolean bool3 = beamsOverlap(paramInt, paramRect1, paramRect3);
    boolean bool1 = false;
    if (!bool3) {
      if (!bool2)
        return false; 
      if (!isToDirectionOf(paramInt, paramRect1, paramRect3))
        return true; 
      if (paramInt != 17) {
        if (paramInt == 66)
          return true; 
        if (majorAxisDistance(paramInt, paramRect1, paramRect2) < majorAxisDistanceToFarEdge(paramInt, paramRect1, paramRect3))
          bool1 = true; 
        return bool1;
      } 
      return true;
    } 
    return false;
  }
  
  private static boolean beamsOverlap(int paramInt, @NonNull Rect paramRect1, @NonNull Rect paramRect2) { // Byte code:
    //   0: iload_0
    //   1: bipush #17
    //   3: if_icmpeq -> 64
    //   6: iload_0
    //   7: bipush #33
    //   9: if_icmpeq -> 38
    //   12: iload_0
    //   13: bipush #66
    //   15: if_icmpeq -> 64
    //   18: iload_0
    //   19: sipush #130
    //   22: if_icmpne -> 28
    //   25: goto -> 38
    //   28: new java/lang/IllegalArgumentException
    //   31: dup
    //   32: ldc 'direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.'
    //   34: invokespecial <init> : (Ljava/lang/String;)V
    //   37: athrow
    //   38: aload_2
    //   39: getfield right : I
    //   42: aload_1
    //   43: getfield left : I
    //   46: if_icmplt -> 62
    //   49: aload_2
    //   50: getfield left : I
    //   53: aload_1
    //   54: getfield right : I
    //   57: if_icmpgt -> 62
    //   60: iconst_1
    //   61: ireturn
    //   62: iconst_0
    //   63: ireturn
    //   64: aload_2
    //   65: getfield bottom : I
    //   68: aload_1
    //   69: getfield top : I
    //   72: if_icmplt -> 88
    //   75: aload_2
    //   76: getfield top : I
    //   79: aload_1
    //   80: getfield bottom : I
    //   83: if_icmpgt -> 88
    //   86: iconst_1
    //   87: ireturn
    //   88: iconst_0
    //   89: ireturn }
  
  public static <L, T> T findNextFocusInAbsoluteDirection(@NonNull L paramL, @NonNull CollectionAdapter<L, T> paramCollectionAdapter, @NonNull BoundsAdapter<T> paramBoundsAdapter, @Nullable T paramT, @NonNull Rect paramRect, int paramInt) {
    Rect rect1 = new Rect(paramRect);
    if (paramInt != 17) {
      if (paramInt != 33) {
        if (paramInt != 66) {
          if (paramInt == 130) {
            rect1.offset(0, -(paramRect.height() + 1));
          } else {
            throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
          } 
        } else {
          rect1.offset(-(paramRect.width() + 1), 0);
        } 
      } else {
        rect1.offset(0, paramRect.height() + 1);
      } 
    } else {
      rect1.offset(paramRect.width() + 1, 0);
    } 
    Object object = null;
    int i = paramCollectionAdapter.size(paramL);
    Rect rect2 = new Rect();
    byte b;
    for (b = 0; b < i; b++) {
      Object object1 = paramCollectionAdapter.get(paramL, b);
      if (object1 != paramT) {
        paramBoundsAdapter.obtainBounds(object1, rect2);
        if (isBetterCandidate(paramInt, paramRect, rect2, rect1)) {
          rect1.set(rect2);
          object = object1;
        } 
      } 
    } 
    return (T)object;
  }
  
  public static <L, T> T findNextFocusInRelativeDirection(@NonNull L paramL, @NonNull CollectionAdapter<L, T> paramCollectionAdapter, @NonNull BoundsAdapter<T> paramBoundsAdapter, @Nullable T paramT, int paramInt, boolean paramBoolean1, boolean paramBoolean2) {
    int i = paramCollectionAdapter.size(paramL);
    ArrayList arrayList = new ArrayList(i);
    byte b;
    for (b = 0; b < i; b++)
      arrayList.add(paramCollectionAdapter.get(paramL, b)); 
    Collections.sort(arrayList, new SequentialComparator(paramBoolean1, paramBoundsAdapter));
    switch (paramInt) {
      default:
        throw new IllegalArgumentException("direction must be one of {FOCUS_FORWARD, FOCUS_BACKWARD}.");
      case 2:
        return (T)getNextFocusable(paramT, arrayList, paramBoolean2);
      case 1:
        break;
    } 
    return (T)getPreviousFocusable(paramT, arrayList, paramBoolean2);
  }
  
  private static <T> T getNextFocusable(T paramT, ArrayList<T> paramArrayList, boolean paramBoolean) {
    int i;
    int j = paramArrayList.size();
    if (paramT == null) {
      i = -1;
    } else {
      i = paramArrayList.lastIndexOf(paramT);
    } 
    return (++i < j) ? (T)paramArrayList.get(i) : ((paramBoolean && j > 0) ? (T)paramArrayList.get(0) : null);
  }
  
  private static <T> T getPreviousFocusable(T paramT, ArrayList<T> paramArrayList, boolean paramBoolean) {
    int i;
    int j = paramArrayList.size();
    if (paramT == null) {
      i = j;
    } else {
      i = paramArrayList.indexOf(paramT);
    } 
    return (--i >= 0) ? (T)paramArrayList.get(i) : ((paramBoolean && j > 0) ? (T)paramArrayList.get(j - 1) : null);
  }
  
  private static int getWeightedDistanceFor(int paramInt1, int paramInt2) { return paramInt1 * 13 * paramInt1 + paramInt2 * paramInt2; }
  
  private static boolean isBetterCandidate(int paramInt, @NonNull Rect paramRect1, @NonNull Rect paramRect2, @NonNull Rect paramRect3) {
    boolean bool2 = isCandidate(paramRect1, paramRect2, paramInt);
    boolean bool1 = false;
    if (!bool2)
      return false; 
    if (!isCandidate(paramRect1, paramRect3, paramInt))
      return true; 
    if (beamBeats(paramInt, paramRect1, paramRect2, paramRect3))
      return true; 
    if (beamBeats(paramInt, paramRect1, paramRect3, paramRect2))
      return false; 
    if (getWeightedDistanceFor(majorAxisDistance(paramInt, paramRect1, paramRect2), minorAxisDistance(paramInt, paramRect1, paramRect2)) < getWeightedDistanceFor(majorAxisDistance(paramInt, paramRect1, paramRect3), minorAxisDistance(paramInt, paramRect1, paramRect3)))
      bool1 = true; 
    return bool1;
  }
  
  private static boolean isCandidate(@NonNull Rect paramRect1, @NonNull Rect paramRect2, int paramInt) {
    if (paramInt != 17) {
      if (paramInt != 33) {
        if (paramInt != 66) {
          if (paramInt == 130)
            return ((paramRect1.top < paramRect2.top || paramRect1.bottom <= paramRect2.top) && paramRect1.bottom < paramRect2.bottom); 
          throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        } 
        return ((paramRect1.left < paramRect2.left || paramRect1.right <= paramRect2.left) && paramRect1.right < paramRect2.right);
      } 
      return ((paramRect1.bottom > paramRect2.bottom || paramRect1.top >= paramRect2.bottom) && paramRect1.top > paramRect2.top);
    } 
    return ((paramRect1.right > paramRect2.right || paramRect1.left >= paramRect2.right) && paramRect1.left > paramRect2.left);
  }
  
  private static boolean isToDirectionOf(int paramInt, @NonNull Rect paramRect1, @NonNull Rect paramRect2) {
    if (paramInt != 17) {
      if (paramInt != 33) {
        if (paramInt != 66) {
          if (paramInt == 130)
            return (paramRect1.bottom <= paramRect2.top); 
          throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        } 
        return (paramRect1.right <= paramRect2.left);
      } 
      return (paramRect1.top >= paramRect2.bottom);
    } 
    return (paramRect1.left >= paramRect2.right);
  }
  
  private static int majorAxisDistance(int paramInt, @NonNull Rect paramRect1, @NonNull Rect paramRect2) { return Math.max(0, majorAxisDistanceRaw(paramInt, paramRect1, paramRect2)); }
  
  private static int majorAxisDistanceRaw(int paramInt, @NonNull Rect paramRect1, @NonNull Rect paramRect2) {
    if (paramInt != 17) {
      if (paramInt != 33) {
        if (paramInt != 66) {
          if (paramInt == 130)
            return paramRect2.top - paramRect1.bottom; 
          throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        } 
        return paramRect2.left - paramRect1.right;
      } 
      return paramRect1.top - paramRect2.bottom;
    } 
    return paramRect1.left - paramRect2.right;
  }
  
  private static int majorAxisDistanceToFarEdge(int paramInt, @NonNull Rect paramRect1, @NonNull Rect paramRect2) { return Math.max(1, majorAxisDistanceToFarEdgeRaw(paramInt, paramRect1, paramRect2)); }
  
  private static int majorAxisDistanceToFarEdgeRaw(int paramInt, @NonNull Rect paramRect1, @NonNull Rect paramRect2) {
    if (paramInt != 17) {
      if (paramInt != 33) {
        if (paramInt != 66) {
          if (paramInt == 130)
            return paramRect2.bottom - paramRect1.bottom; 
          throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        } 
        return paramRect2.right - paramRect1.right;
      } 
      return paramRect1.top - paramRect2.top;
    } 
    return paramRect1.left - paramRect2.left;
  }
  
  private static int minorAxisDistance(int paramInt, @NonNull Rect paramRect1, @NonNull Rect paramRect2) {
    if (paramInt != 17)
      if (paramInt != 33) {
        if (paramInt != 66) {
          if (paramInt != 130)
            throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}."); 
          return Math.abs(paramRect1.left + paramRect1.width() / 2 - paramRect2.left + paramRect2.width() / 2);
        } 
      } else {
        return Math.abs(paramRect1.left + paramRect1.width() / 2 - paramRect2.left + paramRect2.width() / 2);
      }  
    return Math.abs(paramRect1.top + paramRect1.height() / 2 - paramRect2.top + paramRect2.height() / 2);
  }
  
  public static interface BoundsAdapter<T> {
    void obtainBounds(T param1T, Rect param1Rect);
  }
  
  public static interface CollectionAdapter<T, V> {
    V get(T param1T, int param1Int);
    
    int size(T param1T);
  }
  
  private static class SequentialComparator<T> extends Object implements Comparator<T> {
    private final FocusStrategy.BoundsAdapter<T> mAdapter;
    
    private final boolean mIsLayoutRtl;
    
    private final Rect mTemp1 = new Rect();
    
    private final Rect mTemp2 = new Rect();
    
    SequentialComparator(boolean param1Boolean, FocusStrategy.BoundsAdapter<T> param1BoundsAdapter) {
      this.mIsLayoutRtl = param1Boolean;
      this.mAdapter = param1BoundsAdapter;
    }
    
    public int compare(T param1T1, T param1T2) {
      Rect rect1 = this.mTemp1;
      Rect rect2 = this.mTemp2;
      this.mAdapter.obtainBounds(param1T1, rect1);
      this.mAdapter.obtainBounds(param1T2, rect2);
      int i = rect1.top;
      int j = rect2.top;
      byte b = -1;
      if (i < j)
        return -1; 
      if (rect1.top > rect2.top)
        return 1; 
      if (rect1.left < rect2.left) {
        if (this.mIsLayoutRtl)
          b = 1; 
        return b;
      } 
      if (rect1.left > rect2.left)
        return this.mIsLayoutRtl ? -1 : 1; 
      if (rect1.bottom < rect2.bottom)
        return -1; 
      if (rect1.bottom > rect2.bottom)
        return 1; 
      if (rect1.right < rect2.right) {
        if (this.mIsLayoutRtl)
          b = 1; 
        return b;
      } 
      return (rect1.right > rect2.right) ? (this.mIsLayoutRtl ? -1 : 1) : 0;
    }
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v4\widget\FocusStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */