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

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Alexander Perucci (http://www.alexanderperucci.com/)
 *
 */
public class State implements Serializable {
   private static final long serialVersionUID = -2971031901120572413L;
   private Map<Participant, com.sesygroup.choreography.concreteparticipantbehavior.model.State> participantToConcreteParticipantBehaviorStateMap;
   private Map<Participant, MessageQueue> participantToMessageQueueMap;

   public static State newInstance(final State aState) {
      Map<Participant, com.sesygroup.choreography.concreteparticipantbehavior.model.State> newParticipantToConcreteParticipantBehaviorStateMap
            = new LinkedHashMap<Participant, com.sesygroup.choreography.concreteparticipantbehavior.model.State>();
      Map<Participant, MessageQueue> newParticipantToMessageQueueMap = new LinkedHashMap<Participant, MessageQueue>();

      aState.getParticipantToConcreteParticipantBehaviorStateMap()
            .forEach((key, value) -> newParticipantToConcreteParticipantBehaviorStateMap.put(key, value));
      aState.getParticipantToMessageQueueMap().forEach((key, value) -> {
         MessageQueue newMessageQueue = new MessageQueue(value.getCapacity());
         /*
          * required check because if the capacity is equal to zero the getQueue() return null; see the constructor of
          * MessageQueue
          */
         if (value.getCapacity() != 0) {
            newMessageQueue.getQueue().addAll(value.getQueue());
         }
         newParticipantToMessageQueueMap.put(key, newMessageQueue);
      });

      return new State(newParticipantToConcreteParticipantBehaviorStateMap, newParticipantToMessageQueueMap);

   }

   public State() {
      super();
      participantToConcreteParticipantBehaviorStateMap
            = new LinkedHashMap<Participant, com.sesygroup.choreography.concreteparticipantbehavior.model.State>();
      participantToMessageQueueMap = new LinkedHashMap<Participant, MessageQueue>();
   }

   public State(
         final Map<Participant, com.sesygroup.choreography.concreteparticipantbehavior.model.State> participantToConcreteParticipantBehaviorStateMap,
         final Map<Participant, MessageQueue> participantToMessageQueueMap) {
      super();
      this.participantToConcreteParticipantBehaviorStateMap = participantToConcreteParticipantBehaviorStateMap;
      this.participantToMessageQueueMap = participantToMessageQueueMap;
   }

   public Map<Participant, com.sesygroup.choreography.concreteparticipantbehavior.model.State> getParticipantToConcreteParticipantBehaviorStateMap() {
      return participantToConcreteParticipantBehaviorStateMap;
   }

   public void setParticipantToConcreteParticipantBehaviorStateMap(
         final Map<Participant, com.sesygroup.choreography.concreteparticipantbehavior.model.State> participantToConcreteParticipantBehaviorStateMap) {
      this.participantToConcreteParticipantBehaviorStateMap = participantToConcreteParticipantBehaviorStateMap;
   }

   public Map<Participant, MessageQueue> getParticipantToMessageQueueMap() {
      return participantToMessageQueueMap;
   }

   public void setParticipantToMessageQueueMap(final Map<Participant, MessageQueue> participantToMessageQueueMap) {
      this.participantToMessageQueueMap = participantToMessageQueueMap;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((participantToMessageQueueMap == null)
            ? 0
            : participantToMessageQueueMap.hashCode());
      result = prime * result + ((participantToConcreteParticipantBehaviorStateMap == null)
            ? 0
            : participantToConcreteParticipantBehaviorStateMap.hashCode());
      return result;
   }

   @Override
   public boolean equals(final Object obj) {
      if (this == obj) {
         return true;
      }
      if (obj == null) {
         return false;
      }
      if (getClass() != obj.getClass()) {
         return false;
      }
      State other = (State) obj;

      if (participantToMessageQueueMap == null) {
         if (other.participantToMessageQueueMap != null) {
            return false;
         }
      } else if (!participantToMessageQueueMap.equals(other.participantToMessageQueueMap)) {
         return false;
      }
      if (participantToConcreteParticipantBehaviorStateMap == null) {
         if (other.participantToConcreteParticipantBehaviorStateMap != null) {
            return false;
         }
      } else if (!participantToConcreteParticipantBehaviorStateMap
            .equals(other.participantToConcreteParticipantBehaviorStateMap)) {
         return false;
      }
      return true;
   }

   @Override
   public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("(");

      for (Map.Entry<Participant, com.sesygroup.choreography.concreteparticipantbehavior.model.State> entry : participantToConcreteParticipantBehaviorStateMap
            .entrySet()) {
         stringBuilder.append(
               entry.getKey() + ":" + entry.getValue() + ":" + participantToMessageQueueMap.get(entry.getKey()) + ",");
      }

      // remove last separator char if needed
      try {
         stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(","));
      } catch (StringIndexOutOfBoundsException e) {
         /*
          * do not nothing because the lastIndexOf method can return -1 if the string not contains the separator char so
          * the deleteCharAt method throws an StringIndexOutOfBoundsException
          */

      }
      stringBuilder.append(")");
      return stringBuilder.toString();
   }

}
