/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author arjan
 */

@XmlAccessorType(XmlAccessType.FIELD)
public class Movie {

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    
    private int id;
      @XmlElement(name = "title")
    private String title;
      
      @XmlElement(name = "description")
    private String descripiton;
    
    @XmlJavaTypeAdapter(PublishedDateAdapter.class)
    @XmlElement(name = "publisheddate")
    private LocalDateTime publishedDate;
    
      @XmlElement(name = "duration")
    private int duration;
      
      @XmlElement(name = "posterURL")
    private String posterURL;
      
      @XmlElement(name = "beginDate")
    private String beginDate;

    @XmlElementWrapper
    @XmlElement(name = "genre")
    public List<Genre> genres = new ArrayList<>();
       @XmlElementWrapper
    @XmlElement(name = "artist")
    public List<Artist> artists = new ArrayList<>();

    public Movie(){
    }
    
    public Movie(String title, String descripiton, LocalDateTime publishedDate, int duration, String posterURL, String beginDate) {
        this.title = title;
        this.descripiton = descripiton;
        this.publishedDate = publishedDate;
        this.duration = duration;
        this.posterURL = posterURL;
        this.beginDate = beginDate;
    }

    public Movie(int id, String title, String descripiton, LocalDateTime publishedDate, int duration, String posterURL, String beginDate) {
        this(title, descripiton, publishedDate, duration, posterURL, beginDate);
        this.id = id;
    }

    public Movie(int id, String title, String descripiton, LocalDateTime publishedDate, int duration, String posterURL, String beginDate, List<Genre> genres, List<Artist> artists) {
        this(id,title, descripiton, publishedDate, duration, posterURL, beginDate);
        this.genres = genres;
        this.artists = artists;
    }
    
    

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescripiton(String descripiton) {
        this.descripiton = descripiton;
    }

    public void setPublishedDate(LocalDateTime publishedDate) {
        this.publishedDate = publishedDate;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setPosterURL(String posterURL) {
        this.posterURL = posterURL;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public List<Artist> getArtists() {
        return artists;
    }
    
   

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescripiton() {
        return descripiton;
    }

    public LocalDateTime getPublishedDate() {
        return publishedDate;
    }

    public int getDuration() {
        return duration;
    }

    public String getPosterURL() {
        return posterURL;
    }

    public String getBeginDate() {
        return beginDate;
    }

    @Override
    public String toString() {
        return id + " - " + title;
    }

}
