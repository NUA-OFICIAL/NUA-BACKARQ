package com.nua.core.base.responses;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.nua.core.base.constants.Constants;
import com.nua.core.base.dto.NUAPaginacion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class NUAResponseApi<T> extends ResponseEntity<NUAResponseApi.NUABody<T>> {

    private static final Logger log = LoggerFactory.getLogger(NUAResponseApi.class);

    public NUAResponseApi(NUABody<T> body, HttpStatusCode status) {
        super(body, status);
    }

    public NUAResponseApi(NUABody<T> body, HttpHeaders headers, HttpStatus statusCode) {
        super(body, headers, statusCode);
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class NUABody<T> {
        private int code;
        private NUAPaginacion paginacion;
        private String mensaje;
        private T data;

        public NUABody() {
        }

        public int getCode() {
            return code;
        }

        public NUAPaginacion getPaginacion() {
            return paginacion;
        }

        public String getMensaje() {
            return mensaje;
        }

        public T getData() {
            return data;
        }

    }

    public static <T> Builder<T> data(T data) {
        return new Builder<T>().data(data);
    }


    public static <T> NUAResponseApi<T> created() {
        return new Builder<T>().created();
    }

    public static class Builder<T> {

        private final NUABody<T> body = new NUABody<>();
        private HttpHeaders headers = new HttpHeaders();

        public Builder<T> data(T data) {
            body.data = data;
            return this;
        }

        public Builder<T> paginacion(NUAPaginacion paginacion) {
            body.paginacion = paginacion;
            return this;
        }

        public Builder<T> header(String name, String value) {
            this.headers.add(name, value);
            return this;
        }

        public Builder<T> headers(HttpHeaders headers) {
            this.headers = headers;
            return this;
        }

        public Builder<T> cookie(ResponseCookie cookie) {
            log.info("Cookie: {}", cookie);
            this.headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
            return this;
        }

        public Builder<T> mensaje(String mensaje) {
            body.mensaje = mensaje;
            return this;
        }

        public NUAResponseApi<T> build(HttpStatus status) {
            return new NUAResponseApi<>(body, headers, status);
        }

        public NUAResponseApi<T> ok() {
            body.code = Constants.CODE_200;
            body.mensaje = Constants.MENSAJE_PETICION;
            return build(HttpStatus.valueOf(body.code));
        }


        public NUAResponseApi<T> login() {
            body.code = Constants.CODE_200;
            body.mensaje = Constants.MENSAJE_LOGIN;
            return build(HttpStatus.valueOf(body.code));
        }

        public NUAResponseApi<T> created() {
            body.code = Constants.CODE_201;
            body.mensaje = Constants.MENSAJE_PETICION_201;
            return build(HttpStatus.valueOf(body.code));
        }

        public NUAResponseApi<T> refresh() {
            body.code = Constants.CODE_200;
            body.mensaje = Constants.MENSAJE_LOGIN;
            return build(HttpStatus.valueOf(body.code));
        }

        public NUAResponseApi<T> error(int status) {
            body.code = status;
            return build(HttpStatus.valueOf(body.code));
        }
    }

}
