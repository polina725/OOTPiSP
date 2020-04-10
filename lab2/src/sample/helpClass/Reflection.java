package sample.helpClass;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Map;

import static sample.helpClass.AdditionalFunction.getClassName;

public class Reflection {
    public static void getAllFieldNames(Class<?> c, ArrayList<String> fieldNames) {
        try {
            Field[] declaredFields = c.getDeclaredFields();
            for (int i=declaredFields.length - 1 ;i>=0;i--) {
                if (declaredFields[i].getType().toString().contains("class") && !declaredFields[i].getType().toString().contains("String")){
                    String tmp = getClassName(declaredFields[i].getType().toString());
                    Class<?> c1 = Class.forName(tmp);
                    getAllFieldNames(c1, fieldNames);
                }
                else
                    fieldNames.add(declaredFields[i].getName());
            }
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        c = c.getSuperclass();
        if (c != null)
            getAllFieldNames(c,fieldNames);
    }

    public static void setFieldsValue(Class<?> c, Object obj, Map<String,String> map) throws IllegalAccessException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        Field[] declaredFields = c.getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            if (field.toString().contains("int")){
                try {
                    int tmp = Integer.parseInt(map.get(field.getName()));
                    field.setInt(obj,tmp);
                }catch(NumberFormatException e){
                  //  System.out.println("\tNOT A NUMBER!!!!!!!!");
                }
            }
            else if(field.toString().contains("String")){
                String tmp = map.get(field.getName());
                field.set(obj, tmp);
            }
            else if(field.getType().toString().contains("class") && !field.getType().toString().contains("String")){
                String tmp = getClassName(field.getType().toString());
                Class<?> c1 = Class.forName(tmp);
                Object obj1 =c1.getDeclaredConstructor().newInstance();
                setFieldsValue(c1,obj1,map);
                field.set(obj, obj1);
            }
        }
        c = c.getSuperclass();
        if (c != null)
            setFieldsValue(c,obj,map);
    }

    public static void getFieldsValue(Class<?> c,Object obj,Map<String,String> map) throws IllegalAccessException {
        Field[] declaredFields = c.getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            if(field.getType().toString().contains("class") && !field.getType().toString().contains("String")){
                String tmp = getClassName(field.getType().toString());
                try {
                    Class<?> c1 = Class.forName(tmp);
                    getFieldsValue(c1,field.get(obj),map);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            else
                map.put(field.getName(),field.get(obj).toString());
        }
        c = c.getSuperclass();
        if (c != null)
            getFieldsValue(c,obj,map);
    }
}
