// We still use the Junit 3 framework since the APA server is not yet migrated to JUnit 4

package studiplayer.cert;

import studiplayer.audio.SortCriterion;
import junit.framework.TestCase;

public class SortCriterionTest extends TestCase {

    @SuppressWarnings("rawtypes")
    private Class clazz = SortCriterion.class;

    public void testEntries() {
        Object[] consts = clazz.getEnumConstants();
        assertNotNull("Kein Enum", consts);
        assertTrue("Falsche Anzahl Enum-Entries", consts.length == 4);
    }
}
