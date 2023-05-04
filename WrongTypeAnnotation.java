package ap.xmlserializer;

/**
 * Advanced Programming - Assignment 1.2
 * 
 * Class example with wrong typed annotations
 * @author marco
 */

@XMLable
public class WrongTypeAnnotation {
    @XMLfield(type = "int") // Wrong type!
    private String secret;
    
    public WrongTypeAnnotation(){}
}
