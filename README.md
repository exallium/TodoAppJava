# TodoAppJava

The purpose of this application is to demonstrate an approach to building out a small application without utilizing any third-party libraries.

The technologies in this app are as follows:

1. Room for Database management and access
1. LiveData for Observable data streaming
1. RecyclerView for displaying a sequential list of items
1. AsyncTask for submitting tasks to a background thread (Database Access)
1. ViewModel for managing presentation logic and mediating data access.
1. Dagger to piece everything together and allow me to hide object construction

All of the above are distributed by Google.

Robolectric is utilized for Unit Testing, which is technically third party, but this is only touching my tests, and is recommended by Google.

## Architecture

The Application has a very simple architecture, based on Clean design principles. Basically, the primary goal was to make sure my dependency arrows were always pointing in the right direction.

1. Android Activity serves as a LifecycleOwner for purposes of mediating LiveData observation
1. TodoListAdapter is responsible for displaying and manipulating TodoListItems, via it's internals (Its ViewHolder and focus management code)
1. ViewModel is responsible for maintaining the state of our screen, which is essentially the list of TodoListItems as well as the currently focused position.
1. ViewModel passes through data updates to it's Contract.
1. This Contract can be thought of as the 'Model' in MVVM or MVP.  Essentially, it is a descriptive interface that lets us interact with the rest of the system.
1. In our case, this contract gets a Dao to communicate with the Database, and has access to AsyncTask implementations to do work off of the foreground thread.
1. By Default, AsyncTask runs tasks in a Serialized fashion, so we don't need to worry about maintaining our own job queue.
1. All of these pieces are strung together utilizing Dagger (see the di package)

## Notes

* Please note that this is just an example implementation. There are a million ways to write an Android application, and a lot of them are good enough. This is not and will not be intended for any kind of actual app release.
* Normally, I would break things out into Feature packages. Given that this was just a single screen, I decided that this was unnecessary.
