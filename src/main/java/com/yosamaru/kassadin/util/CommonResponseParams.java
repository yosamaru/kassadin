package com.yosamaru.kassadin.util;


import com.alibaba.fastjson.JSONArray;

public class CommonResponseParams {
	/**
	 * 返回码 - 必填
	 */
	private String errCode;

	/**
	 * 返回描述 - 必填
	 */
	private String errMessage;

	/**
	 * 业务数据 - 必填
	 */
	private Object content;

	/**
	 * 构造一个{@link CommonResponseParams}对象
	 *
	 * @param errCode    返回码
	 * @param errMessage 返回描述
	 * @param content    业务数据
	 */
	private CommonResponseParams(final String errCode, final String errMessage, final Object content) {
		this.errCode = errCode;
		this.errMessage = errMessage;
		this.content = content;
	}

	/**
	 * 返回成功结果，没有响应数据
	 *
	 * @return 公共响应参数实体
	 */
	public static CommonResponseParams ofSuccessful() {
		return ofSuccessful(null);
	}

	/**
	 * 返回成功结果
	 *
	 * @param content 返回的数据
	 * @param <T>     返回的数据的类型
	 * @return 公共响应参数实体
	 */
	public static <T> CommonResponseParams ofSuccessful(final T content) {
		return new CommonResponseParams(
				ReturnCode.OK.getCode(),
				ReturnCode.OK.getMessage(),
				JSONArray.toJSON(content));
	}

	/**
	 * 返回失败结果
	 *
	 * @return 公共响应参数实体
	 */
	public static CommonResponseParams ofFailure() {
		return new CommonResponseParams(
				ReturnCode.FAIL.getCode(),
				ReturnCode.FAIL.getMessage(),
				null);
	}

	public static CommonResponseParams ofFailure(String errMessage) {
		return new CommonResponseParams(
				ReturnCode.FAIL.getCode(),
				errMessage,
				null);
	}

	/**
	 * 返回失败结果
	 *
	 * @param returnCode 错误的返回码
	 * @return 公共响应参数实体
	 */
	public static CommonResponseParams ofFailure(ReturnCode returnCode) {
		return new CommonResponseParams(
				returnCode.getCode(),
				returnCode.getMessage(),
				null);
	}

	/**
	 * 返回带有自定义错误信息的失败结果
	 *
	 * @param returnCode 错误相关的返回码
	 * @param errMessage 自定义的错误信息
	 * @return 公共响应参数实体
	 */
	public static CommonResponseParams ofFailure(ReturnCode returnCode, String errMessage) {
		return new CommonResponseParams(
				returnCode.getCode(),
				errMessage,
				null);
	}

	public String getErrCode() {
		return errCode;
	}

	public String getErrMessage() {
		return errMessage;
	}

	public Object getContent() {
		return content;
	}
}
