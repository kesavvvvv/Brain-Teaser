package android.support.v4.content;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ProviderInfo;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.support.annotation.GuardedBy;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import org.xmlpull.v1.XmlPullParserException;

public class FileProvider extends ContentProvider {
  private static final String ATTR_NAME = "name";
  
  private static final String ATTR_PATH = "path";
  
  private static final String[] COLUMNS = { "_display_name", "_size" };
  
  private static final File DEVICE_ROOT = new File("/");
  
  private static final String META_DATA_FILE_PROVIDER_PATHS = "android.support.FILE_PROVIDER_PATHS";
  
  private static final String TAG_CACHE_PATH = "cache-path";
  
  private static final String TAG_EXTERNAL = "external-path";
  
  private static final String TAG_EXTERNAL_CACHE = "external-cache-path";
  
  private static final String TAG_EXTERNAL_FILES = "external-files-path";
  
  private static final String TAG_EXTERNAL_MEDIA = "external-media-path";
  
  private static final String TAG_FILES_PATH = "files-path";
  
  private static final String TAG_ROOT_PATH = "root-path";
  
  @GuardedBy("sCache")
  private static HashMap<String, PathStrategy> sCache = new HashMap();
  
  private PathStrategy mStrategy;
  
  private static File buildPath(File paramFile, String... paramVarArgs) {
    int i = paramVarArgs.length;
    byte b = 0;
    while (b < i) {
      String str = paramVarArgs[b];
      File file = paramFile;
      if (str != null)
        file = new File(paramFile, str); 
      b++;
      paramFile = file;
    } 
    return paramFile;
  }
  
  private static Object[] copyOf(Object[] paramArrayOfObject, int paramInt) {
    Object[] arrayOfObject = new Object[paramInt];
    System.arraycopy(paramArrayOfObject, 0, arrayOfObject, 0, paramInt);
    return arrayOfObject;
  }
  
  private static String[] copyOf(String[] paramArrayOfString, int paramInt) {
    String[] arrayOfString = new String[paramInt];
    System.arraycopy(paramArrayOfString, 0, arrayOfString, 0, paramInt);
    return arrayOfString;
  }
  
  private static PathStrategy getPathStrategy(Context paramContext, String paramString) {
    synchronized (sCache) {
      PathStrategy pathStrategy1 = (PathStrategy)sCache.get(paramString);
      PathStrategy pathStrategy2 = pathStrategy1;
      if (pathStrategy1 == null)
        try {
          pathStrategy2 = parsePathStrategy(paramContext, paramString);
          sCache.put(paramString, pathStrategy2);
        } catch (IOException paramContext) {
          throw new IllegalArgumentException("Failed to parse android.support.FILE_PROVIDER_PATHS meta-data", paramContext);
        } catch (XmlPullParserException paramContext) {
          throw new IllegalArgumentException("Failed to parse android.support.FILE_PROVIDER_PATHS meta-data", paramContext);
        }  
      return pathStrategy2;
    } 
  }
  
  public static Uri getUriForFile(@NonNull Context paramContext, @NonNull String paramString, @NonNull File paramFile) { return getPathStrategy(paramContext, paramString).getUriForFile(paramFile); }
  
