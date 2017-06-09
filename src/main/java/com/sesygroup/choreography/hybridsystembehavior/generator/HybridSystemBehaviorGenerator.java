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

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sesygroup.choreography.concreteparticipantbehavior.model.ConcreteParticipantBehavior;
import com.sesygroup.choreography.hybridsystembehavior.Validation;
import com.sesygroup.choreography.hybridsystembehavior.model.HybridSystemBehavior;
import com.sesygroup.choreography.hybridsystembehavior.model.Participant;
import com.sesygroup.choreography.hybridsystembehavior.model.State;
import com.sesygroup.choreography.hybridsystembehavior.model.Transition;

/**
 *
 * @author Alexander Perucci (http://www.alexanderperucci.com/)
 *
 */
public class HybridSystemBehaviorGenerator {
   private final Logger logger = LoggerFactory.getLogger(HybridSystemBehaviorGenerator.class);
   private Map<Participant, ConcreteParticipantBehavior> participantToConcreteParticipantBehaviorMap;
   private Map<Participant, Integer> participantToMessageQueueSizeMap;

   public HybridSystemBehaviorGenerator(
         final Map<Participant, ConcreteParticipantBehavior> participantToConcreteParticipantBehaviorMap,
         final int messageQueueSize) {
      super();

      Validation.validateParticipantToConcreteParticipantBehaviorMap(participantToConcreteParticipantBehaviorMap);
      Validation.validateMessageQueueSize(messageQueueSize);

      participantToMessageQueueSizeMap = new LinkedHashMap<Participant, Integer>();
      participantToConcreteParticipantBehaviorMap
            .forEach((key, value) -> participantToMessageQueueSizeMap.put(key, new Integer(messageQueueSize)));
   }

   public HybridSystemBehaviorGenerator(
         final Map<Participant, ConcreteParticipantBehavior> participantToConcreteParticipantBehaviorMap,
         final Map<Participant, Integer> participantToMessageQueueSizeMap) {
      super();

      Validation.validateParticipantToConcreteParticipantBehaviorMap(participantToConcreteParticipantBehaviorMap);
      Validation.validateParticipantToMessageQueueSizeMap(participantToMessageQueueSizeMap);
      Validation.validateEqualkeySet(participantToConcreteParticipantBehaviorMap, participantToMessageQueueSizeMap);

      this.participantToConcreteParticipantBehaviorMap = participantToConcreteParticipantBehaviorMap;
      this.participantToMessageQueueSizeMap = participantToMessageQueueSizeMap;

   }

   public Map<Participant, ConcreteParticipantBehavior> getParticipantToConcreteParticipantBehaviorMap() {
      return participantToConcreteParticipantBehaviorMap;
   }

   public Map<Participant, Integer> getParticipantToMessageQueueSizeMap() {
      return participantToMessageQueueSizeMap;
   }

   public HybridSystemBehavior generate() {
      HybridSystemBehavior hybridSystemBehavior = new HybridSystemBehavior(participantToConcreteParticipantBehaviorMap);
      Set<State> sourceStatesToBeCheck = new LinkedHashSet<State>();
      Set<State> statesVerified = new LinkedHashSet<State>();

      /*
       * FIRST STEP: create initial state
       */
      State initialState = HybridSystemBehaviorGeneratorUtils
            .createInitialState(participantToConcreteParticipantBehaviorMap, participantToMessageQueueSizeMap);
      hybridSystemBehavior.setInitialState(initialState);
      sourceStatesToBeCheck.add(initialState);

      /*
       * ITERATION STEP: find and add all states and transitions as long as we have a source states to be check
       */
      while (sourceStatesToBeCheck.iterator().hasNext()) {
         State sourceState = sourceStatesToBeCheck.iterator().next();
         sourceStatesToBeCheck.remove(sourceState);

         if (!hybridSystemBehavior.getStates().stream().collect(Collectors.toList()).contains(sourceState)) {
            hybridSystemBehavior.getStates().add(sourceState);
         }

         if (!statesVerified.stream().collect(Collectors.toList()).contains(sourceState)) {
            HybridSystemBehaviorGeneratorUtils
                  .findAsynchSendActTransitions(participantToConcreteParticipantBehaviorMap, sourceState)
                  .forEach(transition -> manageTransition(transition, hybridSystemBehavior, sourceStatesToBeCheck));
            HybridSystemBehaviorGeneratorUtils
                  .findAsynchReceiveActAndMsgConsumptionTransitions(participantToConcreteParticipantBehaviorMap,
                        sourceState)
                  .forEach(transition -> manageTransition(transition, hybridSystemBehavior, sourceStatesToBeCheck));
            HybridSystemBehaviorGeneratorUtils
                  .findSynchSendReceiveActAndMsgConsumptionTransitions(participantToConcreteParticipantBehaviorMap,
                        sourceState)
                  .forEach(transition -> manageTransition(transition, hybridSystemBehavior, sourceStatesToBeCheck));
            HybridSystemBehaviorGeneratorUtils
                  .findInternalActionTransitions(participantToConcreteParticipantBehaviorMap, sourceState)
                  .forEach(transition -> manageTransition(transition, hybridSystemBehavior, sourceStatesToBeCheck));

            statesVerified.add(sourceState);
         }
      }

      /*
       * Validate the generated hybrid system behavior
       */
      Validation.validateHybridSystemBehavior(hybridSystemBehavior);

      return hybridSystemBehavior;
   }

   private void manageTransition(final Transition transition, final HybridSystemBehavior hybridSystemBehavior,
         final Set<State> sourceStatesToCheck) {
      /*
       * important: convert the set to list before using the contains method because, if you change an element in the
       * Set, Set.contains(element) may fail to locate it, since the hashCode of the element will be different than what
       * it was when the element was first added to the HashSet
       */
      if (!hybridSystemBehavior.getTransitions().stream().collect(Collectors.toList()).contains(transition)) {
         hybridSystemBehavior.getTransitions().add(transition);
      } else {
         logger.info("The transition " + transition
               + " already exists in the set of transitions, therefore the transition it is not added");
         return;
      }

      if (!sourceStatesToCheck.stream().collect(Collectors.toList()).contains(transition.getTargetState())) {
         sourceStatesToCheck.add(transition.getTargetState());
      } else {
         logger.info("The target state " + transition.getTargetState() + " of the transition " + transition
               + " already exists in the set of source states to be check, therefore it is not added");

      }

      List<State> states = hybridSystemBehavior.getStates().stream().collect(Collectors.toList());
      if (!states.contains(transition.getTargetState())) {
         hybridSystemBehavior.getStates().add(transition.getTargetState());
      } else {
         logger.info("The target state " + transition.getTargetState() + " of the transition " + transition
               + " already exists in the set of hybrid system behavior states, therefore the target state will be changed with the existing one");
         transition.setTargetState(states.get(states.indexOf(transition.getTargetState())));
      }

   }
}
