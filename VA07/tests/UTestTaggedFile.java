import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class UTestTaggedFile {

    @Ignore
    @Test
    public void test_play_01() throws Exception {
        TaggedFile tf = new TaggedFile("audiofiles/Rock 812.mp3");
        tf.play();
        // Note: cancel playback in console window
    }
    @Test
    public void test_timeFormatter_10() throws Exception{
        assertEquals("Wrong time format", "05:05", TaggedFile.timeFormatter(305862000L));
    }
    @Test
    public void test_timeFormatter_08() throws Exception{
        try{
            // Call method with time value that underflows our format
            TaggedFile.timeFormatter(-1L);
            // We should never get here
            fail("Time value underflows format; expecting exception");
        }
        catch(RuntimeException e){
            // Expected
        }
    }
    @Test
    public void test_readAndStoreTags() throws Exception{
        TaggedFile tf = new TaggedFile();
        tf.readAndStoreTags("audiofiles/Rock 812.mp3");
        assertEquals("Wrong author", "Eisbach", tf.getAuthor());
        assertEquals("Wrong title", "Rock 812", tf.getTitle());
        assertEquals("Wrong album", "The Sea, the Sky", tf.getAlbum());
        assertEquals("Wrong time format", "05:31", tf.getFormattedDuration());
    }
}
