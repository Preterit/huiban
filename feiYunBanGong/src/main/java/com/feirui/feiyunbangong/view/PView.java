package com.feirui.feiyunbangong.view;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 成员变量命名对应xml的id名，不需要事件，直接留空
 * 
 * @author feirui1
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PView {
	public int id() default 0;

	// 点击事件
	public String click() default "";

	// 长按事件
	public String longClick() default "";

	// 点击某一项
	public String itemClick() default "";

	// 长按某一项
	public String itemLongClick() default "";

}
