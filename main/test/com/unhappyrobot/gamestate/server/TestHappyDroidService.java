package com.unhappyrobot.gamestate.server;

import com.google.common.collect.Lists;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.LinkedList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestHappyDroidService extends HappyDroidService {
  private LinkedList<String> responses;

  public TestHappyDroidService() {
    super();
    responses = Lists.newLinkedList();
  }

  @Override
  public HttpResponse makeGetRequest(String uri) {
    try {
      String responseString = responses.poll();
      byte[] bytes = responseString.getBytes();

      HttpEntity entity = mock(HttpEntity.class);
      when(entity.getContent()).thenReturn(new ByteArrayInputStream(bytes));
      when(entity.getContentLength()).thenReturn((long) bytes.length);
      HttpResponse response = mock(HttpResponse.class);
      when(response.getEntity()).thenReturn(entity);
      return response;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public void queueResponse(String content) {
    responses.add(content);
  }
}
