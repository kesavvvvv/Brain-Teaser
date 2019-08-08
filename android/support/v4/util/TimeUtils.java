package android.support.v4.util;

import android.support.annotation.RestrictTo;
import java.io.PrintWriter;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public final class TimeUtils {
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static final int HUNDRED_DAY_FIELD_LEN = 19;
  
  private static final int SECONDS_PER_DAY = 86400;
  
  private static final int SECONDS_PER_HOUR = 3600;
  
  private static final int SECONDS_PER_MINUTE = 60;
  
  private static char[] sFormatStr;
  
  private static final Object sFormatSync = new Object();
  
  static  {
    sFormatStr = new char[24];
  }
  
  private static int accumField(int paramInt1, int paramInt2, boolean paramBoolean, int paramInt3) { return (paramInt1 > 99 || (paramBoolean && paramInt3 >= 3)) ? (paramInt2 + 3) : ((paramInt1 > 9 || (paramBoolean && paramInt3 >= 2)) ? (paramInt2 + 2) : ((paramBoolean || paramInt1 > 0) ? (paramInt2 + 1) : 0)); }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static void formatDuration(long paramLong1, long paramLong2, PrintWriter paramPrintWriter) {
    if (paramLong1 == 0L) {
      paramPrintWriter.print("--");
      return;
    } 
    formatDuration(paramLong1 - paramLong2, paramPrintWriter, 0);
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static void formatDuration(long paramLong, PrintWriter paramPrintWriter) { formatDuration(paramLong, paramPrintWriter, 0); }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static void formatDuration(long paramLong, PrintWriter paramPrintWriter, int paramInt) {
    synchronized (sFormatSync) {
      paramInt = formatDurationLocked(paramLong, paramInt);
      paramPrintWriter.print(new String(sFormatStr, 0, paramInt));
      return;
    } 
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static void formatDuration(long paramLong, StringBuilder paramStringBuilder) {
    synchronized (sFormatSync) {
      int i = formatDurationLocked(paramLong, 0);
      paramStringBuilder.append(sFormatStr, 0, i);
      return;
    } 
  }
  
  private static int formatDurationLocked(long paramLong, int paramInt) {
    byte b3;
    byte b2;
    int k;
    byte b1;
    if (sFormatStr.length < paramInt)
      sFormatStr = new char[paramInt]; 
    char[] arrayOfChar = sFormatStr;
    if (paramLong == 0L) {
      while (paramInt - 1 < 0)
        arrayOfChar[0] = ' '; 
      arrayOfChar[0] = '0';
      return false + true;
    } 
    if (paramLong > 0L) {
      b1 = 43;
    } else {
      paramLong = -paramLong;
      b1 = 45;
    } 
    int i3 = (int)(paramLong % 1000L);
    int i = (int)Math.floor((paramLong / 1000L));
    if (i > 86400) {
      j = i / 86400;
      i -= 86400 * j;
    } else {
      j = 0;
    } 
    if (i > 3600) {
      b2 = i / 3600;
      i -= b2 * 3600;
    } else {
      b2 = 0;
    } 
    if (i > 60) {
      b3 = i / 60;
      k = i - b3 * 60;
    } else {
      b3 = 0;
      k = i;
    } 
    int n = 0;
    int i2 = 0;
    int i1 = 3;
    boolean bool = false;
    if (paramInt != 0) {
      i = accumField(j, 1, false, 0);
      if (i > 0)
        bool = true; 
      i += accumField(b2, 1, bool, 2);
      if (i > 0) {
        bool = true;
      } else {
        bool = false;
      } 
      i += accumField(b3, 1, bool, 2);
      if (i > 0) {
        bool = true;
      } else {
        bool = false;
      } 
      int i4 = i + accumField(k, 1, bool, 2);
      if (i4 > 0) {
        i = 3;
      } else {
        i = 0;
      } 
      i4 += accumField(i3, 2, true, i) + 1;
      i = i2;
      while (true) {
        n = i;
        if (i4 < paramInt) {
          arrayOfChar[i] = ' ';
          i++;
          i4++;
          continue;
        } 
        break;
      } 
    } 
    arrayOfChar[n] = b1;
    n++;
    if (paramInt != 0) {
      paramInt = 1;
    } else {
      paramInt = 0;
    } 
    boolean bool1 = true;
    int m = 2;
    int j = printField(arrayOfChar, j, 'd', n, false, 0);
    if (j != n) {
      bool = true;
    } else {
      bool = false;
    } 
    if (paramInt != 0) {
      i = 2;
    } else {
      i = 0;
    } 
    j = printField(arrayOfChar, b2, 'h', j, bool, i);
    if (j != n) {
      bool = true;
    } else {
      bool = false;
    } 
    if (paramInt != 0) {
      i = 2;
    } else {
      i = 0;
    } 
    j = printField(arrayOfChar, b3, 'm', j, bool, i);
    if (j != n) {
      bool = bool1;
    } else {
      bool = false;
    } 
    if (paramInt != 0) {
      i = m;
    } else {
      i = 0;
    } 
    i = printField(arrayOfChar, k, 's', j, bool, i);
    if (paramInt != 0 && i != n) {
      paramInt = i1;
    } else {
      paramInt = 0;
    } 
    paramInt = printField(arrayOfChar, i3, 'm', i, true, paramInt);
    arrayOfChar[paramInt] = 's';
    return paramInt + 1;
  }
  
  private static int printField(char[] paramArrayOfChar, int paramInt1, char paramChar, int paramInt2, boolean paramBoolean, int paramInt3) { // Byte code:
    //   0: iload #4
    //   2: ifne -> 12
    //   5: iload_3
    //   6: istore #6
    //   8: iload_1
    //   9: ifle -> 146
    //   12: iload #4
    //   14: ifeq -> 23
    //   17: iload #5
    //   19: iconst_3
    //   20: if_icmpge -> 35
    //   23: iload_1
    //   24: istore #6
    //   26: iload_3
    //   27: istore #7
    //   29: iload_1
    //   30: bipush #99
    //   32: if_icmple -> 64
    //   35: iload_1
    //   36: bipush #100
    //   38: idiv
    //   39: istore #6
    //   41: aload_0
    //   42: iload_3
    //   43: iload #6
    //   45: bipush #48
    //   47: iadd
    //   48: i2c
    //   49: castore
    //   50: iload_3
    //   51: iconst_1
    //   52: iadd
    //   53: istore #7
    //   55: iload_1
    //   56: iload #6
    //   58: bipush #100
    //   60: imul
    //   61: isub
    //   62: istore #6
    //   64: iload #4
    //   66: ifeq -> 75
    //   69: iload #5
    //   71: iconst_2
    //   72: if_icmpge -> 95
    //   75: iload #6
    //   77: bipush #9
    //   79: if_icmpgt -> 95
    //   82: iload #6
    //   84: istore #5
    //   86: iload #7
    //   88: istore_1
    //   89: iload_3
    //   90: iload #7
    //   92: if_icmpeq -> 124
    //   95: iload #6
    //   97: bipush #10
    //   99: idiv
    //   100: istore_3
    //   101: aload_0
    //   102: iload #7
    //   104: iload_3
    //   105: bipush #48
    //   107: iadd
    //   108: i2c
    //   109: castore
    //   110: iload #7
    //   112: iconst_1
    //   113: iadd
    //   114: istore_1
    //   115: iload #6
    //   117: iload_3
    //   118: bipush #10
    //   120: imul
    //   121: isub
    //   122: istore #5
    //   124: aload_0
    //   125: iload_1
    //   126: iload #5
    //   128: bipush #48
    //   130: iadd
    //   131: i2c
    //   132: castore
    //   133: iload_1
    //   134: iconst_1
    //   135: iadd
    //   136: istore_1
    //   137: aload_0
    //   138: iload_1
    //   139: iload_2
    //   140: castore
    //   141: iload_1
    //   142: iconst_1
    //   143: iadd
    //   144: istore #6
    //   146: iload #6
    //   148: ireturn }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v\\util\TimeUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */