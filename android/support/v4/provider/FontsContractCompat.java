package android.support.v4.provider;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.CancellationSignal;
import android.os.Handler;
import android.provider.BaseColumns;
import android.support.annotation.GuardedBy;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.annotation.VisibleForTesting;
import android.support.v4.content.res.FontResourcesParserCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.TypefaceCompat;
import android.support.v4.graphics.TypefaceCompatUtil;
import android.support.v4.util.LruCache;
import android.support.v4.util.Preconditions;
import android.support.v4.util.SimpleArrayMap;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class FontsContractCompat {
  private static final int BACKGROUND_THREAD_KEEP_ALIVE_DURATION_MS = 10000;
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static final String PARCEL_FONT_RESULTS = "font_results";
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  static final int RESULT_CODE_PROVIDER_NOT_FOUND = -1;
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  static final int RESULT_CODE_WRONG_CERTIFICATES = -2;
  
  private static final String TAG = "FontsContractCompat";
  
  private static final SelfDestructiveThread sBackgroundThread;
  
  private static final Comparator<byte[]> sByteArrayComparator;
  
  static final Object sLock;
  
  @GuardedBy("sLock")
  static final SimpleArrayMap<String, ArrayList<SelfDestructiveThread.ReplyCallback<TypefaceResult>>> sPendingReplies;
  
  static final LruCache<String, Typeface> sTypefaceCache = new LruCache(16);
  
  static  {
    sBackgroundThread = new SelfDestructiveThread("fonts", 10, 10000);
    sLock = new Object();
    sPendingReplies = new SimpleArrayMap();
    sByteArrayComparator = new Comparator<byte[]>() {
        public int compare(byte[] param1ArrayOfByte1, byte[] param1ArrayOfByte2) {
          if (param1ArrayOfByte1.length != param1ArrayOfByte2.length)
            return param1ArrayOfByte1.length - param1ArrayOfByte2.length; 
          for (byte b = 0; b < param1ArrayOfByte1.length; b++) {
            if (param1ArrayOfByte1[b] != param1ArrayOfByte2[b])
              return param1ArrayOfByte1[b] - param1ArrayOfByte2[b]; 
          } 
          return 0;
        }
      };
  }
  
  @Nullable
  public static Typeface buildTypeface(@NonNull Context paramContext, @Nullable CancellationSignal paramCancellationSignal, @NonNull FontInfo[] paramArrayOfFontInfo) { return TypefaceCompat.createFromFontInfo(paramContext, paramCancellationSignal, paramArrayOfFontInfo, 0); }
  
  private static List<byte[]> convertToByteArrayList(Signature[] paramArrayOfSignature) {
    ArrayList arrayList = new ArrayList();
    for (byte b = 0; b < paramArrayOfSignature.length; b++)
      arrayList.add(paramArrayOfSignature[b].toByteArray()); 
    return arrayList;
  }
  
  private static boolean equalsByteArrayList(List<byte[]> paramList1, List<byte[]> paramList2) {
    if (paramList1.size() != paramList2.size())
      return false; 
    for (byte b = 0; b < paramList1.size(); b++) {
      if (!Arrays.equals((byte[])paramList1.get(b), (byte[])paramList2.get(b)))
        return false; 
    } 
    return true;
  }
  
  @NonNull
  public static FontFamilyResult fetchFonts(@NonNull Context paramContext, @Nullable CancellationSignal paramCancellationSignal, @NonNull FontRequest paramFontRequest) throws PackageManager.NameNotFoundException {
    ProviderInfo providerInfo = getProvider(paramContext.getPackageManager(), paramFontRequest, paramContext.getResources());
    return (providerInfo == null) ? new FontFamilyResult(1, null) : new FontFamilyResult(0, getFontFromProvider(paramContext, paramFontRequest, providerInfo.authority, paramCancellationSignal));
  }
  
  private static List<List<byte[]>> getCertificates(FontRequest paramFontRequest, Resources paramResources) { return (paramFontRequest.getCertificates() != null) ? paramFontRequest.getCertificates() : FontResourcesParserCompat.readCerts(paramResources, paramFontRequest.getCertificatesArrayResId()); }
  
  @NonNull
  @VisibleForTesting
  static FontInfo[] getFontFromProvider(Context paramContext, FontRequest paramFontRequest, String paramString, CancellationSignal paramCancellationSignal) { // Byte code:
    //   0: new java/util/ArrayList
    //   3: dup
    //   4: invokespecial <init> : ()V
    //   7: astore #14
    //   9: new android/net/Uri$Builder
    //   12: dup
    //   13: invokespecial <init> : ()V
    //   16: ldc 'content'
    //   18: invokevirtual scheme : (Ljava/lang/String;)Landroid/net/Uri$Builder;
    //   21: aload_2
    //   22: invokevirtual authority : (Ljava/lang/String;)Landroid/net/Uri$Builder;
    //   25: invokevirtual build : ()Landroid/net/Uri;
    //   28: astore #16
    //   30: new android/net/Uri$Builder
    //   33: dup
    //   34: invokespecial <init> : ()V
    //   37: ldc 'content'
    //   39: invokevirtual scheme : (Ljava/lang/String;)Landroid/net/Uri$Builder;
    //   42: aload_2
    //   43: invokevirtual authority : (Ljava/lang/String;)Landroid/net/Uri$Builder;
    //   46: ldc 'file'
    //   48: invokevirtual appendPath : (Ljava/lang/String;)Landroid/net/Uri$Builder;
    //   51: invokevirtual build : ()Landroid/net/Uri;
    //   54: astore #17
    //   56: aconst_null
    //   57: astore #15
    //   59: aload #15
    //   61: astore_2
    //   62: getstatic android/os/Build$VERSION.SDK_INT : I
    //   65: bipush #16
    //   67: if_icmple -> 157
    //   70: aload #15
    //   72: astore_2
    //   73: aload_0
    //   74: invokevirtual getContentResolver : ()Landroid/content/ContentResolver;
    //   77: astore_0
    //   78: aload #15
    //   80: astore_2
    //   81: aload_1
    //   82: invokevirtual getQuery : ()Ljava/lang/String;
    //   85: astore_1
    //   86: aload #15
    //   88: astore_2
    //   89: aload_0
    //   90: aload #16
    //   92: bipush #7
    //   94: anewarray java/lang/String
    //   97: dup
    //   98: iconst_0
    //   99: ldc '_id'
    //   101: aastore
    //   102: dup
    //   103: iconst_1
    //   104: ldc 'file_id'
    //   106: aastore
    //   107: dup
    //   108: iconst_2
    //   109: ldc 'font_ttc_index'
    //   111: aastore
    //   112: dup
    //   113: iconst_3
    //   114: ldc_w 'font_variation_settings'
    //   117: aastore
    //   118: dup
    //   119: iconst_4
    //   120: ldc_w 'font_weight'
    //   123: aastore
    //   124: dup
    //   125: iconst_5
    //   126: ldc_w 'font_italic'
    //   129: aastore
    //   130: dup
    //   131: bipush #6
    //   133: ldc_w 'result_code'
    //   136: aastore
    //   137: ldc_w 'query = ?'
    //   140: iconst_1
    //   141: anewarray java/lang/String
    //   144: dup
    //   145: iconst_0
    //   146: aload_1
    //   147: aastore
    //   148: aconst_null
    //   149: aload_3
    //   150: invokevirtual query : (Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Landroid/os/CancellationSignal;)Landroid/database/Cursor;
    //   153: astore_0
    //   154: goto -> 240
    //   157: aload #15
    //   159: astore_2
    //   160: aload_0
    //   161: invokevirtual getContentResolver : ()Landroid/content/ContentResolver;
    //   164: astore_0
    //   165: aload #15
    //   167: astore_2
    //   168: aload_1
    //   169: invokevirtual getQuery : ()Ljava/lang/String;
    //   172: astore_1
    //   173: aload #15
    //   175: astore_2
    //   176: aload_0
    //   177: aload #16
    //   179: bipush #7
    //   181: anewarray java/lang/String
    //   184: dup
    //   185: iconst_0
    //   186: ldc '_id'
    //   188: aastore
    //   189: dup
    //   190: iconst_1
    //   191: ldc 'file_id'
    //   193: aastore
    //   194: dup
    //   195: iconst_2
    //   196: ldc 'font_ttc_index'
    //   198: aastore
    //   199: dup
    //   200: iconst_3
    //   201: ldc_w 'font_variation_settings'
    //   204: aastore
    //   205: dup
    //   206: iconst_4
    //   207: ldc_w 'font_weight'
    //   210: aastore
    //   211: dup
    //   212: iconst_5
    //   213: ldc_w 'font_italic'
    //   216: aastore
    //   217: dup
    //   218: bipush #6
    //   220: ldc_w 'result_code'
    //   223: aastore
    //   224: ldc_w 'query = ?'
    //   227: iconst_1
    //   228: anewarray java/lang/String
    //   231: dup
    //   232: iconst_0
    //   233: aload_1
    //   234: aastore
    //   235: aconst_null
    //   236: invokevirtual query : (Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   239: astore_0
    //   240: aload #14
    //   242: astore_1
    //   243: aload_0
    //   244: ifnull -> 515
    //   247: aload #14
    //   249: astore_1
    //   250: aload_0
    //   251: astore_2
    //   252: aload_0
    //   253: invokeinterface getCount : ()I
    //   258: ifle -> 515
    //   261: aload_0
    //   262: astore_2
    //   263: aload_0
    //   264: ldc_w 'result_code'
    //   267: invokeinterface getColumnIndex : (Ljava/lang/String;)I
    //   272: istore #7
    //   274: aload_0
    //   275: astore_2
    //   276: new java/util/ArrayList
    //   279: dup
    //   280: invokespecial <init> : ()V
    //   283: astore_3
    //   284: aload_0
    //   285: astore_2
    //   286: aload_0
    //   287: ldc '_id'
    //   289: invokeinterface getColumnIndex : (Ljava/lang/String;)I
    //   294: istore #8
    //   296: aload_0
    //   297: astore_2
    //   298: aload_0
    //   299: ldc 'file_id'
    //   301: invokeinterface getColumnIndex : (Ljava/lang/String;)I
    //   306: istore #9
    //   308: aload_0
    //   309: astore_2
    //   310: aload_0
    //   311: ldc 'font_ttc_index'
    //   313: invokeinterface getColumnIndex : (Ljava/lang/String;)I
    //   318: istore #10
    //   320: aload_0
    //   321: astore_2
    //   322: aload_0
    //   323: ldc_w 'font_weight'
    //   326: invokeinterface getColumnIndex : (Ljava/lang/String;)I
    //   331: istore #11
    //   333: aload_0
    //   334: astore_2
    //   335: aload_0
    //   336: ldc_w 'font_italic'
    //   339: invokeinterface getColumnIndex : (Ljava/lang/String;)I
    //   344: istore #12
    //   346: aload_3
    //   347: astore_1
    //   348: aload_0
    //   349: astore_2
    //   350: aload_0
    //   351: invokeinterface moveToNext : ()Z
    //   356: ifeq -> 515
    //   359: iload #7
    //   361: iconst_m1
    //   362: if_icmpeq -> 550
    //   365: aload_0
    //   366: astore_2
    //   367: aload_0
    //   368: iload #7
    //   370: invokeinterface getInt : (I)I
    //   375: istore #4
    //   377: goto -> 380
    //   380: iload #10
    //   382: iconst_m1
    //   383: if_icmpeq -> 556
    //   386: aload_0
    //   387: astore_2
    //   388: aload_0
    //   389: iload #10
    //   391: invokeinterface getInt : (I)I
    //   396: istore #5
    //   398: goto -> 401
    //   401: iload #9
    //   403: iconst_m1
    //   404: if_icmpne -> 426
    //   407: aload_0
    //   408: astore_2
    //   409: aload #16
    //   411: aload_0
    //   412: iload #8
    //   414: invokeinterface getLong : (I)J
    //   419: invokestatic withAppendedId : (Landroid/net/Uri;J)Landroid/net/Uri;
    //   422: astore_1
    //   423: goto -> 442
    //   426: aload_0
    //   427: astore_2
    //   428: aload #17
    //   430: aload_0
    //   431: iload #9
    //   433: invokeinterface getLong : (I)J
    //   438: invokestatic withAppendedId : (Landroid/net/Uri;J)Landroid/net/Uri;
    //   441: astore_1
    //   442: iload #11
    //   444: iconst_m1
    //   445: if_icmpeq -> 562
    //   448: aload_0
    //   449: astore_2
    //   450: aload_0
    //   451: iload #11
    //   453: invokeinterface getInt : (I)I
    //   458: istore #6
    //   460: goto -> 463
    //   463: iload #12
    //   465: iconst_m1
    //   466: if_icmpeq -> 570
    //   469: aload_0
    //   470: astore_2
    //   471: aload_0
    //   472: iload #12
    //   474: invokeinterface getInt : (I)I
    //   479: iconst_1
    //   480: if_icmpne -> 570
    //   483: iconst_1
    //   484: istore #13
    //   486: goto -> 489
    //   489: aload_0
    //   490: astore_2
    //   491: aload_3
    //   492: new android/support/v4/provider/FontsContractCompat$FontInfo
    //   495: dup
    //   496: aload_1
    //   497: iload #5
    //   499: iload #6
    //   501: iload #13
    //   503: iload #4
    //   505: invokespecial <init> : (Landroid/net/Uri;IIZI)V
    //   508: invokevirtual add : (Ljava/lang/Object;)Z
    //   511: pop
    //   512: goto -> 346
    //   515: aload_0
    //   516: ifnull -> 525
    //   519: aload_0
    //   520: invokeinterface close : ()V
    //   525: aload_1
    //   526: iconst_0
    //   527: anewarray android/support/v4/provider/FontsContractCompat$FontInfo
    //   530: invokevirtual toArray : ([Ljava/lang/Object;)[Ljava/lang/Object;
    //   533: checkcast [Landroid/support/v4/provider/FontsContractCompat$FontInfo;
    //   536: areturn
    //   537: astore_0
    //   538: aload_2
    //   539: ifnull -> 548
    //   542: aload_2
    //   543: invokeinterface close : ()V
    //   548: aload_0
    //   549: athrow
    //   550: iconst_0
    //   551: istore #4
    //   553: goto -> 380
    //   556: iconst_0
    //   557: istore #5
    //   559: goto -> 401
    //   562: sipush #400
    //   565: istore #6
    //   567: goto -> 463
    //   570: iconst_0
    //   571: istore #13
    //   573: goto -> 489
    // Exception table:
    //   from	to	target	type
    //   62	70	537	finally
    //   73	78	537	finally
    //   81	86	537	finally
    //   89	154	537	finally
    //   160	165	537	finally
    //   168	173	537	finally
    //   176	240	537	finally
    //   252	261	537	finally
    //   263	274	537	finally
    //   276	284	537	finally
    //   286	296	537	finally
    //   298	308	537	finally
    //   310	320	537	finally
    //   322	333	537	finally
    //   335	346	537	finally
    //   350	359	537	finally
    //   367	377	537	finally
    //   388	398	537	finally
    //   409	423	537	finally
    //   428	442	537	finally
    //   450	460	537	finally
    //   471	483	537	finally
    //   491	512	537	finally }
  
  @NonNull
  static TypefaceResult getFontInternal(Context paramContext, FontRequest paramFontRequest, int paramInt) {
    try {
      FontFamilyResult fontFamilyResult = fetchFonts(paramContext, null, paramFontRequest);
      int i = fontFamilyResult.getStatusCode();
      byte b = -3;
      if (i == 0) {
        Typeface typeface = TypefaceCompat.createFromFontInfo(paramContext, null, fontFamilyResult.getFonts(), paramInt);
        if (typeface != null)
          b = 0; 
        return new TypefaceResult(typeface, b);
      } 
      if (fontFamilyResult.getStatusCode() == 1)
        b = -2; 
      return new TypefaceResult(null, b);
    } catch (android.content.pm.PackageManager.NameNotFoundException paramContext) {
      return new TypefaceResult(null, -1);
    } 
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static Typeface getFontSync(Context paramContext, final FontRequest request, @Nullable final ResourcesCompat.FontCallback fontCallback, @Nullable final Handler handler, boolean paramBoolean, int paramInt1, final int style) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramFontRequest.getIdentifier());
    stringBuilder.append("-");
    stringBuilder.append(paramInt2);
    final String id = stringBuilder.toString();
    Typeface typeface = (Typeface)sTypefaceCache.get(str);
    if (typeface != null) {
      if (paramFontCallback != null)
        paramFontCallback.onFontRetrieved(typeface); 
      return typeface;
    } 
    if (paramBoolean && paramInt1 == -1) {
      null = getFontInternal(paramContext, paramFontRequest, paramInt2);
      if (paramFontCallback != null)
        if (null.mResult == 0) {
          paramFontCallback.callbackSuccessAsync(null.mTypeface, paramHandler);
        } else {
          paramFontCallback.callbackFailAsync(null.mResult, paramHandler);
        }  
      return null.mTypeface;
    } 
    Callable<TypefaceResult> callable = new Callable<TypefaceResult>() {
        public FontsContractCompat.TypefaceResult call() throws Exception {
          FontsContractCompat.TypefaceResult typefaceResult = FontsContractCompat.getFontInternal(context, request, style);
          if (typefaceResult.mTypeface != null)
            FontsContractCompat.sTypefaceCache.put(id, typefaceResult.mTypeface); 
          return typefaceResult;
        }
      };
    if (paramBoolean)
      try {
        return ((TypefaceResult)sBackgroundThread.postAndWait(callable, paramInt1)).mTypeface;
      } catch (InterruptedException null) {
        return null;
      }  
    if (paramFontCallback == null) {
      null = null;
    } else {
      null = new SelfDestructiveThread.ReplyCallback<TypefaceResult>() {
          public void onReply(FontsContractCompat.TypefaceResult param1TypefaceResult) {
            if (param1TypefaceResult == null) {
              fontCallback.callbackFailAsync(1, handler);
              return;
            } 
            if (param1TypefaceResult.mResult == 0) {
              fontCallback.callbackSuccessAsync(param1TypefaceResult.mTypeface, handler);
              return;
            } 
            fontCallback.callbackFailAsync(param1TypefaceResult.mResult, handler);
          }
        };
    } 
    synchronized (sLock) {
      if (sPendingReplies.containsKey(str)) {
        if (null != null)
          ((ArrayList)sPendingReplies.get(str)).add(null); 
        return null;
      } 
      if (null != null) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(null);
        sPendingReplies.put(str, arrayList);
      } 
      sBackgroundThread.postAndReply(callable, new SelfDestructiveThread.ReplyCallback<TypefaceResult>() {
            public void onReply(FontsContractCompat.TypefaceResult param1TypefaceResult) {
              synchronized (FontsContractCompat.sLock) {
                ArrayList arrayList = (ArrayList)FontsContractCompat.sPendingReplies.get(id);
                if (arrayList == null)
                  return; 
                FontsContractCompat.sPendingReplies.remove(id);
                for (byte b = 0; b < arrayList.size(); b++)
                  ((ReplyCallback)arrayList.get(b)).onReply(param1TypefaceResult); 
                return;
              } 
            }
          });
      return null;
    } 
  }
  
  @Nullable
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  @VisibleForTesting
  public static ProviderInfo getProvider(@NonNull PackageManager paramPackageManager, @NonNull FontRequest paramFontRequest, @Nullable Resources paramResources) throws PackageManager.NameNotFoundException {
    String str = paramFontRequest.getProviderAuthority();
    ProviderInfo providerInfo = paramPackageManager.resolveContentProvider(str, 0);
    if (providerInfo != null) {
      List list;
      if (providerInfo.packageName.equals(paramFontRequest.getProviderPackage())) {
        List list1 = convertToByteArrayList((paramPackageManager.getPackageInfo(providerInfo.packageName, 64)).signatures);
        Collections.sort(list1, sByteArrayComparator);
        list = getCertificates(paramFontRequest, paramResources);
        for (byte b = 0; b < list.size(); b++) {
          ArrayList arrayList = new ArrayList((Collection)list.get(b));
          Collections.sort(arrayList, sByteArrayComparator);
          if (equalsByteArrayList(list1, arrayList))
            return providerInfo; 
        } 
        return null;
      } 
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("Found content provider ");
      stringBuilder1.append(str);
      stringBuilder1.append(", but package was not ");
      stringBuilder1.append(list.getProviderPackage());
      throw new PackageManager.NameNotFoundException(stringBuilder1.toString());
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("No package found for authority: ");
    stringBuilder.append(str);
    throw new PackageManager.NameNotFoundException(stringBuilder.toString());
  }
  
  @RequiresApi(19)
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static Map<Uri, ByteBuffer> prepareFontData(Context paramContext, FontInfo[] paramArrayOfFontInfo, CancellationSignal paramCancellationSignal) {
    HashMap hashMap = new HashMap();
    int i = paramArrayOfFontInfo.length;
    for (byte b = 0; b < i; b++) {
      FontInfo fontInfo = paramArrayOfFontInfo[b];
      if (fontInfo.getResultCode() == 0) {
        Uri uri = fontInfo.getUri();
        if (!hashMap.containsKey(uri))
          hashMap.put(uri, TypefaceCompatUtil.mmap(paramContext, paramCancellationSignal, uri)); 
      } 
    } 
    return Collections.unmodifiableMap(hashMap);
  }
  
  public static void requestFont(@NonNull final Context context, @NonNull final FontRequest request, @NonNull final FontRequestCallback callback, @NonNull Handler paramHandler) { paramHandler.post(new Runnable() {
          public void run() {
            try {
              FontsContractCompat.FontFamilyResult fontFamilyResult = FontsContractCompat.fetchFonts(context, null, request);
              if (fontFamilyResult.getStatusCode() != 0) {
                switch (fontFamilyResult.getStatusCode()) {
                  default:
                    callerThreadHandler.post(new Runnable() {
                          public void run() { callback.onTypefaceRequestFailed(-3); }
                        });
                    return;
                  case 2:
                    callerThreadHandler.post(new Runnable() {
                          public void run() { callback.onTypefaceRequestFailed(-3); }
                        });
                    return;
                  case 1:
                    break;
                } 
                callerThreadHandler.post(new Runnable() {
                      public void run() { callback.onTypefaceRequestFailed(-2); }
                    });
                return;
              } 
              FontsContractCompat.FontInfo[] arrayOfFontInfo = fontFamilyResult.getFonts();
              if (arrayOfFontInfo == null || arrayOfFontInfo.length == 0) {
                callerThreadHandler.post(new Runnable() {
                      public void run() { callback.onTypefaceRequestFailed(1); }
                    });
                return;
              } 
              int j = arrayOfFontInfo.length;
              for (final int resultCode = 0; i < j; i++) {
                FontsContractCompat.FontInfo fontInfo = arrayOfFontInfo[i];
                if (fontInfo.getResultCode() != 0) {
                  i = fontInfo.getResultCode();
                  if (i < 0) {
                    callerThreadHandler.post(new Runnable() {
                          public void run() { callback.onTypefaceRequestFailed(-3); }
                        });
                    return;
                  } 
                  callerThreadHandler.post(new Runnable() {
                        public void run() { callback.onTypefaceRequestFailed(resultCode); }
                      });
                  return;
                } 
              } 
              final Typeface typeface = FontsContractCompat.buildTypeface(context, null, arrayOfFontInfo);
              if (typeface == null) {
                callerThreadHandler.post(new Runnable() {
                      public void run() { callback.onTypefaceRequestFailed(-3); }
                    });
                return;
              } 
              callerThreadHandler.post(new Runnable() {
                    public void run() { callback.onTypefaceRetrieved(typeface); }
                  });
              return;
            } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {
              callerThreadHandler.post(new Runnable() {
                    public void run() { callback.onTypefaceRequestFailed(-1); }
                  });
              return;
            } 
          }
        }); }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static void resetCache() { sTypefaceCache.evictAll(); }
  
  public static final class Columns implements BaseColumns {
    public static final String FILE_ID = "file_id";
    
    public static final String ITALIC = "font_italic";
    
    public static final String RESULT_CODE = "result_code";
    
    public static final int RESULT_CODE_FONT_NOT_FOUND = 1;
    
    public static final int RESULT_CODE_FONT_UNAVAILABLE = 2;
    
    public static final int RESULT_CODE_MALFORMED_QUERY = 3;
    
    public static final int RESULT_CODE_OK = 0;
    
    public static final String TTC_INDEX = "font_ttc_index";
    
    public static final String VARIATION_SETTINGS = "font_variation_settings";
    
    public static final String WEIGHT = "font_weight";
  }
  
  public static class FontFamilyResult {
    public static final int STATUS_OK = 0;
    
    public static final int STATUS_UNEXPECTED_DATA_PROVIDED = 2;
    
    public static final int STATUS_WRONG_CERTIFICATES = 1;
    
    private final FontsContractCompat.FontInfo[] mFonts;
    
    private final int mStatusCode;
    
    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public FontFamilyResult(int param1Int, @Nullable FontsContractCompat.FontInfo[] param1ArrayOfFontInfo) {
      this.mStatusCode = param1Int;
      this.mFonts = param1ArrayOfFontInfo;
    }
    
    public FontsContractCompat.FontInfo[] getFonts() { return this.mFonts; }
    
    public int getStatusCode() { return this.mStatusCode; }
  }
  
  public static class FontInfo {
    private final boolean mItalic;
    
    private final int mResultCode;
    
    private final int mTtcIndex;
    
    private final Uri mUri;
    
    private final int mWeight;
    
    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public FontInfo(@NonNull Uri param1Uri, @IntRange(from = 0L) int param1Int1, @IntRange(from = 1L, to = 1000L) int param1Int2, boolean param1Boolean, int param1Int3) {
      this.mUri = (Uri)Preconditions.checkNotNull(param1Uri);
      this.mTtcIndex = param1Int1;
      this.mWeight = param1Int2;
      this.mItalic = param1Boolean;
      this.mResultCode = param1Int3;
    }
    
    public int getResultCode() { return this.mResultCode; }
    
    @IntRange(from = 0L)
    public int getTtcIndex() { return this.mTtcIndex; }
    
    @NonNull
    public Uri getUri() { return this.mUri; }
    
    @IntRange(from = 1L, to = 1000L)
    public int getWeight() { return this.mWeight; }
    
    public boolean isItalic() { return this.mItalic; }
  }
  
  public static class FontRequestCallback {
    public static final int FAIL_REASON_FONT_LOAD_ERROR = -3;
    
    public static final int FAIL_REASON_FONT_NOT_FOUND = 1;
    
    public static final int FAIL_REASON_FONT_UNAVAILABLE = 2;
    
    public static final int FAIL_REASON_MALFORMED_QUERY = 3;
    
    public static final int FAIL_REASON_PROVIDER_NOT_FOUND = -1;
    
    public static final int FAIL_REASON_SECURITY_VIOLATION = -4;
    
    public static final int FAIL_REASON_WRONG_CERTIFICATES = -2;
    
    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public static final int RESULT_OK = 0;
    
    public void onTypefaceRequestFailed(int param1Int) {}
    
    public void onTypefaceRetrieved(Typeface param1Typeface) {}
    
    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public static @interface FontRequestFailReason {}
  }
  
  @Retention(RetentionPolicy.SOURCE)
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static @interface FontRequestFailReason {}
  
  private static final class TypefaceResult {
    final int mResult;
    
    final Typeface mTypeface;
    
    TypefaceResult(@Nullable Typeface param1Typeface, int param1Int) {
      this.mTypeface = param1Typeface;
      this.mResult = param1Int;
    }
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v4\provider\FontsContractCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */