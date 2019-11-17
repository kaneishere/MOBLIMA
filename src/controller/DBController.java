package controller;

import model.AgeGroup;
import model.account.Admin;
import model.account.Customer;
import model.cinema.Cinema;
import model.cinema.CinemaType;
import model.cinema.Cineplex;
import model.cinema.ShowTime;
import model.movie.Movie;
import model.movie.MovieEnums;
import model.movie.Review;
import model.transaction.Booking;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

import static service.TicketPriceService.*;

/** This is a DBController (Database Controller) class where it will handle all database related task such as
  - create, read, update and delete
  - add/remove cinema
  - add/remove cineplex
  - add/remove showtimes
  - add/get sales
 **/

public class DBController {
    /**
     * initializes DBController with an instance of the serialized database object
     */
    private SerializedDB serializedDB = SerializedDB.getInstance();
    /**
     * singleton instance of DBController
     */
    private static DBController dbController = null;

    /**
     * private constructor for DBController
     */
    private DBController(){ }

    /**
     * This method is defined to create the .dat file
     *
     * @param DBName The name of the database that wants to be created
     * @throws IOException If the file is not found
     */
    public void createDB(String DBName) throws IOException {

        String dir = "src/database/";
        File myFile = new File(dir +DBName + ".dat");
        myFile.createNewFile();
    }

    /**
     * This method is defined to delete the .dat file
     *
     * @param DBName The name of the database that wants to be delete
     * @throws IOException If the file is not found
     */
    public void deleteDB(String DBName) throws IOException {

        String dir = "src/database/";
        File myFile = new File(dir + DBName + ".dat");
        myFile.delete();
    }

    /**This method is defined to write data to .dat file
     * @param DBName    The name of the database that wants to be update
     * @param serializedDB     A list of stored items to be stored into the .dat file
     * @throws IOException  If the file is not found
     */
    public void updateDB(SerializedDB serializedDB, String DBName) throws IOException {

        String dir = "src/database/";
        File myFile = new File(dir + DBName + ".dat");
        if(!myFile.exists()) {
            myFile.createNewFile();
        }else {
            SerializeDB.writeSerializedObject(dir + DBName + ".dat", serializedDB);
        }
    }

    /**This method is defined to read data from .dat file
     * @param  DBName       The name of the database that wants to be read
     * @return A list of stored items in the .dat file
     * @throws IOException  If the file is not found
     */
    public SerializedDB readDB(String DBName) throws IOException {

        String dir = "src/database/";
        SerializedDB serializedDB = new SerializedDB();
        File myFile = new File(dir + DBName + ".dat");
        if(!myFile.exists()) {
            myFile.createNewFile();
        }else {
            serializedDB = SerializeDB.readSerializedObject(dir + DBName + ".dat");
        }
        return serializedDB;
    }

    /**
     * method to add a Customer object to the serialized data object
     * @param customer
     */
    public void addCustomer(Customer customer) {
        serializedDB.addCustomer(customer);
    }

    /**
     * method to get all Customer objects as an ArrayList from the serialized data
     */
    public ArrayList<Customer> getCustomer() {
        return serializedDB.getCustomers();
    }

    /**
     * method to add a new Admin object to the serialized database
     * @param username
     * @param password
     */
    public void addAdmin(String username, String password) {
        serializedDB.getAdmins().add(new Admin(username, password));
    }

    /**
     * method to get all Admin objects as an ArrayList
     */
    public ArrayList<Admin> getAdmin() {
        return serializedDB.getAdmins();
    }

    /**
     * method to get all Movie objects as an ArrayList
     */
    public ArrayList<Movie> getMovies() {
        return serializedDB.getMovies();
    }

    /**
     * method to get all cineplexes as a hashmap with the Cineplex names as keys and the Cineplex objects as values
     */
    public HashMap<String, Cineplex> getCineplexes() {

        return serializedDB.getCineplexes();
    }

    /**This method is defined to add the cineplex into the temporary database
     * @param cineplex      The selected cineplex object to be added into the database
     */
    public void addCineplex(Cineplex cineplex){

        serializedDB.addCineplexes(cineplex);
    }

