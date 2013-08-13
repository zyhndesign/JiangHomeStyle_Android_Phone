package com.cidesign.jianghomestylephone.http;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

import org.apache.http.HttpStatus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.cidesign.jianghomestylephone.R;
import com.cidesign.jianghomestylephone.db.DatabaseHelper;
import com.cidesign.jianghomestylephone.entity.ArticleEntity;
import com.cidesign.jianghomestylephone.entity.FileListEntity;
import com.cidesign.jianghomestylephone.service.DownloadService;
import com.cidesign.jianghomestylephone.tools.StorageUtils;
import com.cidesign.jianghomestylephone.tools.XmlParseTools;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.DeleteBuilder;

public class ArticalOperation
{
	private static final String TAG = ArticalOperation.class.getSimpleName();

	private AQuery aq;
	public DatabaseHelper dbHelper;
	private SharedPreferences settings = null;
	private String request_address = "";
	public Activity activity;

	public ArticalOperation(Activity activity, AQuery aq, DatabaseHelper dbHelper)
	{
		this.activity = activity;
		this.aq = aq;
		this.dbHelper = dbHelper;
		settings = activity.getSharedPreferences("LAST_UPDATE_TIME", Context.MODE_PRIVATE);
		request_address = activity.getResources().getString(R.string.request_address);
	}

	public void getArticleInfo()
	{
		String url = request_address + "/travel/dataUpdate?lastUpdateDate=" + settings.getString("lastUpdateTime", "0")+"&category=1";
		
		long expire = 15 * 1000;

		aq.ajax(url, String.class, expire, new AjaxCallback<String>()
		{
			@Override
			public void callback(String url, String html, AjaxStatus status)
			{
				if (HttpStatus.SC_OK == status.getCode())
				{
					new InsertDBArticleDate().execute(html);
				}
			}
		});
	}

	class InsertDBArticleDate extends AsyncTask<String, Integer, String>
	{
		List<FileListEntity> listFile = null;
		RuntimeExceptionDao<FileListEntity, Integer> dao = null;

		@Override
		protected void onPreExecute()
		{

		}

		@Override
		protected String doInBackground(String... params)
		{			
			listFile = XmlParseTools.parseFileList(params[0]);
			
			if (listFile.size() > 0)
			{
				String time = "0";

				dao = dbHelper.getFileListDataDao();

				for (FileListEntity fileEntity : listFile)
				{
					if (fileEntity.getOperation() == 'u')
					{
						fileEntity.setDownloadFlag(0);
						dao.createOrUpdate(fileEntity);
					}
					else if (fileEntity.getOperation() == 'd')
					{
						// ɾ���ļ���¼������
						dao.delete(fileEntity);

						// ɾ��SD����Ӧ�ļ���
						File file = new File(StorageUtils.FILE_ROOT + fileEntity.getServerID());
						if (file.isDirectory())
						{
							StorageUtils.delete(file);
						}

						// ɾ���ļ�������¼
						RuntimeExceptionDao<ArticleEntity, Integer> articleDao = dbHelper.getArticleListDataDao();
						DeleteBuilder<ArticleEntity, Integer> delBuilder = articleDao.deleteBuilder();
						try
						{
							delBuilder.where().eq("serverID", fileEntity.getServerID());
							delBuilder.delete();
						}
						catch (SQLException e)
						{
							e.printStackTrace();
						}

					}
					time = fileEntity.getTimestamp();
				}

				// �������һ�θ���ʱ��
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("lastUpdateTime", time);				
				editor.commit();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result)
		{
			if (dao == null)
			{
				dao = dbHelper.getFileListDataDao();
			}
			List<FileListEntity> listFile = dao.queryForEq("downloadFlag", 0);

			if (listFile.size() > 0)
			{
				// ���������������
				Intent intent = new Intent(activity, DownloadService.class);
				intent.putExtra("RESULT", 0);
				activity.startService(intent);
			}
		}
	}
}
