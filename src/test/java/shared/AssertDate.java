package shared;

import karate.org.thymeleaf.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SuppressWarnings({"unused", "WeakerAccess"})
public class AssertDate {

    private static Logger log = LoggerFactory.getLogger(AssertDate.class);


    final public static String offsetRegex = "y:[-]?[0-9]+,m:[-]?[0-9]+,d:[-]?[0-9]+";
    final static DateTimeFormatter ISO_OFFSET_DATE_TIME = DateTimeFormatter.ISO_OFFSET_DATE_TIME;


    final static ZoneId ZONE_ID_UTC;
    final static ZoneId ZONE_ID_US_PACIFIC;

    static ZoneId ZONE_ID_DEFAULT;

    /**
     * TIMEZONE defaults to US/Pacific but can overridden through ENV: ZONE_ID_DEFAULT
     * see https://www.mkyong.com/java/java-display-list-of-timezone-with-gmt/ for valid values
     */
    static {
        String ZONE_ID_DEFAULT_ENV = System.getenv("ZONE_ID_DEFAULT");

        ZONE_ID_UTC = ZoneId.of("UTC");
        ZONE_ID_US_PACIFIC = ZoneId.of("US/Pacific");
        ZONE_ID_DEFAULT = ZONE_ID_US_PACIFIC;

        if (ZONE_ID_DEFAULT_ENV != null) {
            try {
                ZONE_ID_DEFAULT = ZoneId.of(ZONE_ID_DEFAULT_ENV);
            } catch (Exception ex) {
                log.error("Error: unable to parse env ZONE_ID_DEFAULT:" + ZONE_ID_DEFAULT_ENV);
                ex.printStackTrace();
            }
        }


    }

    /* ************************************************************************************************************/

    public static Boolean checkDateWithOffset(String actualZonedDateTimeString, String expectedOffsetFromNowYMD, String toleranceYMD) {
        return checkDateWithOffset(actualZonedDateTimeString, expectedOffsetFromNowYMD, toleranceYMD, ZONE_ID_DEFAULT);
    }

    public static Boolean checkDateWithOffset(String actualZonedDateTimeString, String expectedOffsetFromNowYMD, String toleranceYMD, String zoneId) {
        return checkDateWithOffset(actualZonedDateTimeString, expectedOffsetFromNowYMD, toleranceYMD, ZoneId.of(zoneId));
    }

    /**
     * Validates if <code>actualZonedDateTimeString</code> is <code>expectedOffsetFromNowYMD</code> from UTC now
     * within <code>toleranceYMD</code>
     * Negative values are allowed in #'s for YMD strings
     *
     * @param actualZonedDateTimeString the actualZonedDateTimeString to compare to now in format iso8601
     * @param expectedOffsetFromNowYMD  the expected offset +/- from now in format y:#,m:#,d:#
     * @param toleranceYMD              the tolerance +/- in format y:#,m:#,d:#
     * @param zoneId                    the zoneId for date string representation
     * @return whether the now + expectedOffsetFromNowYMD  is within the toleranceYMD of actualZonedDateTimeString
     */
    public static Boolean checkDateWithOffset(String actualZonedDateTimeString, String expectedOffsetFromNowYMD, String toleranceYMD, ZoneId zoneId) {
        if (StringUtils.isEmpty(actualZonedDateTimeString)) {
            throw new NullPointerException("ZonedDateTimeString");
        }

        ZonedDateTime actualZonedDateTime = parseZonedDateTime(actualZonedDateTimeString, zoneId);
        Integer[] offsetYMD = getYmdFromOffset(expectedOffsetFromNowYMD);

        ZonedDateTime expectedZonedDateTime = ZonedDateTime.now().withZoneSameInstant(zoneId).plusYears(offsetYMD[0]).plusMonths(offsetYMD[1]).plusDays(offsetYMD[2]);
        return checkDateDiff(actualZonedDateTime, expectedZonedDateTime, toleranceYMD);
    }

    /* ************************************************************************************************************/

    /**
     * @param dateTimeString a date time string in iso8601 format
     * @param pattern        a date formatter pattern used in DateTimeFormatter.ofPattern(pattern)
     * @return a new datetime string in the requested pattern
     */
    public static String convert(String dateTimeString, String pattern) {
        //TODO: replace with call to convertZone with null zoneId after testing/verification that it is equivalent
        OffsetDateTime time = OffsetDateTime.parse(dateTimeString);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return time.format(formatter);
    }

    public static String convertZone(String dateTimeString, String zoneId) {
        return convertZone(dateTimeString, zoneId, null);
    }

    public static String convertZone(String dateTimeString, String zoneIdString, String dateTimeFormatterPattern) {
        ZoneId zoneId = null;
        if (!StringUtils.isEmpty(zoneIdString)) {
            zoneId = ZoneId.of(zoneIdString);
        }

        DateTimeFormatter dateTimeFormatter = ISO_OFFSET_DATE_TIME;
        if (!StringUtils.isEmpty(dateTimeFormatterPattern)) {
            DateTimeFormatter.ofPattern(dateTimeFormatterPattern);
        }

        return convertZone(dateTimeString, zoneId, dateTimeFormatter);
    }

    public static String convertZone(String dateTimeString, ZoneId zoneId, DateTimeFormatter dateTimeFormatter ) {
        ZonedDateTime time = parseZonedDateTime(dateTimeString).withZoneSameInstant(zoneId);
        return time.format(dateTimeFormatter);
    }

    /* ************************************************************************************************************/

    /**
     * asserts whether the requested datetime string is <code>days</code> after now
     *
     * @param dateTimeString a datetime string in iso8601 format
     * @param days           how many days after now
     */
    public static void dateIsAfterDays(String dateTimeString, Integer days) {
        ZonedDateTime thisDate = parseZonedDateTime(dateTimeString);
        ZonedDateTime thenDate = ZonedDateTime.now().withZoneSameInstant(ZONE_ID_DEFAULT).plusDays(days);
        assertTrue(thisDate.compareTo(thenDate) > 0,
                "thisDate: " + thisDate
                        + " > thenDate: " + thenDate
                        + " + days: " + days + " - ");
    }

    /* ************************************************************************************************************/

    /**
     * asserts whether the requested datetime string is <code>days</code> before now
     *
     * @param dateTimeString a datetime string in iso8601 format
     * @param days           how many days before
     */
    public static void dateIsBeforeDays(String dateTimeString, Integer days) {
        ZonedDateTime thisDate = parseZonedDateTime(dateTimeString);
        ZonedDateTime thenDate = ZonedDateTime.now().minusDays(days);
        assertTrue( thisDate.compareTo(thenDate) < 0,
                "thenDate: " + thenDate
                        + " < thisDate: " + thisDate
                        + " + days: " + days + " - "
               );
    }

    /* ************************************************************************************************************/

    public static String getOffset(String dateTimeString, String ymdOffset) {
        return getOffset(dateTimeString, ymdOffset, ZONE_ID_DEFAULT);
    }

    public static String getOffset(String dateTimeString, String ymdOffset, String zoneId) {
        return getOffset(dateTimeString, ymdOffset, ZoneId.of(zoneId));
    }

    /**
     * Offset from today in format y:#,m:#,d:#
     * For example: if today is 2018-01-01 and you pass an offset of y:0,m:1,d:0, the response is 2018-02-01
     * The order of addition is years, then months, then days.
     * Negative values are allowed
     *
     * @param dateTimeString a date string in iso8601 format
     * @param ymdOffset      in format y:#,m:#,d:#
     * @return a String representing dateTimeString + ymdOffset
     */
    public static String getOffset(String dateTimeString, String ymdOffset, ZoneId zoneId) {

        if (StringUtils.isEmpty(dateTimeString)) {
            throw new NullPointerException("startZonedDateTimeString");
        }
        ZonedDateTime start = parseZonedDateTime(dateTimeString, zoneId);
        Integer[] dateForward = getYmdFromOffset(ymdOffset);
        ZonedDateTime then = start.plusYears(dateForward[0]).plusMonths(dateForward[1]).plusDays(dateForward[2]);

        return ISO_OFFSET_DATE_TIME.format(then);
    }

    /* ************************************************************************************************************/

    public static String getOffsetDate(String dateTimeString, String ymdOffset) {
        return getOffsetDate(dateTimeString, ymdOffset, ZONE_ID_DEFAULT);
    }

    public static String getOffsetDate(String dateTimeString, String ymdOffset, String zoneId) {
        return getOffsetDate(dateTimeString, ymdOffset, ZoneId.of(zoneId));
    }

