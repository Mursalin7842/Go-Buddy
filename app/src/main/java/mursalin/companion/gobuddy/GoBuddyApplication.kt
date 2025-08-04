// FILE: app/src/main/java/mursalin/companion/gobuddy/GoBuddyApplication.kt
// NOTE: Rename your file from "GoBuddyAplication.kt" to "GoBuddyApplication.kt"
/*
 * PRODUCTION: The Application class is annotated with @HiltAndroidApp to enable
 * dependency injection for the entire application. The class itself is empty
 * as Hilt handles the initialization logic defined in the 'di' modules.
 */
package mursalin.companion.gobuddy

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GoBuddyApplication : Application()
