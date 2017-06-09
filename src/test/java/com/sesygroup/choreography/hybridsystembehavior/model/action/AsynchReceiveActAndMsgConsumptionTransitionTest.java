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
package com.sesygroup.choreography.hybridsystembehavior.model.action;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import com.sesygroup.choreography.concreteparticipantbehavior.model.message.InputMessage;
import com.sesygroup.choreography.hybridsystembehavior.model.MessageQueue;
import com.sesygroup.choreography.hybridsystembehavior.model.Participant;
import com.sesygroup.choreography.hybridsystembehavior.model.State;
import com.sesygroup.choreography.hybridsystembehavior.model.action.AsynchReceiveActAndMsgConsumptionTransition;

/**
 *
 * @author Alexander Perucci (http://www.alexanderperucci.com/)
 *
 */
public class AsynchReceiveActAndMsgConsumptionTransitionTest {
   private static AsynchReceiveActAndMsgConsumptionTransition mockedAsynchReceiveActAndMsgConsumptionTransition;
   private static AsynchReceiveActAndMsgConsumptionTransition asynchReceiveActAndMsgConsumptionTransition;

   @BeforeClass
   public static void setUp() {
      // source state
      Participant participantOne = new Participant("p1");
      com.sesygroup.choreography.concreteparticipantbehavior.model.State concreteStateParticipantOne
            = new com.sesygroup.choreography.concreteparticipantbehavior.model.State("s0");
      MessageQueue messageQueueParticipantOne = new MessageQueue(1);
      InputMessage inputMessage = new InputMessage("message");
      messageQueueParticipantOne.getQueue().add(inputMessage);

      Participant participantTwo = new Participant("p2");
      com.sesygroup.choreography.concreteparticipantbehavior.model.State concreteStateParticipantTwo
            = new com.sesygroup.choreography.concreteparticipantbehavior.model.State("s0");
      MessageQueue messageQueueParticipantTwo = new MessageQueue(1);

      Map<Participant, com.sesygroup.choreography.concreteparticipantbehavior.model.State> participantToStateMap
            = new LinkedHashMap<Participant, com.sesygroup.choreography.concreteparticipantbehavior.model.State>();
      participantToStateMap.put(participantOne, concreteStateParticipantOne);
      participantToStateMap.put(participantTwo, concreteStateParticipantTwo);

      Map<Participant, MessageQueue> participantToMessageQueueMap = new LinkedHashMap<Participant, MessageQueue>();
      participantToMessageQueueMap.put(participantOne, messageQueueParticipantOne);
      participantToMessageQueueMap.put(participantTwo, messageQueueParticipantTwo);
      State sourceState = new State(participantToStateMap, participantToMessageQueueMap);

      // target state
      com.sesygroup.choreography.concreteparticipantbehavior.model.State newConcreteStateParticipantOne
            = new com.sesygroup.choreography.concreteparticipantbehavior.model.State("s1");
      MessageQueue newMessageQueueParticipantOne = new MessageQueue(1);

      Map<Participant, com.sesygroup.choreography.concreteparticipantbehavior.model.State> newParticipantToStateMap
            = new LinkedHashMap<Participant, com.sesygroup.choreography.concreteparticipantbehavior.model.State>();
      newParticipantToStateMap.put(participantOne, newConcreteStateParticipantOne);
      newParticipantToStateMap.put(participantTwo, concreteStateParticipantTwo);

      Map<Participant, MessageQueue> newParticipantToMessageQueueMap = new LinkedHashMap<Participant, MessageQueue>();
      newParticipantToMessageQueueMap.put(participantOne, newMessageQueueParticipantOne);
      newParticipantToMessageQueueMap.put(participantTwo, messageQueueParticipantTwo);
      State targetState = new State(newParticipantToStateMap, newParticipantToMessageQueueMap);

      asynchReceiveActAndMsgConsumptionTransition
            = new AsynchReceiveActAndMsgConsumptionTransition(sourceState, targetState, inputMessage);
      mockedAsynchReceiveActAndMsgConsumptionTransition
            = Mockito.mock(AsynchReceiveActAndMsgConsumptionTransition.class);
      Mockito.when(mockedAsynchReceiveActAndMsgConsumptionTransition.getSourceState()).thenReturn(sourceState);
      Mockito.when(mockedAsynchReceiveActAndMsgConsumptionTransition.getTargetState()).thenReturn(targetState);
      Mockito.when(mockedAsynchReceiveActAndMsgConsumptionTransition.toString())
            .thenReturn("((p1:s0:[message],p2:s0:[]), epsilon, (p1:s1:[],p2:s0:[]))");
   }

   @Test
   public void testGetSourceState() {
      Assert.assertEquals(mockedAsynchReceiveActAndMsgConsumptionTransition.getSourceState(),
            asynchReceiveActAndMsgConsumptionTransition.getSourceState());
   }

   @Test
   public void testGetTargetState() {
      Assert.assertEquals(mockedAsynchReceiveActAndMsgConsumptionTransition.getTargetState(),
            asynchReceiveActAndMsgConsumptionTransition.getTargetState());
   }

   @Test
   public void testToString() {
      Assert.assertEquals(mockedAsynchReceiveActAndMsgConsumptionTransition.toString(),
            asynchReceiveActAndMsgConsumptionTransition.toString());
   }
}
