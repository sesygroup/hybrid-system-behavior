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

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.PredicateUtils;
import org.apache.commons.lang3.Validate;

import com.sesygroup.choreography.concreteparticipantbehavior.model.ConcreteParticipantBehavior;
import com.sesygroup.choreography.concreteparticipantbehavior.model.Message;
import com.sesygroup.choreography.concreteparticipantbehavior.model.message.InputMessage;
import com.sesygroup.choreography.concreteparticipantbehavior.model.message.OutputMessage;
import com.sesygroup.choreography.hybridsystembehavior.model.HybridSystemBehavior;
import com.sesygroup.choreography.hybridsystembehavior.model.Participant;

/**
 *
 * @author Alexander Perucci (http://www.alexanderperucci.com/)
 *
 */
public class Validation {

   /**
    * <p>
    * Validate that the specified argument is not {@code null} by using
    * {@link Validate#notNull(Object, String, Object...)}; otherwise throwing a {@code NullPointerException}.
    * </p>
    *
    * <p>
    * Validate that the specified argument is not empty by using {@link Validate#notEmpty(Map, String, Object...)};
    * otherwise throwing a {@code IllegalArgumentException}.
    * </p>
    *
    * <p>
    * Validate that the key set of specified argument has no {@code null} elements by using
    * {@link Validate#noNullElements(Iterable, String, Object...)}; otherwise throwing a
    * {@code IllegalArgumentException}.
    * </p>
    *
    * <p>
    * Validate that the values of specified argument has no {@code null} elements by using
    * {@link Validate#noNullElements(Iterable, String, Object...)}; otherwise throwing a
    * {@code IllegalArgumentException}.
    * </p>
    *
    * <p>
    * Validate that for each pair of participants i and j, where i is not equal to j, the set of {@link InputMessage} i
    * and j are mutually disjoint; otherwise throwing a {@code IllegalArgumentException}.
    * </p>
    *
    * <p>
    * Validate that for each pair of participants i and j, where i is not equal to j, the set of {@link OutputMessage} i
    * and j are mutually disjoint; otherwise throwing a {@code IllegalArgumentException}.
    * </p>
    *
    * @param participantToConcreteParticipantBehaviorMap the map to check
    */
   public static final void validateParticipantToConcreteParticipantBehaviorMap(
         final Map<Participant, ConcreteParticipantBehavior> participantToConcreteParticipantBehaviorMap) {
      Validate.notNull(participantToConcreteParticipantBehaviorMap, ValidationMessages.IS_NULL_EXCEPTION_MESSAGE,
            "participantToConcreteParticipantBehaviorMap");

      Validate.notEmpty(participantToConcreteParticipantBehaviorMap, ValidationMessages.IS_EMPTY_MAP_EXCEPTION_MESSAGE,
            "participantToConcreteParticipantBehaviorMap");

      Validate.noNullElements(participantToConcreteParticipantBehaviorMap.keySet(),
            ValidationMessages.IS_ELEMENTS_NULL_EXCEPTION_MESSAGE,
            "participantToConcreteParticipantBehaviorMap.keySet()");

      Validate.noNullElements(participantToConcreteParticipantBehaviorMap.values(),
            ValidationMessages.IS_ELEMENTS_NULL_EXCEPTION_MESSAGE,
            "participantToConcreteParticipantBehaviorMap.values()");

      validateEmptyIntersectionConcreteParticipantBehaviorInputMessages(participantToConcreteParticipantBehaviorMap);

      validateEmptyIntersectionConcreteParticipantBehaviorOutputMessages(participantToConcreteParticipantBehaviorMap);
   }

