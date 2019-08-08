package android.support.v4.content;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.os.CancellationSignal;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Arrays;

public class CursorLoader extends AsyncTaskLoader<Cursor> {
  CancellationSignal mCancellationSignal;
  
  Cursor mCursor;
  
  final Loader<Cursor>.ForceLoadContentObserver mObserver = new Loader.ForceLoadContentObserver(this);
  
  String[] mProjection;
  
  String mSelection;
  
  String[] mSelectionArgs;
  
  String mSortOrder;
  
  Uri mUri;
  
  public CursorLoader(@NonNull Context paramContext) { super(paramContext); }
  
  public CursorLoader(@NonNull Context paramContext, @NonNull Uri paramUri, @Nullable String[] paramArrayOfString1, @Nullable String paramString1, @Nullable String[] paramArrayOfString2, @Nullable String paramString2) {
    super(paramContext);
    this.mUri = paramUri;
    this.mProjection = paramArrayOfString1;
    this.mSelection = paramString1;
    this.mSelectionArgs = paramArrayOfString2;
    this.mSortOrder = paramString2;
  }
  
  public void cancelLoadInBackground() { // Byte code:
    //   0: aload_0
    //   1: invokespecial cancelLoadInBackground : ()V
    //   4: aload_0
    //   5: monitorenter
    //   6: aload_0
    //   7: getfield mCancellationSignal : Landroid/support/v4/os/CancellationSignal;
    //   10: ifnull -> 20
    //   13: aload_0
    //   14: getfield mCancellationSignal : Landroid/support/v4/os/CancellationSignal;
    //   17: invokevirtual cancel : ()V
    //   20: aload_0
    //   21: monitorexit
    //   22: return
    //   23: astore_1
    //   24: aload_0
    //   25: monitorexit
    //   26: aload_1
    //   27: athrow
    // Exception table:
    //   from	to	target	type
    //   6	20	23	finally
    //   20	22	23	finally
    //   24	26	23	finally }
  
  public void deliverResult(Cursor paramCursor) {
    if (isReset()) {
      if (paramCursor != null)
        paramCursor.close(); 
      return;
    } 
    Cursor cursor = this.mCursor;
    this.mCursor = paramCursor;
    if (isStarted())
      super.deliverResult(paramCursor); 
    if (cursor != null && cursor != paramCursor && !cursor.isClosed())
      cursor.close(); 
  }
  
  @Deprecated
  public void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString) {
    super.dump(paramString, paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mUri=");
    paramPrintWriter.println(this.mUri);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mProjection=");
    paramPrintWriter.println(Arrays.toString(this.mProjection));
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mSelection=");
    paramPrintWriter.println(this.mSelection);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mSelectionArgs=");
    paramPrintWriter.println(Arrays.toString(this.mSelectionArgs));
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mSortOrder=");
    paramPrintWriter.println(this.mSortOrder);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mCursor=");
    paramPrintWriter.println(this.mCursor);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mContentChanged=");
    paramPrintWriter.println(this.mContentChanged);
  }
  
  @Nullable
  public String[] getProjection() { return this.mProjection; }
  
  @Nullable
  public String getSelection() { return this.mSelection; }
  
  @Nullable
  public String[] getSelectionArgs() { return this.mSelectionArgs; }
  
  @Nullable
  public String getSortOrder() { return this.mSortOrder; }
  
  @NonNull
  public Uri getUri() { return this.mUri; }
  
  public Cursor loadInBackground() { // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual isLoadInBackgroundCanceled : ()Z
    //   6: ifne -> 123
    //   9: aload_0
    //   10: new android/support/v4/os/CancellationSignal
    //   13: dup
    //   14: invokespecial <init> : ()V
    //   17: putfield mCancellationSignal : Landroid/support/v4/os/CancellationSignal;
    //   20: aload_0
    //   21: monitorexit
    //   22: aload_0
    //   23: invokevirtual getContext : ()Landroid/content/Context;
    //   26: invokevirtual getContentResolver : ()Landroid/content/ContentResolver;
    //   29: aload_0
    //   30: getfield mUri : Landroid/net/Uri;
    //   33: aload_0
    //   34: getfield mProjection : [Ljava/lang/String;
    //   37: aload_0
    //   38: getfield mSelection : Ljava/lang/String;
    //   41: aload_0
    //   42: getfield mSelectionArgs : [Ljava/lang/String;
    //   45: aload_0
    //   46: getfield mSortOrder : Ljava/lang/String;
    //   49: aload_0
    //   50: getfield mCancellationSignal : Landroid/support/v4/os/CancellationSignal;
    //   53: invokestatic query : (Landroid/content/ContentResolver;Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Landroid/support/v4/os/CancellationSignal;)Landroid/database/Cursor;
    //   56: astore_1
    //   57: aload_1
    //   58: ifnull -> 90
    //   61: aload_1
    //   62: invokeinterface getCount : ()I
    //   67: pop
    //   68: aload_1
    //   69: aload_0
    //   70: getfield mObserver : Landroid/support/v4/content/Loader$ForceLoadContentObserver;
    //   73: invokeinterface registerContentObserver : (Landroid/database/ContentObserver;)V
    //   78: goto -> 90
    //   81: astore_2
    //   82: aload_1
    //   83: invokeinterface close : ()V
    //   88: aload_2
    //   89: athrow
    //   90: aload_0
    //   91: monitorenter
    //   92: aload_0
    //   93: aconst_null
    //   94: putfield mCancellationSignal : Landroid/support/v4/os/CancellationSignal;
    //   97: aload_0
    //   98: monitorexit
    //   99: aload_1
    //   100: areturn
    //   101: astore_1
    //   102: aload_0
    //   103: monitorexit
    //   104: aload_1
    //   105: athrow
    //   106: astore_1
    //   107: aload_0
    //   108: monitorenter
    //   109: aload_0
    //   110: aconst_null
    //   111: putfield mCancellationSignal : Landroid/support/v4/os/CancellationSignal;
    //   114: aload_0
    //   115: monitorexit
    //   116: aload_1
    //   117: athrow
    //   118: astore_1
    //   119: aload_0
    //   120: monitorexit
    //   121: aload_1
    //   122: athrow
    //   123: new android/support/v4/os/OperationCanceledException
    //   126: dup
    //   127: invokespecial <init> : ()V
    //   130: athrow
    //   131: astore_1
    //   132: aload_0
    //   133: monitorexit
    //   134: aload_1
    //   135: athrow
    // Exception table:
    //   from	to	target	type
    //   2	22	131	finally
    //   22	57	106	finally
    //   61	78	81	java/lang/RuntimeException
    //   61	78	106	finally
    //   82	90	106	finally
    //   92	99	101	finally
    //   102	104	101	finally
    //   109	116	118	finally
    //   119	121	118	finally
    //   123	131	131	finally
    //   132	134	131	finally }
  
  public void onCanceled(Cursor paramCursor) {
    if (paramCursor != null && !paramCursor.isClosed())
      paramCursor.close(); 
  }
  
  protected void onReset() {
    super.onReset();
    onStopLoading();
    Cursor cursor = this.mCursor;
    if (cursor != null && !cursor.isClosed())
      this.mCursor.close(); 
    this.mCursor = null;
  }
  
  protected void onStartLoading() {
    Cursor cursor = this.mCursor;
    if (cursor != null)
      deliverResult(cursor); 
    if (takeContentChanged() || this.mCursor == null)
      forceLoad(); 
  }
  
  protected void onStopLoading() { cancelLoad(); }
  
  public void setProjection(@Nullable String[] paramArrayOfString) { this.mProjection = paramArrayOfString; }
  
  public void setSelection(@Nullable String paramString) { this.mSelection = paramString; }
  
  public void setSelectionArgs(@Nullable String[] paramArrayOfString) { this.mSelectionArgs = paramArrayOfString; }
  
  public void setSortOrder(@Nullable String paramString) { this.mSortOrder = paramString; }
  
  public void setUri(@NonNull Uri paramUri) { this.mUri = paramUri; }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v4\content\CursorLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */