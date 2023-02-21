package ru.otus.library.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.library.mapper.MigrationBookMapper;
import ru.otus.library.model.entity.Book;
import ru.otus.library.model.entity.jpa.BookJpa;
import ru.otus.library.repository.BookRepository;

import javax.persistence.EntityManagerFactory;

@EnableBatchProcessing
@Configuration
public class MigrationConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    private MigrationBookMapper migrationBookMapper;

    private BookRepository bookRepository;

    public MigrationConfig(MigrationBookMapper migrationBookMapper, BookRepository bookRepository) {
        this.migrationBookMapper = migrationBookMapper;
        this.bookRepository = bookRepository;
    }

    @Bean
    JobLauncherTestUtils jobLauncherTestUtils() {
        return new JobLauncherTestUtils();
    }

    @Bean
    public Step step1(JpaPagingItemReader<BookJpa> bookReader, RepositoryItemWriter<Book> mongoBookWriter, ItemProcessor bookProcessor) {
        return stepBuilderFactory.get("step1")
                .chunk(30)
                .reader(bookReader)
                .processor(bookProcessor)
                .writer(mongoBookWriter)
                .build();
    }

    @Bean
    public Job noSqlMigrationJob(Step step1) {
        return jobBuilderFactory.get("noSqlMigrationJob")
                .incrementer(new RunIdIncrementer())
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public JpaPagingItemReader<BookJpa> bookReader(EntityManagerFactory entityManagerFactory) {
        return new JpaPagingItemReaderBuilder<BookJpa>()
                .name("bookReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(30)
                .queryString("select b from BookJpa b")
                .build();
    }

    @Bean
    public ItemProcessor<BookJpa, Book> bookProcessor() {
        return migrationBookMapper::bookJpaToBookMongo;
    }

    @Bean
    public RepositoryItemWriter<Book> mongoBookWriter() {
        return new RepositoryItemWriterBuilder<Book>()
                .repository(bookRepository).methodName("save")
                .build();
    }
}
