package android.support.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.TYPE, ElementType.PARAMETER})
public @interface WorkerThread {}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\annotation\WorkerThread.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */