package android.support.v4.widget;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.RestrictTo;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SimpleCursorAdapter extends ResourceCursorAdapter {
  private CursorToStringConverter mCursorToStringConverter;
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  protected int[] mFrom;
  
  String[] mOriginalFrom;
  
  private int mStringConversionColumn = -1;
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  protected int[] mTo;
  
  private ViewBinder mViewBinder;
  
  @Deprecated
  public SimpleCursorAdapter(Context paramContext, int paramInt, Cursor paramCursor, String[] paramArrayOfString, int[] paramArrayOfInt) {
    super(paramContext, paramInt, paramCursor);
    this.mTo = paramArrayOfInt;
    this.mOriginalFrom = paramArrayOfString;
    findColumns(paramCursor, paramArrayOfString);
  }
  
  public SimpleCursorAdapter(Context paramContext, int paramInt1, Cursor paramCursor, String[] paramArrayOfString, int[] paramArrayOfInt, int paramInt2) {
    super(paramContext, paramInt1, paramCursor, paramInt2);
    this.mTo = paramArrayOfInt;
    this.mOriginalFrom = paramArrayOfString;
    findColumns(paramCursor, paramArrayOfString);
  }
  
  private void findColumns(Cursor paramCursor, String[] paramArrayOfString) {
    if (paramCursor != null) {
      int i = paramArrayOfString.length;
      int[] arrayOfInt = this.mFrom;
      if (arrayOfInt == null || arrayOfInt.length != i)
        this.mFrom = new int[i]; 
      for (byte b = 0; b < i; b++)
        this.mFrom[b] = paramCursor.getColumnIndexOrThrow(paramArrayOfString[b]); 
      return;
    } 
    this.mFrom = null;
  }
  
  public void bindView(View paramView, Context paramContext, Cursor paramCursor) {
    ViewBinder viewBinder = this.mViewBinder;
    int i = this.mTo.length;
    int[] arrayOfInt1 = this.mFrom;
    int[] arrayOfInt2 = this.mTo;
    byte b;
    for (b = 0; b < i; b++) {
      View view = paramView.findViewById(arrayOfInt2[b]);
      if (view != null) {
        boolean bool = false;
        if (viewBinder != null)
          bool = viewBinder.setViewValue(view, paramCursor, arrayOfInt1[b]); 
        if (!bool) {
          String str2 = paramCursor.getString(arrayOfInt1[b]);
          String str1 = str2;
          if (str2 == null)
            str1 = ""; 
          if (view instanceof TextView) {
            setViewText((TextView)view, str1);
          } else if (view instanceof ImageView) {
            setViewImage((ImageView)view, str1);
          } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(view.getClass().getName());
            stringBuilder.append(" is not a ");
            stringBuilder.append(" view that can be bounds by this SimpleCursorAdapter");
            throw new IllegalStateException(stringBuilder.toString());
          } 
        } 
      } 
    } 
  }
  
  public void changeCursorAndColumns(Cursor paramCursor, String[] paramArrayOfString, int[] paramArrayOfInt) {
    this.mOriginalFrom = paramArrayOfString;
    this.mTo = paramArrayOfInt;
    findColumns(paramCursor, this.mOriginalFrom);
    changeCursor(paramCursor);
  }
  
  public CharSequence convertToString(Cursor paramCursor) {
    CursorToStringConverter cursorToStringConverter = this.mCursorToStringConverter;
    if (cursorToStringConverter != null)
      return cursorToStringConverter.convertToString(paramCursor); 
    int i = this.mStringConversionColumn;
    return (i > -1) ? paramCursor.getString(i) : super.convertToString(paramCursor);
  }
  
  public CursorToStringConverter getCursorToStringConverter() { return this.mCursorToStringConverter; }
  
  public int getStringConversionColumn() { return this.mStringConversionColumn; }
  
  public ViewBinder getViewBinder() { return this.mViewBinder; }
  
  public void setCursorToStringConverter(CursorToStringConverter paramCursorToStringConverter) { this.mCursorToStringConverter = paramCursorToStringConverter; }
  
  public void setStringConversionColumn(int paramInt) { this.mStringConversionColumn = paramInt; }
  
  public void setViewBinder(ViewBinder paramViewBinder) { this.mViewBinder = paramViewBinder; }
  
  public void setViewImage(ImageView paramImageView, String paramString) {
    try {
      paramImageView.setImageResource(Integer.parseInt(paramString));
      return;
    } catch (NumberFormatException numberFormatException) {
      paramImageView.setImageURI(Uri.parse(paramString));
      return;
    } 
  }
  
  public void setViewText(TextView paramTextView, String paramString) { paramTextView.setText(paramString); }
  
  public Cursor swapCursor(Cursor paramCursor) {
    findColumns(paramCursor, this.mOriginalFrom);
    return super.swapCursor(paramCursor);
  }
  
  public static interface CursorToStringConverter {
    CharSequence convertToString(Cursor param1Cursor);
  }
  
  public static interface ViewBinder {
    boolean setViewValue(View param1View, Cursor param1Cursor, int param1Int);
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v4\widget\SimpleCursorAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */