package com.cidesign.jianghomestylephone.async;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.androidquery.AQuery;
import com.cidesign.jianghomestylephone.db.DatabaseHelper;
import com.cidesign.jianghomestylephone.entity.ArticleEntity;
import com.cidesign.jianghomestylephone.entity.FileListEntity;
import com.cidesign.jianghomestylephone.http.DownLoadThread;
import com.cidesign.jianghomestylephone.service.DownloadService;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

public class AsyncDownTask extends AsyncTask<List<FileListEntity>, Void, Integer>
{
	private static final String TAG = AsyncDownTask.class.getSimpleName();
	private final int SUCCESS = 1;
	private final int FAILURE = 0;
	private Context ctx;
	private RuntimeExceptionDao<FileListEntity, Integer> fileDao = null;
	private RuntimeExceptionDao<ArticleEntity, Integer> articleDao = null;
	private DatabaseHelper dbHelper;

	public AsyncDownTask(Context ctx, DatabaseHelper dbHelper)
	{
		this.ctx = ctx;
		this.dbHelper = dbHelper;
	}

	@Override
	protected void onPreExecute()
	{

	}

	@Override
	protected Integer doInBackground(List<FileListEntity>... params)
	{
		fileDao = dbHelper.getFileListDataDao();
		articleDao = dbHelper.getArticleListDataDao();

		List<FileListEntity> listFile = params[0];
		ExecutorService service = Executors.newFixedThreadPool(3);

		for (FileListEntity flEntity : listFile)
		{
			service.execute(new DownLoadThread(ctx,fileDao, articleDao, flEntity));
		}
		// �ر������߳�
		service.shutdown();

		try
		{
			while (!service.awaitTermination(1, TimeUnit.SECONDS))
			{
				return SUCCESS;
			}
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		return FAILURE;
	}

	@Override
	protected void onPostExecute(Integer result)
	{
		if (result == SUCCESS)
		{
			Intent intent = new Intent(ctx, DownloadService.class);
			ctx.stopService(intent);
		}
	}
}
