package com.cidesign.jianghomestylephone.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "filelist")
public class FileListEntity
{
	@DatabaseField(generatedId = true)
	int id;

	@DatabaseField
	String serverID;

	@DatabaseField
	String name; // ��������

	@DatabaseField
	int size; // ��С���ֽڣ�

	@DatabaseField
	String url; // ����������Url

	@DatabaseField
	String timestamp;

	@DatabaseField
	String md5; // �ļ�ѹ������md5

	@DatabaseField
	int downloadFlag; // 0��δ���� 1��������

	@DatabaseField
	String insertDate;

	char operation;

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getServerID()
	{
		return serverID;
	}

	public void setServerID(String serverID)
	{
		this.serverID = serverID;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
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

	public char getOperation()
	{
		return operation;
	}

	public void setOperation(char operation)
	{
		this.operation = operation;
	}
}
