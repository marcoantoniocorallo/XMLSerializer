package ap.xmlserializer;

/**
 * Exception raised for wrong annotations i.e. a field of type A annotated with type B
 * @author marco
 */
public class WrongAnnotationException extends Exception{
    public WrongAnnotationException(){super();}
    
    public WrongAnnotationException(String s){super(s);}
}
