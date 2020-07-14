package com.ne.boot.persist.mybatis;

import com.ne.boot.common.exception.NEException;
import org.apache.ibatis.javassist.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xiezhouyan on 17-3-16.
 */
public class RowBoundsAdapter {
    public static final String PAGE_CLASS_NAME = "com.ne.boot.common.entity.Page";
    public static final String ROWBONDS_CLASS_NAME = "org.apache.ibatis.session.RowBounds";
    private static final Logger logger = LoggerFactory.getLogger(RowBoundsAdapter.class);
    private boolean used = false;
    private static final RowBoundsAdapter instance = new RowBoundsAdapter();

    private RowBoundsAdapter() {
    }

    public static RowBoundsAdapter getInstance() {
        return instance;
    }

    public synchronized void usePage() {
        try {
            if (used) {
                logger.debug("rowbounds has been set");
                return;
            }

            ClassPool pool = ClassPool.getDefault();
            ClassClassPath classPath = new ClassClassPath(this.getClass());
            pool.insertClassPath(classPath);
            CtClass cc = pool.get(PAGE_CLASS_NAME);
            if (cc.isFrozen()) {
                logger.info("page is frozen");
                cc.defrost();
            }
            cc.setSuperclass(pool.get(ROWBONDS_CLASS_NAME));
            cc.toClass();
            used = true;
        } catch (CannotCompileException | NotFoundException e) {
            throw new NEException(e);
        }
    }
}