    /**This method is defined to remove the cineplex from the temporary database
     * @param name      The name of the cineplex to be removed from the database
     */
    public void removeCineplex(String name){

        HashMap<String, Cineplex> cineplexes = serializedDB.getCineplexes();
        cineplexes.remove(name);
        serializedDB.setCineplexes(cineplexes);
    }

    /**This method is defined to add the cinema into the temporary database
     * @param cineplex      The selected cineplex of the cinema to be added
     * @param cinema        The selected cinema to be added
     */
    public void addCinema(Cineplex cineplex, Cinema cinema){

        HashMap<String, Cineplex> cineplexes = serializedDB.getCineplexes();
        Cineplex selected_cineplex = cineplexes.get(cineplex.getName());
        ArrayList<Cinema> cinema_list= selected_cineplex.getCinemas();
        cinema_list.add(cinema);
        cineplex.setCinemas(cinema_list);
        cineplexes.replace(cineplex.getName(), cineplex);
        serializedDB.setCineplexes(cineplexes);
    }

    /**This method is defined to remove the cinema from the temporary database
     * @param cineplex      The selected cineplex of the cinema to be removed
     * @param cinema        The selected cinema to be removed
     */
    public void removeCinema(Cineplex cineplex, Cinema cinema){

        HashMap<String, Cineplex> cineplexes = serializedDB.getCineplexes();
        Cineplex selected_cineplex = cineplexes.get(cineplex.getName());
        ArrayList<Cinema> cinema_list= selected_cineplex.getCinemas();
        cinema_list.remove(cinema);
        cineplex.setCinemas(cinema_list);
        cineplexes.replace(cineplex.getName(), cineplex);
        serializedDB.setCineplexes(cineplexes);
    }

    /**
     * Method to add a ShowTime object to SerializedDB
     * @param showTime
     */
    public void addShowTimes(ShowTime showTime) {
        serializedDB.getCineplexes().get(showTime.getCineplex().getName()).getShowTimes().get(showTime.getDateOfMovie()).add(showTime);
    }

    /**This method is defined to add the showtime into the temporary database
     * @param cineplex       The cineplex object
     * @param dateOfMovie   The date of the showtime
     * @param newShowTime  new ShowTime object
     */
    public void addShowTimes(Cineplex cineplex, LocalDate dateOfMovie, ShowTime newShowTime){

        try {
            serializedDB.getCineplexes().get(cineplex.getName()).getShowTimes().get(dateOfMovie).add(newShowTime);
        } catch (NullPointerException e) {
            serializedDB.getCineplexes().get(cineplex.getName()).getShowTimes().put(dateOfMovie, new ArrayList<ShowTime>());
            addShowTimes(cineplex, dateOfMovie, newShowTime);
        }
    }

    /**
     * Method to remove the specified ShowTime object from SerializedDB
     * @param oldShowTime
     */
    public void removeShowTimes(ShowTime oldShowTime) {
        removeShowTimes(oldShowTime.getMovie(), oldShowTime.getDateOfMovie(), oldShowTime.getTimeOfMovie(), oldShowTime.getCinema(),
                oldShowTime.getCineplex());
    }

    /**This method is defined to remove the showtime from the temporary database in the system
     * @param movie         The movie object
     * @param dateOfMovie   The date of the showtime
     * @param timeOfMovie   The time of the showtime
     * @param cinema        The cinema that premieres the movie
     * @param cineplex      The cineplex that premieres the movie
     */
    public void removeShowTimes(Movie movie, LocalDate dateOfMovie, LocalTime timeOfMovie, Cinema cinema,
                                Cineplex cineplex){

        ArrayList<ShowTime> showTimes = serializedDB.getCineplexes().get(cineplex.getName()).getShowTimes().get(dateOfMovie);
        for (ShowTime showTime : showTimes) {
            if (showTime.getMovie() == movie && showTime.getTimeOfMovie() == timeOfMovie &&
                    showTime.getCinema() == cinema) {
                serializedDB.getCineplexes().get(cineplex.getName()).getShowTimes().get(dateOfMovie).remove(showTime);
            }
        }
    }

    /**This method is to return an instance of its own
     * @return: An instance of DBController
     */
    public static DBController getInstance()
    {

        if (dbController == null)
            dbController = new DBController();

        return dbController;
    }

