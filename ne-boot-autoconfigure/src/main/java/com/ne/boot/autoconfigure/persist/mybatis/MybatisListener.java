package com.ne.boot.autoconfigure.persist.mybatis;

import com.ne.boot.persist.mybatis.RowBoundsAdapter;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.util.ClassUtils;

/**
 * Created by xiezhouyan on 17-3-16.
 */
public class MybatisListener implements ApplicationListener<ApplicationPreparedEvent> {
    public static final String ROWBONDS_CLASS_NAME = "org.apache.ibatis.session.RowBounds";
    private static final boolean isPersenet = ClassUtils.isPresent(ROWBONDS_CLASS_NAME, RowBoundsAdapter.class.getClassLoader());

    @Override
    public void onApplicationEvent(ApplicationPreparedEvent event) {
        if (isPersenet) {
            RowBoundsAdapter.getInstance().usePage();
        }
    }
}
