package shared;
import static net.javacrumbs.jsonunit.JsonAssert.when;
import static net.javacrumbs.jsonunit.core.Option.IGNORING_ARRAY_ORDER;
import static net.javacrumbs.jsonunit.core.Option.IGNORING_EXTRA_FIELDS;

/**
 * Provides utilities for comparing json payloads
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class AssertJson {

    /**
     * Compares expected and actual json, ignoring extra properties in actual
     * @param expected
     * @param actual
     */
    public static void jsonUnitEquals(final String expected, final String actual) {
        net.javacrumbs.jsonunit.JsonAssert.assertJsonEquals(expected, actual, when(IGNORING_EXTRA_FIELDS, IGNORING_ARRAY_ORDER));
    }

    /**
     * Compares expected and actual json, requiring same properties in actual and expected
     * @param expected
     * @param actual
     */

    public static void jsonUnitEqualsStrict(final String expected, final String actual) {
        net.javacrumbs.jsonunit.JsonAssert.assertJsonEquals(expected, actual, when(IGNORING_ARRAY_ORDER));
    }

}
