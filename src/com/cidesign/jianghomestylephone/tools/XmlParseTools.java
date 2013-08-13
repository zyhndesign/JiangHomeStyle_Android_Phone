package com.cidesign.jianghomestylephone.tools;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.cidesign.jianghomestylephone.entity.ArticleEntity;
import com.cidesign.jianghomestylephone.entity.FileListEntity;

import android.util.Log;
import android.util.Xml;

public class XmlParseTools
{
	private static final String TAG = XmlParseTools.class.getSimpleName();

	public static ArticleEntity readDocXML(String path)
	{
		ArticleEntity articleEntity = null;

		File file = new File(path);
		if (!file.exists())
		{
			return null;
		}

		InputStream inputStream = null;
		try
		{
			inputStream = new BufferedInputStream(new FileInputStream(file));
			List<ArticleEntity> list = XmlParseTools.parseArticleList(inputStream);
			if (list.size() > 0)
			{
				articleEntity = list.get(0);
			}
			return articleEntity;
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (inputStream != null)
				{
					inputStream.close();
				}
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * ������ȡ��XML�ļ��ַ�����˵�����ڶ���������Ҫ����
	 * 
	 * @param xmlStr
	 * @return
	 */
	public static List<FileListEntity> parseFileList(String xmlStr)
	{
		List<FileListEntity> fileList = null;
		FileListEntity fileListEntity = null;
		String date = TimeTools.getCurrentDateTime();
		try
		{
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(new StringReader(xmlStr));
			int event = parser.getEventType();

			while (event != XmlPullParser.END_DOCUMENT)
			{
				switch (event)
				{
				case XmlPullParser.START_DOCUMENT:
					fileList = new ArrayList<FileListEntity>();
					break;
				case XmlPullParser.START_TAG:
					String name = parser.getName();
					if ("file".equals(name))
					{
						fileListEntity = new FileListEntity();
					}
					if (fileListEntity != null)
					{
						if ("id".equals(name))
						{
							fileListEntity.setServerID(parser.nextText());
						}
						else if ("name".equals(name))
						{
							fileListEntity.setName(parser.nextText());
							fileListEntity.setInsertDate(date);
							fileListEntity.setDownloadFlag(0);
						}
						else if ("size".equals(name))
						{
							String str = parser.nextText();
							if (str != null && !str.trim().equals(""))
							{
								fileListEntity.setSize(Integer.parseInt(str));
							}
						}
						else if ("url".equals(name))
						{
							fileListEntity.setUrl(parser.nextText());
						}
						else if ("timestamp".equals(name))
						{
							fileListEntity.setTimestamp(parser.nextText());
						}
						else if ("md5".equals(name))
						{
							fileListEntity.setMd5(parser.nextText());
						}
						else if ("op".equals(name))
						{
							fileListEntity.setOperation(parser.nextText().charAt(0));
						}
					}
					break;
				case XmlPullParser.END_TAG:
					if ("file".equals(parser.getName()))
					{
						fileList.add(fileListEntity);
						fileListEntity = null;
					}
					break;
				}

				event = parser.next();
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return fileList;
	}

	/**
	 * ������ȡ��XML�ļ��ַ�����˵�����ڶ���������Ҫ����
	 * 
	 * @param xmlStr
	 * @return
	 */
	public static List<FileListEntity> parseFileInputStreamList(InputStream inputStream)
	{
		List<FileListEntity> fileList = null;
		FileListEntity fileListEntity = null;
		String date = TimeTools.getCurrentDateTime();
		try
		{
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(inputStream, "UTF-8");
			int event = parser.getEventType();

			while (event != XmlPullParser.END_DOCUMENT)
			{
				switch (event)
				{
				case XmlPullParser.START_DOCUMENT:
					fileList = new ArrayList<FileListEntity>();
					break;
				case XmlPullParser.START_TAG:
					String name = parser.getName();
					if ("file".equals(name))
					{
						fileListEntity = new FileListEntity();
					}
					if (fileListEntity != null)
					{
						if ("id".equals(name))
						{
							fileListEntity.setServerID(parser.nextText());
						}
						else if ("name".equals(name))
						{
							fileListEntity.setName(parser.nextText());
							fileListEntity.setInsertDate(date);
							fileListEntity.setDownloadFlag(0);
						}
						else if ("size".equals(name))
						{
							String str = parser.nextText();
							if (str != null && !str.trim().equals(""))
							{
								fileListEntity.setSize(Integer.parseInt(str));
							}
						}
						else if ("url".equals(name))
						{
							fileListEntity.setUrl(parser.nextText());
						}
						else if ("timestamp".equals(name))
						{
							fileListEntity.setTimestamp(parser.nextText());
						}
						else if ("md5".equals(name))
						{
							fileListEntity.setMd5(parser.nextText());
						}
						else if ("op".equals(name))
						{
							fileListEntity.setOperation(parser.nextText().charAt(0));
						}
					}
					break;
				case XmlPullParser.END_TAG:
					if ("file".equals(parser.getName()))
					{
						fileList.add(fileListEntity);
						fileListEntity = null;
					}
					break;
				}

				event = parser.next();
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return fileList;
	}

	/**
	 * ���������ļ����е��ļ�����
	 * 
	 * @param inputStream
	 * @return
	 */
	public static List<ArticleEntity> parseArticleList(InputStream inputStream)
	{
		List<ArticleEntity> articleList = null;
		try
		{
			ArticleEntity articleEntity = null;
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(inputStream, "UTF-8");

			int event = parser.getEventType();// ������һ���¼�
			while (event != XmlPullParser.END_DOCUMENT)
			{
				switch (event)
				{
				case XmlPullParser.START_DOCUMENT:// �жϵ�ǰ�¼��Ƿ����ĵ���ʼ�¼�
					articleList = new ArrayList<ArticleEntity>();// ��ʼ��books����
					break;
				case XmlPullParser.START_TAG:// �жϵ�ǰ�¼��Ƿ��Ǳ�ǩԪ�ؿ�ʼ�¼�
					if ("doc".equals(parser.getName()))
					{// �жϿ�ʼ��ǩԪ���Ƿ���book
						articleEntity = new ArticleEntity();
					}
					if (articleEntity != null)
					{
						if ("title".equals(parser.getName()))
						{
							articleEntity.setTitle(parser.nextText());
						}
						else if ("profile".equals(parser.getName()))
						{
							articleEntity.setProfile_path(parser.nextText());
						}
						else if ("post_date".equals(parser.getName()))
						{
							articleEntity.setPost_date(parser.nextText());
						}
						else if ("author".equals(parser.getName()))
						{
							articleEntity.setAuthor(parser.nextText());
						}
						else if ("description".equals(parser.getName()))
						{
							articleEntity.setDescription(parser.nextText());
						}
						else if ("category".equals(parser.getName()))
						{
							// �羰 ���� ���� ����
							String category = parser.nextText();
							if (category.equals("1/3"))
							{
								articleEntity.setCategory(JiangCategory.LANDSCAPE);
							}
							else if (category.equals("1/2"))
							{
								articleEntity.setCategory(JiangCategory.HUMANITY);
							}
							else if (category.equals("1/5"))
							{
								articleEntity.setCategory(JiangCategory.STORY);
							}
							else if (category.equals("1/4"))
							{
								articleEntity.setCategory(JiangCategory.COMMUNITY);
							}
						}
						else if ("main_file".equals(parser.getName()))
						{
							articleEntity.setMain_file_path(parser.nextText());
						}
						else if ("background".equals(parser.getName()))
						{
							articleEntity.setMax_bg_img(parser.nextText());
						}
						else if ("headline".equals(parser.getName()))
						{
							String temp = parser.nextText();
							if (temp.equals("true"))
							{
								articleEntity.setIsHeadline(1);
							}
							else
							{
								articleEntity.setIsHeadline(0);
							}							
						}
					}
					break;
				case XmlPullParser.END_TAG:// �жϵ�ǰ�¼��Ƿ��Ǳ�ǩԪ�ؽ����¼�
					if ("doc".equals(parser.getName()))
					{// �жϽ�����ǩԪ���Ƿ���book
						articleList.add(articleEntity);// ��book��ӵ�books����
						articleEntity = null;
					}
					break;
				}
				event = parser.next();// ������һ��Ԫ�ز�������Ӧ�¼�
			}// end while
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (XmlPullParserException e)
		{
			e.printStackTrace();
		}
		return articleList;
	}

}
