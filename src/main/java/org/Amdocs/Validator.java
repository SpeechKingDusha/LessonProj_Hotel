package org.Amdocs;

import java.util.ArrayList;

public class Validator {

    private static final String REGEXPHONENUMBER = "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7}$";
    private static final String REGEXEMAIL = "\\A[^@]+@([^@\\.]+\\.)+[^@\\.]+\\z";
    private boolean isGood;
    private ArrayList<String> brokenFields;
    private String message;

    public boolean isGood() {
        return isGood;
    }

    public void setGood(boolean good) {
        isGood = good;
    }

    public ArrayList<String> getBrokenFields() {
        return brokenFields;
    }

    public void setBrokenFields(ArrayList<String> brokenFields) {
        this.brokenFields = brokenFields;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static Validator validate(PeopleInfo people, String[] columnsName){
        Validator validator = new Validator();
        validator.brokenFields = new ArrayList<>();
        StringBuilder message = new StringBuilder();
        validator.isGood=true;
        if (people.getGuestName().isEmpty()){
            validator.isGood=false;
            validator.brokenFields.add(columnsName[1]);
        }
        if (people.getContactNo().isEmpty()){
            validator.isGood=false;
            validator.brokenFields.add(columnsName[2]);
        }
        else if (!people.getContactNo().matches(REGEXPHONENUMBER)){
               validator.isGood=false;
               validator.brokenFields.add(columnsName[2]);
               message.append("Contact number must contain only number. Example: +79991118866 \n");
            };

        if (people.getEmailID().isEmpty()){
            validator.isGood=false;
            validator.brokenFields.add(columnsName[6]);
        }
        else if (!people.getEmailID().matches(REGEXEMAIL)){
            validator.isGood=false;
            validator.brokenFields.add(columnsName[6]);
            message.append("Email is not correct. Example: andreysp@amdocs.com \n");
        }
        if (people.getSumGuests() == null || people.getSumGuests() < 1 ){
            validator.isGood=false;
            message.append("SumGuests field must be > 0. \n");
            validator.brokenFields.add(columnsName[7]);
        }
        if (validator.brokenFields.size() > 0){
            int i = 0;
            message.append("These fields are empty or incorrect:\n");
            for (String empty: validator.brokenFields){
                i++;
                message.append(i +".) " + empty+"\n");
            }
        }
        validator.message = message.toString();
        return  validator;
    }
}
