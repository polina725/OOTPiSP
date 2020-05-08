package sample.pluginSupport;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


public class PluginLoader {
    private ArrayList<Plugin> plugins;
    private final String pluginDirectory="src/plugins";//"C:\\Users\\Polina\\IdeaProjects\\Project1\\plugins";
    private ArrayList<String> allFileExtensionEndings;
    private ArrayList<String> allPluginNames;

    public ArrayList<Plugin> getPlugins() {
        return this.plugins;
    }

    public ArrayList<String> getAllFileExtensionEndings() {
        return this.allFileExtensionEndings;
    }

    public ArrayList<String> getAllPluginNames() {
        return this.allPluginNames;
    }

    public PluginLoader(){
        loadPlugins();
        this.allPluginNames= new ArrayList<>();
        this.allFileExtensionEndings = new ArrayList<>();
        for (Plugin pl : this.plugins){
            this.allPluginNames.add(pl.getEncodingName());
            this.allFileExtensionEndings.add(pl.getFileExtensionEnding());
        }
    }

    private void loadPlugins(){
        ArrayList<String> jars=getAllJarFiles();
        this.plugins = new ArrayList<>();
        for (String jarName:jars){
            Plugin plugin;
            try {
                plugin = findPluginImplementationClass(this.pluginDirectory+"/"+jarName);
            } catch (IOException e) {
                continue;
            }
            if (plugin!=null)
                this.plugins.add(plugin);
        }
      // return plugins;
    }

    private ArrayList<String> getAllJarFiles(){
        File dir = new File(this.pluginDirectory);
        String[] jars=dir.list();
        ArrayList<String> pluginsFiles=new ArrayList<>();
        if (jars != null)
            for(String fileName : jars)
                if(fileName.endsWith(".jar"))
                    pluginsFiles.add(fileName);
        return pluginsFiles;
    }

    private Plugin findPluginImplementationClass(String jarName) throws IOException {
        URL[] urls = new URL[]{ new URL("jar:file:" +jarName+"!/") };
        URLClassLoader cl = URLClassLoader.newInstance(urls);

        JarFile jarFile = new JarFile(jarName);
        Enumeration<JarEntry> e = jarFile.entries();

        Plugin pf = null;
        while (e.hasMoreElements()) {
            JarEntry je = e.nextElement();
            if(je.isDirectory() || !je.getName().endsWith(".class")){
                continue;
            }

            String className = je.getName().substring(0,je.getName().length()-6);
            className = className.replace('/', '.');
            Class<?> c = null;
            try {
                c = cl.loadClass(className);
            } catch (ClassNotFoundException ignored) {}

            if (c!=null) {
                Class<?>[] intfArr = c.getInterfaces();
                for (Class<?> intf : intfArr) {
                    if (intf.getName().contains("Plugin")) {
                        try {
                            pf = (Plugin) c.getDeclaredConstructor().newInstance();
                         //   System.out.println("loaded " + pf.getClass() + " from " + jarFile.getName());
                        } catch (InstantiationException | IllegalAccessException |
                                InvocationTargetException | NoSuchMethodException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }

        }
        return pf;
    }
}
