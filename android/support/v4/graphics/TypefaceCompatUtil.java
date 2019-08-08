package android.support.v4.graphics;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.os.Process;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.util.Log;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class TypefaceCompatUtil {
  private static final String CACHE_FILE_PREFIX = ".font";
  
  private static final String TAG = "TypefaceCompatUtil";
  
  public static void closeQuietly(Closeable paramCloseable) {
    if (paramCloseable != null)
      try {
        paramCloseable.close();
        return;
      } catch (IOException paramCloseable) {} 
  }
  
  @Nullable
  @RequiresApi(19)
  public static ByteBuffer copyToDirectBuffer(Context paramContext, Resources paramResources, int paramInt) {
    file = getTempFile(paramContext);
    paramContext = null;
    if (file == null)
      return null; 
    try {
      boolean bool = copyToFile(file, paramResources, paramInt);
      if (bool)
        byteBuffer = mmap(file); 
      return byteBuffer;
    } finally {
      file.delete();
    } 
  }
  
  public static boolean copyToFile(File paramFile, Resources paramResources, int paramInt) {
    InputStream inputStream = null;
    try {
      inputStream1 = paramResources.openRawResource(paramInt);
      inputStream = inputStream1;
      return copyToFile(paramFile, inputStream1);
    } finally {
      closeQuietly(inputStream);
    } 
  }
  
  public static boolean copyToFile(File paramFile, InputStream paramInputStream) {
    FileOutputStream fileOutputStream2 = null;
    FileOutputStream fileOutputStream1 = null;
    StrictMode.ThreadPolicy threadPolicy = StrictMode.allowThreadDiskWrites();
    try {
      FileOutputStream fileOutputStream = new FileOutputStream(paramFile, false);
      fileOutputStream1 = fileOutputStream;
      fileOutputStream2 = fileOutputStream;
      byte[] arrayOfByte = new byte[1024];
      while (true) {
        fileOutputStream1 = fileOutputStream;
        fileOutputStream2 = fileOutputStream;
        int i = paramInputStream.read(arrayOfByte);
        if (i != -1) {
          fileOutputStream1 = fileOutputStream;
          fileOutputStream2 = fileOutputStream;
          fileOutputStream.write(arrayOfByte, 0, i);
          continue;
        } 
        break;
      } 
      closeQuietly(fileOutputStream);
      StrictMode.setThreadPolicy(threadPolicy);
      return true;
    } catch (IOException paramFile) {
      fileOutputStream1 = fileOutputStream2;
      StringBuilder stringBuilder = new StringBuilder();
      fileOutputStream1 = fileOutputStream2;
      stringBuilder.append("Error copying resource contents to temp file: ");
      fileOutputStream1 = fileOutputStream2;
      stringBuilder.append(paramFile.getMessage());
      fileOutputStream1 = fileOutputStream2;
      Log.e("TypefaceCompatUtil", stringBuilder.toString());
      closeQuietly(fileOutputStream2);
      StrictMode.setThreadPolicy(threadPolicy);
      return false;
    } finally {}
    closeQuietly(fileOutputStream1);
    StrictMode.setThreadPolicy(threadPolicy);
    throw paramFile;
  }
  
  @Nullable
  public static File getTempFile(Context paramContext) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(".font");
    stringBuilder.append(Process.myPid());
    stringBuilder.append("-");
    stringBuilder.append(Process.myTid());
    stringBuilder.append("-");
    String str = stringBuilder.toString();
    for (byte b = 0; b < 100; b++) {
      file = paramContext.getCacheDir();
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append(str);
      stringBuilder1.append(b);
      file = new File(file, stringBuilder1.toString());
      try {
        boolean bool = file.createNewFile();
        if (bool)
          return file; 
      } catch (IOException file) {}
    } 
    return null;
  }
  
  @Nullable
  @RequiresApi(19)
  public static ByteBuffer mmap(Context paramContext, CancellationSignal paramCancellationSignal, Uri paramUri) {
    contentResolver = paramContext.getContentResolver();
    try {
      parcelFileDescriptor = contentResolver.openFileDescriptor(paramUri, "r", paramCancellationSignal);
      if (parcelFileDescriptor == null) {
        if (parcelFileDescriptor != null)
          parcelFileDescriptor.close(); 
        return null;
      } 
      try {
        fileInputStream = new FileInputStream(parcelFileDescriptor.getFileDescriptor());
        try {
          FileChannel fileChannel = fileInputStream.getChannel();
          long l = fileChannel.size();
          return fileChannel.map(FileChannel.MapMode.READ_ONLY, 0L, l);
        } catch (Throwable paramCancellationSignal) {
          try {
            throw paramCancellationSignal;
          } finally {}
        } finally {
          contentResolver = null;
        } 
        if (paramCancellationSignal != null) {
          try {
            fileInputStream.close();
          } catch (Throwable fileInputStream) {
            paramCancellationSignal.addSuppressed(fileInputStream);
          } 
        } else {
          fileInputStream.close();
        } 
        throw contentResolver;
      } catch (Throwable paramCancellationSignal) {
        try {
          throw paramCancellationSignal;
        } finally {}
      } finally {
        contentResolver = null;
      } 
      if (parcelFileDescriptor != null)
        if (paramCancellationSignal != null) {
          try {
            parcelFileDescriptor.close();
          } catch (Throwable parcelFileDescriptor) {
            paramCancellationSignal.addSuppressed(parcelFileDescriptor);
          } 
        } else {
          parcelFileDescriptor.close();
        }  
      throw contentResolver;
    } catch (IOException contentResolver) {
      return null;
    } 
  }
  
  @Nullable
  @RequiresApi(19)
  private static ByteBuffer mmap(File paramFile) {
    try {
      Object object;
      fileInputStream = new FileInputStream(paramFile);
      try {
        FileChannel fileChannel = fileInputStream.getChannel();
        long l = fileChannel.size();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, 0L, l);
      } catch (Throwable null) {
        try {
          throw object;
        } finally {}
      } finally {
        paramFile = null;
      } 
      if (object != null) {
        try {
          fileInputStream.close();
        } catch (Throwable fileInputStream) {
          object.addSuppressed(fileInputStream);
        } 
      } else {
        fileInputStream.close();
      } 
      throw paramFile;
    } catch (IOException paramFile) {
      return null;
    } 
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v4\graphics\TypefaceCompatUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */