package com.mbfw.plugin;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import javax.xml.bind.PropertyException;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

import com.mbfw.entity.Page;
import com.mbfw.util.ReflectHelper;
import com.mbfw.util.Tools;

/**
 * 类名称：PagePlugin.java
 * 
 * @author 研发中心
 * @version 1.0
 */
@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }) })
public class PagePlugin implements Interceptor {

	/**
	 * 数据库方言
	 */
	private static String dialect = "";
	/**
	 * 拦截的对比参数
	 */
	private static String pageSqlId = "";

	/**
	 * 常数
	 */
	private static final int INT_1 = -1;

	/**
	 * 功能:根据mybatis中的sql语句对象的id判断是否需要分页
	 * 
	 * @param mappedStatement
	 *            mybatis中的sql语句对象
	 * @return 是否需要分页
	 */
	private boolean needPage(MappedStatement mappedStatement) {
		return mappedStatement.getId().matches(pageSqlId);
	}

	/**
	 * 功能:获取分页Page对象
	 * 
	 * @param parameterObject
	 *            sql语句对象的参数对象
	 * @param count
	 *            总行数
	 * @return 分页Page对象
	 * @throws SecurityException
	 *             抛错
	 * @throws NoSuchFieldException
	 *             抛错
	 * @throws IllegalArgumentException
	 *             抛错
	 * @throws IllegalAccessException
	 *             抛错
	 */
	private Page getPage(Object parameterObject, int count)
			throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Page page = null;
		if (parameterObject instanceof Page) {
			page = (Page) parameterObject;
			page.setEntityOrField(true);
			page.setTotalResult(count);
		} else {
			Field pageField = ReflectHelper.getFieldByFieldName(parameterObject, "page");
			if (pageField != null) {
				page = (Page) ReflectHelper.getValueByFieldName(parameterObject, "page");
				if (page == null) {
					page = new Page();
				}
				page.setEntityOrField(false);
				page.setTotalResult(count);
				ReflectHelper.setValueByFieldName(parameterObject, "page", page);
			} else {
				throw new NoSuchFieldException(parameterObject.getClass().getName() + "不存在 page 属性,分页查询报错");
			}
		}
		return page;
	}

	@Override
	public Object intercept(Invocation ivk) throws Throwable {
		if (ivk.getTarget() instanceof RoutingStatementHandler) {
			RoutingStatementHandler statementHandler = (RoutingStatementHandler) ivk.getTarget();
			BaseStatementHandler delegate = (BaseStatementHandler) ReflectHelper.getValueByFieldName(statementHandler,
					"delegate");
			MappedStatement mappedStatement = (MappedStatement) ReflectHelper.getValueByFieldName(delegate,
					"mappedStatement");
			if (needPage(mappedStatement)) {
				BoundSql boundSql = delegate.getBoundSql();
				Object parameterObject = boundSql.getParameterObject();
				if (parameterObject == null) {
					throw new NullPointerException("parameterObject尚未实例化！");
				} else {
					Connection connection = (Connection) ivk.getArgs()[0];
					String sql = boundSql.getSql();
					String fhsql = sql;
					String countSql = "select count(0) from (" + fhsql + ")  tmp_count";
					PreparedStatement countStmt = connection.prepareStatement(countSql);
					BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(), countSql,
							boundSql.getParameterMappings(), parameterObject);
					setParameters(countStmt, mappedStatement, countBS, parameterObject);
					ResultSet rs = countStmt.executeQuery();
					int count = 0;
					if (rs.next()) {
						count = rs.getInt(1);
					}
					rs.close();
					countStmt.close();
					Page page = getPage(parameterObject, count);
					String pageSql = generatePageSql(sql, page);
					ReflectHelper.setValueByFieldName(boundSql, "sql", pageSql); // 将分页sql语句反射回BoundSql.
				}
			}
		}
		return ivk.proceed();
	}

	/**
	 * 功能:设置查询总行数的查询请求参数
	 * 
	 * @param ps
	 *            查询对象
	 * @param mappedStatement
	 *            原查询对象
	 * @param boundSql
	 *            BoundSql对象
	 * @param parameterObject
	 *            原请求sql查询的参数对象
	 * @throws SQLException
	 *             抛错
	 */
	private void setParameters(PreparedStatement ps, MappedStatement mappedStatement, BoundSql boundSql,
			Object parameterObject) throws SQLException {
		ErrorContext.instance().activity("setting parameters").object(mappedStatement.getParameterMap().getId());
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		if (parameterMappings != null) {
			Configuration configuration = mappedStatement.getConfiguration();
			TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
			MetaObject metaObject = parameterObject == null ? null : configuration.newMetaObject(parameterObject);
			for (int i = 0; i < parameterMappings.size(); i++) {
				ParameterMapping parameterMapping = parameterMappings.get(i);
				if (parameterMapping.getMode() != ParameterMode.OUT) {
					setParameters(ps, mappedStatement, boundSql, parameterObject, metaObject, parameterMapping,
							configuration, i, typeHandlerRegistry);
				}
			}
		}
	}

	/**
	 * 功能:
	 * 
	 * @param ps
	 *            查询对象
	 * @param mappedStatement
	 *            原查询对象
	 * @param boundSql
	 *            BoundSql对象
	 * @param parameterObject
	 *            原请求sql查询的参数对象
	 * @param metaObject
	 *            metaObject对象
	 * @param parameterMapping
	 *            parameterMapping对象
	 * @param configuration
	 *            configuration对象
	 * @param i
	 *            第几个
	 * @param typeHandlerRegistry
	 *            typeHandlerRegistry对象
	 * @throws SQLException
	 *             抛错
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setParameters(PreparedStatement ps, MappedStatement mappedStatement, BoundSql boundSql,
			Object parameterObject, MetaObject metaObject, ParameterMapping parameterMapping,
			Configuration configuration, int i, TypeHandlerRegistry typeHandlerRegistry) throws SQLException {
		Object value;
		String propertyName = parameterMapping.getProperty();
		PropertyTokenizer prop = new PropertyTokenizer(propertyName);
		if (parameterObject == null) {
			value = null;
		} else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
			value = parameterObject;
		} else if (boundSql.hasAdditionalParameter(propertyName)) {
			value = boundSql.getAdditionalParameter(propertyName);
		} else {
			value = getValue(propertyName, metaObject, prop, configuration, boundSql);
		}
		TypeHandler typeHandler = parameterMapping.getTypeHandler();
		nullException(typeHandler, propertyName, mappedStatement);
		typeHandler.setParameter(ps, i + 1, value, parameterMapping.getJdbcType());
	}

	/**
	 * 功能:获取value值
	 * 
	 * @param propertyName
	 *            属性名
	 * @param metaObject
	 *            metaObject对象
	 * @param prop
	 *            PropertyTokenizer对象
	 * @param configuration
	 *            Configuration对象
	 * @param boundSql
	 *            BoundSql对象
	 * @return value值
	 */
	private Object getValue(String propertyName, MetaObject metaObject, PropertyTokenizer prop,
			Configuration configuration, BoundSql boundSql) {
		Object value;
		if (propertyName.startsWith(ForEachSqlNode.ITEM_PREFIX) && boundSql.hasAdditionalParameter(prop.getName())) {
			value = boundSql.getAdditionalParameter(prop.getName());
			value = getValue(value, propertyName, prop, configuration);
		} else {
			value = getValue(propertyName, metaObject);
		}
		return value;
	}

	/**
	 * 功能:获取value值
	 * 
	 * @param propertyName
	 *            属性名
	 * @param metaObject
	 *            metaObject对象
	 * @return value值
	 */
	private Object getValue(String propertyName, MetaObject metaObject) {
		return metaObject == null ? null : metaObject.getValue(propertyName);
	}

	/**
	 * 功能:获取value值
	 * 
	 * @param value
	 *            Object对象
	 * @param propertyName
	 *            属性名
	 * @param prop
	 *            PropertyTokenizer对象
	 * @param configuration
	 *            Configuration对象
	 * @return value值
	 */
	private Object getValue(Object value, String propertyName, PropertyTokenizer prop, Configuration configuration) {
		return value == null ? null
				: configuration.newMetaObject(value).getValue(propertyName.substring(prop.getName().length()));
	}

	/**
	 * 功能: typeHandler对象为空抛错
	 * 
	 * @param typeHandler
	 *            typeHandler对象
	 * @param propertyName
	 *            属性名
	 * @param mappedStatement
	 *            原查询对象
	 */
	@SuppressWarnings("rawtypes")
	private void nullException(TypeHandler typeHandler, String propertyName, MappedStatement mappedStatement) {
		if (typeHandler == null) {
			throw new ExecutorException("分页查询，查询总行数报错：There was no TypeHandler found for parameter " + propertyName
					+ " of statement " + mappedStatement.getId());
		}
	}

	/**
	 * 根据数据库方言，生成特定的分页sql
	 * 
	 * @param sql
	 *            原sql语句
	 * @param page
	 *            分页参数对象
	 * @return 带分页的sql语句
	 */
	private String generatePageSql(String sql, Page page) {
		StringBuffer pageSql = new StringBuffer();
		if (notNeedToGeneratePageSql(sql, page)) {
			return sql;
		}else if ("mysql".equals(dialect)) {
			pageSql.append(sql);
			pageSql.append(" limit " + page.getCurrentResult() + "," + page.getShowCount());
		} else if ("oracle".equals(dialect)) {
			pageSql.append("select * from (select tmp_tb.*,ROWNUM row_id from (");
			pageSql.append(sql);
			pageSql.append(") tmp_tb where ROWNUM<=");
			pageSql.append(page.getCurrentResult() + page.getShowCount());
			pageSql.append(") where row_id>");
			pageSql.append(page.getCurrentResult());
		} else if ("sqlserver".equals(dialect)) {
			int index = getOrderSqlIndex(sql);
			pageSql.append("SELECT TOP " + page.getShowCount() + " K.* FROM ( ");
			pageSql.append("SELECT ROW_NUMBER() OVER(" + getSqlserverOrderSql(sql, index) + " ) AS ROWID, O.* FROM("
					+ getSqlserverSql(sql, index) + ") AS O ) AS K WHERE ROWID>" + page.getCurrentResult());
			pageSql.append(" ORDER BY ROWID ");
		}
		return pageSql.toString();
	}

	/**
	 * 功能:判断是否创建带分页的sql语句
	 * 
	 * @param sql
	 *            原sql语句
	 * @param page
	 *            分页参数对象
	 * @return true不创建，flase创建
	 */
	private boolean notNeedToGeneratePageSql(String sql, Page page) {
		return page == null || StringUtils.isEmpty(dialect);
	}

	/**
	 * 功能:获取sql中的order语句，没有则默认 order by 1
	 * 
	 * @param sql
	 *            原上去了语句
	 * @param index
	 *            order位置，没有为-1
	 * @return order语句
	 */
	private String getSqlserverOrderSql(String sql, int index) {
		String order = index == INT_1 ? " ORDER BY GETDATE() " : sql.substring(index, sql.length());
		while (order.contains(".")) {
			String first = order.substring(0, order.lastIndexOf('.'));
			int kg = first.lastIndexOf(" ");
			int dh = first.lastIndexOf(",");
			first = first.substring(0, kg > dh ? kg : dh + 1);
			String last = " " + order.substring(order.lastIndexOf('.') + 1, order.length());
			order = first + last;
		}
		return order;
	}

	/**
	 * 功能:获取sql中的不含order的语句部分
	 * 
	 * @param sql
	 *            原sql语句
	 * @param index
	 *            order 位置，没有为-1
	 * @return sql中的不含order的语句部分
	 */
	private String getSqlserverSql(String sql, int index) {
		return index == INT_1 ? sql : sql.substring(0, index);
	}

	/**
	 * 功能:获取 sql中orderby位置
	 * 
	 * @param sql
	 *            sql语句
	 * @return sql中orderby位置
	 */
	private int getOrderSqlIndex(String sql) {
		String order = " order ";
		String mysql = sql.toLowerCase();
		return mysql.indexOf(order);

	}

	@Override
	public Object plugin(Object arg0) {
		return Plugin.wrap(arg0, this);
	}

	@Override
	public void setProperties(Properties p) {
		dialect = p.getProperty("dialect");
		if (StringUtils.isEmpty(dialect)) {
			try {
				throw new PropertyException("dialect property is not found!分页查询失败，未设置数据库方言");
			} catch (PropertyException e) {
			}
		}
		pageSqlId = p.getProperty("pageSqlId");
		if (StringUtils.isEmpty(pageSqlId)) {
			try {
				throw new PropertyException("pageSqlId property is not found!分页查询失败，未设置拦截sqlID");
			} catch (PropertyException e) {
			}
		}
	}

}
