package com.pluralsight;
import org.apache.commons.dbcp2.BasicDataSource;
import javax.sql.DataSource;
import java.util.List;
import java.util.Scanner;
public class Main {
    private static final String JDBC_URL = "jdbc:mysql://127.0.0.1:3306/sakila";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password1";
    private static final DataSource dataSource = setupDataSource();
    private static final DataManager dataManager = new DataManager(dataSource);
    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);

        System.out.println("Search for actors by name:");
        System.out.print("Enter the first name of an actor: ");
        String firstName = keyboard.nextLine();

        System.out.print("Enter the last name of an actor: ");
        String lastName = keyboard.nextLine();

        List<Actor> actors = dataManager.searchActorsByName(firstName, lastName);
        if (!actors.isEmpty()) {
            System.out.println("Actors found:");
            for (Actor actor : actors) {
                System.out.println(actor.getFirstName() + " " + actor.getLastName() +
                        " (ID: " + actor.getActorId() + ")");
            }
            System.out.println("\nEnter an actor ID to see the movies they're in:");

            try {
                int actorId = Integer.parseInt(keyboard.nextLine());
                List<Film> films = dataManager.getFilmsByActorId(actorId);
                if (!films.isEmpty()) {
                    System.out.println("Movies featuring actor with ID " + actorId + ":");
                    for (Film film : films) {
                        System.out.println(film.getTitle() + " (" + film.getReleaseYear() + ")");
                    }
                } else {
                    System.out.println("No movies found for actor with ID " + actorId);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid actor ID.");
            }
        } else {
            System.out.println("No actors found with the specified name.");
        }
        keyboard.close();
    }
    private static DataSource setupDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(JDBC_URL);
        dataSource.setUsername(DB_USER);
        dataSource.setPassword(DB_PASSWORD);
        dataSource.setMinIdle(5);
        dataSource.setMaxIdle(10);
        dataSource.setMaxOpenPreparedStatements(100);
        return dataSource;
    }
}