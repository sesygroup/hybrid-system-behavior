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

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.PredicateUtils;

import com.sesygroup.choreography.concreteparticipantbehavior.model.ConcreteParticipantBehavior;
import com.sesygroup.choreography.concreteparticipantbehavior.model.Message;
import com.sesygroup.choreography.concreteparticipantbehavior.model.Transition;
import com.sesygroup.choreography.concreteparticipantbehavior.model.action.AsynchronousReceiveActionTransition;
import com.sesygroup.choreography.concreteparticipantbehavior.model.action.AsynchronousSendActionTransition;
import com.sesygroup.choreography.concreteparticipantbehavior.model.action.SynchronousReceiveActionTransition;
import com.sesygroup.choreography.concreteparticipantbehavior.model.action.SynchronousSendActionTransition;
import com.sesygroup.choreography.concreteparticipantbehavior.model.message.InputMessage;
import com.sesygroup.choreography.concreteparticipantbehavior.model.message.OutputMessage;
import com.sesygroup.choreography.hybridsystembehavior.Validation;
import com.sesygroup.choreography.hybridsystembehavior.model.MessageQueue;
import com.sesygroup.choreography.hybridsystembehavior.model.Participant;
import com.sesygroup.choreography.hybridsystembehavior.model.State;
import com.sesygroup.choreography.hybridsystembehavior.model.action.AsynchReceiveActAndMsgConsumptionTransition;
import com.sesygroup.choreography.hybridsystembehavior.model.action.AsynchSendActTransition;
import com.sesygroup.choreography.hybridsystembehavior.model.action.InternalActionTransition;
import com.sesygroup.choreography.hybridsystembehavior.model.action.SynchSendReceiveActAndMsgConsumptionTransition;

/**
 *
 * @author Alexander Perucci (http://www.alexanderperucci.com/)
 *
 */
public class HybridSystemBehaviorGeneratorUtils {

   public static State createInitialState(
         final Map<Participant, ConcreteParticipantBehavior> participantToConcreteParticipantBehaviorMap,
         final Map<Participant, Integer> participantToMessageQueueSizeMap) {

      Map<Participant, com.sesygroup.choreography.concreteparticipantbehavior.model.State> participantToConcreteParticipantBehaviorStateMap
            = new LinkedHashMap<Participant, com.sesygroup.choreography.concreteparticipantbehavior.model.State>();
      participantToConcreteParticipantBehaviorMap.forEach(
            (key, value) -> participantToConcreteParticipantBehaviorStateMap.put(key, value.getInitialState()));

      Map<Participant, MessageQueue> participantToMessageQueueMap = new LinkedHashMap<Participant, MessageQueue>();
      participantToMessageQueueSizeMap
            .forEach((key, value) -> participantToMessageQueueMap.put(key, new MessageQueue(value)));

      return new State(participantToConcreteParticipantBehaviorStateMap, participantToMessageQueueMap);
   }

