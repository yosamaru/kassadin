package com.yosamaru.kassadin.service.impl;

import com.yosamaru.kassadin.dao.BaseDao;
import com.yosamaru.kassadin.dao.BookDao;
import com.yosamaru.kassadin.entity.PO.AccountPO;
import com.yosamaru.kassadin.entity.PO.AuthorPO;
import com.yosamaru.kassadin.entity.PO.BookPO;
import com.yosamaru.kassadin.entity.PO.CategoryPO;
import com.yosamaru.kassadin.entity.PO.PublisherPO;
import com.yosamaru.kassadin.service.AuthorService;
import com.yosamaru.kassadin.service.BookService;
import com.yosamaru.kassadin.service.CategoryService;
import com.yosamaru.kassadin.service.ContractService;
import com.yosamaru.kassadin.service.PublisherService;
import com.yosamaru.kassadin.util.CommonResponseParams;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Service;

@Service
public class BookServiceAbstract extends AbstractBaseService<BookPO, Long> implements BookService {
	@Resource
	private BookDao bookDao;

	@Resource
	private AuthorService authorService;

	@Resource
	private CategoryService categoryService;

	@Resource
	private PublisherService publisherService;

	@Resource
	private ContractService contractService;

	@Resource
	private EntityManager entityManager;

	@Override
	public BaseDao<BookPO, Long> getDAO() {
		return bookDao;
	}

	String sqlPart = " id, subject, description, isbn, content, content_type as contentType, author_id as authorId, publisher_id as publisherId, category_id as categoryId, price, file_download_uri as fileDownloadUri, create_time as createTime, update_time as updateTime ";

	@Override
	public BookPO findInfo(final Long id) {
		BookPO bookPO = bookDao.getById(id);
		if (bookPO.getAuthorId() != null) {
			bookPO.setAuthorPO(authorService.findById(bookPO.getAuthorId()));
		}
		if (bookPO.getCategoryId() != null) {
			bookPO.setCategoryPO(categoryService.findById(bookPO.getCategoryId()));
		}
		if (bookPO.getPublisherId() != null) {
			bookPO.setPublisherPO(publisherService.findById(bookPO.getPublisherId()));
		}
		return bookPO;
	}

	@Override
	public CommonResponseParams buyBook(final AccountPO accountPO, final Long id) throws Exception {
		BookPO bookPO = this.findInfo(id);

		if (bookPO == null) return CommonResponseParams.ofFailure("无此书籍");

		return contractService.buy(accountPO, bookPO);
	}

	@Override
	public CommonResponseParams addBook(final BookPO bookPO) {
		if (bookPO.getAuthorId() == null || bookPO.getCategoryId() == null || bookPO.getPublisherId() == null) {
			return CommonResponseParams.ofFailure("参数不全");
		}

		AuthorPO authorPO = authorService.findById(bookPO.getAuthorId());
		if (authorPO == null) {
			return CommonResponseParams.ofFailure("作者信息不正确");
		}

		CategoryPO categoryPO = categoryService.findById(bookPO.getCategoryId());

		if (categoryPO == null) {
			return CommonResponseParams.ofFailure("类别信息不正确");
		}

		PublisherPO publisherPO = publisherService.findById(bookPO.getCategoryId());
		if (publisherPO == null) {
			return CommonResponseParams.ofFailure("出版信息不正确");
		}

		this.save(bookPO);

		return CommonResponseParams.ofSuccessful();
	}


	@Override
	public List<BookPO> likeSearch(final String subject, final List<Long> authorId, final List<Long> categoryId, final List<Long> publisherId) {

		if (StringUtils.isBlank(subject) && (authorId == null || authorId.size() < 1) && (categoryId == null || categoryId.size() < 1) && (publisherId == null || publisherId.size() < 1)) {
			return null;
		}

		StringBuffer sql = new StringBuffer("select");
		sql.append(sqlPart);
		sql.append("from book_info t where 1=1 ");
		if (subject != null) {
			sql.append("and subject like ");
			sql.append("'%" + subject + "%' ");
		}

		if (authorId != null && authorId.size() > 0) {
			sql.append("and author_id in(");
			for (int i = 0; i < authorId.size(); i++) {
				sql.append(" " + authorId.get(i));
				if (authorId.size() - 1 != i) {
					sql.append(",");
				}
			}
			sql.append(") ");
		}

		if (categoryId != null && categoryId.size() > 0) {
			sql.append("and category_id in(");
			for (int i = 0; i < categoryId.size(); i++) {
				sql.append(" " + categoryId.get(i));
				if (categoryId.size() - 1 != i) {
					sql.append(",");
				}
			}
			sql.append(") ");
		}

		if (publisherId != null && publisherId.size() > 0) {
			sql.append("and publisher_id in(");
			for (int i = 0; i < publisherId.size(); i++) {
				sql.append(" " + publisherId.get(i));
				if (publisherId.size() - 1 != i) {
					sql.append(",");
				}
			}
			sql.append(") ");
		}


		Query nativeQuery = entityManager.createNativeQuery(sql.toString());

		nativeQuery.unwrap(SQLQuery.class)
				.addScalar("id", StandardBasicTypes.LONG)
				.addScalar("subject", StandardBasicTypes.STRING)
				.addScalar("description", StandardBasicTypes.STRING)
				.addScalar("isbn", StandardBasicTypes.STRING)
				.addScalar("content", StandardBasicTypes.STRING)
				.addScalar("contentType", StandardBasicTypes.STRING)
				.addScalar("authorId", StandardBasicTypes.LONG)
				.addScalar("publisherId", StandardBasicTypes.LONG)
				.addScalar("categoryId", StandardBasicTypes.LONG)
				.addScalar("price", StandardBasicTypes.BIG_DECIMAL)
				.addScalar("fileDownloadUri", StandardBasicTypes.STRING)
				.addScalar("createTime", StandardBasicTypes.TIMESTAMP)
				.addScalar("updateTime", StandardBasicTypes.TIMESTAMP)
				.setResultTransformer(Transformers.aliasToBean(BookPO.class));

		List<BookPO> resultList = nativeQuery.getResultList();
		return resultList;
	}

	@Override
	public List<BookPO> findAll() {
		List<BookPO> bookPOS = super.findAll();
		for (Iterator<BookPO> beanIterator = bookPOS.iterator(); beanIterator.hasNext(); ) {
			BookPO bookPO = beanIterator.next();
			AuthorPO authorPO = authorService.findById(bookPO.getAuthorId());
			CategoryPO categoryPO = categoryService.findById(bookPO.getCategoryId());
			PublisherPO publisherPO = publisherService.findById(bookPO.getCategoryId());

			bookPO.setAuthorName(authorPO.getAuthorName());
			bookPO.setPublisherName(publisherPO.getPublisherName());
			bookPO.setCategoryName(categoryPO.getCategoryName());
		}
		return bookPOS;
	}
}
