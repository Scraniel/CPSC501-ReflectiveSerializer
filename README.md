# CPSC501-ReflectiveSerializer
An object serializer / deserializer that operates on arbitrary objects using reflection.

It is made up of 2 programs, the sender (which serializes data) and the receiver (which deserializes data). They are meant to be run on 2 separate computers so as to show that the data is sent accross a network.

To use, run reciever.Server on the receiving side, and then sender.ObjectCreator on the sending side. The easiest way to do this (and how I tested it) is to run them using Eclipse. I've included the relevant .project files so you can simply import the project and voila. I have also included the version of JDOM I used in the JDOM folder. If you are not running through eclipse, remember to add this to your classpath before running. 
