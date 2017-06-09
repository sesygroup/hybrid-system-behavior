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

import com.sesygroup.choreography.concreteparticipantbehavior.model.message.OutputMessage;
import com.sesygroup.choreography.hybridsystembehavior.model.MessageQueue;
import com.sesygroup.choreography.hybridsystembehavior.model.Participant;
import com.sesygroup.choreography.hybridsystembehavior.model.State;
import com.sesygroup.choreography.hybridsystembehavior.model.action.InternalActionTransition;

/**
 *
 * @author Alexander Perucci (http://www.alexanderperucci.com/)
 *
 */
public class InternalActionTransitionTest {
   private static InternalActionTransition mockedInternalActionTransition;
   private static InternalActionTransition internalActionTransition;

   @BeforeClass
   public static void setUp() {
      // source state
      Participant participantOne = new Participant("p1");
      com.sesygroup.choreography.concreteparticipantbehavior.model.State concreteStateParticipantOne
            = new com.sesygroup.choreography.concreteparticipantbehavior.model.State("s0");
      MessageQueue messageQueueParticipantOne = new MessageQueue(1);
      messageQueueParticipantOne.getQueue().add(new OutputMessage("message"));

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

      Map<Participant, com.sesygroup.choreography.concreteparticipantbehavior.model.State> newParticipantToStateMap
            = new LinkedHashMap<Participant, com.sesygroup.choreography.concreteparticipantbehavior.model.State>();
      newParticipantToStateMap.put(participantOne, newConcreteStateParticipantOne);
      newParticipantToStateMap.put(participantTwo, concreteStateParticipantTwo);

      State targetState = new State(newParticipantToStateMap, participantToMessageQueueMap);

      internalActionTransition = new InternalActionTransition(sourceState, targetState);
      mockedInternalActionTransition = Mockito.mock(InternalActionTransition.class);
      Mockito.when(mockedInternalActionTransition.getSourceState()).thenReturn(sourceState);
      Mockito.when(mockedInternalActionTransition.getTargetState()).thenReturn(targetState);
      Mockito.when(mockedInternalActionTransition.toString())
            .thenReturn("((p1:s0:[message],p2:s0:[]), epsilon, (p1:s1:[message],p2:s0:[]))");
   }

   @Test
   public void testGetSourceState() {
      Assert.assertEquals(mockedInternalActionTransition.getSourceState(), internalActionTransition.getSourceState());
   }

   @Test
   public void testGetTargetState() {
      Assert.assertEquals(mockedInternalActionTransition.getTargetState(), internalActionTransition.getTargetState());
   }

   @Test
   public void testToString() {
      Assert.assertEquals(mockedInternalActionTransition.toString(), internalActionTransition.toString());
   }
}
