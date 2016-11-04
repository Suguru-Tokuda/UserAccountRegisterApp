/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import model.Profile;
import dao.ProfileDAO;
import JavaMail.JavaMailApp;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.context.FacesContext;

/**
 *
 * @author Sneh Vyas & Suguru Tokuda
 */
@ManagedBean
@SessionScoped
public class ProfileController {

    Profile profile;
    ProfileDAO profileDAO = new ProfileDAO();
    private boolean Mailed;
    private String Return;
    private String response;
    private String usercheck;
    private String passwordcheck;
    private String message;
    private String userResult = "";
    private String testAjax = "";
    private String userNameId = "";

    public void setUserNameId(String userNameId) {
        this.userNameId = userNameId;
    }

    public String getUserNameId() {
        return userNameId;
    }

    public void setUserResult(String userResult) {
        this.userResult = userResult;
    }

    public void setTestAjax(String testAjax) {
        this.testAjax = testAjax;
    }

    public String getTestAjax() {
//        ProfileDAO profileDAO = new ProfileDAO();
        try {
            userResult = profileDAO.checkUserExistence(profile);
        } catch (Exception r) {
            r.printStackTrace();
        }
        return testAjax;
    }

    public String getUserResult() throws SQLException {
        System.out.println("GGGGGGFDFFFF");
//        ProfileDAO aSignUpDAO = new ProfileDAO();
        userResult = profileDAO.checkUserExistence(profile);
        return userResult;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setPasswordcheck(String passwordcheck) {
        this.passwordcheck = passwordcheck;
    }

    public String getPasswordcheck() {
        return passwordcheck;
    }

    public void setUsercheck(String usercheck) {
        this.usercheck = usercheck;
    }

    public String getUsercheck() {
        return usercheck;
    }

    public String UserIDC(Profile signUpProfile) {
//        ProfileDAO profileDAO = new ProfileDAO();
        String pass = signUpProfile.getConfirmPassword();
        String conPass = signUpProfile.getPassword();
        if (conPass.equals(pass)) {
            if (!signUpProfile.getUserID().isEmpty()) {
                boolean ucheck = profileDAO.Usercheck(signUpProfile);
                if (ucheck == true) {
                    usercheck = "User ID is invalid";
                }
            } else {
                usercheck = "";
            }
        } else {
            usercheck = "Password Don't match with confirm password";
        }
        return usercheck;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        String resultStr = "";
        resultStr += "Hello " + profile.getFirstName() + " " + profile.getLastName() + ",<br/>";
        resultStr += "You have Succesfully Signed up With UserID :" + profile.getUserID() + ".<br/> " + "You will recieve a confirmation mail." + "<br/>";
        resultStr += "You have SignedUp with Email :" + profile.getEmail() + ".<br/>";
        resultStr += "Your Security Question is :" + profile.getSecurityQuestion();
        resultStr += "with answer : " + profile.getSecurityAnswer();
        response = resultStr;
        return response;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public ProfileController() {
        profile = new Profile();

    }

    //Beginning of login methods
    public String login() throws SQLException {
        String retVal = null;
        int counter = profileDAO.login(profile);

        if (counter == 1) {
            retVal = "LoginGood.xhtml";
        } else if (counter == 2) {
            retVal = "LoginBad.xhtml";
        } else if (counter == 3) {
            retVal = "Login.xhtml";
        }
        return retVal;
    }

    public Profile getUserInfo() throws SQLException {
        Profile retVal = null;
        retVal = profileDAO.getProfile(profile.getUserID());
        return retVal;
    }
    //End of login methods

    public String createProfile() {
//        ProfileDAO profileDAO = new ProfileDAO();
        JavaMailApp mailing = new JavaMailApp();
        String ucheck = UserIDC(profile);

        if (ucheck == null) {
            // Creating a new object each time.
            int rowCount = profileDAO.createProfile(profile, "Create"); // Doing anything with the object after this?
            if (rowCount == 1) {
                Return = "echo.xhtml";
                Mailed = mailing.mail(profile);
            }// navigate to "response.xhtml"
            else {
                Return = "error.xhtml";
            }
        } else {
            Return = "signUp.xhtml";
            usercheck = ucheck;
        }
        return Return;
    }

    public String updateProfile() {
        String retVal = null;
        int rowCount = profileDAO.createProfile(profile, "Update");
        if (rowCount == 1) {
            retVal = "LoginGood.xhtml";
        }
        return retVal;
    }

    public String retrieveProfile() throws SQLException {
        if (profile == null) {
            Return = "Login.xhtml";
        } else {
//            ProfileDAO profileDAO = new ProfileDAO();    // Creating a new object each time.
            profile = profileDAO.findByUID(profile); // Doing anything with the object after this?
            // if multiple found, just pick the 1st one. If none?
            if (profile != null) {
                Return = "Update.xhtml"; // navigate to "update2.xhtml"
            } else {
                Return = "LoginBad.xhtml";
            }
        }
        return Return;
    }

    public void onload() {
        if (profile == null || profile.getUserID() == null) {
            FacesContext fc = FacesContext.getCurrentInstance();
            ConfigurableNavigationHandler nav = (ConfigurableNavigationHandler) fc.getApplication().getNavigationHandler();
            nav.performNavigation("Login?faces-redirect=true");
        }
    }
}