/**
 * Advanced Programming - Assignment 1.2
 * 
 * This file contains the annotations with which to mark classes and fields to serialize in XML tags.
 * 
 * Design choices:
 * - The attribute type of XMLfield is a string because the example provided uses it,
 *   but I guess: why don't use an enum to restrict the possible types to only primitives and string?
 * @author marco
 */

package ap.xmlserializer;
import java.lang.annotation.*;

/**
 * @XMLable provides information about the class. 
 * The presence of this annotation says that the objects of this class should be serialized. 
 * If instead the annotation is absent, the element must contain only the empty tag <notXMLable />.
 */
@Retention(RetentionPolicy.RUNTIME) // visible at runtime
@Target({ElementType.TYPE})         // annotates classes
@interface XMLable { 
    // no informations carried
}


/**
 * @XMLfield identifies serializable fields (instance variables, of primitive types or strings). 
 * The annotation has a mandatory argument type, which is the type of the field  
 * and an optional argument name, also of type String, which is the XML tag to be used for the field. 
 * If the argument is not provided, the variable’s name is used as a tag.
 */
@Retention(RetentionPolicy.RUNTIME) // visible at runtime
@Target({ElementType.FIELD})        // annotates fields
@interface XMLfield{
   
    String type ();
    String name () default "";
}