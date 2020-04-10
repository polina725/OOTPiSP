package com.company;
import java.lang.reflect.Field;
//import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args){

        Scanner scan = new Scanner(System.in);
        System.out.println("Device's type: ");
        String str = scan.nextLine();
        try{
            Class<?> c = Class.forName("com.company." + str);
            if (c != null) {
                Object obj = c.getDeclaredConstructor().newInstance();
                getInfo(c,obj,true);
                getInfo(c,obj,false);
            }
        }catch(ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        System.out.println("END");
    }

    public static void setFields(Class<?> c, Object obj) throws IllegalAccessException{
        Scanner scan = new Scanner(System.in);
        Field[] declaredFields = c.getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            System.out.println("Type for field" + field + ": ");
            if (field.toString().contains("int")){
                int tmp = scan.nextInt();
                field.setInt(obj,tmp);
            }
            else if(field.toString().contains("String")){
                String tmp = scan.nextLine();
                field.set(obj, tmp);
            }
        }
    }

    public static void getFields(Class<?> c, Object obj) throws IllegalAccessException {
        Field[] declaredFields = c.getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            System.out.println(field + " value: " + field.get(obj));
        }
    }

    public static void getInfo(Class<?> c, Object obj, boolean setFields) {
        if (setFields) try {
            setFields(c, obj);
        } catch (IllegalAccessException e){
            e.printStackTrace();
        }
        else try {
            getFields(c, obj);
        }catch (IllegalAccessException e){
            e.printStackTrace();
        }
        c = c.getSuperclass();
        if (c != null)
            getInfo(c, obj, setFields);
    }
}


/*        String[] classNames = {"com.company.Device","com.company.PC","com.company.Laptop","com.company.WirelessHeadphones","com.company.Accumulator"};
        for (String className : classNames){
            System.out.println("\tENTER RECURSION");
            try{
                getInfo(className);
            } catch (ClassNotFoundException e){
                e.printStackTrace();
            }
            System.out.println("\tOUT OF RECURSION\n");
        }*/