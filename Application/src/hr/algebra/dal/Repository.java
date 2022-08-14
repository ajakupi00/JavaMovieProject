/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dal;

import hr.algebra.models.Artist;
import hr.algebra.models.Genre;
import hr.algebra.models.Movie;
import hr.algebra.models.Role;
import hr.algebra.models.User;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author arjan
 */
public interface Repository {

    /* GENRES */
    
    
    List<Genre> getGenres() throws Exception;
    Optional<Genre> getGenre(int id) throws Exception;
    int createGenre(Genre genre) throws Exception;
    void updateGenre(Genre genre) throws Exception;
    void deleteGenre(int id) throws Exception;

    
    void addGenreForMovie(Genre genre, Movie movie) throws Exception;
    void deleteGenreForMovie(Genre genre, Movie movie) throws Exception;
    List<Genre> getGenresForMovie(int id) throws Exception;
    
    /* ARTIST */
    
    List<Artist> getArtists() throws Exception;
    Optional<Artist> getArtist(int id) throws Exception;
    int createArtist(Artist artist) throws Exception;
    void updateArtist(Artist artist) throws Exception;
    void deleteArtist(int id) throws Exception;
    
    void addArtistForMovie(Artist artist, Movie movie) throws Exception;
    void deleteArtistForMovie(Artist artist, Movie movie) throws Exception;
    List<Artist> getArtistsForMovie(int id) throws Exception;
    
    Optional<Role> getRole(int id) throws Exception;
    
    /* MOVIE */
    
    List<Movie> getMovies() throws Exception;
    Optional<Movie> getMovie(int id) throws Exception;
    int createMovie(Movie movie) throws Exception;
    void updateMovie(Movie movie) throws Exception;
    void deleteMovie(int id) throws Exception;
    void deleteAllMovies() throws Exception;
    
    
    /* ID-S */
    
    int findArtist(Artist artist) throws Exception;
    int findGenre(Genre genre) throws Exception;
    
    
    /* USER */
    
    Optional<User> AuthUser(String username, String password);
    void CreateUser(String username, String password);
    
}
