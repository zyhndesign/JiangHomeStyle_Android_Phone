package com.cidesign.jianghomestylephone.http;

import java.io.File;
import java.util.List;

import org.apache.http.HttpStatus;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.cidesign.jianghomestylephone.R;
import com.cidesign.jianghomestylephone.db.DatabaseHelper;
import com.cidesign.jianghomestylephone.entity.ContentEntity;
import com.cidesign.jianghomestylephone.tools.StorageUtils;
import com.cidesign.jianghomestylephone.tools.XmlAndJsonParseTools;
import com.j256.ormlite.dao.RuntimeExceptionDao;

public class ArticalOperation
{
	private static final String TAG = ArticalOperation.class.getSimpleName();

	private AQuery aq;
	public DatabaseHelper dbHelper;
	private SharedPreferences settings = null;
	private String request_address = "";
	public Activity activity;
	
	private List<ContentEntity> listFile = null;
	private RuntimeExceptionDao<ContentEntity, Integer> dao = null;
	
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
		String url = request_address + "/travel/dataUpdate.json?lastUpdateDate=" + settings.getString("lastUpdateTime", "0")
				+ "&category=1";
		
		long expire = 15 * 1000;

		aq.ajax(url, String.class, expire, new AjaxCallback<String>()
		{

			@Override
			public void callback(String url, String html, AjaxStatus status)
			{
				if (HttpStatus.SC_OK == status.getCode())
				{
					listFile = XmlAndJsonParseTools.parseListByJson(html);
					
					if (listFile.size() > 0)
					{
						String time = "0";

						dao = dbHelper.getContentDataDao();

						for (ContentEntity cEntity : listFile)
						{
							if (cEntity.getOperation() == 'u')
							{
								Log.d(TAG, "更新文章" + cEntity.getServerID());
								cEntity.setDownloadFlag(0);
								dao.createOrUpdate(cEntity);
							}
							else if (cEntity.getOperation() == 'd')
							{
								Log.d(TAG, "删除文章" + cEntity.getServerID());
								// 删除文件记录表数据
								dao.delete(cEntity);

								// 删除SD卡对应文件夹
								File file = new File(StorageUtils.FILE_ROOT + cEntity.getServerID());
								if (file.isDirectory())
								{
									StorageUtils.delete(file);
								}
								file = new File(StorageUtils.THUMB_IMG_ROOT + cEntity.getServerID());
								if (file.isDirectory())
								{
									StorageUtils.delete(file);
								}
							}
							time = cEntity.getTimestamp();
						}

						// 更新最后一次更新时间
						SharedPreferences.Editor editor = settings.edit();
						editor.putString("lastUpdateTime", time);
						editor.commit();
					}
				}
				else
				{
					Toast.makeText(activity, "请打开网络！", Toast.LENGTH_LONG).show();
				}
			}
		});
	}
}
