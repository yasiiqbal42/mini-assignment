package com.movie.services;

import com.movie.exception.ApplicationException;
import com.movie.models.Movie;
import com.movie.repositories.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MovieService {

    private static Logger logger = LoggerFactory.getLogger(MovieService.class);

    @Autowired
    private MovieRepository movieRepository;

    public Movie save(Movie movie) {
        logger.info("save movie " + this.getClass().getName());
        return movieRepository.save(movie);
    }

    public String deleteAll() {
        logger.info("Delete All movie - Service layer " + this.getClass().getName());
        return movieRepository.deleteAll();
    }

    public List<Movie> saveAll(List<Movie> movieList) {
        logger.info("Uploading movie from CSV to Database " + this.getClass().getName());
        return movieRepository.saveAll(movieList);
    }


    public List<Movie> loadData() {
        List<Movie> movieList = new ArrayList<>();

        logger.info("Loading Process Started " + this.getClass().getName());
        try {
            Movie movie;
            logger.info("Reading the CSV file from directory");
            BufferedReader br = new BufferedReader(new FileReader("src/main/resources/movies.csv"));
            String line = "";
            while ((line = br.readLine()) != null)   //returns a Boolean value
            {
                logger.info("Splitting the records based on regex");

                String[] movie_row = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);// use comma as separator
                movie = new Movie(movie_row[1], movie_row[3], movie_row[9], movie_row[16], movie_row[20], movie_row[5], movie_row[6], movie_row[7], movie_row[8]);
                movieList.add(movie);
            }

            logger.info("Data Mapped successfully from csv to Model");
            return movieRepository.saveAll(movieList);
//            return ResponseEntity.status(HttpStatus.OK).body("Movies table updated in DB");
        } catch (FileNotFoundException e) {
            throw new ApplicationException(e.getMessage());
        } catch (IOException e) {
            throw new ApplicationException(e.getMessage());
        } catch (Exception e){
            throw new ApplicationException(e.getMessage());
        }
    }


    public Movie findById(String id) {
        logger.info("find movie by id" + this.getClass().getName());
        return movieRepository.findById(id);
    }

    public List<Movie> findAll() {
        logger.info("findAll movies " + this.getClass().getName());
        return movieRepository.findAll();
    }

    public String update(String id, Movie movie) {
        logger.info("update movie " + this.getClass().getName());
        return movieRepository.update(id, movie);
    }

    public String delete(String id) {
        logger.info("Deleting a movie" + this.getClass().getName());
        return movieRepository.delete(id);
    }

    public List<Movie> getDataByDirectorAndYear(String director, String startYear, String endYear) {
        logger.info("Data based on director & Year Range" + this.getClass().getName());
        return movieRepository.getDataByDirectorAndYear(director, startYear, endYear);
    }

    public List<Movie> getDataByReviewInEnglishLanguage(String userReview) {
        logger.info("Data based on user review & english language" + this.getClass().getName());
        return movieRepository.getDataByReviewInEnglishLanguage(userReview);
    }

    public List<Movie> getHighestBudgetTitles(String country, String year) {
        logger.info("Highest Budget Titles based on Country & Year" + this.getClass().getName());
        return movieRepository.getHighestBudgetTitles(country,year);
    }
}
