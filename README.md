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

### Break Down Tests

Explain what each test does and why

```
1.androidTest
        //TODO: Students explain their testing here.
2. test
        //TODO: Students explain their testing here.
```

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
    [] Create a screen that displays the reminders retrieved from local storage.
        [] If there are no reminders, display a  "No Data"  indicator. 
        [] If there are any errors, display an error message.
    [] Create a screen that shows a map with the user's current location and asks the user to select
        a point of interest to create a reminder.
        [] The app asks the user to select a location or POI on the map and add a new marker
            at that location Upon saving, the selected location is returned to the Save Reminder 
            page and the user is asked to input the title and description for the reminder.
        [] When the reminder is saved, a geofencing request is created.
    [] Create a screen to add a reminder when a user reaches the selected location.
        Each reminder should include
        a. title
        b. description
        c. selected location
    [x] Style the map
        [x] Map Styling has been updated using the map styling wizard to generate a nice looking map
        [] Users have the option to change map type.
    [] Display a notification when a selected POI is reached
        [] When the user enters a geofence, a reminder is retrieved from the local storage 
            and a notification showing the reminder title will appear, even if the app is not open.
### Reminders
    [] Add a screen to create reminders
        [] All reminders in the location DB is displayed
        [] If the location DB is empty, a no data indicator is displayed.
        [] User can navigate from this screen to another screen to create a new reminder.
        [] Reminder data should be saved to local storage.
    [] Display details about a reminder when a selected POI is reached and the user clicked on the notification.
        [] When the user clicks a notification, when he clicks on it, 
            a new screen appears to display the reminder details.
### Testing
    [] Use MVVM and Dependency Injection to architect your app.
        [] The app follows the MVVM design pattern and uses ViewModels to hold the 
            live data objects, do the validation and interact with the data sources.
        [] The student retrieved the ViewModels and DataSources using Koin.
    [] Provide testing for the ViewModels, Coroutines and LiveData objects.
        [] RemindersListViewModelTest or SaveReminderViewModelTest are present in the test package 
            that tests the functions inside the view model.
        [] Live data objects are tested using shouldReturnError and check_loading testing functions
    [] Create a FakeDataSource to replace the Data Layer and test the app in isolation.
        [] Project repo contains a FakeDataSource class that acts as a test double
            for the LocalDataSource.
    [] Use Espresso and Mockito to test each screen of the app:
        [] Automation Testing using ViewMatchers and ViewInteractions to simulate 
            user interactions with the app.
        [] Testing for Snackbar and Toast messages.
        [] Testing the fragments’ navigation.
        [] The testing classes are at androidTest package.
        [] Add testing for the error messages.
    [] Test DAO (Data Access Object) and Repository classes.
        [] Testing uses Room.inMemoryDatabaseBuilder to create a Room DB instance.
        [] inserting and retrieving data using DAO.
        [] predictable errors like data not found.
    [] Add End-To-End testing for the Fragments navigation.


## Student Deliverables:

1. APK file of the final project.
2. Git Repository or zip file with the code.

## Built With

* [Koin](https://github.com/InsertKoinIO/koin) - A pragmatic lightweight dependency injection framework for Kotlin.
* [FirebaseUI Authentication](https://github.com/firebase/FirebaseUI-Android/blob/master/auth/README.md) - FirebaseUI provides a drop-in auth solution that handles the UI flows for signing
* [JobIntentService](https://developer.android.com/reference/androidx/core/app/JobIntentService) - Run background service from the background application, Compatible with >= Android O.

## License
