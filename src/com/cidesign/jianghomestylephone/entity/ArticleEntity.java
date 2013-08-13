package com.cidesign.jianghomestylephone.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "articlelist")
public class ArticleEntity
{
	@DatabaseField(id = true)
	String serverID;

	@DatabaseField
	private String title; // ����

	@DatabaseField
	private String profile_path; // չʾͼƬλ��

	@DatabaseField
	private String post_date; // ����ʱ��

	@DatabaseField
	private String author; // ����

	@DatabaseField
	private String description; // ����

	@DatabaseField
	private int category; // ���� 0:ӡ�� 1����� 2��С�ǣ�3��

	@DatabaseField
	private String main_file_path; // ������SD��·��

	@DatabaseField
	private String max_bg_img; // ��Ŀ������ͼ

	@DatabaseField
	private int isHeadline; //0:�� 1��Ϊͷ��
	
	public String getServerID()
	{
		return serverID;
	}

	public void setServerID(String serverID)
	{
		this.serverID = serverID;
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

}
