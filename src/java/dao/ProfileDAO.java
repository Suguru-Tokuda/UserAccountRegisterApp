package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
//import java.util.ArrayList;
import model.Profile;

/**
 *
 * @author Sneh Vyas & Suguru Tokuda
 */
public class ProfileDAO {

    //to ckeck weather the userid is avalible or not for singup page
    public String checkUserExistence(Profile aProfile) throws SQLException {

        String query = "SELECT * FROM Profile.signup ";
        query += "WHERE userid = '" + aProfile.getUserID() + "'";
        DBHelper.loadDriver("org.apache.derby.jdbc.ClientDriver");
        // if doing the above in Oracle: DBHelper.loadDriver("oracle.jdbc.driver.OracleDriver");
        String myDB = "jdbc:derby://localhost:1527/Profile";
        // if doing the above in Oracle:  String myDB = "jdbc:oracle:thin:@oracle.itk.ilstu.edu:1521:ora478";
        Connection DBConn = DBHelper.connect2DB(myDB, "itkstu", "student");

        Statement stmt = DBConn.createStatement();
        System.out.println("Query " + query);
        ResultSet rs = stmt.executeQuery(query);
        try {
            if (aProfile.getUserID().isEmpty()) {
                return "";
            } else {
                if (rs.next()) {
                    return "username already exists";
                } else {
                    return "username is available";
                }
            }
        } catch (NullPointerException n) {
            return "";
        }
    }

