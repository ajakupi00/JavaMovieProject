/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.parsers.rss;

import hr.algebra.utils.FileUtils;
import hr.algebra.factory.ParserFactory;
import hr.algebra.factory.UrlConnectionFactory;
import hr.algebra.models.Artist;
import hr.algebra.models.Genre;
import hr.algebra.models.Movie;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import org.jsoup.Jsoup;

/**
 *
 * @author ajakupi
 */
public class MovieParser {

    private static final String RSS_URL = "https://www.blitz-cinestar.hr/rss.aspx?najava=1";
    private static final String ATTRIBUTE_URL = "url";
    private static final String EXT = ".jpg";
    private static final String DIR = "assets";

    private static final int DIRECTOR_ID = 2;
    private static final int ACTOR_ID = 1;

    private MovieParser() {
    }

    public static List<Movie> parse() throws IOException, XMLStreamException, Exception {
        List<Movie> movies = new ArrayList<>();
        HttpURLConnection con = UrlConnectionFactory.getHttpUrlConnection(RSS_URL);
        try (InputStream is = con.getInputStream()) {
            XMLEventReader reader = ParserFactory.createStaxParser(is);
            Optional<TagType> tagType = Optional.empty();
            Movie movie = null;
            StartElement startElement = null;
            while (reader.hasNext()) {

                XMLEvent event = reader.nextEvent();
                switch (event.getEventType()) {
                    case XMLStreamConstants.START_ELEMENT:
                        startElement = event.asStartElement();
                        String qName = startElement.getName().getLocalPart();
                        tagType = TagType.from(qName);
                        if (tagType.isPresent() && tagType.get().equals(TagType.ITEM)) {
                            movie = new Movie();
                            movies.add(movie);
                        }
                        break;
                    case XMLStreamConstants.CHARACTERS:
                        if (tagType.isPresent() && movie != null) {
                            Characters characters = event.asCharacters();
                            String data = characters.getData().trim();
                            switch (tagType.get()) {
                                case TITLE:
                                    if (!data.isEmpty()) {
                                        movie.setTitle(data);
                                    }
                                    break;
                                case DESCRIPTION:
                                    if (!data.isEmpty()) {
                                        String desc = Jsoup.parse(data).text();
                                        movie.setDescripiton(desc);
                                    }
                                    break;
                                case PLAKAT:
                                    if (data != null && movie.getPosterURL() == null) {
                                        handlePicture(movie, data);
                                    }
                                    break;
                                case PUB_DATE:
                                    if (!data.isEmpty()) {
                                        LocalDateTime publishedDate = LocalDateTime.parse(data, DateTimeFormatter.RFC_1123_DATE_TIME);
                                        publishedDate.format(Movie.DATE_FORMATTER);
                                        movie.setPublishedDate(publishedDate);
                                    }
                                    break;
                                case REDATELJ:
                                    if (!data.isEmpty()) {
                                        List<Artist> arr = Artist.parseArtistFromData(data, DIRECTOR_ID);
                                        arr.forEach(movie.artists::add);
                                    }
                                    break;
                                case GLUMCI:
                                    if (!data.isEmpty()) {
                                        List<Artist> arr = Artist.parseArtistFromData(data, ACTOR_ID);
                                        arr.forEach(movie.artists::add);
                                    }
                                    break;
                                case TRAJANJE:
                                    if (!data.isEmpty()) {
                                        movie.setDuration(Integer.parseInt(data));
                                    }
                                    break;
                                case ZANR:
                                    if (!data.isEmpty()) {
                                        String[] genre = data.split(",");
                                        for (int i = 0; i < genre.length; i++) {
                                            movie.genres.add(new Genre(genre[i]));
                                        }
                                    }
                                    break;

                                case POCETAK:
                                    if (!data.isEmpty()) {
                                        movie.setBeginDate(data);

                                    }
                                    break;
                                /*default:
                                    throw new AssertionError(tagType.get().name());*/
                            }
                        }
                        break;
                }
            }
        }
        return movies;

    }

    private static void handlePicture(Movie movie, String pictureUrl) {

        try {
            String ext = pictureUrl.substring(pictureUrl.lastIndexOf("."));
            if (ext.length() > 4) {
                ext = EXT;
            }
            String pictureName = UUID.randomUUID() + ext;
            String localPicturePath = DIR + File.separator + pictureName;

            FileUtils.copyFromUrl(pictureUrl, localPicturePath);
            movie.setPosterURL(localPicturePath);
        } catch (IOException ex) {
            Logger.getLogger(MovieParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private enum TagType {

        ITEM("item"),
        TITLE("title"),
        PUB_DATE("pubDate"),
        DESCRIPTION("description"),
        REDATELJ("redatelj"),
        GLUMCI("glumci"),
        TRAJANJE("trajanje"),
        ZANR("zanr"),
        PLAKAT("plakat"),
        POCETAK("pocetak");

        private final String name;

        private TagType(String name) {
            this.name = name;
        }

        private static Optional<TagType> from(String name) {
            for (TagType value : values()) {
                if (value.name.equals(name)) {
                    return Optional.of(value);
                }
            }
            return Optional.empty();
        }
    }

}
