package net.smb.Macros.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.LinkedList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.renderer.Tessellator;

public class Reflection {
    private static Field fieldModifiers = null;
    private static boolean forgeModLoader = false;

    public static void setPrivateValue(Class<?> instanceClass, Object instance, String fieldName, String obfuscatedFieldName, String seargeFieldName, Object value) throws NoSuchFieldException, IllegalArgumentException, SecurityException {
        Reflection.setPrivateValueInternal(instanceClass, instance, Reflection.getObfuscatedFieldName(fieldName, obfuscatedFieldName, seargeFieldName), value);
    }

    public static void setPrivateValue(Class<?> instanceClass, Object instance, String fieldName, Object value) throws NoSuchFieldException, IllegalArgumentException, SecurityException {
        Reflection.setPrivateValueInternal(instanceClass, instance, fieldName, value);
    }

    @SuppressWarnings("unchecked")
	public static <T> T getPrivateValue(Class<?> instanceClass, Object instance, String fieldName, String obfuscatedFieldName, String seargeFieldName) throws NoSuchFieldException, IllegalArgumentException, SecurityException {
        return (T)Reflection.getPrivateValueInternal(instanceClass, instance, Reflection.getObfuscatedFieldName(fieldName, obfuscatedFieldName, seargeFieldName));
    }

    @SuppressWarnings("unchecked")
	public static <T> T getPrivateValue(Class<?> instanceClass, Object instance, String fieldName) throws NoSuchFieldException, IllegalArgumentException, SecurityException {
        return (T)Reflection.getPrivateValueInternal(instanceClass, instance, fieldName);
    }

    private static String getObfuscatedFieldName(String fieldName, String obfuscatedFieldName, String seargeFieldName) {
        if (forgeModLoader) {
            return seargeFieldName;
        }
        return !Tessellator.instance.getClass().getSimpleName().equals("Tessellator") ? obfuscatedFieldName : fieldName;
    }

    private static void setPrivateValueInternal(Class<?> instanceClass, Object instance, String fieldName, Object value) throws NoSuchFieldException, IllegalArgumentException, SecurityException {
        try {
            Field field = instanceClass.getDeclaredField(fieldName);
            int fieldFieldModifiers = fieldModifiers.getInt(field);
            if ((fieldFieldModifiers & 0x10) != 0) {
                fieldModifiers.setInt(field, fieldFieldModifiers & 0xFFFFFFEF);
            }
            field.setAccessible(true);
            field.set(instance, value);
        }
        catch (IllegalAccessException illegalaccessexception) {
            // empty catch block
        }
    }

    public static Object getPrivateValueInternal(Class<?> instanceClass, Object instance, String fieldName) throws NoSuchFieldException, IllegalArgumentException, SecurityException {
        try {
            Field field = instanceClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(instance);
        }
        catch (IllegalAccessException illegalaccessexception) {
            return null;
        }
    }

    public static File getPackagePath(Class<?> packageClass) throws MalformedURLException, URISyntaxException, UnsupportedEncodingException {
        File packagePath = null;
        URL protectionDomainLocation = packageClass.getProtectionDomain().getCodeSource().getLocation();
        if (protectionDomainLocation != null) {
            if (protectionDomainLocation.toString().indexOf(33) > -1 && protectionDomainLocation.toString().startsWith("jar:")) {
                protectionDomainLocation = new URL(protectionDomainLocation.toString().substring(4, protectionDomainLocation.toString().indexOf(33)));
            }
            packagePath = new File(protectionDomainLocation.toURI());
        } else {
            String reflectionClassPath = Reflection.class.getResource("/" + Reflection.class.getName().replace('.', '/') + ".class").getPath();
            if (reflectionClassPath.indexOf(33) > -1) {
                reflectionClassPath = URLDecoder.decode(reflectionClassPath, "UTF-8");
                packagePath = new File(reflectionClassPath.substring(5, reflectionClassPath.indexOf(33)));
            }
        }
        if (packagePath != null && packagePath.isFile() && packagePath.getName().endsWith(".class")) {
            @SuppressWarnings("unused")
			String[] classPathEntries;
            String discoveredPath = "";
            String absolutePath = packagePath.getAbsolutePath();
            String classPath = System.getProperty("java.class.path");
            String classPathSeparator = System.getProperty("path.separator");
            for (String classPathEntry : classPathEntries = classPath.split(classPathSeparator)) {
                try {
                    String classPathPart = new File(classPathEntry).getAbsolutePath();
                    if (!absolutePath.startsWith(classPathPart) || classPathPart.length() <= discoveredPath.length()) continue;
                    discoveredPath = classPathPart;
                }
                catch (Exception ex) {
                    // empty catch block
                }
            }
            if (discoveredPath.length() > 0) {
                packagePath = new File(discoveredPath);
            }
        }
        return packagePath;
    }

