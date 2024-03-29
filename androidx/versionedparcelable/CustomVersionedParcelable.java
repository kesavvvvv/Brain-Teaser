package androidx.versionedparcelable;

import android.support.annotation.RestrictTo;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public abstract class CustomVersionedParcelable implements VersionedParcelable {
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public void onPostParceling() {}
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public void onPreParceling(boolean paramBoolean) {}
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\androidx\versionedparcelable\CustomVersionedParcelable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */