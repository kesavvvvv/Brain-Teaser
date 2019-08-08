package android.support.v13.view.inputmethod;

import android.content.ClipDescription;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.view.inputmethod.InputContentInfo;

public final class InputConnectionCompat {
  private static final String COMMIT_CONTENT_ACTION = "android.support.v13.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT";
  
  private static final String COMMIT_CONTENT_CONTENT_URI_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_URI";
  
  private static final String COMMIT_CONTENT_DESCRIPTION_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION";
  
  private static final String COMMIT_CONTENT_FLAGS_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS";
  
  private static final String COMMIT_CONTENT_LINK_URI_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI";
  
  private static final String COMMIT_CONTENT_OPTS_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_OPTS";
  
  private static final String COMMIT_CONTENT_RESULT_RECEIVER = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_RESULT_RECEIVER";
  
  public static final int INPUT_CONTENT_GRANT_READ_URI_PERMISSION = 1;
  
  public static boolean commitContent(@NonNull InputConnection paramInputConnection, @NonNull EditorInfo paramEditorInfo, @NonNull InputContentInfoCompat paramInputContentInfoCompat, int paramInt, @Nullable Bundle paramBundle) {
    boolean bool1;
    ClipDescription clipDescription = paramInputContentInfoCompat.getDescription();
    boolean bool2 = false;
    String[] arrayOfString = EditorInfoCompat.getContentMimeTypes(paramEditorInfo);
    int i = arrayOfString.length;
    byte b = 0;
    while (true) {
      bool1 = bool2;
      if (b < i) {
        if (clipDescription.hasMimeType(arrayOfString[b])) {
          bool1 = true;
          break;
        } 
        b++;
        continue;
      } 
      break;
    } 
    if (!bool1)
      return false; 
    if (Build.VERSION.SDK_INT >= 25)
      return paramInputConnection.commitContent((InputContentInfo)paramInputContentInfoCompat.unwrap(), paramInt, paramBundle); 
    Bundle bundle = new Bundle();
    bundle.putParcelable("android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_URI", paramInputContentInfoCompat.getContentUri());
    bundle.putParcelable("android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION", paramInputContentInfoCompat.getDescription());
    bundle.putParcelable("android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI", paramInputContentInfoCompat.getLinkUri());
    bundle.putInt("android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS", paramInt);
    bundle.putParcelable("android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_OPTS", paramBundle);
    return paramInputConnection.performPrivateCommand("android.support.v13.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT", bundle);
  }
  
  @NonNull
  public static InputConnection createWrapper(@NonNull InputConnection paramInputConnection, @NonNull EditorInfo paramEditorInfo, @NonNull final OnCommitContentListener listener) {
    if (paramInputConnection != null) {
      if (paramEditorInfo != null) {
        if (paramOnCommitContentListener != null)
          return (Build.VERSION.SDK_INT >= 25) ? new InputConnectionWrapper(paramInputConnection, false) {
              public boolean commitContent(InputContentInfo param1InputContentInfo, int param1Int, Bundle param1Bundle) { return listener.onCommitContent(InputContentInfoCompat.wrap(param1InputContentInfo), param1Int, param1Bundle) ? true : super.commitContent(param1InputContentInfo, param1Int, param1Bundle); }
            } : ((EditorInfoCompat.getContentMimeTypes(paramEditorInfo).length == 0) ? paramInputConnection : new InputConnectionWrapper(paramInputConnection, false) {
              public boolean performPrivateCommand(String param1String, Bundle param1Bundle) { return InputConnectionCompat.handlePerformPrivateCommand(param1String, param1Bundle, listener) ? true : super.performPrivateCommand(param1String, param1Bundle); }
            }); 
        throw new IllegalArgumentException("onCommitContentListener must be non-null");
      } 
      throw new IllegalArgumentException("editorInfo must be non-null");
    } 
    throw new IllegalArgumentException("inputConnection must be non-null");
  }
  
  static boolean handlePerformPrivateCommand(@Nullable String paramString, @NonNull Bundle paramBundle, @NonNull OnCommitContentListener paramOnCommitContentListener) {
    ResultReceiver resultReceiver;
    null = TextUtils.equals("android.support.v13.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT", paramString);
    byte b2 = 0;
    b1 = 0;
    if (!null)
      return false; 
    if (paramBundle == null)
      return false; 
    paramString = null;
    try {
      resultReceiver1 = (ResultReceiver)paramBundle.getParcelable("android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_RESULT_RECEIVER");
      resultReceiver = resultReceiver1;
      Uri uri1 = (Uri)paramBundle.getParcelable("android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_URI");
      resultReceiver = resultReceiver1;
      ClipDescription clipDescription = (ClipDescription)paramBundle.getParcelable("android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION");
      resultReceiver = resultReceiver1;
      Uri uri2 = (Uri)paramBundle.getParcelable("android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI");
      resultReceiver = resultReceiver1;
      int i = paramBundle.getInt("android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS");
      resultReceiver = resultReceiver1;
      paramBundle = (Bundle)paramBundle.getParcelable("android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_OPTS");
      resultReceiver = resultReceiver1;
      return paramOnCommitContentListener.onCommitContent(new InputContentInfoCompat(uri1, clipDescription, uri2), i, paramBundle);
    } finally {
      if (resultReceiver != null) {
        b1 = b2;
        if (false)
          b1 = 1; 
        resultReceiver.send(b1, null);
      } 
    } 
  }
  
  public static interface OnCommitContentListener {
    boolean onCommitContent(InputContentInfoCompat param1InputContentInfoCompat, int param1Int, Bundle param1Bundle);
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v13\view\inputmethod\InputConnectionCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */