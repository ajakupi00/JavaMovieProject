/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dal.sql;

import hr.algebra.dal.Repository;
import hr.algebra.models.Artist;
import hr.algebra.models.Genre;
import hr.algebra.models.Movie;
import hr.algebra.models.Role;
import hr.algebra.models.User;
import hr.algebra.models.UserRole;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 *
 * @author dnlbe
 */
public class SqlRepository implements Repository {

    /* GENRE */
    private static final String ID_GENRE = "IDGenre";
    private static final String NAME = "Name";

    private static final String CREATE_GENRE = "{ CALL createGenre (?,?) }";
    private static final String UPDATE_GENRE = "{ CALL updateGenre (?,?) }";
    private static final String DELETE_GENRE = "{ CALL deleteGenre (?) }";
    private static final String SELECT_GENRE = "{ CALL readGenre (?) }";
    private static final String SELECT_GENRES = "{ CALL readGenres }";

    private static final String ADD_GENRE_4_MOVIE = "{ CALL addGenreForMovie (?,?) }";
    private static final String REMOVE_GENRE_4_MOVIE = "{ CALL deleteGenreForMovie (?,?) }";
    private static final String SELECT_GENRES_4_MOVIE = "{ CALL readGenresForMovie (?) }";

    /*  ROLE  */
    private static final String ID_ROLE = "IDRole";

    private static final String SELECT_ROLE = "{ CALL readRole (?) }";

    /*  ARTIST */
    private static final String ID_ARTIST = "IDArtist";

    private static final String CREATE_ARTIST = "{ CALL createArtist (?,?,?) }";
    private static final String UPDATE_ARTIST = "{ CALL updateArtist (?,?) }";
    private static final String DELETE_ARTIST = "{ CALL deleteArtist (?) }";
    private static final String SELECT_ARTIST = "{ CALL readArtist (?) }";
    private static final String SELECT_ARTISTS = "{ CALL readArtists  }";

    private static final String ADD_ARTIST_4_MOVIE = "{ CALL addArtistForMovie (?,?) }";
    private static final String REMOVE_ARTIST_4_MOVIE = "{ CALL deleteArtistForMovie (?,?) }";
    private static final String SELECT_ARTISTS_4_MOVIE = "{ CALL ReadArtistForMovie (?) }";


    /*  MOVIE */
    private static final String ID_MOVIE = "IDMovie";
    private static final String TITLE = "Title";
    private static final String DESCRIPTION = "Description";
    private static final String PUBLISHED_DATE = "PublishedDate";
    private static final String DURATION = "Duration";
    private static final String POSTER = "PosterURL";
    private static final String BEGIN_DATE = "BeginDate";

    private static final String CREATE_MOVIE = "{ CALL createMovie (?,?,?,?,?,?,?) }";
    private static final String UPDATE_MOVIE = "{ CALL updateMovie (?,?,?,?,?,?,?) }";
    private static final String DELETE_MOVIE = "{ CALL deleteMovie (?) }";
    private static final String SELECT_MOVIE = "{ CALL readMovie(?) }";
    private static final String SELECT_MOVIES = "{ CALL readMovies }";

    private static final String DELETE_ALL_MOVIES = "{ CALL deleteAllMovies }";

    /* ID-S */
    private static final String FIND_ARTIST = "{ CALL findArtist (?,?) }";
    private static final String FIND_GENRE = "{ CALL findGENRE (?) }";

    /* USER  */
    private static final String ID_USER = "IDUser";
    private static final String USERNAME = "Username";
    private static final String PASSWORD = "Password";
    
    private static final String AUTH_USER = "{ CALL AuthUser (?,?)}";
    private static final String CREATE_USER = "{ CALL RegisterUser (?,?)}";
    

