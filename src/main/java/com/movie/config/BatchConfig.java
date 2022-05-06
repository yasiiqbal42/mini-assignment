//package com.movie.config;
//
//import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
//import com.amazonaws.services.dynamodbv2.document.DynamoDB;
//import com.movie.models.Movie;
//
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.slf4j.Logger;
//import com.movie.services.MovieService;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
//import org.springframework.batch.core.launch.support.RunIdIncrementer;
//import org.springframework.batch.item.ItemWriter;
//import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
//import org.springframework.batch.item.database.ItemPreparedStatementSetter;
//import org.springframework.batch.item.database.JdbcBatchItemWriter;
//import org.springframework.batch.item.database.JpaItemWriter;
//import org.springframework.batch.item.file.FlatFileItemReader;
//import org.springframework.batch.item.file.LineMapper;
//import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
//import org.springframework.batch.item.file.mapping.DefaultLineMapper;
//import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.web.bind.annotation.RequestBody;
//import com.amazonaws.services.dynamodbv2.document.TableWriteItems;
//
//import javax.sql.DataSource;
//import java.util.List;
//
//@Configuration
//@EnableBatchProcessing
//public class BatchConfig {
//
//    @Autowired
//    private DynamoDBMapper dynamoDBMapper;
//
//    @Autowired
//    private DataSource dataSource;
//
//    @Autowired
//    private MovieService movieService;
//
//    @Autowired
//    private JobBuilderFactory jobBuilderFactory;
//
//    @Autowired
//    private StepBuilderFactory stepBuilderFactory;
//
//    @Bean
//    public FlatFileItemReader<Movie> reader() {
//        FlatFileItemReader<Movie> reader = new FlatFileItemReader<>();
//        reader.setResource(new ClassPathResource("movies.csv"));
//        reader.setLineMapper(getLineMapper());
//        reader.setLinesToSkip(1);
//        return reader;
//    }
//
//    private LineMapper<Movie> getLineMapper() {
//        DefaultLineMapper<Movie> lineMapper = new DefaultLineMapper<>();
//
//        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
//
//        lineTokenizer.setNames(new String[]{"title", "year",
//                "budget", "genre"
//                , "duration", "country", "language"});
//        lineTokenizer.setIncludedFields(new int[]{1, 3, 20, 5, 6, 7, 8});
//
//        BeanWrapperFieldSetMapper<Movie> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
//        fieldSetMapper.setTargetType(Movie.class);
//        lineMapper.setLineTokenizer(lineTokenizer);
//        lineMapper.setFieldSetMapper(fieldSetMapper);
//        return lineMapper;
//
//    }
//
//    @Bean
//    public MovieItemProcessor processor() {
//        return new MovieItemProcessor();
//    }
//
//    @Bean
//    public JdbcBatchItemWriter<Movie> writer() {
//        JdbcBatchItemWriter<Movie> writer = new JdbcBatchItemWriter<>();
//        writer.setItemPreparedStatementSetter(new ItemPreparedStatementSetter<Movie>() {
//            @Override
//            public void setValues(Movie movie, PreparedStatement preparedStatement) throws SQLException {
//                dynamoDBMapper.batchSave(movie);
//            }
//        });
//
//        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Movie>());
//        writer.setSql("insert into Movie(title,year,Budget,genre,duration,country,language) " +
//                "values (:title,:year,:Budget,:genre,:duration,:country,:language)");
//
////        writer.setSql("INSERT into Movie value { 'title':':title','year':':year','Budget':':Budget', 'genre':':genre', 'duration':':duration', 'country':':country', 'language':':language'}");
//
//        writer.setDataSource(this.dataSource);
//        return writer;
//    //need to implement it for Dynamo DB
//}
////    @Bean
////    public void write(final List<? extends List<Movie>> lists) throws Exception {
////        final List<Movie> consolidatedList = new ArrayList<>();
////        for (final List<Movie> list : lists) {
////            consolidatedList.addAll(list);
////        }
////        delegate.write(consolidatedList);
////    }
//
////    public void write(List<? extends List<Movie>> items) {
////        JpaItemWriter<Movie> writer = new JpaItemWriter<>();
////        for(List<Movie> o : items)
////        {
////            writer.write(o);
////        }
////    }
////
////
////
////    public List<Movie> saveAll(List<Movie> movieList){
////        dynamoDBMapper.batchSave(movieList);
////        return movieList;
////    }
//
//
//    @Bean
//    public Job importUserJob() {
//        return this.jobBuilderFactory.get("USER-IMPORT-JOB")
//                .incrementer(new RunIdIncrementer())
//                .flow(step1())
//                .end()
//                .build();
//    }
//
//    @Bean
//    public Step step1() {
//        return this.stepBuilderFactory.get("step1")
//                .<Movie, Movie>chunk(10)
//                .reader(reader())
//                .processor(processor())
//                .writer(writer())
//                .build();
//    }
//}
//
