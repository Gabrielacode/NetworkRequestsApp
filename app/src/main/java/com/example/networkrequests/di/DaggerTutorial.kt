package com.example.networkrequests.di

//Dagger mostly uses abstraction to create the objects or dependencies we need
//We will start with Components
//A component is an interface or abstract class that contains the dependencies we need
//We annotate with the @Component annotation
//When we need the dependencies we need
//We can call dagger  to create the implementation of the component  by either a component builder or factory if provided or we use the default dagger way
//From the implementation we can get the dependencies we need as they are created by Dagger
//Dagger creates all the objects for us
//We specify a function that returns the object we need
//When a dependencies also depend on other objects Dagger needs to know a way to create these objects
// We use the @Inject annotation to do that
//we use the @Inject on constructors , methods or fields
//We should not  mistake it for its literal term  like inject
//When we apply @Inject on a constructor of a class  we tell Dagger how to create an instance of the class if needed
// as dependency of another class
//In the constructor of the class we put the @Inject annotation they may also be other dependencies
//These dependencies should also have an @Inject Constructor so that Dagger can create them for the object we want and pass them
//So it turns to recursive creation of dependencies are created for dependencies
//We can also the Inject annotation on fields of a class
//Dagger uses the same way as Constructors and creates the objects and puts them into the fields
//If u dont put @Inject on a  constructor of class
//Dagger will not know a  way to create an instance of a class
//But @Inject does not work everywhere
//1 Interfaces cannot be constructed
//2 Third Party class or libraries cannot be annotated
//and Configurable class must be configured
//so how do we do
//For interfaces for example we can pass an interface as a parameter of a constrctor
//But how will Dagger know how to create a class that implements an interface
//@Binds  and @Provides come into play
//These annotations helps us provides methods that help to provides of a class or
//implementations of interfaces that cannot be annotated with Inject or need to be configured
//The @Binds is put on a abstract method whose return type defines the implementation we should inject when it is needed
//The parameter is what is passed
//For example
/*
@Module
interface SamsungTvModule{
@Binds
  ->in Java
 abstract Tv samsungTv(SamsungTv tv);
 ->In Kotlin
 abstract samsungTv(tv:SamsungTv):Tv
 }

 This tells Dagger that an dependency or object that needs a  Tv  should be given the SamsungTv
 The SamsungTv should be a class that has Inject constructor so that Dagger knows how to create it
 so if there is a class  like
 class Room{
 Tv tv;
 @Inject
 Room (Tv tv){
 this.tv = tv
 }
 }
 Then when Dagger is creating the class Dagger will pass the Samsung Tv
 since the Tv interface cannot be created as it cannot be annotated
 so the method is abstract as Dagger doesnt need to create an implementation of the method
 It just need the return type to know what type is depended on and the parameter to know what to provide when taht type is depended on
 //But the bind methods cannot be put in a component
 //They need to be put in an interface or abstract class that with annotated with a @Module
The Module classes , abstract  classes or interfaces contains all the binds and provides methods
a component needs to resolve dependencies
We pass the class type in the parameter of the @Component
for example
@Component(modules = SamsungTvModule::class.java)
interface LivingRoomComponent{
inJava -> Room livingRoom();
inKotlin fun livingRoom():Room
}

It is good practice to prefix @Bind methods  with bind  and @ Provides methods with provide, and suffix modules with Module   within their names
//These modules allows flexibility in the dependency injection
Let us now look at Provides
We should favour abstraction if possible a it allows for flexibility and re-usability
@Provides is similar to the @Bind
But here it is a method that has an implementation
Just like Bind , the return type is the type we want
//So when there is a dependency of that type Dagger goes to that module and
// calls the Provides method which returns  an implementation of that type
//Here we can pass a parameter or not
//They dont need to create a new instance of the type
//And they can be good for configurable objects or third party librarys

Now we have learnt about Bindings and Components and Modules
Let us learn about MultiBinding
Multi bindings allows also us to use different objects from different modules into a collection like a set or a map
without depending on a single module
   For now it is applied to sets and maps

   Set Multibings
   for example if our components needs a set as a dependency
   We can create a module
   We can then create a provides method that returns a implementation of a set of particular type
   or create many modules that contribute thier own set elements and are collected and used in the component by dagger when a set of type dependcy is need

   Lets see an example
   lets us say we have a component
   @Component
   interface ListOfName{
     Set<String> names();
   }

   We can different modules that provides a provide method which either returns a string or a set of strings
   for example
   @Module
   class ModuleName{

   @Provide
   @IntoSet ->This tells Dagger that this methods returns a string that can be added to an injectable multibound  set of strings
   static String provideAName(){
     return "Gabriel"
   }
   }
    @Module
   class ModuleNames{

   @Provide
   @ElementsIntoSet ->This tells Dagger that this methods returns a set of strings  that can be added to an injectable multibound  set of strings when need as a dependency
     return setOf("Obi","Paul","Emmanuel")
   }
   }
   Now we will add the modules to our component

   @Component(modules = [ModuleNames::class.java,ModuleName::class.java])
   interface ListOfName{
     Set<String> names();
   }
   it will return a set of strings containing "Gabriel","Obi","Paul","Emmanuel"
   when a set of strings need
   This creates a kind of Plugin system where when we need extra functionality we can add it to a by just specifying a module


  We can also do for maps
  As we know a map is a list of a key -values
  When we need
  But in Dagger we pass the keys as an annotation to the provide method
  The keys should not clash
  There are already in built annotaions for string , int , primitive and class keys
   for example
   @Module
   interface SamsungTvModule{
    @Provides
    @IntoMap
    @StringKey("samsung")
     static Tv provideSamsungTv(){
     return SamsungTv();
     }
   }
    @Module
   interface LgTvModule{
    @Provides
    @IntoMap
    @StringKey("lg")
     static Tv provideLGTv(){
     return LGTv();
     }
   }
   @Component(modules = LgTvModules::class.java,SamsungTvModule::class.java)
   interface LivingRoom{
      Map<String,Tv> listOfTvs();
   }
   This will return a map that has keys of samsung and lg
   and values of SamsungTv and LgTv
   We can also create custom key annotations or key annotations that have multiple values
   and many more in the documentation

   NOTE : javax inject package is used as a standard for dependency injection in Java
   just like Persistence Api was used as the standard for ORMs
   This is good as we can know that most frameworks use the same annotations like @Inject and @Singleton

   Let us look at one of the scope functions
   Scope functions   like Singletons define the scope of dependencies They are defined in the javax.inject package

   Let us look at
   the @Singleton annotation tells Dagger that the only one instance of that type should be used when there is a dependency of that type
  We can put them on a class that has @Inject constructor or a @Binds or @provides methods
  So anytime an instance of that  type is needed then if one is available it is used
  if not it is created
  We can also annotate components with @Singleton tells dagger that the classes annotated with this type are bound to the lifecycle of the component
  and the same instance is returned when the type is requested
  so if a create a custom scope and annotate it to a type and also a component
  So an instance of the type is provided per component

  Let us look at subcomponents
   As we know that a component manages a dependency graph and also uses a modules to provide bindings for dependencies
   Sub components are also components that have have their own dependency graph and modules but also have have access to  the bindings of the parent graph and also its own bindings in its modules
   But a parent component doesnt have binding of its children modules
   Sibling components cannot depend on each other bindings
   Just like Super classes and Sub Classes

   When adding sub components to a parent component we specify the subcomponent in Module  we want to install into the component
   for example
   @Module(subcomponents=Calls::class.java)
   interface Vars{}

   @Component(module= Vars::class.java)
   interface ShoeComponent{
   }

    This also use ful when using scopes
    as Two sub components can share the same scope but a child component cannot share the scope with its parent
The @BindInstance is normally passed into the factory  or builder of a component
It tells Dagger that the instance of the typ we passed will be used in the component will be used when any request of the type in the component is need
So if Dagger needs to inject an instance the type it uses that type we passed

Qualifiers in Dagger help us to differentiate between instances of a type that Dagger will pass based on the name of the qualifier
Here we can help differentiate what instance of the type is injected based on the qualifier name
We can create them by our annotation class having the @Qualifier annotation
for example

@Qualifier
@Retention
@Documented
public @interface CouchName{
String name() default""
}
then in our module
@Module
interface CouchModule{
@Binds
@CouchName("Fluffy")
 abstract Couch provideFluffyCouch(FluffyCouch couch)

 @Binds
@CouchName("Flat")
 abstract Couch provideFlatCouch(FlatCouch couch)
}

Now in our dependency
class Living Room{
@Inject LivingRoom (
@Couch("Fluffy) Couch flfuf,
@Couch("Flat") Couch flayt)

Here Dagger will inject a Fluffy Couch for the Qualifier with a name Fluffy parameter
and a Flat Couch for the Flat Couch Qualifier


Now lets us look at scopes
We can create custom scopes by creating an annotation class that has the annotation @Scope in it
Scopes tell Dagger to share the same instance of the type among allrequests or dependents of that type in a subcomponent or component that shares the same scope annotation

So @Singleton is just a scope annotation
The lifecycle of the instance is tied to the lifetime of the component withinthe scope
The name is completly meaningless
Child components have the multi bindings of their parents as well as themselves
BindsOptionalOf annotation tells Dagger to create an instance of atype if it knows how to create it or return null
That means if the type has an inject constructor

We will use Dagger First b4 Dagger Hilt
Let us try to determine a graph of all the components
          Application -We need a  component
             |
          Activity - We need a component for its viewmodel  and itself
             |
      Both Remote andCache Fragment   - It is the most recent fragment


Now lets create a Application component
}


We will now use Dagger Hilt
Yay
Dagger Hilt is just the same as Dagger
In Dagger Hilt , the plugin is used to generate components
forus to start we need to annotate ou app class with @HiltApplication
We will move our modules
* */

//