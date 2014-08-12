package com.example.savingsamuel;

import java.util.Comparator;

public class ProjectileComparator implements Comparator<Projectile> {
	private static ProjectileComparator pInstance = new ProjectileComparator();
	
	public static ProjectileComparator Instance() {
		return pInstance;
	}

	@Override
	public int compare(Projectile arg0, Projectile arg1) {
		if(arg0.vPosition.z < arg1.vPosition.z)
			return -1;
		if(arg0.vPosition.z == arg1.vPosition.z)
			return 0;
		return 1;
	}

}
