package com.movie.controller;

import com.movie.exception.ApplicationException;
import com.movie.models.Movie;
import com.movie.services.MovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/movie")
public class MovieController {

    private static Logger logger = LoggerFactory.getLogger(MovieService.class);

    private HttpHeaders setTimeHeader(Instant startTime) {
        long time = Duration.between(startTime, Instant.now()).toMillis();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("X-TIME-TO-EXECUTE", String.valueOf(time));
        return responseHeaders;
    }

    @Autowired
    private MovieService movieService;

    @PostMapping
    public ResponseEntity<Movie> save(@RequestBody Movie movie) {
        logger.info("save movie " + this.getClass().getName());
        Instant start = Instant.now();
        return new ResponseEntity<>(movieService.save(movie),setTimeHeader(start), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> findById(@PathVariable(value = "id") String id) {
        logger.info("find movie by id" + this.getClass().getName());
        Instant start = Instant.now();
        return new ResponseEntity<>(movieService.findById(id),setTimeHeader(start), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Movie>> findAll() {
        logger.info("findAll movies " + this.getClass().getName());
        Instant start = Instant.now();
        return new ResponseEntity<>(movieService.findAll(), setTimeHeader(start), HttpStatus.OK);
    }


    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable(value = "id") String id,
                                         @RequestBody Movie movie) {
        logger.info("update movie " + this.getClass().getName());
        Instant start = Instant.now();
        return new ResponseEntity<>(movieService.update(id, movie),setTimeHeader(start), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable(value = "id") String id) {
        logger.info("Deleting a movie " + this.getClass().getName());
        Instant start = Instant.now();
        return new ResponseEntity<>(movieService.delete(id),setTimeHeader(start), HttpStatus.OK);
    }

    @GetMapping("/upload")
    public ResponseEntity<List<Movie>> load() {
        logger.info("Uploading movie from CSV to Database " + this.getClass().getName());
        Instant start = Instant.now();
        return new ResponseEntity<>(movieService.loadData(),setTimeHeader(start), HttpStatus.OK);
    }

//    @GetMapping("/deleteAll")
//    public ResponseEntity<String> deleteAll() {
//        logger.info("Deleting All movie from Database " + this.getClass().getName());
//        return new ResponseEntity<>(movieService.deleteAll(), HttpStatus.OK);
//    }

    @GetMapping("/director/{director}")
    public ResponseEntity<List<Movie>> getDataByDirectorAndYear(@PathVariable(value = "director") String director,
                                                                @RequestHeader("startYear") String startYear,
                                                                @RequestHeader("endYear") String endYear) {
        logger.info("Data based on director & Year Range" + this.getClass().getName());
        Instant start = Instant.now();
        return new ResponseEntity<>(movieService.getDataByDirectorAndYear(director, startYear, endYear),setTimeHeader(start), HttpStatus.OK);

    }

    @GetMapping("/userReview/{userReview}")
    public ResponseEntity<List<Movie>> getDataByReviewInEnglishLanguage(@PathVariable(value = "userReview") String userReview) {
        logger.info("Data based on user review & english language" + this.getClass().getName());

        Instant start = Instant.now();
        return new ResponseEntity<>(movieService.getDataByReviewInEnglishLanguage(userReview),setTimeHeader(start), HttpStatus.OK);

    }

    @GetMapping("/{country}/{year}")
    public ResponseEntity<List<Movie>> getHighestBudgetTitles(@PathVariable(value = "country") String country,
                                              @PathVariable(value = "year") String year) {
        logger.info("Highest Budget Titles based on Country & Year" + this.getClass().getName());
        Instant start = Instant.now();
        return new ResponseEntity<>(movieService.getHighestBudgetTitles(country, year),setTimeHeader(start), HttpStatus.OK);

    }


}