    /**
     * @param dateTimeString a dateTime string in iso8601 format
     * @param ymdOffset      a ymd offset see <code>offsetRegex</code>
     * @param zoneId         the ZoneId for the time
     * @return dateTimeString + ymdOffset
     */
    public static String getOffsetDate(String dateTimeString, String ymdOffset, ZoneId zoneId) {
        return stripTimeFromDate(getOffset(dateTimeString, ymdOffset, zoneId));
    }

    /* ************************************************************************************************************/

    public static String getOffsetFromNow(String ymdOffset) {
        return getOffsetFromNow(ymdOffset, ZONE_ID_DEFAULT);
    }

    public static String getOffsetFromNow(String ymdOffset, String zoneId) {
        return getOffsetFromNow(ymdOffset, ZoneId.of(zoneId));
    }

    /**
     * @param ymdOffset a ymd offset see <code>offsetRegex</code>
     * @param zoneId    the zoneId for date string representation
     * @return a datetime string representing now + ymdOffset for zoneId
     */
    public static String getOffsetFromNow(String ymdOffset, ZoneId zoneId) {
        ZonedDateTime start = ZonedDateTime.now().withZoneSameInstant(zoneId);
        return getOffset(ISO_OFFSET_DATE_TIME.format(start), ymdOffset, zoneId);
    }

    /* ************************************************************************************************************/
    public static String getOffsetFromNowDate(String ymdOffset) {
        return getOffsetFromNowDate(ymdOffset, "UTC");
    }

    /**
     * @param ymdOffset a ymd offset see <code>offsetRegex</code>
     * @param zoneId    the zoneId for date string representation
     * @return a string representing a now + ymdOffset for zoneId, with no time
     */
    public static String getOffsetFromNowDate(String ymdOffset, String zoneId) {
        return stripTimeFromDate(getOffsetFromNow(ymdOffset, zoneId));
    }

    /* ************************************************************************************************************/

    public static String now() {
        return now(ISO_OFFSET_DATE_TIME, ZONE_ID_DEFAULT);
    }

    public static String now(String zoneIdString) {
        return now(ISO_OFFSET_DATE_TIME, ZoneId.of(zoneIdString));
    }

    public static String now(String dateTimeFormatterPattern, String zoneId) {
        return now(DateTimeFormatter.ofPattern(dateTimeFormatterPattern), ZoneId.of(zoneId));
    }

    /**
     * @param dateTimeFormatter the DateTimeFormatter for the string
     * @param zoneId            the zoneId for date string representation
     * @return an iso8601 datetime string for the given zoneid
     */
    public static String now(DateTimeFormatter dateTimeFormatter, ZoneId zoneId) {
        return dateTimeFormatter.format(ZonedDateTime.now().withZoneSameInstant(zoneId));
    }

    /* ************************************************************************************************************/

    public static String nowDate() {
        return nowDate(ZONE_ID_DEFAULT);
    }

    public static String nowDate(String zoneId) {
        return nowDate(ZoneId.of(zoneId));
    }

    /**
     * @param zoneId the zoneId for date string representation
     * @return a string representing now as a date for the provided zoneid
     */
    public static String nowDate(ZoneId zoneId) {
        return stripTimeFromDate(now(ISO_OFFSET_DATE_TIME, zoneId));
    }

    /* ************************************************************************************************************/

    /**
     * Use to strip time from a string containing a single date. Uses greedy matching (crops everything after "T")
     *
     * @param iso8601Date
     * @return
     */
    public static String stripTimeFromDate(String iso8601Date) {
        return iso8601Date.replaceFirst("T.*", "");
    }

    /* ************************************************************************************************************/

    /**
     * Use this when multiple dates to replace, e.g. json payloads.
     * Does exact matching on formats so may need to add additional formats if one is not yet supported.
     *
     * @param payload a String which can contain multiple dates
     * @return
     */
    public static String stripTimeFromDates(String payload) {
        return payload.replace("[UTC]", "")
                .replace("[US/Pacific]", "")
                .replaceAll("T[0-9]{2}:[0-9]{2}:[0-9]{2}.[0-9]{6}-[0-9]{2}:[0-9]{2}", "")
                .replaceAll("T[0-9]{2}:[0-9]{2}:[0-9]{2}.[0-9]{6}(Z)?", "")
                .replaceAll("T[0-9]{2}:[0-9]{2}:[0-9]{2}(Z)?", "");
    }

    /* ************************************************************************************************************/

    public static String toMillis(String dateTime) {
        return Long.toString(parseZonedDateTime(dateTime).toInstant().toEpochMilli());
    }


