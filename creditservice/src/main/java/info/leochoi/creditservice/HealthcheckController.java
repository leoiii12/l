package info.leochoi.creditservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthcheckController {

  @GetMapping(path = "/healthz", produces = "text/plain")
  public String calculator() {

    return "OK";
  }
}
