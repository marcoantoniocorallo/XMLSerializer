package ap.xmlserializer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ReflectPermission;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Function;

/**
 * Advanced Programming - Assignment 1.2
 * 
 * XMLSerializer provides a public statid method serialize that serialize an array of objects
 * annotated with @XMLable and @XMLfield.
 * The output must be an XML file called fileName.xml containing one element for each object in arr.
 * Each element must have as main tag the name of the class of the corresponding object.
 * 
 * Design choices:
 * - For simplicity this API deletes the output filename if already exists one;
 * - At the beginning serialize checks if a security manager is installed and, 
 *   if it is, if it restricts ReflectPermission. If it does, then print a message and returns.
 * - There is a check on the annotated type. It must be the same of the real type of the field.
 * - XMLSerializer is, in turn, XMLable: I thought it was a funny test :)
 * 
 * @author marco
 */

@XMLable
public class XMLSerializer {
    @XMLfield(type="String", name="Versioning_Line")
    private static final String firstLine = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    
    @XMLfield(type="String")
    private static final String notXML = "<notXMLable />";
    
    private static FileWriter fw;
    private static HashMap<Class,ArrayList<Function<Object,String>>> map = new HashMap<>();

    /**
     * Serialize the objects in arr that are annotated with @XMLable;
     * For each xmlable object, serialize the fields annotated with @XMLfield;
     * This method creates a file fileName.xml containing all the serialized objects.
     * @param arr Array containing (some) xmlable objects
     * @param fileName name of the output file to create
     */
    public static void serialize(Object [ ] arr, String fileName){        
        // Check null
        checkParams(arr, fileName);
       
        // Check the accessibility of the fields
        try{ checkAccess(); }
        catch(NullPointerException e) { } // no security manager 
        catch(AccessControlException sec){
            System.out.println(
                "Warning: the security manager installed may restrict the access to the fields."
                + "\nUnrestrict the policy for a correct serialization."
            );
            return;
        }
        
        // Create file and filewriter
        try { 
            createFile(fileName); 
            fw = new FileWriter(fileName);
            
            // write XML version line
            fw.write(firstLine);
        }
        catch (IllegalArgumentException iae) { 
            System.out.println(iae.getMessage());
            return; 
        } 
        catch (IOException e) { 
            System.out.println("An errorr occurred while creating the file."); 
            return; 
        }
        
        // for each object: write its serialization or notXML
        for (int i=0; i < arr.length; i++){
            Class c = arr[i].getClass();
            
            try{
                if (c.getAnnotation(XMLable.class) != null)
                    writeObjectXML(arr[i]);
                else 
                    writeNotXML(c.getSimpleName());
            }
            catch (IOException io){
                System.out.println("An errorr occurred while creating the file."); 
                break; // goto filewriter close
            }
            catch (WrongAnnotationException w){
                System.out.println(w.getMessage() + " at object " + i + ": " + c.getSimpleName());
                break; // goto filewriter close
            }
        }
        
        // close resources
        try { fw.close(); } 
        catch (IOException ex) { System.out.println("Error occurred closing the file."); }
        
    }
    
    /**
     * Check if a security manager is setted, with a restricted policy to ReflectPermission;
     * @throws AccessControlException if the security manager setted restricts the ReflectPermissions;
     * @throws NullPointerException if no security manager are setted;
     */
    private static void checkAccess(){
        System.getSecurityManager().checkPermission(new ReflectPermission("suppressAccessChecks"));
    }
    
    /**
     * Serialize an object in an XML element and write it into the output file;
     * @param o the object to serialize
     * @throws IOException if an error occurs during the writing
     */
    private static void writeObjectXML(Object o) 
            throws IOException, WrongAnnotationException{
        Class c = o.getClass();
        String xml = null;
        
        // If class has not been already explored -> introspection and store information in map
        if (!map.containsKey(c))
                introspect(c);
    
        xml = serializeObject(map.get(c), o);
        fw.write(xml);
    }
    
