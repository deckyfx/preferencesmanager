package com.github.deckyfx.preferencesmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by decky on 9/9/16.
 */
public class PreferencesManager {
    private static final String DEFAULT_SHARED_PREFERENCES_NAME         = "_preferences";
    private HashMap<String, Preferences> mPreferences;

    public PreferencesManager(Context context) {
        this.mPreferences   = new HashMap<String, Preferences>();
        this.add(context, DEFAULT_SHARED_PREFERENCES_NAME);
    }

    public Preferences add(Context context, String name) {
        Preferences preferences = new Preferences(context, name);
        this.mPreferences.put(name, preferences);
        preferences.refresh(context);
        return preferences;
    }

    public Preferences get(Context context, String name) {
        if (TextUtils.isEmpty(name)) {
            name = DEFAULT_SHARED_PREFERENCES_NAME;
        }
        Preferences pref = this.mPreferences.get(name);
        if (pref != null) {
            pref.refresh(context);
        } else {
            pref = this.add(context, name);
        }
        return pref;
    }

    public Preferences getDefault(Context context){
        return this.get(context, null);
    }

    public class Preferences {
        private SharedPreferences mPreferences;
        private SharedPreferences.Editor mEditor;
        public String name;
        private boolean mIsDefault;

        public Preferences(Context context, String name){
            this.name       = name;
            if (this.name.equals(DEFAULT_SHARED_PREFERENCES_NAME)) {
                this.mIsDefault = true;
            } else {
                this.name   = context.getPackageName() + "." + name;
            }
        }

        public void refresh(Context context) {
            if (this.mIsDefault) {
                this.mPreferences = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
            } else {
                this.mPreferences = context.getSharedPreferences(this.getFullName(context), Context.MODE_PRIVATE);
            }
        }

        public String getName() {
            return this.name;
        }

        public String getFullName(Context context) {
            if (this.name.equals(DEFAULT_SHARED_PREFERENCES_NAME)) {
                return context.getPackageName() + this.name;
            } else {
                return DEFAULT_SHARED_PREFERENCES_NAME;
            }
        }

        public void loadDefaultValue(Context context, int resource) {
            if (this.mIsDefault) {
                PreferenceManager.setDefaultValues(context, resource, true);
            } else {
                PreferenceManager.setDefaultValues(context, this.getFullName(context), Context.MODE_PRIVATE, resource, true);
            }
            this.refresh(context);
        }

        public void set(String prefkey, Object value){
            if (value == null) {
                value = "";
            }
            String type = value.getClass().getName();
            this.mEditor = this.mPreferences.edit();
            if (type.equals(String.class.getCanonicalName())) {
                String v = (String) value;
                this.mEditor.putString(prefkey, v);
            } else if (type.equals(Boolean.class.getCanonicalName())) {
                Boolean v = (Boolean) value;
                this.mEditor.putBoolean(prefkey, v);
            } else if (type.equals(Float.class.getCanonicalName())) {
                Float v = (Float) value;
                this.mEditor.putFloat(prefkey, v);
            } else if (type.equals(Integer.class.getCanonicalName())) {
                int v = (Integer) value;
                this.mEditor.putInt(prefkey, v);
            } else if (type.equals(Long.class.getCanonicalName())) {
                long v = (Long) value;
                this.mEditor.putLong(prefkey, v);
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
