package info.leochoi.creditservice;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

/** This is a replacement to override the default behaviour to throw HttpClientErrorException */
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

  @Override
  public boolean hasError(@NotNull ClientHttpResponse httpResponse) {
    return false;
  }

  @Override
  public void handleError(@NotNull ClientHttpResponse httpResponse) {}
}
