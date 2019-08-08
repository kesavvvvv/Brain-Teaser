package android.support.v4.os;

import android.os.Build;
import android.support.annotation.GuardedBy;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.Size;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
final class LocaleListHelper {
  private static final Locale EN_LATN;
  
  private static final Locale LOCALE_AR_XB;
  
  private static final Locale LOCALE_EN_XA;
  
  private static final int NUM_PSEUDO_LOCALES = 2;
  
  private static final String STRING_AR_XB = "ar-XB";
  
  private static final String STRING_EN_XA = "en-XA";
  
  @GuardedBy("sLock")
  private static LocaleListHelper sDefaultAdjustedLocaleList;
  
  @GuardedBy("sLock")
  private static LocaleListHelper sDefaultLocaleList;
  
  private static final Locale[] sEmptyList = new Locale[0];
  
  private static final LocaleListHelper sEmptyLocaleList = new LocaleListHelper(new Locale[0]);
  
  @GuardedBy("sLock")
  private static Locale sLastDefaultLocale;
  
  @GuardedBy("sLock")
  private static LocaleListHelper sLastExplicitlySetLocaleList;
  
  private static final Object sLock;
  
  private final Locale[] mList;
  
  @NonNull
  private final String mStringRepresentation;
  
  static  {
    LOCALE_EN_XA = new Locale("en", "XA");
    LOCALE_AR_XB = new Locale("ar", "XB");
    EN_LATN = LocaleHelper.forLanguageTag("en-Latn");
    sLock = new Object();
    sLastExplicitlySetLocaleList = null;
    sDefaultLocaleList = null;
    sDefaultAdjustedLocaleList = null;
    sLastDefaultLocale = null;
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  LocaleListHelper(@NonNull Locale paramLocale, LocaleListHelper paramLocaleListHelper) {
    if (paramLocale != null) {
      byte b;
      int j;
      if (paramLocaleListHelper == null) {
        j = 0;
      } else {
        j = paramLocaleListHelper.mList.length;
      } 
      int k = -1;
      int i = 0;
      while (true) {
        b = k;
        if (i < j) {
          if (paramLocale.equals(paramLocaleListHelper.mList[i])) {
            b = i;
            break;
          } 
          i++;
          continue;
        } 
        break;
      } 
      if (b == -1) {
        i = 1;
      } else {
        i = 0;
      } 
      k = i + j;
      Locale[] arrayOfLocale = new Locale[k];
      arrayOfLocale[0] = (Locale)paramLocale.clone();
      if (b == -1) {
        for (i = 0; i < j; i++)
          arrayOfLocale[i + 1] = (Locale)paramLocaleListHelper.mList[i].clone(); 
      } else {
        for (i = 0; i < b; i++)
          arrayOfLocale[i + 1] = (Locale)paramLocaleListHelper.mList[i].clone(); 
        for (i = b + 1; i < j; i++)
          arrayOfLocale[i] = (Locale)paramLocaleListHelper.mList[i].clone(); 
      } 
      StringBuilder stringBuilder = new StringBuilder();
      for (i = 0; i < k; i++) {
        stringBuilder.append(LocaleHelper.toLanguageTag(arrayOfLocale[i]));
        if (i < k - 1)
          stringBuilder.append(','); 
      } 
      this.mList = arrayOfLocale;
      this.mStringRepresentation = stringBuilder.toString();
      return;
    } 
    throw new NullPointerException("topLocale is null");
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  LocaleListHelper(@NonNull Locale... paramVarArgs) {
    if (paramVarArgs.length == 0) {
      this.mList = sEmptyList;
      this.mStringRepresentation = "";
      return;
    } 
    Locale[] arrayOfLocale = new Locale[paramVarArgs.length];
    HashSet hashSet = new HashSet();
    StringBuilder stringBuilder = new StringBuilder();
    byte b = 0;
    while (b < paramVarArgs.length) {
      Locale locale = paramVarArgs[b];
      if (locale != null) {
        if (!hashSet.contains(locale)) {
          locale = (Locale)locale.clone();
          arrayOfLocale[b] = locale;
          stringBuilder.append(LocaleHelper.toLanguageTag(locale));
          if (b < paramVarArgs.length - 1)
            stringBuilder.append(','); 
          hashSet.add(locale);
          b++;
          continue;
        } 
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("list[");
        stringBuilder2.append(b);
        stringBuilder2.append("] is a repetition");
        throw new IllegalArgumentException(stringBuilder2.toString());
      } 
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("list[");
      stringBuilder1.append(b);
      stringBuilder1.append("] is null");
      throw new NullPointerException(stringBuilder1.toString());
    } 
    this.mList = arrayOfLocale;
    this.mStringRepresentation = stringBuilder.toString();
  }
  
  private Locale computeFirstMatch(Collection<String> paramCollection, boolean paramBoolean) {
    int i = computeFirstMatchIndex(paramCollection, paramBoolean);
    return (i == -1) ? null : this.mList[i];
  }
  
  private int computeFirstMatchIndex(Collection<String> paramCollection, boolean paramBoolean) {
    Locale[] arrayOfLocale = this.mList;
    if (arrayOfLocale.length == 1)
      return 0; 
    if (arrayOfLocale.length == 0)
      return -1; 
    int j = Integer.MAX_VALUE;
    int i = j;
    if (paramBoolean) {
      int k = findFirstMatchIndex(EN_LATN);
      if (k == 0)
        return 0; 
      i = j;
      if (k < Integer.MAX_VALUE)
        i = k; 
    } 
    Iterator iterator = paramCollection.iterator();
    while (iterator.hasNext()) {
      int k = findFirstMatchIndex(LocaleHelper.forLanguageTag((String)iterator.next()));
      if (k == 0)
        return 0; 
      j = i;
      if (k < i)
        j = k; 
      i = j;
    } 
    return (i == Integer.MAX_VALUE) ? 0 : i;
  }
  
  private int findFirstMatchIndex(Locale paramLocale) {
    byte b = 0;
    while (true) {
      Locale[] arrayOfLocale = this.mList;
      if (b < arrayOfLocale.length) {
        if (matchScore(paramLocale, arrayOfLocale[b]) > 0)
          return b; 
        b++;
        continue;
      } 
      break;
    } 
    return Integer.MAX_VALUE;
  }
  
  @NonNull
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  static LocaleListHelper forLanguageTags(@Nullable String paramString) {
    if (paramString == null || paramString.isEmpty())
      return getEmptyLocaleList(); 
    String[] arrayOfString = paramString.split(",", -1);
    Locale[] arrayOfLocale = new Locale[arrayOfString.length];
    for (byte b = 0; b < arrayOfLocale.length; b++)
      arrayOfLocale[b] = LocaleHelper.forLanguageTag(arrayOfString[b]); 
    return new LocaleListHelper(arrayOfLocale);
  }
  
  @NonNull
  @Size(min = 1L)
  static LocaleListHelper getAdjustedDefault() {
    getDefault();
    synchronized (sLock) {
      return sDefaultAdjustedLocaleList;
    } 
  }
  
  @NonNull
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  @Size(min = 1L)
  static LocaleListHelper getDefault() {
    null = Locale.getDefault();
    synchronized (sLock) {
      if (!null.equals(sLastDefaultLocale)) {
        sLastDefaultLocale = null;
        if (sDefaultLocaleList != null && null.equals(sDefaultLocaleList.get(0)))
          return sDefaultLocaleList; 
        sDefaultLocaleList = new LocaleListHelper(null, sLastExplicitlySetLocaleList);
        sDefaultAdjustedLocaleList = sDefaultLocaleList;
      } 
      return sDefaultLocaleList;
    } 
  }
  
  @NonNull
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  static LocaleListHelper getEmptyLocaleList() { return sEmptyLocaleList; }
  
  private static String getLikelyScript(Locale paramLocale) {
    if (Build.VERSION.SDK_INT >= 21) {
      String str = paramLocale.getScript();
      return !str.isEmpty() ? str : "";
    } 
    return "";
  }
  
  private static boolean isPseudoLocale(String paramString) { return ("en-XA".equals(paramString) || "ar-XB".equals(paramString)); }
  
  private static boolean isPseudoLocale(Locale paramLocale) { return (LOCALE_EN_XA.equals(paramLocale) || LOCALE_AR_XB.equals(paramLocale)); }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  static boolean isPseudoLocalesOnly(@Nullable String[] paramArrayOfString) {
    if (paramArrayOfString == null)
      return true; 
    if (paramArrayOfString.length > 3)
      return false; 
    int i = paramArrayOfString.length;
    for (byte b = 0; b < i; b++) {
      String str = paramArrayOfString[b];
      if (!str.isEmpty() && !isPseudoLocale(str))
        return false; 
    } 
    return true;
  }
  
  @IntRange(from = 0L, to = 1L)
  private static int matchScore(Locale paramLocale1, Locale paramLocale2) { throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\r\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:659)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\r\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\r\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\r\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\r\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\r\n"); }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  static void setDefault(@NonNull @Size(min = 1L) LocaleListHelper paramLocaleListHelper) { setDefault(paramLocaleListHelper, 0); }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  static void setDefault(@NonNull @Size(min = 1L) LocaleListHelper paramLocaleListHelper, int paramInt) {
    if (paramLocaleListHelper != null) {
      if (!paramLocaleListHelper.isEmpty())
        synchronized (sLock) {
          (sLastDefaultLocale = paramLocaleListHelper.get(paramInt)).setDefault(sLastDefaultLocale);
          sLastExplicitlySetLocaleList = paramLocaleListHelper;
          sDefaultLocaleList = paramLocaleListHelper;
          if (paramInt == 0) {
            sDefaultAdjustedLocaleList = sDefaultLocaleList;
          } else {
            sDefaultAdjustedLocaleList = new LocaleListHelper(sLastDefaultLocale, sDefaultLocaleList);
          } 
          return;
        }  
      throw new IllegalArgumentException("locales is empty");
    } 
    throw new NullPointerException("locales is null");
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (!(paramObject instanceof LocaleListHelper))
      return false; 
    Locale[] arrayOfLocale = ((LocaleListHelper)paramObject).mList;
    if (this.mList.length != arrayOfLocale.length)
      return false; 
    byte b = 0;
    while (true) {
      Locale[] arrayOfLocale1 = this.mList;
      if (b < arrayOfLocale1.length) {
        if (!arrayOfLocale1[b].equals(arrayOfLocale[b]))
          return false; 
        b++;
        continue;
      } 
      break;
    } 
    return true;
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  Locale get(int paramInt) {
    if (paramInt >= 0) {
      Locale[] arrayOfLocale = this.mList;
      if (paramInt < arrayOfLocale.length)
        return arrayOfLocale[paramInt]; 
    } 
    return null;
  }
  
  @Nullable
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  Locale getFirstMatch(String[] paramArrayOfString) { return computeFirstMatch(Arrays.asList(paramArrayOfString), false); }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  int getFirstMatchIndex(String[] paramArrayOfString) { return computeFirstMatchIndex(Arrays.asList(paramArrayOfString), false); }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  int getFirstMatchIndexWithEnglishSupported(Collection<String> paramCollection) { return computeFirstMatchIndex(paramCollection, true); }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  int getFirstMatchIndexWithEnglishSupported(String[] paramArrayOfString) { return getFirstMatchIndexWithEnglishSupported(Arrays.asList(paramArrayOfString)); }
  
  @Nullable
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  Locale getFirstMatchWithEnglishSupported(String[] paramArrayOfString) { return computeFirstMatch(Arrays.asList(paramArrayOfString), true); }
  
  public int hashCode() {
    int i = 1;
    byte b = 0;
    while (true) {
      Locale[] arrayOfLocale = this.mList;
      if (b < arrayOfLocale.length) {
        i = i * 31 + arrayOfLocale[b].hashCode();
        b++;
        continue;
      } 
      break;
    } 
    return i;
  }
  
  @IntRange(from = -1L)
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  int indexOf(Locale paramLocale) {
    byte b = 0;
    while (true) {
      Locale[] arrayOfLocale = this.mList;
      if (b < arrayOfLocale.length) {
        if (arrayOfLocale[b].equals(paramLocale))
          return b; 
        b++;
        continue;
      } 
      break;
    } 
    return -1;
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  boolean isEmpty() { return (this.mList.length == 0); }
  
  @IntRange(from = 0L)
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  int size() { return this.mList.length; }
  
  @NonNull
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  String toLanguageTags() { return this.mStringRepresentation; }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("[");
    byte b = 0;
    while (true) {
      Locale[] arrayOfLocale = this.mList;
      if (b < arrayOfLocale.length) {
        stringBuilder.append(arrayOfLocale[b]);
        if (b < this.mList.length - 1)
          stringBuilder.append(','); 
        b++;
        continue;
      } 
      break;
    } 
    stringBuilder.append("]");
    return stringBuilder.toString();
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v4\os\LocaleListHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */