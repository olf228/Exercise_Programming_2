package studiplayer;

import org.junit.Test;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class UTestAudioFileFactory {

    @Test
    public void test_getInstance_01() throws Exception{
        try {
            AudioFileFactory.getInstance("unknown.xxx");
            fail("Unknown suffix; expecting exception");
        }
        catch (RuntimeException e){
            // Excepted
        }
    }

    @Test
    public void test_getInstance_02() throws Exception{
        try{
            AudioFileFactory.getInstance("nonexistent.mp3");
            fail("File is not readable; expecting exception");
        }
        catch (RuntimeException e){
            // Expected
        }
    }

    @Test
    public void test_getInstance_03() throws Exception{
        AudioFile af1 = AudioFileFactory.getInstance("audiofiles/Eisbach Deep Snow.ogg");
        assertTrue("Excepting object of type TaggedFile", af1 instanceof TaggedFile);

        AudioFile af2 = AudioFileFactory.getInstance("audiofiles/wellenmeister - tranquility.wav");
        assertTrue("Expecting object of type WavFile", af2 instanceof WavFile);

        AudioFile af3 = AudioFileFactory.getInstance("audiofiles/special.oGg");
        assertTrue("Expecting object of type TaggedFile", af3 instanceof TaggedFile);
    }

    @Test
    public void test_loadFromM3U_02() throws Exception{
        String m3u_pathname = "playlist.m3u";
        String mp3_pathname = "corrupt.mp3";
        //Create the M3U file with one entry for a non-existent mp3 file
        FileWriter writer = null;
        try{
            // Create a Filewriter
            writer = new FileWriter(m3u_pathname);
            writer.write(mp3_pathname+System.getProperty("line.separator"));
        }
        catch(IOException e){
            throw new RuntimeException("Unable to store M3U file: "+m3u_pathname);
        }
        finally{
            try{
                writer.close();
            }
            catch(IOException e) {
                // Swallow Exception
            }
        }
        // OK,the playlist for testing is in place
        PlayList pl = new PlayList();
        // The next statement will cause a stack trace to be printed onto
        // the console. However, execution is not terminated with an error
        // since we watch the exception in PlayList.loadFromM3U()
        // The test succeeds.
        pl.loadFromM3U(m3u_pathname);
        new File(m3u_pathname).delete();
    }
}
