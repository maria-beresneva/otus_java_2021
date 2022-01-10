package ru.otus.jdbc.mapper;

import ru.otus.core.annotation.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private final Class clazz;
    private final Constructor constructor;
    private Field idField;
    private final List<Field> nonIdFields;
    private final List<Field> allFields;

    public EntityClassMetaDataImpl(Class clazz) {
        this.clazz = clazz;
        List<Field> fields = Arrays.asList(clazz.getDeclaredFields());
        if (fields.size() == 0) {
            throw new IllegalArgumentException("Object has no fields!");
        }

        nonIdFields = new ArrayList<>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class)) {
                idField = field;
            } else
                nonIdFields.add(field);
        }

        allFields = new ArrayList<>(nonIdFields);
        allFields.add(idField);

        constructor = Arrays.stream(clazz.getConstructors())
                .filter(constructor -> constructor.getParameterCount() == 0)
                .findFirst()
                .get();
    }

    @Override
    public String getName() {
        return clazz.getSimpleName();
    }

    @Override
    public Constructor<T> getConstructor() {
        return constructor;
    }

    @Override
    public Field getIdField() {
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        return allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return nonIdFields;
    }
}
