import java.lang.reflect.Field;
import junit.framework.TestCase;
import static java.lang.reflect.Modifier.*;

@SuppressWarnings("rawtypes")
public class AttributesTest extends TestCase {
    // Liste aller Klassen
    private Class[] clazzA = { 
            AudioFile.class,
            SampledFile.class,
            TaggedFile.class,
            WavFile.class,
            };

    public void testAttributes() {
        // Teste alle Klassen im Array clazzA
        for (Class theClass : clazzA) {
            try {
                // Teste alle Attribute
                for (Field field : theClass.getDeclaredFields()) {
                    field.setAccessible(true);
                    String attShort = field.getName();

                    // Attributnamen beginnen mit kleinen Buchstaben
                    //
                    // Ausnahmen:
                    // - synthetische Attribute (etwa Expansionen von ENUMS)
                    // - Konstanten: also Modifier final
                    assertTrue(
                            "Attribut "
                                    + attShort
                                    + "; Name des Attributs soll mit Kleinbuchstaben anfangen",
                            Character.isLowerCase(attShort.charAt(0))
                                    || field.isSynthetic()
                                    || isFinal(field.getModifiers()) );

                    // Attribute sind nicht public
                    //
                    // Ausnahmen:
                    // - statische Attribute: also Modifier static
                    // - synthetische Attribute (diese werden aber auch immer static generiert)
                    int mod = field.getModifiers();
                    // Kodierung der Implikation: a -> b == ~a || b        
                    assertTrue("Zugriff auf Attribut'" + attShort
                            + "' darf nicht public sein!", 
                            !isPublic(mod) || isStatic(field.getModifiers())                            
                    );
                }
            } catch (SecurityException e) {
                fail(e.toString());
            }
        }
    }
}
