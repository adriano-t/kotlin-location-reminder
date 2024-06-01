# Location Reminder

A Todo list app with location reminders that remind the user to do something when he reaches a specific location. The app will require the user to create an account and login to set and access reminders.

## Getting Started

1. Clone the project to your local machine.
2. Open the project using Android Studio.

### Dependencies

```
1. A created project on Firebase console.
2. A create a project on Google console.
```

### Installation

Step by step explanation of how to get a dev environment running.

```
1. To enable Firebase Authentication:
        a. Go to the authentication tab at the Firebase console and enable Email/Password and Google Sign-in methods.
        b. download `google-services.json` and add it to the app.
2. To enable Google Maps:
    a. Go to APIs & Services at the Google console.
    b. Select your project and go to APIs & Credentials.
    c. Create a new api key and restrict it for android apps.
    d. Add your package name and SHA-1 signing-certificate fingerprint.
    c. Enable Maps SDK for Android from API restrictions and Save.
    d. Copy the api key to the `google_maps_api.xml`
3. Run the app on your mobile phone or emulator with Google Play Services in it.
```

## Testing

Right click on the `test` or `androidTest` packages and select Run Tests

### Tests Break Down

1. androidTest
   This directory contains instrumented end-to-end tests running on a real Android device or emulator.

    - RemindersDaoTest.kt
      Tests database operations for saving, retrieving, and deleting reminders.

    - RemindersLocalRepositoryTest.kt
      Verifies repository interactions with the DAO for CRUD operations on reminders.

    - ReminderListFragmentTest.kt
      Ensures the fragment displays reminders correctly and handles user interactions.

    - RemindersActivityTest.kt
      Tests the main activity's initialization, fragment display, and navigation.

2. test
   This directory contains unit tests running on the current machine without needing an Android device.
   They test individual functions to ensure they behave as intended.

    - data/FakeDataSource.kt
      Provides a fake data source for isolated repository and ViewModel testing.

    - reminderlist/RemindersListViewModelTest.kt
      Verifies the ViewModel's logic for loading reminders and handling data states.

    - SaveReminderViewModelTest.kt
      Tests the ViewModel's logic for saving reminders, validating input, and updating UI state.


## Project Milestones
### User Authentication
    [x] The project includes a FirebaseUI dependency
    [x] Import the google-services.json
    [x] Create a Login screen to ask users to login using an email address or a Google account. 
        [x] Upon successful login, navigate the user to the Reminders screen.   
        [x] If there is no account, the app should navigate to a Register screen.
    [x] Authentication is enabled through the Firebase console.
    [x] Create a Register screen to allow a user to register using an email address 
        or a Google account.
### Map View
    [x] Create a Map view that shows the user's current location
        [x] A screen that shows a map and asks the user to allow the location 
            permission to show his location on the map.
        [x] The app works on all the different Android versions including Android Q.
    [x] Create a screen that shows a map with the user's current location and asks the user to select
        a point of interest to create a reminder.
        [x] The app asks the user to select a location or POI on the map and add a new marker
            at that location Upon saving, the selected location is returned to the Save Reminder 
            page and the user is asked to input the title and description for the reminder.
        [x] When the reminder is saved, a geofencing request is created.
    [x] Style the map
        [x] Map Styling has been updated using the map styling wizard to generate a nice looking map
        [x] Users have the option to change map type.
    [x] Display a notification when a selected POI is reached
        [x] When the user enters a geofence, a reminder is retrieved from the local storage 
            and a notification showing the reminder title will appear, even if the app is not open.
### Reminders
    [x] Add a screen to create reminders
        [x] All reminders in the location DB is displayed
        [x] If the location DB is empty, a no data indicator is displayed.
        [x] User can navigate from this screen to another screen to create a new reminder.
        [x] Reminder data should be saved to local storage.
    [x] Display details about a reminder when a selected POI is reached and the user clicked on the notification.
        [x] When the user clicks a notification, when he clicks on it, 
            a new screen appears to display the reminder details.
### Testing
    [x] Use MVVM and Dependency Injection to architect your app.
        [x] The app follows the MVVM design pattern and uses ViewModels to hold the 
            live data objects, do the validation and interact with the data sources.
        [x] The student retrieved the ViewModels and DataSources using Koin.
    [x] Provide testing for the ViewModels, Coroutines and LiveData objects.
        [x] RemindersListViewModelTest or SaveReminderViewModelTest are present in the test package 
            that tests the functions inside the view model.
        [x] Live data objects are tested using shouldReturnError and check_loading testing functions
    [x] Create a FakeDataSource to replace the Data Layer and test the app in isolation.
        [x] Project repo contains a FakeDataSource class that acts as a test double
            for the LocalDataSource.
    [x] Use Espresso and Mockito to test each screen of the app:
        [x] Automation Testing using ViewMatchers and ViewInteractions to simulate 
            user interactions with the app.
        [x] Testing for Snackbar and Toast messages.
        [x] Testing the fragments’ navigation.
        [x] The testing classes are at androidTest package.
        [x] Add testing for the error messages.
    [x] Test DAO (Data Access Object) and Repository classes.
        [x] Testing uses Room.inMemoryDatabaseBuilder to create a Room DB instance.
        [x] inserting and retrieving data using DAO.
        [x] predictable errors like data not found.
    [x] Add End-To-End testing for the Fragments navigation.


## Student Deliverables:

1. APK file of the final project.
2. Git Repository or zip file with the code.

## Built With

* [Koin](https://github.com/InsertKoinIO/koin) - A pragmatic lightweight dependency injection framework for Kotlin.
* [FirebaseUI Authentication](https://github.com/firebase/FirebaseUI-Android/blob/master/auth/README.md) - FirebaseUI provides a drop-in auth solution that handles the UI flows for signing
* [JobIntentService](https://developer.android.com/reference/androidx/core/app/JobIntentService) - Run background service from the background application, Compatible with >= Android O.