   /**
    * <p>
    * Validate that the specified argument is not {@code null} by using
    * {@link Validate#notNull(Object, String, Object...)}; otherwise throwing a {@code NullPointerException}.
    * </p>
    *
    * <p>
    * Validate that the specified argument is not empty by using {@link Validate#notEmpty(Map, String, Object...)};
    * otherwise throwing a {@code IllegalArgumentException}.
    * </p>
    *
    * <p>
    * Validate that the key set of specified argument has no {@code null} elements by using
    * {@link Validate#noNullElements(Iterable, String, Object...)}; otherwise throwing a
    * {@code IllegalArgumentException}.
    * </p>
    *
    * <p>
    * Validate that the values of specified argument has no {@code null} elements by using
    * {@link Validate#noNullElements(Iterable, String, Object...)}; otherwise throwing a
    * {@code IllegalArgumentException}.
    * </p>
    *
    * @param participantToMessageQueueSizeMap the map to check
    */
   public static final void validateParticipantToMessageQueueSizeMap(
         final Map<Participant, Integer> participantToMessageQueueSizeMap) {
      Validate.notNull(participantToMessageQueueSizeMap, ValidationMessages.IS_NULL_EXCEPTION_MESSAGE,
            "participantToMessageQueueSizeMap");

      Validate.notEmpty(participantToMessageQueueSizeMap, ValidationMessages.IS_EMPTY_MAP_EXCEPTION_MESSAGE,
            "participantToMessageQueueSizeMap");

      Validate.noNullElements(participantToMessageQueueSizeMap.keySet(),
            ValidationMessages.IS_ELEMENTS_NULL_EXCEPTION_MESSAGE, "participantToMessageQueueSizeMap.keySet()");

      Validate.noNullElements(participantToMessageQueueSizeMap.values(),
            ValidationMessages.IS_ELEMENTS_NULL_EXCEPTION_MESSAGE, "participantToMessageQueueSizeMap.values()");
   }

   /**
    * Validate that the key set of {@code <i>participantToConcreteParticipantBehaviorMap</i>} contain exactly the same
    * elements with exactly the same cardinalities of {@code <i>participantToMessageQueueSizeMap</i>} by using
    * {@link CollectionUtils#isEqualCollection(Collection, Collection)}; otherwise throwing a
    * {@code IllegalArgumentException}.
    *
    * @param participantToConcreteParticipantBehaviorMap the map to check
    * @param participantToMessageQueueSizeMap the map to check
    */
   public static final void validateEqualkeySet(
         final Map<Participant, ConcreteParticipantBehavior> participantToConcreteParticipantBehaviorMap,
         final Map<Participant, Integer> participantToMessageQueueSizeMap) {

      Validate.isTrue(
            CollectionUtils.isEqualCollection(participantToConcreteParticipantBehaviorMap.keySet(),
                  participantToMessageQueueSizeMap.keySet()),
            ValidationMessages.IS_NOT_EQUAL_COLLETION_EXCEPTION_MESSAGE,
            "participantToConcreteParticipantBehaviorMap.keySet()", "participantToMessageQueueSizeMap.keySet()",
            participantToConcreteParticipantBehaviorMap.keySet(), participantToMessageQueueSizeMap.keySet());
   }

   /**
    * Validate that the value of specified argument is grater than or equal to zero; otherwise throwing a
    * {@code IllegalArgumentException}.
    *
    * @param messageQueueSize the size of the queue to check
    */
   public static final void validateMessageQueueSize(final int messageQueueSize) {
      Validate.isTrue(messageQueueSize >= 0, ValidationMessages.IS_NOT_GRATER_OR_EQUAL_EXCEPTION_MESSAGE,
            "messageQueueSize", 0, messageQueueSize);
   }

