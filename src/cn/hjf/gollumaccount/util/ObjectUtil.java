package cn.hjf.gollumaccount.util;

import java.lang.reflect.Field;

import android.util.Log;

public final class ObjectUtil {

    /**
     * 判断两个对象是否equals
     * @param cls
     * @param o1
     * @param o2
     * @return
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    public static boolean equals(Class<?> cls, Object o1, Object o2) throws IllegalAccessException, IllegalArgumentException {
        // 取得所有成员变量
        Field[] fields = cls.getDeclaredFields();
        // 默认是equals
        boolean equals = true;
        for (Field field : fields) {
            field.setAccessible(true);
            // 两个对象的当前成员变量都为空，equals
            if (field.get(o1) == null && field.get(o2) == null) {
                equals = equals && true;
            }
            // 两个对象的当前成员变量一个为空，一个不为空，not equals
            else if (!(field.get(o1) != null && field.get(o2) != null)) {
                equals = equals && false;
            }
            // 两个对象的当前成员变量都不为空，根据不同类型进行比较
            else {
                // 变量类型是String
                if (field.getType().equals(String.class)) {
                    equals = equals && field.get(o1).equals(field.get(o2));
                }
                // 变量类型是Integer
                else if (field.getType().equals(Integer.class)) {
                    equals = equals && field.get(o1) == field.get(o2);
                }
                // 变量类型是Double
                else if (field.getType().equals(Double.class)) {
                    equals = equals && field.get(o1) == field.get(o2);
                }
                // 变量类型是Float
                else if (field.getType().equals(Float.class)) {
                    equals = equals && field.get(o1) == field.get(o2);
                }
            }
        }

        return equals;
    }
}
