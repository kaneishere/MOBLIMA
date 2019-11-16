package model.account;

import model.Model;
import model.transaction.Booking;

import java.util.ArrayList;

/*This is the customer class where it would consist the information of the customers such as
    - customer name
    - mobile number
    - email address
 */
public class Customer implements Model {
    private String userName;
    private String password;
    private String mobileNumber;
    private String emailAddress;
    //    private int mobileNumber;
    private ArrayList<Booking> bookingHistory = new ArrayList<Booking>();

    public Customer(String userName, String password, String mobileNumber, String emailAddress) {
        /**This constructor for Customer
         * @param username    The username of the customer
         * @param password         The password of the customer
         * @param mobileNumber  The mobileNumber of the customer
         * @param emailAddress  The emailAddress of the customer
         */
        this.userName = userName;
        this.password = password;
        this.mobileNumber = mobileNumber;
        this.emailAddress = emailAddress;
    }

    public String getUserName() {
        /**This method is used to obtain the username
         * @return the username of the customer
         */
        return userName;
    }

    public void setUserName(String userName) {
        /**This method is used to set the username
         * @param the username of the customer
         */
        this.userName = userName;
    }

    public String getPassword() {
        /**This method is used to obtain the password
         * @return the password of the customer
         */
        return password;
    }

    public void setPassword(String password) {
        /**This method is used to set the password
         * @param the password of the customer
         */
        this.password = password;
    }

    public String getMobileNumber() {
        /**This method is used to obtain the mobile number
         * @return the mobile number of the customer
         */
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        /**This method is used to set the mobile number
         * @param the mobile number of the customer
         */
        this.mobileNumber = mobileNumber;
    }

    public String getEmailAddress() {
        /**This method is used to obtain the email
         * @return the email of the customer
         */
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        /**This method is used to set the email
         * @param the email of the customer
         */
        this.emailAddress = emailAddress;
    }

    public void addBookingHistory(Booking booking) {
        /**This method will add the bookings into the customer's booking history for the customer to view later in the
         * future
         * @param booking           The booking to be added into the customer's booking history
         */
        bookingHistory.add(booking);
    }

    public ArrayList<Booking> getBookingHistory() {
        /**This method will get the bookings into the customer's booking history
         * @return The customer's booking history
         */
        return bookingHistory;
    }

}
