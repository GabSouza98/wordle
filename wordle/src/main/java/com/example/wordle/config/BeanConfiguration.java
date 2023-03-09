package com.example.wordle.config;

import com.example.wordle.infrastructure.JdbiUserWordsRepository;
import com.example.wordle.infrastructure.JdbiUsersRepository;
import com.example.wordle.infrastructure.JdbiWordsRepository;
import com.example.wordle.repository.UserWordsRepository;
import com.example.wordle.repository.UsersRepository;
import com.example.wordle.repository.WordsRepository;
import com.example.wordle.util.BooleanArgumentFactory;
import com.example.wordle.util.CustomDbValueArgumentFactory;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.spi.JdbiPlugin;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;
import java.util.List;

@Configuration
public class BeanConfiguration {

    @Bean
    public Jdbi jdbi(DataSource ds, List<JdbiPlugin> jdbiPlugins, List<RowMapper<?>> rowMappers) {
        TransactionAwareDataSourceProxy dataSourceProxy = new TransactionAwareDataSourceProxy(ds);
        Jdbi jdbi = Jdbi.create(dataSourceProxy);

        jdbiPlugins.forEach(jdbi::installPlugin);
        rowMappers.forEach(jdbi::registerRowMapper);

        jdbi.registerArgument(new BooleanArgumentFactory());
        jdbi.registerArgument(new CustomDbValueArgumentFactory());
        jdbi.installPlugins();
        return jdbi;
    }

    @Bean
    public JdbiPlugin sqlObjectPlugin() { return new SqlObjectPlugin(); }

    @Bean
    public WordsRepository wordsRepository(Jdbi jdbi) {
        return jdbi.onDemand(JdbiWordsRepository.class);
    }

    @Bean
    public UsersRepository usersRepository(Jdbi jdbi) {
        return jdbi.onDemand(JdbiUsersRepository.class);
    }

    @Bean
    public UserWordsRepository userWordsRepository(Jdbi jdbi) {
        return jdbi.onDemand(JdbiUserWordsRepository.class);
    }

}
