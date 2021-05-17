// Helper class for emulating platform dependent behavior for other platforms
// than the real one

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Properties;
import java.util.Enumeration;

public class EmulateOtherOs {
    static char fileSeparatorReal = java.io.File.separatorChar;
    static String osNameReal = System.getProperty("os.name");

    // A helper that removes the 'final' modifier
    // We use it to alter java.io.File.separatorChar
    public static void setFinalStatic(Field field, Object newValue)
            throws Exception {
        field.setAccessible(true);

        // remove final modifier from field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(null, newValue);
    }

    public static void listAllSystemProperties() {
        Properties p = System.getProperties();
        Enumeration enuProp = p.propertyNames();
        while (enuProp.hasMoreElements()) {
            String propertyName = (String) enuProp.nextElement();
            String propertyValue = p.getProperty(propertyName);
            System.out.println(propertyName + ": " + propertyValue);
        }
    }

    public static void reset() throws Exception {
        setFinalStatic(java.io.File.class.getField("separatorChar"),
                fileSeparatorReal);
        Properties p = System.getProperties();
        p.setProperty("file.separator", String.valueOf(fileSeparatorReal));
        p.setProperty("os.name", osNameReal);
    }

    public static void emulateWindows() throws Exception {
        setFinalStatic(java.io.File.class.getField("separatorChar"), '\\');
        Properties p = System.getProperties();
        p.setProperty("file.separator", "\\");
        p.setProperty("os.name", "Windows 7");
    }

    public static void emulateLinux() throws Exception {
        setFinalStatic(java.io.File.class.getField("separatorChar"), '/');
        Properties p = System.getProperties();
        p.setProperty("file.separator", "/");
        p.setProperty("os.name", "Linux");
    }

    public static void emulateMac() throws Exception {
        setFinalStatic(java.io.File.class.getField("separatorChar"), '/');
        Properties p = System.getProperties();
        p.setProperty("file.separator", "/");
        p.setProperty("os.name", "Mac OS");
    }

    public static void listRelevantSystemProperties() {
        System.out.println("File.separator1:" + java.io.File.separatorChar);
        System.out.println("File.separator2:"
                + System.getProperty("file.separator"));
        System.out.println("OS.name:" + System.getProperty("os.name"));
    }

}
