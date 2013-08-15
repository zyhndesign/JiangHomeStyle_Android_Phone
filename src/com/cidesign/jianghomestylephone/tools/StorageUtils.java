package com.cidesign.jianghomestylephone.tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;

public class StorageUtils
{
	private static final String TAG = StorageUtils.class.getSimpleName();

	private static final String SDCARD_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
	public static final String APP_ROOT = SDCARD_ROOT + "JiangHomeStyle/";
	public static final String FILE_ROOT = APP_ROOT + "articles/";
	public static final String FILE_TEMP_ROOT = APP_ROOT + "temp/";
	public static final String THUMB_IMG_ROOT = APP_ROOT + "thumb/";
	
	private static final long LOW_STORAGE_THRESHOLD = 1024 * 1024 * 10;

	public static boolean isSdCardWrittenable()
	{
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
		{
			return true;
		}
		return false;
	}

	public static long getAvailableStorage()
	{

		String storageDirectory = null;
		storageDirectory = Environment.getExternalStorageDirectory().toString();

		try
		{
			StatFs stat = new StatFs(storageDirectory);
			long avaliableSize = ((long) stat.getAvailableBlocks() * (long) stat.getBlockSize());
			return avaliableSize;
		}
		catch (RuntimeException ex)
		{
			return 0;
		}
	}

	public static boolean checkAvailableStorage()
	{
		if (getAvailableStorage() < LOW_STORAGE_THRESHOLD)
		{
			return false;
		}

		return true;
	}

	public static boolean isSDCardPresent()
	{
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

	public static void mkdir() throws IOException
	{
		File file = new File(APP_ROOT);
		if (!file.exists() || !file.isDirectory())
			file.mkdir();

		file = new File(FILE_ROOT);
		if (!file.exists() || !file.isDirectory())
			file.mkdir();

		file = new File(FILE_TEMP_ROOT);
		if (!file.exists() || !file.isDirectory())
			file.mkdir();
		
		file = new File(THUMB_IMG_ROOT);
		if (!file.exists() || !file.isDirectory())
			file.mkdir();
	}

	/**
	 * ����·��ɾ��ָ����Ŀ¼���ļ������۴������
	 * 
	 * @param sPath
	 *            Ҫɾ����Ŀ¼���ļ�
	 * @return ɾ���ɹ����� true�����򷵻� false��
	 */
	public static boolean DeleteFolder(String sPath)
	{
		boolean flag = false;
		File file = new File(sPath);
		// �ж�Ŀ¼���ļ��Ƿ����
		if (!file.exists())
		{ // �����ڷ��� false
			return flag;
		}
		else
		{
			// �ж��Ƿ�Ϊ�ļ�
			if (file.isFile())
			{ // Ϊ�ļ�ʱ����ɾ���ļ�����
				return deleteFile(sPath);
			}
			else
			{ // ΪĿ¼ʱ����ɾ��Ŀ¼����
				return deleteDirectory(sPath);
			}
		}
	}

	/**
	 * ɾ�������ļ�
	 * 
	 * @param sPath
	 *            ��ɾ���ļ����ļ���
	 * @return �����ļ�ɾ���ɹ�����true�����򷵻�false
	 */
	public static boolean deleteFile(String sPath)
	{
		boolean flag = false;
		File file = new File(sPath);
		// ·��Ϊ�ļ��Ҳ�Ϊ�������ɾ��
		if (file.isFile() && file.exists())
		{
			file.delete();
			flag = true;
		}
		return flag;
	}

	public static boolean deleteDirectory(String sPath)
	{
		// ���sPath�����ļ��ָ�����β���Զ�����ļ��ָ���
		if (!sPath.endsWith(File.separator))
		{
			sPath = sPath + File.separator;
		}
		File dirFile = new File(sPath);
		// ���dir��Ӧ���ļ������ڣ����߲���һ��Ŀ¼�����˳�
		if (!dirFile.exists() || !dirFile.isDirectory())
		{
			return false;
		}
		boolean flag = true;
		// ɾ���ļ����µ������ļ�(������Ŀ¼)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++)
		{
			// ɾ�����ļ�
			if (files[i].isFile())
			{
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			} // ɾ����Ŀ¼
			else
			{
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag)
			return false;
		// ɾ����ǰĿ¼
		if (dirFile.delete())
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public static Bitmap getLoacalBitmap(String url)
	{

		try
		{
			FileInputStream fis = new FileInputStream(url);
			return BitmapFactory.decodeStream(fis);

		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public static String size(long size)
	{

		if (size / (1024 * 1024) > 0)
		{
			float tmpSize = (float) (size) / (float) (1024 * 1024);
			DecimalFormat df = new DecimalFormat("#.##");
			return "" + df.format(tmpSize) + "MB";
		}
		else if (size / 1024 > 0)
		{
			return "" + (size / (1024)) + "KB";
		}
		else
			return "" + size + "B";
	}

	public static boolean delete(File path)
	{

		boolean result = true;
		if (path.exists())
		{
			if (path.isDirectory())
			{
				for (File child : path.listFiles())
				{
					result &= delete(child);
				}
				result &= path.delete(); // Delete empty directory.
			}
			if (path.isFile())
			{
				result &= path.delete();
			}
			if (!result)
			{
				Log.e(null, "Delete failed;");
			}
			return result;
		}
		else
		{
			Log.e(null, "File does not exist.");
			return false;
		}
	}
}
