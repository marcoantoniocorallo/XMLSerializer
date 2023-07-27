package ap.xmlserializer;
import ap.xmlserializer.XMLable;
import ap.xmlserializer.XMLfield;

/**
 * Class Example: it contains both public and private fields
 * @author marco
 */

@XMLable
public class Student {
    @XMLfield(type = "String")
    public String firstName;
    
    @XMLfield(type = "String", name = "surname")
    public String lastName;
    
    @XMLfield(type = "int")
    private int age;
    
    public Student(){}
    
    public Student(String fn, String ln, int age) {
        this.firstName = fn;
        this.lastName = ln;
        this.age = age;
    }
}