  private static int modeToMode(String paramString) {
    if ("r".equals(paramString))
      return 268435456; 
    if ("w".equals(paramString) || "wt".equals(paramString))
      return 738197504; 
    if ("wa".equals(paramString))
      return 704643072; 
    if ("rw".equals(paramString))
      return 939524096; 
    if ("rwt".equals(paramString))
      return 1006632960; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Invalid mode: ");
    stringBuilder.append(paramString);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  private static PathStrategy parsePathStrategy(Context paramContext, String paramString) {
    SimplePathStrategy simplePathStrategy = new SimplePathStrategy(paramString);
    XmlResourceParser xmlResourceParser = paramContext.getPackageManager().resolveContentProvider(paramString, 128).loadXmlMetaData(paramContext.getPackageManager(), "android.support.FILE_PROVIDER_PATHS");
    if (xmlResourceParser != null) {
      while (true) {
        int i = xmlResourceParser.next();
        if (i != 1) {
          if (i == 2) {
            File[] arrayOfFile1;
            String str4 = xmlResourceParser.getName();
            String str2 = xmlResourceParser.getAttributeValue(null, "name");
            String str3 = xmlResourceParser.getAttributeValue(null, "path");
            String str1 = null;
            File[] arrayOfFile2 = null;
            paramString = null;
            if ("root-path".equals(str4)) {
              arrayOfFile1 = DEVICE_ROOT;
            } else if ("files-path".equals(str4)) {
              arrayOfFile1 = paramContext.getFilesDir();
            } else if ("cache-path".equals(str4)) {
              arrayOfFile1 = paramContext.getCacheDir();
            } else if ("external-path".equals(str4)) {
              arrayOfFile1 = Environment.getExternalStorageDirectory();
            } else if ("external-files-path".equals(str4)) {
              arrayOfFile2 = ContextCompat.getExternalFilesDirs(paramContext, null);
              if (arrayOfFile2.length > 0)
                arrayOfFile1 = arrayOfFile2[0]; 
            } else if ("external-cache-path".equals(str4)) {
              arrayOfFile2 = ContextCompat.getExternalCacheDirs(paramContext);
              paramString = str1;
              if (arrayOfFile2.length > 0)
                arrayOfFile1 = arrayOfFile2[0]; 
            } else {
              paramString = str1;
              if (Build.VERSION.SDK_INT >= 21) {
                arrayOfFile1 = arrayOfFile2;
                if ("external-media-path".equals(str4)) {
                  File[] arrayOfFile = paramContext.getExternalMediaDirs();
                  arrayOfFile1 = arrayOfFile2;
                  if (arrayOfFile.length > 0)
                    arrayOfFile1 = arrayOfFile[0]; 
                } 
              } 
            } 
            if (arrayOfFile1 != null)
              simplePathStrategy.addRoot(str2, buildPath(arrayOfFile1, new String[] { str3 })); 
          } 
          continue;
        } 
        break;
      } 
      return simplePathStrategy;
    } 
    throw new IllegalArgumentException("Missing android.support.FILE_PROVIDER_PATHS meta-data");
  }
  
  public void attachInfo(@NonNull Context paramContext, @NonNull ProviderInfo paramProviderInfo) {
    super.attachInfo(paramContext, paramProviderInfo);
    if (!paramProviderInfo.exported) {
      if (paramProviderInfo.grantUriPermissions) {
        this.mStrategy = getPathStrategy(paramContext, paramProviderInfo.authority);
        return;
      } 
      throw new SecurityException("Provider must grant uri permissions");
    } 
    throw new SecurityException("Provider must not be exported");
  }
  
  public int delete(@NonNull Uri paramUri, @Nullable String paramString, @Nullable String[] paramArrayOfString) { throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\r\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:659)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\r\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\r\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\r\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\r\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\r\n"); }
  
  public String getType(@NonNull Uri paramUri) {
    File file = this.mStrategy.getFileForUri(paramUri);
    int i = file.getName().lastIndexOf('.');
    if (i >= 0) {
      String str = file.getName().substring(i + 1);
      str = MimeTypeMap.getSingleton().getMimeTypeFromExtension(str);
      if (str != null)
        return str; 
    } 
    return "application/octet-stream";
  }
  
  public Uri insert(@NonNull Uri paramUri, ContentValues paramContentValues) { throw new UnsupportedOperationException("No external inserts"); }
  
  public boolean onCreate() { return true; }
  
  public ParcelFileDescriptor openFile(@NonNull Uri paramUri, @NonNull String paramString) throws FileNotFoundException { return ParcelFileDescriptor.open(this.mStrategy.getFileForUri(paramUri), modeToMode(paramString)); }
  
  public Cursor query(@NonNull Uri paramUri, @Nullable String[] paramArrayOfString1, @Nullable String paramString1, @Nullable String[] paramArrayOfString2, @Nullable String paramString2) {
    File file = this.mStrategy.getFileForUri(paramUri);
    String[] arrayOfString = paramArrayOfString1;
    if (paramArrayOfString1 == null)
      arrayOfString = COLUMNS; 
    paramArrayOfString2 = new String[arrayOfString.length];
    Object[] arrayOfObject = new Object[arrayOfString.length];
    byte b2 = 0;
    int i = arrayOfString.length;
    byte b1 = 0;
    while (b1 < i) {
      byte b;
      paramString2 = arrayOfString[b1];
      if ("_display_name".equals(paramString2)) {
        paramArrayOfString2[b2] = "_display_name";
        arrayOfObject[b2] = file.getName();
        b = b2 + true;
      } else {
        b = b2;
        if ("_size".equals(paramString2)) {
          paramArrayOfString2[b2] = "_size";
          arrayOfObject[b2] = Long.valueOf(file.length());
          b = b2 + true;
        } 
      } 
      b1++;
      b2 = b;
    } 
    arrayOfString = copyOf(paramArrayOfString2, b2);
    arrayOfObject = copyOf(arrayOfObject, b2);
    MatrixCursor matrixCursor = new MatrixCursor(arrayOfString, 1);
    matrixCursor.addRow(arrayOfObject);
    return matrixCursor;
  }
  
  public int update(@NonNull Uri paramUri, ContentValues paramContentValues, @Nullable String paramString, @Nullable String[] paramArrayOfString) { throw new UnsupportedOperationException("No external updates"); }
  
  static interface PathStrategy {
    File getFileForUri(Uri param1Uri);
    
    Uri getUriForFile(File param1File);
  }
  
  static class SimplePathStrategy implements PathStrategy {
    private final String mAuthority;
    
    private final HashMap<String, File> mRoots = new HashMap();
    
    SimplePathStrategy(String param1String) { this.mAuthority = param1String; }
    
    void addRoot(String param1String, File param1File) {
      if (!TextUtils.isEmpty(param1String))
        try {
          File file = param1File.getCanonicalFile();
          this.mRoots.put(param1String, file);
          return;
        } catch (IOException param1String) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("Failed to resolve canonical path for ");
          stringBuilder.append(param1File);
          throw new IllegalArgumentException(stringBuilder.toString(), param1String);
        }  
      throw new IllegalArgumentException("Name must not be empty");
    }
    
    public File getFileForUri(Uri param1Uri) {
      File file1;
      String str2 = param1Uri.getEncodedPath();
      int i = str2.indexOf('/', 1);
      String str1 = Uri.decode(str2.substring(1, i));
      str2 = Uri.decode(str2.substring(i + 1));
      file2 = (File)this.mRoots.get(str1);
      if (file2 != null) {
        file1 = new File(file2, str2);
        try {
          File file = file1.getCanonicalFile();
          if (file.getPath().startsWith(file2.getPath()))
            return file; 
          throw new SecurityException("Resolved path jumped beyond configured root");
        } catch (IOException file2) {
          StringBuilder stringBuilder1 = new StringBuilder();
          stringBuilder1.append("Failed to resolve canonical path for ");
          stringBuilder1.append(file1);
          throw new IllegalArgumentException(stringBuilder1.toString());
        } 
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Unable to find configured root for ");
      stringBuilder.append(file1);
      throw new IllegalArgumentException(stringBuilder.toString());
    }
    
    public Uri getUriForFile(File param1File) { // Byte code:
      //   0: aload_1
      //   1: invokevirtual getCanonicalPath : ()Ljava/lang/String;
      //   4: astore #4
      //   6: aconst_null
      //   7: astore_1
      //   8: aload_0
      //   9: getfield mRoots : Ljava/util/HashMap;
      //   12: invokevirtual entrySet : ()Ljava/util/Set;
      //   15: invokeinterface iterator : ()Ljava/util/Iterator;
      //   20: astore #5
      //   22: aload #5
      //   24: invokeinterface hasNext : ()Z
      //   29: ifeq -> 105
      //   32: aload #5
      //   34: invokeinterface next : ()Ljava/lang/Object;
      //   39: checkcast java/util/Map$Entry
      //   42: astore_3
      //   43: aload_3
      //   44: invokeinterface getValue : ()Ljava/lang/Object;
      //   49: checkcast java/io/File
      //   52: invokevirtual getPath : ()Ljava/lang/String;
      //   55: astore #6
      //   57: aload_1
      //   58: astore_2
      //   59: aload #4
      //   61: aload #6
      //   63: invokevirtual startsWith : (Ljava/lang/String;)Z
      //   66: ifeq -> 100
      //   69: aload_1
      //   70: ifnull -> 98
      //   73: aload_1
      //   74: astore_2
      //   75: aload #6
      //   77: invokevirtual length : ()I
      //   80: aload_1
      //   81: invokeinterface getValue : ()Ljava/lang/Object;
      //   86: checkcast java/io/File
      //   89: invokevirtual getPath : ()Ljava/lang/String;
      //   92: invokevirtual length : ()I
      //   95: if_icmple -> 100
      //   98: aload_3
      //   99: astore_2
      //   100: aload_2
      //   101: astore_1
      //   102: goto -> 22
      //   105: aload_1
      //   106: ifnull -> 231
      //   109: aload_1
      //   110: invokeinterface getValue : ()Ljava/lang/Object;
      //   115: checkcast java/io/File
      //   118: invokevirtual getPath : ()Ljava/lang/String;
      //   121: astore_2
      //   122: aload_2
      //   123: ldc '/'
      //   125: invokevirtual endsWith : (Ljava/lang/String;)Z
      //   128: ifeq -> 144
      //   131: aload #4
      //   133: aload_2
      //   134: invokevirtual length : ()I
      //   137: invokevirtual substring : (I)Ljava/lang/String;
      //   140: astore_2
      //   141: goto -> 156
      //   144: aload #4
      //   146: aload_2
      //   147: invokevirtual length : ()I
      //   150: iconst_1
      //   151: iadd
      //   152: invokevirtual substring : (I)Ljava/lang/String;
      //   155: astore_2
      //   156: new java/lang/StringBuilder
      //   159: dup
      //   160: invokespecial <init> : ()V
      //   163: astore_3
      //   164: aload_3
      //   165: aload_1
      //   166: invokeinterface getKey : ()Ljava/lang/Object;
      //   171: checkcast java/lang/String
      //   174: invokestatic encode : (Ljava/lang/String;)Ljava/lang/String;
      //   177: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   180: pop
      //   181: aload_3
      //   182: bipush #47
      //   184: invokevirtual append : (C)Ljava/lang/StringBuilder;
      //   187: pop
      //   188: aload_3
      //   189: aload_2
      //   190: ldc '/'
      //   192: invokestatic encode : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
      //   195: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   198: pop
      //   199: aload_3
      //   200: invokevirtual toString : ()Ljava/lang/String;
      //   203: astore_1
      //   204: new android/net/Uri$Builder
      //   207: dup
      //   208: invokespecial <init> : ()V
      //   211: ldc 'content'
      //   213: invokevirtual scheme : (Ljava/lang/String;)Landroid/net/Uri$Builder;
      //   216: aload_0
      //   217: getfield mAuthority : Ljava/lang/String;
      //   220: invokevirtual authority : (Ljava/lang/String;)Landroid/net/Uri$Builder;
      //   223: aload_1
      //   224: invokevirtual encodedPath : (Ljava/lang/String;)Landroid/net/Uri$Builder;
      //   227: invokevirtual build : ()Landroid/net/Uri;
      //   230: areturn
      //   231: new java/lang/StringBuilder
      //   234: dup
      //   235: invokespecial <init> : ()V
      //   238: astore_1
      //   239: aload_1
      //   240: ldc 'Failed to find configured root that contains '
      //   242: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   245: pop
      //   246: aload_1
      //   247: aload #4
      //   249: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   252: pop
      //   253: new java/lang/IllegalArgumentException
      //   256: dup
      //   257: aload_1
      //   258: invokevirtual toString : ()Ljava/lang/String;
      //   261: invokespecial <init> : (Ljava/lang/String;)V
      //   264: athrow
      //   265: astore_2
      //   266: new java/lang/StringBuilder
      //   269: dup
      //   270: invokespecial <init> : ()V
      //   273: astore_2
      //   274: aload_2
      //   275: ldc 'Failed to resolve canonical path for '
      //   277: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   280: pop
      //   281: aload_2
      //   282: aload_1
      //   283: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
      //   286: pop
      //   287: new java/lang/IllegalArgumentException
      //   290: dup
      //   291: aload_2
      //   292: invokevirtual toString : ()Ljava/lang/String;
      //   295: invokespecial <init> : (Ljava/lang/String;)V
      //   298: athrow
      // Exception table:
      //   from	to	target	type
      //   0	6	265	java/io/IOException }
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v4\content\FileProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */