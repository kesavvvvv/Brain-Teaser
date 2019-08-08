package android.support.v4.provider;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;
import android.webkit.MimeTypeMap;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

class RawDocumentFile extends DocumentFile {
  private File mFile;
  
  RawDocumentFile(@Nullable DocumentFile paramDocumentFile, File paramFile) {
    super(paramDocumentFile);
    this.mFile = paramFile;
  }
  
  private static boolean deleteContents(File paramFile) {
    File[] arrayOfFile = paramFile.listFiles();
    int i = 1;
    byte b = 1;
    if (arrayOfFile != null) {
      int j = arrayOfFile.length;
      byte b1 = 0;
      while (true) {
        i = b;
        if (b1 < j) {
          File file = arrayOfFile[b1];
          i = b;
          if (file.isDirectory())
            i = b & deleteContents(file); 
          b = i;
          if (!file.delete()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to delete ");
            stringBuilder.append(file);
            Log.w("DocumentFile", stringBuilder.toString());
            b = 0;
          } 
          b1++;
          continue;
        } 
        break;
      } 
    } 
    return i;
  }
  
  private static String getTypeForName(String paramString) {
    int i = paramString.lastIndexOf('.');
    if (i >= 0) {
      paramString = paramString.substring(i + 1).toLowerCase();
      paramString = MimeTypeMap.getSingleton().getMimeTypeFromExtension(paramString);
      if (paramString != null)
        return paramString; 
    } 
    return "application/octet-stream";
  }
  
  public boolean canRead() { return this.mFile.canRead(); }
  
  public boolean canWrite() { return this.mFile.canWrite(); }
  
  @Nullable
  public DocumentFile createDirectory(String paramString) {
    File file = new File(this.mFile, paramString);
    return (file.isDirectory() || file.mkdir()) ? new RawDocumentFile(this, file) : null;
  }
  
  @Nullable
  public DocumentFile createFile(String paramString1, String paramString2) {
    String str1;
    String str2 = MimeTypeMap.getSingleton().getExtensionFromMimeType(paramString1);
    paramString1 = paramString2;
    if (str2 != null) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(paramString2);
      stringBuilder.append(".");
      stringBuilder.append(str2);
      str1 = stringBuilder.toString();
    } 
    file = new File(this.mFile, str1);
    try {
      file.createNewFile();
      return new RawDocumentFile(this, file);
    } catch (IOException file) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Failed to createFile: ");
      stringBuilder.append(file);
      Log.w("DocumentFile", stringBuilder.toString());
      return null;
    } 
  }
  
  public boolean delete() {
    deleteContents(this.mFile);
    return this.mFile.delete();
  }
  
  public boolean exists() { return this.mFile.exists(); }
  
  public String getName() { return this.mFile.getName(); }
  
  @Nullable
  public String getType() { return this.mFile.isDirectory() ? null : getTypeForName(this.mFile.getName()); }
  
  public Uri getUri() { return Uri.fromFile(this.mFile); }
  
  public boolean isDirectory() { return this.mFile.isDirectory(); }
  
  public boolean isFile() { return this.mFile.isFile(); }
  
  public boolean isVirtual() { return false; }
  
  public long lastModified() { return this.mFile.lastModified(); }
  
  public long length() { return this.mFile.length(); }
  
  public DocumentFile[] listFiles() {
    ArrayList arrayList = new ArrayList();
    File[] arrayOfFile = this.mFile.listFiles();
    if (arrayOfFile != null) {
      int i = arrayOfFile.length;
      for (byte b = 0; b < i; b++)
        arrayList.add(new RawDocumentFile(this, arrayOfFile[b])); 
    } 
    return (DocumentFile[])arrayList.toArray(new DocumentFile[arrayList.size()]);
  }
  
  public boolean renameTo(String paramString) {
    File file = new File(this.mFile.getParentFile(), paramString);
    if (this.mFile.renameTo(file)) {
      this.mFile = file;
      return true;
    } 
    return false;
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v4\provider\RawDocumentFile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */