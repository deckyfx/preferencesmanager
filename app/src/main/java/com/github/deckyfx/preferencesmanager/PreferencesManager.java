package com.github.deckyfx.preferencesmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by decky on 9/9/16.
 */
public class PreferencesManager {
    private static final String DEFAULT_SHARED_PREFERENCES_NAME         = "_preferences";
    private Context mContext;
    private HashMap<String, Preferences> mPreferences;

    public PreferencesManager(Context context) {
        this.mContext = context;
        android.content.SharedPreferences defaultPref = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        this.mPreferences = new HashMap<String, Preferences>();

        Preferences defaultPreference = new Preferences(this.mContext, this.getName(DEFAULT_SHARED_PREFERENCES_NAME));
        defaultPreference.setPreferences(defaultPref);
        this.add(this.getName(DEFAULT_SHARED_PREFERENCES_NAME), defaultPreference);
    }

    public Preferences add(String name) {
        Preferences preferences = new Preferences(this.mContext, this.getName(name));
        preferences.refresh();
        this.add(this.getName(name), preferences);
        return preferences;
    }

    public void add(String name, Preferences pref) {
        this.mPreferences.put(name, pref);
    }

    public Preferences get(String name){
        if (name == null ) {
            name = "";
        }
        if (name.length() == 0) {
            name = DEFAULT_SHARED_PREFERENCES_NAME;
        }
        name     = this.getName(name);
        Preferences pref = this.mPreferences.get(name);
        pref.refresh();
        return pref;
    }

    public Preferences getDefault(){
        return this.get(null);
    }

    public String getName(String name){
        return this.mContext.getPackageName() + "." + name;
    }

    public class Preferences {
        private Context mContext;
        private android.content.SharedPreferences mPreferences;
        private android.content.SharedPreferences.Editor mEditor;
        public String name;

        public Preferences(Context context, String name){
            this.mContext = context;
            this.name = name;
        }

        public void refresh() {
            this.mPreferences = this.mContext.getSharedPreferences(this.mContext.getPackageName() + this.name, Context.MODE_PRIVATE);
        }

        public void setPreferences(android.content.SharedPreferences preferences) {
            this.mPreferences = preferences;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void loadDefaultValue(int resource) {
            PreferenceManager.setDefaultValues(this.mContext, resource, true);
            Map<String, ?> allPreferences = this.mPreferences.getAll();
            for (Map.Entry<String, ?> entry : allPreferences.entrySet()) {
                String type = entry.getValue().getClass().getName();
                this.set(entry.getKey(), entry.getValue());
            }
        }

        public void set(String name, Object value){
            if (value == null) {
                value = "";
            }
            String type = value.getClass().getName();
            this.mEditor = this.mPreferences.edit();
            if (type.equals(String.class.getCanonicalName())) {
                String v = (String) value;
                this.mEditor.putString(name, v);
            } else if (type.equals(Boolean.class.getCanonicalName())) {
                Boolean v = (Boolean) value;
                this.mEditor.putBoolean(name, v);
            } else if (type.equals(Float.class.getCanonicalName())) {
                Float v = (Float) value;
                this.mEditor.putFloat(name, v);
            } else if (type.equals(Integer.class.getCanonicalName())) {
                int v = (Integer) value;
                this.mEditor.putInt(name, v);
            } else if (type.equals(Long.class.getCanonicalName())) {
                long v = (Long) value;
                this.mEditor.putLong(name, v);
            }
            this.mEditor.commit();
            this.mEditor.apply();
        }

        public Object get(String name){
            return this.getAll().get(name);
        }

        public Map<String, ?> getAll(){
            return this.mPreferences.getAll();
        }

        public boolean getBoolean(String name){
            return this.mPreferences.getBoolean(name, false);
        }

        public float getFloat(String name){
            return this.mPreferences.getFloat(name, 0);
        }

        public int getInt(String name){
            return this.mPreferences.getInt(name, 0);
        }

        public long getLong(String name){
            return this.mPreferences.getLong(name, 0);
        }

        public String getString(String name){
            return this.mPreferences.getString(name, "");
        }

        public Set<String> getStringSet(String name){
            return this.mPreferences.getStringSet(name, null);
        }

        public boolean contains(String name){
            return this.mPreferences.contains(name);
        }

        public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener){
            this.mPreferences.registerOnSharedPreferenceChangeListener(listener);
        }

        public void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener){
            this.mPreferences.unregisterOnSharedPreferenceChangeListener(listener);
        }

        public void clear(String key) {
            this.mEditor.remove(key);
            this.mEditor.commit();
            this.mEditor.apply();
        }

        public void clear() {
            this.mEditor.clear();
            this.mEditor.commit();
            this.mEditor.apply();
        }

        public boolean isEmpty() {
            int size = this.getAll().size();
            return size == 0;
        }
    }
}