    /*************************** Helpers *************************/
    protected static Integer[] getYmdFromOffset(String offset) {
        if (StringUtils.isEmpty(offset)) {
            throw new NullPointerException("ymdOffset");
        }
        offset = offset.replace(" ", "");
        if (!offset.matches(offsetRegex)) {
            throw new IllegalArgumentException(offset + " must be in format " + offsetRegex);
        }
        String[] inputs = offset.split(",");
        assertEquals(inputs.length, 3);
        String[] yearAr = inputs[0].split(":");
        String[] monthAr = inputs[1].split(":");
        String[] dayAr = inputs[2].split(":");
        Integer[] dateArr = new Integer[3];
        dateArr[0] = Integer.parseInt(yearAr[1]);
        dateArr[1] = Integer.parseInt(monthAr[1]);
        dateArr[2] = Integer.parseInt(dayAr[1]);
        return dateArr;
    }

    /**
     * TODO: refactor to void after update usages
     * @param actual  actual datte
     * @param expected expected date
     * @param toleranceYMD tolerance in ymd format
     * @return (ignore) will throw if fails, or return true.
     */
    public static Boolean checkDateDiff(String actual, String expected, String toleranceYMD) {
        ZonedDateTime actualZonedDateTime = parseZonedDateTime(actual);
        ZonedDateTime expectedZonedDateTime = parseZonedDateTime(expected);
        return checkDateDiff(actualZonedDateTime, expectedZonedDateTime, toleranceYMD);

    }

    /**
     * //TODO: refactor to void after update upstream usages
     * Validates if the difference between <code>actualZonedDateTime</code> is <code>expectedZonedDateTime</code> from UTC now
     * within <code>toleranceYMD</code>
     * Negative values are allowed in #'s for YMD strings
     *
     * @param actualZonedDateTime   the actualZonedDateTimeString to compare to now in format iso8601
     * @param expectedZonedDateTime the expected offset +/- from now in format y:#,m:#,d:#
     * @param toleranceYMD          the tolerance +/- in format y:#,m:#,d:#
     * @return whether the actualZonedDateTimeString is within the expected offset +/- within
     */
    protected static Boolean checkDateDiff(ZonedDateTime actualZonedDateTime, ZonedDateTime expectedZonedDateTime, String toleranceYMD) {

        Integer[] arToleranceYMD = getYmdFromOffset(toleranceYMD);
        ZonedDateTime expectedLowerBound = expectedZonedDateTime.minusYears(arToleranceYMD[0]).minusMonths(arToleranceYMD[1]).minusDays(arToleranceYMD[2]);
        ZonedDateTime expectedUpperBound = expectedZonedDateTime.plusYears(arToleranceYMD[0]).plusMonths(arToleranceYMD[1]).plusDays(arToleranceYMD[2]);

        if (expectedLowerBound.compareTo(actualZonedDateTime) <= 0 && actualZonedDateTime.compareTo(expectedUpperBound) <= 0) {
            return true;
        } else {
            throw new AssertionError("actual: " + toOffsetString(actualZonedDateTime) + " was not within toleranceYMD: " + toleranceYMD  + " of expected: " + toOffsetString(expectedZonedDateTime)
                    + ";\n " + toOffsetString(expectedLowerBound)  + " <= " + toOffsetString(actualZonedDateTime) +  " <= " + toOffsetString(expectedUpperBound));
        }
    }

    protected static ZonedDateTime getOffsetZonedDate(String datetimeString, String offset) {
        return parseZonedDateTime(getOffset(datetimeString, offset)).withZoneSameInstant(ZONE_ID_DEFAULT);
    }

    /* ************************************************************************************************************/
    protected static ZonedDateTime parseZonedDateTime(String datetimeString) {
        return parseZonedDateTime(datetimeString, ZONE_ID_DEFAULT);
    }

    protected static String toOffsetString(ZonedDateTime zonedDateTime) {
        return ISO_OFFSET_DATE_TIME.format(zonedDateTime);
    }

    /**
     * @param datetimeString an iso8601 dateTime string
     * @param zoneId         the zoneId for the string representation
     * @return a ZonedDateTime from the string in zone(d
     */
    protected static ZonedDateTime parseZonedDateTime(String datetimeString, ZoneId zoneId) {
        try {
            return ZonedDateTime.parse(datetimeString).withZoneSameInstant(zoneId);
        } catch (DateTimeParseException ex) {

            return LocalDate.parse(datetimeString).atStartOfDay(zoneId);
        }
    }

}
