package com.jacky.compatible.launcher.features.eliminateprocess;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.jacky.compatible.launcher.model.TaskInfo;

import java.util.ArrayList;
import java.util.List;

/*
 * by:kangzizhaung
 */
public class TaskInfoProvider {
	private final PackageManager pmManager;
	private final ActivityManager aManager;

	public TaskInfoProvider(Context context) {
		pmManager = context.getPackageManager();
		aManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
	}

	// Iterate through the passed list and pass all the application information into taskinfo
	public List<TaskInfo> GetAllTask(List<RunningAppProcessInfo> list) {
		List<TaskInfo> taskInfos = new ArrayList<>();
		for (RunningAppProcessInfo appProcessInfo : list) {
			TaskInfo info = new TaskInfo();
			int id = appProcessInfo.pid;
			info.setId(id);
			String packageName = appProcessInfo.processName;
			info.setPackageName(packageName);
			try {
				// ApplicationInfo is AndroidMainfest inside the file Application encapsulation of nodes
				ApplicationInfo applicationInfo = pmManager.getPackageInfo(
						packageName, 0).applicationInfo;
				Drawable icon = applicationInfo.loadIcon(pmManager);
				info.setIcon(icon);
				String name = applicationInfo.loadLabel(pmManager).toString();
				info.setName(name);
				info.setIsSystemProcess(!IsSystemApp(applicationInfo));
				android.os.Debug.MemoryInfo[] memoryInfo = aManager
						.getProcessMemoryInfo(new int[] { id });
				int memory = memoryInfo[0].getTotalPrivateDirty();
				info.setMemory(memory);
				taskInfos.add(info);
				info = null;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				info.setName(packageName);
				info.setIsSystemProcess(true);
			}
		}
		return taskInfos;
	}

	public Boolean IsSystemApp(ApplicationInfo info) {
		// Some system applications can be updated. If the user downloads a system application to update the original,
		// it’s not a system application. This is how to judge this situation.
		if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
			return true;
		}
		else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
			return true;
		}
		return false;
	}
}
