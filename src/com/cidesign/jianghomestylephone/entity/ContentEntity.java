package com.cidesign.jianghomestylephone.entity;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
/*
* @Title: ContentEntity.java 
* @Package com.cidesign.jianghomestyle.entity 
* @Description: article relative content entity class and insert or update into database 
* @author liling  
* @date 2013��8��14�� ����10:44:35 
* @version V2.0
 */
@DatabaseTable(tableName = "contentlist")
public class ContentEntity
{	
	@DatabaseField(id = true)
	String serverID;
		
	@DatabaseField
	int size; //��С���ֽڣ�
	
	@DatabaseField
	String url; // ����������Url
	
	@DatabaseField
	String timestamp;
	
	@DatabaseField
	String md5; //�ļ�ѹ������md5

	@DatabaseField
	int downloadFlag; //0��δ����   1��������
	
	@DatabaseField
	String insertDate;
	
	@DatabaseField
	private String title; //����
	
	@DatabaseField
	private String profile_path; //չʾͼƬλ��
	
	@DatabaseField
	private String post_date; //����ʱ��
	
	@DatabaseField
	private String author; //����
	
	@DatabaseField
	private String description; //����
	
	@DatabaseField
	private int category; //���� 0:ӡ�� 1����� 2��С�ǣ�3��
	
	@DatabaseField
	private String main_file_path; //������SD��·��

	@DatabaseField
	private String max_bg_img; //��Ŀ������ͼ

	@DatabaseField
	private int isHeadline; //0:�� 1��Ϊͷ��
	
	char operation;

	public String getServerID()
	{
		return serverID;
	}

	public void setServerID(String serverID)
	{
		this.serverID = serverID;
	}

	public int getSize()
	{
		return size;
	}

	public void setSize(int size)
	{
		this.size = size;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getTimestamp()
	{
		return timestamp;
	}

	public void setTimestamp(String timestamp)
	{
		this.timestamp = timestamp;
	}

	public String getMd5()
	{
		return md5;
	}

	public void setMd5(String md5)
	{
		this.md5 = md5;
	}

	public int getDownloadFlag()
	{
		return downloadFlag;
	}

	public void setDownloadFlag(int downloadFlag)
	{
		this.downloadFlag = downloadFlag;
	}

	public String getInsertDate()
	{
		return insertDate;
	}

	public void setInsertDate(String insertDate)
	{
		this.insertDate = insertDate;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getProfile_path()
	{
		return profile_path;
	}

	public void setProfile_path(String profile_path)
	{
		this.profile_path = profile_path;
	}

	public String getPost_date()
	{
		return post_date;
	}

	public void setPost_date(String post_date)
	{
		this.post_date = post_date;
	}

	public String getAuthor()
	{
		return author;
	}

	public void setAuthor(String author)
	{
		this.author = author;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public int getCategory()
	{
		return category;
	}

	public void setCategory(int category)
	{
		this.category = category;
	}

	public String getMain_file_path()
	{
		return main_file_path;
	}

	public void setMain_file_path(String main_file_path)
	{
		this.main_file_path = main_file_path;
	}

	public String getMax_bg_img()
	{
		return max_bg_img;
	}

	public void setMax_bg_img(String max_bg_img)
	{
		this.max_bg_img = max_bg_img;
	}

	public int getIsHeadline()
	{
		return isHeadline;
	}

	public void setIsHeadline(int isHeadline)
	{
		this.isHeadline = isHeadline;
	}

	public char getOperation()
	{
		return operation;
	}

	public void setOperation(char operation)
	{
		this.operation = operation;
	}	
}
