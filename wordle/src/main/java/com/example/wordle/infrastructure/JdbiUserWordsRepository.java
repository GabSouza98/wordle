package com.example.wordle.infrastructure;

import com.example.wordle.domain.Histogram;
import com.example.wordle.repository.UserWordsRepository;
import com.example.wordle.rowMapper.HistogramRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.locator.UseClasspathSqlLocator;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.statement.UseRowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@UseClasspathSqlLocator
public interface JdbiUserWordsRepository extends UserWordsRepository {

    @Override
    @SqlUpdate
    void save(@Bind Long wordId, @Bind Long userId, @Bind Integer attempts);

    @Override
    @SqlQuery
    Long getTotalGames(@Bind Long userId);

    @Override
    @SqlQuery
    Long getTotalWins(@Bind Long userId);

    @Override
    @SqlQuery
    @UseRowMapper(HistogramRowMapper.class)
    List<Histogram> getHistogram(@Bind Long userId);

}