   /**
    * Validate that for each pair of participants i and j the set of {@link InputMessage}s of i and j are disjoint by
    * using {@link CollectionUtils#containsAny(Collection, Collection)}; otherwise throwing a
    * {@code IllegalArgumentException}.
    *
    * @param participantToConcreteParticipantBehaviorMap the concrete participant behaviors to check
    */
   public static final void validateEmptyIntersectionConcreteParticipantBehaviorInputMessages(
         final Map<Participant, ConcreteParticipantBehavior> participantToConcreteParticipantBehaviorMap) {
      List<Participant> participants
            = new LinkedList<Participant>(participantToConcreteParticipantBehaviorMap.keySet());
      for (int i = 0; i < participants.size() - 1; i++) {
         for (int j = i + 1; j < participants.size(); j++) {
            Collection<Message> coll1 = CollectionUtils.select(
                  participantToConcreteParticipantBehaviorMap.get(participants.get(i)).getMessages(),
                  PredicateUtils.instanceofPredicate(InputMessage.class));

            Collection<Message> coll2 = CollectionUtils.select(
                  participantToConcreteParticipantBehaviorMap.get(participants.get(j)).getMessages(),
                  PredicateUtils.instanceofPredicate(InputMessage.class));

            /*
             * CollectionUtils.containsAny(coll1, coll2) returns true iff at least one element is in both collections.
             * In other words, this method returns true iff the intersection of coll1 and coll2 is not empty.
             */
            Validate.isTrue(!CollectionUtils.containsAny(coll1, coll2),
                  ValidationMessages.IS_NOT_EMPTY_INTERSECTION_INPUT_MESSAGES_EXCEPTION_MESSAGE,
                  participants.get(i).getName(), coll1, participants.get(j).getName(), coll2);
         }
      }
   }

   /**
    * Validate that for each pair of participants i and j the set of {@link OutputMessage}s of i and j are disjoint by
    * using {@link CollectionUtils#containsAny(Collection, Collection)}; otherwise throwing a
    * {@code IllegalArgumentException}.
    *
    * @param participantToConcreteParticipantBehaviorMap the concrete participant behaviors to check
    */
   public static final void validateEmptyIntersectionConcreteParticipantBehaviorOutputMessages(
         final Map<Participant, ConcreteParticipantBehavior> participantToConcreteParticipantBehaviorMap) {
      List<Participant> participants
            = new LinkedList<Participant>(participantToConcreteParticipantBehaviorMap.keySet());
      /*
       * TODO merge this method with the previous one
       * 'validateEmptyIntersectionConcreteParticipantBehaviorInputMessages' by using generics Class<? extends Message>
       * messageClass. The problem is to restrict messageClass parameter only to InputMessage.class and
       * OutputMessage.class but the parameter accept also Message.class
       */
      for (int i = 0; i < participants.size() - 1; i++) {
         for (int j = i + 1; j < participants.size(); j++) {
            Collection<Message> coll1 = CollectionUtils.select(
                  participantToConcreteParticipantBehaviorMap.get(participants.get(i)).getMessages(),
                  PredicateUtils.instanceofPredicate(OutputMessage.class));

            Collection<Message> coll2 = CollectionUtils.select(
                  participantToConcreteParticipantBehaviorMap.get(participants.get(j)).getMessages(),
                  PredicateUtils.instanceofPredicate(OutputMessage.class));

            /*
             * CollectionUtils.containsAny(coll1, coll2) returns true iff at least one element is in both collections.
             * In other words, this method returns true iff the intersection of coll1 and coll2 is not empty.
             */
            Validate.isTrue(!CollectionUtils.containsAny(coll1, coll2),
                  ValidationMessages.IS_NOT_EMPTY_INTERSECTION_OUTPUT_MESSAGES_EXCEPTION_MESSAGE,
                  participants.get(i).getName(), coll1, participants.get(j).getName(), coll2);
         }
      }
   }

