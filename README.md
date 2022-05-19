# Booking App Viewer

## Instructions

You should be able to run the app right away.
The app is built on:

`
Android Studio Chipmunk | 2021.2.1
Build #AI-212.5712.43.2112.8512546, built on April 28, 2022
`

The project uses the following dependencies versions:

`Kotlin`: 1.6.10 
`AGP`: 7.2.0
`Gradle Wrapper`: 7.3.3

To be able to run the project you might need to upgrade some of your tools.

## Architectural choices
The project is structured using a simple implementation of the `MVI` architecture,
which transforms Intents (interactions) into a `View State` which will be observed by the view as a single source of truth (`SSOT`).

I have chosen MVI architectural pattern for a few reasons:
1. It offers `SSOT` (the `View State`) unlike `MVVM` architecture, where ViewModel (`VM`) can have multiple streams (`LiveData`, `StateFlow`, others.)
2. It's easier to debug, since it only deals with a `SSOT`.

`MVI` actors:
- `Reducer`: responsible for holding and processing state.
- `VM`: fetches data from the data sources, presentation layer business-logic, and handles events from the UI layer transforming them into Intentions passed to the `Reducer`.

This project also uses `Clean architecture` (layered) for better separation of concerns and easier refactoring.

This approach introduces a little bit more of boilerplate compared to other architectural options (read MVVM), but it gives flexibility and agility at scale.

## The app

Single screen app, displays a CalendarView.
For brevity, I chose to use the following library: https://github.com/kizitonwose/CalendarView 

It provides an implementation of calendar view with possibility to add events and display them as dots underneath each day view.
The library is one of the few kotlin libraries providing similar APIs.

Some of the available Jetpack Compose libraries available have been considered. However some of the features required by the exercise weren't built-in in those libraries (still in development).

I then decided to find a trade-off between implementing a whole CalendarView from scratch (or extending existing Compose library) and being able to provide a solution to the exercise in a reasonable amount of time.

This is how the single screen app looks like:
<figure>
    <img src="/snapshot/image.png" width="300"/>
</figure>

## Next steps

1. Consider building a custom Calendar View to have full control over it, for better performance, customisation and adaptation to more specific product requirements.
2. Add ability to add events from UI and add them to device Calendar.
3. Integrate a remote data source for real time updates.
4. Enable scroll action between months. (Intentionally disabled for the purposes of this exercise)
5. Implement call to action on event ui element (e.g. navigate to details screen)
6. Implement google directions to venue from current/last known location.
7. Add more unit tests (i.e. test DataSource)

## Considerations

The current implementation presents some drawbacks since the view gets recreated everytime a date is tapped.
This is mainly because of the limitation of the library used.
However, with more time and as a next step, I would like to implement a custom solution as mentioned at point 1. of the previous section.




