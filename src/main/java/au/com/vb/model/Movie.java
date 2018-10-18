package au.com.vb.model;

import java.util.Objects;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "movie", propOrder = { "imdbId", "title", "genre", "rating", "year", "summary" })
public class Movie {

  protected String imdbId;
  protected String title;
  protected String genre;
  protected String rating;
  protected String year;
  protected String summary;

  public Movie(String imdbId, String title, String genre, String rating, String year, String summary) {
    this.imdbId = imdbId;
    this.title = title;
    this.genre = genre;
    this.rating = rating;
    this.year = year;
    this.summary = summary;
  }

  public Movie() {}

  public String getImdbId() {
    return imdbId;
  }

  public void setImdbId(String imdbId) {
    this.imdbId = imdbId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getGenre() {
    return genre;
  }

  public void setGenre(String genre) {
    this.genre = genre;
  }

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public String getRating() {
    return rating;
  }

  public void setRating(String rating) {
    this.rating = rating;
  }

  public String getYear() {
    return year;
  }

  public void setYear(String year) {
    this.year = year;
  }
  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;

    if (o == null || getClass() != o.getClass())
      return false;

    Movie movie = (Movie) o;

    return Objects.equals(imdbId, movie.imdbId) &&
            Objects.equals(title, movie.title) &&
            Objects.equals(genre, movie.genre);
  }

  @Override
  public int hashCode() {
    return Objects.hash(imdbId, title, genre);
  }

  @Override
  public String toString() {
    return "Movie{" +
            "imdbId='" + imdbId + '\'' +
            ", title='" + title + '\'' +
            ", genre='" + genre + '\'' +
            ", rating='" + rating + '\'' +
            ", year='" + year + '\'' +
            ", summary='" + summary + '\'' +
            '}';
  }
}
