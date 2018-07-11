package com.ruijie.service;

import com.github.pagehelper.PageInfo;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.GenericTypeResolver;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 增删改查基类
 * 
 * @author tangxin
 *
 * @param <T>
 */

public abstract class BaseDaoService<T> {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private Mapper<T> mapper;

	/**
	 * 保存实体，null的属性也会保存，不使用数据库默认值
	 * 
	 * @param entity
	 *            实体
	 * @return 实体
	 */
	@Transactional
	public T save(T entity) {
		int rows = mapper.insert(entity);
		if (rows == 0) {
			logger.warn("0 row affected");
		}
		return entity;
	}

	/**
	 * 保存实体，忽略null属性，使用数据库默认值
	 * 
	 * @param entity
	 *            实体
	 * @return 实体
	 */
	@Transactional
	public T saveSelective(T entity) {
		int rows = mapper.insertSelective(entity);
		if (rows == 0) {
			logger.warn("0 row affected");
		}
		return entity;
	}

	/**
	 * 根据主键更新实体全部字段，null值会被更新
	 * 
	 * @param entity
	 *            实体
	 * @return 实体
	 */
	@Transactional
	public T updateById(T entity) {
		int rows = mapper.updateByPrimaryKey(entity);
		if (rows == 0) {
			logger.warn("0 row affected");
		}
		return entity;
	}

	/**
	 * 根据主键更新属性不为null的值
	 * 
	 * @param entity
	 *            实体
	 * @return 实体
	 */
	@Transactional
	public T updateByIdSelective(T entity) {
		int rows = mapper.updateByPrimaryKeySelective(entity);
		if (rows == 0) {
			logger.warn("0 row affected");
		}
		return entity;
	}

	/**
	 * 删除主键对应的实体
	 * 
	 * @param id
	 *            主键
	 * @return 删除的行数
	 */
	@Transactional
	public int deleteById(Object id) {
		int rows = mapper.deleteByPrimaryKey(id);
		if (rows == 0) {
			logger.warn("0 row affected");
		}
		return rows;
	}

	/**
	 * 根据实体中的属性删除对应的实体，条件使用等号
	 * 
	 * @param entity
	 *            实体
	 * @return 删除的行数
	 */
	@Transactional
	public int delete(T entity) {
		int rows = mapper.delete(entity);
		if (rows == 0) {
			logger.warn("0 row affected");
		}
		return rows;
	}

	/**
	 * 根据主键查询记录
	 * 
	 * @param id
	 *            主键
	 * @return 单个实体
	 */
	public T findOne(Object id) {
		return mapper.selectByPrimaryKey(id);
	}

	/**
	 * 根据实体中的属性进行查询，查询条件使用等号
	 * 
	 * @param entity
	 *            查询条件
	 * @return 实体集合
	 */
	public List<T> find(T entity) {
		return mapper.select(entity);
	}

	/**
	 * 根据map的非空属性进行查询，查询条件使用等号
	 * 
	 * @param paraMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> find(Map<String, Object> paraMap) {
		if (paraMap == null) {
			return null;
		}
		Iterator<Map.Entry<String, Object>> it = paraMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Object> entry = it.next();
			if (Objects.toString(paraMap.get(entry.getKey()),"").equals("")) {
				it.remove();
			}
		}
		try {
			Class<?>[] clazzs = GenericTypeResolver.resolveTypeArguments(getClass(), BaseDaoService.class);
			Class<T> genericType = (Class<T>) clazzs[0];
			T instance = genericType.newInstance();
			BeanUtils.populate(instance, paraMap);
			return find(instance);
		} catch (Exception e) {
			logger.error("find error", e);
		}
		return null;
	}

	/**
	 * 查询所有记录
	 * 
	 * @return 实体集合
	 */
	public List<T> find() {
		return mapper.selectAll();
	}

	/**
	 * 根据传入的查询条件查询
	 * @param example
	 * @return 匹配的实体集合
	 */
	public List<T> findByExample(Example example) {
		return mapper.selectByExample(example);
	}
	/**
	 * 根据实体属性和RowBounds进行分页查询，查询条件使用等号
	 * <p>
	 * eg：RowBounds(1,10)，1是页码，10是每页记录数
	 * 
	 * @return 实体集合
	 */
	public PageInfo<T> findByPage(T entity, RowBounds rowBounds) {
		List<T> list = mapper.selectByRowBounds(entity, rowBounds);
		return new PageInfo<>(list);
	}

	/**
	 * 根据map的非空属性和RowBounds进行分页查询，查询条件使用等号
	 * <p>
	 * eg：RowBounds(1,10)，1是页码，10是每页记录数
	 * 
	 * @return 实体集合
	 */
	@SuppressWarnings("unchecked")
	public PageInfo<T> findByPage(Map<String, Object> paraMap, RowBounds rowBounds) {
		if (paraMap == null) {
			return null;
		}
		Iterator<Map.Entry<String, Object>> it = paraMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Object> entry = it.next();
			if (Objects.toString(paraMap.get(entry.getKey()),"").equals("")) {
				it.remove();
			}
		}
		try {
			Class<?>[] clazzs = GenericTypeResolver.resolveTypeArguments(getClass(), BaseDaoService.class);
			Class<T> genericType = (Class<T>) clazzs[0];
			T instance = genericType.newInstance();
			BeanUtils.populate(instance, paraMap);
			return findByPage(instance, rowBounds);
		} catch (Exception e) {
			logger.error("find error", e);
		}
		return null;
	}

	/**
	 * 根据实体中的属性查询总数，查询条件使用等号
	 * 
	 * @return 记录数
	 */
	public int count(T entity) {
		return mapper.selectCount(entity);
	}




}
