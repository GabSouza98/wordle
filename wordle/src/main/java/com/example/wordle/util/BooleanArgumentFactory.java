package com.example.wordle.util;

import org.jdbi.v3.core.argument.Argument;
import org.jdbi.v3.core.argument.ArgumentFactory;
import org.jdbi.v3.core.config.ConfigRegistry;

import java.lang.reflect.Type;
import java.util.Optional;

public class BooleanArgumentFactory implements ArgumentFactory {

    private static final String FLAG_YES = "S";
    private static final String FLAG_NO = "NO";

    private static final Argument YES = (position, statement, ctx) -> statement.setString(position, FLAG_YES);
    private static final Argument NO = (position, statement, ctx) -> statement.setString(position, FLAG_NO);

    @Override
    public Optional<Argument> build(Type type, Object value, ConfigRegistry config) {
        if(Boolean.TRUE.equals(value)) {
            return Optional.of(YES);
        } else if (Boolean.FALSE.equals(value)) {
            return Optional.of(NO);
        } else {
            return Optional.empty();
        }

    }

}
