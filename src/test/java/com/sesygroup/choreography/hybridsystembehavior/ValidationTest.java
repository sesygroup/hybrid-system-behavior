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
package com.sesygroup.choreography.hybridsystembehavior;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import com.sesygroup.choreography.concreteparticipantbehavior.model.ConcreteParticipantBehavior;
import com.sesygroup.choreography.concreteparticipantbehavior.model.message.InputMessage;
import com.sesygroup.choreography.concreteparticipantbehavior.model.message.OutputMessage;
import com.sesygroup.choreography.hybridsystembehavior.Validation;
import com.sesygroup.choreography.hybridsystembehavior.model.HybridSystemBehavior;
import com.sesygroup.choreography.hybridsystembehavior.model.MessageQueue;
import com.sesygroup.choreography.hybridsystembehavior.model.Participant;
import com.sesygroup.choreography.hybridsystembehavior.model.State;
import com.sesygroup.choreography.hybridsystembehavior.model.action.InternalActionTransition;

/**
 *
 * @author Alexander Perucci (http://www.alexanderperucci.com/)
 *
 */
public class ValidationTest {

   @BeforeClass
   public static void setUp() {

   }

   @Test(expected = NullPointerException.class)
   public void testValidateParticipantToConcreteParticipantBehaviorMapNull() {
      Validation.validateParticipantToConcreteParticipantBehaviorMap(null);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testValidatePaticipantToConcreteParticipantBehavioMapEmpty() {
      Validation.validateParticipantToConcreteParticipantBehaviorMap(
            new LinkedHashMap<Participant, ConcreteParticipantBehavior>());
   }

   @Test(expected = IllegalArgumentException.class)
   public void testValidatePaticipantToConcreteParticipantBehavioMapNullElementsInKeySet() {
      Map<Participant, ConcreteParticipantBehavior> participantToConcreteParticipantBehaviorMap
            = new LinkedHashMap<Participant, ConcreteParticipantBehavior>();
      participantToConcreteParticipantBehaviorMap.put(new Participant("p1"), new ConcreteParticipantBehavior());
      participantToConcreteParticipantBehaviorMap.put(null, new ConcreteParticipantBehavior());
      Validation.validateParticipantToConcreteParticipantBehaviorMap(participantToConcreteParticipantBehaviorMap);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testValidatePaticipantToConcreteParticipantBehavioMapNullElementsInValueSet() {
      Map<Participant, ConcreteParticipantBehavior> participantToConcreteParticipantBehaviorMap = new LinkedHashMap<>();
      participantToConcreteParticipantBehaviorMap.put(new Participant("p1"), new ConcreteParticipantBehavior());
      participantToConcreteParticipantBehaviorMap.put(new Participant("p2"), null);
      Validation.validateParticipantToConcreteParticipantBehaviorMap(participantToConcreteParticipantBehaviorMap);
   }

   @Test(expected = NullPointerException.class)
   public void testValidateParticipantToMessageQueueSizeMapNull() {
      Validation.validateParticipantToMessageQueueSizeMap(null);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testValidateParticipantToMessageQueueSizeMapEmpty() {
      Validation.validateParticipantToMessageQueueSizeMap(new LinkedHashMap<Participant, Integer>());
   }

   @Test(expected = IllegalArgumentException.class)
   public void testValidateParticipantToMessageQueueSizeMapNullElementsInKeySet() {
      Map<Participant, Integer> participantToMessageQueueSizeMap = new LinkedHashMap<Participant, Integer>();
      participantToMessageQueueSizeMap.put(null, new Integer(0));
      Validation.validateParticipantToMessageQueueSizeMap(participantToMessageQueueSizeMap);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testValidateParticipantToMessageQueueSizeMapNullElementsInValueSet() {
      Map<Participant, Integer> participantToMessageQueueSizeMap = new LinkedHashMap<Participant, Integer>();
      participantToMessageQueueSizeMap.put(new Participant("p1"), null);
      Validation.validateParticipantToMessageQueueSizeMap(participantToMessageQueueSizeMap);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testValidateEqualkeySet() {
      Map<Participant, ConcreteParticipantBehavior> participantToConcreteParticipantBehaviorMap
            = new LinkedHashMap<Participant, ConcreteParticipantBehavior>();
      participantToConcreteParticipantBehaviorMap.put(new Participant("p1"), new ConcreteParticipantBehavior());

      Map<Participant, Integer> participantToMessageQueueSizeMap = new LinkedHashMap<Participant, Integer>();
      participantToMessageQueueSizeMap.put(new Participant("p1"), 1);
      participantToMessageQueueSizeMap.put(new Participant("p2"), 1);
      Validation.validateEqualkeySet(participantToConcreteParticipantBehaviorMap, participantToMessageQueueSizeMap);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testValidateMessageQueueSize() {
      Validation.validateMessageQueueSize(-1);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testValidateEmptyIntersectionConcreteParticipantBehaviorInputMessages() {
      ConcreteParticipantBehavior concreteParticipantBehaviorOne = new ConcreteParticipantBehavior();
      concreteParticipantBehaviorOne.getMessages().add(new InputMessage("m1"));
      ConcreteParticipantBehavior concreteParticipantBehaviorTwo = new ConcreteParticipantBehavior();
      concreteParticipantBehaviorTwo.getMessages().add(new InputMessage("m1"));

      Map<Participant, ConcreteParticipantBehavior> participantToConcreteParticipantBehaviorMap
            = new LinkedHashMap<Participant, ConcreteParticipantBehavior>();
      participantToConcreteParticipantBehaviorMap.put(new Participant("p1"), concreteParticipantBehaviorOne);
      participantToConcreteParticipantBehaviorMap.put(new Participant("p2"), concreteParticipantBehaviorTwo);

      Validation.validateEmptyIntersectionConcreteParticipantBehaviorInputMessages(
            participantToConcreteParticipantBehaviorMap);

   }

   @Test(expected = IllegalArgumentException.class)
   public void testValidateEmptyIntersectionConcreteParticipantBehaviorOutputMessages() {
      ConcreteParticipantBehavior concreteParticipantBehaviorOne = new ConcreteParticipantBehavior();
      concreteParticipantBehaviorOne.getMessages().add(new OutputMessage("m1"));
      ConcreteParticipantBehavior concreteParticipantBehaviorTwo = new ConcreteParticipantBehavior();
      concreteParticipantBehaviorTwo.getMessages().add(new OutputMessage("m1"));

      Map<Participant, ConcreteParticipantBehavior> participantToConcreteParticipantBehaviorMap
            = new LinkedHashMap<Participant, ConcreteParticipantBehavior>();
      participantToConcreteParticipantBehaviorMap.put(new Participant("p1"), concreteParticipantBehaviorOne);
      participantToConcreteParticipantBehaviorMap.put(new Participant("p2"), concreteParticipantBehaviorTwo);

      Validation.validateEmptyIntersectionConcreteParticipantBehaviorOutputMessages(
            participantToConcreteParticipantBehaviorMap);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testValidateStatesHybridSystemBehavior() {
      Participant participantOne = new Participant("p1");
      Participant participantTwo = new Participant("p2");

      com.sesygroup.choreography.concreteparticipantbehavior.model.State concreteStateParticipantOne
            = new com.sesygroup.choreography.concreteparticipantbehavior.model.State("s0");
      com.sesygroup.choreography.concreteparticipantbehavior.model.State concreteStateParticipantTwo
            = new com.sesygroup.choreography.concreteparticipantbehavior.model.State("s0");

      MessageQueue messageQueueParticipantOne = new MessageQueue(1);
      messageQueueParticipantOne.getQueue().add(new OutputMessage("m1"));
      MessageQueue messageQueueParticipantTwo = new MessageQueue(1);

      Map<Participant, com.sesygroup.choreography.concreteparticipantbehavior.model.State> participantStates
            = new LinkedHashMap<Participant, com.sesygroup.choreography.concreteparticipantbehavior.model.State>();
      participantStates.put(participantOne, concreteStateParticipantOne);
      participantStates.put(participantTwo, concreteStateParticipantTwo);

      Map<Participant, MessageQueue> participantQueues = new LinkedHashMap<Participant, MessageQueue>();
      participantQueues.put(participantOne, messageQueueParticipantOne);
      participantQueues.put(participantTwo, messageQueueParticipantTwo);
      State state = new State(participantStates, participantQueues);

      HybridSystemBehavior hybridSystemBehavior = new HybridSystemBehavior();
      hybridSystemBehavior.getTransitions().add(new InternalActionTransition(state, state));
      Validation.validateHybridSystemBehavior(hybridSystemBehavior);
   }

   @Test(expected = NullPointerException.class)
   public void testValidateInitialStateHybridSystemBehavior() {
      Participant participantOne = new Participant("p1");
      Participant participantTwo = new Participant("p2");

      com.sesygroup.choreography.concreteparticipantbehavior.model.State concreteStateParticipantOne
            = new com.sesygroup.choreography.concreteparticipantbehavior.model.State("s0");
      com.sesygroup.choreography.concreteparticipantbehavior.model.State concreteStateParticipantTwo
            = new com.sesygroup.choreography.concreteparticipantbehavior.model.State("s0");

      MessageQueue messageQueueParticipantOne = new MessageQueue(1);
      messageQueueParticipantOne.getQueue().add(new OutputMessage("m1"));
      MessageQueue messageQueueParticipantTwo = new MessageQueue(1);

      Map<Participant, com.sesygroup.choreography.concreteparticipantbehavior.model.State> participantStates
            = new LinkedHashMap<Participant, com.sesygroup.choreography.concreteparticipantbehavior.model.State>();
      participantStates.put(participantOne, concreteStateParticipantOne);
      participantStates.put(participantTwo, concreteStateParticipantTwo);

      Map<Participant, MessageQueue> participantQueues = new LinkedHashMap<Participant, MessageQueue>();
      participantQueues.put(participantOne, messageQueueParticipantOne);
      participantQueues.put(participantTwo, messageQueueParticipantTwo);
      State state = new State(participantStates, participantQueues);

      HybridSystemBehavior hybridSystemBehavior = new HybridSystemBehavior();
      hybridSystemBehavior.getStates().add(state);
      hybridSystemBehavior.getTransitions().add(new InternalActionTransition(state, state));
      Validation.validateHybridSystemBehavior(hybridSystemBehavior);
   }

}
