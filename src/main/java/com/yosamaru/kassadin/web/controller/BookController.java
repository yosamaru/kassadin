package com.yosamaru.kassadin.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.yosamaru.kassadin.entity.PO.AccountPO;
import com.yosamaru.kassadin.entity.PO.AuthorPO;
import com.yosamaru.kassadin.entity.PO.BookPO;
import com.yosamaru.kassadin.entity.PO.CategoryPO;
import com.yosamaru.kassadin.entity.PO.PublisherPO;
import com.yosamaru.kassadin.entity.VO.SearchVO;
import com.yosamaru.kassadin.service.AccountService;
import com.yosamaru.kassadin.service.AuthorService;
import com.yosamaru.kassadin.service.BookService;
import com.yosamaru.kassadin.service.CategoryService;
import com.yosamaru.kassadin.service.ContractService;
import com.yosamaru.kassadin.service.ModeratorService;
import com.yosamaru.kassadin.service.PublisherService;
import com.yosamaru.kassadin.service.impl.FileService;
import com.yosamaru.kassadin.util.CommonResponseParams;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/book")
public class BookController {

	Logger log = LoggerFactory.getLogger(BookController.class);

	@Resource
	private BookService bookService;

	@Resource
	private ContractService contractService;

	@Resource
	private AccountService accountService;

	@Resource
	private FileService fileService;

	@Resource
	private ModeratorService moderatorService;

	@Resource
	private AuthorService authorService;

	@Resource
	private CategoryService categoryService;

