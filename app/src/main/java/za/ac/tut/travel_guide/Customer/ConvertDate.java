/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.ac.tut.travel_guide.Customer;

/**
 *
 * @author Tshepo
 */
public class ConvertDate {
        public static java.sql.Date convertUtilDateToSqlDate(java.util.Date date){
        if(date != null) 
        {
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            return sqlDate;
        }
        return null;
    }
    
}
