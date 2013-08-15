package com.cidesign.jianghomestylephone.async;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.cidesign.jianghomestylephone.DetailActivity;
import com.cidesign.jianghomestylephone.R;
import com.cidesign.jianghomestylephone.db.DatabaseHelper;
import com.cidesign.jianghomestylephone.entity.ContentEntity;
import com.cidesign.jianghomestylephone.tools.FileOperationTools;
import com.cidesign.jianghomestylephone.tools.MD5Tools;
import com.cidesign.jianghomestylephone.tools.StorageUtils;
import com.j256.ormlite.dao.RuntimeExceptionDao;

public class AsyncDownTask extends AsyncTask<Void, Void, Integer>
{
	private static final String TAG = AsyncDownTask.class.getSimpleName();
	
	private final int SUCCESS = 1;
	private final int FAILURE = 0;
	private ContentEntity cEntity;
	private RuntimeExceptionDao<ContentEntity, Integer> dao = null;
	private DatabaseHelper dbHelper;
	private ProgressDialog mypDialog = null;
	private Activity activity;
	
	public AsyncDownTask(Activity activity,ContentEntity cEntity)
	{
		this.activity = activity;
		this.cEntity = cEntity;
	}

	@Override
	protected void onPreExecute()
	{
		dbHelper = new DatabaseHelper(activity.getApplicationContext());
		dao = dbHelper.getContentDataDao();
		mypDialog = new ProgressDialog(activity);
		mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		// 设置进度条风格，风格为圆形，旋转的
		mypDialog.setTitle("读取内容");
		// 设置ProgressDialog 标题
		mypDialog.setMessage(activity.getResources().getString(R.string.loading));
		// 设置ProgressDialog 提示信息
		mypDialog.setIcon(R.drawable.loading_spinner_bg);

		mypDialog.setIndeterminate(false);
		// 设置ProgressDialog 的进度条是否不明确
		mypDialog.setCancelable(false);
		// 设置ProgressDialog 是否可以按退回按键取消
		mypDialog.show();
	}

	@Override
	protected Integer doInBackground(Void... params)
	{
		String address = cEntity.getUrl();
		if (address != null && !address.equals(""))
		{
			File target = new File(StorageUtils.FILE_TEMP_ROOT + cEntity.getServerID() + ".zip");

			OutputStream output = null;
			try
			{
				URL url = new URL(address);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();

				InputStream input = conn.getInputStream();
				if (!target.exists())
				{
					target.createNewFile();// 新建文件
					output = new FileOutputStream(target);
					// 读取大文件
					byte[] buffer = new byte[4 * 1024];
					int count = 0;
					while((count = input.read(buffer)) != -1)
			        {
						output.write(buffer, 0, count);
			        }
					
					output.flush();

					//校验下载压缩包的MD5值
					
					if (cEntity.getMd5().equals(MD5Tools.getFileMD5String(target)))
					{
						// 下载后判断文件夹是否存在
						File file = new File(StorageUtils.FILE_ROOT + cEntity.getServerID());
						if (file.isDirectory())
						{
							Log.d(TAG, "删除已经存在的文件夹：" + cEntity.getServerID());
							StorageUtils.delete(file);
						}
											
						// 下载完毕后解压文件
						FileOperationTools.unZip(StorageUtils.FILE_TEMP_ROOT + cEntity.getServerID() + ".zip", StorageUtils.FILE_ROOT);
						
						// 更新数据库表信息
						cEntity.setDownloadFlag(1);
						cEntity.setMain_file_path("doc/main.html");
						dao.update(cEntity);
						
					}
					else
					{
						Log.d(TAG, "下次启动后重新下载"+cEntity.getServerID());
					}
					StorageUtils.delete(target);
				}
				return SUCCESS;
			}
			catch (MalformedURLException e)
			{
				e.printStackTrace();
				return FAILURE;
			}
			catch (IOException e)
			{
				e.printStackTrace();
				return FAILURE;
			}
			finally
			{
				try
				{
					if (output != null)
					{
						output.close();
					}
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		return FAILURE;
	}

	@Override
	protected void onPostExecute(Integer result)
	{
		if (result == SUCCESS)
		{
			String url = "file://" + StorageUtils.FILE_ROOT + cEntity.getServerID() + "/doc/main.phone.html";
			Intent intent = new Intent(activity, DetailActivity.class);
			intent.putExtra("url", url);
			intent.putExtra("title", cEntity.getTitle());
			activity.startActivity(intent);
		}
		
		if(mypDialog != null)
		{
			mypDialog.dismiss();
		}
	}
}
