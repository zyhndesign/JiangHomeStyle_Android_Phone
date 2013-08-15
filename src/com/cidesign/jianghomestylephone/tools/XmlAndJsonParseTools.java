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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.cidesign.jianghomestylephone.entity.ContentEntity;

import android.util.Log;
import android.util.Xml;

public class XmlAndJsonParseTools
{
	private static final String TAG = XmlAndJsonParseTools.class.getSimpleName();
	
	/**
	 * get the XML file from server through network and then parse article abstract 
	 * @param xmlStr
	 * @return
	 */
	public static List<ContentEntity> parseFileList(String xmlStr)
	{		
		List<ContentEntity> contentList = null;
		ContentEntity contentEntity = null;
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
					contentList = new ArrayList<ContentEntity>();
					break;
				case XmlPullParser.START_TAG:
					String name = parser.getName();
					if ("file".equals(name))
					{
						contentEntity = new ContentEntity();
					}
					if (contentEntity != null)
					{
						if ("id".equals(name))
						{
							contentEntity.setServerID(parser.nextText());
						}
						else if ("size".equals(name))
						{
							String str = parser.nextText();
							if (str != null && !str.trim().equals(""))
							{
								contentEntity.setSize(Integer.parseInt(str));
							}
						}
						else if ("url".equals(name))
						{
							contentEntity.setUrl(parser.nextText());
						}
						else if ("timestamp".equals(name))
						{
							contentEntity.setTimestamp(parser.nextText());
						}
						else if ("md5".equals(name))
						{
							contentEntity.setMd5(parser.nextText());
						}
						else if ("op".equals(name))
						{
							contentEntity.setOperation(parser.nextText().charAt(0));
						}
						if ("name".equals(parser.getName()))
						{
							contentEntity.setTitle(parser.nextText());
							contentEntity.setInsertDate(date);
							contentEntity.setDownloadFlag(0);
						}
						else if ("profile".equals(parser.getName()))
						{
							contentEntity.setProfile_path(parser.nextText());
						}
						else if ("post_date".equals(parser.getName()))
						{
							contentEntity.setPost_date(parser.nextText());
						}
						else if ("author".equals(parser.getName()))
						{
							contentEntity.setAuthor(parser.nextText());
						}
						else if ("description".equals(parser.getName()))
						{
							contentEntity.setDescription(parser.nextText());
						}
						else if ("category".equals(parser.getName()))
						{
							//�羰  ����  ����  ���� 
							String category = parser.nextText();
							if (category.equals("1/3"))
							{
								contentEntity.setCategory(JiangFinalVariables.LANDSCAPE);
							}
							else if (category.equals("1/2"))
							{
								contentEntity.setCategory(JiangFinalVariables.HUMANITY);
							}
							else if (category.equals("1/5"))
							{
								contentEntity.setCategory(JiangFinalVariables.STORY);
							}
							else if (category.equals("1/4"))
							{
								contentEntity.setCategory(JiangFinalVariables.COMMUNITY);
							}
						}
						else if ("main_file".equals(parser.getName()))
						{
							contentEntity.setMain_file_path(parser.nextText());
						}
						else if ("background".equals(parser.getName()))
						{
							contentEntity.setMax_bg_img(parser.nextText());
						}
						else if ("headline".equals(parser.getName()))
						{
							String temp = parser.nextText();
							if (temp.equals("true"))
							{
								contentEntity.setIsHeadline(1);
							}
							else
							{
								contentEntity.setIsHeadline(0);
							}							
						}
					}
					break;
				case XmlPullParser.END_TAG:
					if ("file".equals(parser.getName()))
					{
						contentList.add(contentEntity);
						contentEntity = null;						
					}	
					break;
				}

				event = parser.next();
			}

		}
		catch (Exception e)
		{
			Log.d(TAG, "parser runtime exception.....");
			e.printStackTrace();			
		}
		return contentList;
	}
	
	public static List<ContentEntity> parseListByJson(String jsonStr)
	{		
		List<ContentEntity> list = null;
		try
		{
			String date = TimeTools.getCurrentDateTime();
			ContentEntity cEntity = null;
			JSONArray jsonArray = new JSONArray(jsonStr);
			int arrayLength = jsonArray.length();
			list = new ArrayList<ContentEntity>();
			for (int i = 0; i < arrayLength; i++)
			{
				cEntity = new ContentEntity();
				JSONObject tempObject = (JSONObject)jsonArray.get(i);
				cEntity.setServerID(tempObject.getString("id"));
				cEntity.setTitle(tempObject.getString("name"));
				cEntity.setInsertDate(date);
				cEntity.setDownloadFlag(0);
				
				String str = tempObject.getString("size");
				if (str != null && !str.trim().equals(""))
				{
					cEntity.setSize(Integer.parseInt(str));
				}
				cEntity.setUrl(tempObject.getString("url"));
				cEntity.setTimestamp(tempObject.getString("timestamp"));
				cEntity.setMd5(tempObject.getString("md5"));
				cEntity.setOperation(tempObject.getString("op").charAt(0));
				JSONObject subObject = (JSONObject)tempObject.get("info");
				cEntity.setProfile_path(subObject.getString("profile"));
				cEntity.setPost_date(subObject.getString("postDate"));
				cEntity.setAuthor(subObject.getString("author"));
				cEntity.setDescription(subObject.getString("description"));
				
				String category = subObject.getString("category");
				if (category.equals("1/3"))
				{
					cEntity.setCategory(JiangFinalVariables.LANDSCAPE);
				}
				else if (category.equals("1/2"))
				{
					cEntity.setCategory(JiangFinalVariables.HUMANITY);
				}
				else if (category.equals("1/5"))
				{
					cEntity.setCategory(JiangFinalVariables.STORY);
				}
				else if (category.equals("1/4"))
				{
					cEntity.setCategory(JiangFinalVariables.COMMUNITY);
				}
				cEntity.setMax_bg_img(subObject.getString("background"));
				String headline = subObject.getString("headline");
				if (headline.equals("true"))
				{
					cEntity.setIsHeadline(1);
				}
				else
				{
					cEntity.setIsHeadline(0);
				}	
				list.add(cEntity);
			}
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

}
