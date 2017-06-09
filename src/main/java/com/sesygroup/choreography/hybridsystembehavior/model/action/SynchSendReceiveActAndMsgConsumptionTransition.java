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

import java.io.Serializable;

import com.sesygroup.choreography.hybridsystembehavior.model.Participant;
import com.sesygroup.choreography.hybridsystembehavior.model.State;
import com.sesygroup.choreography.hybridsystembehavior.model.Transition;

/**
 *
 * @author Alexander Perucci (http://www.alexanderperucci.com/)
 *
 */
public class SynchSendReceiveActAndMsgConsumptionTransition extends Transition implements Serializable {
   private static final long serialVersionUID = -745610185791637102L;
   private String messageName;
   private Participant sourceParticipant;
   private Participant targetParticipant;

   public SynchSendReceiveActAndMsgConsumptionTransition() {
      super();
      messageName = null;
      sourceParticipant = null;
      targetParticipant = null;
   }

   public SynchSendReceiveActAndMsgConsumptionTransition(final State sourceState, final State targetState,
         final Participant sourceParticipant, final Participant targetParticipant, final String messageName) {
      super(sourceState, targetState);
      this.messageName = messageName;
      this.sourceParticipant = sourceParticipant;
      this.targetParticipant = targetParticipant;
   }

   public String getMessageName() {
      return messageName;
   }

   public void setOutputMessage(final String messageName) {
      this.messageName = messageName;
   }

   public Participant getSourceParticipant() {
      return sourceParticipant;
   }

   public void setSourceParticipant(final Participant sourceParticipant) {
      this.sourceParticipant = sourceParticipant;
   }

   public Participant getTargetParticipant() {
      return targetParticipant;
   }

   public void setTargetParticipant(final Participant targetParticipant) {
      this.targetParticipant = targetParticipant;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((messageName == null)
            ? 0
            : messageName.hashCode());
      result = prime * result + ((super.sourceState == null)
            ? 0
            : super.sourceState.hashCode());
      result = prime * result + ((super.targetState == null)
            ? 0
            : super.targetState.hashCode());
      result = prime * result + ((sourceParticipant == null)
            ? 0
            : sourceParticipant.hashCode());
      result = prime * result + ((targetParticipant == null)
            ? 0
            : targetParticipant.hashCode());
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
      SynchSendReceiveActAndMsgConsumptionTransition other = (SynchSendReceiveActAndMsgConsumptionTransition) obj;
      if (messageName == null) {
         if (other.messageName != null) {
            return false;
         }
      } else if (!messageName.equals(other.messageName)) {
         return false;
      }
      if (super.sourceState == null) {
         if (other.sourceState != null) {
            return false;
         }
      } else if (!super.sourceState.equals(other.sourceState)) {
         return false;
      }
      if (super.targetState == null) {
         if (other.targetState != null) {
            return false;
         }
      } else if (!super.targetState.equals(other.targetState)) {
         return false;
      }
      if (sourceParticipant == null) {
         if (other.sourceParticipant != null) {
            return false;
         }
      } else if (!sourceParticipant.equals(other.sourceParticipant)) {
         return false;
      }
      if (targetParticipant == null) {
         if (other.targetParticipant != null) {
            return false;
         }
      } else if (!targetParticipant.equals(other.targetParticipant)) {
         return false;
      }
      return true;
   }

   @Override
   public String toString() {
      return "(" + sourceParticipant + ":" + sourceState + ", " + messageName + ", " + targetParticipant + ":"
            + targetState + ", synchronous)";
   }
}
