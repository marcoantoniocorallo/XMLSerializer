package ap.xmlserializer;

/**
 * Advanced Programming - Assignment 1.2
 * 
 * @author marco
 */

@XMLable
public class Singer {
   @XMLfield(type = "String", name = "firstname")
   public String name; // public with option attribute
   
   @XMLfield(type = "String", name = "lastname")
   protected String surname; // protected with option attribute
   
   @XMLfield(type = "int")
   private int age; // private without option attribute
   
   // non-XMLable
   private String producer;
   
   @XMLfield(type = "char", name = "ranking")
   private static char rank; // static char
   
   public Singer(){ 
       // automatically initialize the fields, also the static one   
   }
   
   public Singer(String name, String surname){
       this.name = name;
       this.surname = surname;
       //this.rank = 'S'; // unexpected output: the class is initialized but not the static field!
   }
   
   public Singer(String name, String surname, int age, String producer, char rank){
       this.name = name;
       this.surname = surname;
       this.age = age;
       this.producer = producer;
       this.rank = rank;
   }
}
