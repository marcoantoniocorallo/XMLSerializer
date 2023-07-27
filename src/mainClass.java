package ap.xmlserializer;

import javax.swing.JButton;

/**
 * Advanced Programming - Assignment 1.2
 * 
 * @author marco
 */
public class mainClass {

    public static void main(String[] args) {
        
        // test 0, non-XMLable objects
        Object[] o0 = {new Object(), new String("Hello World!"), new JButton()};
        XMLSerializer.serialize(o0, "test0.xml");
        
        // test 1, empty array
        Object[] o1 = {};
        XMLSerializer.serialize(o1, "test1.xml");
        
        // test 2, example provided in the text of the assignment
        Object[] o2 = {new Student("Jane","Doe",42)};
        XMLSerializer.serialize(o2, "test2.xml");
        
        // test 3, array of students
        Object[] o3 = {new Student("Jane","Doe",42), 
                       new Student("Pippo","Baudo",80),
                       new Student("Jackie","Jackie",03)};
        XMLSerializer.serialize(o3, "test3.xml");
        
        // test 4, student with uninitialized fields!
        Object[] o4 = {new Student()};
        XMLSerializer.serialize(o4, "text4.xml");
        
        // test 5, Singer with uninitialized static-field
        // "If the underlying field is static, the class that declared the field is initialized 
        //  if it has not already been initialized." [Java specification]
        // However, it is not said that the constructor of the class initialize the static field!
        Object[] o5 = {new Singer("Francesco", "De Gregori")};
        XMLSerializer.serialize(o5, "test5.xml");
        
        // test 6, array of (fully-initialized, partially-initialized and uninitialized fields) singers
        Object[] o6 = {new Singer("Francesco","Guccini", 82,"Ihavenoidea",'A'),
                       new Singer("Fabrizio","De Andre"),
                       new Singer()
        };
        XMLSerializer.serialize(o6, "test6.xml");
        
        // test 7, all together: student, singer, non-XMLable and also a XMLSerializer
        Object[] o7 = {new Student("Jane","Doe",42), new Singer("Lucio", "Dalla"),
                       new Student(), new Singer(),
                       new String("Hello world!"), new JButton("Hello World!"),
                       new String(), new JButton(), new Object(), new XMLSerializer()
        };
        XMLSerializer.serialize(o7, "test7.xml");
        
        // test 8, wrong type annotation test!
        // Commented to avoid the exception stacktrace
        // Object[] o8 = {new WrongTypeAnnotation()};
        // XMLSerializer.serialize(o8, "test8.xml");
        
        // test 9, class that uses security manager: won't be serialized!
        // Commented to avoid the "terminally deprecated" warnings of SecurityManager.
        // Object [] o9 = {new Teacher("Andrea", "Corradini"),
        //                 new Teacher("Laura", "Bussi")
        // };
        // XMLSerializer.serialize(o9, "test9.xml");
        
    }
    
}