	@Resource
	private PublisherService publisherService;

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public CommonResponseParams create(@RequestBody BookPO bookPO) {
		if (bookPO.getPublisherId() == null || bookPO.getCategoryId() == null || bookPO.getAuthorId() == null || BigDecimal.ZERO.compareTo(bookPO.getPrice()) >= 0) {
			return CommonResponseParams.ofFailure("Publisher/Category/Author/Price is not null");
		}
		return bookService.addBook(bookPO);
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public CommonResponseParams edit(BookPO bookPO) {
		if (bookPO.getId() == null) {
			return CommonResponseParams.ofFailure("ID not null");
		}
		bookService.save(bookPO);
		return CommonResponseParams.ofSuccessful();
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public CommonResponseParams delete(Long id) {
		if (id == null) {
			return CommonResponseParams.ofFailure("ID not null");
		}
		BookPO bookPO = new BookPO();
		bookPO.setId(id);
		bookService.delete(bookPO);
		return CommonResponseParams.ofSuccessful();
	}

	@RequestMapping(value = "/find-all", method = RequestMethod.GET)
	public CommonResponseParams findAll() {
		List<BookPO> bookPOS = bookService.findAll();
		return CommonResponseParams.ofSuccessful(bookPOS);
	}

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public CommonResponseParams list(@RequestParam(value = "page", defaultValue = "0") Integer page, @RequestParam(value = "size", defaultValue = "15") Integer size) {
//		HashMap<String, Object> params = new HashMap<>();
//		PageRequest pageRequest = new PageRequest(page, size);
//		Page<BookPO> beanPage = bookService.list(params, pageRequest);
//		return CommonResponseParams.ofSuccessful(beanPage);
		return CommonResponseParams.ofSuccessful();
	}

	@RequestMapping(value = "buy", method = RequestMethod.GET)
	public CommonResponseParams buyBook(HttpServletRequest request, @RequestParam(value = "id") Long id) {
		if (id == null) {
			return CommonResponseParams.ofFailure("ID not null");
		}
		try {
			//普通用户买书
			//1. 获取用户Id
			AccountPO concurrentUser = (AccountPO) request.getSession().getAttribute("concurrentUser");
			if (concurrentUser != null && concurrentUser.getId() != null) {
				AccountPO us = accountService.findById(concurrentUser.getId());

				if (us == null) {
					return CommonResponseParams.ofFailure("无效用户");
				}
				return bookService.buyBook(concurrentUser, id);
			}

//			//管理人员买书
//			ModeratorPO moderatorUser = (ModeratorPO) request.getSession().getAttribute("moderatorUser");
//			if(moderatorUser!=null&&moderatorUser.getId()!=null){
//				ModeratorPO us = moderatorService.findById(moderatorUser.getId());
//
//				if(us==null){
//					return CommonResponseParams.ofFailure("无效用户");
//				}
//				return bookService.mBuyBook(moderatorUser, id);
//			}

			return CommonResponseParams.ofFailure("无效用户");
		} catch (Exception e) {
			e.printStackTrace();
			return CommonResponseParams.ofFailure("Buy Error");
		}
	}

	@RequestMapping(value = "new-book", method = RequestMethod.POST)
	public CommonResponseParams newBook(@RequestParam("bookFile") MultipartFile bookFile, String bookInfo) {
		try {

			bookInfo = "[" + bookInfo + "]";
			List<BookPO> bookPOS = JSONObject.parseArray(bookInfo, BookPO.class);
			BookPO bookPO = bookPOS.get(0);

			if (bookPO.getPublisherId() == null || bookPO.getCategoryId() == null || bookPO.getAuthorId() == null || BigDecimal.ZERO.compareTo(bookPO.getPrice()) >= 0) {
				return CommonResponseParams.ofFailure("Publisher/Category/Author/Price is not null");
			}

			String fileName = fileService.storeFile(bookFile);
			bookPO.setFileDownloadUri(fileName);
			return bookService.addBook(bookPO);
		} catch (Exception e) {
			e.printStackTrace();
			return CommonResponseParams.ofFailure("书籍添加失败");
		}
	}

	@RequestMapping(value = "search", method = RequestMethod.POST)
	public CommonResponseParams search(@RequestBody SearchVO searchVO) {
		if (StringUtils.isBlank(searchVO.getSubject()) && StringUtils.isBlank(searchVO.getAuthor()) && StringUtils.isBlank(searchVO.getCategory()) && StringUtils.isBlank(searchVO.getPublisher())) {
			List<BookPO> all = bookService.findAll();
			return CommonResponseParams.ofSuccessful(all);
		}
		List<Long> authorId = new ArrayList<>();
		List<Long> categoryId = new ArrayList<>();
		List<Long> publisherId = new ArrayList<>();
		if (StringUtils.isNotBlank(searchVO.getAuthor())) {
			List<AuthorPO> authorPOS = authorService.findLikeName(searchVO.getAuthor());
			if (authorPOS.size() > 0) {
				for (AuthorPO authorPO : authorPOS) {
					authorId.add(authorPO.getId());
				}
			}
		}

		if (StringUtils.isNotBlank(searchVO.getCategory())) {
			List<CategoryPO> categoryPOS = categoryService.findLikeName(searchVO.getCategory());
			if (categoryPOS.size() > 0) {
				for (CategoryPO categoryPO : categoryPOS) {
					categoryId.add(categoryPO.getId());
				}
			}
		}

		if (StringUtils.isNotBlank(searchVO.getPublisher())) {
			List<PublisherPO> publisherPOS = publisherService.findLikeName(searchVO.getPublisher());
			if (publisherPOS.size() > 0) {
				for (PublisherPO publisherPO : publisherPOS) {
					publisherId.add(publisherPO.getId());
				}
			}
		}

		List<BookPO> bookPOS = bookService.likeSearch(searchVO.getSubject(), authorId, categoryId, publisherId);
		if (bookPOS == null) {
			return CommonResponseParams.ofSuccessful();
		}
		for (BookPO bookPO : bookPOS) {
			if (bookPO.getAuthorId() != null) {
				AuthorPO authorPO = authorService.findById(bookPO.getAuthorId());
				bookPO.setAuthorName(authorPO.getAuthorName());
			}
			if (bookPO.getCategoryId() != null) {
				CategoryPO categoryPO = categoryService.findById(bookPO.getCategoryId());
				bookPO.setCategoryName(categoryPO.getCategoryName());
			}

			if (bookPO.getPublisherId() != null) {
				PublisherPO publisherPO = publisherService.findById(bookPO.getPublisherId());
				bookPO.setPublisherName(publisherPO.getPublisherName());
			}
		}

		return CommonResponseParams.ofSuccessful(bookPOS);
	}

}
