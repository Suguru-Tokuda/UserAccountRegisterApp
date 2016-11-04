/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Sneh Vyas
 */
public class Profile {

    private String firstName;
    private String lastName;
    private String userID;
    private String password;
    private String confirmPassword;
    private String email;
    private String securityQuestion;
    private String securityAnswer;
    private String message;
    private String outputMsg;
    private boolean loggedin;
    
    
    
    //Constructor
    public Profile() {
    }
    
    public Profile(String firstName, String lastName, String userID, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userID = userID;
        this.password = password;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public String getUserID() {
        return userID;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }

    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    /**
     * @return the outputMsg
     */
    public String getOutputMsg() {
        return outputMsg;
    }

    /**
     * @param outputMsg the outputMsg to set
     */
    public void setOutputMsg(String outputMsg) {
        this.outputMsg = outputMsg;
    }

    /**
     * @return the loggedin
     */
    public boolean isLoggedin() {
        return loggedin;
    }

    /**
     * @param loggedin the loggedin to set
     */
    public void setLoggedin(boolean loggedin) {
        this.loggedin = loggedin;
    }
}
