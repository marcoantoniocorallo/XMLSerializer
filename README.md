# XMLSerializer
_Advanced Programming_ assignment about Java Reflection and Annotations.

This assignment asks to implement a Java serializer to export an array of objects in XML format.
This is not a simple API for XML serialization: **XMLSerializer** provides a static method `void serialize(Object [] arr, String fileName)` that serialize (into the file `fileName.xml`) each object in `arr` _introspecting_ its class searching for information provided using the `XMLAnnotation` annotation.

`@XMLable` provides information about the class. The presence of this annotation says that the objects of this class should be serialized. 
In this case the corresponding element will contain other elements for the instance variables, if any. 
If instead the annotation is absent, the element corresponding to the object must contain only the empty element `<notXMLable />`.

`@XMLfield` identifies serializable fields. The presence of this annotation states that the field must be serialized. The annotation has a mandatory argument type, which is the type of the field, and an optional argument name, also of type String, which is the XML tag to be used for the field. If the argument is not provided, the variable’s name is used as a tag.
