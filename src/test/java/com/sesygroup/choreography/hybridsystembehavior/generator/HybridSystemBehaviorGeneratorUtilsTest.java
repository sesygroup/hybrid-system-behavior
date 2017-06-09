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
package com.sesygroup.choreography.hybridsystembehavior.generator;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import com.sesygroup.choreography.concreteparticipantbehavior.model.ConcreteParticipantBehavior;
import com.sesygroup.choreography.concreteparticipantbehavior.model.Message;
import com.sesygroup.choreography.concreteparticipantbehavior.model.Transition;
import com.sesygroup.choreography.concreteparticipantbehavior.model.action.AsynchronousReceiveActionTransition;
import com.sesygroup.choreography.concreteparticipantbehavior.model.action.AsynchronousSendActionTransition;
import com.sesygroup.choreography.concreteparticipantbehavior.model.action.InternalActionTransition;
import com.sesygroup.choreography.concreteparticipantbehavior.model.action.SynchronousReceiveActionTransition;
import com.sesygroup.choreography.concreteparticipantbehavior.model.action.SynchronousSendActionTransition;
import com.sesygroup.choreography.concreteparticipantbehavior.model.message.InputMessage;
import com.sesygroup.choreography.concreteparticipantbehavior.model.message.OutputMessage;
import com.sesygroup.choreography.hybridsystembehavior.Validation;
import com.sesygroup.choreography.hybridsystembehavior.generator.HybridSystemBehaviorGeneratorUtils;
import com.sesygroup.choreography.hybridsystembehavior.model.Participant;
import com.sesygroup.choreography.hybridsystembehavior.model.State;

/**
 *
 * @author Alexander Perucci (http://www.alexanderperucci.com/)
 *
 */
public class HybridSystemBehaviorGeneratorUtilsTest {
   @BeforeClass
   public static void setUp() {
   }

   @Test
   public void testCreateInitialState() {
      Participant participantOne = new Participant("p1");
      com.sesygroup.choreography.concreteparticipantbehavior.model.State initialStateParticipantOne
            = new com.sesygroup.choreography.concreteparticipantbehavior.model.State("s0");

      ConcreteParticipantBehavior concreteParticipantBehaviorOne = new ConcreteParticipantBehavior(
            new HashSet<com.sesygroup.choreography.concreteparticipantbehavior.model.State>(
                  Arrays.asList(initialStateParticipantOne)),
            initialStateParticipantOne,
            new HashSet<com.sesygroup.choreography.concreteparticipantbehavior.model.Message>(),
            new HashSet<com.sesygroup.choreography.concreteparticipantbehavior.model.Transition>());

      Participant participantTwo = new Participant("p2");

      com.sesygroup.choreography.concreteparticipantbehavior.model.State initialStateParticipantTwo
            = new com.sesygroup.choreography.concreteparticipantbehavior.model.State("s0");

      ConcreteParticipantBehavior concreteParticipantBehaviorTwo = new ConcreteParticipantBehavior(
            new HashSet<com.sesygroup.choreography.concreteparticipantbehavior.model.State>(
                  Arrays.asList(initialStateParticipantTwo)),
            initialStateParticipantTwo,
            new HashSet<com.sesygroup.choreography.concreteparticipantbehavior.model.Message>(),
            new HashSet<com.sesygroup.choreography.concreteparticipantbehavior.model.Transition>());

      Map<Participant, ConcreteParticipantBehavior> participantToConcreteParticipantBehaviorMap
            = new LinkedHashMap<Participant, ConcreteParticipantBehavior>();
      participantToConcreteParticipantBehaviorMap.put(participantOne, concreteParticipantBehaviorOne);
      participantToConcreteParticipantBehaviorMap.put(participantTwo, concreteParticipantBehaviorTwo);

      Map<Participant, Integer> participantToMessageQueueSizeMap = new LinkedHashMap<Participant, Integer>();
      participantToMessageQueueSizeMap.put(participantOne, 1);
      participantToMessageQueueSizeMap.put(participantTwo, 1);

      State initialState = HybridSystemBehaviorGeneratorUtils
            .createInitialState(participantToConcreteParticipantBehaviorMap, participantToMessageQueueSizeMap);
      State mockedInitialState = Mockito.mock(State.class);
      Mockito.when(mockedInitialState.toString()).thenReturn("(p1:s0:[],p2:s0:[])");
      MatcherAssert.assertThat(initialState.toString(), Matchers.equalTo(mockedInitialState.toString()));

   }

   @Test
   public void testFindIntersectionMessageNameSentAndRecevideByParticipants() {
      Set<Message> coll1 = new LinkedHashSet<Message>();
      coll1.add(new InputMessage("m1"));
      coll1.add(new OutputMessage("m2"));
      coll1.add(new InputMessage("m3"));

      Set<Message> coll2 = new LinkedHashSet<Message>();
      coll2.add(new OutputMessage("m1"));
      coll2.add(new InputMessage("m2"));
      coll2.add(new OutputMessage("m3"));

      MatcherAssert.assertThat(
            HybridSystemBehaviorGeneratorUtils.findIntersectionMessageNameSentAndRecevideByParticipants(coll1, coll2),
            Matchers.containsInAnyOrder("m2"));
   }

   @Test
   public void testFindAsynchronousSendActionTransition() {
      Set<Transition> transitions = new LinkedHashSet<Transition>();
      transitions.add(new AsynchronousSendActionTransition(
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v0"),
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v1"),
            new OutputMessage("m1")));

      transitions.add(new AsynchronousSendActionTransition(
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v0"),
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v2"),
            new OutputMessage("m2")));

      transitions.add(new SynchronousReceiveActionTransition(
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v0"),
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v2"),
            new InputMessage("m1")));

      MatcherAssert.assertThat(
            HybridSystemBehaviorGeneratorUtils.findConcreteAsynchronousSendActionTransition(transitions,
                  new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v0"),
                  new OutputMessage("m1")),
            Matchers.containsInAnyOrder(new AsynchronousSendActionTransition(
                  new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v0"),
                  new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v1"),
                  new OutputMessage("m1"))));
   }

   @Test
   public void testFindAsynchronousReceiveActionTransition() {
      Set<Transition> transitions = new LinkedHashSet<Transition>();
      transitions.add(new AsynchronousSendActionTransition(
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v0"),
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v1"),
            new OutputMessage("m1")));

      transitions.add(new SynchronousReceiveActionTransition(
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v0"),
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v2"),
            new InputMessage("m2")));

      transitions.add(new SynchronousReceiveActionTransition(
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v0"),
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v2"),
            new InputMessage("m1")));

      transitions.add(new AsynchronousReceiveActionTransition(
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v0"),
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v2"),
            new InputMessage("m2")));

      MatcherAssert.assertThat(
            HybridSystemBehaviorGeneratorUtils.findConcreteAsynchronousReceiveActionTransition(transitions,
                  new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v0"),
                  new InputMessage("m2")),
            Matchers.containsInAnyOrder(new AsynchronousReceiveActionTransition(
                  new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v0"),
                  new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v2"),
                  new InputMessage("m2"))));
   }

   @Test
   public void testFindSynchronousSendActionTransition() {
      Set<Transition> transitions = new LinkedHashSet<Transition>();
      transitions.add(new AsynchronousSendActionTransition(
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v0"),
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v1"),
            new OutputMessage("m1")));

      transitions.add(new SynchronousSendActionTransition(
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v0"),
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v2"),
            new OutputMessage("m2")));

      transitions.add(new SynchronousReceiveActionTransition(
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v0"),
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v2"),
            new InputMessage("m1")));

      MatcherAssert.assertThat(
            HybridSystemBehaviorGeneratorUtils.findConcreteSynchronousSendActionTransition(transitions,
                  new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v0"),
                  new OutputMessage("m2")),
            Matchers.containsInAnyOrder(new SynchronousSendActionTransition(
                  new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v0"),
                  new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v2"),
                  new OutputMessage("m2"))));
   }

   @Test
   public void testFindSynchronousReceiveActionTransition() {
      Set<Transition> transitions = new LinkedHashSet<Transition>();
      transitions.add(new AsynchronousSendActionTransition(
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v0"),
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v1"),
            new OutputMessage("m1")));

      transitions.add(new SynchronousReceiveActionTransition(
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v0"),
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v2"),
            new InputMessage("m2")));

      transitions.add(new SynchronousReceiveActionTransition(
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v0"),
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v2"),
            new InputMessage("m1")));

      transitions.add(new AsynchronousReceiveActionTransition(
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v0"),
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v2"),
            new InputMessage("m2")));

      MatcherAssert.assertThat(
            HybridSystemBehaviorGeneratorUtils.findConcreteSynchronousReceiveActionTransition(transitions,
                  new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v0"),
                  new InputMessage("m1")),
            Matchers.containsInAnyOrder(new SynchronousReceiveActionTransition(
                  new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v0"),
                  new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v2"),
                  new InputMessage("m1"))));
   }

   @Test(expected = IllegalArgumentException.class)
   public void testValidateFoundedSynchronousSendReceiveActionTransition() {
      Set<Transition> transitions = new LinkedHashSet<Transition>();
      transitions.add(new AsynchronousSendActionTransition(
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v0"),
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v1"),
            new OutputMessage("m1")));

      transitions.add(new SynchronousSendActionTransition(
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v0"),
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v2"),
            new OutputMessage("m2")));

      transitions.add(new SynchronousSendActionTransition(
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v0"),
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v3"),
            new OutputMessage("m2")));

      transitions.add(new InternalActionTransition(
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v0"),
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v2")));

      transitions.add(new AsynchronousReceiveActionTransition(
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v0"),
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v2"),
            new InputMessage("m4")));

      transitions.add(new InternalActionTransition(
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v0"),
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v3")));

      Set<Transition> otherTransitions = new LinkedHashSet<Transition>();
      otherTransitions.add(new AsynchronousSendActionTransition(
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v0"),
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v1"),
            new OutputMessage("m1")));

      otherTransitions.add(new SynchronousReceiveActionTransition(
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v0"),
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v2"),
            new InputMessage("m2")));

      otherTransitions.add(new InternalActionTransition(
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v0"),
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v2")));

      otherTransitions.add(new AsynchronousReceiveActionTransition(
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v0"),
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v2"),
            new InputMessage("m4")));

      otherTransitions.add(new InternalActionTransition(
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v0"),
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v3")));

      Collection<Transition> synchronousSendActionTransitions
            = HybridSystemBehaviorGeneratorUtils.findConcreteSynchronousSendActionTransition(transitions,
                  new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v0"),
                  new OutputMessage("m2"));
      Collection<Transition> synchronousReceiveActionTransitions
            = HybridSystemBehaviorGeneratorUtils.findConcreteSynchronousReceiveActionTransition(transitions,
                  new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v0"),
                  new InputMessage("m2"));

      Validation.validateFoundedSynchronousSendReceiveActionTransition(synchronousSendActionTransitions,
            synchronousReceiveActionTransitions);
   }

   @Test
   public void testFindInternalActionTransition() {
      Set<Transition> transitions = new LinkedHashSet<Transition>();
      transitions.add(new AsynchronousSendActionTransition(
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v0"),
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v1"),
            new OutputMessage("m1")));

      transitions.add(new SynchronousReceiveActionTransition(
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v0"),
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v2"),
            new InputMessage("m2")));

      transitions.add(new InternalActionTransition(
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v0"),
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v2")));

      transitions.add(new AsynchronousReceiveActionTransition(
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v0"),
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v2"),
            new InputMessage("m2")));

      transitions.add(new InternalActionTransition(
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v0"),
            new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v3")));

      MatcherAssert.assertThat(
            HybridSystemBehaviorGeneratorUtils.findConcreteInternalActionTransition(transitions,
                  new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v0")),
            Matchers.containsInAnyOrder(
                  new InternalActionTransition(
                        new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v0"),
                        new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v3")),
                  new InternalActionTransition(
                        new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v0"),
                        new com.sesygroup.choreography.concreteparticipantbehavior.model.State("v2"))));
   }

}
