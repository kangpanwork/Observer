package code.core;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.TYPE_USE;

@Target({TYPE, TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EventObject {
    String[] name();
}
