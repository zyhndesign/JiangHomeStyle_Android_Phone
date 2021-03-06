package com.cidesign.jianghomestylephone.db;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cidesign.jianghomestylephone.R;
import com.cidesign.jianghomestylephone.entity.ArticleEntity;
import com.cidesign.jianghomestylephone.entity.FileListEntity;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper
{

	// name of the database file for your application -- change to something
	// appropriate for your app
	private static final String DATABASE_NAME = "JiangStyle.db";
	// any time you make changes to your database objects, you may have to
	// increase the database version
	private static final int DATABASE_VERSION = 1;

	// the DAO object we use to access the SimpleData table
	private Dao<FileListEntity, Integer> fileListDao = null;
	private Dao<ArticleEntity, Integer> articleListDao = null;

	private RuntimeExceptionDao<FileListEntity, Integer> fileListRuntimeDao = null;
	private RuntimeExceptionDao<ArticleEntity, Integer> articleRuntimeDao = null;

	public DatabaseHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
	}

	/**
	 * This is called when the database is first created. Usually you should
	 * call createTable statements here to create the tables that will store
	 * your data.
	 */
	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource)
	{
		try
		{
			Log.i(DatabaseHelper.class.getName(), "onCreate");
			TableUtils.createTable(connectionSource, FileListEntity.class);
			TableUtils.createTable(connectionSource, ArticleEntity.class);
		}
		catch (SQLException e)
		{
			Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * This is called when your application is upgraded and it has a higher
	 * version number. This allows you to adjust the various data to match the
	 * new version number.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion)
	{
		try
		{
			Log.i(DatabaseHelper.class.getName(), "onUpgrade");
			TableUtils.dropTable(connectionSource, FileListEntity.class, true);
			TableUtils.dropTable(connectionSource, ArticleEntity.class, true);
			// after we drop the old databases, we create the new ones
			onCreate(db, connectionSource);
		}
		catch (SQLException e)
		{
			Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
			throw new RuntimeException(e);
		}
	}

	public Dao<FileListEntity, Integer> getFileListDao() throws SQLException
	{
		if (fileListDao == null)
		{
			fileListDao = getDao(FileListEntity.class);
		}
		return fileListDao;
	}

	public RuntimeExceptionDao<FileListEntity, Integer> getFileListDataDao()
	{
		if (fileListRuntimeDao == null)
		{
			fileListRuntimeDao = getRuntimeExceptionDao(FileListEntity.class);
		}
		return fileListRuntimeDao;
	}

	public Dao<ArticleEntity, Integer> getArticleListDao() throws SQLException
	{
		if (articleListDao == null)
		{
			articleListDao = getDao(ArticleEntity.class);
		}
		return articleListDao;
	}

	public RuntimeExceptionDao<ArticleEntity, Integer> getArticleListDataDao()
	{
		if (articleRuntimeDao == null)
		{
			articleRuntimeDao = getRuntimeExceptionDao(ArticleEntity.class);
		}
		return articleRuntimeDao;
	}

	/**
	 * Close the database connections and clear any cached DAOs.
	 */
	@Override
	public void close()
	{
		super.close();
		fileListRuntimeDao = null;
		articleRuntimeDao = null;
	}

}
