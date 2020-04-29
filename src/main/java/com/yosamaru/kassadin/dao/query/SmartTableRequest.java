package com.yosamaru.kassadin.dao.query;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yosamaru.kassadin.dao.query.SimpleQuery.Column;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class SmartTableRequest implements Serializable {
	private static final long serialVersionUID = -5060309495315196220L;

	public static class Pagination implements Serializable {
		private static final long serialVersionUID = -4321018173273525684L;

		@JsonProperty("start")
		private int start;

		@JsonProperty("number")
		private int number;

		@JsonProperty("numberOfPages")
		private int numberOfPages;
	}

	public static class Search implements Serializable {
		private static final long serialVersionUID = -487396770381870517L;

		@JsonProperty("predicateObject")
		private Map<String, String> predicateObject;

	}

	public static class SortPiece implements Serializable {
		private static final long serialVersionUID = -481326770381870517L;

		@JsonProperty("predicate")
		private String predicate;

		@JsonProperty("reverse")
		private boolean reverse;
	}

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(SmartTableRequest.class);

	@JsonProperty("pagination")
	private Pagination pagination;

	@JsonProperty("search")
	private Search search;

	@JsonProperty("sortPiece")
	private SortPiece sortPiece;

	@JsonProperty("columns")
	private List<String> columns;

	@JsonProperty("query")
	private String query;

	@JsonProperty("extra")
	private Map<String, Object> extra;

	public Map<String, Object> getExtra() {
		return extra;
	}

	public Pageable buildPageable() {
		return PageRequest.of(this.pagination.start, this.pagination.number);
	}

	public SimpleQuery buildSimpleQuery() {
		final Map<String, Column> map = new TreeMap<>();

		if (columns != null) {
			for (final String name : columns) {
				map.put(name, new SimpleQuery.Column(name, null, StringUtils.isBlank(name) || !name.contains("?")));
			}
		}

		if (search != null) {
			final Map<String, String> predicateObject = search.predicateObject;
			if (predicateObject != null) {
				if (StringUtils.isNotBlank(predicateObject.get("$"))) {
					this.query = predicateObject.get("$");
				}

				for (final Map.Entry<String, String> en : predicateObject.entrySet()) {
					final String name = en.getKey();
					if (!"$".equals(name)) {
						final String value = StringUtils.isNotBlank(en.getValue()) ? en.getValue() : null;
						SimpleQuery.Column sq = map.get(name);
						if (sq != null) {
							sq.setValue(value);
						} else {
							sq = new SimpleQuery.Column(name, null, StringUtils.isBlank(name) || !name.contains("?"), value);
							map.put(name, sq);
						}
					}
				}
			}
		}

		if (sortPiece != null) {
			final String name = sortPiece.predicate;
			if (StringUtils.isNotBlank(name)) {
				final boolean asc = !sortPiece.reverse;
				SimpleQuery.Column sq = map.get(name);
				if (sq != null) {
					sq.setAscending(asc);

				} else {
					sq = new SimpleQuery.Column(name, asc, StringUtils.isBlank(name) || !name.contains("?"));
					map.put(name, sq);
				}
			}
		}

		if (query == null) {
			if (search != null && search.predicateObject != null) {
				query = search.predicateObject.get("$");
				if (StringUtils.isBlank(query)) {
					query = null;
				}
			}
		}
		return new SimpleQuery(query, this.pagination.start, this.pagination.number, extra, map.values());
	}


}
