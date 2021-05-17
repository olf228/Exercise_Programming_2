// We still use the Junit 3 framework since the APA server is not yet migrated to JUnit 4

import java.util.Arrays;
import junit.framework.TestCase;

public class TaggedFileTest extends TestCase {
    @SuppressWarnings("rawtypes")
    private Class clazz = TaggedFile.class;
    private TaggedFile f1;
    private TaggedFile f2;
    private TaggedFile f3;

    { // Initializer block
      // This checks the proper connection of constructors already

        try {
            f1 = new TaggedFile("audiofiles/Rock 812.mp3");
            f2 = new TaggedFile("audiofiles/wellenmeister_awakening.ogg");
            f3 = new TaggedFile("audiofiles/beethoven-ohne-album.mp3");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testSuperClass() {
        assertEquals("TaggedFile ist not derived from SampledFile",
                "SampledFile", clazz.getSuperclass()
                        .getName());
    }

    @SuppressWarnings("unchecked")
    public void testConstructor() {
        try {
            clazz.getDeclaredConstructor(new Class[] { String.class });
        } catch (SecurityException e) {
            fail(e.toString());
        } catch (NoSuchMethodException e) {
            fail("Constructor TaggedFile(String) does not exist");
        }
    }

    // Test the toString implementation in class TaggedFile
    public void testToString() {
        assertEquals("toString not correct",
                "Eisbach - Rock 812 - The Sea, the Sky - 05:31", f1.toString());
        assertEquals(
                "toString not correct",
                "Wellenmeister - TANOM Part I: Awakening - TheAbsoluteNecessityOfMeaning - 05:55",
                f2.toString());
        assertEquals(
                "toString not correct",
                "beethoven-ohne-album - 00:06",
                f3.toString());
    }

    public void test_readAndStoreTags_01() {
        assertEquals("Wrong author", "Eisbach", f1.getAuthor());
        assertEquals("Wrong title", "Rock 812", f1.getTitle());
        assertEquals("Wrong album", "The Sea, the Sky", f1.getAlbum());
        assertEquals("Wrong duration", "05:31", f1.getFormattedDuration());

        assertEquals("Wrong author", "Wellenmeister", f2.getAuthor());
        assertEquals("Wrong title", "TANOM Part I: Awakening", f2.getTitle());
        assertEquals("Wrong album", "TheAbsoluteNecessityOfMeaning",
                f2.getAlbum());
        assertEquals("Wrong duration", "05:55", f2.getFormattedDuration());
    }

    // Test the fields() implementation in class TaggedFile
    public void test_fields_01() {
        assertEquals("Wrong fields",
                "[Eisbach, Rock 812, The Sea, the Sky, 05:31]",
                Arrays.asList(f1.fields()).toString());
        assertEquals(
                "Wrong fields",
                "[Wellenmeister, TANOM Part I: Awakening, TheAbsoluteNecessityOfMeaning, 05:55]",
                Arrays.asList(f2.fields()).toString());
    }

    // Class TaggedFile does only need one attribute (one for the album)
    // The others should have been moved to some super class
    public void testNrAttributes() {
        assertTrue(
                "Do not define any local variables of methods as attributes?",
                clazz.getDeclaredFields().length == 1);
    }

    public void testInvalid() {
        try {
            new TaggedFile("audiofiles/Rock 812.cut.mp3");
            fail("NotPlayableException expected for erroneous MP3 file Rock 812.cut.mp3");
        } catch (RuntimeException e) {
            // Expected
        }
    }
}
