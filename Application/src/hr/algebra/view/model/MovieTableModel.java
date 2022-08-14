package hr.algebra.view.model;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import hr.algebra.models.Artist;
import hr.algebra.models.Movie;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Maja
 */
public class MovieTableModel extends AbstractTableModel {

    private List<Movie> movies;

    private static final int DIRECTOR_ID = 2;
    private static final int ACTOR_ID = 1;
    private static final String[] COLUMN_NAMES = {
        "Id",
        "Title",
        "Published date",
        "Description",
        "Directors",
        "Actors",
        "Duration",
        "Genres",
        "Picture path",
        "Begin date"
    };

    public MovieTableModel(List<Movie> movies) {
        this.movies = movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    @Override
    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch(columnIndex){
            case 0:
                return Integer.class;
        }
        return super.getColumnClass(columnIndex);
    }

    @Override
    public int getRowCount() {
        return movies.size();
    }

    @Override
    public int getColumnCount() {
        return Movie.class.getDeclaredFields().length - 1;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
         switch(columnIndex){
            case 0:
                return movies.get(rowIndex).getId();
            case 1:
                return movies.get(rowIndex).getTitle();
            case 2:
                return movies.get(rowIndex).getPublishedDate();
            case 3:
                return movies.get(rowIndex).getDescripiton();
            case 4:
                return artists(movies.get(rowIndex).getArtists(), DIRECTOR_ID);
            case 5:
                return artists(movies.get(rowIndex).getArtists(), ACTOR_ID);
            case 6:
                return movies.get(rowIndex).getDuration();
            case 7:
                return movies.get(rowIndex).getGenres();
            case 8:
                return movies.get(rowIndex).getPosterURL();
            case 9:
                return movies.get(rowIndex).getBeginDate();
            default:
                throw new RuntimeException("No such column");
        
        }
    }



    private List<Artist> artists(List<Artist> artists, int roleID) {
        List<Artist> a = new ArrayList<>();
        for (Artist artist : artists) {
            if(artist.getRole().getId()== roleID){
                a.add(artist);
                }
        }
        
        return a;
    }

}
