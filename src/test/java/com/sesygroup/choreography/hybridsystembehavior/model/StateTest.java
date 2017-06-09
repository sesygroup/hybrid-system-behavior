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

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import com.sesygroup.choreography.concreteparticipantbehavior.model.message.OutputMessage;
import com.sesygroup.choreography.hybridsystembehavior.model.MessageQueue;
import com.sesygroup.choreography.hybridsystembehavior.model.Participant;
import com.sesygroup.choreography.hybridsystembehavior.model.State;

/**
 *
 * @author Alexander Perucci (http://www.alexanderperucci.com/)
 *
 */
public class StateTest {
   private static State mockedState;
   private static State mockedEmptyState;
   private static State state;

   @BeforeClass
   public static void setUp() {
      Participant participantOne = new Participant("p1");
      Participant participantTwo = new Participant("p2");

      com.sesygroup.choreography.concreteparticipantbehavior.model.State concreteStateParticipantOne
            = new com.sesygroup.choreography.concreteparticipantbehavior.model.State("s0");
      com.sesygroup.choreography.concreteparticipantbehavior.model.State concreteStateParticipantTwo
            = new com.sesygroup.choreography.concreteparticipantbehavior.model.State("s0");

      MessageQueue messageQueueParticipantOne = new MessageQueue(1);
      messageQueueParticipantOne.getQueue().offer(new OutputMessage("m1"));
      MessageQueue messageQueueParticipantTwo = new MessageQueue(1);

      Map<Participant, com.sesygroup.choreography.concreteparticipantbehavior.model.State> participantStates
            = new LinkedHashMap<Participant, com.sesygroup.choreography.concreteparticipantbehavior.model.State>();
      participantStates.put(participantOne, concreteStateParticipantOne);
      participantStates.put(participantTwo, concreteStateParticipantTwo);

      Map<Participant, MessageQueue> participantQueues = new LinkedHashMap<Participant, MessageQueue>();
      participantQueues.put(participantOne, messageQueueParticipantOne);
      participantQueues.put(participantTwo, messageQueueParticipantTwo);
      state = new State(participantStates, participantQueues);

      mockedState = Mockito.mock(State.class);
      Mockito.when(mockedState.toString()).thenReturn("(p1:s0:[m1],p2:s0:[])");

      mockedEmptyState = Mockito.mock(State.class);
      Mockito.when(mockedEmptyState.toString()).thenReturn("()");
   }

   @Test
   public void testNewInstance() {
      Assert.assertEquals(state, State.newInstance(state));
   }

   @Test
   public void testToString() {
      Assert.assertEquals(mockedState.toString(), state.toString());
   }

   @Test
   public void testToStringEmptyState() {
      Map<Participant, com.sesygroup.choreography.concreteparticipantbehavior.model.State> participantStates
            = new LinkedHashMap<Participant, com.sesygroup.choreography.concreteparticipantbehavior.model.State>();
      Map<Participant, MessageQueue> participantQueues = new LinkedHashMap<Participant, MessageQueue>();
      State state = new State(participantStates, participantQueues);
      Assert.assertEquals(mockedEmptyState.toString(), state.toString());
   }

   @Test
   public void testEquals() {
      Map<Participant, com.sesygroup.choreography.concreteparticipantbehavior.model.State> participantToConcreteParticipantBehaviorStateMapOne
            = new LinkedHashMap<Participant, com.sesygroup.choreography.concreteparticipantbehavior.model.State>();
      participantToConcreteParticipantBehaviorStateMapOne.put(new Participant("p1"),
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("s1"));
      participantToConcreteParticipantBehaviorStateMapOne.put(new Participant("p2"),
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("s2"));
      participantToConcreteParticipantBehaviorStateMapOne.put(new Participant("p3"),
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("s2"));
      participantToConcreteParticipantBehaviorStateMapOne.put(new Participant("p4"),
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("s0"));
      participantToConcreteParticipantBehaviorStateMapOne.put(new Participant("p5"),
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("s5"));
      participantToConcreteParticipantBehaviorStateMapOne.put(new Participant("p6"),
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("s5"));

      Map<Participant, MessageQueue> participantToMessageQueueMapOne = new LinkedHashMap<Participant, MessageQueue>();
      participantToMessageQueueMapOne.put(new Participant("p1"), new MessageQueue(1));
      participantToMessageQueueMapOne.put(new Participant("p2"), new MessageQueue(1));
      participantToMessageQueueMapOne.put(new Participant("p3"), new MessageQueue(1));
      participantToMessageQueueMapOne.put(new Participant("p4"), new MessageQueue(0));
      participantToMessageQueueMapOne.put(new Participant("p5"), new MessageQueue(0));
      participantToMessageQueueMapOne.put(new Participant("p6"), new MessageQueue(0));

      State stateOne = new State(participantToConcreteParticipantBehaviorStateMapOne, participantToMessageQueueMapOne);

      Map<Participant, com.sesygroup.choreography.concreteparticipantbehavior.model.State> participantToConcreteParticipantBehaviorStateMapTwo
            = new LinkedHashMap<Participant, com.sesygroup.choreography.concreteparticipantbehavior.model.State>();
      participantToConcreteParticipantBehaviorStateMapTwo.put(new Participant("p1"),
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("s1"));
      participantToConcreteParticipantBehaviorStateMapTwo.put(new Participant("p2"),
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("s2"));
      participantToConcreteParticipantBehaviorStateMapTwo.put(new Participant("p3"),
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("s2"));
      participantToConcreteParticipantBehaviorStateMapTwo.put(new Participant("p4"),
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("s0"));
      participantToConcreteParticipantBehaviorStateMapTwo.put(new Participant("p5"),
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("s5"));
      participantToConcreteParticipantBehaviorStateMapTwo.put(new Participant("p6"),
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("s5"));

      Map<Participant, MessageQueue> participantToMessageQueueMapTwo = new LinkedHashMap<Participant, MessageQueue>();
      participantToMessageQueueMapTwo.put(new Participant("p1"), new MessageQueue(1));
      participantToMessageQueueMapTwo.put(new Participant("p2"), new MessageQueue(1));
      participantToMessageQueueMapTwo.put(new Participant("p3"), new MessageQueue(1));
      participantToMessageQueueMapTwo.put(new Participant("p4"), new MessageQueue(0));
      participantToMessageQueueMapTwo.put(new Participant("p5"), new MessageQueue(0));
      participantToMessageQueueMapTwo.put(new Participant("p6"), new MessageQueue(0));

      State stateTwo = new State(participantToConcreteParticipantBehaviorStateMapTwo, participantToMessageQueueMapTwo);

      MatcherAssert.assertThat(stateOne, Matchers.is(stateTwo));

      MatcherAssert.assertThat(stateOne.equals(stateTwo), Matchers.is(true));

      Set<State> states = new HashSet<State>();
      states.add(stateOne);
      /*
       * important: convert the set to list before using the contains method because, if you change an element in the
       * Set, Set.contains(element) may fail to locate it, since the hashCode of the element will be different than what
       * it was when the element was first added to the HashSet
       */
      MatcherAssert.assertThat(states.stream().collect(Collectors.toList()).contains(stateTwo), Matchers.is(true));

   }
}
