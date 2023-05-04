package ap.xmlserializer;

/**
 * Advanced Programming - Assignment 1.2
 * 
 * This class example uses a security manager and won't be serialized
 * @author marco
 */

@XMLable
public class Teacher {
    @XMLfield(type="String")
    public String name;
    
    @XMLfield(type="String", name="surname")
    public String lastname;
    
    @XMLfield(type="int")
    private int age;
    
    public Teacher() {this("","",0);}
    
    public Teacher(String name, String surname) { 
        this(name, surname, 0);
    }
    
    public Teacher(String name, String surname, int age) { 
        try { System.setSecurityManager(new SecurityManager()); }
        catch (SecurityException e){ } // a security manager is already setted
        this.name = name;
        this.lastname = surname;
        this.age = age;
    }
}
