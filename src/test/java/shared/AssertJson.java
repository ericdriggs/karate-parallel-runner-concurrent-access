package shared;

import karate.org.thymeleaf.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static shared.AssertDate.offsetRegex;
import static net.javacrumbs.jsonunit.JsonAssert.when;
import static net.javacrumbs.jsonunit.core.Option.IGNORING_ARRAY_ORDER;
import static net.javacrumbs.jsonunit.core.Option.IGNORING_EXTRA_FIELDS;

/**
 * Provides utilities for comaparing json payloads
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class AssertJson {

    final static String dateOffsetRegex = "date-offset-" + offsetRegex;
    final static Pattern dateOffsetPattern = Pattern.compile(dateOffsetRegex, Pattern.CASE_INSENSITIVE);

    /**
     * Performs partial matching on json arrays using specified fields from expected.
     * When comparing dates, the time component may be ignored if the expected uses the following regex format:
     * "${json-unit.regex}2018-01-01.*"
     * <p>
     * To validate the date format only, use:  "${json-unit.regex}[0-9]{4}-[0-9]{2}-[0-9]{2}.*"
     * <p>
     * Prefer #assertJsonEquals unless you need to regex-based matching on non-dates since
     * assertJsonEquals has better error messages
     * <p>
     * Throws on assertion failure
     *
     * @param expected the expected json array string with the fields to validate on.
     * @param actual   the actual json array string to validate against
     */
    public static void jsonUnitEquals(final String expected, final String actual) {
        net.javacrumbs.jsonunit.JsonAssert.assertJsonEquals(expected, actual, when(IGNORING_EXTRA_FIELDS, IGNORING_ARRAY_ORDER));
    }

    public static void jsonUnitEqualsStrict(final String expected, final String actual) {
        net.javacrumbs.jsonunit.JsonAssert.assertJsonEquals(expected, actual, when(IGNORING_ARRAY_ORDER));
    }

    /**
     * Performs partial matching on json arrays using specified fields from expected.
     * When comparing iso8601 dates, the time component is ignored.
     * Throws on assertion failure
     *
     * @param expected the expected json array with the fields and values to validate with.
     * @param actual   the actual json array to validate against
     */
    public static void assertJsonEqualsStripTimes(final String expected, final String actual) {
        if (StringUtils.isEmpty(expected)) {
            throw new IllegalArgumentException("Expected json is empty");
        }

        if (StringUtils.isEmpty(actual)) {
            throw new IllegalArgumentException("Actual json is empty");
        }
        jsonUnitEquals(AssertDate.stripTimeFromDates(expected), AssertDate.stripTimeFromDates(actual));
    }

    public static void assertJsonEqualsWithDateOffset(final String expected, final String actual) {
        if (StringUtils.isEmpty(expected)) {
            throw new IllegalArgumentException("Expected json is empty");
        }

        if (StringUtils.isEmpty(actual)) {
            throw new IllegalArgumentException("Actual json is empty");
        }

        String expectedWithDateOffsets = replaceOffsetDates(expected);
        jsonUnitEquals(AssertDate.stripTimeFromDates(expectedWithDateOffsets), AssertDate.stripTimeFromDates(actual));
    }

    //Prefer AssertDate.stripTimeFromDates
    @Deprecated
    public static String stripTimeFromDates(String iso8601date) {
        return AssertDate.stripTimeFromDates(iso8601date);
    }

    private static String replaceOffsetDates(String json) {
        String replacedJson = json;
        Matcher dateOffsetMatcher = dateOffsetPattern.matcher(json);

        while (dateOffsetMatcher.find()) {
            String dateOffset = dateOffsetMatcher.group(0).replace("date-offset-", "");
            String then = AssertDate.getOffsetFromNow(dateOffset);
            replacedJson = dateOffsetMatcher.replaceFirst(then);
            dateOffsetMatcher = dateOffsetPattern.matcher(replacedJson);
        }
        return replacedJson;
    }

}
