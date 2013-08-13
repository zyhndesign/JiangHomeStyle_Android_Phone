package com.cidesign.jianghomestylephone.tools;

import java.sql.SQLException;
import java.util.List;

import com.cidesign.jianghomestylephone.entity.ArticleEntity;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

public class LoadingDataFromDB
{
	private static final String TAG = LoadingDataFromDB.class.getSimpleName();

	/**
	 * ��ȡͷ������
	 * 
	 * @param articleDao
	 * @return
	 */
	public List<ArticleEntity> loadTopFourArticle(RuntimeExceptionDao<ArticleEntity, Integer> articleDao)
	{
		List<ArticleEntity> artileList = null;
		
		try
		{
			QueryBuilder<ArticleEntity, Integer> queryBuilder = articleDao.queryBuilder();
			queryBuilder.where().eq("isHeadline", 1);
			queryBuilder.orderBy("post_date", false);
			queryBuilder.limit(4L);
			PreparedQuery<ArticleEntity> prepqredQuery;
			prepqredQuery = queryBuilder.prepare();
			artileList = articleDao.query(prepqredQuery);
			return artileList;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ��ȡ�羰����
	 * 
	 * @param articleDao
	 * @return
	 */
	public List<ArticleEntity> loadLandscapeArticle(RuntimeExceptionDao<ArticleEntity, Integer> articleDao)
	{
		List<ArticleEntity> artileList = null;
		QueryBuilder<ArticleEntity, Integer> queryBuilder = articleDao.queryBuilder();

		try
		{
			queryBuilder.where().eq("category", JiangCategory.LANDSCAPE);
			queryBuilder.orderBy("post_date", false);
			// queryBuilder.limit(3L);
			// queryBuilder.offset(page_num * landscapePageNum);
			PreparedQuery<ArticleEntity> prepqredQuery;
			prepqredQuery = queryBuilder.prepare();
			artileList = articleDao.query(prepqredQuery);
			return artileList;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ��ȡ�羰����
	 * 
	 * @param articleDao
	 * @return
	 */
	public List<ArticleEntity> loadLandscapeArticleByServierId(RuntimeExceptionDao<ArticleEntity, Integer> articleDao,String serverID)
	{
		List<ArticleEntity> artileList = null;
		QueryBuilder<ArticleEntity, Integer> queryBuilder = articleDao.queryBuilder();

		try
		{
			queryBuilder.where().eq("category", JiangCategory.LANDSCAPE).and().eq("serverID", serverID);
			queryBuilder.orderBy("post_date", false);
			// queryBuilder.limit(3L);
			// queryBuilder.offset(page_num * landscapePageNum);
			PreparedQuery<ArticleEntity> prepqredQuery;
			prepqredQuery = queryBuilder.prepare();
			artileList = articleDao.query(prepqredQuery);
			return artileList;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * �ϼ�
	 * 
	 * @param articleDao
	 * @return
	 */
	public long countLandscapeCount(RuntimeExceptionDao<ArticleEntity, Integer> articleDao)
	{
		long count = 0;
		QueryBuilder<ArticleEntity, Integer> queryBuilder = articleDao.queryBuilder();
		try
		{
			queryBuilder.where().eq("category", JiangCategory.LANDSCAPE);
			PreparedQuery<ArticleEntity> preparedQuery = queryBuilder.prepare();
			count = articleDao.query(preparedQuery).size();
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return count;
	}

	/**
	 * ��ȡ��������
	 * 
	 * @param articleDao
	 * @return
	 */
	public List<ArticleEntity> loadHumanityArticle(RuntimeExceptionDao<ArticleEntity, Integer> articleDao)
	{
		List<ArticleEntity> artileList = null;
		QueryBuilder<ArticleEntity, Integer> queryBuilder = articleDao.queryBuilder();

		try
		{
			queryBuilder.where().eq("category", JiangCategory.HUMANITY);
			queryBuilder.orderBy("post_date", false);
			// queryBuilder.limit(3L);
			PreparedQuery<ArticleEntity> prepqredQuery;
			prepqredQuery = queryBuilder.prepare();
			artileList = articleDao.query(prepqredQuery);
			return artileList;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ��ȡ��������
	 * 
	 * @param articleDao
	 * @return
	 */
	public List<ArticleEntity> loadHumanityArticleByServerId(RuntimeExceptionDao<ArticleEntity, Integer> articleDao,String serverID)
	{
		List<ArticleEntity> artileList = null;
		QueryBuilder<ArticleEntity, Integer> queryBuilder = articleDao.queryBuilder();

		try
		{
			queryBuilder.where().eq("category", JiangCategory.HUMANITY).and().eq("serverID", serverID);
			queryBuilder.orderBy("post_date", false);
			// queryBuilder.limit(3L);
			PreparedQuery<ArticleEntity> prepqredQuery;
			prepqredQuery = queryBuilder.prepare();
			artileList = articleDao.query(prepqredQuery);
			return artileList;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * �ϼ�
	 * 
	 * @param articleDao
	 * @return
	 */
	public long countHumanityCount(RuntimeExceptionDao<ArticleEntity, Integer> articleDao)
	{
		long count = 0;
		QueryBuilder<ArticleEntity, Integer> queryBuilder = articleDao.queryBuilder();
		try
		{
			queryBuilder.where().eq("category", JiangCategory.HUMANITY);
			PreparedQuery<ArticleEntity> preparedQuery = queryBuilder.prepare();
			count = articleDao.query(preparedQuery).size();
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return count;
	}

	/**
	 * ��ȡ��������
	 * 
	 * @param articleDao
	 * @return
	 */
	public List<ArticleEntity> loadStoryArticle(RuntimeExceptionDao<ArticleEntity, Integer> articleDao)
	{
		List<ArticleEntity> artileList = null;
		QueryBuilder<ArticleEntity, Integer> queryBuilder = articleDao.queryBuilder();

		try
		{
			queryBuilder.where().eq("category", JiangCategory.STORY);
			queryBuilder.orderBy("post_date", false);
			// queryBuilder.limit(3L);
			// queryBuilder.offset(page_num * storyPageNum);
			PreparedQuery<ArticleEntity> prepqredQuery;
			prepqredQuery = queryBuilder.prepare();
			artileList = articleDao.query(prepqredQuery);
			return artileList;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * �ϼ�
	 * 
	 * @param articleDao
	 * @return
	 */
	public long countStoryCount(RuntimeExceptionDao<ArticleEntity, Integer> articleDao)
	{
		long count = 0;
		QueryBuilder<ArticleEntity, Integer> queryBuilder = articleDao.queryBuilder();
		try
		{
			queryBuilder.where().eq("category", JiangCategory.STORY);
			PreparedQuery<ArticleEntity> preparedQuery = queryBuilder.prepare();
			count = articleDao.query(preparedQuery).size();
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return count;
	}

	/**
	 * ��ȡ��������
	 * 
	 * @param articleDao
	 * @return
	 */
	public List<ArticleEntity> loadCommunityArticle(RuntimeExceptionDao<ArticleEntity, Integer> articleDao)
	{
		List<ArticleEntity> artileList = null;
		QueryBuilder<ArticleEntity, Integer> queryBuilder = articleDao.queryBuilder();

		try
		{
			queryBuilder.where().eq("category", JiangCategory.COMMUNITY);
			queryBuilder.orderBy("post_date", false);
			// queryBuilder.limit(3L);
			// queryBuilder.offset(page_num * communityPageNum);
			PreparedQuery<ArticleEntity> prepqredQuery;
			prepqredQuery = queryBuilder.prepare();
			artileList = articleDao.query(prepqredQuery);
			return artileList;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ��ȡ��������
	 * 
	 * @param articleDao
	 * @return
	 */
	public List<ArticleEntity> loadCommunityArticleByServerId(RuntimeExceptionDao<ArticleEntity, Integer> articleDao,String serverID)
	{
		List<ArticleEntity> artileList = null;
		QueryBuilder<ArticleEntity, Integer> queryBuilder = articleDao.queryBuilder();

		try
		{
			queryBuilder.where().eq("category", JiangCategory.COMMUNITY).and().eq("serverID", serverID);
			queryBuilder.orderBy("post_date", false);
			// queryBuilder.limit(3L);
			// queryBuilder.offset(page_num * communityPageNum);
			PreparedQuery<ArticleEntity> prepqredQuery;
			prepqredQuery = queryBuilder.prepare();
			artileList = articleDao.query(prepqredQuery);
			return artileList;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * �ϼ�
	 * 
	 * @param articleDao
	 * @return
	 */
	public long countCommunityCount(RuntimeExceptionDao<ArticleEntity, Integer> articleDao)
	{
		long count = 0;
		QueryBuilder<ArticleEntity, Integer> queryBuilder = articleDao.queryBuilder();
		try
		{
			queryBuilder.where().eq("category", JiangCategory.COMMUNITY);
			PreparedQuery<ArticleEntity> preparedQuery = queryBuilder.prepare();
			count = articleDao.query(preparedQuery).size();
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return count;
	}

}
