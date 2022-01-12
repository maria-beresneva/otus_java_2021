package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {
    private final static String SELECT_ALL_TEMPLATE = "select * from %s";
    private final static String SELECT_BY_ID_TEMPLATE = "select %s from %s where %s = ?";
    private final static String INSERT_TEMPLATE = "insert into %s(%s) values (%s)";
    private final static String UPDATE_TEMPLATE = "update %s set %s where %s = ?";

    private final String selectAll;
    private final String selectById;
    private final String insert;
    private final String update;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> entityClassMetaData) {
        String tableName = entityClassMetaData.getName().toLowerCase();

        selectAll = String.format(SELECT_ALL_TEMPLATE, tableName);

        String fieldNameList =
                entityClassMetaData.getAllFields().stream().map(Field::getName).collect(Collectors.joining(","));
        selectById = String.format(SELECT_BY_ID_TEMPLATE, fieldNameList, tableName, entityClassMetaData.getIdField().getName());

        String fieldWithoutIdNameList =
                entityClassMetaData.getFieldsWithoutId().stream().map(Field::getName).collect(Collectors.joining(","));
        String valuesList = "?" + ",?".repeat(entityClassMetaData.getFieldsWithoutId().size() - 1);
        insert = String.format(INSERT_TEMPLATE, tableName, fieldWithoutIdNameList, valuesList);

        String updatedNameList =
                entityClassMetaData.getFieldsWithoutId().stream()
                        .map(field -> field.getName() + "=?")
                        .collect(Collectors.joining(","));
        update = String.format(UPDATE_TEMPLATE, tableName, updatedNameList, entityClassMetaData.getIdField().getName());
    }

    @Override
    public String getSelectAllSql() {
        return selectAll;
    }

    @Override
    public String getSelectByIdSql() {
        return selectById;
    }

    @Override
    public String getInsertSql() {
        return insert;
    }

    @Override
    public String getUpdateSql() {
        return update;
    }
}
