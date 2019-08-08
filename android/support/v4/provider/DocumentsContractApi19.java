package android.support.v4.provider;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;

@RequiresApi(19)
class DocumentsContractApi19 {
  private static final int FLAG_VIRTUAL_DOCUMENT = 512;
  
  private static final String TAG = "DocumentFile";
  
  public static boolean canRead(Context paramContext, Uri paramUri) { return (paramContext.checkCallingOrSelfUriPermission(paramUri, 1) != 0) ? false : (!TextUtils.isEmpty(getRawType(paramContext, paramUri))); }
  
  public static boolean canWrite(Context paramContext, Uri paramUri) {
    if (paramContext.checkCallingOrSelfUriPermission(paramUri, 2) != 0)
      return false; 
    String str = getRawType(paramContext, paramUri);
    int i = queryForInt(paramContext, paramUri, "flags", 0);
    return TextUtils.isEmpty(str) ? false : (((i & 0x4) != 0) ? true : (("vnd.android.document/directory".equals(str) && (i & 0x8) != 0) ? true : ((!TextUtils.isEmpty(str) && (i & 0x2) != 0))));
  }
  
  private static void closeQuietly(@Nullable AutoCloseable paramAutoCloseable) {
    if (paramAutoCloseable != null)
      try {
        paramAutoCloseable.close();
        return;
      } catch (RuntimeException paramAutoCloseable) {
        throw paramAutoCloseable;
      } catch (Exception paramAutoCloseable) {
        return;
      }  
  }
  
  public static boolean exists(Context paramContext, Uri paramUri) {
    Cursor cursor1;
    ContentResolver contentResolver = paramContext.getContentResolver();
    Cursor cursor2 = null;
    paramContext = null;
    boolean bool = true;
    try {
      Cursor cursor = contentResolver.query(paramUri, new String[] { "document_id" }, null, null, null);
      cursor1 = cursor;
      cursor2 = cursor;
      int i = cursor.getCount();
      if (i <= 0)
        bool = false; 
      closeQuietly(cursor);
      return bool;
    } catch (Exception paramUri) {
      cursor1 = cursor2;
      StringBuilder stringBuilder = new StringBuilder();
      cursor1 = cursor2;
      stringBuilder.append("Failed query: ");
      cursor1 = cursor2;
      stringBuilder.append(paramUri);
      cursor1 = cursor2;
      Log.w("DocumentFile", stringBuilder.toString());
      closeQuietly(cursor2);
      return false;
    } finally {}
    closeQuietly(cursor1);
    throw paramUri;
  }
  
  public static long getFlags(Context paramContext, Uri paramUri) { return queryForLong(paramContext, paramUri, "flags", 0L); }
  
  @Nullable
  public static String getName(Context paramContext, Uri paramUri) { return queryForString(paramContext, paramUri, "_display_name", null); }
  
  @Nullable
  private static String getRawType(Context paramContext, Uri paramUri) { return queryForString(paramContext, paramUri, "mime_type", null); }
  
  @Nullable
  public static String getType(Context paramContext, Uri paramUri) {
    String str = getRawType(paramContext, paramUri);
    return "vnd.android.document/directory".equals(str) ? null : str;
  }
  
  public static boolean isDirectory(Context paramContext, Uri paramUri) { return "vnd.android.document/directory".equals(getRawType(paramContext, paramUri)); }
  
  public static boolean isFile(Context paramContext, Uri paramUri) {
    String str = getRawType(paramContext, paramUri);
    return !("vnd.android.document/directory".equals(str) || TextUtils.isEmpty(str));
  }
  
  public static boolean isVirtual(Context paramContext, Uri paramUri) {
    boolean bool2 = DocumentsContract.isDocumentUri(paramContext, paramUri);
    boolean bool1 = false;
    if (!bool2)
      return false; 
    if ((getFlags(paramContext, paramUri) & 0x200L) != 0L)
      bool1 = true; 
    return bool1;
  }
  
  public static long lastModified(Context paramContext, Uri paramUri) { return queryForLong(paramContext, paramUri, "last_modified", 0L); }
  
  public static long length(Context paramContext, Uri paramUri) { return queryForLong(paramContext, paramUri, "_size", 0L); }
  
  private static int queryForInt(Context paramContext, Uri paramUri, String paramString, int paramInt) { return (int)queryForLong(paramContext, paramUri, paramString, paramInt); }
  
  private static long queryForLong(Context paramContext, Uri paramUri, String paramString, long paramLong) {
    Cursor cursor1;
    ContentResolver contentResolver = paramContext.getContentResolver();
    Cursor cursor2 = null;
    paramContext = null;
    try {
      Cursor cursor = contentResolver.query(paramUri, new String[] { paramString }, null, null, null);
      cursor1 = cursor;
      cursor2 = cursor;
      if (cursor.moveToFirst()) {
        cursor1 = cursor;
        cursor2 = cursor;
        if (!cursor.isNull(0)) {
          cursor1 = cursor;
          cursor2 = cursor;
          long l = cursor.getLong(0);
          closeQuietly(cursor);
          return l;
        } 
      } 
      closeQuietly(cursor);
      return paramLong;
    } catch (Exception paramUri) {
      cursor1 = cursor2;
      StringBuilder stringBuilder = new StringBuilder();
      cursor1 = cursor2;
      stringBuilder.append("Failed query: ");
      cursor1 = cursor2;
      stringBuilder.append(paramUri);
      cursor1 = cursor2;
      Log.w("DocumentFile", stringBuilder.toString());
      closeQuietly(cursor2);
      return paramLong;
    } finally {}
    closeQuietly(cursor1);
    throw paramUri;
  }
  
  @Nullable
  private static String queryForString(Context paramContext, Uri paramUri, String paramString1, @Nullable String paramString2) {
    Cursor cursor1;
    ContentResolver contentResolver = paramContext.getContentResolver();
    Cursor cursor2 = null;
    paramContext = null;
    try {
      Cursor cursor = contentResolver.query(paramUri, new String[] { paramString1 }, null, null, null);
      cursor1 = cursor;
      cursor2 = cursor;
      if (cursor.moveToFirst()) {
        cursor1 = cursor;
        cursor2 = cursor;
        if (!cursor.isNull(0)) {
          cursor1 = cursor;
          cursor2 = cursor;
          paramString1 = cursor.getString(0);
          closeQuietly(cursor);
          return paramString1;
        } 
      } 
      closeQuietly(cursor);
      return paramString2;
    } catch (Exception paramUri) {
      cursor1 = cursor2;
      StringBuilder stringBuilder = new StringBuilder();
      cursor1 = cursor2;
      stringBuilder.append("Failed query: ");
      cursor1 = cursor2;
      stringBuilder.append(paramUri);
      cursor1 = cursor2;
      Log.w("DocumentFile", stringBuilder.toString());
      closeQuietly(cursor2);
      return paramString2;
    } finally {}
    closeQuietly(cursor1);
    throw paramUri;
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v4\provider\DocumentsContractApi19.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */