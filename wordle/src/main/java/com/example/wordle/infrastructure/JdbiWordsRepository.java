package com.example.wordle.infrastructure;

import com.example.wordle.repository.WordsRepository;
import org.jdbi.v3.sqlobject.locator.UseClasspathSqlLocator;
import org.jdbi.v3.sqlobject.statement.SqlBatch;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@UseClasspathSqlLocator
public interface JdbiWordsRepository extends WordsRepository {

    @Override
    @SqlBatch
    void saveWordsFromFileToDatabase(List<String> stringList);

    @Override
    @SqlQuery
    List<String> getWords();

    @Override
    @SqlQuery
    String getRandomWord();

    @Override
    @SqlQuery
    Long getWordId(String word);

}
