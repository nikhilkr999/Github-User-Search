# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile


#############################################################
# ✅ Custom rules to remove debug logs in release build
#############################################################

# Remove all Log calls (v, d, i, w, e)
-assumenosideeffects class android.util.Log {
    public static int v(...);
    public static int d(...);
    public static int i(...);
    public static int w(...);
    public static int e(...);
}

# Optional: if using Timber or other logging libraries
# (uncomment if applicable)
#-assumenosideeffects class timber.log.Timber {
#    public static void d(...);
#    public static void v(...);
#    public static void i(...);
#    public static void w(...);
#    public static void e(...);
#}

#############################################################
# ✅ You can add more custom rules below if needed later
#############################################################
