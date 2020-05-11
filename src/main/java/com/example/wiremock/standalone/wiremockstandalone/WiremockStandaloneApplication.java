package com.example.wiremock.standalone.wiremockstandalone;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.ClasspathFileSource;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import com.github.tomakehurst.wiremock.standalone.JsonFileMappingsSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PreDestroy;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SpringBootApplication
public class WiremockStandaloneApplication {

  static WireMockServer wireMockServer;

  public static void main(String[] args) {
    SpringApplication.run(WiremockStandaloneApplication.class, args);

    WireMockConfiguration wireMockConfiguration = WireMockConfiguration.wireMockConfig();
    wireMockConfiguration.mappingSource(new JsonFileMappingsSource(new ClasspathFileSource("mapping")));
    wireMockConfiguration.extensions(new ResponseTemplateTransformer(true));
    wireMockConfiguration.port(9999);
    wireMockServer = new WireMockServer(wireMockConfiguration);

    wireMockServer.start();
    configureFor(9999);
    stubFor(get("/test-method").atPriority(10).willReturn(aResponse()
            .withHeader("content-type", "application/json")
            .withBody("{\n" +
                    "  \"val\": \"someValue\",\n" +
                    "  \"someVal\": \"someOtherValue\"\n" +
                    "}")));
  }

  @PreDestroy
  public void cleanUp() {
    System.out.println("Inside pre destroy");
    wireMockServer.stop();
  }



}