    //To change body of generated methods, choose Tools | Templates.
    // method to check the userid in database for login
    public boolean Usercheck(Profile aProfile) {
        boolean existence = false;
        Connection DBConn = null;
        try {
            //Connecting the database
            DBHelper.loadDriver("org.apache.derby.jdbc.ClientDriver");
            String myDB = "jdbc:derby://localhost:1527/profile";
            DBConn = DBHelper.connect2DB(myDB, "itkstu", "student");

            String query = "SELECT * FROM profile.signup WHERE userid ='" + aProfile.getUserID() + "'";
            Statement stmt = DBConn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                existence = true;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return existence;
    }

    //Suguru
    //Login
    public int login(Profile profile) throws SQLException {
        int retVal = 0;
        String userID = profile.getUserID();
        String pass = this.findPasswordByUserName(userID);

        //Connecting to the database to get the value of "counter"
        String query = "SELECT * FROM Profile.Login WHERE userid = '" + userID + "'";
        DBHelper.loadDriver("org.apache.derby.jdbc.ClientDriver");
        String myDB = "jdbc:derby://localhost:1527/profile";
        Connection DBConn = DBHelper.connect2DB(myDB, "itkstu", "student");
        Statement stmt = DBConn.createStatement();
        int numOfAttempts = 0;
        try {
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                numOfAttempts = rs.getInt("counter");
            }
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(ProfileDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        if ((profile.getPassword().equals(pass)) && (numOfAttempts < 3)) {
            //If the pass words matches and the counter is less than 3, returns true.
            retVal = 1;
            this.resetCounter(userID);
        } else if (!profile.getPassword().equals(pass) && (numOfAttempts < 2)) {
            //If the password doesn't match, increment the counter.
            retVal = 2;
            this.setCounter(userID);
        } else if (numOfAttempts > 3) {
            //If the user fails 3 times to log in, the program changes the outputMsg which is dispalyed in login.xhtml
            retVal = 3;
            profile.setOutputMsg("You have attempted more than 3 times. Talk to the administrator.");
        }
        return retVal;
    }

    public String findPasswordByUserName(String userID) throws SQLException {
        String retVal = null;

        //Connecting to the database to set 
        String query = "SELECT * FROM Profile.Login WHERE userid = '" + userID + "'";
        DBHelper.loadDriver("org.apache.derby.jdbc.ClientDriver");
        String myDB = "jdbc:derby://localhost:1527/profile";
        Connection DBConn = DBHelper.connect2DB(myDB, "itkstu", "student");
        Statement stmt = DBConn.createStatement();
        ResultSet rs;

        try {
            rs = stmt.executeQuery(query);
            if (rs.next()) {
                retVal = rs.getString("password");
            }
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(ProfileDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retVal;
    }

    // This method connects the databsae and increment the value of "counter" in dadtabase.
    public void setCounter(String userID) throws SQLException {
        String query = "UPDATE profile.Login set counter = counter+1 WHERE userid = '" + userID + "'";

        DBHelper.loadDriver("org.apache.derby.jdbc.ClientDriver");
        String myDB = "jdbc:derby://localhost:1527/profile";
        Connection DBConn = DBHelper.connect2DB(myDB, "itkstu", "student");
        Statement stmt = DBConn.createStatement();
        try {
            stmt.executeUpdate(query);
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(ProfileDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //This method reset the the value of counter to "0"
    public void resetCounter(String userID) throws SQLException {
        String query = "UPDATE Profile.Login set counter = 0 WHERE userid = '" + userID + "'";
        DBHelper.loadDriver("org.apache.derby.jdbc.ClientDriver");
        String myDB = "jdbc:derby://localhost:1527/profile";
        Connection DBConn = DBHelper.connect2DB(myDB, "itkstu", "student");
        try {
            Statement stmt = DBConn.createStatement();
            stmt.executeUpdate(query);
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(ProfileDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Suguru 
    //method to block user if 3 failed attempt
    public void resetCounter(Profile aProfile) {

        Connection DBConn = null;
        try {
            DBHelper.loadDriver("org.apache.derby.jdbc.ClientDriver");
            // if doing the above in Oracle: DBHelper.loadDriver("oracle.jdbc.driver.OracleDriver");
            String myDB = "jdbc:derby://localhost:1527/profile";
            // if doing the above in Oracle:  String myDB = "jdbc:oracle:thin:@oracle.itk.ilstu.edu:1521:ora478";
            DBConn = DBHelper.connect2DB(myDB, "itkstu", "student");

            String query = "update profile.Login set counter = counter+1 where userid ='" + aProfile.getUserID() + "'";
            Statement stmt = DBConn.createStatement();
            System.out.println("Query " + query);
            int rs = stmt.executeUpdate(query);

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();

        }

    }

    public Profile getProfile(String userID) throws SQLException {
        Profile retVal = null;

        String query = "SELECT * FROM User.signup WHERE userID = '" + userID + "'";
        DBHelper.loadDriver("org.apache.derby.jbdc.ClientDriver");
        String myDB = "jdbc:derby://localhost:1527/profile";
        Connection DBConn = DBHelper.connect2DB(myDB, "itkstu", "student");
        Statement stmt = DBConn.createStatement();
        String firstName = null;
        String lastName = null;
        String id = null;
        String pass = null;
        try {
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                firstName = rs.getString("firstname");
                lastName = rs.getString("lastname");
                id = rs.getString("userID");
                pass = rs.getString("Password");
            }

            Profile profile = new Profile(firstName, lastName, id, pass);
            retVal = profile;
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(ProfileDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retVal;
    }
     // Suguru -- Login
    
    //This method is used after checking the user already exists.
    public int createProfile(Profile aProfile, String Action) {
        int retVal = 0;
        //Action is either "Create" or "Update"
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }

        int rowCount = 0;
        try {
            String myDB = "jdbc:derby://localhost:1527/profile";
            Connection DBConn = DriverManager.getConnection(myDB, "itkstu", "student");
            Statement stmt = DBConn.createStatement();
            String sinupInsertString = null;
            String loginInsertString = null;
            if (Action.equals("Create")) {
                sinupInsertString = "INSERT INTO Profile.Signup VALUES ('"
                        + aProfile.getFirstName()
                        + "','" + aProfile.getLastName().replaceAll("'", "''").toString()
                        + "','" + aProfile.getUserID().replaceAll("'", "''").toString()
                        + "','" + aProfile.getPassword().replaceAll("'", "''").toString()
                        + "','" + aProfile.getEmail()
                        + "','" + aProfile.getSecurityQuestion().replaceAll("'", "''").toString()
                        + "','" + aProfile.getSecurityAnswer().replaceAll("'", "''").toString()
                        + "')";

                loginInsertString = "INSERT INTO Profile.Login VALUES ('"
                        + aProfile.getUserID()
                        + "','" + aProfile.getPassword()
                        + "',0)";

            } else if (Action.equals("Update")) {
//                sinupInsertString = "UPDATE Profile.Signup SET firstname='" + aProfile.getFirstName()
//                        + "',lastname='" + aProfile.getLastName().replaceAll("'", "''").toString()
//                        + "',password='" + aProfile.getPassword().replaceAll("'", "''").toString()
//                        + "' " + ",email='" + aProfile.getEmail()
//                        + "',securityquestion='" + aProfile.getSecurityQuestion().replaceAll("'", "''").toString()
//                        + "'" + ",securityanswer='" + aProfile.getSecurityAnswer().replaceAll("'", "''").toString()
//                        + "'";
                        sinupInsertString = "UPDATE Profile.Signup SET firstname='" + aProfile.getFirstName()
                        + "',lastname='" + aProfile.getLastName().replaceAll("'", "''").toString()
                        + "',password='" + aProfile.getPassword().replaceAll("'", "''").toString()
                        + "',email='" + aProfile.getEmail().replaceAll("'", "''").toString()
                        + "',securityquestion='" + aProfile.getSecurityQuestion().replaceAll("'", "''").toString()
                        + "',securityanswer='" + aProfile.getSecurityAnswer().replaceAll("'", "''").toString()
                        + "'";
                loginInsertString = "UPDATE profile.Login set userid ='"
                        + aProfile.getUserID() + "',password='"
                        + aProfile.getPassword() + "',counter=0 "
                        + "where userid='" + aProfile.getUserID() + "'";
            }
            rowCount = stmt.executeUpdate(loginInsertString);
            retVal = rowCount;
            rowCount = stmt.executeUpdate(sinupInsertString);
            aProfile.setOutputMsg("Updated information successfully.");
            DBConn.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            String error = e.getMessage();
        }
        // if insert is successful, rowCount will be set to 1 (1 row inserted successfully). Else, insert failed.
        return retVal;
    }

    //this method is used to find the profile by user id to be used for update
    public Profile findByUID(Profile aProfile) throws SQLException {
        String query = "SELECT * FROM Profile.signup WHERE userid = '" + aProfile.getUserID() + "'";
        DBHelper.loadDriver("org.apache.derby.jdbc.ClientDriver");
        String myDB = "jdbc:derby://localhost:1527/profile";
        Connection DBConn = DBHelper.connect2DB(myDB, "itkstu", "student");

        Statement stmt = DBConn.createStatement();
        System.out.println("Query " + query);
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            String variable = rs.getString("firstname");
            aProfile.setFirstName(variable);
            variable = rs.getString("lastname");
            aProfile.setLastName(variable);
            variable = rs.getString("password");
            aProfile.setConfirmPassword(variable);
            variable = rs.getString("password");
            aProfile.setPassword(variable);
            variable = rs.getString("email");
            aProfile.setEmail(variable);
            variable = rs.getString("Userid");
            aProfile.setUserID(variable);
            variable = rs.getString("securityquestion");
            aProfile.setSecurityQuestion(variable);
            variable = rs.getString("securityanswer");
            aProfile.setSecurityAnswer(variable);
        }
        return aProfile;
    }
}