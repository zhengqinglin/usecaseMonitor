package com.ruijie.advice;

import com.ruijie.annotation.RestWrapper;
import com.ruijie.exception.NotFoundException;
import com.ruijie.exception.ServiceException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMappingJacksonResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理类
 */
@RestControllerAdvice
public class GlobalControllerAdvice extends AbstractMappingJacksonResponseBodyAdvice {

    private static final Logger logger = LoggerFactory.getLogger(GlobalControllerAdvice.class);

    /**
     * 默认异常处理，返回500错误
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseInfo<String> defaultExceptionHandler(HttpServletRequest req, Exception e) {
        return getExpResponse(req, e);
    }

    /**
     * 无法找到映射handler的异常处理，返回404错误
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseInfo<String> NoHandlerFoundExceptionHandler(HttpServletRequest req, Exception e) {
        return getExpResponse(req, e);
    }

    /**
     * 无法找到实体，返回204
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseInfo<String> NotFoundExceptionHandler(HttpServletRequest req, Exception e) {
        return getExpResponse(req, e);
    }

    /**
     * 业务层异常
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResponseInfo<Object> ServiceExceptionHandler(HttpServletRequest req, Exception e) {
        ServiceException es = (ServiceException) e;
        logger.error("请求:{}，传回的对象:{}", req.getRequestURL().toString(), es.getRspObj()); // 记录错误信息
        return new ResponseInfo<>(false, es.getRspObj(), req.getRequestURL().toString());
    }

    private ResponseInfo<String> getExpResponse(HttpServletRequest req, Exception e) {
        logger.error("异常", e);
        logger.error("请求:{}，发生异常:{}", req.getRequestURL().toString(), ExceptionUtils.getMessage(e)); // 记录错误信息
        return new ResponseInfo<>(false, e.getMessage(), req.getRequestURL().toString());
    }

    /**
     * 后处理，在方法有RestWrapper注解时，包装return type
     */
    @Override
    protected void beforeBodyWriteInternal(MappingJacksonValue bodyContainer, MediaType contentType,
                                           MethodParameter returnType, ServerHttpRequest request, ServerHttpResponse response) {
        if (returnType.getMethod().isAnnotationPresent(RestWrapper.class)) {
            bodyContainer.setValue(getWrapperResponse(request, bodyContainer.getValue()));
        }
    }

    private ResponseInfo<Object> getWrapperResponse(ServerHttpRequest req, Object data) {
        return new ResponseInfo<>(true, data, req.getURI().toString());
    }

    class ResponseInfo<T> {

        private boolean success;
        private long timestamp;
        private T message;
        private String url;

        public ResponseInfo(boolean success, T message, String url) {
            this.message = message;
            this.success = success;
            this.url = url;
            this.timestamp = System.currentTimeMillis();
        }

        public T getMessage() {
            return message;
        }

        public void setMessage(T message) {
            this.message = message;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

    }
}
