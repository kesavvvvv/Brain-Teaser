package androidx.versionedparcelable;

import android.support.annotation.RestrictTo;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.FIELD})
@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public @interface NonParcelField {}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\androidx\versionedparcelable\NonParcelField.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */