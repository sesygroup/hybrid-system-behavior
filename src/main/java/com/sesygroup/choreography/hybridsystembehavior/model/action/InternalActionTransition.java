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

import com.sesygroup.choreography.hybridsystembehavior.model.State;
import com.sesygroup.choreography.hybridsystembehavior.model.Transition;

/**
 *
 * @author Alexander Perucci (http://www.alexanderperucci.com/)
 *
 */
public class InternalActionTransition extends Transition implements Serializable {
   private static final long serialVersionUID = 7743794783099042149L;

   public InternalActionTransition() {
      super();
   }

   public InternalActionTransition(final State sourceState, final State targetState) {
      super(sourceState, targetState);
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((super.sourceState == null)
            ? 0
            : super.sourceState.hashCode());
      result = prime * result + ((super.targetState == null)
            ? 0
            : super.targetState.hashCode());
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
      InternalActionTransition other = (InternalActionTransition) obj;
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
      return true;
   }

   @Override
   public String toString() {
      return "(" + sourceState + ", " + "epsilon" + ", " + targetState + ")";
   }
}
