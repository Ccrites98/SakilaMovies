package com.pluralsight;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class DataManager {
    private final DataSource dataSource;
    public DataManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    public List<Actor> searchActorsByName(String firstName, String lastName) {
        List<Actor> actors = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT actor_id, first_name, last_name FROM actor WHERE first_name = ? AND last_name = ?"
             )) {
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int actorId = resultSet.getInt("actor_id");
                    String actorFirstName = resultSet.getString("first_name");
                    String actorLastName = resultSet.getString("last_name");

                    Actor actor = new Actor(actorId, actorFirstName, actorLastName);
                    actors.add(actor);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return actors;
    }
    public List<Film> getFilmsByActorId(int actorId) {
        List<Film> films = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT film_id, title, description, release_year, length " +
                             "FROM film " +
                             "JOIN film_actor ON film.film_id = film_actor.film_id " +
                             "WHERE film_actor.actor_id = ?"
             )) {
            preparedStatement.setInt(1, actorId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int filmId = resultSet.getInt("film_id");
                    String title = resultSet.getString("title");
                    String description = resultSet.getString("description");
                    int releaseYear = resultSet.getInt("release_year");
                    int length = resultSet.getInt("length");

                    Film film = new Film(filmId, title, description, releaseYear, length);
                    films.add(film);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return films;
    }
}