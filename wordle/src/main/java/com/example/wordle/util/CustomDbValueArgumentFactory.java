package com.example.wordle.util;

import org.jdbi.v3.core.argument.Argument;
import org.jdbi.v3.core.argument.ArgumentFactory;
import org.jdbi.v3.core.config.ConfigRegistry;

import java.lang.reflect.Type;
import java.sql.Types;
import java.util.Optional;

public class CustomDbValueArgumentFactory implements ArgumentFactory {

    @Override
    public Optional<Argument> build(Type type, Object value, ConfigRegistry config) {
        if (!(value instanceof CustomDbValue)) {
            return Optional.empty();
        }
        var dbValue = ((CustomDbValue) value).getDbValue();
        return Optional.of((position, statement, ctx) -> {
            if(dbValue == null) {
                statement.setNull(position, Types.VARCHAR);
            } else {
                statement.setString(position, dbValue);
            }
        });
    }
}
