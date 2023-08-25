package com.easymedia.enums.company;

import com.easymedia.enums.DatabaseCodeCommonType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.MapUtils;

import java.util.Map;

/**
 * 은행타입
 */
@Schema(title = "은행 타입")
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@RequiredArgsConstructor
public enum BankType implements DatabaseCodeCommonType {
    NONGHYUP("농협중앙회", "11", "KRW", null),
    NONGHYUP_UNIT("단위농협", "12", "ETC", null),
    CHUKHYUP("축협중앙회", "16", "ETC", null),
    WOORI("우리은행", "20", "KRW", null),
    CHOHUNG("구)조흥은행", "21", "KRW", null),
    COMMERCIAL("상업은행", "22", "KRW", null),
    STANDARDCHARTERED("SC제일은행", "23", "KRW", null),
    HANIL("한일은행", "24", "KRW", null),
    SEOUL("서울은행", "25", "KRW", null),
    SHINHAN_OLD("구)신한은행", "26", "KRW", null),
    CITY_OLD("한국씨티은행 (구 한미)", "27", "KRW", null),
    DGB("대구은행", "31", "KRW", null),
    BNK("부산은행", "32", "KRW", null),
    KJB("광주은행", "34", "KRW", null),
    JEJU("제주은행", "35", "KRW", null),
    JBB("전북은행", "37", "KRW", null),
    KANGWON("강원은행", "38", "KRW", null),
    GYEONGNAM("경남은행", "39", "KRW", null),
    BCCARD("비씨카드", "41", "ETC", null),
    KFCC("새마을금고", "45", "KRW", null),
    CU("신용협동조합중앙회", "48", "EUR", null),
    MUTUAL("상호저축은행", "50", "KRW", null),
    CITY("한국씨티은행", "53", "KRW", null),
    HSBC("홍콩상하이은행", "54", "EUR", null),
    DEUTSCHE("도이치은행", "55", "EUR", null),
    ABN_AMRO("ABN암로", "56", "EUR", null),
    JP_MORGAN("JP모건", "57", "EUR", null),
    TOKYO_MITSUBISHI("미쓰비시도쿄은행", "59", null, null),
    BANK_OF_AMERICA("BOA(Bank of America)", "60", null, null),
    SJ("산림조합", "64", "ETC", null),
    BARO("신안상호저축은행", "70", "KRW", null),
    EPOST("우체국", "71", "KRW", null),
    KEBHANA("하나은행", "81", "KRW", null),
    PEACE("평화은행", "83", "KRW", null),
    SHINSEGAE("신세계", "87", "PAY", null),
    SHINHAN("신한(통합)은행", "88", "KRW", null),
    K_BANK("케이뱅크", "89", "KRW", null),
    KAKAO_BANK("카카오뱅크", "90", "KRW", null),
    NAVER_PAY("네이버포인트(포인트 100% 사용)", "91", "PAY", null),
    TOSS_BANK("토스뱅크", "92", "KRW", null),
    TOSS_PAY("토스머니(포인트100% 사용)", "93", "PAY", null),
    SSG_PAY("SSG머니(포인트 100% 사용)", "94", "PAY", null),
    L_PAY("엘포인트(포인트 100% 사용)", "96", "PAY", null),
    KAKAO_PAY("카카오 머니", "97", "PAY", null),
    PAYCO("페이코 (포인트 100% 사용)", "98", "PAY", null),
    KDB("한국산업은행", "02", "KRW", null),
    IBK("기업은행", "03", "KRW", null),
    KB("국민은행", "04", "KRW", null),
    KEBHANA_OLD("하나은행 (구 외환)", "05", "KRW", null),
    KB_OLD("국민은행 (구 주택)", "06", "KRW", null),
    SUHYUP("수협중앙회", "07", "KRW", null),
    MYASSET_STOCK_OLD("유안타증권(구 동양증권)", "ETC", null, null),
    HYUNDAI_STOCK("현대증권", "D2", "ETC", null),
    MIRAEASSET_STOCK("미래에셋증권", "D3", "ETC", null),
    KOREA_STOCK("한국투자증권", "D4", "ETC", null),
    WOORI_STOCK("우리투자증권", "D5", "ETC", null),
    HI_STOCK("하이투자증권", "D6", "ETC", null),
    HMC_STOCK("HMC투자증권", "D7", "ETC", null),
    SK_STOCK("SK증권", "D8", "ETC", null),
    DAISHIN_STOCK("대신증권", "D9", "ETC", null),
    HANAW_STOCK("하나대투증권", "DA", "ETC", null),
    SHINHANINVEST_STOCK("굿모닝신한증권", "ETC", null, null),
    DONGBU_STOCK("동부증권", "DC", "ETC", null),
    EUGENEFN_STOCK("유진투자증권", "DD", "ETC", null),
    MERITZ_STOCK("메리츠증권", "DE", "ETC", null),
    SHINYOUNG_STOCK("신영증권", "DF", "ETC", null),
    DAEWOO_STOCK("대우증권", "DG", "ETC", null),
    SAMSUNG_STOCK("삼성증권", "DH", "ETC", null),
    KYOBO_STOCK("교보증권", "DI", "ETC", null),
    KIWOOM_STOCK("키움증권", "DJ", "ETC", null),
    ETRADE_STOCK("이트레이드", "DK", "ETC", null),
    SOLOMON_STOCK("솔로몬증권", "DL", "ETC", null),
    HANWHAW_STOCK("한화증권", "DM", "ETC", null),
    NHQV_STOCK("NH증권", "DN", "ETC", null),
    BOOKOOK_STOCK("부국증권", "DO", "ETC", null),
    LIG_STOCK("LIG증권", "DP", "ETC", null),
    BANK_WALLET("뱅크월렛", "BW", "PAY", null),
    ;

    private final String label;
    @JsonIgnore
    private final String databaseCode;
    private final String customValue;
    private final String description;

    @JsonCreator
    public static BankType fromCode(Object value) {
        return value instanceof Map ? valueOf(MapUtils.getString((Map) value, "code"))
                : valueOf((String) value);
    }

    public String getCode() {
        return this.name();
    }
}