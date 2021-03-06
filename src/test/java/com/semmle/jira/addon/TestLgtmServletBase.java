package com.semmle.jira.addon;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.atlassian.jira.junit.rules.MockitoContainer;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Rule;

public class TestLgtmServletBase {

  @Rule public MockitoContainer mockitoContainer = new MockitoContainer(this);

  LgtmServlet servlet;
  List<String> log = new ArrayList<>();

  @Before
  public void setupServlet() {
    servlet =
        new LgtmServlet() {
          private static final long serialVersionUID = 1L;

          @Override
          public void log(String msg) {
            log.add(msg);
          }
        };
  }

  private static class MockServletOutputStream extends ServletOutputStream {
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    @Override
    public void write(int b) {
      out.write(b);
    }

    @Override
    public String toString() {
      try {
        return out.toString("UTF-8");
      } catch (UnsupportedEncodingException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public HttpServletResponse mockResponse() throws IOException {
    HttpServletResponse resp = mock(HttpServletResponse.class);
    MockServletOutputStream out = new MockServletOutputStream();

    when(resp.getOutputStream()).thenReturn(out);

    return resp;
  }
}
