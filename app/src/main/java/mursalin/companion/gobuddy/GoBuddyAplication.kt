// FILE: app/src/main/java/mursalin/companion/gobuddy/GoBuddyApplication.kt
/*
 * The Application class is the entry point of the app. It's a good place
 * for initializing singletons or libraries that need a context. Here we will
 * initialize our Appwrite client.
 */
package mursalin.companion.gobuddy

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GoBuddyApplication : Application()