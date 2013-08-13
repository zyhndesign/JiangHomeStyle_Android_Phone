package com.cidesign.jianghomestylephone.service;

import java.io.IOException;
import java.util.List;

import com.cidesign.jianghomestylephone.async.AsyncDownTask;
import com.cidesign.jianghomestylephone.db.DatabaseHelper;
import com.cidesign.jianghomestylephone.entity.FileListEntity;
import com.cidesign.jianghomestylephone.tools.StorageUtils;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class DownloadService extends Service
{
	private static final String TAG = DownloadService.class.getSimpleName();
	private DatabaseHelper dbHelper;
	public static final String BROADCAST_UPDATE_DATA_ACTION = "BROADCAST_UPDATE_DATA_ACTION";
	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	@Override
	public void onCreate()
	{
		super.onCreate();

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		dbHelper = new DatabaseHelper(this);
		RuntimeExceptionDao<FileListEntity, Integer> dao = dbHelper.getFileListDataDao();
		List<FileListEntity> listFile = dao.queryForEq("downloadFlag", 0);
		Log.d(TAG, "��Ҫ���ص��ļ���Ϊ��" + listFile.size());
		if (StorageUtils.isSDCardPresent())
		{
			if (!StorageUtils.checkAvailableStorage())
			{
				Toast.makeText(this, "SD���������㣬�뻻������������õ��ļ���", Toast.LENGTH_LONG).show();
			}
			else
			{
				try
				{
					StorageUtils.deleteDirectory(StorageUtils.FILE_TEMP_ROOT);

					StorageUtils.mkdir();

					dbHelper = new DatabaseHelper(this);

					new AsyncDownTask(this, dbHelper).execute(listFile);
				}
				catch (IOException e)
				{
					e.printStackTrace();
					Toast.makeText(this, "�����ļ��в��ɹ���", Toast.LENGTH_LONG).show();
				}
			}
		}
		else
		{
			Toast.makeText(this, "�����SD��", Toast.LENGTH_LONG).show();
		}
		return START_STICKY;
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}
}
