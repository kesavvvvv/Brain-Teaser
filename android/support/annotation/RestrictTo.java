package android.support.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.PACKAGE})
public @interface RestrictTo {
  Scope[] value();
  
  public enum Scope {
    GROUP_ID, LIBRARY, LIBRARY_GROUP, SUBCLASSES, TESTS;
    
    static  {
      GROUP_ID = new Scope("GROUP_ID", 2);
      TESTS = new Scope("TESTS", 3);
      SUBCLASSES = new Scope("SUBCLASSES", 4);
      $VALUES = new Scope[] { LIBRARY, LIBRARY_GROUP, GROUP_ID, TESTS, SUBCLASSES };
    }
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\annotation\RestrictTo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */