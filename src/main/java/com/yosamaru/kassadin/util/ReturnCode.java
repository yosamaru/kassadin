package com.yosamaru.kassadin.util;

/**
 * 接口返回码和描述
 *
 * @author Boris Zhao
 */
public enum ReturnCode {
	/**
	 * 成功
	 */
	OK("0000", "成功"),

	/**
	 * 服务端异常，当发生未知异常时使用该错误码
	 */
	FAIL("9999", "失败"),

	/**
	 * 请求参数中包含无效参数或请求体为空
	 */
	INVALID_REQUEST_PARAM("0001", "请求参数中包含无效参数或请求体为空"),

	/**
	 * 新数据的主键与已有数据重复
	 */
	DUPLICATED_RECORD("0002", "新数据的主键与已有数据重复"),

	/**
	 * 未找到对应记录
	 */
	NON_EXISTENT_RECORD("0003", "未找到对应记录，请检查主键或操作流水号"),

	/**
	 * 签名校验失败
	 */
	SIGNATURE_VERIFICATION_FAIL("0004", "签名校验失败"),

	// 以下为各模块自定义的错误码
	;

	private String code;
	private String message;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	ReturnCode(final String code, final String message) {
		this.code = code;
		this.message = message;
	}

	/**
	 * 根据状态码获取其错误信息
	 *
	 * @param code 状态码
	 * @return 错误码对应的错误信息。如果没有找到则返回{@code null}
	 */
	public static String getMessageByCode(String code) {
		for (ReturnCode item : values()) {
			if (item.code.equals(code)) {
				return item.message;
			}
		}

		return null;
	}
}