   /**
    *
    * For each pair of participants i and j, find all messages m, such that m is an {@link InputMessage} of i and m is
    * also an {@link OutputMessage} of j. Whether i has an asynchronous send action transition for m, from its
    * {@link com.sesygroup.choreography.concreteparticipantbehavior.model.State} contained in
    * {@code <i>sourceState</i>}, and, the message m can be stored to the queue of j, therefore, a new target
    * {@link State} and an {@link AsynchSendActTransition} from sourceState to targetState must be created.
    *
    * @param participantToConcreteParticipantBehaviorMap the participant to concrete participant behavior map on which
    * asynchronous send action transitions are searched, must not be null
    * @param sourceState the {@link State} from which asynchronous send action transition are searched, must not be null
    * @return a collection of asynchronous send action transitions; an empty collection otherwise
    */
   public static Collection<AsynchSendActTransition> findAsynchSendActTransitions(
         final Map<Participant, ConcreteParticipantBehavior> participantToConcreteParticipantBehaviorMap,
         final State sourceState) {

      Collection<AsynchSendActTransition> asynchSendActTransitions = new LinkedHashSet<AsynchSendActTransition>();

      List<Participant> participants
            = new LinkedList<Participant>(participantToConcreteParticipantBehaviorMap.keySet());

      for (int i = 0; i < participants.size(); i++) {
         for (int j = 0; j < participants.size(); j++) {
            if (i != j) {
               /*
                * find messages name that are in the intersection of input messages of the participant i and are output
                * messages of the participant j
                */
               Collection<String> messageNames = findIntersectionMessageNameSentAndRecevideByParticipants(
                     participantToConcreteParticipantBehaviorMap.get(participants.get(i)).getMessages(),
                     participantToConcreteParticipantBehaviorMap.get(participants.get(j)).getMessages());

               for (String messageName : messageNames) {
                  /*
                   * find all asynchronous send action transition where the participant i send the output message from
                   * its state contained in sourceState
                   */
                  Collection<Transition> asynchronousSendActionTransitions
                        = findConcreteAsynchronousSendActionTransition(
                              CollectionUtils.select(
                                    participantToConcreteParticipantBehaviorMap.get(participants.get(i))
                                          .getTransitions(),
                                    PredicateUtils.instanceofPredicate(AsynchronousSendActionTransition.class)),
                              sourceState.getParticipantToConcreteParticipantBehaviorStateMap()
                                    .get(participants.get(i)),
                              new OutputMessage(messageName));

                  for (Transition transition : asynchronousSendActionTransitions) {
                     State targetState = State.newInstance(sourceState);

                     /*
                      * check whether the participant j can receives the message (i.e., whether the message can be put
                      * in the queue of the participant j)
                      */
                     if (targetState.getParticipantToMessageQueueMap().get(participants.get(j)).getQueue() != null
                           && targetState.getParticipantToMessageQueueMap().get(participants.get(j)).getQueue()
                                 .size() < targetState.getParticipantToMessageQueueMap().get(participants.get(j))
                                       .getCapacity()) {
                        /*
                         * put the message in the queue of the participant j
                         */
                        targetState.getParticipantToMessageQueueMap().get(participants.get(j)).getQueue()
                              .offer(((AsynchronousSendActionTransition) transition).getOutputMessage());

                        /*
                         * change the state of the participant i with the target state of it transition
                         */
                        targetState.getParticipantToConcreteParticipantBehaviorStateMap().put(participants.get(i),
                              transition.getTargetState());

                        /*
                         * create the AsynchSendActTransition
                         */
                        AsynchSendActTransition asynchSendActTransition = new AsynchSendActTransition(sourceState,
                              targetState, participants.get(i), participants.get(j),
                              ((AsynchronousSendActionTransition) transition).getOutputMessage());
                        asynchSendActTransitions.add(asynchSendActTransition);
                     }
                  }
               }
            }
         }
      }
      return asynchSendActTransitions;
   }

