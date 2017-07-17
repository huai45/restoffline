package com.huai.test;

import com.huai.common.domain.Role;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by zhonghuai.zhang on 2017/7/17.
 */
public class FieldTest {

    public static void main(String[] args) throws Exception {
        Role role = new Role();
        Class<?> clazz = role.getClass();
        Field[] fields = clazz.getDeclaredFields();

        System.out.println(fields.length);

        for (int i = 0; i < fields.length; i++) {
            System.out.println(fields[i].getName());
        }


        Field field = clazz.getDeclaredField("rightMap");

//        Type type = field.getType();
//        System.out.println(type);

        Type genericFieldType = field.getGenericType();

        System.out.println(genericFieldType);


        System.out.println(genericFieldType instanceof ParameterizedType);

        ParameterizedType type = (ParameterizedType) genericFieldType;
        Class clazz1 = (Class) type.getActualTypeArguments()[0];

        System.out.println(type.getActualTypeArguments().length);
        System.out.println(type.getActualTypeArguments()[0]);
    }

}
