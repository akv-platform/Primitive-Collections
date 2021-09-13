# Primitive-Collections

This is a Simple Primitive Collections Library i started as a hobby Project.     
It is based on Java's Collection Library and FastUtil.     
But its focus is a different one.     

## Main Features:     
- ArrayLists / LinkedLists
- HashSet/Map (Linked & HashControl)
- TreeSet/Map (RB & AVL)
- EnumMap
- Immutable Maps/Lists/Sets
- Priority Queue
- Streams
- SplitIterators
- Iterators

# Notes about Versions
Any 0.x.0 version (Minor) can be reason for massive changes including API.     
To ensure that problems can be dealt with even if it is breaking the current API.     
Any breaking changes will be Documented (once 1.0 is released)     

# How to install
Using Gradle:
```gradle
repositories {
    maven {
        url = "https://maven.speiger.com/repository/main"
    }
}
dependencies {
	compile 'de.speiger:Primitive-Collections:0.3.5'
}
```
Direct:

| Version 	| Jar                                                                                                                          	| Sources                                                                                                                              	| Java Doc                                                                                                                             	|
|---------	|------------------------------------------------------------------------------------------------------------------------------	|--------------------------------------------------------------------------------------------------------------------------------------	|--------------------------------------------------------------------------------------------------------------------------------------	|
| 0.3.6   	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.3.6/Primitive-Collections-0.3.6.jar) 	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.3.6/Primitive-Collections-0.3.6-sources.jar) 	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.3.6/Primitive-Collections-0.3.6-javadoc.jar) 	|
| 0.3.5   	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.3.5/Primitive-Collections-0.3.5.jar) 	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.3.5/Primitive-Collections-0.3.5-sources.jar) 	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.3.5/Primitive-Collections-0.3.5-javadoc.jar) 	|
| 0.3.4   	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.3.4/Primitive-Collections-0.3.4.jar) 	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.3.4/Primitive-Collections-0.3.4-sources.jar) 	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.3.4/Primitive-Collections-0.3.4-javadoc.jar) 	|
| 0.3.3   	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.3.3/Primitive-Collections-0.3.3.jar) 	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.3.3/Primitive-Collections-0.3.3-sources.jar) 	| [Download](https://maven.speiger.com/repository/main/de/speiger/Primitive-Collections/0.3.3/Primitive-Collections-0.3.3-javadoc.jar) 	|

# Contributing
If you want to contribute.      
This project is created using gradle and java and my Template Library only. Nothing extra.      
If you setup gradle the library will be downloaded automatically.      

Where is everything stored?
- Variables and ClassNames are define [here](https://github.com/Speiger/Primitive-Collections/blob/master/src/builder/java/speiger/src/builder/GlobalVariables.java)
- Templates are stored [here](https://github.com/Speiger/Primitive-Collections/tree/master/src/builder/resources/speiger/assets/collections/templates)
- Tests can be found [here](https://github.com/Speiger/Primitive-Collections/tree/master/src/test/java/speiger/src/collections)

Please if you want to contribute follow the [Rule-Sheet](https://github.com/Speiger/Primitive-Collections/blob/master/RuleSheet.md). It keeps everything in line.


# How to Build

The SourceCode can be generated via:     
/gradlew.bat generateSource      

to build the jar:           
/gradlew.bat build      
do not combine the commands because they can not be executed at the same time.      

## Current Down Sides (Random order)
- Testing for Sub Maps/Sets/Lists are only in a very basic way tested
- Documentation is only present at the lowest level for most cases and needs a typo fixing.