   /**
    *
    * For each participant j, find all messages m, such that m is an {@link InputMessage} of j. Whether j has an
    * asynchronous receive action transition for m, from its
    * {@link com.sesygroup.choreography.concreteparticipantbehavior.model.State} contained in
    * {@code <i>sourceState</i>}, and, the queue of j contains the message m in the head, therefore, a new target
    * {@link State} and an {@link AsynchReceiveActAndMsgConsumptionTransition} from sourceState to targetState must be
    * created.
    *
    * @param participantToConcreteParticipantBehaviorMap the participant to concrete participant behavior map on which
    * asynchronous receive action and message consumption transitions are searched, must not be null
    * @param sourceState the {@link State} from which asynchronous receive action and message consumption transition are
    * searched, must not be null
    * @return a collection of asynchronous receive action and message consumption transitions; an empty collection
    * otherwise
    */
   public static Collection<AsynchReceiveActAndMsgConsumptionTransition> findAsynchReceiveActAndMsgConsumptionTransitions(
         final Map<Participant, ConcreteParticipantBehavior> participantToConcreteParticipantBehaviorMap,
         final State sourceState) {

      Collection<AsynchReceiveActAndMsgConsumptionTransition> asynchReceiveActAndMsgConsumptionTransitions
            = new LinkedHashSet<AsynchReceiveActAndMsgConsumptionTransition>();

      for (Map.Entry<Participant, ConcreteParticipantBehavior> entry : participantToConcreteParticipantBehaviorMap
            .entrySet()) {

         /*
          * get all input messages of the participant
          */
         Collection<Message> inputMessages = CollectionUtils.select(entry.getValue().getMessages(),
               PredicateUtils.instanceofPredicate(InputMessage.class));

         for (Message inputMessage : inputMessages) {
            /*
             * find all asynchronous receive action transition where the participant j receive the input message from
             * its state contained in sourceState
             */
            Collection<Transition> asynchronousReceiveActionTransitions
                  = findConcreteAsynchronousReceiveActionTransition(
                        CollectionUtils.select(entry.getValue().getTransitions(),
                              PredicateUtils.instanceofPredicate(AsynchronousReceiveActionTransition.class)),
                        sourceState.getParticipantToConcreteParticipantBehaviorStateMap().get(entry.getKey()),
                        (InputMessage) inputMessage);

            for (Transition transition : asynchronousReceiveActionTransitions) {
               State targetState = State.newInstance(sourceState);
               /*
                * check whether the participant j can consume the message (i.e., whether the message is in the queue
                * head of the participant j)
                */
               if (targetState.getParticipantToMessageQueueMap().get(entry.getKey()).getQueue() != null
                     && targetState.getParticipantToMessageQueueMap().get(entry.getKey()).getQueue().peek() != null
                     && targetState.getParticipantToMessageQueueMap().get(entry.getKey()).getQueue().peek().getName()
                           .equals(inputMessage.getName())) {
                  /*
                   * consume the message from the queue head of the participant j
                   */
                  targetState.getParticipantToMessageQueueMap().get(entry.getKey()).getQueue().poll();

                  /*
                   * change the state of the participant j with the target state of it transition
                   */
                  targetState.getParticipantToConcreteParticipantBehaviorStateMap().put(entry.getKey(),
                        transition.getTargetState());
                  /*
                   * create the AsynchReceiveActAndMsgConsumptionTransition
                   */
                  AsynchReceiveActAndMsgConsumptionTransition asynchReceiveActAndMsgConsumptionTransition
                        = new AsynchReceiveActAndMsgConsumptionTransition(sourceState, targetState,
                              ((AsynchronousReceiveActionTransition) transition).getInputMessage());
                  asynchReceiveActAndMsgConsumptionTransitions.add(asynchReceiveActAndMsgConsumptionTransition);

               }
            }

         }

      }

      return asynchReceiveActAndMsgConsumptionTransitions;
   }

   /**
    *
    * For each pair of participants i and j, find all messages m, such that m is an {@link InputMessage} of i and m is
    * also an {@link OutputMessage} of j. Whether i has an synchronous send action transition for m from its
    * {@link com.sesygroup.choreography.concreteparticipantbehavior.model.State} contained in
    * {@code <i>sourceState</i>}, and, whether j has an synchronous receive action transition for m from its
    * {@link com.sesygroup.choreography.concreteparticipantbehavior.model.State} contained in
    * {@code <i>sourceState</i>}, therefore, a new target {@link State} and an
    * {@link SynchSendReceiveActAndMsgConsumptionTransition} from sourceState to targetState must be created.
    *
    * @param participantToConcreteParticipantBehaviorMap the participant to concrete participant behavior map on which
    * synchronous send-receive action and message consumption transitions are searched, must not be null
    * @param sourceState the {@link State} from which synchronous send-receive action and message consumption transition
    * are searched, must not be null
    * @return a collection of synchronous send-receive action and message consumption transitions; an empty collection
    * otherwise
    */
   public static Collection<SynchSendReceiveActAndMsgConsumptionTransition> findSynchSendReceiveActAndMsgConsumptionTransitions(
         final Map<Participant, ConcreteParticipantBehavior> participantToConcreteParticipantBehaviorMap,
         final State sourceState) {

      Collection<SynchSendReceiveActAndMsgConsumptionTransition> synchSendReceiveActAndMsgConsumptionTransitions
            = new LinkedHashSet<SynchSendReceiveActAndMsgConsumptionTransition>();

      List<Participant> participants
            = new LinkedList<Participant>(participantToConcreteParticipantBehaviorMap.keySet());

      for (int i = 0; i < participants.size(); i++) {
         for (int j = 0; j < participants.size(); j++) {
            if (i != j) {
               /*
                * find messages name that are in the intersection of input messages of the participant i and are output
                * messages of the participant j
                */
               Collection<String> messageNames = findIntersectionMessageNameSentAndRecevideByParticipants(
                     participantToConcreteParticipantBehaviorMap.get(participants.get(i)).getMessages(),
                     participantToConcreteParticipantBehaviorMap.get(participants.get(j)).getMessages());

               for (String messageName : messageNames) {

                  Collection<Transition> synchronousSendActionTransitions
                        = findConcreteSynchronousSendActionTransition(
                              CollectionUtils.select(
                                    participantToConcreteParticipantBehaviorMap.get(participants.get(i))
                                          .getTransitions(),
                                    PredicateUtils.instanceofPredicate(SynchronousSendActionTransition.class)),
                              sourceState.getParticipantToConcreteParticipantBehaviorStateMap()
                                    .get(participants.get(i)),
                              new OutputMessage(messageName));

                  Collection<Transition> synchronousReceiveActionTransitions
                        = findConcreteSynchronousReceiveActionTransition(
                              CollectionUtils.select(
                                    participantToConcreteParticipantBehaviorMap.get(participants.get(j))
                                          .getTransitions(),
                                    PredicateUtils.instanceofPredicate(SynchronousReceiveActionTransition.class)),
                              sourceState.getParticipantToConcreteParticipantBehaviorStateMap()
                                    .get(participants.get(j)),
                              new InputMessage(messageName));

                  /*
                   * TODO check whether is necessary to perform this check!
                   */
                  Validation.validateFoundedSynchronousSendReceiveActionTransition(synchronousSendActionTransitions,
                        synchronousReceiveActionTransitions);

                  if (!synchronousSendActionTransitions.isEmpty() && !synchronousReceiveActionTransitions.isEmpty()) {
                     /*
                      * whether the if statement is true the check before ensure that we have only one
                      * synchronousSendActionTransitions and only one synchronousReceiveActionTransitions
                      */

                     State targetState = State.newInstance(sourceState);
                     /*
                      * change the state of the participant i with the target state of it transition
                      */
                     targetState.getParticipantToConcreteParticipantBehaviorStateMap().put(participants.get(i),
                           synchronousSendActionTransitions.iterator().next().getTargetState());

                     /*
                      * change the state of the participant j with the target state of it transition
                      */
                     targetState.getParticipantToConcreteParticipantBehaviorStateMap().put(participants.get(j),
                           synchronousReceiveActionTransitions.iterator().next().getTargetState());

                     /*
                      * create the SynchSendReceiveActAndMsgConsumptionTransition
                      */
                     SynchSendReceiveActAndMsgConsumptionTransition synchSendReceiveActAndMsgConsumptionTransition
                           = new SynchSendReceiveActAndMsgConsumptionTransition(sourceState, targetState,
                                 participants.get(i), participants.get(j), messageName);
                     synchSendReceiveActAndMsgConsumptionTransitions
                           .add(synchSendReceiveActAndMsgConsumptionTransition);
                  }
               }
            }
         }
      }
      return synchSendReceiveActAndMsgConsumptionTransitions;
   }

   /**
    *
    * For each participant, find all internal action transition from its
    * {@link com.sesygroup.choreography.concreteparticipantbehavior.model.State} contained in
    * {@code <i>sourceState</i>}, therefore, a new target {@link State} and an {@link InternalActionTransition} from
    * sourceState to targetState must be created.
    *
    * @param participantToConcreteParticipantBehaviorMap the participant to concrete participant behavior map on which
    * on which internal action transitions are searched, must not be null
    * @param sourceState the {@link State} from which internal action transition are searched, must not be null
    * @return a collection of internal action transitions; an empty collection otherwise
    */
   public static Collection<InternalActionTransition> findInternalActionTransitions(
         final Map<Participant, ConcreteParticipantBehavior> participantToConcreteParticipantBehaviorMap,
         final State sourceState) {

      Collection<InternalActionTransition> internalActionTransitions = new LinkedHashSet<InternalActionTransition>();

      for (Map.Entry<Participant, ConcreteParticipantBehavior> entry : participantToConcreteParticipantBehaviorMap
            .entrySet()) {

         /*
          * find all internal action transition from the state contained in sourceState
          */
         Collection<Transition> internalTransitions = findConcreteInternalActionTransition(
               CollectionUtils.select(entry.getValue().getTransitions(),
                     PredicateUtils.instanceofPredicate(
                           com.sesygroup.choreography.concreteparticipantbehavior.model.action.InternalActionTransition.class)),
               sourceState.getParticipantToConcreteParticipantBehaviorStateMap().get(entry.getKey()));

         for (Transition transition : internalTransitions) {
            State targetState = State.newInstance(sourceState);

            /*
             * change the state of the participant with the target state of it transition
             */
            targetState.getParticipantToConcreteParticipantBehaviorStateMap().put(entry.getKey(),
                  transition.getTargetState());
            /*
             * create the InternalActionTransition
             */
            InternalActionTransition internalActionTransition = new InternalActionTransition(sourceState, targetState);
            internalActionTransitions.add(internalActionTransition);
         }
      }

      return internalActionTransitions;
   }

   /**
    * Returns a {@link Collection} containing the intersection between {@code InputMessage}s of
    * {@code <i>participantMessages</i>} and {@code OutputMessage}s of {@code <i>otherParticipantMessages</i>}, by
    * considering only the name of the message.
    *
    * @param participantMessages the first collection, must not be null
    * @param otherParticipantMessages the second collection, must not be null
    * @return the intersection of the two collections; an empty collection otherwise
    */
   protected static Collection<String> findIntersectionMessageNameSentAndRecevideByParticipants(
         final Set<Message> participantMessages, final Set<Message> otherParticipantMessages) {

      return CollectionUtils.intersection(
            CollectionUtils.select(participantMessages, PredicateUtils.instanceofPredicate(OutputMessage.class))
                  .stream().map(Message::getName).collect(Collectors.toSet()),
            CollectionUtils.select(otherParticipantMessages, PredicateUtils.instanceofPredicate(InputMessage.class))
                  .stream().map(Message::getName).collect(Collectors.toSet()));
   }

   /**
    * Find asynchronous send action transitions in {@code <i>transitions</i>} where the source state of the transition
    * is {@code <i>sourceState</i>} and the sent message is {@code <i>outputMessage</i>}.
    *
    * @param transitions the collection on which the search is applied, must not be null
    * @param sourceState the state from which asynchronous send action transition will be searched, must not be null
    * @param outputMessage the message that must be sent, must not be null
    * @return a collection contained all asynchronous send action transitions founded; an empty collection otherwise
    * @see AsynchronousSendActionTransition
    */
   protected static Collection<Transition> findConcreteAsynchronousSendActionTransition(
         final Collection<Transition> transitions,
         final com.sesygroup.choreography.concreteparticipantbehavior.model.State sourceState,
         final OutputMessage outputMessage) {

      return CollectionUtils.select(transitions, new Predicate<Transition>() {
         @Override
         public boolean evaluate(final Transition object) {
            return object.getSourceState().equals(sourceState)
                  && AsynchronousSendActionTransition.class.isInstance(object)
                  && ((AsynchronousSendActionTransition) object).getOutputMessage().equals(outputMessage);
         }
      });
   }

   /**
    * Find asynchronous receive action transitions in {@code <i>transitions</i>} where the source state of the
    * transition is {@code <i>sourceState</i>} and the received message is {@code <i>inputMessage</i>}.
    *
    * @param transitions the collection on which the search is applied, must not be null
    * @param sourceState the state from which asynchronous receive action transitions will be searched, must not be null
    * @param inputMessage the message that must be received, must not be null
    * @return a collection contained all asynchronous receive action transitions founded; an empty collection otherwise
    * @see AsynchronousReceiveActionTransition
    */
   protected static Collection<Transition> findConcreteAsynchronousReceiveActionTransition(
         final Collection<Transition> transitions,
         final com.sesygroup.choreography.concreteparticipantbehavior.model.State sourceState,
         final InputMessage inputMessage) {

      return CollectionUtils.select(transitions, new Predicate<Transition>() {
         @Override
         public boolean evaluate(final Transition object) {
            return object.getSourceState().equals(sourceState)
                  && AsynchronousReceiveActionTransition.class.isInstance(object)
                  && ((AsynchronousReceiveActionTransition) object).getInputMessage().equals(inputMessage);
         }
      });
   }

   /**
    * Find ssynchronous send action transitions in {@code <i>transitions</i>} where the source state of the transition
    * is {@code <i>sourceState</i>} and the sent message is {@code <i>outputMessage</i>}.
    *
    * @param transitions the collection on which the search is applied, must not be null
    * @param sourceState the state from which synchronous send action transition will be searched, must not be null
    * @param outputMessage the message that must be sent, must not be null
    * @return a collection contained all synchronous send action transitions founded; an empty collection otherwise
    * @see SynchronousSendActionTransition
    */
   protected static Collection<Transition> findConcreteSynchronousSendActionTransition(
         final Collection<Transition> transitions,
         final com.sesygroup.choreography.concreteparticipantbehavior.model.State sourceState,
         final OutputMessage outputMessage) {

      return CollectionUtils.select(transitions, new Predicate<Transition>() {
         @Override
         public boolean evaluate(final Transition object) {
            return object.getSourceState().equals(sourceState)
                  && SynchronousSendActionTransition.class.isInstance(object)
                  && ((SynchronousSendActionTransition) object).getOutputMessage().equals(outputMessage);
         }
      });
   }

   /**
    * Find synchronous receive action transitions in {@code <i>transitions</i>} where the source state of the transition
    * is {@code <i>sourceState</i>} and the received message is {@code <i>inputMessage</i>}.
    *
    * @param transitions the collection on which the search is applied, must not be null
    * @param sourceState the state from which synchronous receive action transitions will be searched, must not be null
    * @param inputMessage the message that must be received, must not be null
    * @return a collection contained all synchronous receive action transitions founded; an empty collection otherwise
    * @see SynchronousReceiveActionTransition
    */
   protected static Collection<Transition> findConcreteSynchronousReceiveActionTransition(
         final Collection<Transition> transitions,
         final com.sesygroup.choreography.concreteparticipantbehavior.model.State sourceState,
         final InputMessage inputMessage) {

      return CollectionUtils.select(transitions, new Predicate<Transition>() {
         @Override
         public boolean evaluate(final Transition object) {
            return object.getSourceState().equals(sourceState)
                  && SynchronousReceiveActionTransition.class.isInstance(object)
                  && ((SynchronousReceiveActionTransition) object).getInputMessage().equals(inputMessage);
         }
      });
   }

   /**
    * Find internal action transitions in {@code <i>transitions</i>} where the source state of the transition is
    * {@code <i>sourceState</i>}.
    *
    * @param transitions the collection on which the search is applied, must not be null
    * @param sourceState the state from which internal action transitions will be searched, must not be null
    * @return a collection contained all internal action transitions founded; an empty collection otherwise
    * @see com.sesygroup.choreography.concreteparticipantbehavior.model.action.InternalActionTransition
    */
   protected static Collection<Transition> findConcreteInternalActionTransition(
         final Collection<Transition> transitions,
         final com.sesygroup.choreography.concreteparticipantbehavior.model.State sourceState) {

      return CollectionUtils.select(transitions, new Predicate<Transition>() {
         @Override
         public boolean evaluate(final Transition object) {
            return object.getSourceState().equals(sourceState)
                  && com.sesygroup.choreography.concreteparticipantbehavior.model.action.InternalActionTransition.class
                        .isInstance(object);
         }
      });
   }

   // -----------------------------------------------------------------------

   /**
    * <p>
    * {@code HybridSystemBehaviorGeneratorUtils} instances should NOT be constructed in standard programming. Instead,
    * the class should be used statically.
    * </p>
    *
    * <p>
    * This constructor is public to permit tools that require a JavaBean instance to operate.
    * </p>
    */
   public HybridSystemBehaviorGeneratorUtils() {
      super();
   }
}
