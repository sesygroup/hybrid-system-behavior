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

import com.sesygroup.choreography.concreteparticipantbehavior.model.Message;
import com.sesygroup.choreography.concreteparticipantbehavior.model.message.OutputMessage;
import com.sesygroup.choreography.hybridsystembehavior.model.Participant;
import com.sesygroup.choreography.hybridsystembehavior.model.State;
import com.sesygroup.choreography.hybridsystembehavior.model.Transition;

/**
 *
 * @author Alexander Perucci (http://www.alexanderperucci.com/)
 *
 */
public class AsynchSendActTransition extends Transition implements Serializable {
   private static final long serialVersionUID = 377224997576825993L;
   private OutputMessage outputMessage;
   private Participant sourceParticipant;
   private Participant targetParticipant;

   public AsynchSendActTransition() {
      super();
      outputMessage = null;
      sourceParticipant = null;
      targetParticipant = null;
   }

   public AsynchSendActTransition(final State sourceState, final State targetState, final Participant sourceParticipant,
         final Participant targetParticipant, final OutputMessage outputMessage) {
      super(sourceState, targetState);
      this.outputMessage = outputMessage;
      this.sourceParticipant = sourceParticipant;
      this.targetParticipant = targetParticipant;
   }

   public Message getOutputMessage() {
      return outputMessage;
   }

   public void setMessage(final OutputMessage outputMessage) {
      this.outputMessage = outputMessage;
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
      result = prime * result + ((outputMessage == null)
            ? 0
            : outputMessage.hashCode());
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
      AsynchSendActTransition other = (AsynchSendActTransition) obj;
      if (outputMessage == null) {
         if (other.outputMessage != null) {
            return false;
         }
      } else if (!outputMessage.equals(other.outputMessage)) {
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
      return "(" + sourceParticipant + ":" + sourceState + ", " + outputMessage.getName() + ", " + targetParticipant
            + ":" + targetState + ", asynchronous)";
   }
}
