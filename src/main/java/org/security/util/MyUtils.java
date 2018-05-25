package org.security.util;
import java.security.MessageDigest;
public class MyUtils {
    public static final String ADMIN_AUTH = "hasAnyAuthority('ROLE_ADMIN')";
    public static final String USER_AUTH = "hasAnyAuthority('ROLE_USER')";

    public static final String USER_ADMIN_AUTH = "hasAnyAuthority('ROLE_USER','ROLE_ADMIN')";
    public static final String PERMITALL = "permitAll";

    public static String getMD5(String inStr) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {

            e.printStackTrace();
            return "";
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }

        byte[] md5Bytes = md5.digest(byteArray);

        StringBuffer hexValue = new StringBuffer();

        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }

        return hexValue.toString();
    }
}
