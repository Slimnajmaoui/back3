package com.numeryx.AuthorizationServiceApplication.utilities;

import com.numeryx.AuthorizationServiceApplication.exception.Reason;
import com.numeryx.AuthorizationServiceApplication.exception.UsernameNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import static com.numeryx.AuthorizationServiceApplication.utilities.Constant.USER_NOT_FOUND_EXCEPTION;


/**
 * this class contains all static method for the project
 */
public class Methods {

    private Methods() {
    }

    public static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

    public static String getJWTToken() {
        if (SecurityContextHolder.getContext()
                .getAuthentication() != null) {
            Object details =
                    SecurityContextHolder.getContext()
                            .getAuthentication()
                            .getDetails();
            if (details instanceof OAuth2AuthenticationDetails) {
                OAuth2AuthenticationDetails castedDetails = (OAuth2AuthenticationDetails) details;
                return castedDetails
                        .getTokenType() + ' ' + castedDetails.getTokenValue();
            }
        }
        throw new UnauthorizedUserException("Invalid User Credentials");
    }

    public static boolean isValidDate(String dateStr, String dateFormat) {
        DateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    public static List<String> getIdentificationFromRequest(String identification) {
        List<String> identifications = Arrays.asList(identification.split(","));
        if (identifications.size() != 2) {
            throw new UsernameNotFoundException("User not found", Arrays.asList(new Reason(UsernameNotFoundException.ReasonCode.USER_NOT_FOUND, USER_NOT_FOUND_EXCEPTION)));
        }
        return identifications;
    }
}
