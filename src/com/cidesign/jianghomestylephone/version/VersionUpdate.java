package com.cidesign.jianghomestylephone.version;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.cidesign.jianghomestylephone.MainActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

public class VersionUpdate
{
	private static final String TAG = VersionUpdate.class.getSimpleName();

	public ProgressDialog pBar;
	private Handler handler = new Handler();

	private int newVerCode = 0;
	private String newVerName = "";

	private Context ctx;

	private Activity activity;
	private AQuery aq;

	public VersionUpdate(Context ctx, Activity activity, AQuery aq)
	{
		this.ctx = ctx;
		this.activity = activity;
		this.aq = aq;
	}

	public void getServerVerCode()
	{
		try
		{
			String url = VersionConfig.UPDATE_SERVER + VersionConfig.UPDATE_VERJSON;

			long expire = 10 * 1000;
			
			aq.ajax(url, String.class, expire, new AjaxCallback<String>()
			{
				@Override
				public void callback(String url, String html, AjaxStatus status)
				{
					if (html != null)
					{
						JSONArray array;
						try
						{
							array = new JSONArray(html);
							if (array.length() > 0)
							{
								JSONObject obj = array.getJSONObject(0);
								try
								{
									newVerCode = Integer.parseInt(obj.getString("verCode"));
									newVerName = obj.getString("verName");
									int vercode = VersionConfig.getVerCode(ctx);

									if (newVerCode > vercode)
									{
										doNewVersionUpdate();
									}
									else
									{
										startMainActivity();
									}
								}
								catch (Exception e)
								{
									newVerCode = -1;
									newVerName = "";
								}
							}
						}
						catch (JSONException e1)
						{
							startMainActivity();
							e1.printStackTrace();
						}
					}
					else
					{
						new Handler().postDelayed(new Runnable(){
							public void run(){
								startMainActivity();
							}
						}, 2000);
						
					}
				}
			});

		}
		catch (Exception e)
		{
			startMainActivity();
			e.printStackTrace();
		}
	}

	private void notNewVersionShow()
	{
		int verCode = VersionConfig.getVerCode(ctx);
		String verName = VersionConfig.getVerName(ctx);
		StringBuffer sb = new StringBuffer();
		sb.append("��ǰ�汾:");
		sb.append(verName);
		sb.append(" Code:");
		sb.append(verCode);
		sb.append(",\n�������°�,�������!");
		Dialog dialog = new AlertDialog.Builder(ctx).setTitle("�������").setMessage(sb.toString())// ��������
				.setPositiveButton("ȷ��",// ����ȷ����ť
						new DialogInterface.OnClickListener()
						{

							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								dialog.dismiss();
							}

						}).create();// ����
		// ��ʾ�Ի���
		dialog.show();
	}

	private void doNewVersionUpdate()
	{
		int verCode = VersionConfig.getVerCode(ctx);
		String verName = VersionConfig.getVerName(ctx);
		StringBuffer sb = new StringBuffer();
		sb.append("��ǰ�汾:");
		sb.append(verName);
		sb.append(" Code:");
		sb.append(verCode);
		sb.append(", �����°汾:");
		sb.append(newVerName);
		sb.append(" Code:");
		sb.append(newVerCode);
		sb.append(", �Ƿ����?");
		
		Dialog dialog = new AlertDialog.Builder(activity).setTitle("�������").setMessage(sb.toString())
		// ��������
				.setPositiveButton("����",// ����ȷ����ť
						new DialogInterface.OnClickListener()
						{

							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								pBar = new ProgressDialog(activity);
								pBar.setTitle("��������");
								pBar.setMessage("���Ժ�...");
								pBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
								pBar.show();
								downFile(VersionConfig.UPDATE_SERVER + VersionConfig.UPDATE_APKNAME);
							}

						}).setNegativeButton("�ݲ�����", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int whichButton)
					{
						startMainActivity();
						// ���"ȡ��"��ť֮���˳�����
						dialog.dismiss();
					}
				}).create();// ����
		// ��ʾ�Ի���
		dialog.show();
	}

	void downFile(final String url)
	{
		new Thread()
		{
			public void run()
			{
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(url);
				HttpResponse response;
				try
				{
					response = client.execute(get);
					HttpEntity entity = response.getEntity();
					long length = entity.getContentLength();
					InputStream is = entity.getContent();
					FileOutputStream fileOutputStream = null;
					if (is != null)
					{

						File file = new File(Environment.getExternalStorageDirectory(), VersionConfig.UPDATE_SAVENAME);
						fileOutputStream = new FileOutputStream(file);

						byte[] buf = new byte[1024];
						int ch = -1;
						int count = 0;
						while ((ch = is.read(buf)) != -1)
						{
							fileOutputStream.write(buf, 0, ch);
							count += ch;
							if (length > 0)
							{
							}
						}

					}
					fileOutputStream.flush();
					if (fileOutputStream != null)
					{
						fileOutputStream.close();
					}
					down();
				}
				catch (ClientProtocolException e)
				{
					e.printStackTrace();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}

		}.start();

	}

	private void down()
	{
		handler.post(new Runnable()
		{
			public void run()
			{
				pBar.cancel();
				update();
			}
		});

	}

	private void update()
	{

		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory(), VersionConfig.UPDATE_SAVENAME)),
				"application/vnd.android.package-archive");
		activity.startActivity(intent);
	}

	private void startMainActivity()
	{
		activity.startActivity(new Intent(ctx, MainActivity.class));
		if (activity != null)
		{
			activity.finish();
		}
	}
}