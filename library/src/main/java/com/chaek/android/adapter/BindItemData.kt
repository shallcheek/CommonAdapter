package com.chaek.android.adapter

import java.lang.annotation.Inherited
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import kotlin.reflect.KClass

/**
 * 配合[CommonAdapter] 和 [AbstractItemView] 使用注解类
 * <br></br>
 * 绑定数据源的class与AdapterItemView的关系
 * <br></br>
 * class为数组类型可以是多个class对应一个AdapterItemView
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
annotation class BindItemData(
        /**
         * 对应的class
         *
         * @return class数组
         */
        vararg val value: KClass<*>)