   /**
    * <p>
    * Validate that the {@code <i>synchronousSendActionTransitions</i>} collection has only zero or one element by using
    * {@link Validate#inclusiveBetween(Object, Object, Comparable, String, Object...)}; otherwise throwing a
    * {@code IllegalArgumentException}.
    * </p>
    *
    * <p>
    * Validate that the {@code <i>synchronousReceiveActionTransitions</i>} collection has only zero or one element by
    * using {@link Validate#inclusiveBetween(Object, Object, Comparable, String, Object...)}; otherwise throwing a
    * {@code IllegalArgumentException}.
    * </p>
    *
    * <p>
    * Validate that the {@code <i>synchronousSendActionTransitions</i>} collection and the
    * {@code <i>synchronousReceiveActionTransitions</i>} collection have same cardinality; otherwise throwing a
    * {@code IllegalArgumentException}.
    * </p>
    *
    * @param synchronousSendActionTransitions the synchronous send action transitions to check
    * @param synchronousReceiveActionTransitions the synchronous send action transitions to check
    */
   public static final void validateFoundedSynchronousSendReceiveActionTransition(
         final Collection<com.sesygroup.choreography.concreteparticipantbehavior.model.Transition> synchronousSendActionTransitions,
         final Collection<com.sesygroup.choreography.concreteparticipantbehavior.model.Transition> synchronousReceiveActionTransitions) {

      Validate.inclusiveBetween(0, 1, synchronousSendActionTransitions.size(),
            ValidationMessages.IS_NOT_IN_BOUNDARIES_EXCEPTION_MESSAGE, "synchronousSendActionTransitions.size()",
            synchronousSendActionTransitions.size(), 0, 1, synchronousSendActionTransitions);
      Validate.inclusiveBetween(0, 1, synchronousReceiveActionTransitions.size(),
            ValidationMessages.IS_NOT_IN_BOUNDARIES_EXCEPTION_MESSAGE, "synchronousReceiveActionTransitions.size()",
            synchronousReceiveActionTransitions.size(), 0, 1, synchronousReceiveActionTransitions);
      /*Validate.isTrue(synchronousSendActionTransitions.size() == synchronousReceiveActionTransitions.size(),
            ValidationMessages.IS_NOT_EQUAL_CRDINALITY_COLLETION_EXCEPTION_MESSAGE, "synchronousSendActionTransitions",
            "synchronousReceiveActionTransitions", synchronousSendActionTransitions,
            synchronousSendActionTransitions.size(), synchronousReceiveActionTransitions,
            synchronousReceiveActionTransitions.size());
            */
   }

   /**
    * <p>
    * Validate that for each transition that are in the {@code <i>hybridSystemBehavior</i>} the source state is also
    * contained in the set of state; otherwise throwing a {@code IllegalArgumentException}.
    * </p>
    *
    * <p>
    * Validate that for each transition that are in the {@code <i>hybridSystemBehavior</i>} the target state is also
    * contained in the set of state; otherwise throwing a {@code IllegalArgumentException}.
    * </p>
    *
    * <p>
    * Validate that the {@code <i>hybridSystemBehavior</i>} initial state is not null and it is also contained in the
    * set of state; otherwise throwing a {@code NullPointerException} or a {@code IllegalArgumentException}.
    * </p>
    *
    * @param hybridSystemBehavior the hybrid system behavior to check
    */
   public static final void validateHybridSystemBehavior(final HybridSystemBehavior hybridSystemBehavior) {

      hybridSystemBehavior.getTransitions()
            .forEach(transition -> Validate.isTrue(
                  hybridSystemBehavior.getStates().contains(transition.getSourceState()),
                  ValidationMessages.IS_STATE_NOT_IN_SET_OF_STATES_EXCEPTION_MESSAGE, transition.getSourceState()));

      hybridSystemBehavior.getTransitions()
            .forEach(transition -> Validate.isTrue(
                  hybridSystemBehavior.getStates().contains(transition.getTargetState()),
                  ValidationMessages.IS_STATE_NOT_IN_SET_OF_STATES_EXCEPTION_MESSAGE, transition.getTargetState()));

      Validate.notNull(hybridSystemBehavior.getInitialState(), ValidationMessages.IS_NULL_EXCEPTION_MESSAGE,
            "hybridSystemBehavior.getInitialState()");

      Validate.isTrue(hybridSystemBehavior.getStates().contains(hybridSystemBehavior.getInitialState()),
            ValidationMessages.IS_STATE_NOT_IN_SET_OF_STATES_EXCEPTION_MESSAGE, hybridSystemBehavior.getInitialState());
   }

   // -----------------------------------------------------------------------
   /**
    * <p>
    * {@code ValidationMessages} instances should NOT be constructed in standard programming. Instead, the class should
    * be used statically.
    * </p>
    *
    * <p>
    * This constructor is public to permit tools that require a JavaBean instance to operate.
    * </p>
    */
   public Validation() {
      super();
   }

}
