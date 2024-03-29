package android.support.v4.text.util;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.util.PatternsCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.webkit.WebView;
import android.widget.TextView;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class LinkifyCompat {
  private static final Comparator<LinkSpec> COMPARATOR;
  
  private static final String[] EMPTY_STRING = new String[0];
  
  static  {
    COMPARATOR = new Comparator<LinkSpec>() {
        public int compare(LinkifyCompat.LinkSpec param1LinkSpec1, LinkifyCompat.LinkSpec param1LinkSpec2) { return (param1LinkSpec1.start < param1LinkSpec2.start) ? -1 : ((param1LinkSpec1.start > param1LinkSpec2.start) ? 1 : ((param1LinkSpec1.end < param1LinkSpec2.end) ? 1 : ((param1LinkSpec1.end > param1LinkSpec2.end) ? -1 : 0))); }
      };
  }
  
  private static void addLinkMovementMethod(@NonNull TextView paramTextView) {
    MovementMethod movementMethod = paramTextView.getMovementMethod();
    if ((movementMethod == null || !(movementMethod instanceof LinkMovementMethod)) && paramTextView.getLinksClickable())
      paramTextView.setMovementMethod(LinkMovementMethod.getInstance()); 
  }
  
  public static void addLinks(@NonNull TextView paramTextView, @NonNull Pattern paramPattern, @Nullable String paramString) {
    if (shouldAddLinksFallbackToFramework()) {
      Linkify.addLinks(paramTextView, paramPattern, paramString);
      return;
    } 
    addLinks(paramTextView, paramPattern, paramString, null, null, null);
  }
  
  public static void addLinks(@NonNull TextView paramTextView, @NonNull Pattern paramPattern, @Nullable String paramString, @Nullable Linkify.MatchFilter paramMatchFilter, @Nullable Linkify.TransformFilter paramTransformFilter) {
    if (shouldAddLinksFallbackToFramework()) {
      Linkify.addLinks(paramTextView, paramPattern, paramString, paramMatchFilter, paramTransformFilter);
      return;
    } 
    addLinks(paramTextView, paramPattern, paramString, null, paramMatchFilter, paramTransformFilter);
  }
  
  @SuppressLint({"NewApi"})
  public static void addLinks(@NonNull TextView paramTextView, @NonNull Pattern paramPattern, @Nullable String paramString, @Nullable String[] paramArrayOfString, @Nullable Linkify.MatchFilter paramMatchFilter, @Nullable Linkify.TransformFilter paramTransformFilter) {
    if (shouldAddLinksFallbackToFramework()) {
      Linkify.addLinks(paramTextView, paramPattern, paramString, paramArrayOfString, paramMatchFilter, paramTransformFilter);
      return;
    } 
    SpannableString spannableString = SpannableString.valueOf(paramTextView.getText());
    if (addLinks(spannableString, paramPattern, paramString, paramArrayOfString, paramMatchFilter, paramTransformFilter)) {
      paramTextView.setText(spannableString);
      addLinkMovementMethod(paramTextView);
    } 
  }
  
  public static boolean addLinks(@NonNull Spannable paramSpannable, int paramInt) {
    if (shouldAddLinksFallbackToFramework())
      return Linkify.addLinks(paramSpannable, paramInt); 
    if (paramInt == 0)
      return false; 
    URLSpan[] arrayOfURLSpan = (URLSpan[])paramSpannable.getSpans(0, paramSpannable.length(), URLSpan.class);
    for (int i = arrayOfURLSpan.length - 1; i >= 0; i--)
      paramSpannable.removeSpan(arrayOfURLSpan[i]); 
    if ((paramInt & 0x4) != 0)
      Linkify.addLinks(paramSpannable, 4); 
    ArrayList arrayList = new ArrayList();
    if ((paramInt & true) != 0) {
      Pattern pattern = PatternsCompat.AUTOLINK_WEB_URL;
      Linkify.MatchFilter matchFilter = Linkify.sUrlMatchFilter;
      gatherLinks(arrayList, paramSpannable, pattern, new String[] { "http://", "https://", "rtsp://" }, matchFilter, null);
    } 
    if ((paramInt & 0x2) != 0)
      gatherLinks(arrayList, paramSpannable, PatternsCompat.AUTOLINK_EMAIL_ADDRESS, new String[] { "mailto:" }, null, null); 
    if ((paramInt & 0x8) != 0)
      gatherMapLinks(arrayList, paramSpannable); 
    pruneOverlaps(arrayList, paramSpannable);
    if (arrayList.size() == 0)
      return false; 
    for (LinkSpec linkSpec : arrayList) {
      if (linkSpec.frameworkAddedSpan == null)
        applyLink(linkSpec.url, linkSpec.start, linkSpec.end, paramSpannable); 
    } 
    return true;
  }
  
  public static boolean addLinks(@NonNull Spannable paramSpannable, @NonNull Pattern paramPattern, @Nullable String paramString) { return shouldAddLinksFallbackToFramework() ? Linkify.addLinks(paramSpannable, paramPattern, paramString) : addLinks(paramSpannable, paramPattern, paramString, null, null, null); }
  
  public static boolean addLinks(@NonNull Spannable paramSpannable, @NonNull Pattern paramPattern, @Nullable String paramString, @Nullable Linkify.MatchFilter paramMatchFilter, @Nullable Linkify.TransformFilter paramTransformFilter) { return shouldAddLinksFallbackToFramework() ? Linkify.addLinks(paramSpannable, paramPattern, paramString, paramMatchFilter, paramTransformFilter) : addLinks(paramSpannable, paramPattern, paramString, null, paramMatchFilter, paramTransformFilter); }
  
  @SuppressLint({"NewApi"})
  public static boolean addLinks(@NonNull Spannable paramSpannable, @NonNull Pattern paramPattern, @Nullable String paramString, @Nullable String[] paramArrayOfString, @Nullable Linkify.MatchFilter paramMatchFilter, @Nullable Linkify.TransformFilter paramTransformFilter) { // Byte code:
    //   0: invokestatic shouldAddLinksFallbackToFramework : ()Z
    //   3: ifeq -> 18
    //   6: aload_0
    //   7: aload_1
    //   8: aload_2
    //   9: aload_3
    //   10: aload #4
    //   12: aload #5
    //   14: invokestatic addLinks : (Landroid/text/Spannable;Ljava/util/regex/Pattern;Ljava/lang/String;[Ljava/lang/String;Landroid/text/util/Linkify$MatchFilter;Landroid/text/util/Linkify$TransformFilter;)Z
    //   17: ireturn
    //   18: aload_2
    //   19: astore #10
    //   21: aload_2
    //   22: ifnonnull -> 29
    //   25: ldc ''
    //   27: astore #10
    //   29: aload_3
    //   30: ifnull -> 41
    //   33: aload_3
    //   34: astore_2
    //   35: aload_3
    //   36: arraylength
    //   37: iconst_1
    //   38: if_icmpge -> 45
    //   41: getstatic android/support/v4/text/util/LinkifyCompat.EMPTY_STRING : [Ljava/lang/String;
    //   44: astore_2
    //   45: aload_2
    //   46: arraylength
    //   47: iconst_1
    //   48: iadd
    //   49: anewarray java/lang/String
    //   52: astore #11
    //   54: aload #11
    //   56: iconst_0
    //   57: aload #10
    //   59: getstatic java/util/Locale.ROOT : Ljava/util/Locale;
    //   62: invokevirtual toLowerCase : (Ljava/util/Locale;)Ljava/lang/String;
    //   65: aastore
    //   66: iconst_0
    //   67: istore #6
    //   69: iload #6
    //   71: aload_2
    //   72: arraylength
    //   73: if_icmpge -> 116
    //   76: aload_2
    //   77: iload #6
    //   79: aaload
    //   80: astore_3
    //   81: aload_3
    //   82: ifnonnull -> 91
    //   85: ldc ''
    //   87: astore_3
    //   88: goto -> 99
    //   91: aload_3
    //   92: getstatic java/util/Locale.ROOT : Ljava/util/Locale;
    //   95: invokevirtual toLowerCase : (Ljava/util/Locale;)Ljava/lang/String;
    //   98: astore_3
    //   99: aload #11
    //   101: iload #6
    //   103: iconst_1
    //   104: iadd
    //   105: aload_3
    //   106: aastore
    //   107: iload #6
    //   109: iconst_1
    //   110: iadd
    //   111: istore #6
    //   113: goto -> 69
    //   116: iconst_0
    //   117: istore #8
    //   119: aload_1
    //   120: aload_0
    //   121: invokevirtual matcher : (Ljava/lang/CharSequence;)Ljava/util/regex/Matcher;
    //   124: astore_1
    //   125: aload_1
    //   126: invokevirtual find : ()Z
    //   129: ifeq -> 198
    //   132: aload_1
    //   133: invokevirtual start : ()I
    //   136: istore #6
    //   138: aload_1
    //   139: invokevirtual end : ()I
    //   142: istore #7
    //   144: iconst_1
    //   145: istore #9
    //   147: aload #4
    //   149: ifnull -> 166
    //   152: aload #4
    //   154: aload_0
    //   155: iload #6
    //   157: iload #7
    //   159: invokeinterface acceptMatch : (Ljava/lang/CharSequence;II)Z
    //   164: istore #9
    //   166: iload #9
    //   168: ifeq -> 195
    //   171: aload_1
    //   172: iconst_0
    //   173: invokevirtual group : (I)Ljava/lang/String;
    //   176: aload #11
    //   178: aload_1
    //   179: aload #5
    //   181: invokestatic makeUrl : (Ljava/lang/String;[Ljava/lang/String;Ljava/util/regex/Matcher;Landroid/text/util/Linkify$TransformFilter;)Ljava/lang/String;
    //   184: iload #6
    //   186: iload #7
    //   188: aload_0
    //   189: invokestatic applyLink : (Ljava/lang/String;IILandroid/text/Spannable;)V
    //   192: iconst_1
    //   193: istore #8
    //   195: goto -> 125
    //   198: iload #8
    //   200: ireturn }
  
  public static boolean addLinks(@NonNull TextView paramTextView, int paramInt) {
    if (shouldAddLinksFallbackToFramework())
      return Linkify.addLinks(paramTextView, paramInt); 
    if (paramInt == 0)
      return false; 
    CharSequence charSequence = paramTextView.getText();
    if (charSequence instanceof Spannable) {
      if (addLinks((Spannable)charSequence, paramInt)) {
        addLinkMovementMethod(paramTextView);
        return true;
      } 
      return false;
    } 
    SpannableString spannableString = SpannableString.valueOf(charSequence);
    if (addLinks(spannableString, paramInt)) {
      addLinkMovementMethod(paramTextView);
      paramTextView.setText(spannableString);
      return true;
    } 
    return false;
  }
  
  private static void applyLink(String paramString, int paramInt1, int paramInt2, Spannable paramSpannable) { paramSpannable.setSpan(new URLSpan(paramString), paramInt1, paramInt2, 33); }
  
  private static String findAddress(String paramString) { return (Build.VERSION.SDK_INT >= 28) ? WebView.findAddress(paramString) : FindAddress.findAddress(paramString); }
  
  private static void gatherLinks(ArrayList<LinkSpec> paramArrayList, Spannable paramSpannable, Pattern paramPattern, String[] paramArrayOfString, Linkify.MatchFilter paramMatchFilter, Linkify.TransformFilter paramTransformFilter) {
    Matcher matcher = paramPattern.matcher(paramSpannable);
    while (matcher.find()) {
      int i = matcher.start();
      int j = matcher.end();
      if (paramMatchFilter == null || paramMatchFilter.acceptMatch(paramSpannable, i, j)) {
        LinkSpec linkSpec = new LinkSpec();
        linkSpec.url = makeUrl(matcher.group(0), paramArrayOfString, matcher, paramTransformFilter);
        linkSpec.start = i;
        linkSpec.end = j;
        paramArrayList.add(linkSpec);
      } 
    } 
  }
  
  private static void gatherMapLinks(ArrayList<LinkSpec> paramArrayList, Spannable paramSpannable) {
    String str = paramSpannable.toString();
    int i = 0;
    try {
      while (true) {
        String str1 = findAddress(str);
        if (str1 != null) {
          int j = str.indexOf(str1);
          if (j < 0)
            return; 
          linkSpec = new LinkSpec();
          int k = j + str1.length();
          linkSpec.start = i + j;
          linkSpec.end = i + k;
          str = str.substring(k);
          i += k;
          try {
            str1 = URLEncoder.encode(str1, "UTF-8");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("geo:0,0?q=");
            stringBuilder.append(str1);
            linkSpec.url = stringBuilder.toString();
            paramArrayList.add(linkSpec);
            continue;
          } catch (UnsupportedEncodingException linkSpec) {
            continue;
          } 
        } 
        break;
      } 
      return;
    } catch (UnsupportedOperationException paramArrayList) {
      return;
    } 
  }
  
  private static String makeUrl(@NonNull String paramString, @NonNull String[] paramArrayOfString, Matcher paramMatcher, @Nullable Linkify.TransformFilter paramTransformFilter) {
    byte b2;
    String str1;
    String str3 = paramString;
    if (paramTransformFilter != null)
      str3 = paramTransformFilter.transformUrl(paramMatcher, paramString); 
    byte b3 = 0;
    byte b1 = 0;
    while (true) {
      b2 = b3;
      paramString = str3;
      if (b1 < paramArrayOfString.length) {
        if (str3.regionMatches(true, 0, paramArrayOfString[b1], 0, paramArrayOfString[b1].length())) {
          b3 = 1;
          b2 = b3;
          paramString = str3;
          if (!str3.regionMatches(false, 0, paramArrayOfString[b1], 0, paramArrayOfString[b1].length())) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(paramArrayOfString[b1]);
            stringBuilder.append(str3.substring(paramArrayOfString[b1].length()));
            str1 = stringBuilder.toString();
            b2 = b3;
          } 
          break;
        } 
        b1++;
        continue;
      } 
      break;
    } 
    String str2 = str1;
    if (b2 == 0) {
      str2 = str1;
      if (paramArrayOfString.length > 0) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(paramArrayOfString[0]);
        stringBuilder.append(str1);
        str2 = stringBuilder.toString();
      } 
    } 
    return str2;
  }
  
  private static void pruneOverlaps(ArrayList<LinkSpec> paramArrayList, Spannable paramSpannable) {
    URLSpan[] arrayOfURLSpan = (URLSpan[])paramSpannable.getSpans(0, paramSpannable.length(), URLSpan.class);
    byte b1;
    for (b1 = 0; b1 < arrayOfURLSpan.length; b1++) {
      LinkSpec linkSpec = new LinkSpec();
      linkSpec.frameworkAddedSpan = arrayOfURLSpan[b1];
      linkSpec.start = paramSpannable.getSpanStart(arrayOfURLSpan[b1]);
      linkSpec.end = paramSpannable.getSpanEnd(arrayOfURLSpan[b1]);
      paramArrayList.add(linkSpec);
    } 
    Collections.sort(paramArrayList, COMPARATOR);
    int i = paramArrayList.size();
    for (byte b2 = 0; b2 < i - 1; b2++) {
      LinkSpec linkSpec1 = (LinkSpec)paramArrayList.get(b2);
      LinkSpec linkSpec2 = (LinkSpec)paramArrayList.get(b2 + 1);
      b1 = -1;
      if (linkSpec1.start <= linkSpec2.start && linkSpec1.end > linkSpec2.start) {
        if (linkSpec2.end <= linkSpec1.end) {
          b1 = b2 + 1;
        } else if (linkSpec1.end - linkSpec1.start > linkSpec2.end - linkSpec2.start) {
          b1 = b2 + 1;
        } else if (linkSpec1.end - linkSpec1.start < linkSpec2.end - linkSpec2.start) {
          b1 = b2;
        } 
        if (b1 != -1) {
          URLSpan uRLSpan = ((LinkSpec)paramArrayList.get(b1)).frameworkAddedSpan;
          if (uRLSpan != null)
            paramSpannable.removeSpan(uRLSpan); 
          paramArrayList.remove(b1);
          i--;
          continue;
        } 
      } 
    } 
  }
  
  private static boolean shouldAddLinksFallbackToFramework() { return (Build.VERSION.SDK_INT >= 28); }
  
  private static class LinkSpec {
    int end;
    
    URLSpan frameworkAddedSpan;
    
    int start;
    
    String url;
  }
  
  @Retention(RetentionPolicy.SOURCE)
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static @interface LinkifyMask {}
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v4\tex\\util\LinkifyCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */