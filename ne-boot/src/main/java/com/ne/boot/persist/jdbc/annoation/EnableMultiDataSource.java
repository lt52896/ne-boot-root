package com.ne.boot.persist.jdbc.annoation;

import com.ne.boot.persist.jdbc.MultiDataSourceRegister;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Created by xiezhouyan on 16-7-3.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(MultiDataSourceRegister.class)
public @interface EnableMultiDataSource {
    String[] keys();

    String defaultKey();
}
