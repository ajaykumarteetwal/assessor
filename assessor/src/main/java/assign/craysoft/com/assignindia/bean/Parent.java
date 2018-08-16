package assign.craysoft.com.assignindia.bean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import assign.craysoft.com.assignindia.util.Exclude;

public abstract class Parent<T extends Parent> implements Serializable, Cloneable {
    private boolean status;
    private int responseCode;
    private String message;
    private String name;
    private T data;
    private T[] rows;

    protected Parent(JSONObject jsonObject) {
//        data = data();
        if (jsonObject != null)
            parse(jsonObject, Parent.class, this);
    }

//    protected abstract T data();

    public boolean isStatus() {
        return status;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }

    public T getData() {
        return data;
    }

    public T[] getRows() {
        return rows;
    }

    protected String mapResponseToken(String localToken) {
        return localToken;
    }

    private void parse(JSONObject jsonObject, Class aClass, Object object) {
        Field[] declaredFields = aClass.getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(Exclude.class))
                continue;
            if (!field.isAccessible())
                field.setAccessible(true);
            String name = mapResponseToken(field.getName());
            Object value = jsonObject.opt(name);
            if (value != null) {
                try {
                    if (value instanceof JSONObject) {
                        setJsonValue(field, object, JSONObject.class.cast(value));
                    } else if (value instanceof JSONArray) {
                        JSONArray array = (JSONArray) value;
                        Class<?> fieldType = field.getType();
                        if (fieldType.isArray() && array.length() > 0) {
                            Object[] arrayInstance = (Object[]) field.get(object);
                            if (arrayInstance == null) {
                                if (fieldType.getComponentType().isAssignableFrom(object.getClass()))
                                    arrayInstance = (Object[]) Array.newInstance(object.getClass(), array.length());
                                else
                                    arrayInstance = (Object[]) Array.newInstance(fieldType.getComponentType(), array.length());
                                field.set(object, arrayInstance);
                            }
                            for (int index = 0; index < array.length(); index++) {
                                value = array.opt(index);
                                if (value != null) {
                                    Object o;
                                    if (value instanceof JSONObject)
                                        o = setJsonValue(field, object, JSONObject.class.cast(value));
                                    else {
                                        o = fieldType.getComponentType().newInstance();
                                        if (o.getClass().isAssignableFrom(value.getClass()))
                                            o = value;
                                    }
                                    arrayInstance[index] = o;
                                }
                            }
                        }
                    } else
                        setValueByTrim(field, object, value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
//        Class<?> superclass = aClass.getSuperclass();
//        if (!superclass.isAssignableFrom(Parent.class)) {
//            parse(jsonObject, superclass, object);
//        }
    }

    private Object setJsonValue(Field field, Object object, JSONObject value) throws Exception {
        Class<?> fieldType = field.getType().isArray() ? field.getType().getComponentType() : field.getType();
        Object o = field.getType().isArray() ? null : field.get(object);
        if (o == null) {
            if (fieldType.isAssignableFrom(object.getClass())) {
                if (field.getType().isArray()) {
                    Constructor<?> constructor = object.getClass().getConstructor(JSONObject.class);
                    if (constructor != null) {
                        if (!constructor.isAccessible())
                            constructor.setAccessible(true);
                        o = constructor.newInstance(value);
                    } else
                        o = fieldType.newInstance();
                } else
                    o = object;
            } else
                o = fieldType.newInstance();
            if (!field.getType().isArray())
                field.set(object, o);
        }
        parse(value, o.getClass(), o);
        return o;
    }

    private void setValueByTrim(Field field, Object object, Object value) throws IllegalAccessException {
        if (value instanceof String && (field.getType() == boolean.class || field.getType() == Boolean.class)) {
            field.set(object, Boolean.valueOf(String.valueOf(value).trim()));
        } else if (value instanceof String && (field.getType() == int.class || field.getType() == Integer.class)) {
            field.set(object, Integer.valueOf(String.valueOf(value).trim()));
        } else if (value instanceof String && (field.getType() == long.class || field.getType() == Long.class)) {
            field.set(object, Long.valueOf(String.valueOf(value).trim()));
        } else
            field.set(object, value);
    }

    @Override
    public String toString() {
        return toJSONObject().toString();
    }

    public JSONObject toJSONObject() {
        return toJSONObject(this);
    }

    private JSONObject toJSONObject(Object object) {
        return toJSONObject(object, true);
    }

    private JSONObject toJSONObject(Object object, boolean isOuterCall) {
        JSONObject jsonObject = new JSONObject();
        Class aClass = object.getClass();
        if (isOuterCall)
            while (!((aClass = aClass.getSuperclass())).getName().equals(Object.class.getName()))
                if (aClass.getName().equals(Parent.class.getName())) {
                    break;
                }
        for (Field field : aClass.getDeclaredFields()) {
            try {
                String name = field.getName();
                if (field.isAnnotationPresent(Exclude.class) || name.equals("serialVersionUID"))
                    continue;
                if (!field.isAccessible())
                    field.setAccessible(true);
                Object value = field.get(object);
                if (value != null) {
                    if (value instanceof Parent) {
                        jsonObject.put(name, toJSONObject(value, false));
                    } else
                        jsonObject.put(name, value);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return jsonObject;
    }
}