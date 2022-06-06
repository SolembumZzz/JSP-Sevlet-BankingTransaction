package com.cg.utils;

import com.cg.service.CustomerService;
import com.cg.service.ICustomerService;

public class Validation {
    static ICustomerService customerService = new CustomerService();
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
