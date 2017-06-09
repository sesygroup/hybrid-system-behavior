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
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.sesygroup.choreography.concreteparticipantbehavior.model.ConcreteParticipantBehavior;
import com.sesygroup.choreography.concreteparticipantbehavior.model.Message;
import com.sesygroup.choreography.concreteparticipantbehavior.model.State;
import com.sesygroup.choreography.concreteparticipantbehavior.model.Transition;
import com.sesygroup.choreography.concreteparticipantbehavior.model.action.AsynchronousReceiveActionTransition;
import com.sesygroup.choreography.concreteparticipantbehavior.model.action.AsynchronousSendActionTransition;
import com.sesygroup.choreography.concreteparticipantbehavior.model.action.InternalActionTransition;
import com.sesygroup.choreography.concreteparticipantbehavior.model.action.SynchronousReceiveActionTransition;
import com.sesygroup.choreography.concreteparticipantbehavior.model.action.SynchronousSendActionTransition;
import com.sesygroup.choreography.concreteparticipantbehavior.model.message.InputMessage;
import com.sesygroup.choreography.concreteparticipantbehavior.model.message.OutputMessage;
import com.sesygroup.choreography.hybridsystembehavior.generator.HybridSystemBehaviorGenerator;
import com.sesygroup.choreography.hybridsystembehavior.model.HybridSystemBehavior;
import com.sesygroup.choreography.hybridsystembehavior.model.Participant;