    @Override
    public List<Genre> getGenres() throws Exception {
        List<Genre> genres = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_GENRES);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                genres.add(new Genre(
                        rs.getInt(ID_GENRE),
                        rs.getString(NAME)));
            }
        }
        return genres;
    }

    @Override
    public Optional<Genre> getGenre(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_GENRE)) {
            stmt.setInt("@" + ID_GENRE, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Genre(
                            rs.getInt(ID_GENRE),
                            rs.getString(NAME)
                    ));
                }
            }
        }
        return Optional.empty();

    }

    @Override
    public int createGenre(Genre genre) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE_GENRE)) {
            stmt.setString("@" + NAME, genre.getName());
            stmt.registerOutParameter("@" + ID_GENRE, Types.INTEGER);

            stmt.executeUpdate();
            return stmt.getInt("@" + ID_GENRE);
        }
    }

    @Override
    public void updateGenre(Genre genre) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(UPDATE_GENRE)) {
            stmt.setInt("@" + ID_GENRE, genre.getId());
            stmt.setString("@" + NAME, genre.getName());

            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteGenre(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(DELETE_GENRE)) {
            stmt.setInt("@" + ID_GENRE, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public void addGenreForMovie(Genre genre, Movie movie) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(ADD_GENRE_4_MOVIE)) {
            stmt.setInt("@" + ID_MOVIE, movie.getId());
            stmt.setInt("@" + ID_GENRE, genre.getId());

            stmt.executeUpdate();
        }
    }

    @Override
    public List<Genre> getGenresForMovie(int id) throws Exception {
        List<Genre> genres = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_GENRES_4_MOVIE)) {
            stmt.setInt("@" + ID_MOVIE, id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    genres.add(new Genre(
                            rs.getInt(ID_GENRE),
                            rs.getString(NAME)
                    ));
                }
            }
        }
        return genres;
    }

    @Override
    public void deleteGenreForMovie(Genre genre, Movie movie) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(REMOVE_GENRE_4_MOVIE)) {
            stmt.setInt("@" + ID_MOVIE, movie.getId());
            stmt.setInt("@" + ID_GENRE, genre.getId());

            stmt.executeUpdate();
        }
    }

    @Override
    public List<Artist> getArtists() throws Exception {
        List<Artist> artists = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_ARTISTS);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                artists.add(new Artist(
                        rs.getInt(ID_ARTIST),
                        rs.getString(NAME),
                        getRole(rs.getInt(ID_ROLE)).orElse(null)));
            }

        }
        return artists;
    }

    @Override
    public Optional<Artist> getArtist(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_ARTIST)) {
            stmt.setInt("@" + ID_ARTIST, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Artist(
                            rs.getInt(ID_ARTIST),
                            rs.getString(NAME),
                            getRole(rs.getInt(ID_ROLE)).orElse(null)
                    ));
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public int createArtist(Artist artist) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE_ARTIST)) {
            stmt.setString("@" + NAME, artist.getName());
            stmt.setInt("@" + ID_ROLE, artist.getRole().getId());
            stmt.registerOutParameter("@" + ID_ARTIST, Types.INTEGER);

            stmt.executeUpdate();
            return stmt.getInt("@" + ID_ARTIST);

        }
    }

    @Override
    public void updateArtist(Artist artist) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(UPDATE_ARTIST)) {
            stmt.setInt("@" + ID_ARTIST, artist.getId());
            stmt.setString("@" + NAME, artist.getName());

            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteArtist(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(DELETE_ARTIST)) {
            stmt.setInt("@" + ID_ARTIST, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public void addArtistForMovie(Artist artist, Movie movie) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(ADD_ARTIST_4_MOVIE)) {
            stmt.setInt("@" + ID_MOVIE, movie.getId());
            stmt.setInt("@" + ID_ARTIST, artist.getId());

            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteArtistForMovie(Artist artist, Movie movie) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(REMOVE_ARTIST_4_MOVIE)) {
            stmt.setInt("@" + ID_MOVIE, movie.getId());
            stmt.setInt("@" + ID_ARTIST, artist.getId());

            stmt.executeUpdate();
        }
    }

    @Override
    public List<Artist> getArtistsForMovie(int id) throws Exception {
        List<Artist> artists = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_ARTISTS_4_MOVIE)) {
            stmt.setInt("@" + ID_MOVIE, id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    artists.add(new Artist(
                            rs.getInt(ID_ARTIST),
                            rs.getString(NAME),
                            getRole(rs.getInt(ID_ROLE)).orElse(null)
                    ));
                }
            }
        }
        return artists;
    }

    @Override
    public List<Movie> getMovies() throws Exception {
        List<Movie> movies = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_MOVIES);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Movie movie = new Movie(
                        rs.getInt(ID_MOVIE),
                        rs.getString(TITLE),
                        rs.getString(DESCRIPTION),
                        LocalDateTime.parse(rs.getString(PUBLISHED_DATE).replace(' ', 'T'), Movie.DATE_FORMATTER),
                        rs.getInt(DURATION),
                        rs.getString(POSTER),
                        rs.getString(BEGIN_DATE));
                movie.setGenres(getGenresForMovie(movie.getId()));
                movie.setArtists(getArtistsForMovie(movie.getId()));

                movies.add(movie);
            }

        }

        return movies;
    }

    @Override
    public Optional<Movie> getMovie(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_MOVIE)) {
            stmt.setInt("@" + ID_MOVIE, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    final LocalDateTime pubDate = LocalDateTime.parse(rs.getString(PUBLISHED_DATE).replace(' ', 'T'), Movie.DATE_FORMATTER);
                    return Optional.of(new Movie(
                            rs.getInt(ID_MOVIE),
                            rs.getString(TITLE),
                            rs.getString(DESCRIPTION), pubDate,
                            rs.getInt(DURATION),
                            rs.getString(POSTER),
                            rs.getString(BEGIN_DATE),
                            getGenresForMovie(rs.getInt(ID_MOVIE)),
                            getArtistsForMovie(rs.getInt(ID_MOVIE))
                    ));
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public int createMovie(Movie movie) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE_MOVIE)) {
            stmt.setString("@" + TITLE, movie.getTitle());
            stmt.setString("@" + DESCRIPTION, movie.getDescripiton());
            stmt.setString("@" + PUBLISHED_DATE, movie.getPublishedDate().format(Movie.DATE_FORMATTER));
            stmt.setInt("@" + DURATION, movie.getDuration());
            stmt.setString("@" + POSTER, movie.getPosterURL());
            stmt.setString("@" + BEGIN_DATE, movie.getBeginDate());
            stmt.registerOutParameter("@" + ID_MOVIE, Types.INTEGER);
            stmt.executeUpdate();
            return stmt.getInt("@" + ID_MOVIE);

        }
    }

    @Override
    public void updateMovie(Movie movie) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(UPDATE_MOVIE)) {
            stmt.setInt("@" + ID_MOVIE, movie.getId());
            stmt.setString("@" + TITLE, movie.getTitle());
            stmt.setString("@" + DESCRIPTION, movie.getDescripiton());
            stmt.setString("@" + PUBLISHED_DATE, movie.getPublishedDate().format(Movie.DATE_FORMATTER));
            stmt.setInt("@" + DURATION, movie.getDuration());
            stmt.setString("@" + POSTER, movie.getPosterURL());
            stmt.setString("@" + BEGIN_DATE, movie.getBeginDate());

            stmt.executeUpdate();

        }
    }

    @Override
    public void deleteMovie(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(DELETE_MOVIE)) {
            stmt.setInt("@" + ID_MOVIE, id);

            stmt.executeUpdate();
        }

    }

    @Override
    public void deleteAllMovies() throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(DELETE_ALL_MOVIES)) {

            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<Role> getRole(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_ROLE)) {
            stmt.setInt("@" + ID_ROLE, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Role(
                            rs.getInt(ID_ROLE),
                            rs.getString(NAME)
                    ));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public int findArtist(Artist artist) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(FIND_ARTIST)) {
            stmt.setString("@" + NAME, artist.getName());
            stmt.setInt("@" + ID_ROLE, artist.getRole().getId());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Artist rsArtist = new Artist(
                            rs.getInt(ID_ARTIST),
                            rs.getString(NAME),
                            getRole(rs.getInt(ID_ROLE)).orElse(null));
                    return rsArtist.getId();
                }
            }
        }
        throw new Exception("Artist not found!");
    }

    @Override
    public int findGenre(Genre genre) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(FIND_GENRE)) {
            stmt.setString("@" + NAME, genre.getName());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Genre rsGenre = new Genre(
                            rs.getInt(ID_GENRE),
                            rs.getString(NAME));
                    return rsGenre.getId();
                }
            }
        }
        throw new Exception("Genre not found!");
    }

    @Override
    public Optional<User> AuthUser(String username, String password) {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(AUTH_USER)) {
            stmt.setString("@" + USERNAME, username);
            stmt.setString("@" + PASSWORD, password);

            try(ResultSet rs = stmt.executeQuery()){
                if(rs.next()){
                    return Optional.of(new User
                        (rs.getInt(ID_USER),
                         rs.getString(USERNAME),
                         rs.getString(PASSWORD),
                         getUserRole(rs.getInt("RoleID")))       
                        );
                }
            } catch (Exception ex) {
                Logger.getLogger(SqlRepository.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SqlRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Optional.empty();

    }

    private UserRole getUserRole(int roleID) throws Exception {
        switch(roleID){
            case 1: 
               return new UserRole(roleID, "admin");
            case 2:
                return new UserRole(roleID, "user");
            default:
                throw new Exception("No such role!");
        }
                
    }

    @Override
    public void CreateUser(String username, String password) {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE_USER)) {
            stmt.setString("@" + USERNAME, username);
            stmt.setString("@" + PASSWORD, password);

            stmt.executeUpdate();
           

        } catch (SQLException ex) {
            Logger.getLogger(SqlRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
