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

/**
 *
 * @author Alexander Perucci (http://www.alexanderperucci.com/)
 *
 */
public abstract class Transition {
   protected State sourceState;
   protected State targetState;

   public Transition() {
      super();
      sourceState = null;
      targetState = null;
   }

   public Transition(final State sourceState, final State targetState) {
      super();
      this.sourceState = sourceState;
      this.targetState = targetState;
   }

   public State getSourceState() {
      return sourceState;
   }

   public void setSourceState(final State sourceState) {
      this.sourceState = sourceState;
   }

   public State getTargetState() {
      return targetState;
   }

   public void setTargetState(final State targetState) {
      this.targetState = targetState;
   }

   @Override
   public abstract int hashCode();

   @Override
   public abstract boolean equals(final Object obj);

   @Override
   public abstract String toString();

}
