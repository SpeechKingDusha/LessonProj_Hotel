package org.Amdocs;

import java.util.ArrayList;

public class Validator {

    private boolean isGood;
    private ArrayList<String> emptyFields;
    private String message;

    public boolean isGood() {
        return isGood;
    }

    public void setGood(boolean good) {
        isGood = good;
    }

    public ArrayList<String>  getEmptyFields() {
        return emptyFields;
    }

    public void setEmptyFields(ArrayList<String>  emptyFields) {
        this.emptyFields = emptyFields;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static Validator validate(PeopleInfo people, String[] columnsName){
        Validator validator = new Validator();
        validator.emptyFields = new ArrayList<>();
        StringBuilder message = new StringBuilder();
        validator.isGood=true;
        if (people.getGuestName().isEmpty()){
            validator.isGood=false;
            validator.emptyFields.add(columnsName[1]);
        }
        if (people.getContactNo().isEmpty()){
            validator.isGood=false;
            validator.emptyFields.add(columnsName[2]);
        }
        if (people.getEmailID().isEmpty()){
            validator.isGood=false;
            validator.emptyFields.add(columnsName[6]);
        }
        if (people.getSumGuests() < 1){
            validator.isGood=false;
            message.append("SumGuests field must be > 0. \n");
            validator.emptyFields.add(columnsName[7]);
        }
        if (validator.emptyFields.size() > 0){
            int i = 0;
            message.append("This fields are empty:\n");
            for (String empty: validator.emptyFields){
                i++;
                message.append(i +".)" + empty+"\n");
            }
        }
        validator.message = message.toString();
        return  validator;
    }
}