    /** 
     * Introspects a given class, storing the xmlable fields in a structure binded to the class in map.
     * @param c the class to introspect
     */
    private static void introspect(Class c) throws WrongAnnotationException{
        ArrayList<Function<Object,String>> A = new ArrayList<>();
        
        // Only one introspection per class
        // System.out.println("Introspection of "+c.getSimpleName());
        
        // The first (and the last) element is the name of the class (constant function)
        A.add((o) -> "\n<" + c.getSimpleName() + ">" );
        
        for ( Field f : c.getDeclaredFields()){
            XMLfield annotation = (XMLfield) f.getAnnotation(XMLfield.class);
            if (annotation != null){

                // get informations
                String name = annotation.name().equals("") ? f.getName() : annotation.name();
                String type = annotation.type();

                // check if the annotated type matches the real one
                match_types(f.getType().getSimpleName().toLowerCase(), type.toLowerCase());
                
                Function lambda = 
                        (Object o) -> XMLtag(name, type, getValue(f,o));
                A.add(lambda);
            }
        }
        A.add((o) -> "\n</" + c.getSimpleName() + ">" );
        
        // update the map
        map.put(c, A);
    }
    
    /**
     * Creates an XML element representing the given object.
     * @param c the list of fields of the class, in the form:
     *          [fun _ -> className; fun o -> field.get(o); fun o -> field.get(o); ... ]
     * @param o the object to serialize
     * @return An xml string representing o
     */
    private static String serializeObject(ArrayList<Function<Object,String>> c, Object o){
        String xml = "";
        for (Function lambda : c)
            xml += lambda.apply(o);
        return xml;
    };
    
    /**
     * Get the type of the field annotation and its value and check the correspondence
     * @param classtype the value of the field
     * @param annotatedtype the annotated type
     * @throws WrongAnnotationException if 
     *         - annotatedtype is not a primitive type or a string;
     *         - classtype's type doesn't match annotatedtype
     */
    private static void match_types(String classtype, String annotatedtype) 
            throws WrongAnnotationException{
        
        switch (annotatedtype) {
            case "int":
            case "float":
            case "char":
            case "bool":
            case "string":
                if (classtype.equals(annotatedtype)) break;
            default:
                throw new WrongAnnotationException(
                        "Error: type mismatch: annotated "+annotatedtype+" have "+classtype
                );
        }
    }
        
    /**
     * Get the value of the field f for the object o;
     * @param f field to access
     * @param o instance to access
     * @return the value of o.f
     */
    private static Object getValue (Field f, Object o){
        Object obj = Modifier.isStatic(f.getModifiers()) ? null : o;
        if (!f.canAccess(obj))
            f.setAccessible(true);
        
        // The accessibility flag is setted to true and 
        // the presence of the security manager has been checked; it should not throw the IAE
        Object ret = null;
        try { ret =  f.get(o); } 
        catch (IllegalAccessException ex) { }
        
        return ret;
    }
    
    /**
     * Write the tag for no-XMLable classes, propagating the eventual IOException;
     * @throws IOException if errors occur in the writing
     */
    private static void writeNotXML(String className) throws IOException{
        fw.write(
                "\n<" + className + ">"
                +"\n\t"+XMLSerializer.notXML
                +"\n</" + className + ">"
        );
    }
    
    /**
     * @param name the tag name
     * @param type the type of the field
     * @param value the value enclosed in the tag
     * @return a string representing the generated xml tag
     */
    private static String XMLtag(String name, String type, Object value){
        return "\n\t<"+name+" type=\""+type+"\">"+value+"</"+name+">";
    }
    
    /**
     * Try to create a file filename
     * @param filename name of the file to create
     * @throws IOException if an error occurs during the creation
     * @throws IllegalArgumentException if the file filename already exists
     */
    private static void createFile(String filename) 
            throws IOException{
        File f = new File(filename);
        if (f.exists())        f.delete();
        if (!f.createNewFile()) 
            throw new IllegalArgumentException("File "+filename+" already exists.");        
    }
    
    /**
     * Checks if params are null
     * @param arr a possible null Array of possible null objects
     * @param fileName a possible null string
     * @throws IllegalArgumentException if at least one reference is null
     */
    private static void checkParams(Object[] arr, String fileName) throws IllegalArgumentException{
        if ( 
                arr == null || 
                Arrays.stream(arr).anyMatch(obj -> obj == null) || 
                fileName == null 
            )   throw new IllegalArgumentException("Error: null object found!");
    }
    
}