/**
 *
 * @author Alexander Perucci (http://www.alexanderperucci.com/)
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class HybridSystemBehaviorGeneratorTest {
   @Mock
   private ConcreteParticipantBehavior mockedConcreteParticipantBehaviorP1;
   @Mock
   private ConcreteParticipantBehavior mockedConcreteParticipantBehaviorP2;
   @Mock
   private ConcreteParticipantBehavior mockedConcreteParticipantBehaviorP3;
   @Mock
   private ConcreteParticipantBehavior mockedConcreteParticipantBehaviorP4;
   @Mock
   private ConcreteParticipantBehavior mockedConcreteParticipantBehaviorP5;
   @Mock
   private ConcreteParticipantBehavior mockedConcreteParticipantBehaviorP6;

   @Before
   public void setUpMockedConcreteParticipantBehaviorP1() {
      Mockito.when(mockedConcreteParticipantBehaviorP1.getInitialState()).thenReturn(new State("s0"));
      Mockito.when(mockedConcreteParticipantBehaviorP1.getStates())
            .thenReturn(new LinkedHashSet<State>(Arrays.asList(new State("s0"), new State("s1"))));
      Mockito.when(mockedConcreteParticipantBehaviorP1.getMessages())
            .thenReturn(new LinkedHashSet<Message>(Arrays.asList(new OutputMessage("m1"))));
      Mockito.when(mockedConcreteParticipantBehaviorP1.getTransitions()).thenReturn(new LinkedHashSet<Transition>(Arrays
            .asList(new AsynchronousSendActionTransition(new State("s0"), new State("s1"), new OutputMessage("m1")))));
   }

   @Before
   public void setUpMockedConcreteParticipantBehaviorP2() {
      Mockito.when(mockedConcreteParticipantBehaviorP2.getInitialState()).thenReturn(new State("s0"));
      Mockito.when(mockedConcreteParticipantBehaviorP2.getStates())
            .thenReturn(new LinkedHashSet<State>(Arrays.asList(new State("s0"), new State("s2"), new State("s5"))));
      Mockito.when(mockedConcreteParticipantBehaviorP2.getMessages())
            .thenReturn(new LinkedHashSet<Message>(Arrays.asList(new OutputMessage("m2"), new OutputMessage("m5"))));
      Mockito.when(mockedConcreteParticipantBehaviorP2.getTransitions())
            .thenReturn(new LinkedHashSet<Transition>(Arrays.asList(
                  new AsynchronousSendActionTransition(new State("s0"), new State("s2"), new OutputMessage("m2")),
                  new InternalActionTransition(new State("s2"), new State("s5")),
                  new AsynchronousSendActionTransition(new State("s2"), new State("s5"), new OutputMessage("m5")))));
   }

   @Before
   public void setUpMockedConcreteParticipantBehaviorP3() {
      Mockito.when(mockedConcreteParticipantBehaviorP3.getInitialState()).thenReturn(new State("s0"));
      Mockito.when(mockedConcreteParticipantBehaviorP3.getStates()).thenReturn(new LinkedHashSet<State>(
            Arrays.asList(new State("s0"), new State("s1"), new State("s2"), new State("s5"))));
      Mockito.when(mockedConcreteParticipantBehaviorP3.getMessages()).thenReturn(new LinkedHashSet<Message>(Arrays
            .asList(new InputMessage("m1"), new InputMessage("m2"), new InputMessage("m5"), new OutputMessage("m6"))));
      Mockito.when(mockedConcreteParticipantBehaviorP3.getTransitions())
            .thenReturn(new LinkedHashSet<Transition>(Arrays.asList(
                  new AsynchronousReceiveActionTransition(new State("s0"), new State("s1"), new InputMessage("m1")),
                  new AsynchronousReceiveActionTransition(new State("s1"), new State("s2"), new InputMessage("m2")),
                  new AsynchronousReceiveActionTransition(new State("s2"), new State("s5"), new InputMessage("m5")),
                  new InternalActionTransition(new State("s2"), new State("s5")),
                  new SynchronousSendActionTransition(new State("s2"), new State("s5"), new OutputMessage("m6")))));
   }

   @Before
   public void setUpMockedConcreteParticipantBehaviorP4() {
      Mockito.when(mockedConcreteParticipantBehaviorP4.getInitialState()).thenReturn(new State("s0"));
      Mockito.when(mockedConcreteParticipantBehaviorP4.getStates())
            .thenReturn(new LinkedHashSet<State>(Arrays.asList(new State("s0"), new State("s3"))));
      Mockito.when(mockedConcreteParticipantBehaviorP4.getMessages())
            .thenReturn(new LinkedHashSet<Message>(Arrays.asList(new OutputMessage("m3"))));
      Mockito.when(mockedConcreteParticipantBehaviorP4.getTransitions())
            .thenReturn(new LinkedHashSet<Transition>(Arrays.asList(
                  new SynchronousSendActionTransition(new State("s0"), new State("s3"), new OutputMessage("m3")),
                  new InternalActionTransition(new State("s0"), new State("s3")))));
   }

   @Before
   public void setUpMockedConcreteParticipantBehaviorP5() {
      Mockito.when(mockedConcreteParticipantBehaviorP5.getInitialState()).thenReturn(new State("s0"));
      Mockito.when(mockedConcreteParticipantBehaviorP5.getStates())
            .thenReturn(new LinkedHashSet<State>(Arrays.asList(new State("s0"), new State("s4"), new State("s5"))));
      Mockito.when(mockedConcreteParticipantBehaviorP5.getMessages())
            .thenReturn(new LinkedHashSet<Message>(Arrays.asList(new OutputMessage("m4"), new OutputMessage("m7"))));
      Mockito.when(mockedConcreteParticipantBehaviorP5.getTransitions())
            .thenReturn(new LinkedHashSet<Transition>(Arrays.asList(
                  new SynchronousSendActionTransition(new State("s0"), new State("s4"), new OutputMessage("m4")),
                  new SynchronousSendActionTransition(new State("s0"), new State("s5"), new OutputMessage("m7")),
                  new InternalActionTransition(new State("s0"), new State("s5")),
                  new SynchronousSendActionTransition(new State("s4"), new State("s5"), new OutputMessage("m7")))));
   }

   @Before
   public void setUpMockedConcreteParticipantBehaviorP6() {
      Mockito.when(mockedConcreteParticipantBehaviorP6.getInitialState()).thenReturn(new State("s0"));
      Mockito.when(mockedConcreteParticipantBehaviorP6.getStates()).thenReturn(new LinkedHashSet<State>(
            Arrays.asList(new State("s0"), new State("s3"), new State("s4"), new State("s5"))));
      Mockito.when(mockedConcreteParticipantBehaviorP6.getMessages()).thenReturn(new LinkedHashSet<Message>(Arrays
            .asList(new InputMessage("m3"), new InputMessage("m4"), new InputMessage("m6"), new InputMessage("m7"))));
      Mockito.when(mockedConcreteParticipantBehaviorP6.getTransitions())
            .thenReturn(new LinkedHashSet<Transition>(Arrays.asList(
                  new SynchronousReceiveActionTransition(new State("s0"), new State("s3"), new InputMessage("m3")),
                  new SynchronousReceiveActionTransition(new State("s0"), new State("s4"), new InputMessage("m4")),
                  new InternalActionTransition(new State("s0"), new State("s5")),
                  new SynchronousReceiveActionTransition(new State("s3"), new State("s4"), new InputMessage("m6")),
                  new SynchronousReceiveActionTransition(new State("s4"), new State("s5"), new InputMessage("m7")))));
   }

   @After
   public void tearDown() {
      mockedConcreteParticipantBehaviorP1 = null;
      mockedConcreteParticipantBehaviorP2 = null;
      mockedConcreteParticipantBehaviorP3 = null;
      mockedConcreteParticipantBehaviorP4 = null;
      mockedConcreteParticipantBehaviorP5 = null;
      mockedConcreteParticipantBehaviorP6 = null;
   }

   @Test
   public void testGenerate() {
      Map<Participant, ConcreteParticipantBehavior> participantToConcreteParticipantBehaviorMap
            = new HashMap<Participant, ConcreteParticipantBehavior>();
      participantToConcreteParticipantBehaviorMap.put(new Participant("P1"), mockedConcreteParticipantBehaviorP1);
      participantToConcreteParticipantBehaviorMap.put(new Participant("P2"), mockedConcreteParticipantBehaviorP2);
      participantToConcreteParticipantBehaviorMap.put(new Participant("P3"), mockedConcreteParticipantBehaviorP3);
      participantToConcreteParticipantBehaviorMap.put(new Participant("P4"), mockedConcreteParticipantBehaviorP4);
      participantToConcreteParticipantBehaviorMap.put(new Participant("P5"), mockedConcreteParticipantBehaviorP5);
      participantToConcreteParticipantBehaviorMap.put(new Participant("P6"), mockedConcreteParticipantBehaviorP6);

      Map<Participant, Integer> participantToMessageQueueSizeMap = new HashMap<Participant, Integer>();
      participantToMessageQueueSizeMap.put(new Participant("P1"), 1);
      participantToMessageQueueSizeMap.put(new Participant("P2"), 1);
      participantToMessageQueueSizeMap.put(new Participant("P3"), 1);
      participantToMessageQueueSizeMap.put(new Participant("P4"), 0);
      participantToMessageQueueSizeMap.put(new Participant("P5"), 0);
      participantToMessageQueueSizeMap.put(new Participant("P6"), 0);

      HybridSystemBehaviorGenerator hybridSystemBehaviorGenerator = new HybridSystemBehaviorGenerator(
            participantToConcreteParticipantBehaviorMap, participantToMessageQueueSizeMap);

      HybridSystemBehavior hybridSystemBehavior = hybridSystemBehaviorGenerator.generate();

      MatcherAssert.assertThat(hybridSystemBehavior.getStates().size(), Matchers.is(162));
      MatcherAssert.assertThat(hybridSystemBehavior.getTransitions().size(), Matchers.is(468));
   }

}
