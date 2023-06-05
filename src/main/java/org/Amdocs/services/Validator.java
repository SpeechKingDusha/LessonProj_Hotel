package org.Amdocs.services;

import org.Amdocs.models.PeopleInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Validator {

    private static final String REGEXPHONENUMBER = "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7}$";
    private static final String REGEXEMAIL = "\\A[^@]+@([^@\\.]+\\.)+[^@\\.]+\\z";
    private static final String REGEXDATE = "(0?[1-9]|[12][0-9]|3[01])-(0?[1-9]|1[012])-((19|20)\\d\\d)";
    private static final Map<ColumnsEnum, String> messErrorOfValid = new HashMap<>();

    static {
        messErrorOfValid.put(ColumnsEnum.CONTACT_NO, "The contact number can only contain numbers and a plus sign. Example: +79991118866 \n");
        messErrorOfValid.put(ColumnsEnum.CHECK_IN, "CheckIn is not in the correct format. (DD:MM:YYYY) \n");
        messErrorOfValid.put(ColumnsEnum.CHECK_OUT, "CheckOut is not in the correct format. (DD:MM:YYYY) \n");
        messErrorOfValid.put(ColumnsEnum.E_MAIL_ID, "The email is not in the correct format. Example: andreysp@amdocs.com. \n");
        messErrorOfValid.put(ColumnsEnum.ANY, "These fields are empty or invalid:\n");
        messErrorOfValid.put(ColumnsEnum.SUM_GUESTS, "The SumGuests field must be an integer and greater than 0.\n");
    }

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

    public static Validator validate(PeopleInfo people, String[] columnsName) {
        Validator validator = new Validator();
        validator.brokenFields = new ArrayList<>();
        StringBuilder message = new StringBuilder();
        validator.isGood = true;
        if (people.getGuestName().isEmpty()) {
            validator.isGood = false;
            validator.brokenFields.add(columnsName[1]);
        }
        if (people.getContactNo().isEmpty()) {
            validator.isGood = false;
            validator.brokenFields.add(columnsName[2]);
        } else if (!people.getContactNo().matches(REGEXPHONENUMBER)) {
            validator.isGood = false;
            validator.brokenFields.add(columnsName[2]);
            message.append(messErrorOfValid.get(ColumnsEnum.CONTACT_NO));
        }
        if (!validateDate(people.getCheckIn())) {
            validator.isGood = false;
            validator.brokenFields.add(columnsName[4]);
            //message.append("CheckIn is incorrect format. (DD:MM:YYYY) \n");
            message.append(messErrorOfValid.get(ColumnsEnum.CHECK_IN));
        }
        if (!validateDate(people.getCheckOut())) {
            validator.isGood = false;
            validator.brokenFields.add(columnsName[5]);
            message.append(messErrorOfValid.get(ColumnsEnum.CHECK_OUT));
        }
        if (people.getEmailID().isEmpty()) {
            validator.isGood = false;
            validator.brokenFields.add(columnsName[6]);
        } else if (!people.getEmailID().matches(REGEXEMAIL)) {
            validator.isGood = false;
            validator.brokenFields.add(columnsName[6]);
            message.append(messErrorOfValid.get(ColumnsEnum.E_MAIL_ID));
        }
        if (people.getSumGuests() == null || people.getSumGuests() < 1) {
            validator.isGood = false;
            message.append(messErrorOfValid.get(ColumnsEnum.SUM_GUESTS));
            validator.brokenFields.add(columnsName[7]);
        }
        if (validator.brokenFields.size() > 0) {
            int i = 0;
            message.append(messErrorOfValid.get(ColumnsEnum.ANY));
            for (String empty : validator.brokenFields) {
                i++;
                message.append(i + ".) " + empty + "\n");
            }
        }
        validator.message = message.toString();
        return validator;
    }

    private static boolean validateDate(String str) {
        if (str == null) return false;
        if (!str.matches(REGEXDATE)) return false;
        return true;
    }

    enum ColumnsEnum {
        GUEST_NAME,
        CONTACT_NO,
        ROOM,
        CHECK_IN,
        CHECK_OUT,
        E_MAIL_ID,
        SUM_GUESTS,
        ANY
    }
}
