package android.support.v4.database;

import android.text.TextUtils;

@Deprecated
public final class DatabaseUtilsCompat {
  @Deprecated
  public static String[] appendSelectionArgs(String[] paramArrayOfString1, String[] paramArrayOfString2) {
    if (paramArrayOfString1 != null) {
      if (paramArrayOfString1.length == 0)
        return paramArrayOfString2; 
      String[] arrayOfString = new String[paramArrayOfString1.length + paramArrayOfString2.length];
      System.arraycopy(paramArrayOfString1, 0, arrayOfString, 0, paramArrayOfString1.length);
      System.arraycopy(paramArrayOfString2, 0, arrayOfString, paramArrayOfString1.length, paramArrayOfString2.length);
      return arrayOfString;
    } 
    return paramArrayOfString2;
  }
  
  @Deprecated
  public static String concatenateWhere(String paramString1, String paramString2) {
    if (TextUtils.isEmpty(paramString1))
      return paramString2; 
    if (TextUtils.isEmpty(paramString2))
      return paramString1; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("(");
    stringBuilder.append(paramString1);
    stringBuilder.append(") AND (");
    stringBuilder.append(paramString2);
    stringBuilder.append(")");
    return stringBuilder.toString();
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v4\database\DatabaseUtilsCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */