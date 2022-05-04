package com.movie.services;

import com.movie.models.Movie;
import com.movie.repositories.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    private static Logger logger = LoggerFactory.getLogger(MovieService.class);

    @Autowired
    private MovieRepository movieRepository;

    public Movie save(Movie movie){
        logger.info("save movie " + this.getClass().getName());
        return movieRepository.save(movie);
    }

    public List<Movie> saveAll(List<Movie> movieList){
        logger.info("save movies " + this.getClass().getName());
        return  movieRepository.saveAll(movieList);
    }

    public Movie findById(String id){
        logger.info("find movie by id" + this.getClass().getName());
        return movieRepository.findById(id);
    }

    public List<Movie> findAll(){
        logger.info("findAll movies " + this.getClass().getName());
        return movieRepository.findAll();
    }

    public String update(String id, Movie movie){
        logger.info("update movie " + this.getClass().getName());
        return movieRepository.update(id, movie);
    }

    public String delete(String id){
        logger.info("Edit Configurations… movie " + this.getClass().getName());
        return movieRepository.delete(id);
    }

}