    public static <T> LinkedList<Class<? extends T>> getSubclassesFor(Class<T> superClass, Class<?> packageClass, String prefix) {
        block6: {
            try {
                File packagePath = Reflection.getPackagePath(packageClass);
                if (packagePath != null) {
                    LinkedList<Class<? extends T>> classes = new LinkedList<Class<? extends T>>();
                    ClassLoader classloader = superClass.getClassLoader();
                    if (packagePath.isDirectory()) {
                        Reflection.enumerateDirectory(prefix, superClass, classloader, classes, packagePath);
                    } else if (packagePath.isFile() && (packagePath.getName().endsWith(".jar") || packagePath.getName().endsWith(".zip") || packagePath.getName().endsWith(".litemod"))) {
                        Reflection.enumerateCompressedPackage(prefix, superClass, classloader, classes, packagePath);
                    }
                    return classes;
                }
            }
            catch (Throwable th) {
                if (th.getMessage() == null) break block6;
            }
        }
        return new LinkedList<Class<? extends T>>();
    }

    protected static <T> void enumerateCompressedPackage(String prefix, Class<T> superClass, ClassLoader classloader, LinkedList<Class<? extends T>> classes, File packagePath) throws FileNotFoundException, IOException {
        FileInputStream fileinputstream = new FileInputStream(packagePath);
        ZipInputStream zipinputstream = new ZipInputStream(fileinputstream);
        ZipEntry zipentry = null;
        do {
            String className;
            if ((zipentry = zipinputstream.getNextEntry()) == null || !zipentry.getName().endsWith(".class")) continue;
            String classFileName = zipentry.getName();
            className = classFileName.lastIndexOf(47) > -1 ? classFileName.substring(classFileName.lastIndexOf(47) + 1) : classFileName;
            if (prefix != null && !className.startsWith(prefix)) continue;
            try {
                String fullClassName = classFileName.substring(0, classFileName.length() - 6).replaceAll("/", ".");
                Reflection.checkAndAddClass(classloader, superClass, classes, fullClassName);
            }
            catch (Exception ex) {
                // empty catch block
            }
        } while (zipentry != null);
        fileinputstream.close();
    }

    private static <T> void enumerateDirectory(String prefix, Class<T> superClass, ClassLoader classloader, LinkedList<Class<? extends T>> classes, File packagePath) {
        Reflection.enumerateDirectory(prefix, superClass, classloader, classes, packagePath, "");
    }

    private static <T> void enumerateDirectory(String prefix, Class<T> superClass, ClassLoader classloader, LinkedList<Class<? extends T>> classes, File packagePath, String packageName) {
        @SuppressWarnings("unused")
		File[] classFiles;
        for (File classFile : classFiles = packagePath.listFiles()) {
            if (classFile.isDirectory()) {
                Reflection.enumerateDirectory(prefix, superClass, classloader, classes, classFile, packageName + classFile.getName() + ".");
                continue;
            }
            if (!classFile.getName().endsWith(".class") || prefix != null && !classFile.getName().startsWith(prefix)) continue;
            String classFileName = classFile.getName();
            String className = packageName + classFileName.substring(0, classFileName.length() - 6);
            Reflection.checkAndAddClass(classloader, superClass, classes, className);
        }
    }

    @SuppressWarnings("unchecked")
	protected static <T> void checkAndAddClass(ClassLoader classloader, Class<T> superClass, LinkedList<Class<? extends T>> classes, String className) {
        if (className.indexOf(36) > -1) {
            return;
        }
        try {
            Class<?> subClass = classloader.loadClass(className);
            if (subClass != null && !superClass.equals(subClass) && superClass.isAssignableFrom(subClass) && !classes.contains(subClass)) {
                classes.add((Class<? extends T>) subClass);
            }
        }
        catch (Throwable th) {
            // empty catch block
        }
    }

    static {
        forgeModLoader = ClientBrandRetriever.getClientModName().contains("fml");
        try {
            fieldModifiers = Field.class.getDeclaredField("modifiers");
            fieldModifiers.setAccessible(true);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }
}

