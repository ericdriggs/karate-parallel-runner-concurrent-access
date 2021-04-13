package examples;

import static net.javacrumbs.jsonunit.JsonAssert.when;
import static net.javacrumbs.jsonunit.core.Option.IGNORING_ARRAY_ORDER;
import static net.javacrumbs.jsonunit.core.Option.IGNORING_EXTRA_FIELDS;

/**
 * Provides utilities for comaparing json payloads
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class AssertJson {

    /**
     * Performs partial matching on json strings comparing only against specified properties from expected.
     * Extra properties in actual objects will be ignored.
     *
     * When comparing dates, the time component may be ignored if the expected uses the following regex format:
     * "${json-unit.regex}2018-01-01.*"
     *
     * To validate the date format only, use:  "${json-unit.regex}[0-9]{4}-[0-9]{2}-[0-9]{2}.*"
     *
     * Throws on assertion failure
     *
     @param expected the expected json string with the fields to validate on.
     @param actual the actual json string to validate against
     */
    public static void jsonUnitEquals(final String expected, final String actual) {
        try {
            net.javacrumbs.jsonunit.JsonAssert.assertJsonEquals(expected, actual, when(IGNORING_EXTRA_FIELDS, IGNORING_ARRAY_ORDER));
        } catch ( Throwable t) {
            t.printStackTrace();
            throw t;
        }
    }

    /**
     * Performs strict matching on json strings, requiring both expected and actual match on all properties.
     * Extra fields in actual will cause assertion failures.
     *
     * When comparing dates, the time component may be ignored if the expected uses the following regex format:
     * "${json-unit.regex}2018-01-01.*"
     *
     * To validate the date format only, use:  "${json-unit.regex}[0-9]{4}-[0-9]{2}-[0-9]{2}.*"
     *
     * Throws on assertion failure
     *
     @param expected the expected json string with the fields to validate on.
     @param actual the actual json string to validate against
     */
    public static void jsonUnitEqualsStrict(final String expected, final String actual) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        net.javacrumbs.jsonunit.JsonAssert.assertJsonEquals(expected, actual, when(IGNORING_ARRAY_ORDER));

    }


}
