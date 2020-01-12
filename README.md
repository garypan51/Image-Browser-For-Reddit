# Image Browser For Reddit

The following app displays images from imaged based subreddits. 
It is meant to demonstrate how to make api calls to Reddit and parse json response files.
Essentially the app displays images from a post (if the post contains an image), and allows 
for users to see comments from that post.


## Building the App

### Pre-requisites

* Android Studio 3.5+
* Android SDK 29
* AndroidX

### Clone the repo:

`git clone https://github.com/garypan51/Image-Browser-For-Reddit.git`

### Android Studio (Recommended version 3.5)

* From the Android Studio Launcher select `Open an existing Android Project` and navigate to the root directory of your project.
* Click 'OK' to open the the project in Android Studio.
* A Gradle sync should start, if not then force gradle sync by re-building project `Build -> Rebuild Project`


## Running the App

Connect an Android device to your development machine if you wish to run the app on a physical device.

Create an Android Emulator if one doesn't already exist to run the app on a emulator.

* Select `Run -> Run 'app'` from the menu bar
* Select the device/emulator you wish to run the app on and click 'OK'


## Estimated RoadMap

The following are a list of items that should be worked on sorted by the order of priority and/or importance.
1. UNIT TESTS!!! (Q1 2020)
    * Make sure that every viewModel class has it's own unit tests.
2. UI Redesign (Q1 - Q2 2020)
    * Rework design to be user friendly.
        * Possibly remove the option to switch to staggeredView as it makes the images look to clusered up. Consider switching to a gridview instead.
    * Incorporate more material design.
        * Switch colors and elements such as the container that has the image or the comments popup.   
3. Architecture Refactor (Q2 - Q3 2020)
    * Migrate to Gradle Kotlin.
    * Better Repository, api, and viewmodel structure.
    * Add dagger for dependency injection for repositories, viewModels, and fragments.
4. Add More Features (Q3 - Q4 2020)
    * Add Oauth and Sign In to fetch subreddits of a user.
    * Add theme switching based on whether user has dark mode enabled on Android 10 devices(currently switching from light to dark mode is controlled in the settings menu of the app).
    * Add Search for Subreddits functionality.
    * Add user related functionalities such as commenting, liking, subscribing to a Subreddit, etc.
    * Allow users to save image to their device.

## License

This project is licensed under the MIT License, so feel free to use it as you wish. See LICENSE file for more details.

## Notice 

The Following software/libraries were utilized. See NOTICE file for more details.

* Android Open Source Project
* Kotlin
* Android JetPack
    * ViewModels
    * Paging Library
* RetroFit
* Glide
* Gson
* RxJava
* PhotoView
* Circle Indicator
