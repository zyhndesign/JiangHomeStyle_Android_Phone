package com.cidesign.jianghomestylephone;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.androidquery.AQuery;
import com.cidesign.jianghomestylephone.db.DatabaseHelper;
import com.cidesign.jianghomestylephone.entity.ArticleEntity;
import com.cidesign.jianghomestylephone.entity.FileListEntity;
import com.cidesign.jianghomestylephone.tools.FileOperationTools;
import com.cidesign.jianghomestylephone.tools.MD5Tools;
import com.cidesign.jianghomestylephone.tools.StorageUtils;
import com.cidesign.jianghomestylephone.tools.XmlParseTools;
import com.cidesign.jianghomestylephone.version.VersionUpdate;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class SplashActivity extends Activity
{
	private static final String TAG = SplashActivity.class.getSimpleName();

	private SharedPreferences settings = null;
	private SharedPreferences downTimeSettings = null;

	private DatabaseHelper dbHelper;
	private RuntimeExceptionDao<FileListEntity, Integer> fileListDao = null;
	private RuntimeExceptionDao<ArticleEntity, Integer> articleDao = null;
	private AQuery aq;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.splash);
		settings = getSharedPreferences("INIT_PARAMS", Context.MODE_PRIVATE);
		downTimeSettings = getSharedPreferences("LAST_UPDATE_TIME", Context.MODE_PRIVATE);
		dbHelper = new DatabaseHelper(this);
		fileListDao = dbHelper.getFileListDataDao();
		articleDao = dbHelper.getArticleListDataDao();
		aq = new AQuery(this);
	}

	@Override
	public void onStart()
	{
		super.onStart();

		int initData = settings.getInt("initData", 0);
		if (initData == 0) // δ��ʼ��
		{
			if (StorageUtils.isSDCardPresent())
			{
				try
				{
					StorageUtils.mkdir();

					new InitDataTask().execute();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			else
			{
				Toast.makeText(this, "�����SD������Ӧ����ҪSD���������ݣ�", Toast.LENGTH_LONG).show();
			}
		}
		else if (initData == 1) // �Ѿ���ʼ��
		{
			VersionUpdate vUpdate = new VersionUpdate(this.getApplicationContext(), SplashActivity.this, aq);
			vUpdate.getServerVerCode();
		}
	}

	class InitDataTask extends AsyncTask<Void, Void, String>
	{
		List<FileListEntity> listFile = null;
		RuntimeExceptionDao<FileListEntity, Integer> dao = null;

		@Override
		protected void onPreExecute()
		{
			// ��һ��ִ�з���
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Void... arg0)
		{
			String time = "0";
			FileOperationTools.copyAssets(getApplicationContext());
			// ��ȡ������SD�����嵥�б��ļ�list.xml

			try
			{
				listFile = XmlParseTools.parseFileInputStreamList(getAssets().open("filelist.xml"));

				for (FileListEntity flEntity : listFile)
				{
					fileListDao.create(flEntity);
					File target = new File(StorageUtils.FILE_TEMP_ROOT + flEntity.getServerID() + ".zip");
					if (flEntity.getMd5().equals(MD5Tools.getFileMD5String(target)))
					{
						// ���غ��ж��ļ����Ƿ����
						File file = new File(StorageUtils.FILE_ROOT + flEntity.getServerID());
						if (file.isDirectory())
						{
							StorageUtils.delete(file);
						}

						// ������Ϻ��ѹ�ļ�
						FileOperationTools.unZip(StorageUtils.FILE_TEMP_ROOT + flEntity.getServerID() + ".zip",
								StorageUtils.FILE_ROOT);

						// �������ݿ����Ϣ
						flEntity.setDownloadFlag(1);
						fileListDao.update(flEntity);

						ArticleEntity articleEntity = XmlParseTools.readDocXML(StorageUtils.FILE_ROOT + "/"
								+ flEntity.getServerID() + "/doc.xml");
						articleEntity.setServerID(flEntity.getServerID());
						articleEntity.setPost_date(flEntity.getTimestamp());
						if (articleEntity != null)
						{
							articleDao.createOrUpdate(articleEntity);
						}

						time = flEntity.getTimestamp();

						StorageUtils.delete(target);
					}
				}
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// ������ɺ󣬶�ȡ�����ļ��б�
			return time;
		}

		@Override
		protected void onPostExecute(String result)
		{
			Log.d("", "��ʼ�����....");
			super.onPostExecute(result);
			settings.edit().putInt("initData", 1).commit();
			downTimeSettings.edit().putString("lastUpdateTime", result).commit();

			startActivity(new Intent(SplashActivity.this, MainActivity.class));
			finish();
		}
	}
}
