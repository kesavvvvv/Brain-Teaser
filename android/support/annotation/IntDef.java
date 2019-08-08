package android.support.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.ANNOTATION_TYPE})
public @interface IntDef {
  boolean flag() default false;
  
  int[] value() default {};
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\annotation\IntDef.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */