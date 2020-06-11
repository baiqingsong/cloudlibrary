package com.dawn.androidlibrary.util.wifi;

import android.os.Build.VERSION;

import java.lang.reflect.Field;


class Version {
	
	final static int SDK = get();
	
	private static int get() {
		 final Class<VERSION> versionClass = VERSION.class;
		 try {
			 // First try to read the recommended field android.os.Build.VERSION.SDK_INT.
			final Field sdkIntField = versionClass.getField("SDK_INT");
			return sdkIntField.getInt(null);
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
