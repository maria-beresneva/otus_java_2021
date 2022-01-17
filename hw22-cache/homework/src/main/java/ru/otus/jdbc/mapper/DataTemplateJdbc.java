package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;
import ru.otus.core.sessionmanager.DataBaseOperationException;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
            try {
                if (rs.next()) {
                    return deserializeObjectFromSelectResult(entityClassMetaData, rs);
                }
                return null;
            } catch (SQLException e) {
                throw new DataTemplateException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long insert(Connection connection, T entity) {
        List<Object> params = entityClassMetaData.getFieldsWithoutId().stream().map(field -> {
            try {
                Field instanceOfField = entity.getClass().getDeclaredField(field.getName());
                instanceOfField.setAccessible(true);
                return instanceOfField.get(entity);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                System.out.printf("Failed to insert to %s %n", field.getName());
            }
            return null;
        }).toList();
        return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), params);
    }

    @Override
    public void update(Connection connection, T client) {
        throw new UnsupportedOperationException();
    }

    private T deserializeObjectFromSelectResult(EntityClassMetaData<T> entityClassMetaData, ResultSet selectResult) {
        T classInstance = wrapException(() -> entityClassMetaData.getConstructor().newInstance());
        System.out.println("Empty class instance created: " + classInstance);
        entityClassMetaData.getAllFields()
                .forEach(field -> {
                    try {
                        String value = selectResult.getString(field.getName());
                        if(value != null) {
                            setFieldValue(field, value, classInstance);
                        }
                    } catch (SQLException e) {
                        System.out.printf("Failed to get %s value from select result %s. Will leave as null.", field.getName(), selectResult);
                    }
                });
        return classInstance;
    }

    private void setFieldValue(Field field, String valueInString, T objectToUpdate) {
        try {
            field.setAccessible(true);
            java.io.Serializable castValue = castFieldValueFromString(field.getType(), valueInString);
            field.set(objectToUpdate, castValue);
        } catch (IllegalAccessException e) {
            System.out.printf("Error! Trying to get field %s from object of class %s", field.getName(), objectToUpdate.getClass().getName());
        }

    }

    private static java.io.Serializable castFieldValueFromString(Class<?> fieldClass, String valueInString) {
        if (valueInString.equals("UNKNOWN") || valueInString.equals("NULL"))
            return null;

        if (fieldClass.equals(String.class))
            return valueInString;
        if (fieldClass.equals(char.class) || fieldClass.equals(Character.class))
            return valueInString.charAt(0);

        if (fieldClass.equals(byte.class) || fieldClass.equals(Byte.class))
            return Byte.parseByte(valueInString);
        if (fieldClass.equals(short.class) || fieldClass.equals(Short.class))
            return Short.parseShort(valueInString);
        if (fieldClass.equals(int.class) || fieldClass.equals(Integer.class))
            return Integer.parseInt(valueInString);
        if (fieldClass.equals(long.class) || fieldClass.equals(Long.class))
            return Long.parseLong(valueInString);
        if (fieldClass.equals(BigInteger.class))
            return new BigInteger(valueInString);
        if (fieldClass.equals(float.class) || fieldClass.equals(Float.class))
            return Float.parseFloat(valueInString);
        if (fieldClass.equals(double.class) || fieldClass.equals(Double.class))
            return Double.parseDouble(valueInString);
        if (fieldClass.equals(BigDecimal.class))
            return new BigDecimal(valueInString);

        if (fieldClass.equals(boolean.class) || fieldClass.equals(Boolean.class))
            return Boolean.parseBoolean(valueInString);

        System.out.printf("Error! For now only standard field classes are supported. %s is not one of them. Will set to null.", fieldClass.getName());
        return null;
    }

    private <T> T wrapException(Callable<T> action) {
        try {
            return action.call();
        } catch (Exception ex) {
            throw new DataBaseOperationException("exception", ex);
        }
    }
}
