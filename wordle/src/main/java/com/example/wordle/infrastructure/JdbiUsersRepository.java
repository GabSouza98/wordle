package com.example.wordle.infrastructure;

import com.example.wordle.repository.UsersRepository;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.locator.UseClasspathSqlLocator;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.springframework.stereotype.Repository;

@Repository
@UseClasspathSqlLocator
public interface JdbiUsersRepository extends UsersRepository {

    @Override
    @SqlUpdate
    void saveUuid(@Bind String uuid);

    @Override
    @SqlQuery
    Long getUserIdByUuid(@Bind String uuid);

}
