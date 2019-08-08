package android.support.v4.util;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.text.TextUtils;
import java.util.Collection;
import java.util.Locale;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class Preconditions {
  public static void checkArgument(boolean paramBoolean) {
    if (paramBoolean)
      return; 
    throw new IllegalArgumentException();
  }
  
  public static void checkArgument(boolean paramBoolean, Object paramObject) {
    if (paramBoolean)
      return; 
    throw new IllegalArgumentException(String.valueOf(paramObject));
  }
  
  public static float checkArgumentFinite(float paramFloat, String paramString) {
    if (!Float.isNaN(paramFloat)) {
      if (!Float.isInfinite(paramFloat))
        return paramFloat; 
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append(paramString);
      stringBuilder1.append(" must not be infinite");
      throw new IllegalArgumentException(stringBuilder1.toString());
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramString);
    stringBuilder.append(" must not be NaN");
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public static float checkArgumentInRange(float paramFloat1, float paramFloat2, float paramFloat3, String paramString) {
    if (!Float.isNaN(paramFloat1)) {
      if (paramFloat1 >= paramFloat2) {
        if (paramFloat1 <= paramFloat3)
          return paramFloat1; 
        throw new IllegalArgumentException(String.format(Locale.US, "%s is out of range of [%f, %f] (too high)", new Object[] { paramString, Float.valueOf(paramFloat2), Float.valueOf(paramFloat3) }));
      } 
      throw new IllegalArgumentException(String.format(Locale.US, "%s is out of range of [%f, %f] (too low)", new Object[] { paramString, Float.valueOf(paramFloat2), Float.valueOf(paramFloat3) }));
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramString);
    stringBuilder.append(" must not be NaN");
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public static int checkArgumentInRange(int paramInt1, int paramInt2, int paramInt3, String paramString) {
    if (paramInt1 >= paramInt2) {
      if (paramInt1 <= paramInt3)
        return paramInt1; 
      throw new IllegalArgumentException(String.format(Locale.US, "%s is out of range of [%d, %d] (too high)", new Object[] { paramString, Integer.valueOf(paramInt2), Integer.valueOf(paramInt3) }));
    } 
    throw new IllegalArgumentException(String.format(Locale.US, "%s is out of range of [%d, %d] (too low)", new Object[] { paramString, Integer.valueOf(paramInt2), Integer.valueOf(paramInt3) }));
  }
  
  public static long checkArgumentInRange(long paramLong1, long paramLong2, long paramLong3, String paramString) {
    if (paramLong1 >= paramLong2) {
      if (paramLong1 <= paramLong3)
        return paramLong1; 
      throw new IllegalArgumentException(String.format(Locale.US, "%s is out of range of [%d, %d] (too high)", new Object[] { paramString, Long.valueOf(paramLong2), Long.valueOf(paramLong3) }));
    } 
    throw new IllegalArgumentException(String.format(Locale.US, "%s is out of range of [%d, %d] (too low)", new Object[] { paramString, Long.valueOf(paramLong2), Long.valueOf(paramLong3) }));
  }
  
  @IntRange(from = 0L)
  public static int checkArgumentNonnegative(int paramInt) {
    if (paramInt >= 0)
      return paramInt; 
    throw new IllegalArgumentException();
  }
  
  @IntRange(from = 0L)
  public static int checkArgumentNonnegative(int paramInt, String paramString) {
    if (paramInt >= 0)
      return paramInt; 
    throw new IllegalArgumentException(paramString);
  }
  
  public static long checkArgumentNonnegative(long paramLong) {
    if (paramLong >= 0L)
      return paramLong; 
    throw new IllegalArgumentException();
  }
  
  public static long checkArgumentNonnegative(long paramLong, String paramString) {
    if (paramLong >= 0L)
      return paramLong; 
    throw new IllegalArgumentException(paramString);
  }
  
  public static int checkArgumentPositive(int paramInt, String paramString) {
    if (paramInt > 0)
      return paramInt; 
    throw new IllegalArgumentException(paramString);
  }
  
  public static float[] checkArrayElementsInRange(float[] paramArrayOfFloat, float paramFloat1, float paramFloat2, String paramString) {
    StringBuilder stringBuilder1;
    StringBuilder stringBuilder2 = new StringBuilder();
    stringBuilder2.append(paramString);
    stringBuilder2.append(" must not be null");
    checkNotNull(paramArrayOfFloat, stringBuilder2.toString());
    byte b = 0;
    while (b < paramArrayOfFloat.length) {
      float f = paramArrayOfFloat[b];
      if (!Float.isNaN(f)) {
        if (f >= paramFloat1) {
          if (f <= paramFloat2) {
            b++;
            continue;
          } 
          throw new IllegalArgumentException(String.format(Locale.US, "%s[%d] is out of range of [%f, %f] (too high)", new Object[] { paramString, Integer.valueOf(b), Float.valueOf(paramFloat1), Float.valueOf(paramFloat2) }));
        } 
        throw new IllegalArgumentException(String.format(Locale.US, "%s[%d] is out of range of [%f, %f] (too low)", new Object[] { paramString, Integer.valueOf(b), Float.valueOf(paramFloat1), Float.valueOf(paramFloat2) }));
      } 
      stringBuilder1 = new StringBuilder();
      stringBuilder1.append(paramString);
      stringBuilder1.append("[");
      stringBuilder1.append(b);
      stringBuilder1.append("] must not be NaN");
      throw new IllegalArgumentException(stringBuilder1.toString());
    } 
    return stringBuilder1;
  }
  
  public static <T> T[] checkArrayElementsNotNull(T[] paramArrayOfT, String paramString) { // Byte code:
    //   0: aload_0
    //   1: ifnull -> 58
    //   4: iconst_0
    //   5: istore_2
    //   6: iload_2
    //   7: aload_0
    //   8: arraylength
    //   9: if_icmpge -> 56
    //   12: aload_0
    //   13: iload_2
    //   14: aaload
    //   15: ifnull -> 25
    //   18: iload_2
    //   19: iconst_1
    //   20: iadd
    //   21: istore_2
    //   22: goto -> 6
    //   25: new java/lang/NullPointerException
    //   28: dup
    //   29: getstatic java/util/Locale.US : Ljava/util/Locale;
    //   32: ldc '%s[%d] must not be null'
    //   34: iconst_2
    //   35: anewarray java/lang/Object
    //   38: dup
    //   39: iconst_0
    //   40: aload_1
    //   41: aastore
    //   42: dup
    //   43: iconst_1
    //   44: iload_2
    //   45: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   48: aastore
    //   49: invokestatic format : (Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   52: invokespecial <init> : (Ljava/lang/String;)V
    //   55: athrow
    //   56: aload_0
    //   57: areturn
    //   58: new java/lang/StringBuilder
    //   61: dup
    //   62: invokespecial <init> : ()V
    //   65: astore_0
    //   66: aload_0
    //   67: aload_1
    //   68: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   71: pop
    //   72: aload_0
    //   73: ldc ' must not be null'
    //   75: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   78: pop
    //   79: new java/lang/NullPointerException
    //   82: dup
    //   83: aload_0
    //   84: invokevirtual toString : ()Ljava/lang/String;
    //   87: invokespecial <init> : (Ljava/lang/String;)V
    //   90: athrow }
  
  @NonNull
  public static <C extends Collection<T>, T> C checkCollectionElementsNotNull(C paramC, String paramString) { // Byte code:
    //   0: aload_0
    //   1: ifnull -> 74
    //   4: lconst_0
    //   5: lstore_2
    //   6: aload_0
    //   7: invokeinterface iterator : ()Ljava/util/Iterator;
    //   12: astore #4
    //   14: aload #4
    //   16: invokeinterface hasNext : ()Z
    //   21: ifeq -> 72
    //   24: aload #4
    //   26: invokeinterface next : ()Ljava/lang/Object;
    //   31: ifnull -> 41
    //   34: lload_2
    //   35: lconst_1
    //   36: ladd
    //   37: lstore_2
    //   38: goto -> 14
    //   41: new java/lang/NullPointerException
    //   44: dup
    //   45: getstatic java/util/Locale.US : Ljava/util/Locale;
    //   48: ldc '%s[%d] must not be null'
    //   50: iconst_2
    //   51: anewarray java/lang/Object
    //   54: dup
    //   55: iconst_0
    //   56: aload_1
    //   57: aastore
    //   58: dup
    //   59: iconst_1
    //   60: lload_2
    //   61: invokestatic valueOf : (J)Ljava/lang/Long;
    //   64: aastore
    //   65: invokestatic format : (Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   68: invokespecial <init> : (Ljava/lang/String;)V
    //   71: athrow
    //   72: aload_0
    //   73: areturn
    //   74: new java/lang/StringBuilder
    //   77: dup
    //   78: invokespecial <init> : ()V
    //   81: astore_0
    //   82: aload_0
    //   83: aload_1
    //   84: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   87: pop
    //   88: aload_0
    //   89: ldc ' must not be null'
    //   91: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   94: pop
    //   95: new java/lang/NullPointerException
    //   98: dup
    //   99: aload_0
    //   100: invokevirtual toString : ()Ljava/lang/String;
    //   103: invokespecial <init> : (Ljava/lang/String;)V
    //   106: athrow }
  
  public static <T> Collection<T> checkCollectionNotEmpty(Collection<T> paramCollection, String paramString) {
    if (paramCollection != null) {
      if (!paramCollection.isEmpty())
        return paramCollection; 
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append(paramString);
      stringBuilder1.append(" is empty");
      throw new IllegalArgumentException(stringBuilder1.toString());
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramString);
    stringBuilder.append(" must not be null");
    throw new NullPointerException(stringBuilder.toString());
  }
  
  public static int checkFlagsArgument(int paramInt1, int paramInt2) {
    if ((paramInt1 & paramInt2) == paramInt1)
      return paramInt1; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Requested flags 0x");
    stringBuilder.append(Integer.toHexString(paramInt1));
    stringBuilder.append(", but only 0x");
    stringBuilder.append(Integer.toHexString(paramInt2));
    stringBuilder.append(" are allowed");
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  @NonNull
  public static <T> T checkNotNull(T paramT) {
    if (paramT != null)
      return paramT; 
    throw new NullPointerException();
  }
  
  @NonNull
  public static <T> T checkNotNull(T paramT, Object paramObject) {
    if (paramT != null)
      return paramT; 
    throw new NullPointerException(String.valueOf(paramObject));
  }
  
  public static void checkState(boolean paramBoolean) { checkState(paramBoolean, null); }
  
  public static void checkState(boolean paramBoolean, String paramString) {
    if (paramBoolean)
      return; 
    throw new IllegalStateException(paramString);
  }
  
  @NonNull
  public static <T extends CharSequence> T checkStringNotEmpty(T paramT) {
    if (!TextUtils.isEmpty(paramT))
      return paramT; 
    throw new IllegalArgumentException();
  }
  
  @NonNull
  public static <T extends CharSequence> T checkStringNotEmpty(T paramT, Object paramObject) {
    if (!TextUtils.isEmpty(paramT))
      return paramT; 
    throw new IllegalArgumentException(String.valueOf(paramObject));
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v\\util\Preconditions.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */