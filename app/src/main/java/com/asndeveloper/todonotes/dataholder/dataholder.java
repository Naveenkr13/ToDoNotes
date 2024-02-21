package com.asndeveloper.todonotes.dataholder;

public class dataholder {
String NameRegi, ProfilePic ,EmailRegi,PasswordRegi;

    public dataholder(String nameRegi,  String emailRegi, String passwordRegi) {
        NameRegi = nameRegi;

        EmailRegi = emailRegi;
        PasswordRegi = passwordRegi;
    }

    public String getNameRegi() {
        return NameRegi;
    }

    public void setNameRegi(String nameRegi) {
        NameRegi = nameRegi;
    }


    public String getEmailRegi() {
        return EmailRegi;
    }

    public void setEmailRegi(String emailRegi) {
        EmailRegi = emailRegi;
    }

    public String getPasswordRegi() {
        return PasswordRegi;
    }

    public void setPasswordRegi(String passwordRegi) {
        PasswordRegi = passwordRegi;
    }
}
