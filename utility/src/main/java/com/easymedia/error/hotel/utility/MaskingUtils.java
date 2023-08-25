package com.easymedia.error.hotel.utility;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MaskingUtils {

    // ID 마스킹(앞4자리 이후 마스킹)
    public static String idMasking(String memberId) {
        if (StringUtils.isBlank(memberId)) {
            return null;
        }
        return memberId.replaceAll("(?<=.{4}).", "*");
    }

    // 이름 가운데 글자 마스킹
    public static String nameMasking(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        // 한글만 (영어, 숫자 포함 이름은 제외)
        String regex = "(^[가-힣]+)$";
        Matcher matcher = Pattern.compile(regex).matcher(name);
        if (matcher.find()) {
            int length = name.length();
            String middleMask = "";
            if (length > 2) {
                middleMask = name.substring(1, length - 1);
            } else {
                // 이름이 외자
                middleMask = name.substring(1, length);
            }
            String dot = "";
            for (int i = 0; i < middleMask.length(); i++) {
                dot += "*";
            }
            if (length > 2) {
                return name.substring(0, 1) + middleMask.replace(middleMask, dot) + name.substring(length - 1, length);
            } else {
                // 이름이 외자 마스킹 리턴
                return name.substring(0, 1) + middleMask.replace(middleMask, dot);
            }
        }
        return name;
    }

    // 휴대폰번호 마스킹(가운데 숫자 4자리 마스킹)
    public static String phoneMasking(String phoneNo) {
        if (StringUtils.isBlank(phoneNo)) {
            return null;
        }
        String regex = "(\\d{2,3})-?(\\d{3,4})-?(\\d{4})$";
        Matcher matcher = Pattern.compile(regex).matcher(phoneNo);
        if (matcher.find()) {
            String target = matcher.group(2);
            int length = target.length();
            char[] c = new char[length];
            Arrays.fill(c, '*');
            return phoneNo.replace(target, String.valueOf(c));
        }
        return phoneNo;
    }

    // 이메일 마스킹(앞3자리 이후 '@'전까지 마스킹)
    public static String emailMasking(String email) {
        if (StringUtils.isBlank(email)) {
            return null;
        }
        String regex = "\\b(\\S+)+@(\\S+.\\S+)";
        Matcher matcher = Pattern.compile(regex).matcher(email);
        if (matcher.find()) {
            // 네글자 초과인 경우 뒤 마스킹 처리
            String target = matcher.group(1);
            int length = target.length();
            if (length > 3) {
                char[] c = new char[length - 3];
                Arrays.fill(c, '*');
                return email.replace(target, target.substring(0, 3) + String.valueOf(c));
            }
        }
        return email;
    }

    // 이메일 마스킹(앞3자리 이후 '@'전까지 마스킹)
    public static String emailMasking(String email, int len) {
        if (StringUtils.isBlank(email)) {
            return null;
        }
        String regex = "\\b(\\S+)+@(\\S+.\\S+)";
        Matcher matcher = Pattern.compile(regex).matcher(email);
        if (matcher.find()) {
            // 네글자 초과인 경우 뒤 마스킹 처리
            String target = matcher.group(1);
            int length = target.length();
            if (length > len) {
                char[] c = new char[length - len];
                Arrays.fill(c, '*');
                return email.replace(target, target.substring(0, len) + String.valueOf(c));
            }
        }
        return email;
    }

    // 계좌번호 마스킹(뒤 5자리)
    public static String accountNoMasking(String accountNo) {
        if (StringUtils.isBlank(accountNo)) {
            return null;
        }
        // 계좌번호는 숫자만 파악하므로
        String regex = "(^[0-9]+)$";
        Matcher matcher = Pattern.compile(regex).matcher(accountNo);
        if (matcher.find()) {
            int length = accountNo.length();
            if (length > 5) {
                char[] c = new char[5];
                Arrays.fill(c, '*');
                return accountNo.replace(accountNo, accountNo.substring(0, length - 5) + String.valueOf(c));
            }
        }
        return accountNo;
    }

    // 생년월일 마스킹(8자리)
    public static String birthMasking(String birthday) {
        if (StringUtils.isBlank(birthday)) {
            return null;
        }
        String regex = "^((19|20)\\d\\d)?([-/.])?(0[1-9]|1[012])([-/.])?(0[1-9]|[12][0-9]|3[01])$";
        Matcher matcher = Pattern.compile(regex).matcher(birthday);
        if (matcher.find()) {
            return birthday.replace("[0-9]", "*");
        }
        return birthday;
    }

    // 카드번호 가운데 8자리 마스킹
    public static String cardMasking(String cardNo) {
        if (StringUtils.isBlank(cardNo)) {
            return null;
        }
        // 카드번호 16자리 또는 15자리 '-'포함/미포함 상관없음
        String regex = "(\\d{4})-?(\\d{4})-?(\\d{4})-?(\\d{3,4})$";
        Matcher matcher = Pattern.compile(regex).matcher(cardNo);
        if (matcher.find()) {
            String target = matcher.group(2) + matcher.group(3);
            int length = target.length();
            char[] c = new char[length];
            Arrays.fill(c, '*');
            return cardNo.replace(target, String.valueOf(c));
        }
        return cardNo;
    }

    // 주소 마스킹(신주소, 구주소, 도로명 주소 숫자만 전부 마스킹)
    public static String addressMasking(String address) {
        if (StringUtils.isBlank(address)) {
            return null;
        }
        // 신(구)주소, 도로명 주소
        String regex = "(([가-힣]+(\\d{1,5}|\\d{1,5}(,|.)\\d{1,5}|)+(읍|면|동|가|리))(^구|)((\\d{1,5}(~|-)\\d{1,5}|\\d{1,5})(가|리|)|))([ ](산(\\d{1,5}(~|-)\\d{1,5}|\\d{1,5}))|)|";
        String newRegx = "(([가-힣]|(\\d{1,5}(~|-)\\d{1,5})|\\d{1,5})+(로|길))";
//        String newRegx = "([ ](산(d(~|-)d|d))|)|(([가-\u001AR]|(d(~|-)d)|d)+(로|길))";
        Matcher matcher = Pattern.compile(regex).matcher(address);
        Matcher newMatcher = Pattern.compile(newRegx).matcher(address);
        if (matcher.find()) {
            return address.replaceAll("[0-9]", "*");
        } else if (newMatcher.find()) {
            return address.replaceAll("[0-9]", "*");
        }
        return address;
    }
}
