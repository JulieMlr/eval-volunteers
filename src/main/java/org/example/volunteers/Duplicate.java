package org.example.volunteers;

import org.example.utils.Levenshtein;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Duplicate {

    public Duplicate() {

    }

    public static List<List<Volunteer>> triByName(List<Volunteer> volunteers) {
        List<List<Volunteer>> listOfVolunteer =  new ArrayList<>();

        for (int i = 0; i < volunteers.size() ; i++) {
            List<Volunteer> newVolunteers = new ArrayList<>();
            newVolunteers.add(new Volunteer (volunteers.get(i).firstName, volunteers.get(i).lastName, volunteers.get(i).nickName, volunteers.get(i).eMail, volunteers.get(i).phone));
            for (int j = 0; j < volunteers.size(); j++) {
                if(volunteers.get(i).lastName.toUpperCase(Locale.ROOT).equals(volunteers.get(j).lastName.toUpperCase(Locale.ROOT))&& volunteers.get(i).firstName.toUpperCase(Locale.ROOT).equals(volunteers.get(j).firstName.toUpperCase(Locale.ROOT))&& !volunteers.get(i).firstName.equals("") &&!volunteers.get(i).lastName.equals("")&& i!=j)
                {
                    newVolunteers.add(new Volunteer (volunteers.get(j).firstName, volunteers.get(j).lastName, volunteers.get(j).nickName, volunteers.get(j).eMail, volunteers.get(j).phone));
                    volunteers.remove(j);
                }
            }
            listOfVolunteer.add(newVolunteers);
        }
        return listOfVolunteer;
    }


    public static List<Volunteer> regroupByName(List<Volunteer> volunteers) {
        List<List<Volunteer>> listOfVolunteer = triByName(volunteers);
        List<Volunteer> newVolunteers = new ArrayList<>();

        for (int i = 0; i < listOfVolunteer.size(); i++) {
            if (listOfVolunteer.get(i).size() > 1) {
                String email = listOfVolunteer.get(i).get(0).eMail;
                String phone = listOfVolunteer.get(i).get(0).phone;
                String nickname = listOfVolunteer.get(i).get(0).nickName;
                for (int j = 1; j < listOfVolunteer.get(i).size();  j++) {
                    if(!listOfVolunteer.get(i).get(j).eMail.equals("") ) {
                        email = email + " " + listOfVolunteer.get(i).get(j).eMail;
                    }
                    if(!listOfVolunteer.get(i).get(j).phone.equals("")) {
                        phone = phone + " " + listOfVolunteer.get(i).get(j).phone;
                    }
                    if(!listOfVolunteer.get(i).get(j).nickName.equals("")) {
                        nickname = nickname + " " + listOfVolunteer.get(i).get(j).nickName;
                    }
                }
                newVolunteers.add(new Volunteer(listOfVolunteer.get(i).get(0).firstName, listOfVolunteer.get(i).get(0).lastName, nickname, email, phone));
            }
            else {
                newVolunteers.add(new Volunteer(listOfVolunteer.get(i).get(0).firstName, listOfVolunteer.get(i).get(0).lastName, listOfVolunteer.get(i).get(0).nickName, listOfVolunteer.get(i).get(0).eMail, listOfVolunteer.get(i).get(0).phone));
            }
        }
        return newVolunteers;
    }

    public static List<Volunteer> duplicateByLevenshtein(List<Volunteer> volunteers)
    {
        List<Volunteer> newVolunteers = new ArrayList<>();

        for (int i = 0; i < volunteers.size(); i++)
        {
            Volunteer v1 = volunteers.get(i);
            for (int j = i + 1; j < volunteers.size(); j++)
            {
                Volunteer v2 = volunteers.get(j);

                // check first name and last name similarities and phone numbers
                if (
                        Levenshtein.isSimilar(v1.firstName.toUpperCase(), v2.firstName.toUpperCase(), Volunteer.SIMILARITY_ACCEPTANCE) &&
                                Levenshtein.isSimilar(v1.lastName.toUpperCase(), v2.lastName.toUpperCase(), Volunteer.SIMILARITY_ACCEPTANCE) &&
                                v1.parsedPhoneNumber().equals(v2.parsedPhoneNumber())
                )
                {
                    if(v1.eMail.equals("") && !v2.eMail.equals("")) {
                        v1.eMail = v2.eMail;
                    }
                    if(!v1.eMail.equals("") && !v2.eMail.equals("")) {
                        if (!v1.eMail.equals(v2.eMail)) {
                            v1.eMail = v1.eMail + " " + v2.eMail;
                        }
                    }
                    if(v1.nickName.equals("") && !v2.nickName.equals("")) {
                        v1.nickName = v2.nickName;
                    }
                    volunteers.remove(v2);
                    newVolunteers.add(v1);
                    break;
                }
                else // not the same
                {
                    newVolunteers.add(v1);
                }
            }
        }
        return newVolunteers;
    }
}
