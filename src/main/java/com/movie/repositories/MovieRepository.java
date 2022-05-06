package com.movie.repositories;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.movie.exception.ApplicationException;
import com.movie.models.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class MovieRepository {

    private static Logger logger = LoggerFactory.getLogger(MovieRepository.class);

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    public Movie save(Movie movie) {
        logger.info("save movie " + this.getClass().getName());
        dynamoDBMapper.save(movie);
        return movie;
    }

    public List<Movie> saveAll(List<Movie> movieList) {
        logger.info("Uploading movie from CSV to Database " + this.getClass().getName());
        dynamoDBMapper.batchSave(movieList);
        return movieList;
    }

    public Movie findById(String id) {
        logger.info("find movie by id" + this.getClass().getName());
        return dynamoDBMapper.load(Movie.class, id);
    }

    public List<Movie> findAll() {
        logger.info("findAll movies " + this.getClass().getName());
        return dynamoDBMapper.scan(Movie.class, new DynamoDBScanExpression());
    }

    public String update(String id, Movie movie) {
        logger.info("update movie " + this.getClass().getName());

        try {
            dynamoDBMapper.save(movie,
                    new DynamoDBSaveExpression()
                            .withExpectedEntry("id",
                                    new ExpectedAttributeValue(
                                            new AttributeValue().withS(id)
                                    )));
            return id;
        } catch (Exception e) {
            throw new ApplicationException(e.getMessage());
        }
    }

    public String deleteAll() {
        logger.info("Delete All from Database - Repository");
        AmazonDynamoDBClient client = new AmazonDynamoDBClient(new ProfileCredentialsProvider());
        client.setRegion(Region.getRegion(Regions.US_EAST_1));
        DynamoDB dynamoDB = new DynamoDB(client);

        Table table = dynamoDB.getTable("Movie");
        table.delete();
        return "Deleted Successfully";
    }

    public String delete(String id) {
        logger.info("Deleting a movie " + this.getClass().getName());
        Movie movie = dynamoDBMapper.load(Movie.class, id);
        dynamoDBMapper.delete(movie);
        return "Movie deleted successfully:: " + id;
    }


    public List<Movie> getDataByDirectorAndYear(String director, String startYear, String endYear) {

        logger.info("Data based on director & Year Range" + this.getClass().getName());

        try {

            HashMap<String, AttributeValue> condition = new HashMap<>();

            condition.put(":v1", new AttributeValue().withS(director));
            condition.put(":v2", new AttributeValue().withS(String.valueOf(startYear)));
            condition.put(":v3", new AttributeValue().withS(String.valueOf(endYear)));

            DynamoDBScanExpression expression = new DynamoDBScanExpression()
                    .withFilterExpression("director = :v1 and yearOfRelease >= :v2 and yearOfRelease <= :v3")
                    .withExpressionAttributeValues(condition);

            List<Movie> dbResult = dynamoDBMapper.scan(Movie.class, expression);

            logger.info("List of movies received DynamoDb {}", dbResult.size());
            return dbResult;
        } catch (Exception e) {
            throw new ApplicationException(e.getMessage());
        }
    }

    public List<Movie> getDataByReviewInEnglishLanguage(String userReview) {
        logger.info("Data based on user review & english language" + this.getClass().getName());

        try {
            String lang = "English";
            HashMap<String, AttributeValue> condition = new HashMap<>();

            condition.put(":v1", new AttributeValue().withS(lang));
            condition.put(":v2", new AttributeValue().withS(String.valueOf(userReview)));

            DynamoDBScanExpression expression = new DynamoDBScanExpression()
                    .withFilterExpression("lang = :v1 and userReview > :v2")
                    .withExpressionAttributeValues(condition);

            List<Movie> dbResult = dynamoDBMapper.scan(Movie.class, expression);

            logger.info("List of movies received DynamoDb {}", dbResult.size());

            return dbResult;
        } catch (Exception e) {
            throw new ApplicationException(e.getMessage());
        }
    }

    public List<Movie> getHighestBudgetTitles(String country, String year) {
        logger.info("Highest Budget Titles based on Country & Year" + this.getClass().getName());

        try {
            HashMap<String, AttributeValue> condition = new HashMap<String, AttributeValue>();
            condition.put(":v1", new AttributeValue().withS(country));
            condition.put(":v2", new AttributeValue().withS(year));
            DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                    .withFilterExpression("country = :v1 and yearOfRelease = :v2")
                    .withExpressionAttributeValues(condition);

            List<Movie> movieList = dynamoDBMapper.scan(Movie.class, scanExpression);

            logger.debug("Highest budget movie title using user country and year");
            return movieList;
//                .stream().max(Comparator.comparing(Movie::getBudget)).get().getTitle();
        } catch (Exception e) {
            throw new ApplicationException(e.getMessage());
        }
    }
}