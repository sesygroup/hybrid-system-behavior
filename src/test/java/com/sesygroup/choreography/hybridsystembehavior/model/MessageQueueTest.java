/*
 * Copyright 2017 Software Engineering and Synthesis Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sesygroup.choreography.hybridsystembehavior.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import com.sesygroup.choreography.concreteparticipantbehavior.model.message.InputMessage;
import com.sesygroup.choreography.concreteparticipantbehavior.model.message.OutputMessage;
import com.sesygroup.choreography.hybridsystembehavior.model.MessageQueue;

/**
 *
 * @author Alexander Perucci (http://www.alexanderperucci.com/)
 *
 */
public class MessageQueueTest {
   private static MessageQueue mockedMessageQueue;
   private static MessageQueue messageQueue;

   @BeforeClass
   public static void setUp() {

      messageQueue = new MessageQueue(1);
      messageQueue.getQueue().offer(new InputMessage("m1"));

      mockedMessageQueue = Mockito.mock(MessageQueue.class);
      Mockito.when(mockedMessageQueue.toString()).thenReturn("[m1]");
   }

   @Test
   public void testToString() {
      Assert.assertEquals(mockedMessageQueue.toString(), messageQueue.toString());
   }

   @Test
   public void testMessageQueueEqual() {
      Assert.assertTrue(new MessageQueue(1).equals(new MessageQueue(1)));
   }

   @Test
   public void testMessageQueueEqualList() {
      List<MessageQueue> messageQueuesOne = new ArrayList<MessageQueue>();
      MessageQueue messageQueueOne = new MessageQueue(2);
      messageQueueOne.getQueue().offer(new InputMessage("m1"));
      messageQueueOne.getQueue().offer(new InputMessage("m2"));
      messageQueuesOne.add(messageQueueOne);

      List<MessageQueue> messageQueuesTwo = new ArrayList<MessageQueue>();
      MessageQueue messageQueueTwo = new MessageQueue(2);
      messageQueueTwo.getQueue().offer(new OutputMessage("m1"));
      messageQueueTwo.getQueue().offer(new InputMessage("m2"));
      messageQueuesTwo.add(messageQueueTwo);

      Assert.assertTrue(ListUtils.isEqualList(messageQueuesOne, messageQueuesTwo));
   }

   @Test
   public void testMessageQueueNotEqualListOrderMessage() {
      List<MessageQueue> messageQueuesOne = new ArrayList<MessageQueue>();
      MessageQueue messageQueueOne = new MessageQueue(2);
      messageQueueOne.getQueue().offer(new InputMessage("m1"));
      messageQueueOne.getQueue().offer(new InputMessage("m2"));
      messageQueuesOne.add(messageQueueOne);

      List<MessageQueue> messageQueuesTwo = new ArrayList<MessageQueue>();
      MessageQueue messageQueueTwo = new MessageQueue(2);
      messageQueueTwo.getQueue().offer(new OutputMessage("m2"));
      messageQueueTwo.getQueue().offer(new InputMessage("m1"));
      messageQueuesTwo.add(messageQueueTwo);

      Assert.assertFalse(ListUtils.isEqualList(messageQueuesOne, messageQueuesTwo));
   }

}
