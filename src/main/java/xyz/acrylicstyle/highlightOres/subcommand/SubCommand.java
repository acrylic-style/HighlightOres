package xyz.acrylicstyle.highlightOres.subcommand;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks class as SubCommand.<br />
 * Annotated class must extend SubCommandExecutor or it will fail to load command.<br />
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SubCommand {
    String name();
    String alias() default "";
    String usage();
    String description();
}
