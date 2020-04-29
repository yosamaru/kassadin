package com.yosamaru.kassadin.dao.impl;

import com.yosamaru.kassadin.dao.AuthorDao;
import com.yosamaru.kassadin.entity.PO.AuthorPO;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class AuthorDaoImpl extends AbstractBaseDao<AuthorPO, Long> implements AuthorDao {

	@Override
	public List<AuthorPO> listAuthorByLikeName(String authorName) {
		Session session = getSession();
		CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		CriteriaQuery<AuthorPO> criteriaQuery = criteriaBuilder.createQuery(AuthorPO.class);
		Root<AuthorPO> root = criteriaQuery.from(AuthorPO.class);
		criteriaQuery.select(root).where(criteriaBuilder.like(root.get("authorName"), authorName));
		Query<AuthorPO> query = session.createQuery(criteriaQuery);
		List<AuthorPO> resultList = query.getResultList();
		return resultList;
	}
}