    /**This method is defined to load all the data from the database into the system
     * @throws IOException  If the file is not found
     */
    public void load()  {

        DBController dbController = DBController.getInstance();
        try {
            //Retrieve all the data from database to list
            SerializedDB serializedDBObj = dbController.readDB("SerializedDB");

            //Loads all the prices from the data into the system
            serializedDB.setTicketPricing(serializedDBObj.getPublicHolidayDates(), serializedDBObj.getPublicHolidayCharges(), serializedDBObj.getWeekendCharges(), serializedDBObj.getBasePrice());
            serializedDB.setCineplexes(serializedDBObj.getCineplexes());
            serializedDB.setAdmins(serializedDBObj.getAdmins());
            serializedDB.setBookings(serializedDBObj.getBookings());
            serializedDB.setCustomers(serializedDBObj.getCustomers());
            serializedDB.setMovies(serializedDBObj.getMovies());

            try {
                loadEnums(serializedDBObj);
            }catch(Exception e){
                System.out.println("[System: Error in loading enums]");
            }
            setAllPrices();
        } catch(IOException e){
            serializedDB = new SerializedDB();
        }
    }

    /**This method is defined to load the enums to the temporary database where it stores that data when the system
     * is running
     * @param serializedDBObj  The data extracted from the database
     */
    public void loadEnums(SerializedDB serializedDBObj){

        //Declaration of variable
        int i;

        //Loads all the movie type prices into the system
        i = 0;
        for (MovieEnums.MovieType m : MovieEnums.MovieType.values()) {
            m.setTicketPrice((double) serializedDBObj.getMovieType().get(i));
            i++;
        }

        //Loads all the cinema type prices into the system
        i = 0;
        for (CinemaType c : CinemaType.values()) {
            c.setTicketPrice((double) serializedDBObj.getCinemaType().get(i));
            i++;
        }

        //Loads all the discount prices for different citizen of different age group into the system
        i = 0;
        for (AgeGroup group : AgeGroup.values()) {
            group.setTicketPrice((double) serializedDBObj.getAgeGroup().get(i));
            i++;
        }
    }

    /**
     * This method is used to set all the enums in the serializedDB
     */
    public void setEnums(){
        serializedDB.setEnum();
    }

    /**This method is defined to save all the data from the database into the system
     * @throws IOException  If the file is not found
     */
    public void save() {

        DBController dbController = DBController.getInstance();
        try {
            //Saves all the base/holiday/weekend pricing and holiday dates into the database
            dbController.updateDB(serializedDB, "SerializedDB");
        }catch(IOException e){
            try {
                createDB("SerializedDB");
                dbController.updateDB(serializedDB, "SerializedDB");
            }catch (IOException i){
                System.out.println("[System: Error in creating database]");
            }
        }
    }

    /**This method is defined to increment the sales each time based on the number of tickets for each booking
     * @param booking  The booking selected to add to the sales
     */
    public void addSales(Booking booking){

        serializedDB.addSales(booking.getMovie().getTitle(), booking.getTickets().size());
    }

    /**
     * Method to add a review to SerializedDB
     * @param movie
     * @param review
     */
    public void addReview(Movie movie, Review review) {
        serializedDB.addReview(movie, review);
    }

    /**This method is defined to get the array of the sales
     * @return An array list of sales for each movie
     */
    public HashMap<String, Integer> getSales(){

        return serializedDB.getSales();
    }

    /**This method is defined to get the sales for a given movie
     * @param movieName  the movie name used to get the number of sales
     * @return: the number of sales for the specified movie
     */
    public Integer getSalesFigure(String movieName){

        return serializedDB.getSalesFigure(movieName);
    }

    /**
     * This method is defined to confirm the changes of the ticket when exiting the configure system settings panel
     */
    public void commitTicketDetails() {

        setEnums();
        serializedDB.setTicketPricing(getPublicHolidayDates(), getPublicHolidayCharges(), getWeekendCharges(), getBasePrice());
    }

    /**This method is defined to add movies into the temporary database when the application is running
     * @param movie The movie created to be added into the database
     */
    public void addMovies(Movie movie){

        serializedDB.addMovies(movie);
    }

}



