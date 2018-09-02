package za.ac.tut.travel_guide.util;

import java.util.StringTokenizer;

/**
 * Created by Thulani on 2017/10/13.
 */

public class email {
    private static String email,place_id;

    public static String getPlace_id() {
        return place_id;
    }

    public static void setPlace_id(String place) {
        place_id = place;
    }


    public static String getEmail() {
        return email;
    }

    public static void setEmail(String emal) {
        email = emal;
    }
}
