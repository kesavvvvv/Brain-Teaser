package android.support.v4.provider;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import java.util.ArrayList;

@RequiresApi(21)
class TreeDocumentFile extends DocumentFile {
  private Context mContext;
  
  private Uri mUri;
  
  TreeDocumentFile(@Nullable DocumentFile paramDocumentFile, Context paramContext, Uri paramUri) {
    super(paramDocumentFile);
    this.mContext = paramContext;
    this.mUri = paramUri;
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
  
  @Nullable
  private static Uri createFile(Context paramContext, Uri paramUri, String paramString1, String paramString2) {
    try {
      return DocumentsContract.createDocument(paramContext.getContentResolver(), paramUri, paramString1, paramString2);
    } catch (Exception paramContext) {
      return null;
    } 
  }
  
  public boolean canRead() { return DocumentsContractApi19.canRead(this.mContext, this.mUri); }
  
  public boolean canWrite() { return DocumentsContractApi19.canWrite(this.mContext, this.mUri); }
  
  @Nullable
  public DocumentFile createDirectory(String paramString) {
    Uri uri = createFile(this.mContext, this.mUri, "vnd.android.document/directory", paramString);
    return (uri != null) ? new TreeDocumentFile(this, this.mContext, uri) : null;
  }
  
  @Nullable
  public DocumentFile createFile(String paramString1, String paramString2) {
    Uri uri = createFile(this.mContext, this.mUri, paramString1, paramString2);
    return (uri != null) ? new TreeDocumentFile(this, this.mContext, uri) : null;
  }
  
  public boolean delete() {
    try {
      return DocumentsContract.deleteDocument(this.mContext.getContentResolver(), this.mUri);
    } catch (Exception exception) {
      return false;
    } 
  }
  
  public boolean exists() { return DocumentsContractApi19.exists(this.mContext, this.mUri); }
  
  @Nullable
  public String getName() { return DocumentsContractApi19.getName(this.mContext, this.mUri); }
  
  @Nullable
  public String getType() { return DocumentsContractApi19.getType(this.mContext, this.mUri); }
  
  public Uri getUri() { return this.mUri; }
  
  public boolean isDirectory() { return DocumentsContractApi19.isDirectory(this.mContext, this.mUri); }
  
  public boolean isFile() { return DocumentsContractApi19.isFile(this.mContext, this.mUri); }
  
  public boolean isVirtual() { return DocumentsContractApi19.isVirtual(this.mContext, this.mUri); }
  
  public long lastModified() { return DocumentsContractApi19.lastModified(this.mContext, this.mUri); }
  
  public long length() { return DocumentsContractApi19.length(this.mContext, this.mUri); }
  
  public DocumentFile[] listFiles() {
    contentResolver = this.mContext.getContentResolver();
    Cursor cursor1 = this.mUri;
    Uri uri = DocumentsContract.buildChildDocumentsUriUsingTree(cursor1, DocumentsContract.getDocumentId(cursor1));
    ArrayList arrayList = new ArrayList();
    cursor1 = null;
    Cursor cursor2 = null;
    try {
      Cursor cursor = contentResolver.query(uri, new String[] { "document_id" }, null, null, null);
      while (true) {
        cursor2 = cursor;
        Cursor cursor3 = cursor;
        if (cursor.moveToNext()) {
          cursor2 = cursor;
          cursor3 = cursor;
          String str = cursor.getString(0);
          cursor2 = cursor;
          cursor3 = cursor;
          arrayList.add(DocumentsContract.buildDocumentUriUsingTree(this.mUri, str));
          continue;
        } 
        break;
      } 
      cursor1 = cursor;
      closeQuietly(cursor1);
    } catch (Exception contentResolver) {
      Cursor cursor6 = cursor1;
      StringBuilder stringBuilder = new StringBuilder();
      Cursor cursor5 = cursor1;
      stringBuilder.append("Failed query: ");
      Cursor cursor4 = cursor1;
      stringBuilder.append(contentResolver);
      Cursor cursor3 = cursor1;
      Log.w("DocumentFile", stringBuilder.toString());
      closeQuietly(cursor1);
    } finally {}
    Uri[] arrayOfUri = (Uri[])arrayList.toArray(new Uri[arrayList.size()]);
    DocumentFile[] arrayOfDocumentFile = new DocumentFile[arrayOfUri.length];
    for (byte b = 0; b < arrayOfUri.length; b++)
      arrayOfDocumentFile[b] = new TreeDocumentFile(this, this.mContext, arrayOfUri[b]); 
    return arrayOfDocumentFile;
  }
  
  public boolean renameTo(String paramString) {
    try {
      Uri uri = DocumentsContract.renameDocument(this.mContext.getContentResolver(), this.mUri, paramString);
      if (uri != null) {
        this.mUri = uri;
        return true;
      } 
      return false;
    } catch (Exception paramString) {
      return false;
    } 
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v4\provider\TreeDocumentFile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */