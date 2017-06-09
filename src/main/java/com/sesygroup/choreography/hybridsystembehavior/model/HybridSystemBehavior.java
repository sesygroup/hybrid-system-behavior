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
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.sesygroup.choreography.concreteparticipantbehavior.model.ConcreteParticipantBehavior;
import com.sesygroup.choreography.concreteparticipantbehavior.model.Message;

/**
 *
 * @author Alexander Perucci (http://www.alexanderperucci.com/)
 *
 */
public class HybridSystemBehavior implements Serializable {
   private static final long serialVersionUID = 213610236243776520L;
   private Map<Participant, ConcreteParticipantBehavior> participantToConcreteParticipantBehaviorMap;
   private Set<State> states;
   private State initialState;
   private Set<Message> messages;
   private Set<Transition> transitions;

   public HybridSystemBehavior() {
      this(new LinkedHashMap<Participant, ConcreteParticipantBehavior>());
   }

   public HybridSystemBehavior(
         final Map<Participant, ConcreteParticipantBehavior> participantToConcreteParticipantBehaviorMap) {
      this(participantToConcreteParticipantBehaviorMap, new HashSet<State>(), null, new HashSet<Message>(),
            new HashSet<Transition>());

      participantToConcreteParticipantBehaviorMap.values()
            .forEach(concreteParticipantBehavior -> messages.addAll(concreteParticipantBehavior.getMessages()));
   }

   public HybridSystemBehavior(
         final Map<Participant, ConcreteParticipantBehavior> participantToConcreteParticipantBehaviorMap,
         final Set<State> states, final State initialState, final Set<Message> messages,
         final Set<Transition> transitions) {
      super();
      this.participantToConcreteParticipantBehaviorMap = participantToConcreteParticipantBehaviorMap;
      this.states = states;
      this.initialState = initialState;
      this.messages = messages;
      this.transitions = transitions;
   }

   public Map<Participant, ConcreteParticipantBehavior> getParticipantToConcreteParticipantBehaviorMap() {
      return participantToConcreteParticipantBehaviorMap;
   }

   public void setParticipantToConcreteParticipantBehaviorMap(
         final Map<Participant, ConcreteParticipantBehavior> participantToConcreteParticipantBehaviorMap) {
      this.participantToConcreteParticipantBehaviorMap = participantToConcreteParticipantBehaviorMap;
   }

   public Set<State> getStates() {
      return states;
   }

   public void setStates(final Set<State> states) {
      this.states = states;
   }

   public State getInitialState() {
      return initialState;
   }

   public void setInitialState(final State initialState) {
      this.initialState = initialState;
   }

   public Set<Message> getMessages() {
      return messages;
   }

   public void setMessages(final Set<Message> messages) {
      this.messages = messages;
   }

   public Set<Transition> getTransitions() {
      return transitions;
   }

   public void setTransitions(final Set<Transition> transitions) {
      this.transitions = transitions;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((initialState == null)
            ? 0
            : initialState.hashCode());
      result = prime * result + ((messages == null)
            ? 0
            : messages.hashCode());
      result = prime * result + ((participantToConcreteParticipantBehaviorMap == null)
            ? 0
            : participantToConcreteParticipantBehaviorMap.hashCode());
      result = prime * result + ((states == null)
            ? 0
            : states.hashCode());
      result = prime * result + ((transitions == null)
            ? 0
            : transitions.hashCode());
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
      HybridSystemBehavior other = (HybridSystemBehavior) obj;
      if (initialState == null) {
         if (other.initialState != null) {
            return false;
         }
      } else if (!initialState.equals(other.initialState)) {
         return false;
      }
      if (messages == null) {
         if (other.messages != null) {
            return false;
         }
      } else if (!messages.equals(other.messages)) {
         return false;
      }
      if (participantToConcreteParticipantBehaviorMap == null) {
         if (other.participantToConcreteParticipantBehaviorMap != null) {
            return false;
         }
      } else if (!participantToConcreteParticipantBehaviorMap
            .equals(other.participantToConcreteParticipantBehaviorMap)) {
         return false;
      }
      if (states == null) {
         if (other.states != null) {
            return false;
         }
      } else if (!states.equals(other.states)) {
         return false;
      }
      if (transitions == null) {
         if (other.transitions != null) {
            return false;
         }
      } else if (!transitions.equals(other.transitions)) {
         return false;
      }
      return true;
   }

}
