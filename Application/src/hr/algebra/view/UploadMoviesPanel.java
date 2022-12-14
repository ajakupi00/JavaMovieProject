/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.view;

import hr.algebra.Dashboard;
import hr.algebra.dal.Repository;
import hr.algebra.dal.RepositoryFactory;
import hr.algebra.models.Artist;
import hr.algebra.models.Genre;
import hr.algebra.models.Movie;
import hr.algebra.models.MovieArchive;
import hr.algebra.parsers.rss.MovieParser;
import hr.algebra.utils.JAXBUtils;
import hr.algebra.utils.MessageUtils;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.xml.bind.JAXBException;

/**
 *
 * @author dnlbe
 */
public class UploadMoviesPanel extends javax.swing.JPanel {

    private DefaultListModel<Movie> moviesModel;
    private Repository repository;
    private String FILENAME = "moviesarchive.xml";

    /**
     * Creates new form UploadArticlesPanel
     */
    public UploadMoviesPanel() {
        initComponents();
        init();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnUploadArticles = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        lsMovies = new javax.swing.JList<>();
        btnDelete = new javax.swing.JButton();

        btnUploadArticles.setText("Upload Movies");
        btnUploadArticles.setActionCommand("Upload articles");
        btnUploadArticles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUploadArticlesActionPerformed(evt);
            }
        });

        jScrollPane1.setViewportView(lsMovies);

        btnDelete.setBackground(new java.awt.Color(255, 51, 51));
        btnDelete.setText("Delete");
        btnDelete.setActionCommand("Upload articles");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1183, Short.MAX_VALUE)
                    .addComponent(btnUploadArticles, javax.swing.GroupLayout.DEFAULT_SIZE, 1183, Short.MAX_VALUE)
                    .addComponent(btnDelete, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1183, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 665, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnUploadArticles, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnUploadArticlesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUploadArticlesActionPerformed
        try {
            List<Movie> movies = MovieParser.parse();
            List<Artist> artists = new ArrayList<>();
            List<Genre> genres = new ArrayList<>();
            Instant start = Instant.now();
            for (Movie movie : movies) {
                int movieID = repository.createMovie(movie);
                movie.setId(movieID);

                for (Artist artist : movie.getArtists()) {
                    if (!artists.contains(artist)) {
                        int id = repository.createArtist(artist);
                        artist.setId(id);

                        artists.add(artist);
                    } else {
                        int id = repository.findArtist(artist);
                        artist.setId(id);
                    }
                    repository.addArtistForMovie(artist, movie);
                }

                for (Genre genre : movie.getGenres()) {
                    if (!genres.contains(genre)) {
                        int id = repository.createGenre(genre);
                        genre.setId(id);
                        genres.add(genre);
                    } else {
                        int id = repository.findGenre(genre);
                        genre.setId(id);
                    }

                    repository.addGenreForMovie(genre, movie);
                }
            }

            loadModel();
            Dashboard.MOVIES_UPDATED = true;
            try {
                JAXBUtils.save(new MovieArchive(movies), FILENAME);
                 MessageUtils.showInformationMessage("Info", "Movies saved");
            } catch (JAXBException ex) {
                Logger.getLogger(UploadMoviesPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception ex) {
            MessageUtils.showErrorMessage("Unrecoverable error", "Unable to upload movies");
            System.exit(1);
        }

       
    }//GEN-LAST:event_btnUploadArticlesActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        try {
            repository.deleteAllMovies();
            loadModel();
        } catch (Exception ex) {
            Logger.getLogger(UploadMoviesPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnDeleteActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnUploadArticles;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList<Movie> lsMovies;
    // End of variables declaration//GEN-END:variables

    private void init() {
        try {
            repository = RepositoryFactory.getRepository();
            moviesModel = new DefaultListModel<>();
            loadModel();
        } catch (Exception ex) {
            Logger.getLogger(UploadMoviesPanel.class.getName()).log(Level.SEVERE, null, ex);
            MessageUtils.showErrorMessage("Unrecoverable error", "Cannot initiate the form");
            System.exit(1);
        }
    }

    private void loadModel() throws Exception {
        try {
            List<Movie> movies = repository.getMovies();
            moviesModel.clear();
            movies.forEach(moviesModel::addElement);
            lsMovies.setModel(moviesModel);
        } catch (Exception e) {
            MessageUtils.showErrorMessage("Unrecoverable error", e.getLocalizedMessage());
        }

    }

}
