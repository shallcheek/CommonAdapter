package com.chaek.android.library;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 配合{@link CommonAdapter} 和 {@link AbstractAdapterItemView} 使用注解类
 * <br>
 * 绑定数据源的class与AdapterItemView的关系
 * <br>
 * class为数组类型可以是多个class对应一个AdapterItemView
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface AdapterItemData {
    /**
     * 对应的class
     *
     * @return class数组
     */
    Class<?>[] value();
}