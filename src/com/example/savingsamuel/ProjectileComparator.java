package com.example.savingsamuel;

import java.util.Comparator;

public class ProjectileComparator implements Comparator<Projectile> {
	private static ProjectileComparator _instance = new ProjectileComparator();
	
	public static ProjectileComparator Instance() {
		return _instance;
	}

	@Override
	public int compare(Projectile arg0, Projectile arg1) {
		if(arg0._position.z < arg1._position.z)
			return -1;
		if(arg0._position.z == arg1._position.z)
			return 0;
		return 1;
	}

}
