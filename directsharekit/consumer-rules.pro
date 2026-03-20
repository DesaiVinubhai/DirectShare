# Consumer ProGuard rules for DirectShareKit library.

# Keep all DirectShareKit classes and members
-keep class com.csiw.directsharekit.** { *; }

# Keep ShareManager methods (public API)
-keepclassmembers class com.csiw.directsharekit.ui.ShareManager {
    public static *** *(...);
}


# Keep FileRepository class
-keepclassmembers class com.csiw.directsharekit.data.FileRepository {
    public *** *(...);
}

# Keep ShareUseCase class and inner classes
-keepclassmembers class com.csiw.directsharekit.domain.ShareUseCase {
    public *** *(...);
    public static class **;
}

# Keep all utility classes
-keepclassmembers class com.csiw.directsharekit.utils.** {
    public static *** *(...);
    public *** *(...);
}

# Keep FileProviderHelper
-keepclassmembers class com.csiw.directsharekit.data.FileProviderHelper {
    public static *** *(...);
}

# Keep constants
-keepclassmembers class com.csiw.directsharekit.utils.Constants {
    public static final *** *;
    public static class **;
}

# Preserve line numbers for debugging
-keepattributes SourceFile,LineNumberTable

# Preserve Kotlin metadata
-keepattributes RuntimeVisibleAnnotations,AnnotationDefault
